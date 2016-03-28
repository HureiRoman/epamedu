package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.NewsItem;
import edu.epam.service.NewsService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class GetNewsItemDataCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		String idStr = request.getParameter("id");
		StringBuilder responseBuilder = new StringBuilder();
		if(idStr!=null){
			Integer newsItemId = Integer.parseInt(idStr);
			System.out.println("-----------"+newsItemId);
			NewsItem newsItem = NewsService.getNewsItemById(newsItemId);
			System.out.println(newsItem);
			XStream stream = new  XStream(new StaxDriver());
			stream.alias("newsItem", NewsItem.class);
			String xml = stream.toXML(newsItem);
			responseBuilder.append(xml);
			return responseBuilder.toString();
		}
		responseBuilder.append("<error>");
		responseBuilder.append("</error>");
		return responseBuilder.toString();
	}

}
