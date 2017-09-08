package com.gp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	public static ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	static {
		JSON_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	

	private static final String[] SI_UNITS = { "B", "KB", "MB", "GB", "TB", "PB", "EB" };
	private static final String[] BINARY_UNITS = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB" };

	public static String humanReadableByteCount(final long bytes, final boolean useSIUnits)
	{
	    final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
	    final int base = useSIUnits ? 1000 : 1024;

	    // When using the smallest unit no decimal point is needed, because it's the exact number.
	    if (bytes < base) {
	        return bytes + " " + units[0];
	    }

	    final int exponent = (int) (Math.log(bytes) / Math.log(base));
	    final String unit = units[exponent];
	    return String.format("%.1f %s", bytes / Math.pow(base, exponent), unit);
	}
	
	public static String humanReadableByteCount(final long bytes)
	{

		return humanReadableByteCount(bytes, false);
	}

	/**
	 * Convert a set into a Json String
	 **/
	public static String toJson(Set<?> set){
		if(CollectionUtils.isEmpty(set))
			return "[]";
		try {
			return JSON_MAPPER.writeValueAsString(set);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert Set<String> perm to String", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Convert a Json array String into Set
	 **/
	public static <T> Set<T> toSet(String setJson, Class<T> clazzz){
		if(StringUtils.isBlank(setJson))
			return new HashSet<T>();

		try {
			return JSON_MAPPER.readValue(setJson, new TypeReference<Set<T>>(){});
		} catch ( IOException e) {
			LOGGER.error("Fail convert Set<String> setJson to String", e);
		}
		return new HashSet<T>();
	}

	/**
	 * Convert a Json Object String into Map
	 **/
	public static String toJson(Map<String, Object> propmap){
		if(null == propmap)
			return "{}";
		try {
			return JSON_MAPPER.writeValueAsString(propmap);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert Map<String, Object> propmap to String", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Convert a Json Object String into Map
	 **/
	public static <T> String toJson(Map<String, T> propmap, Class<T> T){
		if(null == propmap)
			return "{}";
		try {
			return JSON_MAPPER.writeValueAsString(propmap);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert Map<String, Object> propmap to String", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Convert json object string into map
	 **/
	public static <T>  Map<String, T> toMap(String props, Class<T> T){
		if(StringUtils.isBlank(props))
			return new HashMap<String,T>();

		try {
			return JSON_MAPPER.readValue(props, new TypeReference<Map<String, T>>(){});
		} catch ( IOException e) {
			LOGGER.error("Fail convert Json string to Map<String, Object> propmap", e);
		}
		return new HashMap<String,T>();
	}

	
	/**
	 * Convert a Json Object String into Map
	 **/
	public static String toJson(List<?> list){
		if(CollectionUtils.isEmpty(list))
			return "{}";
		try {
			return JSON_MAPPER.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fail convert List<?> list to String", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Convert json object string into list
	 **/
	public static <T> List<T> toList(String listJson, Class<T> clazz){
		if(StringUtils.isBlank(listJson))
			return new ArrayList<T>();

		try {
			return JSON_MAPPER.readValue(listJson, new TypeReference<List<T>>(){});
		} catch ( IOException e) {
			LOGGER.error("Fail convert Json string to List< Object> listJson", e);
		}
		return new ArrayList<T>();
	}
	
	/**
	 * Convert json object string into map
	 **/
	public static <T>  Map<String, T> toMap(Object bean, Class<T> T){
		if(null == bean)
			return new HashMap<String,T>();

		try {
			return JSON_MAPPER.convertValue(bean, new TypeReference<Map<String, T>>(){});
		} catch ( Exception e) {
			LOGGER.error("Fail convert Bean to Map<String, Object> propmap", e);
		}
		return new HashMap<String,T>();
	}
}
