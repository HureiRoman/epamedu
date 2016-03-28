package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.manager.MessageManager;
import edu.epam.model.CourseComment;
import edu.epam.model.Direction;
import edu.epam.role.CommonUser;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;
import edu.epam.service.DirectionService;

public class CourseReviews extends SimpleTagSupport  {
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
			String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);

			CommonUser user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
			builder.append("<h6>"+MessageManager.getInstance().getProperty(MessageManager.REVIEWS, new Locale(locale))+" "+direction.getName()+"</h6>");
			builder.append("<ul id =\"reviews"+direction.getId()+"\" class=\"collection\"  style=\"max-height: 400px; overflow-y: auto; word-break:break-all;\"    >");
			List<CourseComment> comments = null;
			try {
				comments = DirectionService.getCourseComments(direction.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}  
			if(comments.size()>0){
				for (CourseComment courseComment : comments) {
					User author = null;
					try {
						author = CommonUserService.getUserById(courseComment.getAuthor());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					builder.append("	<li id=\"courseComment"+courseComment.getId()+"\" class=\"collection-item avatar\"><img  src=\""+context+"/images?type=users&id="+courseComment.getAuthor()+"\"  class=\"circle\"> ");
					builder.append(" <span class=\"title\">"+author.getFirstName()+" "+author.getLastName()+"</span>");
					builder.append("<p>");
					builder.append(courseComment.getComment());
					builder.append("</p> ");
					if(user!=null&&user.getRoleType()==RoleType.ADMIN){
					builder.append("<a style=\"cursor: pointer;\"  onclick=\"showDeleteCommentModal("+courseComment.getId()+")\" class=\"secondary-content\"><i class=\"material-icons\">close</i></a>");
					}
				builder.append("</li>");
			}
			}else{
				builder.append("<li id=\"noComments"+direction.getId()+"\" class=\"row\">");
				builder.append("<div class=\"valign-wrapper  offset-s4  col s6\">");
				builder.append("<i class=\"large mdi-content-content-paste blue-text \"></i>");
				builder.append("<h5 class=\"valign center-align\">"+MessageManager.getInstance().getProperty(MessageManager.NO_COMMENTS, new Locale(locale))+"</h5>");
				builder.append("</div>");
				builder.append("</li>");
			}
		   builder.append("</ul>");
			if(user!=null&&user.getRoleType()==RoleType.GRADUATE){
				builder.append("<div class=\"input-group\">");
				builder.append("<textarea style=\"resize: vertical; max-height:100px;\" maxlength=\"1000\" id=\"review_message"+direction.getId()+"\"");
				builder.append("class=\"materialize-textarea\" cols=\"10\" rows=\"5\"");
				builder.append("placeholder=\""+MessageManager.getInstance().getProperty(MessageManager.ENTER_YOUR_REVIEW, new Locale(locale))+"\"></textarea>");
				builder.append("<span class=\"input-group-btn\">");
				builder.append("<button class=\"btn right btn-info\" type=\"button\" id=\"sendReviewButton\"");
				builder.append("onclick=\"leaveReview("+direction.getId()+");\">"+MessageManager.getInstance().getProperty(MessageManager.SEND_REVIEW, new Locale(locale))+"</button>");
				builder.append("</span>");
				builder.append("</div>");
			}
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
