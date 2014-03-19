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
import com.lithium.integrations.Rankings;
import com.lithium.integrations.model.AuthorProfileCombined;
import com.lithium.integrations.model.Solutions;

public class CombineDataFromCallsTxHtml extends AbstractMessageTransformer {
	Map<String, String> inputParams;
	ObjectMapper mapper = new ObjectMapper();

	public AuthorProfileCombined parseJsonBuildAuthor(String jsonStringAuthor, String jsonStringAvator,
			String jsonStringSolutions, String jsonStringRanking) {
		//mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//Response authroResponse = mapper.readValue(jsonStringAvator, Response.class);
		JAXBContext jaxbContextAuthor;
		JAXBContext jaxbContextAvatar;
		JAXBContext jaxbContextSolutions;
		JAXBContext jaxbContextRanking;

		AuthorProfileCombined authorProfileCombined = new AuthorProfileCombined();
		try {
			jaxbContextAuthor = JAXBContext.newInstance(AuthorInformation.class);
			Unmarshaller jaxbUnmarshallerAuthor = jaxbContextAuthor.createUnmarshaller();
			AuthorInformation response = (AuthorInformation) jaxbUnmarshallerAuthor.unmarshal(new ByteArrayInputStream(
					jsonStringAuthor.getBytes()));
			authorProfileCombined.setHref(response.getUser().getHref());
			authorProfileCombined.setAnonymous(response.getUser().getAnonymous().getValue());
			authorProfileCombined.setId(response.getUser().getId().getValue());
			authorProfileCombined.setLast_visit_time(response.getUser().getLastVisitTime().getValue().toString());
			authorProfileCombined.setLogin(response.getUser().getLogin().getValue());
			authorProfileCombined.setAverage_rating(response.getUser().getAverageRating().getValue());
			authorProfileCombined.setAverage_message_rating(response.getUser().getAverageMessageRating().getValue());
			authorProfileCombined.setRegistration_time(response.getUser().getRegistrationTime().getValue().toString());

			List<Profile> profile = response.getUser().getProfiles().profile;
			for (int i = 0; i < profile.size(); i++) {
				if (profile.get(i).getName().equalsIgnoreCase("accept_private_notes_consent_agreement"))
					authorProfileCombined.setAccept_private_notes_consent_agreement(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("autosave_interval"))
					authorProfileCombined.setAutosave_interval(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.delivery_enable"))
					authorProfileCombined.setBadge_delivery_enable(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("badge.email_delivery_freq"))
					authorProfileCombined.setBadge_email_delivery_freq(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("biography"))
					authorProfileCombined.setBiography(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_orientation"))
					authorProfileCombined.setBlogger_badge_orientation(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_avatar"))
					authorProfileCombined.setBlogger_badge_show_avatar(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_blogs"))
					authorProfileCombined.setBlogger_badge_show_blogs(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_ideas"))
					authorProfileCombined.setBlogger_badge_show_ideas(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_kudos"))
					authorProfileCombined.setBlogger_badge_show_kudos(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_post_count"))
					authorProfileCombined.setBlogger_badge_show_post_count(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_rank"))
					authorProfileCombined.setBlogger_badge_show_rank(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.delivery_enable"))
					authorProfileCombined.setBoard_delivery_enable(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("board.sub_email_delivery_freq"))
					authorProfileCombined.setBoard_sub_email_delivery_freq(profile.get(i).getValue());
				if (profile.get(i).getName().equalsIgnoreCase("blogger_badge_show_accepted_solutions"))
					authorProfileCombined.setBlogger_badge_show_accepted_solutions(profile.get(i).getValue());

			}

			jaxbContextAvatar = JAXBContext.newInstance(Avatar.class);
			Unmarshaller jaxbUnmarshallerAvatar = jaxbContextAvatar.createUnmarshaller();
			Avatar avatar = (Avatar) jaxbUnmarshallerAvatar.unmarshal(new ByteArrayInputStream(jsonStringAvator
					.getBytes()));
			authorProfileCombined.setImage(avatar.getImage().getHref());
			authorProfileCombined.setUrl(avatar.getImage().getUrl().getValue());

			jaxbContextSolutions = JAXBContext.newInstance(Solutions.class);
			Unmarshaller jaxbUnmarshallerSolutions = jaxbContextSolutions.createUnmarshaller();
			Solutions solutions = (Solutions) jaxbUnmarshallerSolutions.unmarshal(new ByteArrayInputStream(
					jsonStringSolutions.getBytes()));
			authorProfileCombined.setSolutionsReceivedCount(solutions.getValue());

			jaxbContextRanking = JAXBContext.newInstance(Rankings.class);
			Unmarshaller jaxbUnmarshallerRanking = jaxbContextRanking.createUnmarshaller();
			Rankings rankings = (Rankings) jaxbUnmarshallerRanking.unmarshal(new ByteArrayInputStream(jsonStringRanking
					.getBytes()));
			authorProfileCombined.setAuthorRankingRole(rankings.getRanking().getName().getValue());
			authorProfileCombined.setAuthorRanking(rankings.getRanking().getId().getValue());

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return authorProfileCombined;

	}

	public static String wrapHtmlBody(AuthorProfileCombined authorProfileCombineddy) {
		String output = "";
		String ONE_PRE = "<tr><td>";
		String TWO_PRE = "</td><td>";
		String THREE_POST = "</td></tr>";
		String END = "</table>";

		String IMG_1 = "<img src='";
		String IMG_2 = "'>";

		output += "<style type='text/css'>.tftable {font-size:12px;color:#333333;width:35%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}.tftable th {font-size:12px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}.tftable tr {background-color:#d4e3e5;}.tftable td {font-size:14px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}.tftable tr:hover {background-color:#ffffff;}</style><html><body><H3 align='center'>Author Profile</H3><table class='tftable' border='1' align='center'><tr><th>Author Info</th><th>Community Settings</th></tr>";
		output += ONE_PRE + "login" + TWO_PRE + authorProfileCombineddy.getLogin() + THREE_POST;
		output += ONE_PRE + "ID" + TWO_PRE + authorProfileCombineddy.getId() + THREE_POST;
		//Avatar
		output += ONE_PRE + "Image" + TWO_PRE + IMG_1 + authorProfileCombineddy.getUrl() + IMG_2 + THREE_POST;
		output += ONE_PRE + "Url" + TWO_PRE + authorProfileCombineddy.getImage() + THREE_POST;

		output += ONE_PRE + "anonymous" + TWO_PRE + authorProfileCombineddy.getAnonymous() + THREE_POST;
		output += ONE_PRE + "Href" + TWO_PRE + authorProfileCombineddy.getHref() + THREE_POST;
		//Ranking
		output += ONE_PRE + "Ranking" + TWO_PRE + authorProfileCombineddy.getAuthorRanking() + THREE_POST;
		output += ONE_PRE + "Ranking Role" + TWO_PRE + authorProfileCombineddy.getAuthorRankingRole() + THREE_POST;
		//Solutions
		output += ONE_PRE + "Solutions Received Count" + TWO_PRE + authorProfileCombineddy.getSolutionsReceivedCount()
				+ THREE_POST;

		output += ONE_PRE + "average_message_rating" + TWO_PRE + authorProfileCombineddy.getAverage_message_rating()
				+ THREE_POST;
		output += ONE_PRE + "average_rating" + TWO_PRE + authorProfileCombineddy.getAverage_rating() + THREE_POST;
		output += ONE_PRE + "accept_private_notes_consent_agreement" + TWO_PRE
				+ authorProfileCombineddy.getAccept_private_notes_consent_agreement() + THREE_POST;
		output += ONE_PRE + "autosave_interval" + TWO_PRE + authorProfileCombineddy.getAutosave_interval() + THREE_POST;
		output += ONE_PRE + "badge_delivery_enable" + TWO_PRE + authorProfileCombineddy.getBadge_delivery_enable()
				+ THREE_POST;
		output += ONE_PRE + "badge_email_delivery_freq" + TWO_PRE
				+ authorProfileCombineddy.getBadge_email_delivery_freq() + THREE_POST;
		output += ONE_PRE + "biography" + TWO_PRE + authorProfileCombineddy.getBiography() + THREE_POST;
		output += ONE_PRE + "blogger_badge_orientation" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_orientation() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_accepted_solutions" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_accepted_solutions() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_avatar" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_avatar() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_blogs" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_blogs() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_ideas" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_ideas() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_kudos" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_kudos() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_post_count" + TWO_PRE
				+ authorProfileCombineddy.getBlogger_badge_show_post_count() + THREE_POST;
		output += ONE_PRE + "blogger_badge_show_rank" + TWO_PRE + authorProfileCombineddy.getBlogger_badge_show_rank()
				+ THREE_POST;
		output += ONE_PRE + "board_delivery_enable" + TWO_PRE + authorProfileCombineddy.getBoard_delivery_enable()
				+ THREE_POST;
		output += ONE_PRE + "board_sub_email_delivery_freq" + TWO_PRE
				+ authorProfileCombineddy.getBoard_sub_email_delivery_freq() + THREE_POST;
		output += ONE_PRE + "last_visit_time" + TWO_PRE + authorProfileCombineddy.getLast_visit_time() + THREE_POST;
		output += ONE_PRE + "registration_time" + TWO_PRE + authorProfileCombineddy.getRegistration_time() + THREE_POST;
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
			//System.out.println("**** Data 1:" + list.get(0).toString().trim());
			//System.out.println("**** Data 2:" + list.get(1).toString().trim());
			AuthorProfileCombined authorProfileCombined = parseJsonBuildAuthor((String) list.get(0),
					(String) list.get(1), (String) list.get(2), (String) list.get(3));
			message.setPayload(wrapHtmlBody(authorProfileCombined));
			//Use this for JSON output...
			//message.setPayload(mapper.writeValueAsString(authorProfileCombined));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
