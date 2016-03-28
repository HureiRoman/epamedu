package edu.epam.controllerServlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

public class AjaxController extends HttpServlet {

	static final Logger LOG = Logger.getLogger(AjaxController.class);
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		System.out.println("sending message controller");
		HttpSession session = request.getSession();
		String language = (String) session.getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		if(language==null){
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				Cookie langCookie = null;
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(Constants.SESSION_PARAM_NAME_LOCALE)) {
						langCookie = cookie;
					}
				}
				if (langCookie != null) {
					session.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, langCookie.getValue());
					language = langCookie.getValue();
				}else{
					language = "uk";	 // read from db and write to cookies
					session.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, language);
				}
			}else{
				language = "uk";	 // read from db and write to cookies
				session.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, language);
			}
		}
		Locale locale =  new Locale(language);

		String xmlOrJsonResponse = null; 
		 CommonUser user =(CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		try {
			AjaxActionCommand command = AjaxActionFactory.getInstance()
					.getCommand(request);
			 if(checkPermissions(command,user)){
	            	LOG.info("Chosen command = " + command.getClass());
	    			xmlOrJsonResponse = command.execute(request, response,locale);
	            }else{
	            	xmlOrJsonResponse="<status>forbidden</status>";
	            }
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("General exception = " + e.getMessage());
			response.setContentType("application/xml");
			response.setCharacterEncoding("UTF-8");
			StringBuilder responseBuilder = new StringBuilder();
			responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
			responseBuilder.append("<error></error>");
			xmlOrJsonResponse = responseBuilder.toString();
			
		}
		LOG.info("response = " + xmlOrJsonResponse);
		response.getWriter().write(xmlOrJsonResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.info("redirect to Controller");
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.info("redirect to Controller");
		processRequest(request, response);
	}

	protected boolean checkPermissions(AjaxActionCommand command, CommonUser user) {
		UserPermissions annotation = command.getClass().getAnnotation(
				UserPermissions.class);
		RoleType[] userRoles = annotation.value();
		
		for (RoleType roleType : userRoles) {
			if (roleType.equals(RoleType.ANY)) {
				System.out.println("command " + command.getClass()
						+ " is acessible for GUEST");
				return true;
			}
		}

		if (user == null) {
			for (RoleType roleType : userRoles) {
				if (roleType.equals(RoleType.GUEST)) {
					System.out.println("command " + command.getClass()
							+ " is acessible for GUEST");
					return true;
				}
			}
			System.out.println("command " + command.getClass()
					+ " is  not acessible for GUEST");
		} else {
			RoleType userRoleType = user.getRoleType();
			for (RoleType roleType : userRoles) {
				if (roleType.equals(userRoleType)) {
					System.out.println("command " + command.getClass()
							+ " is acessible for " + userRoleType.name());
					return true; 
				}
			}
			System.out.println("command " + command.getClass()
					+ " is  not acessible for " + userRoleType.name());
		}
		System.out.println("return FALSE");
		return false;
	}
}
