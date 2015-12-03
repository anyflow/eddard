package com.lge.stark.eddard.gateway;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.lge.stark.eddard.Settings;
import com.lge.stark.eddard.model.PushType;
import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

public class PushGateway {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PushGateway.class);

	public final static PushGateway SELF;

	static {
		SELF = new PushGateway();
	}

	private PushManager<SimpleApnsPushNotification> manager;

	private PushGateway() {
		try {
			manager = new PushManager<SimpleApnsPushNotification>(ApnsEnvironment.getSandboxEnvironment(),
					SSLContextUtil.createDefaultSSLContext(Settings.SELF.getProperty("apns.certChainFilePath"),
							Settings.SELF.getProperty("apns.certPassword")),
					null, null, null, new PushManagerConfiguration(), "ApnsPushManager");
		}
		catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			logger.error(e.getMessage(), e);
		}

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

	public boolean sendMessage(PushType pushType, String receiverId, String message) {

		return true;
	}
}