package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.NewsItem;
import edu.epam.service.NewsService;

@UserPermissions({ RoleType.ADMIN })
public class OpenAdminNewsManagement implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		List<NewsItem> news = NewsService.getAllNews();
		request.setAttribute("news", news);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.ADMIN_PANEL_NEWS_PAGE);
	}

}
