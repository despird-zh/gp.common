package com.gp.disruptor;

import java.util.Collection;

public class GenericPayload<T> implements EventPayload{
	
	private EventType eventType;
	
	private T data = null;
	
	public GenericPayload(EventType eventType, T data){
		
		this.eventType = eventType;
		this.data = data;
	}
	
	public T data(){
		
		return this.data;
	}

	@Override
	public EventType getEventType() {
		
		return eventType;
	}

	@Override
	public Collection<EventPayload> getChainEventPayloads() {
		
		return null;
	}
}
