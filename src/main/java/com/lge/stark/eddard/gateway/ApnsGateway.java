package com.lge.stark.eddard.gateway;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Settings;
import com.lge.stark.eddard.model.Fault;
import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.FailedConnectionListener;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;

public class ApnsGateway {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ApnsGateway.class);

	private PushManager<SimpleApnsPushNotification> manager;

	final boolean isProductionMode;
	final String certChainFilePath;
	final String certPassword;

	public ApnsGateway() {
		isProductionMode = Settings.SELF.getBoolean("apns.isProductionMode", false);
		certChainFilePath = Settings.SELF.getProperty("apns.certChainFilePath");
		certPassword = Settings.SELF.getProperty("apns.certPassword");
	}

	public void start() throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {

		ApnsEnvironment environment = isProductionMode ? ApnsEnvironment.getProductionEnvironment()
				: ApnsEnvironment.getSandboxEnvironment();

		manager = new PushManager<SimpleApnsPushNotification>(environment,
				SSLContextUtil.createDefaultSSLContext(certChainFilePath, certPassword), null, null, null,
				new PushManagerConfiguration(), "ApnsPushManager");

		manager.registerRejectedNotificationListener(new RejectedNotificationListener<SimpleApnsPushNotification>() {

			@Override
			public void handleRejectedNotification(PushManager<? extends SimpleApnsPushNotification> pushManager,
					SimpleApnsPushNotification notification, RejectedNotificationReason rejectionReason) {

				logger.error("{} was rejected with rejection reasponse {}.", notification, rejectionReason);
			}
		});

		manager.registerFailedConnectionListener(new FailedConnectionListener<SimpleApnsPushNotification>() {

			@Override
			public void handleFailedConnection(PushManager<? extends SimpleApnsPushNotification> pushManager,
					Throwable cause) {

				logger.error(cause.getMessage(), cause);
			}
		});

		manager.start();
	}

	public void shutdown() {
		try {
			manager.shutdown();
		}
		catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void send(String deviceToken, String message) throws FaultException {
		byte[] token = null;

		try {
			token = TokenUtil.tokenStringToByteArray(deviceToken);
		}
		catch (MalformedTokenStringException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_003.replaceWith("Invalid device token"));
		}

		ApnsPayloadBuilder builder = new ApnsPayloadBuilder();

		builder.setAlertBody(message);
		builder.setSoundFileName("ring-ring.aiff");

		String payload = builder.buildWithDefaultMaximumLength();

		try {
			manager.getQueue().put(new SimpleApnsPushNotification(token, payload));
		}
		catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}
}