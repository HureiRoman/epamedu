package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.constants.TypeOfTasks;
import edu.epam.model.Attachment;
import edu.epam.model.Task;
import edu.epam.model.Topic;
import edu.epam.role.CommonUser;
import edu.epam.service.AttachmentService;
import edu.epam.service.TaskService;
import edu.epam.service.TopicService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.TEACHER})
public class GetTaskByIdCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		
	Integer taskId = Integer.parseInt(request.getParameter("taskId"));
	Task task=TaskService.getTaskById(taskId,TypeOfTasks.TASK);
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	responseBuilder.append(" <task>");
	responseBuilder.append("<title>");
	responseBuilder.append(task.getTitle());
	responseBuilder.append("</title>");
	responseBuilder.append("<body>");
	responseBuilder.append(task.getTaskBody());
	responseBuilder.append("</body>");
	responseBuilder.append("<deadline>");
	responseBuilder.append(sdf.format(task.getDeadline()));
	responseBuilder.append("</deadline>");
	responseBuilder.append(" </task>");
		return responseBuilder.toString();
	}

}
