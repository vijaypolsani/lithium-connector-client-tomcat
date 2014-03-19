package com.lithium.integrations.transformations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.registry.RegistrationException;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.lithium.integrations.AuthorInformation;
import com.lithium.integrations.Avatar;
import com.lithium.integrations.AuthorInformation.User;
import com.lithium.integrations.AuthorInformation.User.Profiles;
import com.lithium.integrations.AuthorInformation.User.Profiles.Profile;
import com.lithium.integrations.RecentTopics;
import com.lithium.integrations.RecentTopics.Messages;
import com.lithium.integrations.RecentTopics.Messages.Message;
import com.lithium.integrations.model.AuthorProfileCombined;

public class PrepareMessageIdCollection extends AbstractMessageTransformer {
	Map<String, String> inputParams;
	ObjectMapper mapper = new ObjectMapper();

	public List<String> prepareMessageIdList(String jsonStringAuthor) {
		//mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//Response authroResponse = mapper.readValue(jsonStringAvator, Response.class);
		JAXBContext jaxbContext;
		List<String> messageIds = new ArrayList<String>();

		try {
			jaxbContext = JAXBContext.newInstance(RecentTopics.class);
			Unmarshaller jaxbUnmarshallerAuthor = jaxbContext.createUnmarshaller();
			RecentTopics recentTopics = (RecentTopics) jaxbUnmarshallerAuthor.unmarshal(new ByteArrayInputStream(
					jsonStringAuthor.getBytes()));
			List<Message> msgs = recentTopics.getMessages().getMessage();
			for (Message msg : msgs) {
				messageIds.add(String.valueOf(msg.getBoardId().getValue()));
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("List of Message Ids: " + messageIds);
		return messageIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			//System.out.println("**** Input Message Data :" + message.getPayloadAsString());
			message.setPayload(prepareMessageIdList(message.getPayloadAsString()));
			//Use this for JSON output...
			//message.setPayload(mapper.writeValueAsString(authorAvatarCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
