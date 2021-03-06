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

import java.util.HashMap;
import java.util.Map;

import com.gp.info.InfoId;

/**
 * This class be used to hold the object during executing the services and DAOs
 * Generic Type <A> indicates the auditdata object type
 * <ul>
 * <li>transfer data across services and DAOs</li>
 * <li>Collect operation audit data during procession</li>
 * </ul>
 * 
 * @author gary diao 
 * @version 0.1 2014-10-10
 **/
public class GeneralContext implements AutoCloseable{
	
	/**
	 * State indicates the execution result 
	 **/
	public static enum ExecState{
		
		SUCCESS,
		FAIL,
		EXCEP,
		UNKNOWN;
	}
	
	/** the value map to store extra objects */
	private Map<String, Object> valuemap;

	/** audit on/off flag */
	private boolean auditable = false;

	/**
	 * Set data to context
	 * @param key the key
	 * @param data the data  
	 **/
	public void putContextData(String key, Object data){
		
		if(null == valuemap)
			valuemap = new HashMap<String, Object>();
		
		valuemap.put(key, data);
	}
	
	/**
	 * Return the object stored in map 
	 * @param key the key of data 
	 **/
	@SuppressWarnings("unchecked")
	public <T> T getContextData(String key, Class<T> clazz){
		
		if(null == valuemap)
			return null;
		
		Object val = valuemap.get(key);
		if(val == null) {
			return null;
		}
		else if(clazz.isAssignableFrom(val.getClass())){
			return (T) val;
		}else{
			
			return null;
		}
	}
	
	/**
	 * Return the object stored in map 
	 * @param key the key of data 
	 **/
	public Object getContextData(String key){
		
		if(null == valuemap)
			return null;
		
		return valuemap.get(key);
	}
	
	/**
	 * the context data map
	 * @return Map the map of key-value pairs. 
	 **/
	public Map<String, Object> getContextMap(){
		
		return valuemap;
	}
	
	/**
	 * indicate support audit collecting or not
	 **/
	public boolean isAuditable(){
		
		return auditable;
	}
	
	/**
	 * switch the context audit on 
	 **/
	public void setAuditable(boolean auditable){
		
		this.auditable = auditable;
	}
	
	/**
	 * Set the access point information
	 * @param client the client data: browser or desktop
	 * @param host the address of client
	 * @param app the application of client
	 * @param version  the version
	 **/
	public void setAccessPoint(String client, String host, String app, String version){
		
		// do nothing
	}
	
	/**
	 * begin the operation with necessary information
	 * 
	 * @param subject the execute user
	 * @param verb the primary verb of operation
	 * @param object the data to locate the data to be processed
	 * @param predicate the parameters etc.
	 **/
	public void beginOperation(String subject, String verb, InfoId<?> object, Object predicate){
		
		// do nothing
	}
	
	/**
	 * Add the predicate to operation primary verb 
	 * @param predicate the bean or Map object preferred 
	 **/
	public void addOperationPredicate(String predicateKey, Object predicate){
		
		// do nothing
	}
	
	
	public void addOperationPredicates(Object predicates){
		// do nothing
	}
	
	/**
	 * Set the operation target object
	 * @param object the target object identifier 
	 **/
	public void setOperationObject(InfoId<?> object){
		// do nothing
	}
	
	/**
	 * end operation
	 * @param state the result of operation reference to ExecState
	 * @param message the message of operation 
	 **/
	public void endOperation(ExecState state, String message){
		
		// do nothing
	}
	
	/**
	 * Get the audit data, the audit data to be implemented in subclass.
	 * 
	 * @return A which hold the audit data
	 **/
	public <A> A getOperationData(Class<A> clazz){
		
		return null;
	}

	/**
	 * Persist the audit data, persist way to be implemented in subclass. 
	 **/
	public void handleOperationData(){		
		// do nothing
	}
	
	@Override
	public void close() {
		
		if(valuemap != null){
			valuemap.clear();
			valuemap = null;
		}
		this.auditable = false;	
	}
}
