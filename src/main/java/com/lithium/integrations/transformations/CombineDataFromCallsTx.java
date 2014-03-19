package com.lithium.integrations.transformations;

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

import com.lithium.integrations.AuthorInformation;
import com.lithium.integrations.Avatar;
import com.lithium.integrations.AuthorInformation.User;
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
			System.out.println("URL:" + avatar.getImage().getUrl().getValue());
			authorAvatarCombined.setImage(avatar.getImage().getHref());
			authorAvatarCombined.setUrl(avatar.getImage().getUrl().getValue());

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return authorAvatarCombined;

	}

	public static String wrapHtmlBody(AuthorAvatarCombined authorAvatarCombineddy) {
		String output = "";
		String ONE_PRE = "<tr><td>";
		String TWO_PRE = "</td><td>";
		String THREE_POST = "</td></tr>";
		String END = "</table>";

		String IMG_1 = "<img src='";
		String IMG_2 = "'>";

		output += "<style type='text/css'>.tftable {font-size:12px;color:#333333;width:35%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}.tftable th {font-size:18px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}.tftable tr {background-color:#d4e3e5;}.tftable td {font-size:16px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}.tftable tr:hover {background-color:#ffffff;}</style><html><body><H1 align='center'>Author Profile</H1><table class='tftable' border='1' align='center'><tr><th>Author Info</th><th>Community Settings</th></tr>";
		output += ONE_PRE +"login" +TWO_PRE+authorAvatarCombineddy.getLogin() + THREE_POST;
		output += ONE_PRE +"ID" +TWO_PRE+authorAvatarCombineddy.getId() + THREE_POST;
		output += ONE_PRE +"Image" +TWO_PRE+IMG_1+authorAvatarCombineddy.getUrl()+IMG_2 + THREE_POST;
		output += ONE_PRE +"Url" +TWO_PRE+authorAvatarCombineddy.getImage() + THREE_POST;
		output += ONE_PRE +"anonymous" +TWO_PRE+authorAvatarCombineddy.getAnonymous() + THREE_POST;
		output += ONE_PRE +"Href" +TWO_PRE+authorAvatarCombineddy.getHref() + THREE_POST;
		output += ONE_PRE +"average_message_rating" +TWO_PRE+authorAvatarCombineddy.getAverage_message_rating() + THREE_POST;
		output += ONE_PRE +"average_rating" +TWO_PRE+authorAvatarCombineddy.getAverage_rating() + THREE_POST;
		output += ONE_PRE +"accept_private_notes_consent_agreement" +TWO_PRE+authorAvatarCombineddy.getAccept_private_notes_consent_agreement() + THREE_POST;
		output += ONE_PRE +"autosave_interval" +TWO_PRE+authorAvatarCombineddy.getAutosave_interval() + THREE_POST;
		output += ONE_PRE +"badge_delivery_enable" +TWO_PRE+authorAvatarCombineddy.getBadge_delivery_enable()+ THREE_POST;
		output += ONE_PRE +"badge_email_delivery_freq" +TWO_PRE+authorAvatarCombineddy.getBadge_email_delivery_freq() + THREE_POST;
		output += ONE_PRE +"biography" +TWO_PRE+authorAvatarCombineddy.getBiography() + THREE_POST;
		output += ONE_PRE +"blogger_badge_orientation" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_orientation() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_accepted_solutions" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_accepted_solutions() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_avatar" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_avatar() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_blogs" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_blogs() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_ideas" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_ideas()+ THREE_POST;
		output += ONE_PRE +"blogger_badge_show_kudos" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_kudos() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_post_count" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_post_count() + THREE_POST;
		output += ONE_PRE +"blogger_badge_show_rank" +TWO_PRE+authorAvatarCombineddy.getBlogger_badge_show_rank() + THREE_POST;
		output += ONE_PRE +"board_delivery_enable" +TWO_PRE+authorAvatarCombineddy.getBoard_delivery_enable() + THREE_POST;
		output += ONE_PRE +"board_sub_email_delivery_freq" +TWO_PRE+authorAvatarCombineddy.getBoard_sub_email_delivery_freq() + THREE_POST;
		output += ONE_PRE +"last_visit_time" +TWO_PRE+authorAvatarCombineddy.getLast_visit_time() + THREE_POST;
		output += ONE_PRE +"registration_time" +TWO_PRE+authorAvatarCombineddy.getRegistration_time() + THREE_POST;
		output += END;
		output += "</body>";
		output += "</html>";

		return output;
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
			message.setPayload(wrapHtmlBody(authorAvatarCombined));
			//Use this for JSON output...
			//message.setPayload(mapper.writeValueAsString(authorAvatarCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
