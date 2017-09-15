package com.gp.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CoreLauncher is the Application entry point post SpringBoot start.
 * Usually it's startup is embedded into ContextRefreshedEvent handling,
 * and the shutdown is embedded into the ServletListener's contextDestroyed
 * 
 **/
public abstract class CoreLauncher {

	private static CoreLauncher  instance;
	
	static Logger LOGGER = LoggerFactory.getLogger(CoreLauncher.class);
	
	public static CoreLauncher getInstance(Class<? extends CoreLauncher> clazz) {
		
		if(null == instance) {
			
			try {
				instance = (CoreLauncher)clazz.newInstance();
			} catch (InstantiationException e) {
				LOGGER.debug("Fail to initial a new instance",e);
			} catch (IllegalAccessException e) {
				LOGGER.debug("Fail to initial a new instance",e);
			}
		}
		
		return instance;
	}

	/**
	 * Start point to define steps as need. 
	 **/
	public abstract void engineOn();
	
	/**
	 * End point to define steps as need. 
	 **/
	public abstract void engineOff();
}
