package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
@UserPermissions({RoleType.ADMIN,RoleType.TRAINEE,RoleType.STUDENT,RoleType.TEACHER,RoleType.HR,RoleType.GRADUATE})
public class ExitComand implements AjaxActionCommand {
	
	static final Logger LOGGER = Logger.getLogger(ExitComand.class);
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");   
		int result = 0;
		
		
		HttpSession session = request.getSession(false);
		if(null != session){
			
			LOGGER.info(" execute log out ");
			session.removeAttribute(Constants.SESSION_PARAM_NAME_USER);
			
//            session.removeAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
			
			session.invalidate();
			result = 1 ;
		
		}
		
		responseBuilder.append("<status>");
		responseBuilder.append(result); 
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
