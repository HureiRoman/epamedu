package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Student;
import edu.epam.role.Teacher;
import edu.epam.service.StudentService;
import edu.epam.service.TeacherService;

@UserPermissions({ RoleType.TEACHER, RoleType.STUDENT })
public class GetChatParticipantsCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer groupId = Integer.parseInt(request.getParameter("groupId"));
		List<Teacher> teachers = TeacherService.getTeachersForGroup(groupId);
		List<Student> students = StudentService
				.getListOfStudentsOfGroup(groupId);
		
		JsonArrayBuilder partisipantsJSONarrayBuilder = Json
				.createArrayBuilder();
		for (Student student : students) {
			partisipantsJSONarrayBuilder.add(
					Json.createObjectBuilder().
					add("id", student.getId()).
					add("firstName", student.getFirstName()).
					add("lastName", student.getLastName())			
					.build());
		}
		for (Teacher teacher : teachers) {
			partisipantsJSONarrayBuilder.add(
					Json.createObjectBuilder().
					add("id", teacher.getId()).
					add("firstName", teacher.getFirstName()).
					add("lastName", teacher.getLastName())			
					.build());
		}
		JsonObject result = Json.createObjectBuilder().add("participants", partisipantsJSONarrayBuilder.build()).build();
		return result.toString();
	}
}
