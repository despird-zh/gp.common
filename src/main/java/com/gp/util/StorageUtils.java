package com.gp.util;

import org.apache.commons.lang.StringUtils;

import com.gp.common.GeneralConstants;
import com.gp.info.InfoId;
import com.gp.util.ByteString;
import com.gp.util.ByteUtils;

public class StorageUtils {
		
	public static long ID_OFFSET = (long)(Math.pow(2,32) - Math.pow(2,31));
	
	/**
	 * convert binary id (long) into file path pattern string
	 * this method disperse the binary file to different path tree.<br>
	 * e.g 367 -> /80/00/01/6f.367<br>
	 *         -> d:/a/80/00/01/6f.367.doc
	 * @param binaryId the id the binary record
	 * @param prefix usually be the root location of storage
	 * @param postfix the extension append to the path 
	 * 
	 * @return String the path key 
	 * 
	 **/
	public static String toPathKey(long binaryId, String prefix, String postfix){
		
		byte[] tbytes = ByteUtils.toBytes(ID_OFFSET + binaryId);
		
		String hexStr = ByteString.of(tbytes).hex();
		
		StringBuffer buf = new StringBuffer();
		if(StringUtils.isNotBlank(prefix))
			buf.append(GeneralConstants.SLASH_SEPARATOR).append(prefix);
		
		buf.append(GeneralConstants.SLASH_SEPARATOR).append(hexStr.substring(8,10));
		buf.append(GeneralConstants.SLASH_SEPARATOR).append(hexStr.substring(10,12));
		buf.append(GeneralConstants.SLASH_SEPARATOR).append(hexStr.substring(12,14));
		buf.append(GeneralConstants.SLASH_SEPARATOR).append(hexStr.substring(14,16));
		
		buf.append(GeneralConstants.NAMES_SEPARATOR).append(binaryId);
		
		if(StringUtils.isNotBlank(postfix))
			buf.append(GeneralConstants.NAMES_SEPARATOR).append(postfix);
		
		return buf.toString();
	}

	/**
	 * convert binary id (long) into file path pattern string
	 * this method disperse the binary file to different path tree.<br>
	 * e.g 367 -> /80/00/01/6f.367
	 * 
	 * @param binaryId the id the binary record
	 * @return String the path key 
	 * 
	 **/
	public static String toPathKey(long binaryId){
				
		return toPathKey(binaryId, StringUtils.EMPTY,StringUtils.EMPTY);
	}
	
	/**
	 * convert binary id (long) into key pattern string<br>
	 * e.g 367 -> 8000016f.367
	 * 
	 * @param binaryId the id the binary record
	 * @return String the path key 
	 * 
	 **/
	public static String toPureKey(long binaryId){
		
		return toPureKey(binaryId, StringUtils.EMPTY,StringUtils.EMPTY);
	}
	
	/**
	 * convert binary id (long) into key pattern string<br>
	 * e.g 367 -> 8000016f.367
	 * 
	 * @param binaryId the id the binary record
	 * @return String the path key 
	 * 
	 **/
	public static String toPureKey(long binaryId, String prefix,String postfix){
		
		byte[] tbytes = ByteUtils.toBytes(ID_OFFSET + binaryId);
		
		String hexStr = ByteString.of(tbytes).hex();
		
		StringBuffer buf = new StringBuffer();
		if(StringUtils.isNotBlank(prefix))
			buf.append(prefix);
		buf.append(hexStr.substring(8));
		
		buf.append(GeneralConstants.NAMES_SEPARATOR).append(binaryId);
		
		if(StringUtils.isNotBlank(postfix))
			buf.append(GeneralConstants.NAMES_SEPARATOR).append(postfix);
		
		return buf.toString();
	}
	
	/**
	 * convert path pattern key into key pattern string<br>
	 * e.g  /80/00/01/6f.367 -> 8000016f.367
	 * 
	 * @param pathKey the path pattern key
	 * @return String the path key 
	 * 
	 **/
	public static String toPureKey(String pathKey){
		
		if(StringUtils.isBlank(pathKey)) return StringUtils.EMPTY;		
		return StringUtils.replace(pathKey, "/", "");
	}
	
	/**
	 * build uri pattern binary location string<br>
	 * e.g  gbin://STORAGE:123/80/00/01/6f.367
	 * 
	 * @param storageKey key of storage
	 * @param binaryId the id of binary record
	 * @return String uri string 
	 **/
	public static String toURIStr(InfoId<Integer> storageId, long binaryId){

		return toURIStr(storageId, binaryId, StringUtils.EMPTY, StringUtils.EMPTY);
	}
	
	/**
	 * Convert the storage id and binaryId and prefix and postfix
	 * into the URI format string
	 **/
	public static String toURIStr(InfoId<Integer> storageId, long binaryId, String prefix, String postfix){
		
		StringBuffer buf = new StringBuffer();
		buf.append(GeneralConstants.GP_SCHEME).append("://")
			.append(storageId.toString())
			.append(toPathKey(binaryId,prefix, postfix));
		
		return buf.toString();
	}
	
	/**
	 * build uri pattern binary location string<br>
	 * e.g  gbin://STORAGE:123/80/00/01/6f.367
	 * 
	 * @param storageKey key of storage
	 * @param pathKey the key of path
	 * @return String uri string 
	 **/
	public static String toURIStr(InfoId<Integer> storageId, String pathKey){
		
		StringBuffer buf = new StringBuffer();
		buf.append(GeneralConstants.GP_SCHEME).append("://")
			.append(storageId.toString())
			.append(pathKey);
		
		return buf.toString();
	}

}
