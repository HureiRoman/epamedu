package edu.epam.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.manager.MessageManager;
import edu.epam.model.GradesOfTask;
import edu.epam.model.StudentAttachment;
import edu.epam.model.Task;
import edu.epam.persistance.TimeToText;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;
import edu.epam.service.AttachmentService;
import edu.epam.service.CommonUserService;
import edu.epam.service.GradesOfTaskService;

public class TaskTagForStudent extends SimpleTagSupport {
	private Task task;
	private GradesOfTask grade;
	private CommonUser teacher;
	private StudentAttachment attachment;
	Student student;
	@Override
	public void doTag() throws JspException, IOException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		try{
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder tagBuilder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		attachment=AttachmentService.getAttachmentForStudentByTaskId(task.getId(), student.getId());
		if(attachment!=null)
			grade = GradesOfTaskService.getGradeBystudentId(student.getId(),task.getId());
		if(grade!=null) {teacher=CommonUserService.getUserById(grade.getTeacherId());
		}
		tagBuilder.append("<li>");
		if(attachment!=null)
		{tagBuilder.append("<div class=\"collapsible-header\" onclick=\"setColorForTag('"
				+task.getId()
				+"','"
				+ dateFormat.format(attachment.getDateAdded() )
				+"','"
				+dateFormat.format(task.getDeadline())
				+"')\">");
		}
		else {
			tagBuilder.append("<div class=\"collapsible-header\" onclick=\"setColorForTag(null,null)\">");
		}
		tagBuilder.append("<i class=\"large material-icons\">work</i>"
				+ task.getTitle());
		tagBuilder.append("</div>");
		tagBuilder.append("<div class=\"collapsible-body\">");
		tagBuilder.append("<div class=\"row\">");
		tagBuilder.append("<div class=\"col s6\">");
		tagBuilder.append("<div class=\"card-panel white \">");
		tagBuilder.append("<b>"+MessageManager.getInstance().getProperty(MessageManager.TASK_BODY, new Locale(locale))+" </b>");
		tagBuilder.append("<blockquote style=\"margin:2%; word-break: break-all;\">" + task.getTaskBody() + "</blockquote>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("<div class=\"col s6\">");
		tagBuilder.append("<div class=\"card-panel\" style=\"background-color:#F0F0F0 !important; \">");
		tagBuilder.append("<b>"+MessageManager.getInstance().getProperty(MessageManager.TASK, new Locale(locale))+"</b>");
		tagBuilder
				.append("<p style=\"margin:2%; padding:0px;\"><i class=\"fa fa-clock-o small blue-text \"></i>"+" <b>"+MessageManager.getInstance().getProperty(MessageManager.DEADLINE, new Locale(locale))+"</b> "+TimeToText.timeToText(task.getDeadline())+ "</p>");
		if(attachment!=null)
		{tagBuilder.append("<p id=\"fileCard");
		tagBuilder.append(task.getId());
				tagBuilder.append("\" style=\"color:white; width:75%; margin:2%; padding:0px;\"><i class=\"fa fa-calendar small blue-text \"></i>"+" <b>"+MessageManager.getInstance().getProperty(MessageManager.DATE_OF_LOADING, new Locale(locale))+" </b> "+TimeToText.timeToText(attachment.getDateAdded())+ "</p>");
		}
		if(grade!=null)
		tagBuilder
		.append("<p style=\"margin:2%; padding:0px;\"><i class=\"fa fa-thumbs-up small blue-text \"></i>"+" <b>"+MessageManager.getInstance().getProperty(MessageManager.MARK, new Locale(locale))+"</b> "+grade.getGrade()+" "+MessageManager.getInstance().getProperty(MessageManager.BY, new Locale(locale))+" "+teacher.getFirstName()+" "+teacher.getLastName()+ "</p>");
		
		tagBuilder.append("<b style=\"margin:2%;\"><i class=\"fa fa-file-text small blue-text \"></i>"+MessageManager.getInstance().getProperty(MessageManager.MY_EXECUTION, new Locale(locale))+" </b>");
		if (attachment == null) {
			tagBuilder.append("<div class=\"file-field input-field\">");
			tagBuilder.append("<input  id=\"attachmentTitle" + task.getId()
					+ "\"  class=\"file-path validate\" type=\"text\"/>");
			tagBuilder.append("<div class=\"btn\">");
			tagBuilder.append("<span>"+MessageManager.getInstance().getProperty(MessageManager.FILES, new Locale(locale))+"</span>");
			tagBuilder.append("<input id=\"file" + task.getId()
					+ "\" type=\"file\" />");
			tagBuilder.append("</div>");
			tagBuilder.append("</div>");
			tagBuilder
					.append("<a onclick=\"sendStudentAttachment("
							+ task.getId()
							+ ",'TASK')\" class=\"btn-floating btn-small waves-effect waves-light right red\"><i class=\"mdi-action-done\"></i></a> <br>");
		} else {
			tagBuilder.append("<div class=\"card-panel\" style=\"width:40%\" id=\"fileCard2");
			tagBuilder.append(task.getId());
					tagBuilder.append("\">");
			tagBuilder.append("<div class=\"row\">");
			tagBuilder.append("<img width=\"100\" height=\"100\" src=\""
					+ context + "/images?type=extensions&extension="
					+ attachment.getExtension() + "\"><br>");
			tagBuilder.append("<label style=\" word-break: break-all;\">" + attachment.getTitle() + "</label>");
			tagBuilder.append("</div>");
			tagBuilder.append("<div class=\"row\" style=\"text-align:center;\">");
			if(grade==null) {
			tagBuilder
					.append("<a onclick=\"");
			 tagBuilder.append("deleteAttachment("
							+ attachment.getId()
							+ ")\"");
			tagBuilder.append(" class=\"btn-floating btn-small waves-effect waves-light red \" ");
				tagBuilder.append(" ><i class=\"mdi-action-delete\"></i></a>");
			}
			tagBuilder
					.append("<a href=\""
							+ context
							+ "/attachments?target=TASK&target_id="
							+ task.getId()
							+ "&attachment_id="
							+ attachment.getId()
							+ "\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
			
			if(attachment.getExtension().equals(".docx")  || attachment.getExtension().equals(".pdf")){
	        	  tagBuilder.append("<a onclick=\"openPreviewModal("+student.getId()+",'TASK',"+task.getId()+","+attachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
	          }
			tagBuilder.append("</div>");
			tagBuilder.append("</div>");
		}

		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</li>");
		writer.write(tagBuilder.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
}
