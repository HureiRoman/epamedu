package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.service.TestService;

@UserPermissions({RoleType.ANY})
public class TestsCommand implements ActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		String testDirection = request.getParameter("direction");
		Direction direction = TestService.getDirection(testDirection);
//		List<Direction> allDirections = TestService.getAllDirections();
//		Direction testDirection = allDirections.get(0); //TODO змінити на селект
		List<Test> tests = TestService.getTestsForDirection(direction.getName());
		
		if(tests.isEmpty() || tests.size() < 20) {
			System.err.println("Менше 20 тестів!");
			request.setAttribute("errorMessage", "The amount of tests is too little yet. Please, try again later.");
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.MAIN_PAGE_PATH);
		}
		
		if(session.getAttribute(edu.epam.constants.Constants.SESSION_PARAM_NAME_USER) == null) {
			tests.subList(20, tests.size()).clear();
		}
		
		long seed = System.nanoTime();
		Collections.shuffle(tests, new Random(seed));
		tests.subList(12, tests.size()).clear();
		request.setAttribute("direction", direction);
		request.setAttribute("tests", tests);
		
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.TESTS_PAGE);
	}
}
