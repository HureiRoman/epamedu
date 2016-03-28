package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.TaskService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;


@UserPermissions({RoleType.TEACHER})
public class DeleteHomeWorkCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String choosedLessonId = request.getParameter("choosedLessonId");
		
		int result = 0;
		boolean resultOfDeleteHW = false;
		
		if(choosedLessonId!=null){
			
			int lessonId = Integer.valueOf(choosedLessonId);
			resultOfDeleteHW = TaskService.deleteHomework(lessonId);
			
			if(resultOfDeleteHW == true ){
				result = 1;// All ok!
			}
		}
		JsonObject topicJsonObject = Json.createObjectBuilder().add("result", result).build();
		
		return topicJsonObject.toString();
	}

}
