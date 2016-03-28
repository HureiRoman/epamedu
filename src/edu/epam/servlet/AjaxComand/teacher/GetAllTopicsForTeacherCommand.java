package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Topic;
import edu.epam.role.CommonUser;
import edu.epam.service.TeacherService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class GetAllTopicsForTeacherCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
//		
//		StringBuilder responseBuilder = new StringBuilder();
//		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		
		CommonUser loginedUser = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		String directionId = request.getParameter("directionId");
		
		if(loginedUser != null && directionId != null){
			int directId = Integer.valueOf(directionId);
			int userId = loginedUser.getId();
			
			List<Topic> teacherTopics = TeacherService.getTeachersTopics(userId,directId);
			
			JsonArrayBuilder jsonArrayOfTopicsBuilder = Json.createArrayBuilder();
			for (Topic topic : teacherTopics) {
				JsonObject topicJsonObject = Json.createObjectBuilder()
						.add("title", topic.getTitle())
						.add("content", topic.getContent())
						.add("topicId", topic.getId())
						.build();
				jsonArrayOfTopicsBuilder.add(topicJsonObject);
			}
			
			JsonArray jsonArrayOfTopics = jsonArrayOfTopicsBuilder.build();
			
			JsonObject arrayOfTopicsObject = Json.createObjectBuilder()
					.add("topics", jsonArrayOfTopics).build();
			return arrayOfTopicsObject.toString();
		}
		return "";
	}

}
