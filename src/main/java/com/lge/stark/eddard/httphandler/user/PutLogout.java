package com.lge.stark.eddard.httphandler.user;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.gateway.UserGateway;

import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "user/{id}/logout" }, httpMethods = { "PUT" })
public class PutLogout extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PutLogout.class);

	@Override
	public String service() {
		String userId = httpRequest().pathParameter("id");

		try {
			UserGateway.SELF.logout(userId);

			return null;
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}