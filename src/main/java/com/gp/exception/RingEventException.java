package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class RingEventException  extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> event_bundles = new HashMap<Locale, ResourceBundle>();

	/**
	 * Constructor with error code and parameters
	 **/
	public RingEventException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}

	/**
	 * Constructor with error code, cause and parameters
	 **/
    public RingEventException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }

	/**
	 * Constructor with error code and parameters
	 **/
	public RingEventException(Locale locale, String errorcode, Object... param) {
		super(locale, errorcode, param);
	}

	/**
	 * Constructor with error code, cause and parameters
	 **/
    public RingEventException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(locale, errorcode, cause, param);
    }

	/**
	 * Constructor with cause
	 **/
    public RingEventException(Throwable cause) {
        super(cause);
    }
    
    @Override
	protected String findMessage(Locale locale, String errorcode,Object ... param){
		
		ResourceBundle rb = event_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, RingEventException.class);
			event_bundles.put(locale, rb);
		}
		String messagePattern ;
		if(rb == null || !rb.containsKey(errorcode)){
			matched = false;
			// try to find from super class.
			return super.findMessage(locale, errorcode, param);
		}else{
			messagePattern = rb.getString(errorcode);
			matched = true;
		}
		return MessageFormat.format(messagePattern, param);
	}

}
