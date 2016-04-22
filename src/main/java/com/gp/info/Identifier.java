package com.gp.info;

public interface Identifier {
	
	public String getTable();
	
	public String getSchema();
	
	public <T> InfoId<T> getInfoId(T sequence);
}
