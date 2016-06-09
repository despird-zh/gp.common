package com.gp.info;

/**
 * A Simple class to wrap key-value pair.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class KVPair<K, V> {
	
	private K key;
	
	private V value;
	
	/**
	 * Default constructor 
	 **/
	public KVPair(){
	}
	
	/**
	 * Default constructor
	 * @param key the key information
	 * @param value the value data 
	 **/
	public KVPair(K key, V value){
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}

}
