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
import edu.epam.model.Attachment;
import edu.epam.model.Direction;
import edu.epam.model.Topic;
import edu.epam.role.Teacher;
import edu.epam.service.AttachmentService;
import edu.epam.service.DirectionService;
import edu.epam.service.TopicService;


@UserPermissions({RoleType.TEACHER})
public class TopicsCommand implements ActionCommand{
	static final Logger LOGGER = Logger.getLogger(TopicsCommand.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response,Locale locale) throws ServletException, IOException {
		try {
		List<Direction> listOfAllDirections;
		List<Direction> listOfDirections;
		List<Attachment> listOfAttachments;
		List<Topic> listOfTopics;
		Teacher teacher=(Teacher)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		listOfAllDirections = DirectionService.getAllDirections();
		listOfDirections=DirectionService.getTeacherDirections(teacher.getId());
		listOfAttachments=AttachmentService.getTeacherAttachments(teacher.getId());
		listOfTopics=TopicService.getTeacherTopics(teacher.getId());
		request.setAttribute("allDirections",listOfAllDirections);
		request.setAttribute("directions",listOfDirections);
		request.setAttribute("attachments",listOfAttachments);
		request.setAttribute("topics",listOfTopics);
		 return ConfigurationManager.getInstance().getProperty(ConfigurationManager.TOPICS_PAGE);
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    return null;
	}
}
