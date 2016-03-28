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
import edu.epam.model.Interview;
import edu.epam.persistance.TimeToText;

public class EventTag extends SimpleTagSupport {
	private Interview event;

	@Override
	public void doTag() throws JspException, IOException {
		try{
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String context = request.getContextPath();
		StringBuilder builder = new StringBuilder();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		builder.append("<li >");
		builder.append("<div class=\"col s12\" >");
		builder.append("<div class=\"card  waves-effect waves-block waves-light\" >");
		builder.append("<div class=\"card-image waves-effect waves-block waves-light\">");
		builder.append("<img class=\"activator\"   src=\""+context+"/img/event.png\" >");
		builder.append("<img class=\"activator\" style=\"position: absolute; !important top: 30px !important;  left: 10px !important; width: 100px !important; height: 100px !important;  opacity: 0.7 !important;\"   src=\""+context+"/images?type=directions&id="+event.getDirectionId()+"\" >");
		builder.append("</div>");
		builder.append("<div class=\"card-content\" >");
		builder.append("<span style=\"font-size: 11px;\" class=\"truncate card-title activator grey-text text-darken-4\"><i class=\" small fa fa-map-marker\"></i>"+event.getPlace()+"</span><br>");
		builder.append("</span> <span style=\"font-size: 11px;\"  class=\"card-title activator grey-text text-darken-4\"><i class=\" small fa fa-calendar\"></i>"+TimeToText.timeToText(event.getDateOfTesting())+"</span>");
		builder.append("</div>");
		if(event.getDescription() != null && !event.getDescription().trim().equals("")){
			builder.append("<div class=\"card-reveal\">");
			builder.append("<span class=\" truncate card-title grey-text text-darken-4\">"+MessageManager.getInstance().getProperty(MessageManager.DETAILS, new Locale(locale))+"</span>");
			builder.append("<p>"+event.getDescription()+"</p>");
			builder.append("</div>");
		}
		builder.append("</div>");
		builder.append("</li>");
		writer.write(builder.toString());
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}

	public Interview getEvent() {
		return event;
	}

	public void setEvent(Interview event) {
		this.event = event;
	}

}
