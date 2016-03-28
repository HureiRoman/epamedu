package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.AdvertisementService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({ RoleType.TEACHER })
public class DeleteAdvertisementComand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer id = null;
		boolean res;
		String idString = request.getParameter("id");
		if(idString!=null){
			id = Integer.parseInt(idString);
		}
		
		res = AdvertisementService.setAdvertisementArchived(id) ;

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(res);
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}

}
