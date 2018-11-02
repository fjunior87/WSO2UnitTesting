package com.xicojunior.wso2.unittest.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xicojunior.wso2.unittest.bean.Payload;

public class JsonParser {
	
	/**
	 * 
	 * @param payloadString
	 * @return
	 */
	public Payload getPayload(String payloadString) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(payloadString, Payload.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param payload
	 * @return
	 */
	public String getPayloadString(Payload payload) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(payload);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
