package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.LessonService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class AutoTurnOnLessonCommand  implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String lessonId = request.getParameter("lessonId");
		String activeStr = request.getParameter("active");
		boolean setLessonActive = false;
		if(lessonId!=null&&activeStr!=null){
			Integer idLesson = Integer.parseInt(lessonId);
			Boolean active = Boolean.valueOf(activeStr); 
			setLessonActive = LessonService.setLessonAutoTurnOn(idLesson,active);
		}

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(setLessonActive?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
