package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Message;
import edu.epam.persistance.TimeToText;
import edu.epam.role.CommonUser;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;
import edu.epam.service.PersonalMessageService;
@UserPermissions({RoleType.TEACHER,RoleType.STUDENT})
public class GetDialogsForUser implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
 
		List<Integer> collocutors = PersonalMessageService
				.getListOfCollocutorsForUser(user.getId());
		
		JsonObjectBuilder responseJsonBuilder = Json.createObjectBuilder();
		JsonArrayBuilder dialogsJsonArrayBuilder = Json.createArrayBuilder();
		for (Integer colocutorId : collocutors) {
			User colocutor  = CommonUserService.getUserById(colocutorId);
			Integer countOfUnreadMessages =  PersonalMessageService.getCountOfUnreadMessages(user.getId(),colocutorId);
			Message lastMessage = PersonalMessageService.getLastMessageForDialog(user.getId(),colocutorId);
			JsonObjectBuilder dialogJsonBuilder = Json.createObjectBuilder();
			JsonObject dialogJsonObject = dialogJsonBuilder.add("collocutorId", colocutorId)
			.add("collocutorName", colocutor.getFirstName()+" "+colocutor.getLastName())
			.add("lastMessageText", lastMessage.getText())
			.add("lastMessageTime", TimeToText.timeToText(lastMessage.getMessageTime()))
			.add("unreadMessages", countOfUnreadMessages)
			.add("lastMessageFromUser", lastMessage.getSender()==user.getId())
			.build();
			dialogsJsonArrayBuilder.add(dialogJsonObject);
		}
		JsonArray dialogsJsonArray = dialogsJsonArrayBuilder.build();
		responseJsonBuilder.add("dialogs", dialogsJsonArray);
		JsonObject responseJson = responseJsonBuilder.build();
		return responseJson.toString();
	}
}
