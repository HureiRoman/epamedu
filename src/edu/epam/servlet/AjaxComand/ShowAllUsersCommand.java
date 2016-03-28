package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.GUEST})
public class ShowAllUsersCommand implements AjaxActionCommand  {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		String role=request.getParameter("role");
		List<CommonUser> listOfUsers=CommonUserService.showAllUsers(role);
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(listOfUsers);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	} 
}