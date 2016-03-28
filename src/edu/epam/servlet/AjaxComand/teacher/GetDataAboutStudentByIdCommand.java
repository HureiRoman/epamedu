package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Student;
import edu.epam.service.StudentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER ,RoleType.STUDENT})
public class GetDataAboutStudentByIdCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer studentId=Integer.valueOf(request.getParameter("studentId"));
		Student student=StudentService.getDataAboutStudentById(studentId);

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <generalTag>");
		responseBuilder.append(" <student>");
		responseBuilder.append(" <fname>");
		responseBuilder.append(student.getFirstName());
		responseBuilder.append("</fname>");
		responseBuilder.append(" <lname>");
		responseBuilder.append(student.getLastName());
		responseBuilder.append("</lname>");
		responseBuilder.append(" <pname>");
		responseBuilder.append(student.getParentName());
		responseBuilder.append("</pname>");
		responseBuilder.append(" <email>");
		responseBuilder.append(student.getEmail());
		responseBuilder.append("</email>");
		responseBuilder.append(" <phone>");
		responseBuilder.append(student.getCv().getPhone());
		responseBuilder.append("</phone>");
		responseBuilder.append(" <birth>");
		responseBuilder.append(student.getCv().getBirth());
		responseBuilder.append("</birth>");
		responseBuilder.append(" <objective>");
		responseBuilder.append(student.getCv().getObjective());
		responseBuilder.append("</objective>");
		responseBuilder.append(" <skills>");
		responseBuilder.append(student.getCv().getSkills());
		responseBuilder.append("</skills>");
		responseBuilder.append(" <additional>");
		responseBuilder.append(student.getCv().getAdditionalInfo());
		responseBuilder.append("</additional>");
		responseBuilder.append(" <education>");
		responseBuilder.append(student.getCv().getEducation());
		responseBuilder.append("</education>");
		responseBuilder.append(" <english>");
		responseBuilder.append(student.getCv().getEnglishLevel());
		responseBuilder.append("</english>");
		responseBuilder.append(" </student>");
		responseBuilder.append(" </generalTag>");
		
		return responseBuilder.toString();
	}
}
