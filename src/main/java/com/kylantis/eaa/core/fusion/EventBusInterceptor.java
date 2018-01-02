package com.kylantis.eaa.core.fusion;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.SendContext;

public class EventBusInterceptor implements Handler<SendContext<?>> {

	@Override
	public void handle(SendContext<?> event) {
		// This verifies that the message destination is one that's authorized
		// Either call event.next or message.fail(..) depending on
		// authentication outccome
	}

}
