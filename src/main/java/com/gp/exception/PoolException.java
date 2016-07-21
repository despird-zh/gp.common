package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import com.gp.exception.BaseException;

public class PoolException extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> pool_bundles = new HashMap<Locale, ResourceBundle>();

	/**
	 * Constructor with error code and parameters
	 **/
	public PoolException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}

	/**
	 * Constructor with error code and parameters and cause
	 **/
    public PoolException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }

	/**
	 * Constructor with error code and parameters
	 **/
	public PoolException(Locale locale, String errorcode, Object... param) {
		super(locale, errorcode, param);
	}

	/**
	 * Constructor with error code and parameters and cause
	 **/
    public PoolException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(locale, errorcode, cause, param);
    }

	/**
	 * Constructor with cause exception
	 **/
    public PoolException(Throwable cause) {
        super(cause);
    }
    	
    @Override
	protected String findMessage(Locale locale, String errorcode,Object ... param){
		
		ResourceBundle rb = pool_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, PoolException.class);
			pool_bundles.put(locale, rb);
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
