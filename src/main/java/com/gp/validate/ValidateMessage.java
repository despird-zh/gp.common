package com.gp.validate;

public class ValidateMessage {
	
	private String property;
	
	private String message;

	public ValidateMessage(String property, String message){
		
		this.property = property;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "ValidationMessage [property=" + property + ", message=" + message + "]";
	}	
}
