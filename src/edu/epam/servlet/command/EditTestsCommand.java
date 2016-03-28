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
import edu.epam.model.Test;
import edu.epam.service.DirectionService;
import edu.epam.service.TestService;

@UserPermissions({ RoleType.TEACHER })
public class EditTestsCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String directionStr = request.getParameter("direction");
		if(directionStr == null || directionStr.equals("")) {
			return  ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.TEACHER_MANAGE_TESTS);
		}
		int id = Integer.parseInt(directionStr);
		Direction direction = DirectionService.getDirectionById(id);
		List<Test> list = TestService.getTestsForDirection(direction.getName());
		request.setAttribute("direction", direction);
		request.setAttribute("tests", list);
		return  ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.TEACHER_MANAGE_TESTS_BY_DIRECTION);
	}

}
