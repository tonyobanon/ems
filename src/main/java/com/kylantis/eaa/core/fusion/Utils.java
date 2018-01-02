package com.kylantis.eaa.core.fusion;

import javax.servlet.http.HttpServletResponse;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;

public class Utils {
	
	public static String toResponse(int status) {
		
		HttpResponseStatus rStatus = HttpResponseStatus.valueOf(status);
		
		return new JsonObject()
				.put("code", rStatus.code())
				.put("class", rStatus.codeClass().name())
				.put("status", rStatus.reasonPhrase()).encode();
	}
	
	public static String toResponse(Throwable T) {
		
		HttpResponseStatus rStatus = HttpResponseStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		return new JsonObject()
				.put("code", rStatus.code())
				.put("class", rStatus.codeClass().name())
				.put("message", T.getMessage()).encode();
	}
	
	public static String toResponse(int status, String message) {
		
		HttpResponseStatus rStatus = HttpResponseStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		return new JsonObject()
				.put("code", rStatus.code())
				.put("class", rStatus.codeClass().name())
				.put("status", rStatus.reasonPhrase())
				.put("message", message).encode();
	}
}
