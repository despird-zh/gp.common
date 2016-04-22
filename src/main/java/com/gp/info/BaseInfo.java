package com.gp.info;

import java.io.Serializable;

public interface BaseInfo<K> extends Serializable{
	
	public InfoId<K> getInfoId();
}
