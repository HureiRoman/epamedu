package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;

@UserPermissions({RoleType.ANY})
public class WrongCommand implements ActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		request.setAttribute("error", 404);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.ERROR_PAGE_PATH);
	}

}
