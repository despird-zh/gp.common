package com.gp.info;

/**
 * this interface define the methods of Table record id, it also support generating new InfoId<Long>
 * 
 **/
public interface Identifier {
	
	/**
	 * Get the table name 
	 **/
	public String getTable();
	
	/**
	 * get the schema name
	 * 
	 * */
	public String getSchema();

	/**
	 * get the id column name
	 *
	 * */
	public String getIdColumn();

	/**
	 * create a new InfoId<T> object. 
	 **/
	public <T> InfoId<T> getInfoId(T sequence);
}
