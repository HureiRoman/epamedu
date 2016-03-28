package edu.epam.servlet.AjaxComand.trainee;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TRAINEE,RoleType.GRADUATE})
public class SubscribeToGetNews implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String directionId = request.getParameter("directionId");
		String activeStr = request.getParameter("active");
		CommonUser logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		
		boolean subscribedToGetNews = false;
		
		if(directionId!=null&&activeStr!=null && logined_user != null){
			Integer idDirection = Integer.parseInt(directionId);
			Boolean active = Boolean.valueOf(activeStr); 
			subscribedToGetNews = TraineeService.subscribeToGetNews(logined_user.getId(),idDirection,active);
		}

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(subscribedToGetNews?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}