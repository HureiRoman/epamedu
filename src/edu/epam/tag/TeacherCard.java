package edu.epam.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.role.CommonUser;
import edu.epam.role.Teacher;

public class TeacherCard extends SimpleTagSupport {
	private Teacher teacher;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		System.out.println("Inside TAG = " + teacher);
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();
		CommonUser loginedUser = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		StringBuilder builder = new StringBuilder();
		try{
		
		builder.append("<li>");
		builder.append("<div class=\"col s12\" style=\"margin-left:10px;margin-right:10px;padding:0px;\">");
		builder.append("<div class=\"card\" style=\"height:200px; width:200px;\">");
			builder.append("<div class=\"card-image waves-effect waves-block waves-light\">");
				builder.append("<img style=\"height:120px;\" class=\"activator\" src=\"" + context+ "/images?type=users&id=" + teacher.getId() + "\">");
			builder.append("</div>");
			builder.append("<div class=\"card-content\" style=\"padding:5px;\">");
				builder.append("<span class=\"card-title activator grey-text text-darken-4\"  style=\"font-size:12px;height:100%;line-height: 10px;\">"+ teacher.getFirstName() + " " + teacher.getLastName()+"</i></span>");
				if(loginedUser.getId() != teacher.getId()){
					builder.append("<p><a class=\"waves-effect btn  waves-light modal-trigger\" href=\"#\" onclick=\"openModalSendMess("+ teacher.getId()+",'"+teacher.getFirstName()+"','"+teacher.getLastName()+"')\" ><i class=\"fa fa-envelope-o\"></i></a></p>");
				}
				builder.append("</div>");
				builder.append("<div class=\"card-reveal\" style=\"padding:5px;\">");
				builder.append("<span class=\"card-title grey-text text-darken-4\" style=\"font-size:14px;width:100%;text-align:center;\">"+ teacher.getFirstName() + " " + teacher.getLastName()+"</i></span>");
				builder.append("<p style=\"font-size:12px;\"> Email : "+teacher.getEmail()+"</p>");
				builder.append("</div>");
			builder.append("</div>");
		builder.append("</div>");
		builder.append("</li>");
		writer.append(builder.toString());
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}
