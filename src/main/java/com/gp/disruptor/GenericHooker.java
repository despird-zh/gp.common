package com.gp.disruptor;

public abstract class GenericHooker<T> extends EventHooker<GenericPayload<T>>{

	public GenericHooker(EventType eventType) {
		super(eventType);
	}

}
