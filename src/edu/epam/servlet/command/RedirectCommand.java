package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;

@UserPermissions({ RoleType.ANY })
public class RedirectCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session.getAttribute("logined_user");
		String page = null;
		String direction = request.getParameter("direction");
		if (direction != null) {
			try {
				page = ConfigurationManager.getInstance().getProperty(direction);
			}
			catch (MissingResourceException e) {
				request.setAttribute("error", 404);
				return ConfigurationManager.getInstance().getProperty(
						ConfigurationManager.ERROR_PAGE_PATH);
			}
			if (page != null) {
				
				if(checkPermision(page, user)) {
					return page;
				}
				else {
					request.setAttribute("error", 403);
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.ERROR_PAGE_PATH);
				}
			}
			else {
				request.setAttribute("error", 404);
				return ConfigurationManager.getInstance().getProperty(
						ConfigurationManager.ERROR_PAGE_PATH);
			}
		}

		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.MAIN_PAGE_PATH);

	}

	private boolean checkPermision(String page, CommonUser user) {
		if(user == null || user.getRoleType() == RoleType.GUEST) {
			if(
					page.matches("(.*)/" + RoleType.ADMIN.toString().toLowerCase() + "/(.*)") ||
					page.matches("(.*)/" + RoleType.HR.toString().toLowerCase() + "/(.*)") ||
					page.matches("(.*)/" + RoleType.TEACHER.toString().toLowerCase() + "/(.*)") ||
					page.matches("(.*)/" + RoleType.TRAINEE.toString().toLowerCase() + "/(.*)") ||
					page.matches("(.*)/" + RoleType.STUDENT.toString().toLowerCase() + "/(.*)") ||
					page.matches("(.*)/" + RoleType.GRADUATE.toString().toLowerCase() + "/(.*)")
					) {
				return false;
			}
		}
		else if(user.getRoleType() == RoleType.HR || user.getRoleType() == RoleType.ADMIN || user.getRoleType() == RoleType.GRADUATE
				|| user.getRoleType() == RoleType.STUDENT || user.getRoleType() == RoleType.TRAINEE 
				||user.getRoleType() == RoleType.TEACHER) {
			if(page.matches("(.*)(/)(.*)(/)(.*)(/)(.*)")) {
				if (user.getRoleType() == RoleType.GRADUATE) {
					return (page.matches("(.*)/" + user.getRoleType().toString().toLowerCase() + "/(.*)") 
							|| page.matches("(.*)/" + RoleType.TRAINEE.toString().toLowerCase() + "/(.*)"));
				}
				else
					return page.matches("(.*)/" + user.getRoleType().toString().toLowerCase() + "/(.*)");
			}
		}
		else if(user.getRoleType() == RoleType.GRADUATE) {
			
		}
		return true;
	}

}
