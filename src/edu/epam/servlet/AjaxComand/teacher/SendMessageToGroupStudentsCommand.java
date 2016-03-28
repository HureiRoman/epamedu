package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.ChatMessageService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class SendMessageToGroupStudentsCommand  implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String group_id = request.getParameter("group_id");
		String text = request.getParameter("text");
		int result = 0;// Default status
		
			if(group_id !=null){
				CommonUser teacher = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
				int groupId = Integer.parseInt(group_id);
				boolean resultOfSending = ChatMessageService.sendMessageToGroupOfStudents(teacher.getId(), groupId, text);
				result = 1; // All ok
			}
		

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
