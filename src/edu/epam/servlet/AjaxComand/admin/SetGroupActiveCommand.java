package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.GroupService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class SetGroupActiveCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String groupIdStr = request.getParameter("id");
		String activeStr = request.getParameter("active");
		boolean setGroupActive = false;
		if(groupIdStr!=null&&activeStr!=null){
			Integer groupId = Integer.parseInt(groupIdStr);
			Boolean active = Boolean.valueOf(activeStr); 
			setGroupActive = GroupService.setGroupActive(groupId,active);
		}

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(setGroupActive?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
