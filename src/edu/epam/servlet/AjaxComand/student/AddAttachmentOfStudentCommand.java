package edu.epam.servlet.AjaxComand.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.AttachmentType;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.StudentAttachment;
import edu.epam.persistance.AttachmentManager;
import edu.epam.role.CommonUser;
import edu.epam.service.AttachmentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.STUDENT})
public class AddAttachmentOfStudentCommand implements AjaxActionCommand {
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 15; // 15 MB
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		CommonUser user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
	    String attachmentTitle = null;
		Integer attachmentId = null;
		Integer taskId = null;
		AttachmentType attachmentType = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		FileItem attachmentFile = null;
		for(FileItem item : items) {
			String fieldName = item.getFieldName();
			if(item.isFormField()) {
				switch (fieldName) {
					case "taskId" : taskId = Integer.parseInt(item.getString("UTF-8").trim()); break;
					case "taskType" : attachmentType = AttachmentType.valueOf(item.getString("UTF-8").trim()); break;
					case "attachmentTitle" : attachmentTitle = item.getString("UTF-8").trim(); break;
					
				}
			} else {
				attachmentFile = item;
			}
		}
		if(attachmentTitle.contains(".")){
			attachmentTitle = attachmentTitle.substring(0,attachmentTitle.lastIndexOf('.'));
		}
		StudentAttachment attachment =  new StudentAttachment();
		attachment.setDateAdded(new Date());
		attachment.setStudentId(user.getId());
		attachment.setTaskId(taskId);
		attachment.setTitle(attachmentTitle);
		String extension = attachmentFile.getName().substring(attachmentFile.getName().lastIndexOf('.'));
		attachment.setExtension(extension);
		boolean attachmentAdded = false;
		boolean attachmentAddedDeployed = false;
		if(checkAttachment(attachmentFile)){
			attachmentId = AttachmentService.addStudentAttachment(attachment);
			attachmentAdded = AttachmentManager.addAttachment(request, attachmentFile, RoleType.STUDENT, user.getId() , attachmentType, taskId, attachmentId);
			System.out.println("ADDIN FILE STUDENT HW! 1");
//			attachmentAddedDeployed = DeployedFile.addAttachment(request,attachmentFile, RoleType.STUDENT, user.getId() , attachmentType, taskId, attachmentId);

		}
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(attachmentAdded?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}
	private static boolean checkAttachment(FileItem item) {
		String fileName = item.getName();
		long fileSize = item.getSize();
		List<String> extensions = new ArrayList<>();
		extensions.add("doc");
		extensions.add("ppt");
		extensions.add("pptx");
		extensions.add("pdf");
		extensions.add("docx");
		extensions.add("zip");
		extensions.add("rar");
		extensions.add("7z");
		extensions.add("jar");
		extensions.add("txt");
		extensions.add("xsl");
		extensions.add("xslx");
		extensions.add("jpg");
		extensions.add("jpeg");
		extensions.add("png");
		extensions.add("gif");

		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		if (!extensions.contains(extension.toLowerCase()) || fileSize > MAX_FILE_SIZE) {
			return false;
		}
		return true;
	}
	
	
}
