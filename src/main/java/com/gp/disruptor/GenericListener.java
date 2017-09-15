package com.gp.disruptor;

public abstract class GenericListener<T> extends EventListener<GenericPayload<T>>{

	public GenericListener(EventType eventType) {
		super(eventType);
	}

}
