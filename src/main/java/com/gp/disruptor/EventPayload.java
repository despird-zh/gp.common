package com.gp.disruptor;

import java.util.Collection;

/**
 * The base class of event payload, it is used to extend for different purpose.
 * 
 * @author despird
 * @version 0.1 2014-3-2
 *
 **/
public interface EventPayload {

	/**
	 * Return the event type of event. it be used to find the event hooker to 
	 * digest it when published to event disruptor engine.
	 **/
	public EventType getEventType();
	
	/**
	 * Return the chain event payloads
	 **/
	public Collection<EventPayload> getChainEventPayloads();
}
