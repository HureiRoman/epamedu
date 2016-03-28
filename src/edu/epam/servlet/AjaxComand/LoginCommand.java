package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.exceptions.NoSuchUser;
import edu.epam.exceptions.YouAreNotConfirmed;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;

@UserPermissions({ RoleType.ANY })
public class LoginCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String email = request.getParameter("email");
		String pass = request.getParameter("password");
		System.out.println("email = " + email + " ; password = " + pass);
		CommonUser logined_user = null;
		int result = 0;

		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append("<result>");
		try {
			logined_user = CommonUserService.checkLogin(email, pass);
			System.out.println(logined_user);
			if (null != logined_user) {
				String lang = logined_user.getLang();
				Cookie cookie = new Cookie(Constants.SESSION_PARAM_NAME_LOCALE, lang);
				cookie.setMaxAge(3600 * 24 * 7); 
				response.addCookie(cookie);
				request.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, lang);
				request.getSession().setAttribute(
						Constants.SESSION_PARAM_NAME_USER, logined_user);
				result = 1; // All OK!
				RoleType roleType = logined_user.getRoleType();
				String direction ="";
				switch (roleType) {
				case ADMIN:
                     direction="ADMIN_PANEL_PAGE";
					break;
				case STUDENT:
					  direction="student_cabinet";
					break;
				case HR:
					  direction="hr_cabinet";
					break;
				case TEACHER:
					  direction="teacher_cabinet";
					break;
				case TRAINEE:
					  direction="TRAINEE_CABINET_PAGE";
					break;
				case GRADUATE:
					  direction="TRAINEE_CABINET_PAGE";
					break;
				}
				responseBuilder.append("<cabinet_url>");
				responseBuilder.append(direction );
				responseBuilder.append("</cabinet_url>");
			}
		} catch (IncorrectPasswordInput e) {
			result = 2; // incorrect pass code
		} catch (YouAreNotConfirmed e) {
			result = 3; // YouAreNotConfirmed error code
		} catch (NoSuchUser e) {
			result = 2; // NoSuchUser error code
		}

		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append("</result>");

		return responseBuilder.toString();
	}

}
