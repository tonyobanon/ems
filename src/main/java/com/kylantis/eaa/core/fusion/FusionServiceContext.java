package com.kylantis.eaa.core.fusion;

import java.lang.reflect.Method;

public class FusionServiceContext {

	private final EndpointClass endpointClass;
	private final EndpointMethod endpointMethod;
	private final BaseService serviceInstance;
	private final Method method;
	private final boolean isClassEnd;
	
	public FusionServiceContext(EndpointClass endpointClass, EndpointMethod endpointMethod, BaseService serviceInstance,
			Method method, boolean isClassEnd) {
		this.endpointClass = endpointClass;
		this.endpointMethod = endpointMethod;
		this.serviceInstance = serviceInstance;
		this.method = method;
		this.isClassEnd = isClassEnd;
	}

	public EndpointClass getEndpointClass() {
		return endpointClass;
	}

	public EndpointMethod getEndpointMethod() {
		return endpointMethod;
	}

	public BaseService getServiceInstance() {
		return serviceInstance;
	}

	public Method getMethod() {
		return method;
	}

	public boolean isClassEnd() {
		return isClassEnd;
	}
	
}
