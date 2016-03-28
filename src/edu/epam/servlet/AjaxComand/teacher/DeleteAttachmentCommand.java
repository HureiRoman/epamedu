package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.AttachmentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class DeleteAttachmentCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer attachmentId=Integer.valueOf(request.getParameter("attachmentId"));
		boolean deleted=AttachmentService.deleteAttachmentById(attachmentId);
		System.out.println("DELETED"+deleted);
		request.setAttribute("topicId",request.getParameter("topicId"));
		
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <attachId>");
		responseBuilder.append(deleted);
		responseBuilder.append(" </attachId>");	
		System.out.println(responseBuilder);
		return responseBuilder.toString();
			}

}
