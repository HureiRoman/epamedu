package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.pagination.Paginator;
import edu.epam.service.DirectionService;
import edu.epam.service.TestService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class TestEditPaginated implements AjaxActionCommand {

	private final int testsPerPage = 10;
	private int testsAmount;
	private int pageNumber;
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		Integer result = 1;

		response.setContentType("application/xml");
	    response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append("<tests>");
		
		Direction direction;
		try {
			String tableNumber = request.getParameter("table_number");
			if(tableNumber == null)
				setPageNumber(1);
			else
				setPageNumber(Integer.valueOf(tableNumber));
			
			String testsTotal = request.getParameter("tests_amount");
			if(testsTotal == null)
				throw new IOException();
			else
				setTestsAmount(Integer.valueOf(testsTotal));
		
			direction = DirectionService.getDirectionById(Integer.parseInt(request.getParameter("direction")));
			
			builder.append("<div class=\"row\">");
			builder.append("<div class=\"col s10 offset-s1\">");
			builder.append("<div class=\"row\"> ");
			builder.append("<div class=\"indigo-text \"> ");
			builder.append("<div class=\"col s12 \"> ");
			
			int i = pageNumber * testsPerPage - (testsPerPage - 1);

			List<Test> testsPortion = TestService.getTestsPortionForDirection(direction.getName(), i - 1, testsPerPage);
			
			int pageCount = testsAmount/testsPerPage;
			if (pageCount * testsPerPage != testsAmount) {
				pageCount++;
			}
			int activePage = pageNumber; //active
			int lastPage = pageCount; //last
			if(!testsPortion.isEmpty()) {
				Paginator.getPages(builder, activePage, lastPage);
			}
			String correctUpperDivTag = "<div class=\"green-text text-accent-4\" style=\"word-break: break-all;\"><i class=\"fa fa-check-square-o\" style=\"margin-bottom: 10px;margin-right: 10px;\"></i><b>";
			String incorrectUpperDivTag = "<div style=\"word-break: break-all;\"><i class=\"fa fa-square-o\" style=\"margin-bottom: 10px;margin-right: 10px;\"></i>";
			
			builder.append("<ul class=\"collapsible\" data-collapsible=\"accordion\">");
			for (Test t : testsPortion) {
				builder.append("<li><div class=\"collapsible-header\"><div class=\"truncate\">" );
				builder.append("<i>" + i + ". </i>");
				i++;
				builder.append("<i class=\"black-text tiny material-icons\">");
				if (t.getCode() != null) {
					builder.append("settings_ethernet");
				}
				else {
					builder.append("list");
				}
				builder.append("</i>");
				builder.append(t.getQuestion());
				builder.append("</div></div>");
				
				builder.append("<div class=\"collapsible-body\" style=\"padding-left: 2%\">");
				builder.append("<h5 class=\"center-align\" style=\"word-break: break-all;\">");
				builder.append(t.getQuestion());
				builder.append("</h5>");
				if (t.getCode() != null) {
					builder.append("<pre  name=\"code\" class=\"" + direction.getCodeLang() + "\" cols=\"60\" rows=\"10\">");
					builder.append(t.getCode());
					builder.append("</pre>");
				}
					//якщо номер відповіді коректний – витягаємо відповідь
					builder.append("<div>");
					if(t.getCorrect().equals(1)) {
						builder.append(correctUpperDivTag);
						builder.append(t.getAnswer1());
						builder.append("</b></div>");
					}
					else {
						builder.append(incorrectUpperDivTag);
						builder.append(t.getAnswer1());
						builder.append("</div>");
					}
					builder.append("</div>");
					builder.append("<div>");
					if(t.getCorrect().equals(2)) {
						builder.append(correctUpperDivTag);
						builder.append(t.getAnswer2());
						builder.append("</b></div>");
					}
					else {
						builder.append(incorrectUpperDivTag);
						builder.append(t.getAnswer2());
						builder.append("</div>");
					}
					builder.append("</div>");
					builder.append("<div>");
					if(t.getCorrect().equals(3)) {
						builder.append(correctUpperDivTag);
						builder.append(t.getAnswer3());
						builder.append("</b></div>");
					}
					else {
						builder.append(incorrectUpperDivTag);
						builder.append(t.getAnswer3());
						builder.append("</div>");
					}
					builder.append("</div>");
					builder.append("<div>");
					if(t.getCorrect().equals(4)) {
						builder.append(correctUpperDivTag);
						builder.append(t.getAnswer4());
						builder.append("</b></div>");
						
					}
					else {
						builder.append(incorrectUpperDivTag);
						builder.append(t.getAnswer4());
						builder.append("</div>");
					}
					builder.append("</div>");
					builder.append("<div class=\"center-align\">");
					builder.append("<div class=\"green-text text-accent-4\">");
					builder.append("<a class=\"modal-action btn-flat\" onclick=\"editChosenTest(" + t.getId() + ");\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.EDIT, locale));
					builder.append("</a>");
					builder.append("<a class=\"modal-action btn-flat\" onclick=\"removeChosenTest(" + t.getId() + ");\">");
					builder.append(MessageManager.getInstance().getProperty(MessageManager.REMOVE, locale));
					builder.append("</a>");
					builder.append("</div></div>");
				builder.append("</div>");
				builder.append("</li>");
			}
			builder.append("</ul>");
			
			if(!testsPortion.isEmpty()) {
				Paginator.getPages(builder, activePage, lastPage);
			}
				
			builder.append("</div></div></div></div></div>");
			
			String testPortion = builder.toString();
			String lowerThan = testPortion.replaceAll("([<])", "&lt;");
			String greaterThan = lowerThan.replaceAll("([>])", "&gt;");
			responseBuilder.append("<testPortion>");
			responseBuilder.append(greaterThan);
			responseBuilder.append("</testPortion>");
			
		} catch (IOException | NullPointerException | SQLException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
			result = 2;
		}
		
		responseBuilder.append("<status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append("</tests>");
		return responseBuilder.toString();
	}

	public int getTestsAmount() {
		return testsAmount;
	}

	public void setTestsAmount(int testsAmount) {
		this.testsAmount = testsAmount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
}
