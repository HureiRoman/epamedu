package edu.epam.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.model.Direction;

public class DirectionRowTag extends SimpleTagSupport  {
  private Direction direction;
  
  @Override
	public void doTag() throws JspException, IOException {
	try{
	  	JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		
		StringBuilder builder = new StringBuilder();
		builder.append("<li  onclick=\"selectDirection("+direction.getId()+",this)\"   class=\"collection-item avatar\">"
				+ "<img class=\"circle  responsive-img\"  src=\""+context+"/images?type=directions&id="+direction.getId()+"\"> <span name=\"title_span\" class=\"title\">"+ direction.getName()+ "</span>");
		builder.append("<div class=\"secondary-content row\">");
		builder.append("<div class=\"col s10\">");
		builder.append(" <div class=\"switch\"><label>Off<input   onchange=\"setDirectionActive( " + direction.getId()
				+ ", $(this).is(':checked') )\"  " + (direction.getIsActive() ? "checked" : " ")
				+ " type=\"checkbox\"><span class=\"lever\"></span>On</label></div>");
		builder.append("</div>");
		builder.append("<div class=\" col s2\">");
		builder.append("<a onclick=\"openEditDirectionModal("+direction.getId()+")\" ");
		builder.append(" class=\"btn-floating btn-small waves-effect tooltipped waves-light brown right\" data-position=\"top\" data-delay=\"50\"");
		builder.append("data-tooltip=\"Edit\"><i class=\"mdi-content-create  \"></i></a>");
		builder.append("</div></div>");
		builder.append("</li>");
		writer.write(builder.toString());
  	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}
  

public Direction getDirection() {
	return direction;
}

public void setDirection(Direction direction) {
	this.direction = direction;
}

  
}
