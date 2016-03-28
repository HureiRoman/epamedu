package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.chat.ChatMessage;
import edu.epam.constants.RoleType;
import edu.epam.persistance.TimeToText;
import edu.epam.service.ChatMessageService;

@UserPermissions({ RoleType.STUDENT, RoleType.TEACHER })
public class GetChatMessagesWithOffsetCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		String offsetStr = request.getParameter("offset");
		String groupIdStr = request.getParameter("groupId");

		if (groupIdStr != null && offsetStr != null) {
			Integer offset = Integer.parseInt(offsetStr);
			Integer groupId = Integer.parseInt(groupIdStr);

			List<ChatMessage> messages = ChatMessageService
					.getMessagesWithOffsetForGroup(groupId, offset,20);
			JsonArrayBuilder jsonArrayOfMessagesBuilder = Json
					.createArrayBuilder();
			for (ChatMessage chatMessage : messages) {
				JsonObject chatMessageJsonObject = Json.createObjectBuilder()
						.add("message", chatMessage.getMessage())
						.add("sender", chatMessage.getSender())
						.add("groupId", chatMessage.getGroupId())
						.add("received", TimeToText.timeToText(chatMessage.getReceived()))
						.build();
				jsonArrayOfMessagesBuilder.add(chatMessageJsonObject);
			}
			JsonArray jsonArrayOfMessages = jsonArrayOfMessagesBuilder.build();

			JsonObject arrayOfMessagesObject = Json.createObjectBuilder()
					.add("messages", jsonArrayOfMessages).build();
			return arrayOfMessagesObject.toString();
		}
		return "";

	}

}
