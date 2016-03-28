package edu.epam.servlet.AjaxComand.hr;

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
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 27/06/15.
 */
@UserPermissions({ RoleType.HR})
public class GetStudentsByGroupIdCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int groupId = Integer.parseInt(request.getParameter("group_id"));

        List<Student> students = StudentService.getListOfStudentsOfGroup(groupId);

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <students>");

        for(Student student : students) {
            responseBuilder.append("<student>");
            responseBuilder.append("<id>" + student.getId() + "</id>");
            responseBuilder.append("<firstname>" + student.getFirstName() + "</firstname>");
            responseBuilder.append("<lastname>" + student.getLastName() + "</lastname>");
            responseBuilder.append("<parentname>" + student.getParentName() + "</parentname>");
            responseBuilder.append("</student>");
        }

        responseBuilder.append("</students>");
        System.out.println(responseBuilder.toString());

        return responseBuilder.toString();
    }
}
