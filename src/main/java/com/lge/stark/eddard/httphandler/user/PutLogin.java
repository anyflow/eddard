package com.lge.stark.eddard.httphandler.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.gateway.UserGateway;
import com.lge.stark.eddard.model.Fault;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "user/{id}/login" }, httpMethods = { "PUT" })
public class PutLogin extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PutLogin.class);

	@Override
	public String service() {
		String userId = httpRequest().pathParameter("id");

		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);
			String deviceId = json.getString("deviceId");

			UserGateway.SELF.login(userId, deviceId);

			return null;
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);

			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);

			return Fault.COMMON_002.toJsonString();
		}
	}
}