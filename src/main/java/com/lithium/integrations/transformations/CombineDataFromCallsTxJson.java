package com.lithium.integrations.transformations;

import java.io.ByteArrayInputStream;
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
import com.lithium.integrations.AuthorInformation.User.Profiles.Profile;
import com.lithium.integrations.model.AuthorAvatarCombined;

public class CombineDataFromCallsTxJson extends AbstractMessageTransformer {
	Map<String, String> inputParams;
	ObjectMapper mapper = new ObjectMapper();

	public AuthorAvatarCombined parseJsonBuildAuthor(String jsonStringAuthor, String jsonStringAvator) {
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
			authorAvatarCombined.setHref(response.getUser().getHref());
			authorAvatarCombined.setAnonymous(response.getUser().getAnonymous().getValue());
			authorAvatarCombined.setId(response.getUser().getId().getValue());
			authorAvatarCombined.setLast_visit_time(response.getUser().getLastVisitTime().getValue().toString());
			authorAvatarCombined.setLogin(response.getUser().getLogin().getValue());
			authorAvatarCombined.setAverage_rating(response.getUser().getAverageRating().getValue());
			authorAvatarCombined.setAverage_message_rating(response.getUser().getAverageMessageRating().getValue());
			authorAvatarCombined.setRegistration_time(response.getUser().getRegistrationTime().getValue().toString());

			List<Profile> profile = response.getUser().getProfiles().profile;
			for (int i = 0; i < profile.size(); i++) {
				if (profile.get(i).getName().equalsIgnoreCase("accept_private_notes_consent_agreement"))
					authorAvatarCombined.setAccept_private_notes_consent_agreement(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("autosave_interval"))
					authorAvatarCombined.setAutosave_interval(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.delivery_enable"))
					authorAvatarCombined.setBadge_delivery_enable(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.email_delivery_freq"))
					authorAvatarCombined.setBadge_email_delivery_freq(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("biography"))
					authorAvatarCombined.setBiography(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_orientation"))
					authorAvatarCombined.setBlogger_badge_orientation(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_avatar"))
					authorAvatarCombined.setBlogger_badge_show_avatar(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_blogs"))
					authorAvatarCombined.setBlogger_badge_show_blogs(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_ideas"))
					authorAvatarCombined.setBlogger_badge_show_ideas(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_kudos"))
					authorAvatarCombined.setBlogger_badge_show_kudos(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_post_count"))
					authorAvatarCombined.setBlogger_badge_show_post_count(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_rank"))
					authorAvatarCombined.setBlogger_badge_show_rank(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.delivery_enable"))
					authorAvatarCombined.setBoard_delivery_enable(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.sub_email_delivery_freq"))
					authorAvatarCombined.setBoard_sub_email_delivery_freq(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_accepted_solutions"))
					authorAvatarCombined.setBlogger_badge_show_accepted_solutions(profile.get(i).getValue());

			}

			jaxbContextAvatar = JAXBContext.newInstance(Avatar.class);
			Unmarshaller jaxbUnmarshaller = jaxbContextAvatar.createUnmarshaller();
			Avatar avatar = (Avatar) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(jsonStringAvator.getBytes()));
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
			//System.out.println("**** Data 1:" + list.get(0).toString().trim());
			//System.out.println("**** Data 2:" + list.get(1).toString().trim());
			AuthorAvatarCombined authorAvatarCombined = parseJsonBuildAuthor((String) list.get(0), (String) list.get(1));
			//message.setPayload(wrapHtmlBody(authorAvatarCombined));
			//Use this for JSON output...
			message.setPayload(mapper.writeValueAsString(authorAvatarCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
