package com.gp.info;

import java.io.Serializable;

/**
 * The interface define the basic method to retrieve the Id information
 *
 * @author gary diao
 * @version v0.1 2015-10-12
 *
 **/
public interface BaseInfo<K> extends Serializable{

	/**
	 * Get the InfoId object
	 **/
	public InfoId<K> getInfoId();

	/**
	 * Get the Id object
	 **/
	public K getId();
}
