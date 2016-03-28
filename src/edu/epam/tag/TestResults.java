package edu.epam.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.role.CommonUser;
import edu.epam.service.DirectionService;

public class TestResults extends SimpleTagSupport {
	private ArrayList<Test> tests;
	private String score;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpSession session = (HttpSession) pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = request.getContextPath();
		
		CommonUser user = (CommonUser)session.getAttribute("logined_user");
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"row\">");
		builder.append("<div class=\"col s8 offset-s2\"> ");
		builder.append("<div class=\"row\"> ");
		builder.append("<div class=\"col s12 \"> ");
		builder.append("<div class=\"card-panel\"> ");
		Direction direction;
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		try {
			direction = DirectionService.getDirectionById(tests.get(0).getDirectionId());
			
			builder.append("<div id=\"ad\"><h4 class=\"center-align\">");
			builder.append(MessageManager.getInstance().getProperty(MessageManager.YOUR_SCORE, new Locale(locale))+" ");
			builder.append(score);
			builder.append("</h4></div>");
			builder.append("<table>");
			builder.append("<tbody>");
			for (int i = 0; i < tests.size(); i++) {
				checkPunctuation(tests.get(i));
				builder.append("<div class=\"indigo-text\">");
				if(tests.get(i).getUserAnswer() != null 
						&& tests.get(i).getUserAnswer().intValue() == tests.get(i).getCorrect().intValue()) {
					
					builder.append("<tr class=\"light-green-text\">");
					builder.append("<td>");
					builder.append("<i class=\"medium mdi-action-done\"></i>");
					builder.append("</td>");
					builder.append("<td><h5><i>");
					builder.append(i + 1);
					builder.append(". ");
					builder.append(tests.get(i).getQuestion());
					builder.append("</i></h5></td>");
					
					if (tests.get(i).getCode() != null) {
						addCode(builder, tests.get(i), direction);
					}
					Method method;
					//якщо номер відповіді коректний – витягаємо відповідь 
					method = tests.get(i).getClass().getMethod("getAnswer" + tests.get(i).getCorrect());
					String correctChoice = (String) method.invoke(tests.get(i));
					builder.append("<tr>");
					builder.append("<td></td><td>");
					builder.append(correctChoice);
					builder.append("</td></tr>");
				}
				else {
					builder.append("<tr class=\"indigo-text\">");
					builder.append("<td>");
					builder.append("<i class=\"medium mdi-content-clear\"></i>");
					builder.append("</td>");
					builder.append("<td><h5><i>");
					builder.append(i + 1);
					builder.append(". ");
					builder.append(tests.get(i).getQuestion());
					builder.append("</i></h5></td>");
					builder.append("</tr>");
					
					if (tests.get(i).getCode() != null) {
						addCode(builder, tests.get(i), direction);
					}
					
					Method method, method2;
					method = tests.get(i).getClass().getMethod("getAnswer" + tests.get(i).getCorrect());
					String correctChoice = (String) method.invoke(tests.get(i));
					method2 = tests.get(i).getClass().getMethod("getAnswer" + tests.get(i).getUserAnswer());
					String userChoice = (String) method2.invoke(tests.get(i));
					
					if(tests.get(i) != null && !userChoice.equals("")) {
						builder.append("<tr>");
						builder.append("<td></td><td><b>");
						builder.append(MessageManager.getInstance().getProperty(MessageManager.YOUR_ANSWER, new Locale(locale)));
						builder.append("</b>");
						builder.append(" ");
						builder.append(userChoice);
						builder.append("</td>");
						builder.append("</tr>");
					}
					else {
						builder.append("<tr>");
						builder.append("<td></td><td><b>");
						builder.append(MessageManager.getInstance().getProperty(MessageManager.YOU_DIDNOT_ANSWER, new Locale(locale)));
						builder.append("</b>");
						builder.append("</td>");
						builder.append("</tr>");
					}
					builder.append("<tr>");
					builder.append("<td></td><td><b>");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.CORRECT, new Locale(locale)));
					builder.append("</b>");
					builder.append(" ");
					builder.append(correctChoice);
					builder.append("</td>");
					builder.append("</tr>");
				}
				builder.append("</div>");
			}
			builder.append("</tbody>");
			builder.append("</table>");
			builder.append("<div class=\"divider\" style=\"margin-top: 15px;\"></div>");
			if (user == null) {
				builder.append("<div class=\"center-align\" style=\"padding-top: 20px;\">");
				builder.append("<a href=\"Controller?command=redirect&direction=registrationPage\" ");
				builder.append("class=\"waves-effect waves-light btn light-green darken-1\">");
				builder.append("&lt;");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.REGISTER, new Locale(locale)));
				builder.append("&gt;");
				builder.append("</a></div>");
			}
			else if (user.getRoleType() == RoleType.STUDENT || user.getRoleType() == RoleType.TRAINEE
					|| user.getRoleType() == RoleType.GRADUATE) {
				builder.append("<div class=\"center-align\" style=\"padding-top: 20px;\">");
				builder.append("<a href=\"" + context + "/Controller?command=testRate\" ");
				builder.append("class=\"waves-effect waves-light btn light-green darken-1\">");
				builder.append("&lt;");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.TO_TEST_RANKING, new Locale(locale)));
				builder.append("&gt;");
				builder.append("</a></div>");
			}
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("</div>");
			
			builder.append("<div id=\"adModal\" class=\"modal\" ");
			builder.append("style=\"height: 242px; width: 622px; background-image: url('");
			builder.append(context);
			builder.append("/img/a.jpg');\">");
			builder.append("<div class=\"modal-content\" style=\" padding-top: 12px; padding-bottom: 12px;\">");
			builder.append("<div class=\"white-text center-align\" style=\"font-family: 'Love Ya Like A Sister', cursive;\">");
			builder.append("<div class=\"row\">");
			builder.append("<div>");
			builder.append("<h5>");
			builder.append(MessageManager.getInstance().getProperty(MessageManager.WANT_TO_GET_KNOW_MORE, new Locale(locale)));
			builder.append("</h5>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("<div class=\"row\">");
			builder.append("<div>");
			builder.append("<a class=\"btn\" href=\"Controller?command=redirect&direction=registrationPage\">");
			builder.append(MessageManager.getInstance().getProperty(MessageManager.REGISTER, new Locale(locale)));
			builder.append("</a>");
			builder.append("</div>");
			builder.append("</div>");
			builder.append("<div class=\"row\">");
			builder.append("<div>");
			builder.append("<h5>");
			builder.append(MessageManager.getInstance().getProperty(MessageManager.JOIN_COMMUNITY, new Locale(locale)));
			builder.append("</h5>");
			builder.append("</div></div></div></div></div></div>");
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		writer.write(builder.toString());
	}

	private void addCode(StringBuilder builder, Test test, Direction direction) {
		builder.append("<tr>");
		builder.append("<td></td><td>");
		builder.append("<pre name=\"code\" class=\"");
		builder.append(direction.getCodeLang());
		builder.append("\" cols=\"60\" rows=\"10\">");
		builder.append(test.getCode());
		builder.append("</pre>");
		builder.append("</td>");
		builder.append("</tr>");
		
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}