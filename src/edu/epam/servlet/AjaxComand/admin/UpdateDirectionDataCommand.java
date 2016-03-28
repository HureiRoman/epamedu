package edu.epam.servlet.AjaxComand.admin;

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
import edu.epam.constants.ImageType;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.persistance.ImageManager;
import edu.epam.service.DirectionService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class UpdateDirectionDataCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int status = 0;
		Integer directionId = null;
		String title = null;
		String description = null;
		String codeLanguge = null;
		
		
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
					case "description" : description = item.getString("UTF-8").trim(); break;	
					case "id" : directionId	 =  Integer.parseInt(item.getString("UTF-8").trim()); break;
					case "code_lang" : codeLanguge = item.getString("UTF-8").trim(); break;
				}
			} else {
				img = item;
			}
		}
		
		Direction direction =  new Direction();
		direction.setName(title);
		direction.setDescription(description);
		direction.setId(directionId);
		direction.setCodeLang(codeLanguge);
		boolean updated = DirectionService.updateDirectionData(direction);
		boolean imageLoaded = false;
		if(updated&&img!=null&&(img.getSize()>100)){
			System.out.println("image founded");
			imageLoaded = ImageManager.loadImage(request, img, ImageType.DIRECTIONS, directionId);
		}	
		
		if(imageLoaded){
			System.out.println("image for direction changed");
		}
		status=(updated)?1:0;
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(status);
		responseBuilder.append("</status>");
		
		return responseBuilder.toString();
	}
	

}
