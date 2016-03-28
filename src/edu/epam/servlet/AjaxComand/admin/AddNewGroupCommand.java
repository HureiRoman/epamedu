package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Group;
import edu.epam.service.GroupService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.ADMIN })
public class AddNewGroupCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int result = 0;
		String title = request.getParameter("title");
		Integer teacherId = Integer
				.parseInt(request.getParameter("teacher_id"));
		Integer directionId = Integer.parseInt(request
				.getParameter("direction_id"));
		Group group = new Group();
		group.setTitle(title);
		ArrayList<Integer> teachersIds = new ArrayList<Integer>();
		teachersIds.add(teacherId);
		group.setTeachers(teachersIds);
		group.setDirectionId(directionId);
		boolean groupCreated = GroupService.createGroup(group);
		result = groupCreated ? 1 : 0;
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}

}
