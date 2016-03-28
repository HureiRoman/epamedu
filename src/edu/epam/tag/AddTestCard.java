package edu.epam.tag;

import java.io.IOException;
import java.util.ArrayList;
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
import edu.epam.model.Test;

public class AddTestCard extends SimpleTagSupport {
	private ArrayList<Test> tests;
	
	@Override
	public void doTag() throws JspException, IOException {
		try{
			JspContext jspContext = getJspContext();
			JspWriter writer = jspContext.getOut();
			PageContext pageContext = (PageContext)jspContext;
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
			StringBuilder builder = new StringBuilder();
			
			builder.append("<div class=\"row\" style=\"margin-bottom: 0px;\">");
			builder.append("<div class=\"col s8 offset-s2\">");
			builder.append("<div class=\"card-panel white\" ");
			builder.append("style=\"margin-top: 15px; padding-top: 10px; padding-bottom: 10px; padding-left: 10px; padding-right: 10px; margin-left: 0px;\">");
			builder.append("<div class=\"row\">");
			builder.append("<div class=\"col s12\">");
			builder.append("<table>");
			builder.append("<tbody>");
			builder.append("<tr>");
			builder.append("<td>");
			if(tests.isEmpty()) {
				builder.append("<h5>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.NO_TESTS_TEACHER, new Locale(locale)));
				builder.append("</h5>");
			}
			else {
				builder.append("<h4>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.ADD_TEST_TEACHER, new Locale(locale)));
				builder.append("</h4>");
			}
			builder.append("</td>");
			builder.append("<td>");
			builder.append("<a onclick=\"addTestCall();\" "
					+ "class=\"btn-floating btn-large waves-effect waves-light green\"><i class=\"mdi-content-add\"></i></a>");
			builder.append("</td>");
			builder.append("</tr>");
			builder.append("</tbody>");
			builder.append("</table>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			
			writer.write(builder.toString());
		}catch(NoDBConnectionsLongTime ex){
			ex.printStackTrace();
			System.out.println("No Db connection long time");
		}
	}

	public ArrayList<Test> getTests() {
		return tests;
	}

	public void setTests(ArrayList<Test> tests) {
		this.tests = tests;
	}
}
