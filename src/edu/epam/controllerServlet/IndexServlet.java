package edu.epam.controllerServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.constants.Constants;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.model.NewsItem;
import edu.epam.service.DirectionService;
import edu.epam.service.InterviewService;
import edu.epam.service.NewsService;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		
		List<NewsItem> news = null;
		try {			
			List<Direction> directions = DirectionService.getAllDirections();
			news = NewsService.getActiveNews();
			List<Interview> interviews =  InterviewService.getFutureInterviews();
			interviews.sort(new Comparator<Interview>() {
				@Override
				public int compare(Interview interview1, Interview interview2) {
					return interview1.getDateOfTesting().compareTo(interview2.getDateOfTesting());
				}
			});
			String reg_sended = request.getParameter("reg_sended");
			if(news!=null&&directions!=null&&interviews!=null){
				request.setAttribute("news", news);
				request.setAttribute("directions", directions);		
				request.setAttribute("events", interviews);
				if(reg_sended != null && reg_sended.equals("true")){
					request.setAttribute("regSended", MessageManager.getInstance().getProperty(MessageManager.REGISTRATION_SUBMITTING, new Locale(language)));
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("pages/index.jsp").forward(request, response);
		
	}

}
