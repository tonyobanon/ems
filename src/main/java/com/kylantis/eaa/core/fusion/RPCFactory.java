package com.kylantis.eaa.core.fusion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ce.ems.base.core.BlockerTodo;
import com.google.common.collect.Lists;

public class RPCFactory {

	private static Boolean prependDomainVariableToUrl = true;
	
	public static void setPrependDomainVariableToUrl(Boolean prependDomainVariableToUrl) {
		RPCFactory.prependDomainVariableToUrl = prependDomainVariableToUrl;
	}
	 
	/**
	 * Generate javascript client stubs
	 * */
	@BlockerTodo("Verify that none of the parameters are javascript reserved keywords")
	public static String generateXHRClient(EndpointClass classAnnotation, Method method,
			EndpointMethod methodAnnotation, Route route) {

		Integer functionalityId = APIRoutes.routesMappings.get(route.toString());
		
		StringBuilder clientFunction = new StringBuilder();

		clientFunction.append("\n");
		clientFunction.append("\n");

		clientFunction.append(" function " + method.getName() + " (");

		List<String> headerParamList = Lists.newArrayList(methodAnnotation.headerParams());

		String[] headerParams = functionalityId >= 0 ? headerParamList.toArray(new String[headerParamList.size()])
				: methodAnnotation.headerParams();

		List<String> functionParamList = new ArrayList<>();

		for (int i = 0; i < headerParams.length; i++) {
			functionParamList.add(headerParams[i]);
		}

		for (int i = 0; i < methodAnnotation.requestParams().length; i++) {
			functionParamList.add(methodAnnotation.requestParams()[i]);
		}

		if (!methodAnnotation.enableMultipart()) {
			for (int i = 0; i < methodAnnotation.bodyParams().length; i++) {
				functionParamList.add(methodAnnotation.bodyParams()[i]);
			}
		} else {
			functionParamList.add("formData");
		}

		String[] functionParams = functionParamList.toArray(new String[functionParamList.size()]);

		for (int i = 0; i < functionParams.length; i++) {
			clientFunction.append(functionParams[i]);
			if (i < functionParams.length - 1) {
				clientFunction.append(", ");
			}
		}

		clientFunction.append(") {");
		clientFunction.append("\n");

		clientFunction.append("\t return new Promise(function(resolve, reject) {");
		clientFunction.append("\n");
		clientFunction.append("\t\t $.ajax({");
		clientFunction.append("\n");
		clientFunction.append("\t\t\t method : \"" + methodAnnotation.method().toString() + "\",");
		clientFunction.append("\n");
		clientFunction.append("\t\t\t async: " + methodAnnotation.isAsync() + ",");
		clientFunction.append("\n");
		clientFunction.append("\t\t\t processData: false,");
		clientFunction.append("\n");
		clientFunction.append(
				"\t\t\t statusCode: {302: function(jqXHR, status, error) { window.location = jqXHR.getResponseHeader(\"X-Location\");}},");

		clientFunction.append("\n");

		if (methodAnnotation.bodyParams().length > 0 && !methodAnnotation.enableMultipart()) {
			
			clientFunction.append("\t\t\t contentType : 'application/json', ");

			clientFunction.append("\n");
			
			clientFunction.append("\t\t\t data :");

			clientFunction.append(" JSON.stringify(");
 
			clientFunction.append("{");

			for (int i = 0; i < methodAnnotation.bodyParams().length; i++) {
				// clientFunction.append("\n");
				// clientFunction.append("\t\t\t\t ")
				clientFunction.append(methodAnnotation.bodyParams()[i]).append(": ")
						.append(methodAnnotation.bodyParams()[i]);
				if (i < methodAnnotation.bodyParams().length - 1) {
					clientFunction.append(", ");
				}
			}

			// clientFunction.append("\n");
			// clientFunction.append("\t\t\t");

			clientFunction.append("}");

			clientFunction.append("), ");

			clientFunction.append("\n");

		} else if (methodAnnotation.enableMultipart()) {

			clientFunction.append("\t\t\t cache : false, ");

			clientFunction.append("\n");

			clientFunction.append("\t\t\t contentType : 'multipart/form-data', ");

			clientFunction.append("\n");

			clientFunction.append("\t\t\t data : formData, ");

			clientFunction.append("\n");

		}

		if (headerParams.length > 0) {
			clientFunction.append("\t\t\t headers : {");

			for (int i = 0; i < headerParams.length; i++) {
				clientFunction.append("\n");
				clientFunction.append("\t\t\t\t ").append(headerParams[i]).append(": ").append(headerParams[i]);
				if (i < headerParams.length - 1) {
					clientFunction.append(", ");
				}
			}
			clientFunction.append("\n");
			clientFunction.append("\t\t\t }, ");
			clientFunction.append("\n");
		}

		StringBuilder requestParams = new StringBuilder();

		if (methodAnnotation.requestParams().length > 0) {
			for (int i = 0; i < methodAnnotation.requestParams().length; i++) {
				requestParams.append(i == 0 ? "?" : "&").append(methodAnnotation.requestParams()[i]).append("=")
						.append("\"").append(" + ").append(methodAnnotation.requestParams()[i]);

				if (i < methodAnnotation.requestParams().length - 1) {
					requestParams.append(" +").append(" \"");
				}
			}
		} else {
			requestParams.append("\"");
		}
  
		clientFunction.append(
				"\t\t\t url: " + (prependDomainVariableToUrl ? "FUSION_API_URL + " : "") + " \" " + APIRoutes.BASE_PATH + classAnnotation.uri() + methodAnnotation.uri() + requestParams.toString());

		clientFunction.append("\n");

		clientFunction.append("\t\t\t }).done(function(o) {");
 
		clientFunction.append("\n");

		clientFunction.append("\t\t\t\t resolve(o);");

		clientFunction.append("\n");
 
		clientFunction.append("\t\t\t })");

		clientFunction.append(".fail(function(jqXHR, status, error){");

		clientFunction.append("\n");

		clientFunction.append("\t\t\t\t if(jqXHR.getResponseHeader(\"X-Location\") === null && jqXHR.status !== 302){");

		clientFunction.append("\n");

		clientFunction.append("\t\t\t\t\t reject(jqXHR);");

		clientFunction.append("\n");

		clientFunction.append("\t\t\t\t }");

		clientFunction.append("\n");

		clientFunction.append("\t\t\t });");

		clientFunction.append("\n");

		clientFunction.append("\t\t });");

		clientFunction.append("\n");

		clientFunction.append(" }");

		clientFunction.append("\n");

		return clientFunction.toString();

	}
	
}
