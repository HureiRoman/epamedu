package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.GRADUATE})	
public class LeaveReviewCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
	     HttpSession session = request.getSession();
	    CommonUser user = (CommonUser) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
		Integer userId = user.getId();
		Integer directionId =  Integer.parseInt(request.getParameter("directionId"));
		String message = request.getParameter("message");
		boolean isAdded = CommonUserService.leaveReview(message,userId,directionId);
		return Json.createObjectBuilder().add("result", isAdded?1:0).build().toString();		
	}

}
