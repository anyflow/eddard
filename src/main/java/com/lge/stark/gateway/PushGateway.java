package com.lge.stark.gateway;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.lge.stark.FaultException;
import com.lge.stark.model.PushType;
import com.lge.stark.smp.smpframe.Smpframe;

public class PushGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PushGateway.class);

	public final static PushGateway SELF;

	static {
		SELF = new PushGateway();
	}

	private LgpsGateway lgps;
	private ApnsGateway apns;
	private GcmGateway gcm;

	private PushGateway() {
		lgps = new LgpsGateway();
		apns = new ApnsGateway();
		gcm = new GcmGateway();
	}

	public void start() throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {

		apns.start();
	}

	public void shutdown() {
		apns.shutdown();
	}

	public void send(PushType pushType, String receiverId, Smpframe smpframe) throws FaultException {
		switch (pushType) {
		case LGPS:
			lgps.send(receiverId, smpframe.toJsonString());
			break;

		case APNS:
			apns.send(receiverId, smpframe.toJsonString());
			break;

		case GCM:
			gcm.send(receiverId, smpframe.toJsonString());
			break;
		}
	}
}