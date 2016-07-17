package com.gp.info;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
		this.idKey = idKey;
		this.id = id;
	}
	
	/**
	 * constructor with table name, id column and id value
	 **/
	public InfoId(String idKey,String idColumn, K id){
		this.idKey = idKey;
		this.idColumn = idColumn;
		this.id = id;
	}
	
	/**
	 * constructor with Identifier and id 
	 **/
	public InfoId(Identifier idKey, K id){
		this.idKey = idKey.getSchema();
		this.id = id;
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
	 * convert InfoId into string, format is {type}:{id} ,e.g audit:a000101 
	 **/
	@Override
	public String toString(){
		
		return this.idKey + GeneralConstants.KEYS_SEPARATOR + this.getIdColumn() + GeneralConstants.KEYS_SEPARATOR + id.toString();
	}
	
	/**
	 * parse the string into InfoId object, format is {type}:{id} ,e.g audit:a000101 
	 * 
	 * @param idstr the id string get from InfoId.toString()
	 * @param idclazz the class of Id
	 **/
	@SuppressWarnings("unchecked")
	public static <M> InfoId<M> parseInfoId(String idstr, Class<M> idclazz){
		
		if(StringUtils.isBlank(idstr)) return null;
		
		String[] parts = StringUtils.split(idstr, GeneralConstants.KEYS_SEPARATOR);
		
		M id = null;
		if(parts.length == 1){
			
			if(Integer.class.equals(idclazz))
				id = (M)new Integer(-1);
			
			else if(Long.class.equals(idclazz))
				id = (M)new Long(-1);
			
			else if(String.class.equals(idclazz))
				id = (M)new String();
			
		}
		
		if(parts.length >= 2 ){
			
			if(Integer.class.equals(idclazz))
				id = (M)Integer.valueOf(parts[1]);
			
			else if(Long.class.equals(idclazz))
				id = (M)Long.valueOf(parts[1]);
			
			else if(String.class.equals(idclazz))
				id = (M)parts[1];
			
		}
				
		return new InfoId<M>(parts[0], id);				
	}

	/**
	 * check if the InforId is valid 
	 * @param the id object to be checked
	 * @return true valid; false invalid
	 **/
	public static boolean isValid(InfoId<?> id){
		
		if(id == null){ 
			
			return false;
		}else if(id.getId() == null ){	
			
			return false;
			
		}else if(ObjectUtils.equals(id.getId(), GeneralConstants.LOCAL_SOURCE )||
				ObjectUtils.equals(id.getId(), GeneralConstants.PERSON_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_ROOT) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.FOLDER_ROOT) ){
			
			return true;
			
		}else if( id.getId() instanceof Integer && (Integer)(id.getId()) < 1){
			
			return false;
		}else if( id.getId() instanceof Long && (Long)(id.getId()) < 1){
			
			return false;
		}
		
		return true;
	}

	/**
	 * Get the column of id 
	 **/
	public String getIdColumn() {
		return idColumn;
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
