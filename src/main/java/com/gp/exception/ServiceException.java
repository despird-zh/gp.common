package com.gp.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import com.gp.validation.ValidationMessage;

public class ServiceException  extends BaseException{

	private static final long serialVersionUID = 1L;
	
	private static Map<Locale, ResourceBundle> svc_bundles = new HashMap<Locale, ResourceBundle>();
	
	/** the message holder */
	private List<ValidationMessage> messages = null;
	
	public ServiceException(String errorcode,String ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public ServiceException(String errorcode, Throwable cause,String ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public ServiceException(Locale locale, String errorcode, String... param) {
		super(errorcode, param);
		this.message = findMessage(locale, errorcode, param);
	}
	
    public ServiceException(Locale locale, String errorcode, Throwable cause,String ...param) {
        super(errorcode, cause);
        this.message = findMessage(locale, errorcode, param);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
    
    @Override
	protected String findMessage(Locale locale, String errorcode,String ... param){
		
		ResourceBundle rb = svc_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, ServiceException.class);
			svc_bundles.put(locale, rb);
		}
		String messagePattern =  (rb == null) ? errorcode :  rb.getString(errorcode);
		if(StringUtils.isBlank(messagePattern)){
			return super.findMessage(locale, errorcode, param);
		}
		return MessageFormatter.arrayFormat(messagePattern, param).getMessage();
	}

	public List<ValidationMessage> getValidationMessages() {
		return messages;
	}

	public void addValidationMessages(List<ValidationMessage> messages) {
		if(this.messages == null)
			this.messages = new ArrayList<ValidationMessage>();
		
		this.messages.addAll(messages);
	}

	public void addValidationMessage(ValidationMessage message){
		
		if(this.messages == null)
			this.messages = new ArrayList<ValidationMessage>();
		
		this.messages.add(message);
	}
}
