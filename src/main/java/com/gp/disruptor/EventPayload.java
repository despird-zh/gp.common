package com.gp.disruptor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The base class of event payload, it is used to extend for different purpose.
 * 
 * @author despird
 * @version 0.1 2014-3-2
 *
 **/
public abstract class EventPayload {
	
	Set<EventPayload> chainPayloads = null;
	
	/**
	 * Return the event type of event. it be used to find the event hooker to 
	 * digest it when published to event disruptor engine.
	 **/
	public abstract EventType getEventType();
	
	/**
	 * Return the chain event payloads
	 **/
	public Collection<EventPayload> getChainEventPayloads(){
		
		return chainPayloads;
	}
	
	/**
	 * Add chain event payload into the collection
	 * @param chainPayload the chain payload 
	 **/
	public void addChainEventPayload(EventPayload chainPayload) {
		
		if(chainPayloads == null) 
			chainPayloads = new HashSet<EventPayload>();
	
		chainPayloads.add(chainPayload);
	}
}
