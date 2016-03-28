package edu.epam.controllerServlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
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
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.manager.ConfigurationManager;
import edu.epam.manager.MessageManager;
import edu.epam.role.CommonUser;
import edu.epam.servlet.command.ActionCommand;

public class Controller extends HttpServlet {
	static final Logger LOG = Logger.getLogger(Controller.class);
	private static final long serialVersionUID = 1L;
	private static final String PARAM_NAME_ERROR_MESSAGE = "errorMessage";

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.info("Enter Controller with request = "
				+ request.getRequestURL().append('?')
						.append(request.getQueryString()));
		String page = null;
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
		
		Locale locale =  new  Locale(language);
		
		CommonUser user = (CommonUser) request.getSession().getAttribute(
				Constants.SESSION_PARAM_NAME_USER);
		try {
			ActionCommand command = ActionFactory.getInstance().getCommand(
					request);
			if (checkPermissions(command, user)) {
				page = command.execute(request, response, locale);
				System.out.println("forwarding to  page = " + page);
			} else {
				request.setAttribute(
						PARAM_NAME_ERROR_MESSAGE,
						MessageManager.getInstance().getProperty(
								MessageManager.FORBIDDEN, locale));
				request.setAttribute("error", 403);
				page = ConfigurationManager.getInstance().getProperty(
						ConfigurationManager.ERROR_PAGE_PATH);
				System.out.println("forwarding to error page = " + page);
			}
		} catch (ServletException e) {
			LOG.error("Servlet exception = " + e.getMessage());
			request.setAttribute(
					PARAM_NAME_ERROR_MESSAGE,
					MessageManager.getInstance().getProperty(
							MessageManager.SERVLET_EXCEPTION_ERROR_MESSAGE,
							locale));
			page = ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.ERROR_PAGE_PATH);
		} catch (IOException e) {
			LOG.error("IOException exception = " + e.getMessage());
			request.setAttribute(
					PARAM_NAME_ERROR_MESSAGE,
					MessageManager.getInstance().getProperty(
							MessageManager.IO_EXCEPTION_ERROR_MESSAGE, locale));
			page = ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.ERROR_PAGE_PATH);
		} catch (NoDBConnectionsLongTime e) {
			//Bodak must make error page
			e.printStackTrace();
			LOG.error("General exception = " + e.getMessage());
			request.setAttribute(
					PARAM_NAME_ERROR_MESSAGE,
					MessageManager.getInstance().getProperty(
							MessageManager.TOTAL_EXCEPTION_ERROR_MESSAGE,
							locale)
							+ e.getMessage());
			page = ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.ERROR_PAGE_PATH);
		}catch (Exception e) {

			e.printStackTrace();
			LOG.error("General exception = " + e.getMessage());
			request.setAttribute(
					PARAM_NAME_ERROR_MESSAGE,
					MessageManager.getInstance().getProperty(
							MessageManager.TOTAL_EXCEPTION_ERROR_MESSAGE,
							locale)
							+ e.getMessage());
			page = ConfigurationManager.getInstance().getProperty(
					ConfigurationManager.ERROR_PAGE_PATH);
		}
		LOG.info("Request forward to page = " + page);
		if(page.equals("")) {
			 response.sendRedirect("");
		}else {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(page);
			dispatcher.forward(request, response);
		}
	}

	protected boolean checkPermissions(ActionCommand command, CommonUser user) {
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

		return false;
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

	@Override
	public String getServletInfo() {
		return "Controller servlet";
	}
}
