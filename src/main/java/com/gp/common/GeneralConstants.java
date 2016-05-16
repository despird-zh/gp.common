package com.gp.common;

/**
 * Define all the global constants
 * 
 * @author gary.diao
 * @version 0.1 2014-5-6
 **/
public class GeneralConstants {
	
	/** Separator used to combine multiple keys ->":" */
	public static final String NAMES_SEPARATOR = ".";
	
	/** Separator used to combine multiple keys ->":" */
	public static final String KEYS_SEPARATOR = ":";

	/** Separator used to combine values -> "|" */
	public static final String VALUES_SEPARATOR = "|";
	
	/** Separator used to combine key-value pairs-> "," */
	public static final String KVPAIRS_SEPARATOR = ",";
	
	/** Separator used to combine key-value -> "=" */
	public static final String KEYVAL_SEPARATOR = "=";
	
	/** Separator used to combine resource and instance -> "@" */
	public static final String RES_INS_SEPARATOR = "@";
	
	/** The constant value of acl owner subject -> "owner" */
	public static final String OWNER_SUBJECT = "owner";
	
	/** The constant value of acl other subject -> "other" */
	public static final String EVERYONE_SUBJECT = "everyone";
	
	/** local system instance to differentiate with remote system*/
	public static final Integer LOCAL_INSTANCE = -9999;
	
	/** person own cabinet work group */
	public static final Long PERSON_WORKGROUP = -999l;
	
	/** organization work group */
	public static final Long ORGHIER_WORKGROUP = -900l;
		
	/** the root organization hierarchy root id */
	public static final Long ORGHIER_ROOT = -99l;
	
	/** the root folder id of cabinet's folder */
	public static final Long FOLDER_ROOT = -98l;
	
	/** the scheme defined in URI string to locate the resource */
	public static String GP_SCHEME = "gbin";
	
	public static String SLASH_SEPARATOR = "/";

}
