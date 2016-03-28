package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Test;
import edu.epam.service.TestService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class EditTestCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer result = 2;
		
		response.setContentType("application/xml");
	    response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append("<test>");
		
		String testId = request.getParameter("test_id");
		String aim = request.getParameter("aim");
		int id = Integer.parseInt(testId);
		
		Test test = TestService.getTestById(id);
		
		if (aim != null && aim.equalsIgnoreCase("edit")) {	// редагування
			
			String question = null;
			String code = null;
			String correct = null;
			String answer1 = null;
			String answer2 = null;
			String answer3 = null;
			String answer4 = null;
			
			question = request.getParameter("question");
			code = request.getParameter("code");
			if (code.equals("") || code.matches("^\\s*$")) code = null;
			correct = request.getParameter("correct");
			answer1 = request.getParameter("answer1");
			answer2 = request.getParameter("answer2");
			answer3 = request.getParameter("answer3");
			answer4 = request.getParameter("answer4");
			
			String message = "";
			String info = MessageManager.getInstance().getProperty(MessageManager.ERROR,  locale);
			
			if(question == null || question.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.EMPTY_QUESTION, locale);//Question is empty!
			}
			else if(correct == null || correct.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.noCorrectAnswer, locale); 
			}
			else if(answer1 == null || answer1.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.specifyAnswer1, locale); 
			}
			else if(answer2 == null || answer2.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.specifyAnswer2, locale);
			}
			else if(answer3 == null || answer3.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.specifyAnswer3, locale);
			}
			else if(answer4 == null || answer4.matches("^\\s*$")) {
				message = MessageManager.getInstance().getProperty(MessageManager.specifyAnswer4, locale);
			}
			else if(code != null && code.length() > 1000) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_CODE, locale);
			}
			else if(question.length() > 350) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_QUESTION, locale);
			}
			else if(answer1.length() > 500) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_ANSWER1, locale);
			}
			else if(answer2.length() > 500) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_ANSWER2, locale);
			}
			else if(answer3.length() > 500) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_ANSWER3, locale);
			}
			else if(answer4.length() > 500) {
				message = MessageManager.getInstance().getProperty(MessageManager.TOO_LONG_ANSWER4, locale);
			}
			else if(test.getQuestion().equalsIgnoreCase(question) && ((test.getCode() == null && code == null) 
					|| ((test.getCode() != null && code != null) && test.getCode().equalsIgnoreCase(code))) 
					&& test.getAnswer1().equalsIgnoreCase(answer1) && test.getAnswer2().equalsIgnoreCase(answer2) 
					&& test.getAnswer3().equalsIgnoreCase(answer3) && test.getAnswer4().equalsIgnoreCase(answer4) 
					&& test.getCorrect().toString().equalsIgnoreCase(correct)) {
				message = MessageManager.getInstance().getProperty(MessageManager.YOU_HAVE_NOT_CHANGE_ANYTHING,  locale);
			}
			else {
				test.setQuestion(question);
				test.setCode(code);
				test.setCorrect(Integer.parseInt(correct));
				test.setAnswer1(answer1);
				test.setAnswer2(answer2);
				test.setAnswer3(answer3);
				test.setAnswer4(answer4);
				if(TestService.editTest(test) != null) {
					result = 1;
					info = MessageManager.getInstance().getProperty(MessageManager.Edited,  locale);
				}
			}
			responseBuilder.append("<info>");
			responseBuilder.append(info);
			responseBuilder.append("</info>");
			responseBuilder.append("<status>");
			responseBuilder.append(result);
			responseBuilder.append("</status>");
			responseBuilder.append("<message>");
			responseBuilder.append(message);
			responseBuilder.append("</message>");
		} 
		
		else if(aim != null && aim.equalsIgnoreCase("removal")) { //test removal
			result = TestService.removeTest(test);
			responseBuilder.append("<status>");
			responseBuilder.append(result);
			responseBuilder.append("</status>");
			responseBuilder.append("<info>");
			responseBuilder.append(MessageManager.getInstance().getProperty(MessageManager.REMOVED,  locale));
			responseBuilder.append("</info>");
		}
		
		else if (aim != null && aim.equalsIgnoreCase("getTest")) { //витягнення поточного тесту для редагування
			if(test != null) {
				responseBuilder.append("<id>");
				responseBuilder.append(test.getId());
				responseBuilder.append("</id>");
				responseBuilder.append("<question>");
				responseBuilder.append(test.getQuestion());
				responseBuilder.append("</question>");
				responseBuilder.append("<code>");
				String code = test.getCode();
				if(test.getCode() == null) code = "";
				responseBuilder.append(code);
				responseBuilder.append("</code>");
				responseBuilder.append("<answer1>");
				responseBuilder.append(test.getAnswer1());
				responseBuilder.append("</answer1>");
				responseBuilder.append("<answer2>");
				responseBuilder.append(test.getAnswer2());
				responseBuilder.append("</answer2>");
				responseBuilder.append("<answer3>");
				responseBuilder.append(test.getAnswer3());
				responseBuilder.append("</answer3>");
				responseBuilder.append("<answer4>");
				responseBuilder.append(test.getAnswer4());
				responseBuilder.append("</answer4>");
				responseBuilder.append("<correct>");
				responseBuilder.append(test.getCorrect());
				responseBuilder.append("</correct>");
				responseBuilder.append("<status>");
				responseBuilder.append(1);
				responseBuilder.append("</status>");
			}
			else {
				responseBuilder.append("<status>");
				responseBuilder.append(2);
				responseBuilder.append("</status>");
				responseBuilder.append("<message>");
				responseBuilder.append(MessageManager.getInstance().getProperty(MessageManager.TEST_NOT_FOUND,  locale));
				responseBuilder.append("</message>");
			}
			
		}
		
		responseBuilder.append("</test>");
		return responseBuilder.toString();
	}
}
