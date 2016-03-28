package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Group;
import edu.epam.service.GroupService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class UpdateGroupDataCommand implements AjaxActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int status = 0;
		String groupIdStr = request.getParameter("id");
		String title = request.getParameter("title");
		System.out.println("SLAVIK "+groupIdStr+" "+title);
		Integer groupId = null;
		if (groupIdStr != null) {
			groupId = Integer.parseInt(groupIdStr);
		}
		boolean updated = false;
		if (groupId != null && title != null) {
			Group group = new Group();
			group.setTitle(title);
			group.setId(groupId);
			updated = GroupService.updateGroup(group);
		}
		status = (updated) ? 1 : 0;
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(status);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
