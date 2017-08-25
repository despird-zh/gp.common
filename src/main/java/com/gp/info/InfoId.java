package com.gp.info;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.gp.common.GeneralConstants;

/**
 * Define the key of table record, it include type and id.
 * the InfoId<K> class will holds the table name and id column information.
 * it also support equals and hashCode operation.
 * 
 * @author gary diao
 * @version 0.1 2015-10-2
 * 
 **/
public class InfoId <K> implements Serializable{

	private static final long serialVersionUID = 1L;

	/** table alias */
	private String idKey;
	
	/** the id column*/
	private String idColumn = "id";
	
	/** record id */
	private K id;

	/**
	 * constructor with table and id 
	 **/
	public InfoId(String idKey, K id){
		if( !(id instanceof Long) || !(id instanceof Integer) || !(id instanceof String)) {
			throw new IllegalArgumentException("the id must be Long/Integer/String");
		}
		this.idKey = idKey;
		this.id = id;
	}
	
	/**
	 * constructor with table name, id column and id value
	 **/
	public InfoId(String idKey,String idColumn, K id){
		this(idKey, id);
		this.idColumn = idColumn;
		
	}
	
	/**
	 * constructor with Identifier and id 
	 **/
	public InfoId(Identifier idKey, K id){

		this(idKey.getSchema(), idKey.getIdColumn(), id);
	}
	
	/**
	 * Get type 
	 **/
	public String getIdKey() {
		return idKey;
	}

	/**
	 * Set type 
	 **/
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}

	/**
	 * Get id 
	 **/
	public K getId() {
		return id;
	}

	/**
	 * set id 
	 **/
	public void setId(K id) {
		this.id = id;
	}	
	
	/**
	 * convert InfoId into string, format is {type}:{column id}:{id} ,e.g audit:audit_id:a000101 
	 **/
	@Override
	public String toString(){
		
		return this.idKey + GeneralConstants.KEYS_SEPARATOR + this.getIdColumn() + GeneralConstants.KEYS_SEPARATOR + id.toString();
	}
	

	/**
	 * Get the column of id 
	 **/
	public String getIdColumn() {
		return idColumn;
	}
	
	/**
	 * Set the column of id 
	 **/
	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof InfoId)) {
			return false;
		}
		// step 3
		InfoId<?> that = (InfoId<?>) other;

		return new EqualsBuilder()
			.append(this.idKey, this.idKey)
			.append(this.id, that.id).isEquals();
		
	}

	@Override
	public int hashCode() {
				
		return new HashCodeBuilder(17, 37)
			.append(this.idKey)
			.append(this.id).toHashCode();
			
	}
}
