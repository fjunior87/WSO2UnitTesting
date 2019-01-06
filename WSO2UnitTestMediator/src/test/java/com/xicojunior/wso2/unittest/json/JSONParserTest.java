package com.xicojunior.wso2.unittest.json;

import org.junit.Test;
import static org.junit.Assert.*;
import com.xicojunior.wso2.unittest.bean.Payload;
import com.xicojunior.wso2.unittest.bean.Property;

/**
 * 
 * @author francisco.ribeiro
 *
 */
public class JSONParserTest {

	@Test
	public void testJsonParser() {
		String payload = "{\r\n    \"payload\" : \"<abc><text>123</text></abc>\",\r\n    \"requestType\": \"xml\",\r\n    \"properties\": [\r\n        {\r\n            \"name\":\"PROP1\",\r\n            \"value\": \"ABC\",\r\n            \"scope\": \"\",\r\n            \"type\":\"\"\r\n        }\r\n    ],    \"sequences\": [\r\n        \"SequenceForTest\"\r\n    ]\r\n}";
		Payload p = new JsonParser().getPayload(payload);
		
		assertNotNull("Payload Object Should not be null", p);
		assertEquals("Payload Type should be xml","xml", p.getRequestType());
		
		assertNotNull("Properties Should not be null", p.getProperties());
		
		assertEquals("Properties Size Should be 1", 1, p.getProperties().size());
		
		Property prop = p.getProperties().get(0);
		
		assertEquals("Property name should be PROP1", "PROP1", prop.getName());
		assertEquals("Property value should be ABC", "ABC", prop.getValue());
		
		assertNotNull("Sequences Should not be null", p.getSequences());
		assertEquals("Sequence Name should be SequenceForTest", "SequenceForTest", p.getSequences().get(0));
		
	}
	
	@Test
	public void testJsonWriteString() {
		
		String payload = "{\r\n    \"payload\" : \"<abc><text>123</text></abc>\",\r\n    \"requestType\": \"xml\",\r\n    \"properties\": [\r\n        {\r\n            \"name\":\"PROP1\",\r\n            \"value\": \"ABC\",\r\n            \"scope\": \"\",\r\n            \"type\":\"\"\r\n        }\r\n    ],\r\n    \r\n    \"sequences\": [\r\n        \"SequenceForTest\"\r\n    ]\r\n}";
		Payload p = new JsonParser().getPayload(payload);
		
		System.out.println(new JsonParser().getPayloadString(p));
		
		
	}
}
	

