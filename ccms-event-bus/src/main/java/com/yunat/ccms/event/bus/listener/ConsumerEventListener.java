package com.yunat.ccms.event.bus.listener;

import java.util.EventListener;

public interface ConsumerEventListener extends EventListener {
	public void handleEvent(ConsumerEvent event) throws Exception;
}
