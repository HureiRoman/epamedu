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

@UserPermissions({ RoleType.ANY })
public class GetLoginedUserDataCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session
				.getAttribute(Constants.SESSION_PARAM_NAME_USER);

		return Json.createObjectBuilder()
				.add("email", user.getEmail())
				.add("firstName", user.getFirstName())
				.add("lastName", user.getLastName())
				.add("id", user.getId())
				.add("role", user.getRoleType().toString())
				.build().toString();
	}

}
