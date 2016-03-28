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
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.service.DirectionService;

public class TestEditModals extends SimpleTagSupport {
	private ArrayList<Test> tests;
	
	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter writer = jspContext.getOut();
		StringBuilder builder = new StringBuilder();
		
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String locale = (String)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);
		
		
		if(!tests.isEmpty()) {
			try {
				builder.append("<div id=\"addTestModal\" class=\"modal indigo-text\"style=\"max-height: 680px !important;\">");
				builder.append("<div class=\"modal-content\">");
				builder.append("<h6 class=\"black-text\">");
				builder.append("<i class=\"small mdi-action-info\"></i>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.NEW, new Locale(locale)));
				Direction direction = DirectionService.getDirectionById(tests.get(0).getDirectionId());
				builder.append(direction.getName());
				builder.append(" "+MessageManager.getInstance().getProperty(MessageManager.TEST, new Locale(locale)));
				builder.append("</h6>");
				builder.append("<div class=\"row\">");
				builder.append("<div class=\"col s6\">");
				builder.append("<div class=\"input-field \">");
				builder.append("<i class=\"mdi-communication-live-help prefix\"></i>");
				builder.append("<inputid=\"new_question\" type=\"text\" class=\"validate\">");
				builder.append("<label for=\"new_question\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.QUESTION, new Locale(locale)));
				builder.append("</label>");
				builder.append("</div>");
				builder.append("<div class=\"input-field\">");
				builder.append("<textarea id=\"new_test_code\" class=\"materialize-textarea\">");
				builder.append("</textarea><label for=\"new_test_code\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CODE_FRAGMENT, new Locale(locale)));
				builder.append("</label>");
				builder.append("</div>");
				builder.append("</div>");
				builder.append("<div class=\"col s6\">");
				builder.append("<div class=\"row\">");
				builder.append("<table>");
				builder.append("<thead style=\"border-bottom-width: 0px;\">");
				builder.append("<tr class=\"text\" style=\"font-style: italic; font-size: 14px;\">");
				builder.append("<td>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CORRECT, new Locale(locale)));
				builder.append("</td><td>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.OPTIONS, new Locale(locale)));
				builder.append("</td>");
				builder.append("</tr>");
				builder.append("</thead>");
				builder.append("<tbody id=\"answers\"></tbody>");
				builder.append("</table>");
				builder.append("</div>");
				builder.append("</div>");
				builder.append("</div>");
				builder.append("<div class=\"modal-footer\">");
				builder.append("<a id=\"butAddTest\" onclick=\"addNewTest();\" class=\"modal-action waves-effect waves-green btn-flat right\"");
				builder.append("style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.ADD, new Locale(locale)));
				builder.append("</a>");
				builder.append("<a class=\"modal-close waves-effect waves-green btn-flat left\"");
				builder.append("style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CANCEL, new Locale(locale)));
				builder.append("</a>");
				builder.append("</div></div></div>");
				
				builder.append("<div id=\"editTestModal\" class=\"modal indigo-text\"");
				builder.append("style=\"max-height: 680px !important;\">");
				builder.append("<div class=\"modal-content\">");
				builder.append("<h6 class=\"black-text\">");
				builder.append("<i class=\"small mdi-action-info\">");
				builder.append("</i>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.EDIT, new Locale(locale)));
				builder.append(direction.getName());
				builder.append(" "+MessageManager.getInstance().getProperty(MessageManager.TEST, new Locale(locale))+"</h6><div class=\"row\">");
				builder.append("<div class=\"col s6\">");
				builder.append("<input type=\"hidden\" id=\"edit_id\"/>");
				builder.append("<div class=\"input-field \">");
				builder.append("<i class=\"mdi-communication-live-help prefix\">");
				builder.append("</i>");
				builder.append("<input id=\"edit_question\" type=\"text\" class=\"validate\" placeholder=\"");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.QUESTION, new Locale(locale)));
				builder.append("\">");
				builder.append("</div>");
				builder.append("<div class=\"input-field\">");
				builder.append("<textarea id=\"edit_test_code\" class=\"materialize-textarea\" placeholder=\"");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CODE_FRAGMENT, new Locale(locale)));
				builder.append("\"></textarea>");
				builder.append("</div>");
				builder.append("</div>");
				builder.append("<div class=\"col s6\">");
				builder.append("<div class=\"row\">");
				builder.append("<table>");
				builder.append("<thead style=\"border-bottom-width: 0px;\">");
				builder.append("<tr class=\"text\" style=\"font-style: italic; font-size: 14px;\">");
				builder.append("<td>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CORRECT, new Locale(locale)));
				builder.append("</td><td>");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.OPTIONS, new Locale(locale)));
				builder.append("</td></tr>");
				builder.append("</thead>");
				builder.append("<tbody id=\"edit_answers\">");
				builder.append("</tbody></table></div></div></div>");
				builder.append("<div class=\"modal-footer\">");
				builder.append("<a id=\"butAddTest\" onclick=\"editTest();\" style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.SUBMIT, new Locale(locale)));
				builder.append("</a>");
				builder.append("<a class=\"modal-close waves-effect waves-green btn-flat left\" style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CANCEL, new Locale(locale)));
				builder.append("</a></div></div>");
				
				builder.append("<div id=\"removeTestModal\" class=\"modal purple lighten-5 red-text text-accent-2\"");
				builder.append("style=\"max-height: 680px !important;\"><div class=\"modal-content\">");
				builder.append("<h6 class=\"black-text\"><i class=\"small mdi-action-info\"></i>");
				builder.append(" ");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.TEST_REMOVAL, new Locale(locale)));
				builder.append("</h6>");
				builder.append("<div class=\"row\">");
				builder.append("<div class=\"col s12\">");
				builder.append("<input type=\"hidden\" id=\"remove_id\"/>");
				builder.append("<h5 class=\"center-align\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.ARE_U_SURE_DELETE_TEST, new Locale(locale)));
				builder.append("</h5></div></div>");
				builder.append("<div class=\"modal-footer purple lighten-5\">");
				builder.append("<div class=\"row\">");
				builder.append("<a id=\"butAddTest\" onclick=\"removeTest();\"");
				builder.append("class=\"modal-action waves-effect waves-green btn-flat right\" style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.REMOVE, new Locale(locale)));
				builder.append("</a>");
				builder.append("<a class=\"modal-close waves-effect waves-green btn-flat left\" style=\"position: inherit;\">");
				builder.append(MessageManager.getInstance().getProperty(MessageManager.CANCEL, new Locale(locale)));
				builder.append("</a></div></div></div></div>");		
				writer.write(builder.toString());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Test> getTests() {
		return tests;
	}

	public void setTests(ArrayList<Test> tests) {
		this.tests = tests;
	}
}
