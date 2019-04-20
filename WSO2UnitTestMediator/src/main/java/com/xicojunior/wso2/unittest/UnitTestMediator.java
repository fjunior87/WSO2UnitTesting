package com.xicojunior.wso2.unittest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.builtin.PropertyMediator;

import com.xicojunior.wso2.unittest.bean.Payload;
import com.xicojunior.wso2.unittest.bean.Property;
import com.xicojunior.wso2.unittest.json.JsonParser;

/**
 * 
 * @author francisco.ribeiro
 */
public class UnitTestMediator extends AbstractMediator {

	private static final String POST_PROCESS_ACTION = "POST_PROCESS";
	private static final String PRE_PROCESS_ACTION = "PRE_PROCESS";
	private static final String PAYLOAD_TYPE = "TEST_PAYLOAD_TYPE";
	private static final String TEST_SEQUENCE_NAME = "TEST_SEQUENCE_NAME";
	private static final String ACTION = "TEST_ACTION";
	private static final String REQUEST_PAYLOAD = "TEST_REQUEST_PAYLOAD";

	private static final String PROPS_TO_REMOVE = "SYNAPSE_RESOURCE," + "TRANSPORT_IN_NAME," + "REST_API_CONTEXT,"
			+ "JSON_OBJECT" + "IsClientDoingSOAP11," + "REST_FULL_REQUEST_PATH," + "rest.url.pattern," + "REST_METHOD,"
			+ "SYNAPSE_REST_API," + "IsClientDoingREST," + "SYNAPSE_REST_API_VERSION," + "__SYNAPSE_RESPONSE_STATE__,"
			+ "SYNAPSE_REST_API_VERSION_STRATEGY," + "IsClientDoingREST," + "REST_SUB_REQUEST_PATH,"
			+ "REST_URL_PREFIX";
	

	private static final String XML = "xml";
	private static final String JSON = "json";

	/**
	 * (non-Javadoc)
	 * @see org.apache.synapse.Mediator#mediate(org.apache.synapse.MessageContext)
	 */
	public boolean mediate(MessageContext context) {

		JsonParser jsonParser = new JsonParser();
		String action = context.getProperty(ACTION).toString();
		Payload payload = null;
		if (action.equals(PRE_PROCESS_ACTION)) {
			String payloadString = getJSONPayload(context);
			context.setProperty(REQUEST_PAYLOAD, payloadString);
			payload = jsonParser.getPayload(payloadString);
			context.setProperty(PAYLOAD_TYPE, payload.getRequestType());
			context.setProperty(TEST_SEQUENCE_NAME, payload.getSequences().get(0));

			addProperties(context, payload.getProperties());
			if(StringUtils.isNotEmpty(payload.getPayload())) {
				if (JSON.equals(payload.getRequestType())) {
					try {
						setJSONPayload(context, payload.getPayload());
					} catch (AxisFault e) {
						log.error("Error While Setting the JSON Payload", e);
						return true;
					}
				}
				if (XML.equals(payload.getRequestType())) {
					try {
						System.out.println(payload.getPayload());
						setXMLPayload(context, payload.getPayload());
					} catch (XMLStreamException e) {
						log.error("Error While Setting the XML Payload", e);
						return true;
					}
				}

			}
			
		}
		if (action.equals(POST_PROCESS_ACTION)) {
			String jsonRequestPayload = context.getProperty(REQUEST_PAYLOAD).toString();
			payload = jsonParser.getPayload(jsonRequestPayload);
			payload.setProperties(getResponseProperties(context));

			if (JSON.equals(payload.getResponseType())) {
				payload.setPayload(getJSONPayload(context));
			}
			if (XML.equals(payload.getResponseType())) {
				payload.setPayload(context.getEnvelope().getBody().getFirstElement().toString());
			}
			try {
				setJSONPayload(context, jsonParser.getPayloadString(payload));
			} catch (AxisFault e) {
				log.error("Error While Setting the JSON Response Payload", e);

			}
		}

		return true;
	}

	/**
	 * Method to return the JSON Payload
	 * 
	 * @param context
	 *            - messageContext
	 * @return jsonPayload - String
	 */
	public String getJSONPayload(MessageContext context) {
		return JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	}

	/**
	 * 
	 * @param messageContext
	 * @param jsonPayload
	 * @throws AxisFault
	 */
	public void setJSONPayload(MessageContext messageContext, String jsonPayload) throws AxisFault {
		JsonUtil.getNewJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext(), jsonPayload, true,
				true);
	}

	public void setXMLPayload(MessageContext messageContext, String xmlPayload) throws XMLStreamException {

		JsonUtil.removeJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext());
		OMElement omXML = AXIOMUtil.stringToOM(xmlPayload);
		System.out.println("omXML" + omXML.toString());
		messageContext.getEnvelope().getBody().addChild(omXML);
		System.out.println(messageContext.getEnvelope());

	}

	/*
	 * 
	 */
	private void addProperties(MessageContext messageContext, List<Property> properties) {
		if (!CollectionUtils.isEmpty(properties)) {
			for (Property prop : properties) {
				PropertyMediator propMediator = new PropertyMediator();
				propMediator.setName(prop.getName());
				propMediator.setValue(prop.getValue(), prop.getType());
				propMediator.setScope(prop.getScope());
				propMediator.setAction(PropertyMediator.ACTION_SET);
				propMediator.mediate(messageContext);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<Property> getResponseProperties(MessageContext messageContext) {
		List<Property> properties = new ArrayList<Property>();
		Set<String> propKeySet = messageContext.getPropertyKeySet();
		
		for (String propKey : propKeySet) {
			if (!shouldRemove(propKey)) {
				Property property = new Property();
				property.setName(propKey);
				property.setValue(messageContext.getProperty(propKey).toString());
				property.setScope("default");
				properties.add(property);
			}
		}
		
		//Add Operation Scope Properties
		properties.addAll(getOperationScopeProperties(messageContext));
		return properties;
	}
	
	/*
	 * 
	 */
	private List<Property> getOperationScopeProperties(MessageContext messageContext) {
		Axis2MessageContext axis2MsgCtx = ((Axis2MessageContext) messageContext); 
		Iterator<String> propNames = axis2MsgCtx.getAxis2MessageContext().getOperationContext().getPropertyNames();
		List<Property> operationProperties = new ArrayList<Property>();
		while(propNames.hasNext()) {
			String propKey = propNames.next(); 
			Property property = new Property();
			property.setName(propKey);
			property.setValue(axis2MsgCtx.getAxis2MessageContext().getOperationContext().getProperty(propKey).toString());
			property.setScope("operation");
			operationProperties.add(property);
		}
		return operationProperties;
	}

	private boolean shouldRemove(String propKey) {
		if (propKey.indexOf("TEST_") >= 0) {
			return true;
		}
		return PROPS_TO_REMOVE.indexOf(propKey) >= 0;
	}

}
