package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.DirectionService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.ADMIN})
public class DeleteDirectionComand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		Integer id = Integer.parseInt(request.getParameter("id"));
		int result = 0;
		try {
			if( DirectionService.deleteDirection(id)){
				result = 1; // direction was deleted
			}else {
				result = 2;// invalid
			} 
		} catch (SQLException e) {
			result = 2;// invalid
		}

		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
