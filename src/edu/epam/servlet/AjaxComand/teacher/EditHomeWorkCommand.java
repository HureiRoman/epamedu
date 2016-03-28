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
public class EditHomeWorkCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String taskTitle = request.getParameter("taskTitle");
		String taskBody = request.getParameter("taskBody");
		String deadline = request.getParameter("deadline");
		String type = request.getParameter("type");
		String choosedLesson = request.getParameter("choosedLesson");
		String taskIdEdit = request.getParameter("taskIdEdit");
		
		int result = 0;
		boolean resultOfAddingHW = false;
		int addHWgeneratedKey = 0;
		Task hw = null;
		System.out.println(taskTitle + taskBody + deadline + type + taskIdEdit);
		if(taskTitle != null && taskBody != null && deadline != null && type != null && taskIdEdit != null && choosedLesson!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date deadline_date = simpleDateFormat.parse(deadline);
			
			 hw = new Task(taskTitle,taskBody,deadline_date,TypeOfTasks.valueOf(type));
			hw.setId(Integer.valueOf(taskIdEdit));
			if(!taskIdEdit.equals("0")){
				resultOfAddingHW = TaskService.updateHomework(hw);
			}else{
				addHWgeneratedKey = TaskService.addHomework(hw);
				hw.setId(addHWgeneratedKey);
				if(addHWgeneratedKey > 0){
					Lesson lessonWithNewHW = new Lesson();
					lessonWithNewHW.setTaskId(addHWgeneratedKey);
					lessonWithNewHW.setId(Integer.valueOf(choosedLesson));
					resultOfAddingHW = LessonService.updateLesson(lessonWithNewHW);
				}
			}
			
			if(resultOfAddingHW == true || addHWgeneratedKey > 0){
				result = 1;// All ok!
			}
		}
		if(addHWgeneratedKey == 0 && hw.getId()!=0){
			addHWgeneratedKey = hw.getId();
		}
		JsonObject topicJsonObject = Json.createObjectBuilder().add("result", result)
				.add("taskTitle", taskTitle)
				.add("taskBody", taskBody)
				.add("deadline", deadline)
				.add("hwNewId", addHWgeneratedKey)
				.build();
		
		return topicJsonObject.toString();
	}

}
