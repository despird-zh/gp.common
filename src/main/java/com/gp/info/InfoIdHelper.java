package com.gp.info;

import java.io.IOException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gp.common.GeneralConstants;

public class InfoIdHelper {

	/**
	 * Make ObjectMapper support the InfoId module 
	 * @param mapper
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectMapper withInfoIdModule(final ObjectMapper mapper) {
		
		final SimpleModule module = new SimpleModule("InfoIdSerializeModule");
		
		JsonSerializer<InfoId> serializer = new InfoIdSerializer();
		JsonDeserializer deserializer = new InfoIdDeserializer();
	    module.addDeserializer(InfoId.class, deserializer);
	    module.addSerializer(InfoId.class, serializer);
	    
	    mapper.registerModule(module);
		return mapper;
	}
	
	/**
	 * The serializer to support InfoId 
	 **/
	@SuppressWarnings("rawtypes")
	public static class InfoIdSerializer extends JsonSerializer<InfoId>{

		@Override
		public void serialize(InfoId arg0, JsonGenerator jsonGenerator, SerializerProvider arg2)
				throws IOException, JsonProcessingException {
			
			String idType = arg0.getId().getClass().getName();
			jsonGenerator.writeString(idType + GeneralConstants.KEYS_SEPARATOR + arg0.toString());
		}
	}
	
	/**
	 * the deserializer to support InfoId
	 **/
	@SuppressWarnings("rawtypes")
	public static class InfoIdDeserializer extends JsonDeserializer<InfoId>{

		@Override
		public InfoId deserialize(JsonParser parser, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			String fullStr = parser.getValueAsString();
			int idx  = fullStr.indexOf(GeneralConstants.KEYS_SEPARATOR );
			String type = fullStr.substring(0, idx);
			InfoId<?> rtv = null;
			try {
				Class<?> clazz = Class.forName(type);
				rtv = InfoIdHelper.parseInfoId(fullStr.substring(idx + 1), clazz);
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				
			}
			return rtv;
		}
		
	}

	/**
	 * parse the string into InfoId object, format is {type}:{id} ,e.g audit:a000101 
	 * 
	 * @param idstr the id string get from InfoId.toString()
	 * @param idclazz the class of Id
	 **/
	@SuppressWarnings("unchecked")
	public static <M> InfoId<M> parseInfoId(String idstr, Class<M> idclazz){
		
		if(StringUtils.isBlank(idstr)) return null;
		
		String[] parts = StringUtils.split(idstr, GeneralConstants.KEYS_SEPARATOR);
		
		M id = null;
		if(parts.length < 3){
			
			if(Integer.class.equals(idclazz))
				id = (M)new Integer(-1);
			
			else if(Long.class.equals(idclazz))
				id = (M)new Long(-1);
			
			else if(String.class.equals(idclazz))
				id = (M)new String();
			
			return new InfoId<M>(parts[0], id);
		}
		
		if(parts.length == 3 ){
			
			if(Integer.class.equals(idclazz))
				id = (M)Integer.valueOf(parts[2]);
			
			else if(Long.class.equals(idclazz))
				id = (M)Long.valueOf(parts[2]);
			
			else if(String.class.equals(idclazz))
				id = (M)parts[2];
			
		}
				
		return new InfoId<M>(parts[0], parts[1], id);	
	}

	/**
	 * check if the InforId is valid 
	 * @param the id object to be checked
	 * @return true valid; false invalid
	 **/
	public static boolean isValid(InfoId<?> id){
		
		if(id == null){ 
			
			return false;
		}else if(id.getId() == null ){	
			
			return false;
			
		}else if(ObjectUtils.equals(id.getId(), GeneralConstants.LOCAL_SOURCE )||
				ObjectUtils.equals(id.getId(), GeneralConstants.PERSONAL_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_ROOT) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.FOLDER_ROOT) ){
			
			return true;
			
		}else if( id.getId() instanceof Integer && (Integer)(id.getId()) < 1){
			
			return false;
		}else if( id.getId() instanceof Long && (Long)(id.getId()) < 1){
			
			return false;
		}
		
		return true;
	}
}
