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
import com.lithium.integrations.TopicMessage;
import com.lithium.integrations.model.AuthorAvatarCombined;

public class PrepareUserId extends AbstractMessageTransformer {
	Map<String, String> inputParams;
	ObjectMapper mapper = new ObjectMapper();

	public String getUserIdHref(String topicMessageInfo) {
		//mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//Response authroResponse = mapper.readValue(jsonStringAvator, Response.class);
		JAXBContext jaxbContext;
		String userIdHref = "";

		try {
			jaxbContext = JAXBContext.newInstance(TopicMessage.class);
			Unmarshaller jaxbUnmarshallerAuthor = jaxbContext.createUnmarshaller();
			TopicMessage topicMessage = (TopicMessage) jaxbUnmarshallerAuthor.unmarshal(new ByteArrayInputStream(
					topicMessageInfo.getBytes()));
			userIdHref = topicMessage.getMessage().getLastEditAuthor().getHref();
			userIdHref.substring(userIdHref.lastIndexOf('/'));

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("**** userId Parsed :" + userIdHref.substring(userIdHref.lastIndexOf('/')+1));
		return userIdHref.substring(userIdHref.lastIndexOf('/')+1);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			message.setPayload(getUserIdHref(message.getPayloadAsString()));
			//Use this for JSON output...
			//message.setPayload(mapper.writeValueAsString(authorAvatarCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
