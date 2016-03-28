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
import edu.epam.constants.RoleType;
import edu.epam.model.Message;
import edu.epam.persistance.TimeToText;
import edu.epam.service.PersonalMessageService;
@UserPermissions({ RoleType.ANY})
public class GetPersonalMessagesWithOffsetCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String offsetStr = request.getParameter("offset");
		String userIdStr = request.getParameter("userId");
		String collocutorIdStr = request.getParameter("collocutorId");
		if (userIdStr != null && offsetStr != null && collocutorIdStr!=null ) {
			Integer offset = Integer.parseInt(offsetStr);
			Integer userId = Integer.parseInt(userIdStr);
            Integer collocutorId  =  Integer.parseInt(request.getParameter("collocutorId"));
			List<Message> messages = PersonalMessageService
					.getMessagesWithOffsetForDialog(userId, collocutorId,20,offset);
			JsonArrayBuilder jsonArrayOfMessagesBuilder = Json
					.createArrayBuilder();
			for (Message message : messages) {
				JsonObject chatMessageJsonObject = Json.createObjectBuilder()
						.add("message", message.getText())
						.add("sender", message.getSender())
						.add("receiver", message.getReceiver())
						.add("received", TimeToText.timeToText(message.getMessageTime()))
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
