package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Advertisement;
import edu.epam.service.AdvertisementService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class UpdateAdvertisementComand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		boolean res;

		Integer id = null;
		String title = null;
		String content = null;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);

		for (FileItem item : items) {
			String fieldName = item.getFieldName();

			if (item.isFormField()) {
				switch (fieldName) {
				case "id":
					id = Integer.parseInt(item.getString("UTF-8").trim());
					break;
				case "title":
					title = item.getString("UTF-8").trim();
					break;
				case "content":
					content = item.getString("UTF-8").trim();
					break;
				}
			}
		}

		Advertisement advertisement = new Advertisement();
		advertisement.setId(id);
		advertisement.setTitle(title);
		advertisement.setContent(content);

		res = AdvertisementService.updateAdvertisement(advertisement);

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(res);
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}

}
