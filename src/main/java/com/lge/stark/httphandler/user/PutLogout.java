package com.lge.stark.httphandler.user;

import com.lge.stark.FaultException;
import com.lge.stark.gateway.UserGateway;

import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "user/{id}/logout" }, httpMethods = { "PUT" })
public class PutLogout extends HttpRequestHandler {

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