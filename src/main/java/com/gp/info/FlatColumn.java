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
	
	private int index;
	
	public FlatColumn(String prefix, int index){
		this.prefix = prefix;
		this.index = index;
	}
	
	@Override
	public int getColIndex() {
		
		return index;
	}

	@Override
	public String getColPrefix() {
		
		return prefix;
	}

	@Override
	public String getColumn() {
		
		return prefix + index;
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
		                .appendSuper(super.equals(obj))
		                .append(index, rhs.getColIndex())
		                .append(prefix, rhs.getColPrefix())
		                .isEquals();
	}
}
