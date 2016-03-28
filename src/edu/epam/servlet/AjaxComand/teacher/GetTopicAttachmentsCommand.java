package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Attachment;
import edu.epam.model.Topic;
import edu.epam.role.CommonUser;
import edu.epam.service.AttachmentService;
import edu.epam.service.TopicService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class GetTopicAttachmentsCommand implements AjaxActionCommand {

		@Override
		public String execute(HttpServletRequest request,
				HttpServletResponse response, Locale locale)
				throws ServletException, IOException, Exception {
			
			StringBuilder responseBuilder = new StringBuilder();
			responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
			
			
			CommonUser loginedUser = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
			String topicId = request.getParameter("topicId");
			List<Attachment> teacherAttachments = null;
			Topic topicObj = null;
			
			if(loginedUser != null && topicId != null){
				int topic_id = Integer.valueOf(topicId);
				int userId = loginedUser.getId();
				
				teacherAttachments = AttachmentService.getAttachmentsByTopicId(topic_id);
				topicObj = TopicService.getTopicById(topic_id);
				System.out.println("teacher Attachment " + teacherAttachments);
				JsonArrayBuilder jsonArrayOfAttachmentsBuilder = Json.createArrayBuilder();
				for (Attachment attachment : teacherAttachments) {
					JsonObject attachmentJsonObject = Json.createObjectBuilder()
							.add("extension", attachment.getExtension())
							.add("title", attachment.getTitle())
							.add("id", attachment.getId())
							.add("teacherId",userId )
							.build();
					jsonArrayOfAttachmentsBuilder.add(attachmentJsonObject);
				}
				
				JsonArray jsonArrayOfAttachments = jsonArrayOfAttachmentsBuilder.build();
				
				JsonObjectBuilder arrayOfTopicsObject = Json.createObjectBuilder().add("files", jsonArrayOfAttachments);
				if(topicObj != null){
					arrayOfTopicsObject.add("topicContent",topicObj.getContent())
					.add("topicTitle",topicObj.getTitle());
				}
				
				return arrayOfTopicsObject.build().toString();
			}
			return "";
		}

	}
