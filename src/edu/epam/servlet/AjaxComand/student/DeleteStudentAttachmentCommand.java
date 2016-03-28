package edu.epam.servlet.AjaxComand.student;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.AttachmentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.STUDENT})
public class DeleteStudentAttachmentCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		String attachmentIdStr = request.getParameter("attachmentId");
		Integer attachmentId = null;
		if (attachmentIdStr != null) {
			attachmentId = Integer.parseInt(attachmentIdStr);
		}
		boolean attachmentDeleted = false;
		if (attachmentId != null) {
			attachmentDeleted = AttachmentService
					.deleteStudentAttachmentById(attachmentId);
		}
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(attachmentDeleted ? "1" : "0");
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}

}
