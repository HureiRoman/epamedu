package edu.epam.tag;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.common.html.HtmlEscapers;

import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.model.Message;
import edu.epam.persistance.TimeToText;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;
import edu.epam.service.PersonalMessageService;


public class DialogTag extends SimpleTagSupport {
    private Integer colocutorId;
    private Integer userId;
    
    
    @Override
    public void doTag() throws JspException, IOException {
    try{
    	User colocutor = null;
    	Integer countOfUnreadMessages = 0;
    	Message lastMessage = null;
    	try {
    	    colocutor  = CommonUserService.getUserById(colocutorId);
			countOfUnreadMessages = PersonalMessageService.getCountOfUnreadMessages(userId,colocutorId);
			lastMessage = PersonalMessageService.getLastMessageForDialog(userId,colocutorId);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		StringBuilder builder = new StringBuilder();
		builder.append("<a href=\""+context+"/Controller?command=dialog&with="+colocutorId+"\" style=\"cursor: pointer;\" class=\"collection-item avatar\">");
		if(countOfUnreadMessages>0){
			builder.append("<span class=\"new badge\">"+countOfUnreadMessages+"</span>");
		}
		builder.append("<img src=\""+context+"/images?type=users&id="+colocutorId+"\"  class=\"circle\"> ");
		builder.append("<span class=\"title\">"+colocutor.getFirstName()+" "+colocutor.getLastName()+"</span>");

		if(lastMessage.getSender()==userId){
			builder.append("<div class=\"row\">"
					+ "<div class=\"col s1\"><img src=\""+context+"/images?type=users&id="+userId+"\" style=\"width: 50px; !important \" class=\"responsive-img\"> </div>"
					+ "<div class=\"col s11\">"+HtmlEscapers.htmlEscaper().escape(lastMessage.getText())+"</div>"
					+ "</div>");
		}else{
			builder.append("<p class=\"truncate\"> "+HtmlEscapers.htmlEscaper().escape(lastMessage.getText())+"</p>");
		}
		if(countOfUnreadMessages==0){
			builder.append("<label class=\"secondary-content\" >"+TimeToText.timeToText(lastMessage.getMessageTime())+"</label>");
		}
		builder.append("</a>");
		writer.write(builder.toString());
    }catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
    }

	public Integer getColocutorId() {
		return colocutorId;
	}


	public void setColocutorId(Integer colocutorId) {
		this.colocutorId = colocutorId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
