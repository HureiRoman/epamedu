package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.constants.TypeOfTasks;
import edu.epam.model.Lesson;
import edu.epam.model.Task;
import edu.epam.service.LessonService;
import edu.epam.service.TaskService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class EditTaskCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String taskTitle = request.getParameter("task_title");
		String taskBody = request.getParameter("task_content");
		String deadline = request.getParameter("task_deadline");
		Integer taskId =Integer.parseInt(request.getParameter("task_id"));
		
		int editingResponse=0;
		if(taskTitle != null && taskBody != null && deadline != null && taskId!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date deadline_date = simpleDateFormat.parse(deadline);
			
			 Task task = new Task(taskTitle,taskBody,deadline_date,TypeOfTasks.TASK);
			 task.setId(taskId);
	        if(TaskService.updateTask(task)) editingResponse=1;
	} else editingResponse=2;
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(editingResponse);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}
}
