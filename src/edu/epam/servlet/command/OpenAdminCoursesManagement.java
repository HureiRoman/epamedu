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
import edu.epam.model.Direction;
import edu.epam.service.DirectionService;

@UserPermissions({ RoleType.ADMIN })
public class OpenAdminCoursesManagement implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		
		List<Direction> directions = DirectionService.getAllDirectionsForAdmin();
		request.setAttribute("directions", directions);
		
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.ADMIN_PANEL_COURSES_PAGE);
	}

}
