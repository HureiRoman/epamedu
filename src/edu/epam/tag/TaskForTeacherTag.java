package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.manager.MessageManager;
import edu.epam.model.StudentAttachment;
import edu.epam.model.Task;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;
import edu.epam.service.StudentService;

public class TaskForTeacherTag extends SimpleTagSupport {
	private Task task;


	@Override
	public void doTag() throws JspException, IOException {
	try{	
		
		int taskId = task.getId();
		List<Student> listStudentsOfGroup = null;
		try {
			listStudentsOfGroup = StudentService.getListOfStudentsOfGroupWithTasks(task.getGroupId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Map<Integer, StudentAttachment> studentIdWithAttachment = new HashMap<>();
		if(task.getListOfStudentAttachmentsTasks() != null){
			for(StudentAttachment attachment : task.getListOfStudentAttachmentsTasks()){
				studentIdWithAttachment.put(attachment.getStudentId(), attachment);
			}
		}
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder tagBuilder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();
		CommonUser teacher = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		tagBuilder.append("<div id=\"taskContent"+taskId+"\" style=\"display:none;\">");
		tagBuilder.append("<div class=\"row\">");			

			tagBuilder.append("<blockquote class=\"col s9\" id=\"taskBlockquote"+taskId+"\"><i>"+MessageManager.getInstance().getProperty(MessageManager.TITLE, new Locale(locale))+" : <b style=\"word-break: break-all;\">"+task.getTitle()+"</b></i></br>");
			tagBuilder.append("<h6 style=\"word-break: break-all;\">"+task.getTaskBody()+"</h6></br>");
			 String dateDeadline = new SimpleDateFormat("dd-MM-yyyy о HH:mm").format(task.getDeadline());
			tagBuilder.append("<i>"+MessageManager.getInstance().getProperty(MessageManager.DEADLINE, new Locale(locale))+" <b>"+dateDeadline+"</b></i></blockquote>");
			
			tagBuilder.append("<div class=\"col s3 \">");
			tagBuilder.append("<a onclick=\"openDeleteStudentTaskModal("+taskId+")\" id=\"deleteStudentTask"+taskId+"\" class=\"btn-floating btn-large waves-effect waves-light red\" style=\"margin-right:2%; width:35px;height:35px;line-height: 41.5px;\"><i class=\"fa fa-minus-circle\" style=\"margin-left:0.8px\"></i></a>");
			tagBuilder.append("<b id=\"deleteTask"+taskId+"\" style=\"font-size:13px;\">"+MessageManager.getInstance().getProperty(MessageManager.DELETE_TASK, new Locale(locale))+"</b>");
			
			tagBuilder.append("<br><a onclick=\"openEditStudentTaskModal("+taskId+")\" id=\"editStudentTask"+taskId+"\" class=\"btn-floating btn-large waves-effect waves-light yellow\" style=\"margin-right:2%; width:35px;height:35px;line-height: 42px;\"><i class=\"fa fa-pencil-square-o\" style=\"margin-left:2.5px\" ></i></a>");
			tagBuilder.append("<b id=\"editTask"+taskId+"\" style=\"font-size:13px;\">"+MessageManager.getInstance().getProperty(MessageManager.EDIT_TASK, new Locale(locale))+"</b>");
									
			tagBuilder.append("</div>");
			
		tagBuilder.append("</div>");
		
		
		if(task.getListOfStudentAttachmentsTasks() == null){
			tagBuilder.append("<i>"+MessageManager.getInstance().getProperty(MessageManager.NO_TASK_DOWNLOADED, new Locale(locale))+"</i>");
		}else{
			if(listStudentsOfGroup != null){
				
				for(Student student : listStudentsOfGroup){
					tagBuilder.append("<div class=\"col s4\">");
						tagBuilder.append("<div class=\"card\" style=\"height:250px;\">");
							tagBuilder.append("<div class=\"card-image waves-effect waves-block waves-light\">");
								tagBuilder.append("<img class=\"activator\" src=\""+ context + "/images?type=users&id="+student.getId()+"\" style=\"height:160px;\">");
							tagBuilder.append("</div>");
						tagBuilder.append("<div class=\"card-content\" style=\"padding:10px;height:80px;\">");
							tagBuilder.append("<span class=\"card-title activator grey-text text-darken-4\" style=\"line-height:0px;font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</span>");
							tagBuilder.append("<p style=\"padding:0px;bottom:0px;position:absolute;\"><a class=\"waves-effect  waves-light modal-trigger\" href=\"#!\" onclick=\"openModalSendMess("+student.getId()+",'"+student.getFirstName()+"','"+student.getLastName()+"')\"><i class=\"fa fa-envelope-o\"></i></a></p>");
						tagBuilder.append("</div>");
						
					
					StudentAttachment attachment = studentIdWithAttachment.get(student.getId());
					
					if(attachment != null){
						if(attachment.getDateAdded().compareTo(task.getDeadline()) >= 0){
							  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:5px;background-color:rgb(252, 138, 97);\">");
							}else{
							  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:5px;background-color:rgb(154, 252, 97)\">");
							}
							  tagBuilder.append("<span class=\"card-title grey-text text-darken-4\" style=\"font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</i></span>");
					          tagBuilder.append("<div class=\"card-panel white \" style=\"padding:5px;margin:0px;height:150px;\" id=\"hw"+attachment.getId()+"\">");
					          tagBuilder.append("<div class=\"row\">");
					          tagBuilder.append("<img style=\"float:left;\" width=\"80\" height=\"80\" src=\""+ context + "/images?type=extensions&extension="+ attachment.getExtension() + "\">");
					          String date = new SimpleDateFormat("dd-MM-yyyy о HH:mm").format(attachment.getDateAdded());
					          tagBuilder.append("<div style=\"font-size:9px;height: 43px;\">"+MessageManager.getInstance().getProperty(MessageManager.SENDED, new Locale(locale))+"</br>"+date+"</div></br></br></br>");
					          
					          tagBuilder.append("<label style=\"padding-left:5px;\">"+attachment.getTitle()+"</label>");
					          tagBuilder.append("</div>");
					          tagBuilder.append("<div class=\"row\">");
					          tagBuilder.append("<div style=\"float:left;\">");
					          if(attachment.getExtension().equals(".docx") || attachment.getExtension().equals(".pdf")){
					        	  tagBuilder.append("<a onclick=\"openPreviewModal("+student.getId()+",'TASK',"+taskId+","+attachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
					          }
					          tagBuilder.append("<a  href=\""+ context+"/files?owner="+student.getId()+"&target=TASK&target_id="+taskId+"&attachment_id="+attachment.getId()+"\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
					          
					          int mark = 0;
					          if(attachment.getMark() != null){
					        	  mark = attachment.getMark();
					          }
					          tagBuilder.append("<div class=\"row\" style=\"float:right;margin:0px;margin-left:10px;\">");
					          tagBuilder.append("<div style=\"color:blue;\" >"+MessageManager.getInstance().getProperty(MessageManager.MARK, new Locale(locale))+" </div>");
					          tagBuilder.append("<div class=\"edit"+attachment.getId()+"\" onclick=\"processInputMark("+attachment.getId()+","+taskId+","+student.getId()+","+teacher.getId()+")\" id=\"mark"+attachment.getId()+"\" id=\""+attachment.getId()+"\">"+mark);
					          tagBuilder.append("</div>");
					          
					          tagBuilder.append("</div>");
					          
					          
					          tagBuilder.append("</div>");
//					          String dateDeadline = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(task.getDeadline());
//					          tagBuilder.append("<div style=\"font-size:10px;padding-left:3px;text-align:center;\">Deadline :</br>"+ dateDeadline+"</div>");
					          tagBuilder.append("</div>");
					          tagBuilder.append("</div>");
					          tagBuilder.append("</div>");
			          }else{
				        	  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:10px;background-color:rgb(242, 0, 0);\">");
				        	  tagBuilder.append("<span class=\"card-title grey-text text-darken-4\" style=\"font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</span>");
				        	  tagBuilder.append("<div class=\"card-panel white \" >");
					          tagBuilder.append("<div class=\"row\">");
					          tagBuilder.append("<img width=\"100\" height=\"100\" src=\""+ context + "/images?type=extensions&extension=.nohomework\"><br>");
					          
					          tagBuilder.append("</div>");
					          tagBuilder.append("</div>");
					          tagBuilder.append("</div>");
			          }
					
					   tagBuilder.append("</div>");
					   tagBuilder.append("</div>");
				}
			}else{
				tagBuilder.append("<div>"+MessageManager.getInstance().getProperty(MessageManager.NO_STUDENTS_IN_GROUP, new Locale(locale))+"</div>");
				//no students in group
			}
			
//			tagBuilder.append("</div>");
//			tagBuilder.append("</div>");
		}
		tagBuilder.append("</div>");
		
		
		writer.write(tagBuilder.toString());
		
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}
	
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
}