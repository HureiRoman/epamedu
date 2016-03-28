package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Advertisement;
import edu.epam.role.Teacher;
import edu.epam.service.AdvertisementService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class CreateAdvertisementComand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer res = 0;

		String title = null;
		String content = null;
		Integer idGroup = null;
		Integer idTeacher = null;
		
		HttpSession session = request.getSession();
		Teacher teacher = (Teacher) session.getAttribute("logined_user");
		idTeacher = teacher.getId();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		
		for (FileItem item : items) {
			String fieldName = item.getFieldName();

			if (item.isFormField()) {
				switch (fieldName) {
				case "title":
					title = item.getString("UTF-8").trim();
					break;
				case "content":
					content = item.getString("UTF-8").trim();
					break;
				case "group_id":
					idGroup = Integer.parseInt(item.getString("UTF-8").trim());
					break;
				}
			}
		}

		Advertisement advertisement = new Advertisement();
		advertisement.setTitle(title);
		advertisement.setContent(content);
		advertisement.setIdGroup(idGroup);
		advertisement.setIdTeacher(idTeacher);
		
		res = AdvertisementService.createNewAdvertisement(advertisement);
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(res);
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}
}
