package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.StudentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.HR})
public class DeleteStudentsHRCommand implements AjaxActionCommand  {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		int result = 0;

		String studentId = request.getParameter("student_id");
		System.out.println(studentId);
		try {
			int id = Integer.parseInt(studentId);
			StudentService.deleteStudent(id);
		} catch (Exception e) {
			e.printStackTrace();
			result = 2;
		}
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
