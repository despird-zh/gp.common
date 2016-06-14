package com.gp.info;

/**
 * CombineInfo help to wrap data with other extra information.
 * the generic P(primary) usually could be TraceableInfo sub classes.
 * the generic E(extended) could be extended data it could be any bean class.
 * 
 * @author gary diao
 * @version 0.1 2016-2-1
 **/
public class CombineInfo<P, E> {

	private P primary;
	
	private E extended;

	public P getPrimary() {
		return primary;
	}

	public void setPrimary(P primary) {
		this.primary = primary;
	}

	public E getExtended() {
		return extended;
	}

	public void setExtended(E extended) {
		this.extended = extended;
	}
	
}
