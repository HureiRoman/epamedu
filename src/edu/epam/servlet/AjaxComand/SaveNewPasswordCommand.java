package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.persistance.HashUtils;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;

@UserPermissions({ RoleType.GUEST })
public class SaveNewPasswordCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String email = request.getParameter("myEmail");
		String newPass = request.getParameter("newPassword");
		String newPassRepeat = request.getParameter("confirmNewPassword");

		System.out.println(newPass + " " + newPassRepeat);

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");

		responseBuilder.append("<result>");
		int result = 1;
		String direction = "";

		if (newPass != null && newPassRepeat != null) {
			if (newPass.equals(newPassRepeat)) {
				if (newPass.length() >= 4) {
					try {
						System.out.println("EMAIL:" + email);
						Integer userId = CommonUserService.getIdByEmail(email);
						CommonUserService.changePassword(userId,
								HashUtils.getMD5(newPass, email));

						CommonUser logined_user = CommonUserService.checkLogin(
								email, newPass);
						RoleType roleType = logined_user.getRoleType();
						request.getSession()
								.setAttribute(
										Constants.SESSION_PARAM_NAME_USER,
										logined_user);
						switch (roleType) {
						case ADMIN:
							direction = "ADMIN_PANEL_PAGE";
							break;
						case STUDENT:
							direction = "STUDENT_PANEL_PAGE";
							break;
						case GRADUATE:
							direction = "TRAINEE_CABINET_PAGE";
							break;
						case HR:
							direction = "hr_cabinet";
							break;
						case TEACHER:
							direction = "teacher_cabinet";
							break;
						case TRAINEE:
							direction = "TRAINEE_CABINET_PAGE";
							break;
						}
					} catch (IncorrectPasswordInput e) {
						result = 4;// bad pass
					}
				} else {
					result = 5;// bad length
				}
			} else {
				result = 3;// pass not equal
			}
		} else {
			result = 2;// enter all data
		}

		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append("<cabinet_url>");
		responseBuilder.append(direction);
		responseBuilder.append("</cabinet_url>");

		responseBuilder.append("</result>");

		return responseBuilder.toString();

	}

}
