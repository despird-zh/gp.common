package com.gp.disruptor;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.gp.common.GeneralConstants;

/**
 * The event type to identify payload and hooker
 **/
public class EventType {

	/** audit event type  */
	public static EventType AUDIT   = new EventType(10);
	/** index event type */
	public static EventType SYNC   = new EventType(11);
	/** index event type */
	public static EventType CORE   = new EventType(12);
	/** unknown event type */
	public static EventType UNKNOWN = new EventType(-1); 
	
	private int type = -1;
	
	public EventType(int type){
		
		this.type = type;
	}
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof EventType)) {
			return false;
		}
		// step 3
		EventType that = (EventType) other;
		// step 4
		return new EqualsBuilder()
			.append(this.type, that.type).isEquals();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder(17, 37).append(this.type).toHashCode();
	}
	
	@Override
	public String toString(){
		String name = null;
		
		switch (this.type) {
			case 10 : 
				name = "AUDIT"; break;
			case 11 : 
				name = "SYNC"; break;
			case 12 : 
				name = "CORE"; break;
			case -1 : 
				name = "UNKNOWN"; break;
			default : 
				name = "UNKNOWN"; 
		}
		
		return (name == null ? "" : name) + GeneralConstants.OPTS_SEPARATOR + String.valueOf(type);
	}

}
