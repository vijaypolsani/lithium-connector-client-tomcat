package com.lithium.integrations.transformations;

import java.util.Map;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.registry.RegistrationException;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class HttpVariablesTx extends AbstractMessageTransformer {
	Map<String, String> inputParams;

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			System.out.println("**** Message Payload Information Keys: " + message.getPayloadAsString());
			inputParams = (Map<String, String>) message.getPayload();
			System.out.println("**** Message Payload Information Keys: " + inputParams.keySet().toString());
			System.out.println("**** Message Payload Information Values: " + inputParams.values().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MuleContext muleContext = message.getMuleContext();
		try {
			muleContext.getRegistry().registerObject("boardname", inputParams.get("boardname"));
		} catch (RegistrationException e) {
			e.printStackTrace();
		}
		return message;
	}
}
