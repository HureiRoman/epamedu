package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.ArrayList;
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

@UserPermissions({RoleType.ANY})	
public class ProcessMultipleGradAndGrop implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		System.out.println("BOOOOOOOOOOOOOOOOOOOOOOO");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		List<FileItem> items = upload.parseRequest(request);
		List<FileItem> attachments=new ArrayList<FileItem>();
		List<String>  fileTitles=new ArrayList<String>();
		for(FileItem item : items) {
			String fieldName = item.getFieldName();
			System.out.println(fieldName);
			attachments.add(item);
		}
		return "";
	}

}
