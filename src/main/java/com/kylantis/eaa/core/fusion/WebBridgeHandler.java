package com.kylantis.eaa.core.fusion;

import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class WebBridgeHandler extends BridgeEventHandler {
	

	@Override
	public void onSocketCreated(BridgeEvent event) {
		
		//A new web client has joined the bus
		
	}

	@Override
	public void onPublish(BridgeEvent event) {
	
	}

	@Override
	public void onReceive(BridgeEvent event) {
		
	}

	@Override
	public void onRegister(BridgeEvent event) {
		
	}

	@Override
	public void onSend(BridgeEvent event) {
		
	}

	@Override
	public void onSocketClosed(BridgeEvent event) {
		
	}

	@Override
	public void onSocketIdle(BridgeEvent event) {
		
	}

	@Override
	public void onUnregister(BridgeEvent event) {
		
	}

	@Override
	public void onSocketPing(BridgeEvent event) {
	
	}
	
	public static void updateUI(){
		
	}

}
