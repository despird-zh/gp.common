package com.gp.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Use to deserializer field's raw data, the JsonParser's source could be String or byte[]
 * So it process this two kinds of input source.
 * 
 * @author gdiao
 * @version 0.1 2016-10-7
 * @version 0.2 2017-8-9 Add byte[] support
 * 
 **/
public class RawJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
           throws IOException, JsonProcessingException {
    		
    		if(jp.getCurrentLocation().getSourceRef() instanceof byte[]) {
    			long begin = jp.getCurrentLocation().getByteOffset();
    	        jp.skipChildren();
    	        long end = jp.getCurrentLocation().getByteOffset();
    	
    	        byte[] jsonSrc = (byte[])jp.getCurrentLocation().getSourceRef();
    	        byte[] jsonBytes = new byte[ (int) (end - begin + 1) ];
    	        System.arraycopy(jsonSrc, (int)begin -1, jsonBytes, 0, jsonBytes.length);
    	        return new String(jsonBytes);
    		}
	    	else if(jp.getCurrentLocation().getSourceRef() instanceof String) {
	        long begin = jp.getCurrentLocation().getCharOffset();
	        jp.skipChildren();
	        long end = jp.getCurrentLocation().getCharOffset();
	
	        String json = jp.getCurrentLocation().getSourceRef().toString();
	        return json.substring((int) begin - 1, (int) end);
    		}else {
    			return "";
    		}
    }
}
