package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.ANY})
public class ChangeLanguageCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		System.out.println("language changing");
		HttpSession session = request.getSession();
		String lang = request.getParameter(Constants.SESSION_PARAM_NAME_LOCALE);
		Cookie cookie = new Cookie(Constants.SESSION_PARAM_NAME_LOCALE, lang);
		CommonUser user =(CommonUser) session.getAttribute(Constants.SESSION_PARAM_NAME_USER);
		if(user!=null){
			 boolean updated = CommonUserService.setLanguage(lang, user.getId());
		}
		cookie.setMaxAge(3600 * 24 * 7); 
		response.addCookie(cookie);
		session.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, lang);
		return "";
	}

}
