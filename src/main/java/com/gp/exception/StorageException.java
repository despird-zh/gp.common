package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import com.gp.exception.BaseException;

public class StorageException extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> storage_bundles = new HashMap<Locale, ResourceBundle>();

	public StorageException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public StorageException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public StorageException(Locale locale, String errorcode, Object... param) {
		super(errorcode, param);
		this.message = findMessage(locale, errorcode, param);
	}
	
    public StorageException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(errorcode, cause);
        this.message = findMessage(locale,errorcode, param);
    }
    
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
		String messagePattern = (rb == null) ? errorcode : rb.getString(errorcode);
		if(StringUtils.isBlank(messagePattern)){
			return super.findMessage(locale, errorcode, param);
		}
		return MessageFormat.format(messagePattern, param);
	}

}
