package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Group;
import edu.epam.model.Lesson;
import edu.epam.model.Task;
import edu.epam.role.Teacher;
import edu.epam.service.GroupService;
import edu.epam.service.LessonService;
import edu.epam.service.TeacherService;

@UserPermissions({RoleType.TEACHER})
public class GroupInfoCommand  implements ActionCommand{
	static final Logger LOGGER = Logger.getLogger(NoCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		HttpSession session = request.getSession();
		
		Integer groupId=Integer.valueOf(request.getParameter("groupId"));
		Group teacherGroup = GroupService.getGroupById(groupId);
		List<Teacher> teachers = TeacherService.getTeachersForGroup(groupId);
		List<Lesson> lessons = LessonService.getAllLessonsForGroup(groupId);
		List<Task> tasksOfGroup = LessonService.getGroupTasks(groupId);
//		List<Student> listStudentsOfGroup = StudentService.getListOfStudentsOfGroupWithTasks(groupId);
		
		
//		for(Task task : tasksOfGroup){
//			System.out.println(task);
//		}
		
		request.setAttribute("lessons", lessons);
		request.setAttribute("teachers", teachers);
		request.setAttribute("myGroup", teacherGroup);
		request.setAttribute("tasksOfGroup", tasksOfGroup);
		request.setAttribute("group_id", groupId);
//		request.setAttribute("listStudentsOfGroup", listStudentsOfGroup);
		
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.TEACHER_GROUP_PAGE);
	}
    
}
