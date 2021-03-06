/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gp.common;

import java.io.InputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Retrieve the configuration variables from environment configuration properties
 * file. 
 * GeneralConfig.getString("demo.key");<br>
 * the property file locate under root of class path:<em>envconfig.properties</em>
 * <p>
 * 	this class extends from Apache configuration PropertiesConfiguration
 * </p>
 * 
 * @see org.apache.commons.configuration.ConfigurationException
 * @author Despird-zh
 * @version 0.1 2014-1-1
 **/
public class GeneralConfig{

	/** override configuration file*/
	public static final String DEFAULT_OVERRIDE_CONFIG = "core-config.properties";
	/** default configuration path */
	public static final String DEFAULT_CONFIG = "META-INF/core-config.properties";
	
	private static Logger LOGGER = LoggerFactory.getLogger(GeneralConfig.class);
	
	private static PropertiesConfiguration selfConfig = null;
	private static PropertiesConfiguration overrideConfig = null;
	
	static{
		try {
			selfConfig = new PropertiesConfiguration(DEFAULT_CONFIG);
			InputStream is = GeneralConfig.class.getClassLoader().getResourceAsStream(DEFAULT_OVERRIDE_CONFIG);
			if(is != null)
				overrideConfig = new PropertiesConfiguration(DEFAULT_OVERRIDE_CONFIG);
		} catch (ConfigurationException e) {
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Fail to load configuration properties file",e);
			else
				LOGGER.warn("Fail to load configuration properties file");
		}	
	}
	
	private GeneralConfig() throws ConfigurationException{}
	
	/**
	 * Get the String value of key 
	 * 
	 * @param key
	 * @param defaultVal
	 **/
	public static String getString(String key,String defaultVal){
		
		String val = null;
		if(overrideConfig != null){
			val = overrideConfig.getString(key);
		}
		
		if(val == null || "".equals(val)){
			val = selfConfig.getString(key, defaultVal);
		}
		
		return val;
	}
	
	/**
	 * Get the String value of key 
	 **/
	public static String getString(String key){
		
		return getString(key,null);
	}

	/**
	 * Get the String Array value of key 
	 **/
	public static String[] getStringArray(String key){
		
		String[] vals = null;
		if(overrideConfig != null){
			vals = overrideConfig.getStringArray(key);
		}
		
		if(vals == null || vals.length == 0){
			vals = selfConfig.getStringArray(key);
		}
		
		return vals;
	}
	
}