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
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Group;
import edu.epam.role.CommonUser;
import edu.epam.service.GroupService;

@UserPermissions({RoleType.TEACHER})
public class ViewGroupsCommand implements ActionCommand{
	static final Logger LOGGER = Logger.getLogger(ViewGroupsCommand.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response,Locale locale) throws ServletException, IOException {
		LOGGER.info("execute view groups");
		CommonUser teacher=(CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		try {
			List<Group> listOfGroups=GroupService.getListOfGroupForTeacher(teacher.getId());
			List<Direction> listOfDirections=GroupService.getListOfDirectionsForTeacher(teacher.getId());
		request.setAttribute("groups", listOfGroups);
		request.setAttribute("directions", listOfDirections);
	   return ConfigurationManager.getInstance().getProperty(ConfigurationManager.VIEW_GROUPS_PAGE);
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
