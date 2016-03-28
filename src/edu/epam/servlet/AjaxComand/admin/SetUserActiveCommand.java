package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.CommonUserService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class SetUserActiveCommand implements AjaxActionCommand {

	@Override 
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String userIdStr = request.getParameter("id"); 
		String activeStr = request.getParameter("active");
		boolean setUserActive = false; 
		if(userIdStr!=null&&activeStr!=null){
			Integer userId = Integer.parseInt(userIdStr); 
			Boolean active = Boolean.valueOf(activeStr);
			setUserActive = CommonUserService.setUserActive(userId,active);
		}

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(setUserActive?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
