package edu.epam.tag;

import java.io.IOException;
import java.util.List;
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
import edu.epam.model.NewsItem;

public class NewsTag extends SimpleTagSupport {
	
	private List<NewsItem> news;
	@Override
	public void doTag() throws JspException, IOException {
		try{
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder tagBuilder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		
		if(news!=null && news.size()>0){

			tagBuilder.append("<div id=\"news\" class=\"slider-pro\">");
			tagBuilder.append("<div class=\"sp-slides\"> ");
			for (NewsItem newsItem : news) {
				tagBuilder.append("<div class=\"sp-slide\"> ");
				tagBuilder.append("<img class=\"sp-image\" src=\""+context+"/css/images/blank.gif\" ");
				tagBuilder.append(" data-src=\""+ context + "/images?type=news&id="+newsItem.getId()+"\" /> ");
				tagBuilder.append("<p class=\"sp-layer sp-white sp-padding\" data-vertical=\"10\" data-horizontal=\"2%\" data-width=\"96%\"  ");
				tagBuilder.append("data-show-transition=\"down\" data-show-delay=\"400\" data-hide-transition=\"up\">"+newsItem.getTitle()+"</p>");
				tagBuilder.append("<p class=\"sp-layer sp-black sp-padding\" style=\"word-break: break-all;\" data-position=\"bottom\"  data-width=\"96%\" data-vertical=\"10\" data-horizontal=\"2%\" data-width=\"96%\" data-show-transition=\"down\" data-show-delay=\"400\" data-hide-transition=\"up\">"+newsItem.getContent()+"</p> ");
				tagBuilder.append("</div>");			
			}
			tagBuilder.append("</div>");		
			tagBuilder.append("<div class=\"sp-thumbnails\">");
			for (NewsItem newsItem : news) {
				tagBuilder.append("<div class=\"sp-thumbnail\">");
					tagBuilder.append("<div class=\"sp-thumbnail-text\">");
			    	tagBuilder.append("<div class=\"sp-thumbnail-title\">"+newsItem.getTitle()+"</div>");
			     	tagBuilder.append("<div class=\"sp-thumbnail-description\">"+newsItem.getDescription()+"</div>");
				tagBuilder.append("</div>");
				
				tagBuilder.append("</div>");
			}
			tagBuilder.append("</div>");
			
			
			tagBuilder.append("</div>");
			
			writer.write(tagBuilder.toString());
			
		}
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}

	public List<NewsItem> getNews() {
		return news;
	}


	public void setNews(List<NewsItem> news) {
		this.news = news;
	}
	
	
}
