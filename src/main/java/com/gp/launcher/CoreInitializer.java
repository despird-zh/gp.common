package com.gp.launcher;

import com.gp.exception.BaseException;

/**
 * CoreInitializer help to initial the component of Core instance 
 * 
 * @author despird
 * @version 0.1 2014-3-4
 **/
public abstract class CoreInitializer {

	LifecycleListener hooker = null;
	/**
	 * Default constructor, here the hooker will be bind to CoreLauncher
	 **/
	public CoreInitializer() throws BaseException{
		
		hooker = setupLifecycleHooker();
	}
	
	/**
	 * Get the Lifecycle event hooker for lifecycle operation 
	 * this method to be called in constructor to set the hooker name.
	 * 
	 * @return LifecycleHooker lifecycle event listener
	 **/
	public abstract LifecycleListener setupLifecycleHooker() throws BaseException;
	
	/**
	 * Get the LifecycleHooker post default constructor and setup 
	 **/
	public LifecycleListener getLifecycleHooker(){
		
		return this.hooker;
	}
}
