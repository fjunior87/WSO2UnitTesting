package com.xicojunior.wso2.unittest.bean;

import java.util.List;

/**
 * 
 * @author francisco.ribeiro
 *
 */
public class Payload {

	private String payload;
	private String requestType;
	private String responseType;
	private List<Property> properties;
	private List<String> sequences;
	
	public String getPayload() {
		return payload;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
