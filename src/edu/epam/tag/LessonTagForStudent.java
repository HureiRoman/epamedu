package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.constants.TypeOfTasks;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.manager.MessageManager;
import edu.epam.model.Attachment;
import edu.epam.model.Lesson;
import edu.epam.model.StudentAttachment;
import edu.epam.model.Task;
import edu.epam.model.Topic;
import edu.epam.persistance.TimeToText;
import edu.epam.service.AttachmentService;
import edu.epam.service.TaskService;
import edu.epam.service.TopicService;

public class LessonTagForStudent extends SimpleTagSupport {
	private Lesson lesson;
	private Integer userId;

	@Override
	public void doTag() throws JspException, IOException {
		try{
		Integer taskId = lesson.getTaskId();
		Integer topicId = lesson.getTopicId();
		Topic topic = null;
		Task task = null;
		StudentAttachment attachment = null;
		List<Attachment> teacherAttachments = new ArrayList<Attachment>();
		try {
			teacherAttachments = AttachmentService
					.getAttachmentsByTopicId(topicId);
			attachment = AttachmentService.getAttachmentForStudentByTaskId(
					taskId, userId);
			task = TaskService.getTaskById(taskId, TypeOfTasks.HW);
			topic = TopicService.getTopicById(topicId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int teacherId = topic.getTeacherId();
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder tagBuilder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		
		tagBuilder.append("<li>");
		tagBuilder.append("<div class=\"collapsible-header\">");
		tagBuilder.append("<i class=\"fa fa-connectdevelop small blue-text\"></i>"
				+ topic.getTitle() + " "
				+ TimeToText.timeToText(lesson.getLessonTime()));
		tagBuilder.append("</div>");
		tagBuilder.append("<div class=\"collapsible-body\">");
		tagBuilder.append("<div class=\"row\">");
		tagBuilder.append("<div class=\"col s5\">");
		tagBuilder.append("<div class=\"card-panel white \">");
		tagBuilder.append("<b><i class=\"fa fa-clock-o small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.LESSON_TIME, new Locale(locale))+"</b> <label> "
				+ TimeToText.timeToText(lesson.getLessonTime())
				+ " </label><br> <b><i class=\"fa fa-hand-o-right small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.TOPIC, new Locale(locale))+"</b> <label>" + topic.getTitle() + "");
		tagBuilder.append("</label><br><b><i class=\"fa fa-list-alt  small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.DESCRIPTION, new Locale(locale))+"</b>");
		tagBuilder
				.append("<blockquote>" + topic.getContent() + "</blockquote>");
		tagBuilder.append("</div>");
		tagBuilder.append("<div class=\"row card-panel white\">");
		if(task != null){
			tagBuilder.append("<b><i class=\"fa fa-home small blue-text\"></i>"+ MessageManager.getInstance().getProperty(MessageManager.HW, new Locale(locale))+"</b>");
			//Saniok added check for null
			tagBuilder.append("<blockquote>" + task.getTaskBody() + "</blockquote>");
			String dateDeadline = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(task.getDeadline());
			tagBuilder.append("<blockquote>"+MessageManager.getInstance().getProperty(MessageManager.DEADLINE, new Locale(locale))+" " + dateDeadline + "</blockquote>");
		
		
		tagBuilder.append("<b><i class=\"fa fa-check small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.MY_PERFORMANCE, new Locale(locale))+"</b>");
		if (attachment == null) {
			tagBuilder.append("<div class=\"file-field input-field\">");
			tagBuilder.append("<input  id=\"attachmentTitle" + taskId
					+ "\"  class=\"file-path validate\" type=\"text\"/>");
			tagBuilder.append("<div class=\"btn\">");
			tagBuilder.append("<span>"+MessageManager.getInstance().getProperty(MessageManager.FILES, new Locale(locale))+"</span>");
			tagBuilder.append("<input id=\"file" + taskId
					+ "\" type=\"file\" />");
			tagBuilder.append("</div>");
			tagBuilder.append("</div>");
			tagBuilder
					.append("<a onclick=\"sendStudentAttachment("
							+ taskId
							+ ",'HOMEWORK')\" class=\"btn-floating btn-small waves-effect waves-light right red\"><i class=\"mdi-action-done\"></i></a>");
		} else {
			tagBuilder.append("<div class=\"card-panel white \">");
			tagBuilder.append("<div class=\"row\">");
			tagBuilder.append("<img width=\"100\" height=\"100\" src=\""
					+ context + "/images?type=extensions&extension="
					+ attachment.getExtension() + "\"><br>");
			tagBuilder.append("<label>" + attachment.getTitle() + "</label>");
			tagBuilder.append("</div>");
			tagBuilder.append("<div class=\"row\">");
			tagBuilder
					.append("<a onclick=\"deleteAttachment("
							+ attachment.getId()
							+ ")\" class=\"btn-floating btn-small waves-effect waves-light red\"><i class=\"mdi-action-delete\"></i></a>");
			tagBuilder
					.append("<a href=\""
							+ context
							+ "/attachments?target=HOMEWORK&target_id="
							+ taskId
							+ "&attachment_id="
							+ attachment.getId()
							+ "\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
			
			 if(attachment.getExtension().equals(".docx") ||  attachment.getExtension().equals(".pdf")){
	        	  tagBuilder.append("<a onclick=\"openPreviewModal("+userId+",'HOMEWORK',"+taskId+","+attachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
	          }
			tagBuilder.append("</div>");
			tagBuilder.append("</div>");
		}
		}else{
			tagBuilder.append("<blockquote> No homework </blockquote>");
		}

		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("<div class=\"col s7\">");
		tagBuilder.append("<b><i class=\"fa fa-paperclip small blue-text\"></i>Files</b><br>");
		tagBuilder.append("<div class=\"card-panel white row\">");
		if (teacherAttachments != null && teacherAttachments.size() > 0) {
			for (Attachment teacherAttachment : teacherAttachments) {
				tagBuilder.append("<div class=\"col s3\">");
//				tagBuilder.append("<div class=\"card-panel white\" 	style=\"min-width: 150px; !important\">");
				tagBuilder.append("<div class=\"row\">");
				tagBuilder.append("<img width=\"100\" height=\"100\" src=\""
						+ context + "/images?type=extensions&extension="
						+ teacherAttachment.getExtension() + "\"><br>");
				tagBuilder.append("<label>" + teacherAttachment.getTitle()
						+ "</label>");
				tagBuilder.append("</div>");
				tagBuilder.append("<div class=\"row\">");
				tagBuilder.append("<a style=\"display:none;\"	onclick=\"showDocument('"
								+ context
								+ "/files?owner="
								+ teacherId
								+ "&owner_role="
								+ "TEACHER"
								+ "&target=topic&target_id="
								+ topicId
								+ "&attachment_id="
								+ teacherAttachment.getId()
								+ "&ext="
								+ teacherAttachment.getExtension()
								+ "')\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
				
				if(teacherAttachment.getExtension().equals(".docx") ||  teacherAttachment.getExtension().equals(".pdf")){
					tagBuilder.append("<a onclick=\"openPreviewModal("+teacherId+",'topic',"+topicId+","+teacherAttachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
				}
				tagBuilder.append("<a	href=\""
								+ context
								+ "/files?owner="
								+ teacherId
								+ "&owner_role="
								+ "TEACHER"
								+ "&target=topic&target_id="
								+ topicId
								+ "&attachment_id="
								+ teacherAttachment.getId()
								+ "\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
				tagBuilder.append("</div>");
				tagBuilder.append("</div>");
//				tagBuilder.append("</div>");
			}
		} else {
			tagBuilder.append("<div class=\"col s3\">");
//			tagBuilder.append("<div class=\"card-panel white\" 	style=\"min-width: 150px; !important\">");
			tagBuilder.append("<img width=\"100\" height=\"100\" src=\""
					+ context + "/img/nofiles.jpg\"  ");
			tagBuilder.append("<label>"+MessageManager.getInstance().getProperty(MessageManager.THERE_IS_NO_FILES, new Locale(locale))+"</label>");
			tagBuilder.append("</div>");
//			tagBuilder.append("</div>");
		}
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</li>");
		writer.write(tagBuilder.toString());
		
		}catch(NoDBConnectionsLongTime ex){
			ex.printStackTrace();
			System.out.println("No Db connection long time");
		}
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}