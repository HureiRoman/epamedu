package edu.epam.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.manager.MessageManager;
import edu.epam.role.CommonUser;

public class EmployeeRowTag extends SimpleTagSupport {
    CommonUser employee;
    
    @Override
    public void doTag() throws JspException, IOException {
    	try{
    	JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		StringBuilder builder = new StringBuilder();
		builder.append("<li class=\"collection-item \">");
		builder.append("<div class=\"row\"> <div class=\"col s1\"><img style=\"max-height: 50; max-width: 50; !important\" src=\""+context+"/images?type=users&id="+employee.getId()+"\"	alt=\"\" class=\" responsive-img circle\"></div>");
		if(employee.getIsActive()){
			builder.append(" <div class=\"col s8\"><span class=\"title\">"+employee.getFirstName()+" "+employee.getLastName()+"<span id=\"userStatus"+employee.getId()+"\"></span></span><br>");
			builder.append("<label>"+employee.getRoleType()+"</label>");	
		}else if(employee.getIsActive() == false && employee.getPassword()!= null){
			
			builder.append(" <div class=\"col s8\"><span class=\"title\"> "+employee.getEmail()+" <span id=\"userStatus"+employee.getId()+"\"> "+MessageManager.getInstance().getProperty(MessageManager.DISACTIVATED, new Locale(locale))+"</span></span><br>");
			builder.append("<label>"+employee.getRoleType()+"</label>");	
		}else{
			builder.append(" <div class=\"col s8\"><span class=\"title\"> "+employee.getEmail()+" <span id=\"userStatus"+employee.getId()+"\"> "+MessageManager.getInstance().getProperty(MessageManager.WAITING_FOR_REGISTRATION, new Locale(locale))+"</span></span><br>");
			builder.append("<label>"+employee.getRoleType()+"</label>");	
		}
		builder.append("</div>");
		if(employee.getPassword()!= null){
			builder.append("<div    class=\"col s3\"> <div class=\"secondary-content\"><div class=\"switch\"><label>Off<input   onchange=\"setEmployeeActive( " + employee.getId()
					+ ", $(this).is(':checked') )\"  " + (employee.getIsActive() ? "checked" : " ")
					+ " type=\"checkbox\"><span class=\"lever\"></span>On</label></div></div></div>");	
		}
		builder.append("</li>");
		writer.write(builder.toString());
				
    }catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}		
    }

	public CommonUser getEmployee() {
		return employee;
	}

	public void setEmployee(CommonUser employee) {
		this.employee = employee;
	}
	
    
}
