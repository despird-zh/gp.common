package com.gp.info;

/**
 * As for flat table might has multiple blind column
 * Use this class to locate which column to be queried
 * 
 * @author gary diao 
 * @version 0.1 2015-12-23
 * 
 **/
public interface FlatColLocator {
	
	/**
	 * Get the index of column 
	 **/
	public int getColumnIndex();
	
	/**
	 * Get the prefix of column 
	 **/
	public String getColumnPrefix();
	
	/**
	 * Get the column name 
	 **/
	public String getColumn();
}
