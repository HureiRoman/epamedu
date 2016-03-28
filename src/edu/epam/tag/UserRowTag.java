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

public class UserRowTag extends SimpleTagSupport {
    CommonUser user;
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
		builder.append("<div class=\"row\"> <div class=\"col s1\"><img style=\"max-height: 50; max-width: 50; !important\" src=\""+context+"/images?type=users&id="+user.getId()+"\"	alt=\"\" class=\" responsive-img circle\"></div>");
		if(user.getIsActive()){
			builder.append(" <div class=\"col s8\"><span class=\"title\">"+user.getFirstName()+" "+user.getLastName()+"</span><br>");
			builder.append("<label>"+user.getRoleType()+"</label>");	
		}else{
			builder.append(" <div class=\"col s8\"><span class=\"title\"> "+user.getEmail()+MessageManager.getInstance().getProperty(MessageManager.WAITING_FOR_REGISTRATION, new Locale(locale))+	" </span><br>");
			builder.append("<label>"+user.getRoleType()+"</label>");	
		}
		builder.append("</div>");
		builder.append("<div class=\"col s3\"> <div class=\"secondary-content\"><div class=\"switch\"><label>Off<input   onchange=\"setUserActive( " + user.getId()
				+ ", $(this).is(':checked') )\"  " + (user.getIsActive() ? "checked" : " ")
				+ " type=\"checkbox\"><span class=\"lever\"></span>On</label></div></div></div></li>");	writer.write(builder.toString());
   
    }catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
    }
	public CommonUser getUser() {
		return user;
	}
	public void setUser(CommonUser user) {
		this.user = user;
	}
   
    
	
    
}
