package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.service.DirectionService;
import edu.epam.service.TestService;

public class TestRate extends SimpleTagSupport {
	private CommonUser logined_user;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		try {
			Map<Direction, List<TestResults>> top10 = DirectionService.getTop10Map();
			CommonUser user = (CommonUser) logined_user;
			StringBuilder builder = new StringBuilder();
			builder.append("<div class=\"col s5 offset-s1\"><div class=\"card\">");
			builder.append("<div class=\"card-content\"><span class=\"card-title black-text\">");
			if(user.getTestResults()!=null&&user.getTestResults().isEmpty()) {
				builder.append("<h5 class=\"center-align\"</h5><i>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.YOU_HAVE_NOT_TEST_PASSED, new Locale(locale)));
				builder.append("</i>");
				builder.append("<div class=\"card-content center-align\"><a onclick=\"$('#testsModal').openModal()\" class=\"modal-action waves-effect waves-green btn\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.PASS_TEST, new Locale(locale)));
				builder.append("</a></div>");
				builder.append("</span></div><div class=\"card-content\">");
			}
			else {
				builder.append(MessageManager.getInstance().getProperty(MessageManager.YOUR_RATE, new Locale(locale)));
				builder.append("</span></div><div class=\"card-content\">");
				for(Direction d : user.getTestResults().keySet()) {
					TestResults t = user.getTestResults().get(d);
					if (t.getTestCount() > 4) {
						t.setPosition(TestService.getUserPosition(user, d));
					}
					builder.append("<div><h4>");
					if(t.getPosition() != null) {
						if (t.getPosition() >= 1 && t.getPosition() <= 3) {
							builder.append("<img src=\"");
							builder.append(context);
							builder.append("/img/awards/top10_");
							builder.append(t.getPosition());
							builder.append(".png\">");
						}
					}
					builder.append(" ");
					builder.append(d.getName());
					builder.append(": ");
					builder.append(t.getRate());
					builder.append("</h4>");
					builder.append("</div>");
					builder.append("<div>");
					if(t.getPosition() != null) {
						builder.append(MessageManager.getInstance().getProperty(MessageManager.POSITION, new Locale(locale))+ " ");
						builder.append(": ");
						builder.append(t.getPosition());
					}
					else {
						builder.append(MessageManager.getInstance().getProperty(MessageManager.YOU_HAVE_TO_PASS_FIVE_TESTS, new Locale(locale)));
					}
					builder.append("</div>");
				}
			}
			builder.append("</div></div></div>");
			builder.append("<div class=\"col s5\"><div class=\"card\">");
			if(top10.isEmpty()) {
				builder.append("<div class=\"card-content\"><span class=\"card-title black-text\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.NO_RANKING_ARE_AVAILABLE, new Locale(locale)));
				builder.append("</span></div><div class=\"card-content\">");
			}
			else {
				builder.append("<div class=\"col s12\" id=\"tabsCol\">");
				builder.append("<ul class=\"tabs\">");
				boolean flag = false;
				for(Direction d : top10.keySet()) {
					builder.append("<li class=\"tab col s1 \">");
					builder.append("<a href=\"#top");
					builder.append(d.getId());
					builder.append("_tab\" class=\"truncate ");
					if (flag == false) {
						builder.append("active ");
						flag = true;
					}
					builder.append("tooltipped\"");
					builder.append(" data-position=\"top\" data-delay=\"10\" data-tooltip=\"");
					builder.append(d.getName());
					builder.append(". TOP 10.");
					builder.append("\">");
					
					builder.append("<img style=\"height:25px;\" src=\"");
					builder.append(context);
					builder.append("/images?type=directions&id=");
					builder.append(d.getId());
					builder.append("\" class=\"circle responsive-img\"> ");
	
					builder.append(d.getName());
					builder.append("</a></li>");
				}
				builder.append("</ul></div>");
				builder.append("<div class=\"row\">");
				for(Direction d : top10.keySet()) {
					builder.append("<div id=\"top" + d.getId() + "_tab\" class=\"col s12\">");
					builder.append("<table class=\"bordered\" >");
					builder.append("<thead>");
					builder.append("<tr>");
					builder.append("<th data-field=\"position\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.POSITION, new Locale(locale)));
					builder.append("</th>");
					builder.append("<th data-field=\"name\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.TRAINEE_NAME, new Locale(locale)));
					builder.append("</th>");
					builder.append("<th data-field=\"points\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.POINTS, new Locale(locale)));
					builder.append("</th>");
					builder.append("</tr>");
					builder.append("</thead>");
					builder.append("<tbody>");
					for(int i = 0; i < top10.get(d).size(); i++) {
						builder.append("<tr>");
						builder.append("<td>");
						builder.append(i + 1);
						builder.append("</td>");
						builder.append("<td>");
						builder.append(top10.get(d).get(i).getTop10UserName());
						builder.append("</td>");
						builder.append("<td>");
						builder.append(top10.get(d).get(i).getRate());
						builder.append("</td>");
						builder.append("</tr>");
					}
					builder.append("</tbody></table></div>");
				}
				builder.append("</div>");
			}
			
			builder.append("</div></div>");
			writer.write(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CommonUser getLogined_user() {
		return logined_user;
	}

	public void setLogined_user(CommonUser logined_user) {
		this.logined_user = logined_user;
	}
}