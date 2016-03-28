package edu.epam.servlet.AjaxComand.teacher;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.StudentVisiting;
import edu.epam.service.StudentVisitingSevice;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by fastforward on 08/07/15.
 */
@UserPermissions({RoleType.TEACHER})
public class SetStudentVisitingCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 0;

        String lessonIdString = request.getParameter("lesson_id");
        String studentIdString = request.getParameter("student_id");
        String isPresent = request.getParameter("is_present");


        if (lessonIdString != null && studentIdString != null && isPresent != null) {
            try {
                int studentId = Integer.parseInt(studentIdString);
                int lessonId = Integer.parseInt(lessonIdString);
                StudentVisiting studentVisiting = new StudentVisiting();
                studentVisiting.setStudentId(studentId);
                studentVisiting.setLessonId(lessonId);
                if(isPresent.equals("true")) {
                    studentVisiting.setIsPresent(true);
                } else {
                    studentVisiting.setIsPresent(false);
                }
                if(!StudentVisitingSevice.isStudentVisitingPresent(studentVisiting)) {
                    StudentVisitingSevice.insertStudentVisiting(studentVisiting);
                } else {
                    StudentVisitingSevice.updateStudentVisiting(studentVisiting);
                }
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
                result = 2;
            }
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
