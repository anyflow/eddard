package com.lge.stark.httphandler;

import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "/", "/index", "home" }, httpMethods = {
		"GET" }, webResourcePath = "/html/index.htm")
public class Index extends HttpRequestHandler {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Index.class);

	@Override
	public String service() {
		return null;
	}
}