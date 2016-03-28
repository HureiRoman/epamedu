package edu.epam.servlet.AjaxComand.trainee;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Application;
import edu.epam.role.CommonUser;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.TRAINEE,RoleType.GRADUATE})
public class ApplyToTestsCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {

		int result = 0;
		boolean result_of_application_send;
		try {
			int interview_id = Integer.valueOf(request.getParameter("interview_id"));
			
			CommonUser logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
			int logined_user_id = logined_user.getId();
			Application application = new Application(logined_user_id,interview_id);
			result_of_application_send = TraineeService.applyToTest(application);
			
			if (result_of_application_send == true) {
				result = 1; // All ok 
				/*logined_user.setIsAplicant("true");*/
				request.getSession().setAttribute(Constants.SESSION_PARAM_NAME_USER, logined_user);
			} else {
				result = 2; // STATUS : Error at adding parameters to DB
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = 3; // STATUS : GENERAL Error in code
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
