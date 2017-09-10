package com.gp.disruptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.exception.RingEventException;
import com.gp.launcher.LifecycleHooker;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * EventDisptcher is a singleton pattern object. It holds the necessary objects
 * needed by Disruptor.
 * 
 * @author despird
 * @version 0.1 2014-6-2
 * 
 * @author diaogc
 * @version 0.2 2017-8-4
 * Update to support EventHandler registering.
 * 
 **/
public class EventDispatcher {

	static Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

	/** The life cycle hooker priority */
	static public int LIFECYCLE_HOOKER_PRIORITY = 0;
	
	private AtomicInteger hookerIdGenerator = new AtomicInteger(100); 	
	/** the Disruptor instance */
	private Disruptor<RingEvent> disruptor = null;
	/** the event handler */
	private Set<EventHandler<RingEvent>> handlers = new HashSet<EventHandler<RingEvent>>();
	//(){ new RingEventHandler() }
	/** the event hooker list */
	private Map<EventType, EventHooker<?>> hookers = new HashMap<EventType, EventHooker<?>>();
	/** single instance */
	private static EventDispatcher instance;
	/** the lifecycle hooker */
	private LifecycleHooker hooker = null;
	/** running flag */
	private boolean running = false;
	/**
	 * default event disptacher
	 **/
	private EventDispatcher() {
		
		// add a default event handler
		handlers.add(new RingEventHandler());
		
		// define a lifecyle hooker.
		hooker = new LifecycleHooker("EventDispatcher", LIFECYCLE_HOOKER_PRIORITY){

			@Override
			public void initial() {
				
				instance.initial(1024);
				sendFeedback(false, "EventDispatcher initial done");
			}

			@Override
			public void startup() {
				instance.startup();
				sendFeedback(false, "EventDispatcher startup done");
			}

			@Override
			public void shutdown() {
				instance.shutdown();
				sendFeedback(false, "EventDispatcher shutdown done");
			}

		};
	}

	/**
	 * Get the single instance of event dispatcher
	 * 
	 * @return the single instance
	 **/
	public static EventDispatcher getInstance() {

		if (null == instance)
			instance = new EventDispatcher();

		return instance;
	}

	/**
	 * Check if the disruptor is running 
	 **/
	public boolean isRunning(){
		
		return this.running;
	}
	
	/**
	 * Start the disruptor
	 **/
	public void startup() {
		
		disruptor.start();
		this.running = true;
	}

	/**
	 * Shutdown the disruptor 
	 **/
	public void shutdown(){
		
		disruptor.shutdown();
		this.running = false;
	}
	
	/**
	 * Get the LifecycleHooker instance, it make EventDispatcher to 
	 * act according to LifecycleEvent.
	 * 
	 *  @return LifecycleHooker
	 **/
	public LifecycleHooker getLifecycleHooker(){
		return this.hooker;
	}
		
	/**
	 * Set up the disruptor
	 **/
	@SuppressWarnings("unchecked")
	private void initial(int buffersize) {
		// Executor that will be used to construct new threads for consumers
		ThreadFactory threadFactory = new ThreadFactory() {
			   public Thread newThread(Runnable r) {
			     return new Thread(r);
			   }
			 };
			 
		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = buffersize;
		EventFactory<RingEvent> eventbuilder = RingEvent.EVENT_FACTORY;
		
		// create new disruptor instance with multiple producers default
		disruptor = new Disruptor<RingEvent>(eventbuilder, bufferSize, threadFactory);
		EventHandler<?>[] handlerArray = new EventHandler<?>[handlers.size()];
		int i = 0;
		for(EventHandler<?> handler : handlers) {
			handlerArray[i] = handler;
			i++;
		}
	
		// Connect the handler
		//EventHandlerGroup<RingEvent> handlerGroup = 
		disruptor.handleEventsWith((EventHandler<RingEvent>[])handlerArray);
		//handlerGroup.then(handlers);
	}

	/**
	 * Get EventHooker instance according to EnentType object. this event hooker
	 * provides the event producer to send payload data.
	 * 
	 * @return EventHooker 
	 **/
	public EventHooker<?> getEventHooker(EventType eventType){
		
		return hookers.get(eventType);
	}
	
