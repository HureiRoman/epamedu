package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.PersonalMessageService;
@UserPermissions({RoleType.STUDENT,RoleType.GRADUATE,RoleType.TEACHER,RoleType.HR,RoleType.TRAINEE})
public class GetCountOfUnreadedMessagesCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
	   CommonUser user = (CommonUser) request.getSession().getAttribute("logined_user");
	   Integer count = PersonalMessageService.getAllUnreadMessageCount(user.getId());
	   return  Json.createObjectBuilder()
				.add("count", count).build().toString();
	}

}
