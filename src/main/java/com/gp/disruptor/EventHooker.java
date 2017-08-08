package com.gp.disruptor;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.gp.exception.RingEventException;
import com.lmax.disruptor.RingBuffer;

/**
 * Basic class for certain type event handler, it holds RingBuffer to
 * provide event producer that to be used for event publishing. 
 *  
 **/
public abstract class EventHooker<T extends EventPayload> {

	/** the event type */
	private EventType eventType;
	/** the ring buffer */
	private RingBuffer<RingEvent> ringBuffer = null;
	/** the event producer */
	private EventProducer<T> producer = null;
	/**
	 * Constructor:specify the eventype supported 
	 **/
	public EventHooker(EventType eventType){
		
		this.eventType = eventType;
	}
	
	/**
	 * Get the supported EventType 
	 **/
	public EventType getEventType(){
		
		return eventType;
	}
	
	/**
	 * Set event type
	 * @param eventType 
	 **/
	public void setEventType(EventType eventType){
		
		this.eventType = eventType;
	}
	
	/**
	 * Set the RingBuffer 
	 * @param ringBuffer 
	 **/
	protected void setRingBuffer(RingBuffer<RingEvent> ringBuffer){
		
		this.ringBuffer = ringBuffer;		
	}
	
	/**
	 * Get the event producer
	 * @return EventProducer<T> 
	 **/
	public EventProducer<T> getEventProducer() throws RingEventException{
		
		if(null == ringBuffer)
			throw new RingEventException("The RingBuffer not initialized yet.");
		if(null == producer)
			this.producer = new EventProducer<T>(ringBuffer, eventType);
		
		return this.producer;
	}
	
	/**
	 * Process the payload of event.
	 * 
	 * @param  payload the payload of ring event
	 * @exception RingEventException
	 * 
	 **/
	public abstract void processPayload(EventPayload payload) throws RingEventException;

	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof EventHooker)) {
			return false;
		}
		// step 3
		EventHooker<?> that = (EventHooker<?>) other;
		// step 4
		return new EqualsBuilder()
			.append(this.eventType, that.eventType).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.eventType)
				.toHashCode();
	}

}
