package edu.epam.servlet.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Group;
import edu.epam.role.Student;
import edu.epam.service.GroupService;
import edu.epam.service.StudentService;

@UserPermissions({RoleType.TEACHER})
public class InfoAboutStudentsOfGroupCommand implements ActionCommand{
	static final Logger LOGGER = Logger.getLogger(NoCommand.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response,Locale locale) throws ServletException, IOException {
		Integer groupId=Integer.valueOf(request.getParameter("groupId"));
		LOGGER.info("showing info about students of group with id="+groupId);
		try {
			Group group=GroupService.getGroupById(groupId);
			List<Student> listOfStudents=StudentService.getListOfStudentsOfGroup(groupId);
		request.setAttribute("students", listOfStudents);
		request.setAttribute("group",group.getTitle());
		 return ConfigurationManager.getInstance().getProperty(ConfigurationManager.VIEW_STUDENTS_PAGE);
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
