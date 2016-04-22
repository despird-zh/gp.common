package com.gp.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class DateTimeUtils {
	
    /** year-month format */  
    private static final String MONTH_FORMAT = "yyyy-MM";  
    /** year-month-day format */  
    private static final String DATE_FORMAT = "yyyy-MM-dd";  
    /** year-month-day hour:minute:second format*/  
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /** hour:minute:second format*/ 
    private static final String TIME_FORMAT = "HH:mm:ss"; 
    /** date format */
    private static SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);  
    /** date format */
    private static SimpleDateFormat sdf_month_format = new SimpleDateFormat(MONTH_FORMAT);  
    /** date format */
    private static SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);  
    /** date format */
    private static SimpleDateFormat sdf_time_format = new SimpleDateFormat(TIME_FORMAT); 
   
    public static final List<Long> TIMES = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1) );
    
    public static final List<String> TIMESTRINGS = Arrays.asList("year","month","day","hour","minute","second");
    
    /**
	 * convert the date into specified time zone 
	 **/
	public static Date toTimeZone(Date origin, TimeZone timezone){
		
		DateTimeZone elzone = DateTimeZone.forTimeZone(timezone);		
		return toTimeZone(origin,elzone);
	}
	
	/**
	 * convert the date into specified time zone 
	 **/
	public static Date toTimeZone(Date origin, String timezone){
		
		DateTimeZone elzone = DateTimeZone.forID(timezone);		
		return toTimeZone(origin,elzone);
	}
	
	/**
	 * convert the date into specified time zone 
	 **/
	public static Date toTimeZone(Date origin, DateTimeZone elzone){
		
		DateTime dateTime = new LocalDateTime(origin.getTime()).toDateTime(elzone);
		return dateTime.toDate();
		
	}
	
	/**
	 * Convert the string into DateTimeZone 
	 **/
	public static DateTimeZone forTimeZone(String timezone){
		
		return DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone));
	}
	
	/**
	 * Get current data time 
	 **/
	public static Date now(){
		
		return new Date(System.currentTimeMillis());
	}
	
	public static String toYearMonth(Date date){
		
		return sdf_month_format.format(date);		
	}
	
	public static String toYearMonthDay(Date date){
		
		return sdf_date_format.format(date);
	}
	
	public static String toDateTime(Date date){
		
		return sdf_datetime_format.format(date);
	}
	
	public static String toTime(Date date){
		
		return sdf_time_format.format(date);
	}

	/**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
	public static String toDuration(long duration) {

	    StringBuffer res = new StringBuffer();
	    for(int i=0;i < TIMES.size(); i++) {
	        Long current = TIMES.get(i);
	        long temp = duration/current;
	        if(temp>0) {
	            res.append(temp).append(" ").append( TIMESTRINGS.get(i) ).append(temp > 1 ? "s" : "");
		        long temp2 = duration%current;
		        if(i < TIMES.size() -1 ){
		        	current = TIMES.get(i + 1);
		        	temp = temp2/current;
		        	if(temp>0) {
		        		res.append(" ").append(temp).append(" ").append( TIMESTRINGS.get(i+1) ).append(temp > 1 ? "s" : "");
		        	}
		        }
	            break;	        
	        }
	    }
	    if("".equals(res.toString()))
	        return "0 second ago";
	    else
	        return res.toString();
	}
	
	 /**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
	public static String toDuration(long duration, List<String> TIMESTRINGS, Locale locale) {

	    StringBuffer res = new StringBuffer();
	    for(int i=0;i < TIMES.size(); i++) {
	        Long current = TIMES.get(i);
	        long temp = duration/current;
	        if(temp>0) {
	            res.append(temp).append(" ").append( TIMESTRINGS.get(i));
	           
	            if(!"zh_cn".equals(locale.getLanguage()))
	            	res.append(temp > 1 ? "s" : "");
	            
		        long temp2 = duration%current;
		        if(i < TIMES.size() -1 ){
		        	current = TIMES.get(i + 1);
		        	temp = temp2/current;
		        	if(temp>0) {
		        		res.append(" ").append(temp).append(" ").append( TIMESTRINGS.get(i+1) );
			            if(!"zh_cn".equals(locale.getLanguage()))
			            	res.append(temp > 1 ? "s" : "");
		        	}
		        }
	            break;	        
	        }
	    }
	    if("".equals(res.toString()))
	        return "0 second ago";
	    else
	        return res.toString();
	}
	
	/**
	 * TEST only 
	 **/
	public static void main(String args[]) {
	    System.out.println(toDuration(123));
	    System.out.println(toDuration(1230));
	    System.out.println(toDuration(12300));
	    System.out.println(toDuration(123000));
	    System.out.println(toDuration(1230000));
	    System.out.println(toDuration(12300000));
	    System.out.println(toDuration(123000000));
	    System.out.println(toDuration(1230000000));
	    System.out.println(toDuration(12300000000L));
	    System.out.println(toDuration(123000000000L));
	    System.out.println(toDuration(123000000000L));
	    
	    List<String > TIMESTRINGS = Arrays.asList("年","月","天","小时","分钟","秒");
	    System.out.println(toDuration(123000000000L, TIMESTRINGS, new Locale("zh_CN")));
	}
}
