package com.gp.disruptor;

public class GenericPayload<T> extends EventPayload{
	
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

}
