package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Student;
import edu.epam.service.StudentService;
@UserPermissions({RoleType.GUEST})
public class GetStudentsForTeacherByGroupCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		Integer teacher_id=(Integer)request.getSession().getAttribute("id");
		
		Integer group_id=(Integer)request.getSession().getAttribute("group_id");
		
		List<Student> allRight=StudentService.getStudentsForTeacherByGroup(teacher_id,group_id);

		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(allRight);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
