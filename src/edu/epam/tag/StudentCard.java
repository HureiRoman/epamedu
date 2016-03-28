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
import edu.epam.role.Student;

public class StudentCard extends SimpleTagSupport {
	private Student student;

	@Override
	public void doTag() throws JspException, IOException {
	try{
		
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		System.out.println("Inside TAG = " + student);
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		CommonUser user =  (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		String context = request.getContextPath();
		StringBuilder builder = new StringBuilder();
		builder.append("<li style=\"margin-right:60px;\">");
		builder.append("<div class=\"col s12\" style=\"margin-left:10px;margin-right:10px;padding:0px;\">");
		builder.append("<div class=\"card\" style=\" width:200px; height:200px;\">");
//		builder.append("<div class=\"card\" style=\"height:150px;width:150px;\">");
			builder.append("<div class=\"card-image waves-effect waves-block waves-light\">");
				builder.append("<img style=\"height:120px;width: 90%;\" onclick=\"openingModal("+ student.getId()+")\" src=\"" + context+ "/images?type=users&id=" + student.getId() + "\">");
			builder.append("</div>");
			builder.append("<div class=\"card-content\" style=\"padding:5px;\">");
				builder.append("<span class=\"card-title grey-text text-darken-4\" onclick=\"openingModal("+ student.getId()+")\" style=\"font-size:12px;height:100%;line-height: 10px;cursor: pointer;\">"+ student.getFirstName() + " " + student.getLastName()+"</i></span>");
				if(user.getId()!=student.getId()){
					builder.append("<p><a class=\"waves-effect btn  waves-light modal-trigger\" href=\"#\" onclick=\"openModalSendMess("+ student.getId()+",'"+student.getFirstName()+"','"+student.getLastName()+"')\" ><i class=\"fa fa-envelope-o\"></i></a></p>");
						
				}
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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}
