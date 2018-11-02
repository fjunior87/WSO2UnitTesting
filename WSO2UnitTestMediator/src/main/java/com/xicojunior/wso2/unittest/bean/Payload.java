package com.xicojunior.wso2.unittest.bean;

import java.util.List;

/**
 * 
 * @author francisco.ribeiro
 *
 */
public class Payload {

	private String payload;
	private String type;
	private List<Property> properties;
	private List<String> sequences;
	
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public List<String> getSequences() {
		return sequences;
	}
	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

	
	
	
	
}
