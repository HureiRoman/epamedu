package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.service.TestService;

@UserPermissions({RoleType.ANY})
public class GetAllDirectionsWithTestsCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		response.setContentType("application/xml");
	    response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		List<Direction> directions = TestService.getAllDirections();
		List<Direction> filteredDirections = selectDirectionsWithTests(directions);
		if (!filteredDirections.isEmpty()) {
			responseBuilder.append("<directionsList>");
			getDirectionsInfo(responseBuilder, filteredDirections, locale);
			responseBuilder.append("</directionsList>");
		}
		else {
			responseBuilder.append("<empty>");
			responseBuilder.append("<status>");
			responseBuilder.append(0); // tests not ready yet
			responseBuilder.append("</status>");
			responseBuilder.append("<message>");
			responseBuilder.append(MessageManager.getInstance().getProperty(MessageManager.NO_TESTS_USER, locale));
			responseBuilder.append("</message>");
			responseBuilder.append("</empty>");

		}
		return responseBuilder.toString();
	}

	private void getDirectionsInfo(StringBuilder responseBuilder,
			List<Direction> filteredDirections, Locale locale) {
		
		for(Direction d : filteredDirections) {
			responseBuilder.append("<direction>");
			responseBuilder.append("<direction_name>");
			responseBuilder.append(d.getName());
			responseBuilder.append("</direction_name>");
			responseBuilder.append("<direction_id>");
			responseBuilder.append(d.getId());
			responseBuilder.append("</direction_id>");
			responseBuilder.append("</direction>");
		}
	}

	private List<Direction> selectDirectionsWithTests(List<Direction> directions) throws SQLException {
		List<Direction> resultList = new ArrayList<Direction>();
		for(Direction d : directions) {
			if(TestService.getTestsForDirection(d.getName()).size() >= 20) {
				resultList.add(d);
			};
		}
		return resultList;
	}
}
