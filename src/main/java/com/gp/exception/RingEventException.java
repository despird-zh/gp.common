package com.gp.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.helpers.MessageFormatter;

public class RingEventException  extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> event_bundles = new HashMap<Locale, ResourceBundle>();
	
	public RingEventException(String errorcode,String ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public RingEventException(String errorcode, Throwable cause,String ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public RingEventException(Locale locale, String errorcode, String... param) {
		super(errorcode, param);
		this.message = findMessage(locale, errorcode, param);
	}
	
    public RingEventException(Locale locale, String errorcode, Throwable cause,String ...param) {
        super(errorcode, cause);
        this.message = findMessage(locale, errorcode, param);
    }
    
    public RingEventException(Throwable cause) {
        super(cause);
    }
    
    @Override
	protected String findMessage(Locale locale, String errorcode,String ... param){
		
		ResourceBundle rb = event_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, RingEventException.class);
			event_bundles.put(locale, rb);
		}
		String messagePattern =  (rb == null) ? errorcode : rb.getString(errorcode);
		if(StringUtils.isBlank(messagePattern)){
			return super.findMessage(locale, errorcode, param);
		}
		return MessageFormatter.arrayFormat(messagePattern, param).getMessage();
	}

}