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
import edu.epam.role.CommonUser;
import edu.epam.service.PersonalMessageService;

@UserPermissions({ RoleType.TEACHER, RoleType.STUDENT, RoleType.TRAINEE,
		RoleType.HR })
public class OpenDialogsListCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		CommonUser user = (CommonUser) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);

		List<Integer> collocutors = PersonalMessageService
				.getListOfCollocutorsForUser(user.getId());

		request.setAttribute("collocutors", collocutors);

		switch (user.getRoleType()) {
		case STUDENT:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.STUDENT_DIALOGS_PAGE);

		case TEACHER:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.TEACHER_DIALOGS_PAGE);

		case HR:

			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.HR_DIALOGS_PAGE);
		case TRAINEE:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.TRAINEE_DIALOGS_PAGE);

		default: return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.MAIN_PAGE_PATH);
		}

	}

}
