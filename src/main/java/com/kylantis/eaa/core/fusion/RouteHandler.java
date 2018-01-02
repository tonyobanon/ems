package com.kylantis.eaa.core.fusion;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class RouteHandler {

	private Handler<RoutingContext> handler;
	private boolean isBlocking;
	
	public RouteHandler(Handler<RoutingContext> handler, boolean isBlocking) {
		this.handler = handler;
		this.isBlocking = isBlocking;
	}

	public Handler<RoutingContext> getHandler() {
		return handler;
	}

	public RouteHandler setHandler(Handler<RoutingContext> handler) {
		this.handler = handler;
		return this;
	}

	public boolean isBlocking() {
		return isBlocking;
	}

	public RouteHandler setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
		return this;
	}
	
}
