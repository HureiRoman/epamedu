package edu.epam.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.model.NewsItem;

public class ManageNewsItemCard extends SimpleTagSupport {
	private NewsItem newsItem;

	@Override
	public void doTag() throws JspException, IOException {
	try{	
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder tagBuilder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		
//        tagBuilder.append("<div class=\"row\">");
        tagBuilder.append("<div class=\"col s6 \"> ");
        tagBuilder.append("<div class=\"card\" style=\"height:450px;overflow-y:auto;\">");
        tagBuilder.append("<div class=\"card-image\">");
		tagBuilder.append("<img class=\"responsive-img\" style=\"max-height: 300px\" src=\""+ context + "/images?type=news&id="+newsItem.getId()+"\" >");
        tagBuilder.append("<span  class=\"card-title\">"+newsItem.getTitle()+"</span>");
        tagBuilder.append("</div>");
        tagBuilder.append("<div class=\"card-content\">");
        tagBuilder.append("<p style=\"word-break:break-all;\">"+newsItem.getDescription()+"</p>");
        tagBuilder.append("</div>");
        tagBuilder.append("<div class=\"card-action\">");
        tagBuilder.append(" <a onclick=\"moderateNewsItem("+newsItem.getId()+")\"   class=\"right  btn-floating btn-medium waves-effect waves-light red\"><i class=\"mdi-content-create\"></i></a> ");
        
        //tagBuilder.append(" <i class=\"small material-icons\"   >delete</i>");
        tagBuilder.append(" <a onclick=\"openDeleteNewsItemModal("+newsItem.getId()+")\"   class=\"right  btn-floating btn-medium waves-effect waves-light red\"><i class=\"mdi-content-clear\"></i></a> ");
        
        tagBuilder.append("<div class=\"switch\"><label>Off<input   onchange=\"setNewsItemAvailable( " +newsItem.getId()+ ", !$(this).is(':checked') )\"  " + (newsItem.getIsArchived() ? "" : "checked")+" type=\"checkbox\"><span class=\"lever\"></span>On</label></div>");
        tagBuilder.append("</div> </div> </div>");
		writer.write(tagBuilder.toString());
		
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}

	public NewsItem getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(NewsItem newsItem) {
		this.newsItem = newsItem;
	}

}
