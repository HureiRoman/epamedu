package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;

@UserPermissions({ RoleType.TEACHER, RoleType.STUDENT, RoleType.TRAINEE,
		RoleType.HR })
public class OpenDialogWithUserCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		CommonUser user = (CommonUser) request.getSession().getAttribute(
				"logined_user");

		String withStr = request.getParameter("with");
		Integer colocutorId = Integer.parseInt(withStr);
		User collocutor = CommonUserService.getUserById(colocutorId);
		request.setAttribute("collocutor", collocutor);

		switch (user.getRoleType()) {
		case STUDENT:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.STUDENT_DIALOG_PAGE);
		case TEACHER:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.TEACHER_DIALOG_PAGE);
		case HR:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.HR_DIALOG_PAGE);
		case TRAINEE:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.TRAINEE_DIALOG_PAGE);

		default:
			return ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.MAIN_PAGE_PATH);
		}

	}

}
