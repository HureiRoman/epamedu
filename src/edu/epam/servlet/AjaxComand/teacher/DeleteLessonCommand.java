package edu.epam.servlet.AjaxComand.teacher;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.LessonService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;


@UserPermissions({RoleType.TEACHER})
public class DeleteLessonCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response, Locale locale) throws Exception {
		
		String lesson_id = request.getParameter("lesson_id");
		int requestStatus = 0;
		
		if(lesson_id != null) {
			int lessonId = Integer.valueOf(lesson_id);
			boolean result =  LessonService.deleteLesson(lessonId);
			if(result == true){
				requestStatus = lessonId; // All ok!
			}
		}
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(requestStatus);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}

