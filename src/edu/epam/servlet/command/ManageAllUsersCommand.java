package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.ADMIN})
public class ManageAllUsersCommand implements ActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		List<CommonUser> users = CommonUserService.getAllUsersForAdmin();
		request.setAttribute("users", users);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.ADMIN_USERS_MANAGEMENT);
	}

}
