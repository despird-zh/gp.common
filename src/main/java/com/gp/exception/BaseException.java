package com.gp.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	private static Map<Locale, ResourceBundle> base_bundles = new HashMap<Locale, ResourceBundle>();
	
	protected String message;
	
	protected static ResourceBundle loadResourceBundle(Locale locale,Class<?> selfclazz){
		
		String fullname = selfclazz.getName();		
		fullname = fullname.replace('.', '/');
		
		ResourceBundle rb = null;
		try{
		rb = ResourceBundle.getBundle(fullname, locale);
		}catch(MissingResourceException mre){
			// ignore
		}
		return rb;		
	}
	
	public BaseException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public BaseException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public BaseException(Locale locale,String errorcode,Object ...param){
		super(errorcode);
		this.message = findMessage(locale,errorcode, param);
	}
	
    public BaseException(Locale locale,String errorcode, Throwable cause,Object ...param) {
        super(errorcode, cause);
        this.message = findMessage(locale,errorcode, param);
    }
    
    public BaseException(Throwable cause) {
        super(cause);
    }
    
	public String getErrorcode(){
		
		return super.getMessage();
	}
	
	@Override
	public String getMessage(){
		
		return this.message;
	}
	
	protected String findMessage(Locale locale, String errorcode,Object ... param){
		
		ResourceBundle rb = base_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, BaseException.class);
			base_bundles.put(locale, rb);
		}
		String messagePattern = (rb == null) ? errorcode : rb.getString(errorcode);
		
		messagePattern = StringUtils.isBlank(messagePattern)? errorcode : messagePattern;
		
		return MessageFormat.format(messagePattern, param);
	}
		
	public void printStackTrace(PrintStream s)
	{	
	    super.printStackTrace(s);
	    
	}
	 
	public void printStackTrace(PrintWriter s)
	{
		super.printStackTrace(s);

	 }
	
	public void printStackTrace()
	{

		printStackTrace(System.err);
	}
	
}
