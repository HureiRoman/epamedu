package edu.epam.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import edu.epam.constants.Constants;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.service.DirectionService;

public class TestsCards extends SimpleTagSupport {
	private ArrayList<Test> tests;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder builder = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);

		Direction direction;
		try {
			direction = DirectionService.getDirectionById(tests.get(0).getDirectionId());
			builder.append("<div class=\"col\"");
			builder.append("<div class=\"col s12\" id=\"tabsCol\">");
			
			builder.append("<ul class=\"tabs\">");
			for (int i = 0; i < tests.size(); i++) {
				builder.append("<li class=\"tab col s1\">");
				builder.append("<a href=\"#test");
				builder.append(i + 1);
				builder.append("_tab\" class=\"");
				if(i == 0) {
					builder.append("active");
				}
				builder.append(" indigo-text\">");
				builder.append(i + 1);
				builder.append(" <i class=\"mdi-action-assignment");
				if(i == tests.size() - 1) {
					builder.append("-turned-in");
				}
				builder.append("\" id=\"test");
				builder.append(i + 1);
				builder.append("\"></i></a></li>");
			}
			builder.append("</ul></div>");

//			С‚С–Р»Рѕ С‚РµСЃС‚Сѓ
			
			builder.append("<div class=\"row\">");
			builder.append("<div class=\"col s2 \">");
			builder.append("<br><div id=\"countdown\"></div>");
			builder.append("</div>");
			
			for (int i = 0; i < tests.size(); i++) {
				checkPunctuation(tests.get(i));
				builder.append("<div class=\"col s8 \">");
				builder.append("<div id=\"test" + (i + 1) + "_tab\" class=\"col s12\">");
				builder.append("<div class=\"row\">");
				builder.append("<div class=\"col s12 \">");
				builder.append("<div class=\"card-panel\">");
				
				builder.append("<h3>" + MessageManager.getInstance().getProperty(MessageManager.TEST, new Locale(locale)) + " #" + (i + 1) + "</h3> <br>");
				builder.append("<article style=\"word-wrap: break-word;\">" + tests.get(i).getQuestion()
						+ "</article>");
				if (tests.get(i).getCode() != null) {
					builder.append("<pre name=\"code\" class=\"" + direction.getCodeLang() + "\" cols=\"60\" rows=\"10\">");
					builder.append(tests.get(i).getCode());
					builder.append("</pre>");
				}
				else {
					builder.append("<pre></pre>");
				}
				builder.append("<div class=\"row\">");
				String answer = null;
				for(int j = 1; j <= 4; j++) {
					switch(j) {
					case 1:
						answer = tests.get(i).getAnswer1();
						break;
					case 2:
						answer = tests.get(i).getAnswer2();
						break;
					case 3:
						answer = tests.get(i).getAnswer3();
						break;
					case 4:
						answer = tests.get(i).getAnswer4();
						break;
					}
					builder.append("<div>");
					builder.append("<input name=\"answer");
					builder.append(i + 1);
					builder.append("\" type=\"radio\" value=\"");
					builder.append(j);
					builder.append("\" id=\"answer");
					builder.append(i + 1);
					builder.append("_");
					builder.append(j);
					builder.append("\" onchange=\"changeImage(");
					builder.append(i + 1);
					builder.append(")\"/> <label class=\"left-align\" style=\"word-wrap: break-word;\" for=\"answer");
					builder.append(i + 1);
					builder.append("_");
					builder.append(j);
					builder.append("\">");
					builder.append(answer);
					builder.append("</label>");
					builder.append("</div>");
				}
				
				if (i < tests.size() - 1) {
					builder.append("<a id=\"test\" onclick=\"selectTab('test"+ (i + 2) + "_tab');\"  ");
					builder.append("class=\"waves-effect waves-green btn-flat right\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.NEXT, new Locale(locale)));
					builder.append("</a>");
				} else {
					builder.append("<a id=\"test\" onclick=\"sendData()\" ");
					builder.append("class=\"waves-effect waves-green btn-flat right\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.SUBMIT, new Locale(locale)));
					builder.append("</a>");
				}
				builder.append("</div>");
				builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			}
			builder.append("</div>");
			builder.append("</div>");
			
			builder.append("<div id=\"testsTimeModal\" class=\"modal\" style=\"top: 30%; width: 550px;\">");
			builder.append("<div class=\"modal-content blue lighten-5 teal-text\" id=\"time_message\" ");
			builder.append("style=\"padding-top: 10px; padding-left: 10px; padding-right: 10px; padding-bottom: 10px;\">");
			builder.append("<div class=\"row valign-wrapper\" style=\"margin-top: 20px;\">");
			builder.append("<div class=\"col s11\">");
			builder.append("<h6 class=\"center\">");
			builder.append(MessageManager.getInstance().getProperty(MessageManager.YOU_HAVE_10_MIN, new Locale(locale)));
			builder.append("</h6>");
			builder.append("</div>");
			builder.append("<div class=\"col s1\" style=\"padding-left: 0px;\">");
			builder.append("<i class=\"small mdi-action-info-outline\"></i>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("<div class=\"row\">");
			builder.append("<div class=\"col s2 offset-s5\">");
			builder.append("<a class=\"modal-close waves-effect waves-green btn-flat center\" ");
			builder.append("onclick=\"countdownStart();\" style=\"position: inherit;\">");
			builder.append("OK");
			builder.append("</a>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.write(builder.toString());
	}

private void checkPunctuation(Test test) {
		
		test.setQuestion(StringEscapeUtils.unescapeHtml(test.getQuestion()));
		if (test.getCode() != null) {
			test.setCode(StringEscapeUtils.unescapeHtml(test.getCode()));
		}
		test.setAnswer1(StringEscapeUtils.unescapeHtml(test.getAnswer1()));
		test.setAnswer2(StringEscapeUtils.unescapeHtml(test.getAnswer2()));
		test.setAnswer3(StringEscapeUtils.unescapeHtml(test.getAnswer3()));
		test.setAnswer4(StringEscapeUtils.unescapeHtml(test.getAnswer4()));
	}

	public ArrayList<Test> getTests() {
		return tests;
	}

	public void setTests(ArrayList<Test> tests) {
		this.tests = tests;
	}

}
