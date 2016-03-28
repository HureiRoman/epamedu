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
import edu.epam.model.Topic;
import edu.epam.persistance.AttachmentManager;
import edu.epam.role.Teacher;
import edu.epam.service.AttachmentService;
import edu.epam.service.TopicService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class EditTopicCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int result = 1;
		StringBuilder responseBuilder = new StringBuilder();

		String title = null;
		String content = null;
		String temp=null;
		Integer direction = null;
		Integer topicId=null;
		
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
					case "direction" :{temp = item.getString("UTF-8").trim();
					if(!temp.equals("null"))
						{ 	direction=Integer.valueOf(temp); }
					break; }
					case "topicId" :{temp = item.getString("UTF-8").trim();
					if(!temp.equals("null"))
						{ 	topicId=Integer.valueOf(temp); }
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
	
			Topic topic = new Topic();
			Teacher teacher=null;
		if(title.equals("")  || content.equals("")  ||
				direction  == null|| topicId==null) {
				result = 2;
		}
		else {
		
				System.out.println(title);
				topic.setTitle(title);
				System.out.println(content);
				topic.setContent(content);
				System.out.println(direction);
				topic.setDirectionId(direction);
				topic.setId(topicId);
				teacher = (Teacher)request.getSession().getAttribute(edu.epam.constants.Constants.SESSION_PARAM_NAME_USER);
				topic.setTeacherId(teacher.getId());
				TopicService.updateTopic(topic);
		}
		
		
		Attachment attachment=new Attachment();
		System.out.println("Attachments "+attachments);
		ArrayList<Integer> listWithAttachmentId=new ArrayList<>();
		for(int i=0;i<attachments.size();i++) {
			System.out.println(attachments.get(i).getName().substring(attachments.get(i).getName().lastIndexOf('.'),attachments.get(i).getName().length()));
			attachment.setExtension(attachments.get(i).getName().substring(attachments.get(i).getName().lastIndexOf('.'),attachments.get(i).getName().length()));
	     	attachment.setTitle(attachments.get(i).getName().substring(0,attachments.get(i).getName().lastIndexOf('.')));
	     	attachment.setDirectionId(direction);
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
		//pop-up window to validate the account via email
		//return сторінку, де юзер реєструвався
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
