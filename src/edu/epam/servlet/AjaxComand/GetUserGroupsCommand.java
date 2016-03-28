package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Group;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;
import edu.epam.service.GroupService;

@UserPermissions({ RoleType.TEACHER, RoleType.STUDENT })
public class GetUserGroupsCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session
				.getAttribute(Constants.SESSION_PARAM_NAME_USER);

		List<Group> groupsOfUser;
		if (user.getRoleType() == RoleType.TEACHER) {
			groupsOfUser = GroupService.getListOfGroupForTeacher(user.getId());
		} else {
			groupsOfUser = new ArrayList<Group>();
			Student student = (Student) user;
			int groupId = student.getGroupId();
			Group groupById = GroupService.getGroupById(groupId);
			groupsOfUser.add(groupById);
		}

		JsonObjectBuilder responseJsonObjectBuilder = Json
				.createObjectBuilder();

		JsonArrayBuilder groupsOfUserJsonArrayBuilder = Json
				.createArrayBuilder();

		for (Group group : groupsOfUser) {
			JsonObjectBuilder groupOfUserObjectBuilder = Json
					.createObjectBuilder();
			JsonObject groupJsonObject = groupOfUserObjectBuilder
					.add("title", group.getTitle()).add("id", group.getId())
					.add("dirId", group.getDirectionId()).build();
			groupsOfUserJsonArrayBuilder.add(groupJsonObject);
		}
		JsonArray groupsOfUserJsonArray = groupsOfUserJsonArrayBuilder.build();

		responseJsonObjectBuilder.add("groups", groupsOfUserJsonArray);
		JsonObject responseJson = responseJsonObjectBuilder.build();
		return responseJson.toString();
	}

}
