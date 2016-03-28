package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.Json;
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
public class AddLessonCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
	
	String homeworkPresent = request.getParameter("homeworkPresent");
	String groupId = request.getParameter("groupId");
	String topicId = request.getParameter("topicId");
	String lessonTime = request.getParameter("lessonTime");
	int result = 0;
	int idOfAddedHW =0;
	System.out.println("homeworkPresent.equalsIgnoreCase(true)" + homeworkPresent.equalsIgnoreCase("true"));
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		if(homeworkPresent.equalsIgnoreCase("true")){
			
			String taskTitle = request.getParameter("taskTitle");
			String taskBody = request.getParameter("taskBody");
			String deadline = request.getParameter("deadline");
			String type = request.getParameter("type");
			
			Date deadline_date = simpleDateFormat.parse(deadline);
			
			Task hw = new Task(taskTitle,taskBody,deadline_date,TypeOfTasks.valueOf(type));
			System.out.println("Task = " + hw);
			idOfAddedHW = TaskService.addHomework(hw);
		}
		int idGroup = Integer.parseInt(groupId);
		int idTopic = Integer.parseInt(topicId);
		Date dateOfLesson = simpleDateFormat.parse(lessonTime);
		
			Lesson lesson = new Lesson(idGroup ,idTopic, idOfAddedHW, false, dateOfLesson);
			System.out.println("Lesson = " + lesson);
			boolean resultOfAddingLesson =  LessonService.addLesson(lesson);
			if(resultOfAddingLesson == true){
				result = 1;//All ok!!
			}
		return Json.createObjectBuilder().add("result", result).build().toString();
		
		
	}

}
