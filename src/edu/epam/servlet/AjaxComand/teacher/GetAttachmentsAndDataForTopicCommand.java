package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Attachment;
import edu.epam.model.Topic;
import edu.epam.service.AttachmentService;
import edu.epam.service.TopicService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class GetAttachmentsAndDataForTopicCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer topicId=Integer.valueOf(request.getParameter("topicId"));
		Topic topic=TopicService.getTopicById(topicId);
		List<Attachment> listOfAttacments=AttachmentService.getAttachmentsByTopicId(topicId);

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <generalTag>");
		responseBuilder.append(" <topics>");
		responseBuilder.append(" <topicTitle>");
		responseBuilder.append(topic.getTitle());
		responseBuilder.append("</topicTitle>");
		responseBuilder.append(" <content>");
		responseBuilder.append(topic.getContent());
		responseBuilder.append("</content>");
		responseBuilder.append(" <direction>");
		responseBuilder.append(topic.getDirectionId());
		responseBuilder.append("</direction>");
		responseBuilder.append(" <Id>");
		responseBuilder.append(topic.getId());
		responseBuilder.append("</Id>");
		responseBuilder.append(" </topics>");
		responseBuilder.append("<ListOfattachments>");
		for (int i = 0; i < listOfAttacments.size(); i++) {
			responseBuilder.append("<attachments>");
			responseBuilder.append(" <title>");
			responseBuilder.append(listOfAttacments.get(i).getTitle());
			responseBuilder.append("</title>");	
			responseBuilder.append(" <extension>");
			responseBuilder.append(listOfAttacments.get(i).getExtension());
			responseBuilder.append("</extension>");	
			responseBuilder.append(" <attachId>");
			responseBuilder.append(listOfAttacments.get(i).getId());
			responseBuilder.append("</attachId>");	
			responseBuilder.append(" <topicId>");
			responseBuilder.append(topic.getId());
			responseBuilder.append("</topicId>");
			responseBuilder.append("</attachments>");
		}
		responseBuilder.append("</ListOfattachments>");
		responseBuilder.append(" </generalTag>");	
		System.out.println(responseBuilder);
		return responseBuilder.toString();
	}

}
