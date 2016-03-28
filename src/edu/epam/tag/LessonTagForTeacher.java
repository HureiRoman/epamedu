package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
import edu.epam.role.Student;
import edu.epam.service.AttachmentService;
import edu.epam.service.TaskService;
import edu.epam.service.TopicService;

public class LessonTagForTeacher extends SimpleTagSupport {
	private Lesson lesson;
	private List<Student> students;
	private int teacherId;
	private int directionId;

	@Override
	public void doTag() throws JspException, IOException {
		try{
		Integer taskId = lesson.getTaskId();
		Integer topicId = lesson.getTopicId();
		Topic topic = null;
		Task task = null;
		List<Topic> allTeacherTopicByDirection = null;
		List<StudentAttachment> studentAttachments = null;
		List<Attachment> teacherAttachments = null;
		Map<Integer,StudentAttachment> student_attachment_map = null; 
		
		
		try {
			teacherAttachments = AttachmentService.getAttachmentsByTopicId(topicId);
			studentAttachments = AttachmentService.getStudentAttachmentsForTopic(taskId);
			
			
			allTeacherTopicByDirection = TopicService.getTeacherTopicsByDirection(teacherId,directionId);
			task = TaskService.getTaskById(taskId, TypeOfTasks.HW);
			topic = TopicService.getTopicById(topicId);
			
			student_attachment_map = new HashMap<Integer, StudentAttachment>();
			if(studentAttachments != null){
				for(StudentAttachment attachment : studentAttachments){
					student_attachment_map.put(attachment.getStudentId(), attachment);
				}
			}
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
		
		tagBuilder.append("<li id=\"lesson"+lesson.getId()+"\">");
		tagBuilder.append("<div class=\"collapsible-header\">");
		String time = MessageManager.getInstance().getProperty(MessageManager.HOUR, new Locale(locale));
		String lessonTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(lesson.getLessonTime());
		String split [] = lessonTime.split(" ");
		lessonTime = split[0] + " " + time+" " + split[1];
			tagBuilder.append("<div class=\"col s6\">");
			tagBuilder.append("<i class=\"fa fa-connectdevelop small blue-text\"></i>"
					+ topic.getTitle() + MessageManager.getInstance().getProperty(MessageManager.DATE, new Locale(locale))+" " + lessonTime);
			tagBuilder.append("</div>");
//		tagBuilder.append("<div class=\"col s3 \" id=\"delete_lesson\" onclick=\"deleteLesson("+ lesson.getId()+")\">");

		//Added by Pavlo
		/*System.out.println("Lesson Time - " + lesson.getLessonTime());
		System.out.println("Today - " + new Date());
		System.out.println("lesson.getLessonTime().before(new Date()) - " + lesson.getLessonTime().before(new Date()));
		System.out.println(">=" + lesson.getLessonTime().compareTo(new Date()));
		*/
		if(lesson.getLessonTime().before(new Date())) {
			tagBuilder.append("<form action=\"/EpamEducationalProject/Controller?command=showVisiting\"  method=\"POST\">");
			tagBuilder.append("<input type=\"hidden\" name=\"lesson_id\" value=\"" + lesson.getId() + "\">");
			tagBuilder.append("<input class=\"col s3 pink_round_button\" type=\"submit\" onclick=\"event.stopPropagation();\" value=\"" + MessageManager.getInstance().getProperty(MessageManager.SET_VISITING, new Locale(locale)) +"\">");
			tagBuilder.append("</form>");
		}
		//end

				tagBuilder.append("<div class=\"col s3 \" id=\"delete_lesson\" onclick=\"openModalDeleteLesson(event,"+ lesson.getId()+")\">");
		tagBuilder.append("<i class=\"mdi-action-delete\" style=\"line-height: 1.5;\"></i>"+ MessageManager.getInstance().getProperty(MessageManager.DELETE_LESSON, new Locale(locale)));
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		
		
		tagBuilder.append("<div class=\"collapsible-body\">");

		tagBuilder.append("<div class=\"row\">");
		tagBuilder.append("<div class=\"row\" style=\"margin-bottom:0px;\">");
		tagBuilder.append("<div class=\"col s5\" style=\"padding-top:30px;padding-bottom:40px;\">");
		
		tagBuilder.append("<div class=\"row\">");
		tagBuilder.append("<div class=\"col s6\" style=\"padding:0px;\">");
		tagBuilder.append("<div class=\"switch\" >");
		tagBuilder.append("<label class=\"col s12\"> "+MessageManager.getInstance().getProperty(MessageManager.OPEN_TO_GROUP, new Locale(locale))+"</br> ");
		tagBuilder.append("<label>Off");
		tagBuilder.append("<input onchange=\"changeLessonStatus("+lesson.getId()+",$(this).is(':checked') )\" ");
		System.out.println("VISIBLE ? = " + lesson.isVisible());
		if(lesson.isVisible()){
			tagBuilder.append("checked ");
		}
		tagBuilder.append("type=\"checkbox\">");
		tagBuilder.append("<span class=\"lever\"></span>On");
		tagBuilder.append("</label></div>");
		
		tagBuilder.append("</div>");
		if(new Date().before(lesson.getLessonTime())){
		
			tagBuilder.append("<div class=\"col s6\" style=\"padding:0px;\">");
			tagBuilder.append("<div class=\"switch\" >");
			tagBuilder.append("<label class=\"col s12\"> "+MessageManager.getInstance().getProperty(MessageManager.AUTOMATICALLY_TURN_ON, new Locale(locale))+" </br> ");
			tagBuilder.append("<label>Off");
			tagBuilder.append("<input onchange=\"autoTurnOnLesson("+lesson.getId()+",$(this).is(':checked') )\" id=\"autoOnSwitcher"+lesson.getId()+"\"");
			System.out.println("VISIBLE ? = " + lesson.isVisible());
			if(lesson.isAutoTurnOn()){
				tagBuilder.append("checked ");
			}
			tagBuilder.append("type=\"checkbox\">");
			tagBuilder.append("<span class=\"lever\"></span>On");
			tagBuilder.append("</label></div>");
			
			tagBuilder.append("</div>");
		}
		
		tagBuilder.append("</div>");
	
		
		tagBuilder.append("<div class=\"card-panel white \">");
		tagBuilder.append("<b><i class=\"fa fa-clock-o small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.LESSON_TIME, new Locale(locale))+": </b> <label> "
				+ lessonTime + " </label><br> <b><i class=\"fa fa-hand-o-right small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.TOPIC, new Locale(locale))+"</b> <label id=\"topicTitle"+lesson.getId()+"\">"
				+ topic.getTitle() + "</label><br>");
		tagBuilder.append(" <b><i class=\"fa fa-list-alt  small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.DESCRIPTION, new Locale(locale))+"</b>");
		tagBuilder.append("<blockquote  style=\"word-break: break-all;\"  id=\"topicDescription"+lesson.getId()+"\">" + topic.getContent() + "</blockquote>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		
		tagBuilder.append("<div class=\"col s7\">");
		tagBuilder.append("<div class=\"row\" style=\"margin:0px;\"><div class=\"col s2\" style=\"margin:2%;\"><b>Files</b></div>");
		if(allTeacherTopicByDirection != null){
			tagBuilder.append("<div class=\"input-field col s6\">");
			tagBuilder.append("<select id=\"changeTopic"+lesson.getId()+"\" onchange=\"changeTopicInLessonAction("+lesson.getId()+")\">");
			tagBuilder.append("<option value=\"\" disabled selected>"+MessageManager.getInstance().getProperty(MessageManager.CHOOSE_TOPIC, new Locale(locale))+"</option>");
				
				for(Topic currentTopic : allTeacherTopicByDirection){
					System.out.println(currentTopic.getId());
					tagBuilder.append("<option value=\""+currentTopic.getId()+"\">"+currentTopic.getTitle()+"</option>");
				}
			tagBuilder.append("</select>");
			if(teacherAttachments != null && teacherAttachments.size()>0){
				tagBuilder.append("<label>"+MessageManager.getInstance().getProperty(MessageManager.CHANGE_TOPIC, new Locale(locale))+"</label>");
			}else{
				tagBuilder.append("<label>"+MessageManager.getInstance().getProperty(MessageManager.ADD_TOPIC, new Locale(locale))+"</label>");
			}
			tagBuilder.append(" </div>");
			
//			tagBuilder.append("<div class=\"col s3\" style=\"margin-top:3%;\">");
//				tagBuilder.append("<a onclick=\"changeTopicInLessonAction("+lesson.getId()+")\" id=\"changeTopicButton"+lesson.getId()+"\" class=\"btn-floating btn-large waves-effect waves-light red\" style=\"display:none; width:35px;height:35px;line-height: 46px;\"><i class=\"mdi-image-rotate-right\"></i></a>");
//			tagBuilder.append(" </div>");
		
		}	
		
		tagBuilder.append("</div>");
		
		
		tagBuilder.append("<div class=\"card-panel white row\">");
		
		tagBuilder.append("<div class=\"col s12\">");
		tagBuilder.append("<div class=\"card-panel white\" 	style=\"min-width: 150px; !important\">");
		tagBuilder.append("<div class=\"row\" id=\"teacherTopicFiles"+lesson.getId()+"\">");
		if(teacherAttachments != null && teacherAttachments.size()>0){
			
			for (Attachment teacherAttachment : teacherAttachments) {
				tagBuilder.append("<div class=\"col s3\">");
				tagBuilder.append("<img width=\"100\" height=\"100\" src=\""
						+ context + "/images?type=extensions&extension="
						+ teacherAttachment.getExtension() + "\"><br>");
				tagBuilder.append("<label>" + teacherAttachment.getTitle()
						+ "</label>");
				tagBuilder.append("<div class=\"row\">");
				if(teacherAttachment.getExtension().equals(".docx") || teacherAttachment.getExtension().equals(".pdf")){
					tagBuilder.append("<a onclick=\"openPreviewModal("+teacherId+",'topic',"+topicId+","+teacherAttachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
				}
				tagBuilder.append("<a	href=\""+ context+ "/attachments?owner="+ teacherId+ "&target=topic&target_id="+ topicId
								+ "&attachment_id="
								+ teacherAttachment.getId()
								+ "\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
				tagBuilder.append("</div>");
				tagBuilder.append("</div>");
			}
		}else{
			System.out.println("ELSE");
			tagBuilder.append("<img width=\"100\" height=\"100\" src=\""+ context + "/images?type=extensions&extension=.no_attachments\"><br>");
			tagBuilder.append("<label> "+MessageManager.getInstance().getProperty(MessageManager.NO_ATTACHMENTS, new Locale(locale))+"</label>");
		}
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		
		tagBuilder.append("<div class=\"col s12\" >");
		tagBuilder.append("<div class=\"row card-panel white\">");
		
		tagBuilder.append("<div class=\"row\" >");
			tagBuilder.append("<div class=\"col s9\">");
				tagBuilder.append("<b><i class=\"fa fa-home small blue-text\"></i>"+MessageManager.getInstance().getProperty(MessageManager.STUDENTS_HOMEWORKS, new Locale(locale))+"</b>");
			tagBuilder.append("</div>");
			tagBuilder.append("<div class=\"col s3\">"); 
				tagBuilder.append("<a onclick=\"editHomeWork("+lesson.getId()+","+lesson.getTaskId()+")\" id=\"editHomeWork"+lesson.getId()+"\" class=\"btn-floating btn-large waves-effect waves-light green\" style=\"margin-right:2%; width:35px;height:35px;line-height: 46px;\"><i class=\"mdi-image-rotate-right\"></i></a>");
				if (task == null) {
					tagBuilder.append("<b id=\"editHWbut"+lesson.getId()+"\" style=\"font-size:13px;\">"+MessageManager.getInstance().getProperty(MessageManager.ADD_HOMEWORK, new Locale(locale))+"</b>");
				}else{
					tagBuilder.append("<b id=\"editHWbut"+lesson.getId()+"\" style=\"font-size:13px;\">"+MessageManager.getInstance().getProperty(MessageManager.EDIT_HOMEWORK, new Locale(locale))+"</b>");
				}
			tagBuilder.append("</div>");
		tagBuilder.append("</div>");
		if (task == null) { 
			tagBuilder.append("<div class=\"file-field input-field\" id=\"isHomeworkPresent"+lesson.getId()+"\">");
			tagBuilder.append("<blockquote id=\"blockquote"+lesson.getId()+"\">"+MessageManager.getInstance().getProperty(MessageManager.YOU_DIDNOT_GIVE_HOMEWORK, new Locale(locale))+"</blockquote>");
			tagBuilder.append("</div>");
		} else if (studentAttachments == null) {
			String dateDeadline = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(task.getDeadline());
			tagBuilder.append("<div class=\"file-field input-field\" id=\"isHomeworkPresent"+lesson.getId()+"\">");
			tagBuilder.append("<blockquote id=\"blockquote"+lesson.getId()+"\"><i>"+MessageManager.getInstance().getProperty(MessageManager.TITLE, new Locale(locale))+" <b id=\"taskTitle"+lesson.getId()+"\">"+task.getTitle()+"</b></i></br><h6 style=\"word-break: break-all;\" id=\"taskBody"+lesson.getId()+"\">" + task.getTaskBody() +"</h6></br><i>"+MessageManager.getInstance().getProperty(MessageManager.DEADLINE, new Locale(locale))+" <b id=\"taskDeadline"+lesson.getId()+"\">"+dateDeadline+"</b></i></blockquote>");
			tagBuilder.append(MessageManager.getInstance().getProperty(MessageManager.NO_HOMEWORKS, new Locale(locale)));
			tagBuilder.append("</div>");
		} else {
			tagBuilder.append("<div id=\"isHomeworkPresent"+lesson.getId()+"\">");//tyt1
			String dateDeadline = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(task.getDeadline());
			tagBuilder.append("<blockquote id=\"blockquote"+lesson.getId()+"\"><i>"+MessageManager.getInstance().getProperty(MessageManager.TITLE, new Locale(locale))+" <b id=\"taskTitle"+lesson.getId()+"\">"+task.getTitle()+"</b></i></br><h6 style=\"word-break: break-all;\" id=\"taskBody"+lesson.getId()+"\">" + task.getTaskBody() +"</h6></br><i>"+MessageManager.getInstance().getProperty(MessageManager.DEADLINE, new Locale(locale))+" <b id=\"taskDeadline"+lesson.getId()+"\">"+dateDeadline+"</b></i></blockquote>");
			for (Student student : students) {
				tagBuilder.append("<div class=\"col s3\">");
				StudentAttachment attachment = student_attachment_map.get(student.getId());
				tagBuilder.append("<div class=\"card\" style=\"height:250px;\">");
				tagBuilder.append("<div class=\"card-image waves-effect waves-block waves-light\">");
					tagBuilder.append("<img class=\"activator\" src=\""+context+"/images?type=users&id="+student.getId()+"\" style=\"height:160px;\" >");
				tagBuilder.append("</div>");
				tagBuilder.append(" <div class=\"card-content\" style=\"padding:10px;height:80px;\">");
					tagBuilder.append("<span class=\"card-title activator grey-text text-darken-4\" style=\"line-height:0px;font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</i></span>");
					tagBuilder.append("<p style=\"padding:0px;bottom:0px;position:absolute;\"><a class=\"waves-effect  waves-light modal-trigger\" href=\"#!\" onclick=\"openModalSendMess("+ student.getId()+",'"+student.getFirstName()+"','"+student.getLastName()+"')\"><i class=\"fa fa-envelope-o\"></i></a></p>");
				tagBuilder.append("</div>");
		          
				if(attachment != null){
						if(attachment.getDateAdded().compareTo(task.getDeadline()) >= 0){
						  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:5px;background-color:rgb(252, 138, 97);\">");
						}else{
						  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:5px;background-color:rgb(154, 252, 97)\">");
						}
						  tagBuilder.append("<span class=\"card-title grey-text text-darken-4\" style=\"font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</i></span>");
				          tagBuilder.append("<div class=\"card-panel white \" style=\"padding:5px;margin:0px;height:150px;\" id=\"hw"+attachment.getId()+"\">");
				          tagBuilder.append("<div class=\"row\">");
				          tagBuilder.append("<img style=\"float:left;\" width=\"100\" height=\"100\" src=\""+ context + "/images?type=extensions&extension="+ attachment.getExtension() + "\">");
				          String date = new SimpleDateFormat("dd-MM-yyyy о HH:mm").format(attachment.getDateAdded());
				          tagBuilder.append("<div style=\"font-size:9px;height: 43px;\">"+MessageManager.getInstance().getProperty(MessageManager.SENDED, new Locale(locale))+"</br>"+date+"</div></br></br></br>");
				          
				          tagBuilder.append("<label style=\"padding-left:5px;\">"+attachment.getTitle()+"</label>");
				          tagBuilder.append("</div>");
				          tagBuilder.append("<div class=\"row\">");
				          tagBuilder.append("<div style=\"float:left;\">");
				          if(attachment.getExtension().equals(".docx") ||  attachment.getExtension().equals(".pdf")){
				        	  tagBuilder.append("<a onclick=\"openPreviewModal("+student.getId()+",'HOMEWORK',"+taskId+","+attachment.getId()+")\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-action-visibility\"></i></a>");
				          }
				          tagBuilder.append("<a  href=\""+ context+"/files?owner="+student.getId()+"&target=HOMEWORK&target_id="+taskId+"&attachment_id="+attachment.getId()+"\" class=\"btn-floating btn-small waves-effect waves-light green\"><i class=\"mdi-file-file-download\"></i></a>");
				          tagBuilder.append("</div>");
//				          String dateDeadline = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(task.getDeadline());
//				          tagBuilder.append("<div style=\"font-size:10px;padding-left:3px;text-align:center;\">Deadline :</br>"+ dateDeadline+"</div>");
				          tagBuilder.append("</div>");
				          tagBuilder.append("</div>");
		          }else{
			        	  tagBuilder.append("<div class=\"card-reveal\" style=\"padding:10px;background-color:rgb(242, 0, 0);\">");
			        	  tagBuilder.append("<span class=\"card-title grey-text text-darken-4\" style=\"font-size:15px;\">"+student.getFirstName()+" "+student.getLastName()+"</i></span>");
			        	  tagBuilder.append("<div class=\"card-panel white \" >");
				          tagBuilder.append("<div class=\"row\">");
				          tagBuilder.append("<img width=\"100\" height=\"100\" src=\""+ context + "/images?type=extensions&extension=.nohomework\"><br>");
				          
				          tagBuilder.append("</div>");
				          
				          tagBuilder.append("</div>");
		          }
		          
		          tagBuilder.append("</div>");
		        tagBuilder.append("</div>");
				
				tagBuilder.append("</div>");
				
			}
			tagBuilder.append("</div>");//tyt1
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

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	public int getDirectionId() {
		return directionId;
	}
	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}
}