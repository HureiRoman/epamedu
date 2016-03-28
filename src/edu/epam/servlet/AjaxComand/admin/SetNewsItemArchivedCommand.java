package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.NewsService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class SetNewsItemArchivedCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String newsItemIdStr = request.getParameter("id");
		String needArchiveStr = request.getParameter("archived");
		boolean setNewsItemArchived = false;
		if(newsItemIdStr!=null&&needArchiveStr!=null){
			Integer newsItemId = Integer.parseInt(newsItemIdStr);
			Boolean needArchive = Boolean.valueOf(needArchiveStr);
			setNewsItemArchived = NewsService.setNewsItemArchived(newsItemId,needArchive);
		}
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(setNewsItemArchived?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
