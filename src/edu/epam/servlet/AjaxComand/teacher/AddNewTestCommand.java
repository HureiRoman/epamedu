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

@UserPermissions({RoleType.TEACHER})
public class AddNewTestCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int result = 2;
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append("<test>");
		
		String question = null;
		String code = null;
		String direction = null;
		String correct = null;
		String answer1 = null;
		String answer2 = null;
		String answer3 = null;
		String answer4 = null;
		
		Test test = new Test();
		question = request.getParameter("question");
		code = request.getParameter("code");
		if (code.equals("") || code.matches("^\\s*$")) code = null;
		direction = request.getParameter("direction");
		correct = request.getParameter("correct");
		answer1 = request.getParameter("answer1");
		answer2 = request.getParameter("answer2");
		answer3 = request.getParameter("answer3");
		answer4 = request.getParameter("answer4");
		
		String message = "";
		String info = MessageManager.getInstance().getProperty(MessageManager.ERROR, locale);//
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
		else {
			test.setQuestion(question);
			test.setDirectionId(Integer.parseInt(direction));
			test.setCode(code);
			test.setAnswer1(answer1);
			test.setAnswer2(answer2);
			test.setAnswer3(answer3);
			test.setAnswer4(answer4);
			test.setCorrect(Integer.parseInt(correct));
			
			Integer testId = TestService.addNewTest(test);
			if (testId == 0) {
				result = 0;
			}
			else {
				result = 1;
				info = MessageManager.getInstance().getProperty(MessageManager.ADDED, locale);
			}
		}
		responseBuilder.append("<message>");
		responseBuilder.append(message);
		responseBuilder.append("</message>");
		responseBuilder.append("<info>");
		responseBuilder.append(info);
		responseBuilder.append("</info>");
		responseBuilder.append("<status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append("</test>");
		return responseBuilder.toString();
	}

}
