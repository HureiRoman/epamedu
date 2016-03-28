package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.bcel.classfile.Constant;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Advertisement;
import edu.epam.role.Student;
import edu.epam.service.AdvertisementService;

@UserPermissions({ RoleType.STUDENT })
public class OpenStudentCabinetCommand  implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		Student student = (Student)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		
		List<Advertisement> listOfAdvertisement = AdvertisementService.getListOfAdvertisementForGroup(student.getGroupId());
		
		listOfAdvertisement.sort(new Comparator<Advertisement>() {

			@Override
			public int compare(Advertisement o1, Advertisement o2) {
				return o2.getAdvertisementDate().compareTo(o1.getAdvertisementDate());
			}
		});
		request.setAttribute("listOfAdvertisement", listOfAdvertisement);
		
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.STUDENT_PANEL_PAGE);
	}

}
