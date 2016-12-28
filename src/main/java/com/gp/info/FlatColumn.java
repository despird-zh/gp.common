package com.gp.info;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class wrap the column of flat table.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class FlatColumn implements FlatColLocator{

	private String prefix ;
	
	private Integer index = null;
	
	/**
	 * The constructor with column 
	 * @param column the column name
	 **/
	public FlatColumn(String column){
		this.prefix = column;
	}
	
	/**
	 * the constructor with prefix and column index
	 * @param prefix the prefix of column
	 * @param index the index of column 
	 **/
	public FlatColumn(String prefix, Integer index){
		this.prefix = prefix;
		this.index = index;
	}
	
	public Integer getColIndex() {
		
		return index;
	}

	public String getColPrefix() {
		
		return prefix;
	}

	@Override
	public String getColumn() {
		
		return (index == null) ? prefix : prefix + index;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37).
			       append(index).
			       append(prefix).
			       toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof FlatColLocator == false) {
		    return false;
		}
		if (this == obj) {
		    return true;
		}
		FlatColLocator rhs = (FlatColLocator) obj;
		
		return new EqualsBuilder()
		                .append(getColumn(), rhs.getColumn())
		                .isEquals();
	}
}
