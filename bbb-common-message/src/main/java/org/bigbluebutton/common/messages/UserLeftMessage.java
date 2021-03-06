package org.bigbluebutton.common.messages;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserLeftMessage implements ISubscribedMessage {
	public static final String USER_LEFT = "user_left_message";
	public final String VERSION = "0.0.1";

	public final String meetingId;
	public final Map<String, Object> user;

	public UserLeftMessage(String meetingID, Map<String, Object> user) {
		this.meetingId = meetingID;
		this.user = user;
	}

	public String toJson() {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put(Constants.MEETING_ID, meetingId);

		java.util.HashMap<String, Object> header = MessageBuilder.buildHeader(USER_LEFT, VERSION, null);

		return MessageBuilder.buildJson(header, payload);				
	}
	public static UserLeftMessage fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);

		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");

			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (USER_LEFT.equals(messageName)) {
					if (payload.has(Constants.MEETING_ID)
							&& payload.has(Constants.USER)) {
						String meetingID = payload.get(Constants.MEETING_ID).getAsString();
						
						JsonObject user = (JsonObject) payload.get(Constants.USER);
						
						Util util = new Util();
						Map<String, Object> userMap = util.extractUser(user);
						
						if (userMap != null) {
							return new UserLeftMessage(meetingID, userMap);							
						}						
					}
				}
			}
		}

		return null;
	}
}
