package edu.epam.servlet.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.service.TestService;
import edu.epam.service.TraineeService;

@UserPermissions({RoleType.ANY})
public class TestsResultCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		
		int points = 0;
		Direction direction = retrieveDirection(request);
		List<Test> tests = retrieveTests(request);
		List<Integer> answers = retrieveAnswers(request);
		
		if(answers.size() < tests.size()) {
			for (int i = answers.size(); i < tests.size(); i++) {
				answers.add(0);
			}
		}
		
		for (int i = 0; i < tests.size(); i++) {
			tests.get(i).setUserAnswer(answers.get(i));
			if (answers.get(i) != null && 
					tests.get(i).getUserAnswer().intValue() == tests.get(i).getCorrect().intValue()) {
				points++;
			}
		}
		request.setAttribute("tests", tests);
		request.setAttribute("score", points + "/" + tests.size()); // СЂРµР·СѓР»СЊС‚Р°С‚, РЅ-Рґ: 11/12
		
		if (session.getAttribute(Constants.SESSION_PARAM_NAME_USER) != null) {
			CommonUser user = (CommonUser)session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
			if(user.getRoleType().equals(RoleType.TRAINEE) || user.getRoleType().equals(RoleType.STUDENT) 
					|| user.getRoleType().equals(RoleType.GRADUATE)) {
//				user user = (user)logined_user;
				for(Direction d : user.getTestResults().keySet()) {
					if (d.getName().equalsIgnoreCase(direction.getName())) {
						direction = d;
					}
				}

				// check if Map of tests exists
				if(user.getTestResults() == null) {
					user.setTestResults(new HashMap<Direction, TestResults>());
				}
				// check if test direction exists
				user.getTestResults().putIfAbsent(direction, //РІР·СЏС‚Рё direction
						new TestResults(direction.getId(), user.getId(), 0, 0));
				// set test count +1
				user.getTestResults().get(direction).setTestCount(
						user.getTestResults().get(direction).getTestCount() + 1);
				// set summarized points
				user.getTestResults().get(direction).setPoints(
						user.getTestResults().get(direction).getPoints() + points);
				// update DB
				if (!TraineeService.passOnlineTest(user, direction)) {
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.ERROR_PAGE_PATH);
				}
			}
		}

		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.TESTS_RESULT_PAGE);
	}

	private Direction retrieveDirection(HttpServletRequest request) throws SQLException {
		String testsDirection = request.getParameter("direction");
		return TestService.getDirection(testsDirection);
	}

	private List<Integer> retrieveAnswers(HttpServletRequest request) {
		List<Integer> answers;
		String[] answersStr = (String[]) request.getParameterValues("answers");
		if(answersStr == null) {
			answers = new ArrayList<Integer>();
			answers.add(0);
			return answers;
		}
		answers = new ArrayList<Integer>(answersStr.length);
		for (String s : answersStr) {
			if(s == null || s.equals("") || s.equals("undefined")) {
				answers.add(0);
			}
			else {
				answers.add(Integer.parseInt(s));
			}
		}
		return answers;
	}

	private List<Test> retrieveTests(HttpServletRequest request) throws SQLException {
		String[] testsStr = (String[])request.getParameterValues("tests");
		List<Integer> testsInt = new ArrayList<Integer>(testsStr.length);
		for(String t : testsStr) {
			testsInt.add(Integer.parseInt(t));
		}
		List<Test> rawOrderedTests = TestService.getTestsById(testsInt);
		List<Test> orderedTests = new ArrayList<Test>(rawOrderedTests.size());
		for(int i = 0; i < testsInt.size(); i++) {
			for(int j = 0; j < rawOrderedTests.size(); j++) {
				if(testsInt.get(i).intValue() == rawOrderedTests.get(j).getId().intValue()) {
					orderedTests.add(rawOrderedTests.get(j));
					break;
				}
			}
		}
		return orderedTests;
	}
}
