package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.gp.exception.BaseException;

public class StorageException extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> storage_bundles = new HashMap<Locale, ResourceBundle>();

	/**
	 * Constructor with error code and parameters
	 **/
	public StorageException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}

	/**
	 * Constructor with error code, cause and parameters
	 **/
    public StorageException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }

	/**
	 * Constructor with error code and parameters
	 **/
	public StorageException(Locale locale, String errorcode, Object... param) {
		super(locale, errorcode, param);
	}

	/**
	 * Constructor with error code, cause and parameters
	 **/
    public StorageException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(locale, errorcode, cause, param);
    }

	/**
	 * Constructor with cause
	 **/
    public StorageException(Throwable cause) {
        super(cause);
    }
    	
    @Override
	protected String findMessage(Locale locale, String errorcode,Object ... param){
		
		ResourceBundle rb = storage_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, StorageException.class);
			storage_bundles.put(locale, rb);
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
