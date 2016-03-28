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
public class CancelApplicationOnTestCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		 response.setContentType("application/xml");
		    response.setCharacterEncoding("UTF-8");
			StringBuilder responseBuilder = new StringBuilder();
			responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");    
			int result = 0;
			
			boolean successfull = false;
			CommonUser logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
			if(logined_user != null){
				int user_id = logined_user.getId();
				int directionId = Integer.valueOf(request.getParameter("directionId"));
				
				successfull = TraineeService.cancelApplicationOnTest(directionId, user_id);
				
				if(successfull == true){
					result = 1;// STATUS : ALL OK!!
				}else{
					result = 2;// STATUS : NOT OK!!
				}
				
			}else{
				result = 2;// STATUS : NOT OK!!
			}
		
		
			responseBuilder.append("<status>");
			System.out.println("status = " + result);
				responseBuilder.append(result);
			responseBuilder.append("</status>");
		
			return responseBuilder.toString();
	}

}
