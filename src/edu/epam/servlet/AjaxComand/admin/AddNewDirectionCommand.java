package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.ImageType;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.persistance.ImageManager;
import edu.epam.service.DirectionService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.ADMIN })
public class AddNewDirectionCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int status = 0;
		String title = null;
		String description = null;
		Integer hrId = null;
		String codeLang = null;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		FileItem img = null;

		for (FileItem item : items) {
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				switch (fieldName) {
				case "title":
					title = item.getString("UTF-8").trim();
					break;
				case "description":
					description = item.getString("UTF-8").trim();
					break;
				case "hr_id":
					hrId = Integer.parseInt(item.getString("UTF-8").trim());
					break;
				case "code_lang":
					codeLang = item.getString("UTF-8").trim();
					break;
				}
			} else {
				img = item;
			}
		}

		Direction direction = new Direction();
		direction.setName(title);
		direction.setRecruter_id(hrId);
		direction.setDescription(description);
		direction.setCodeLang(codeLang);
		Integer directionId = DirectionService.createDirection(direction);
		boolean imageLoaded = false;
		if (directionId != null) {
			imageLoaded = ImageManager.loadImage(request,img, ImageType.DIRECTIONS,
					directionId);
		}

		if (imageLoaded) {
			System.out.println("image for direction loaded");
		}
		status = (directionId != null && imageLoaded) ? 1 : 0;

		JsonObject directionObject = Json.createObjectBuilder()
				.add("id", directionId)
				.add("title", direction.getName())
				.build();
		JsonObjectBuilder responseBuilder = Json.createObjectBuilder()
				.add("status", status)
				.add("direction", directionObject);
		
		return responseBuilder.build().toString();
	}
}
