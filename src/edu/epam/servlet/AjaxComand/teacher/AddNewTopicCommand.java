package edu.epam.servlet.AjaxComand.teacher;

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
import edu.epam.constants.AttachmentType;
import edu.epam.constants.RoleType;
import edu.epam.model.Attachment;
import edu.epam.model.Direction;
import edu.epam.model.Topic;
import edu.epam.persistance.AttachmentManager;
import edu.epam.role.Teacher;
import edu.epam.service.AttachmentService;
import edu.epam.service.DirectionService;
import edu.epam.service.TopicService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class AddNewTopicCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int result = 1;
		StringBuilder responseBuilder = new StringBuilder();

		String title = null;
		String content = null;
		String tempDirection=null;
		Integer directionId = null;
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		List<FileItem> items = upload.parseRequest(request);
		List<FileItem> attachments=new ArrayList<FileItem>();
			for(FileItem item : items) {
			String fieldName = item.getFieldName();
			System.out.println(fieldName);
				switch (fieldName) {
					case "title" : title = item.getString("UTF-8").trim(); break;
					case "content" : content = item.getString("UTF-8").trim(); break;
					case "direction" :{tempDirection = item.getString("UTF-8").trim();
					if(!tempDirection.equals("null"))
						{ 	directionId=Integer.valueOf(tempDirection); }
					break; }
					default: {	
						if(AttachmentManager.checkAttachment(item) == false){
							responseBuilder.append(" <result>");
							responseBuilder.append("<status>");
							responseBuilder.append(3); // Incorrect format
							responseBuilder.append("</status>");
							responseBuilder.append(" </result>");
							return responseBuilder.toString();
						}
						attachments.add(item);
						}
					}
		}
	//String fileName=attachments.get(0).getName().substring(0,attachments.get(0).getName().indexOf('.'));
	String fileExtension=null;
	String file_title=null;
	response.setContentType("application/xml");
	response.setCharacterEncoding("UTF-8");
	responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
	
			Topic topic = new Topic();
			Integer topicId=null;
			Teacher teacher=null;
		if(title.equals("")  || content.equals("")  ||
				directionId  == null) {
				result = 2;
				responseBuilder.append(" <result>");
				responseBuilder.append(" <status>");
				responseBuilder.append(result);
				responseBuilder.append("</status>");
				responseBuilder.append(" </result>");
				return responseBuilder.toString();
		}
		else {
		
				System.out.println(title);
				topic.setTitle(title);
				System.out.println(content);
				topic.setContent(content);
				System.out.println(directionId);
				topic.setDirectionId(directionId);
				teacher = (Teacher)request.getSession().getAttribute(edu.epam.constants.Constants.SESSION_PARAM_NAME_USER);
				topic.setTeacherId(teacher.getId());
				topicId=TopicService.createTopic(topic);
		}
		
		
		Attachment attachment=new Attachment();
		System.out.println("Attachments "+attachments);
		ArrayList<Integer> listWithAttachmentId=new ArrayList<>();
		for(int i=0;i<attachments.size();i++) {
			System.out.println(attachments.get(i).getName().substring(attachments.get(i).getName().indexOf('.'),attachments.get(i).getName().length()));
			attachment.setExtension(attachments.get(i).getName().substring(attachments.get(i).getName().lastIndexOf('.'),attachments.get(i).getName().length()));
	     	attachment.setTitle(attachments.get(i).getName().substring(0,attachments.get(i).getName().indexOf('.')));
	     	attachment.setDirectionId(directionId);
	     	attachment.setTopicId(topicId); 
	     	listWithAttachmentId.add(AttachmentService.createNewAttachment(attachment));
		}
		System.out.println(listWithAttachmentId);
			for(int i=0;i<attachments.size();i++)
			if(attachments.get(i) != null) {
				if(result == 1) {
					 AttachmentManager.addAttachment(request, attachments.get(i),RoleType.TEACHER, teacher.getId(), AttachmentType.TOPIC, topicId, listWithAttachmentId.get(i));
					
					
				}
			}
			System.out.println("K-t6"+attachments.size());
			Direction direction=DirectionService.getDirectionById(directionId);


		responseBuilder.append(" <result>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append(" <topicId>");
		responseBuilder.append(topicId);
		responseBuilder.append("</topicId>");
		responseBuilder.append(" <directionName>");
		responseBuilder.append(direction.getName());
		responseBuilder.append("</directionName>");
		responseBuilder.append("<directionId>");
		responseBuilder.append(direction.getId());
		responseBuilder.append("</directionId>");
		for(int i=0;i<attachments.size();i++)
		{ responseBuilder.append("<files>");
			responseBuilder.append("<fileExt>");
			responseBuilder.append(attachments.get(i).getName().substring(attachments.get(i).getName().lastIndexOf('.'),attachments.get(i).getName().length()));
			responseBuilder.append("</fileExt>");
			responseBuilder.append("<fileId>");
			responseBuilder.append(listWithAttachmentId.get(i));
			responseBuilder.append("</fileId>");
			responseBuilder.append("<fileName>");
			responseBuilder.append(attachments.get(i).getName().substring(0,attachments.get(i).getName().lastIndexOf('.')));
			responseBuilder.append("</fileName>");
			responseBuilder.append("</files>");
		}
			responseBuilder.append(" </result>");
		return responseBuilder.toString();
	}
}
