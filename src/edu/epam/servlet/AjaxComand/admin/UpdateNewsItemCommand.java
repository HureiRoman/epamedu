package edu.epam.servlet.AjaxComand.admin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.ImageType;
import edu.epam.constants.RoleType;
import edu.epam.model.NewsItem;
import edu.epam.persistance.ImageManager;
import edu.epam.service.NewsService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class UpdateNewsItemCommand  implements AjaxActionCommand{

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String title = null;
		String content = null;
		String description = null;
		Integer id = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		FileItem img = null;
		for(FileItem item : items) {
			String fieldName = item.getFieldName();
			if(item.isFormField()) {
				switch (fieldName) {
					case "title" : title = item.getString("UTF-8").trim(); break;
					case "content" : content = item.getString("UTF-8").trim(); break;
					case "description" : description = item.getString("UTF-8").trim(); break;
					case "id" : id = Integer.parseInt(item.getString("UTF-8").trim()); break;
				}
			} else {
				img = item;
			}
		}
		NewsItem newsItem =  new NewsItem();
		newsItem.setContent(content);
        newsItem.setDescription(description);
        newsItem.setMessageDate(Calendar.getInstance().getTime());
		newsItem.setTitle(title);
		Boolean updated = NewsService.updateNewsItemById(newsItem,id);
		if(updated){
			ImageManager.loadImage(request, img, ImageType.NEWS, id);
		}
		return "";
	}

}
