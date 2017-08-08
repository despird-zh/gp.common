package com.gp.disruptor;

public class GenericPayload<T> extends EventPayload{
	
	
	private T data = null;
	
	public GenericPayload(EventType eventType, T data){
		
		this.setEventType(eventType);
		this.data = data;
	}
	
	public T data(){
		
		return this.data;
	}
}
