package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.constants.TypeOfTasks;
import edu.epam.model.Task;
import edu.epam.service.TaskService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class AddNewStudentTaskCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String groupId = request.getParameter("groupId");
		String task_title = request.getParameter("task_title");
		String task_deadline = request.getParameter("task_deadline");
		String task_content = request.getParameter("task_content");
		
		
		int result = 0;
		int generatedKey = 0;
		
		String formattedDeadline  = null;
		if(groupId!=null && task_title != null && task_deadline != null && task_content != null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			
			Date deadline_date = simpleDateFormat.parse(task_deadline);
			formattedDeadline = simpleDateFormat.format(deadline_date);
			
			int idGroup = Integer.valueOf(groupId);
			
			Task task = new Task(task_title,task_content,deadline_date,TypeOfTasks.TASK, idGroup);
			
			generatedKey = TaskService.addNewTaskHomework(task);
			
			if(generatedKey > 0 ){
				result = 1;// All ok!
			}
		}
		JsonObjectBuilder topicJsonObject = Json.createObjectBuilder().add("result", result).add("generatedKeyTaskId", generatedKey);
		if(formattedDeadline!= null){
			topicJsonObject.add("deadline_date", formattedDeadline);
		}
		
		return topicJsonObject.build().toString();
	}

}
