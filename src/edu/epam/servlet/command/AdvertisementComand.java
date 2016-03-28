package edu.epam.servlet.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Advertisement;
import edu.epam.model.Group;
import edu.epam.role.Teacher;
import edu.epam.service.AdvertisementService;
import edu.epam.service.GroupService;

@UserPermissions({ RoleType.TEACHER })
public class AdvertisementComand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		try {

			// idTeacher
			HttpSession session = request.getSession();
			Teacher teacher = (Teacher) session.getAttribute("logined_user");
			Integer idTeacher = teacher.getId();

			List<Group> listOfGroups;
			listOfGroups = GroupService.getListOfGroupForTeacher(idTeacher);
			request.setAttribute("groups", listOfGroups);
			
			List<Advertisement> listOfAdvertisement;
			listOfAdvertisement = AdvertisementService.getListOfAdvertisementByTeacher(idTeacher);
			request.setAttribute("advertisements", listOfAdvertisement);
			
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.ADVERTISEMENT_PAGE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

}
