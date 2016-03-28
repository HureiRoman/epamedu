package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Group;
import edu.epam.model.Lesson;
import edu.epam.model.Task;
import edu.epam.role.Student;
import edu.epam.role.Teacher;
import edu.epam.service.GroupService;
import edu.epam.service.LessonService;
import edu.epam.service.StudentService;
import edu.epam.service.TaskService;
import edu.epam.service.TeacherService;

@UserPermissions({RoleType.STUDENT})
public class ShowStudentGroupPage  implements ActionCommand  {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
       
		
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
		
		int groupId = student.getGroupId();
		System.out.println("groupId "+groupId);
		Group studentGroup = GroupService.getGroupById(groupId);
		List<Teacher> teachers = TeacherService.getTeachersForGroup(groupId);
		List<Lesson> lessons = LessonService.getLessonsForGroup(groupId);
		List<Student> students =  StudentService.getListOfStudentsOfGroup(groupId);
		List<Task> tasks=TaskService.getTasksForGroupByUserId(student.getId());
		System.out.println(lessons);
		request.setAttribute("lessons", lessons);
		request.setAttribute("teachers", teachers);
		request.setAttribute("students", students);
		request.setAttribute("myGroup", studentGroup);
		request.setAttribute("tasks", tasks);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.STUDENT_GROUP_PAGE);
	}
}
