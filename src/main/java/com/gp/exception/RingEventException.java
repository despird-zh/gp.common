package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class RingEventException  extends BaseException{

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> event_bundles = new HashMap<Locale, ResourceBundle>();
	
	public RingEventException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public RingEventException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public RingEventException(Locale locale, String errorcode, Object... param) {
		super(locale, errorcode, param);
	}
	
    public RingEventException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(locale, errorcode, cause, param);
    }
    
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
		String messagePattern =  (rb == null || !rb.containsKey(errorcode)) ? errorcode :  rb.getString(errorcode);
		if(StringUtils.equals(messagePattern, errorcode)){
			return super.findMessage(locale, errorcode, param);
		}
		return MessageFormat.format(messagePattern, param);
	}

}
