package com.xicojunior.wso2.unittest.bean;
/**
 * 
 * @author francisco.ribeiro
 *
 */
public class Property {

	private String name;
	
	private String value;
	
	private String type = "STRING";
	
	private String scope;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
	
	
}
