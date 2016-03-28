package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.DirectionService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class SetDirectionHRCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String directionIdStr = request.getParameter("dir_id");
		String hrIdStr = request.getParameter("hr_id");
		
		boolean setDirectionHr = false;
		if(directionIdStr!=null&&hrIdStr!=null){
			Integer directionId = Integer.parseInt(directionIdStr);
			Integer hrId = Integer.parseInt(hrIdStr);			
			setDirectionHr = DirectionService.setDirectionHR(directionId,hrId);
		}

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(setDirectionHr?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
