package com.gp.disruptor;

import com.gp.exception.BaseException;
import com.gp.launcher.CoreInitializer;
import com.gp.launcher.LifecycleListener;

/**
 * The event initializer
 * 
 * @author despird
 * @version 0.1 2015-4-21
 **/
public class EventEngineInitializer extends CoreInitializer{

	/**
	 * Default constructor 
	 **/
	public EventEngineInitializer() throws BaseException {
		// call super to initial the hooker 
		super();
	}

	@Override
	public LifecycleListener setupLifecycleHooker() throws BaseException {
		EventDispatcher instance = EventDispatcher.getInstance();
		return instance.getLifecycleHooker();
	}

}
