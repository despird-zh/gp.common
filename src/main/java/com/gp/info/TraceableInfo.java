package com.gp.info;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class implements the necessary method for traceable table record.
 *
 * @author  gary diao
 * @version 0.1
 **/
public class TraceableInfo<K> implements BaseInfo<K>{

	private static final long serialVersionUID = 1L;

	private InfoId<K> infoId = null;
		
	private String modifier;
	
	private Date modifyDate;

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public InfoId<K> getInfoId() {
		return infoId;
	}

	public void setInfoId(InfoId<K> infoId) {
		this.infoId = infoId;
	}

	@JsonIgnore
	public K getId(){
		if(null == infoId) return null;
		return infoId.getId();
	}
}
