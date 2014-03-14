package com.lithium.integrations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

import com.lithium.integrations.AuthorInformation.User.Profiles;
import com.lithium.integrations.AuthorInformation.User.Profiles.Profile;
import com.lithium.integrations.model.AuthorAvatarCombined;

public class CombineDataFromCallsTx extends AbstractMessageTransformer {
	Map<String, String> inputParams;
	ObjectMapper mapper = new ObjectMapper();

	public AuthorAvatarCombined AvaparseJsonBuildAuthor(String jsonStringAuthor, String jsonStringAvator) {
		//mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//Response authroResponse = mapper.readValue(jsonStringAvator, Response.class);
		JAXBContext jaxbContextAuthor;
		JAXBContext jaxbContextAvatar;
		AuthorAvatarCombined authorAvatarCombined = new AuthorAvatarCombined();

		try {
			jaxbContextAuthor = JAXBContext.newInstance(AuthorInformation.class);
			Unmarshaller jaxbUnmarshallerAuthor = jaxbContextAuthor.createUnmarshaller();
			AuthorInformation response = (AuthorInformation) jaxbUnmarshallerAuthor.unmarshal(new ByteArrayInputStream(
					jsonStringAuthor.getBytes()));

			System.out.println("HREF:" + response.getUser().getHref());
			authorAvatarCombined.setHref(response.getUser().getHref());
			authorAvatarCombined.setAnonymous(response.getUser().getAnonymous().getValue());
			authorAvatarCombined.setId(response.getUser().getId().getValue());
			authorAvatarCombined.setLast_visit_time(response.getUser().getLastVisitTime().getValue().toString());
			authorAvatarCombined.setLogin(response.getUser().getLogin().getValue());

			List<Profile> profile = response.getUser().getProfiles().profile;
			for (int i = 0; i < profile.size(); i++) {
				if (profile.get(i).getName().equalsIgnoreCase("accept_private_notes_consent_agreement"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("autosave_interval"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.delivery_enable"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.email_delivery_freq"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("biography"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_orientation"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_avatar"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_blogs"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_ideas"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_kudos"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_post_count"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_rank"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.delivery_enable"))
					authorAvatarCombined.setHref(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.sub_email_delivery_freq"))
					authorAvatarCombined.setHref(profile.get(i).getValue());

			}

			jaxbContextAvatar = JAXBContext.newInstance(Avatar.class);
			Unmarshaller jaxbUnmarshaller = jaxbContextAvatar.createUnmarshaller();
			Avatar avatar = (Avatar) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(jsonStringAvator.getBytes()));
			System.out.println("URL:" + avatar.getImage().getUrl().getValue());
			authorAvatarCombined.setImage(avatar.getImage().getHref());
			authorAvatarCombined.setUrl(avatar.getImage().getUrl().getValue());

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return authorAvatarCombined;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			List list = (List) message.getPayload();

			System.out.println("**** Data 1:" + list.get(0).toString().trim());
			System.out.println("**** Data 2:" + list.get(1).toString().trim());
			AuthorAvatarCombined authorAvatarCombined = AvaparseJsonBuildAuthor((String) list.get(0),
					(String) list.get(1));
			message.setPayload(mapper.writeValueAsString(authorAvatarCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