	/**
	 * dispatch event payload to respective hooker
	 * @param ringevent the event
	 * @param sequece
	 * @param endofBatch
	 **/
	private void onRingEvent(RingEvent ringevent, long sequence, boolean endOfBatch) {
		
		// After take payload, it is removed from ring event instance.
		EventType eventType = ringevent.getEventType();
		EventHooker<?> eventHooker = null;
		if(null == eventType) {
			LOGGER.warn("EventType cannot be null");
		}else {
			eventHooker = hookers.get(eventType);
		}

		if (eventHooker != null) {
			EventPayload payload = ringevent.takePayload();
			try {
				// keep the payload with empty chains, avoid the dead-cycle.
				payload.resetChainPayloads();
				// process the event, the chain payload maybe added.
				eventHooker.processPayload(payload);
				// check the chain payloads and re-send it to disruptor
				Collection<EventPayload> chainPayloads = payload.getChainPayloads();
				if(chainPayloads != null) {
					for(EventPayload chainPayload:chainPayloads) {
						if(LOGGER.isDebugEnabled()) {
							LOGGER.debug("found chain payload: {}", chainPayload.getEventType());
						}
						this.sendPayload(chainPayload);
					}
				}
			} catch ( RuntimeException | RingEventException e) {
				// here catch all the exception to avoid destroy the engine
				LOGGER.error("Error when processing event[{" + eventType + "}] payload",  e);
			}

		}else{
			
			LOGGER.warn("EventHooker with event:{} not exists.", eventType);
		}
	}

	/**
	 * Publish event EventPayload to specified EventType
	 * 
	 * @param payload the payload of specified event
	 * @param eventType the type of specified event
	 **/
	public void sendPayload(EventPayload payload){
		
		if(disruptor == null)
			return ;
		
		RingBuffer<RingEvent> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();  // Grab the next sequence
	    try{
	    		RingEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
	        event.setEventType(payload.getEventType());
	        event.setPayload(payload);  
	    }
	    finally
	    {
	        ringBuffer.publish(sequence);// for the sequence
	    }
	}
	
	/**
	 * Get the event producer, which used to publish event
	 * 
	 *  @param eventType the type of event
	 **/
	public EventProducer<?> getEventProducer(EventType eventType) throws RingEventException{
		EventHooker<?> eventHooker = hookers.get(eventType);
		if(eventHooker == null) return null;
		
		return eventHooker.getEventProducer();
	}
	
	/**
	 * Register an event hooker
	 * 
	 * @param eventHooker the hooker of event 
	 **/
	public EventType regEventHooker(EventHooker<?> eventHooker) {

		if(null == eventHooker.getEventType()){
			
			EventType eventType = new EventType(hookerIdGenerator.incrementAndGet());
			eventHooker.setEventType(eventType);
		}
		hookers.put(eventHooker.getEventType(),eventHooker);
		if(disruptor != null)
			eventHooker.setRingBuffer(disruptor.getRingBuffer());
		
		return eventHooker.getEventType();
	}
	
	/**
	 * Register the row event handler, it will be added to disruptor's handler Group to digest 
	 * the event in parallel. 
	 * 
	 * @param eventhandler the handler of event
	 **/
	public void regEventHandler(EventHandler<RingEvent> eventhandler) {
		
		this.handlers.add(eventhandler);
	}
	
	/**
	 * Unregister the specified type of event hooker 
	 * 
	 * @param eventType
	 **/
	public void unRegEventHooker(EventType eventType){
		
		EventHooker<?> eventHooker = hookers.remove(eventType);
		eventHooker.setRingBuffer(null);// clear reference to buffer.
		
	}
	
	/**
	 * Class RingEventHandler to process event payload 
	 **/
	private static class RingEventHandler implements EventHandler<RingEvent> {

		@Override
		public void onEvent(RingEvent ringevent, long sequence,
				boolean endOfBatch) throws Exception {
			instance.onRingEvent(ringevent, sequence, endOfBatch);
		}

	}
}
