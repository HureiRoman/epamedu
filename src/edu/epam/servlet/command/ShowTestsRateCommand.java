package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;

@UserPermissions({RoleType.TRAINEE, RoleType.STUDENT, RoleType.GRADUATE})
public class ShowTestsRateCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
		
		if (user.getRoleType().equals(RoleType.STUDENT)) {
			return "/pages/student/tests_rate.jsp";
		}
		else
			return "/pages/trainee/tests_rate.jsp";
	}

}
