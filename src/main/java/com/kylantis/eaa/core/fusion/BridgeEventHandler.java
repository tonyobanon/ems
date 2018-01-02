package com.kylantis.eaa.core.fusion;

import io.vertx.core.Handler;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public abstract class BridgeEventHandler implements Handler<BridgeEvent> {

	public abstract void onSocketCreated(BridgeEvent event);

	public abstract void onPublish(BridgeEvent event);

	public abstract void onReceive(BridgeEvent event);

	public abstract void onRegister(BridgeEvent event);

	public abstract void onSend(BridgeEvent event);

	public abstract void onSocketClosed(BridgeEvent event);

	public abstract void onSocketIdle(BridgeEvent event);

	public abstract void onUnregister(BridgeEvent event);

	public abstract void onSocketPing(BridgeEvent event);

	@Override
	public void handle(BridgeEvent event) {

		switch (event.type()) {
		case SOCKET_CREATED:
			onSocketCreated(event);
			break;
		case PUBLISH:
			onPublish(event);
			break;
		case RECEIVE:
			onReceive(event);
			break;
		case REGISTER:
			onRegister(event);
			break;
		case SEND:
			onSend(event);
			break;
		case SOCKET_CLOSED:
			onSocketClosed(event);
			break;
		case SOCKET_IDLE:
			onSocketIdle(event);
			break;
		case UNREGISTER:
			onUnregister(event);
			break;
		case SOCKET_PING:
			onSocketPing(event);
			break;
		}
		event.complete(true);
	}

}
