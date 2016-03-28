package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.pagination.Paginator;
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.DirectionService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.HR })
public class ViewTraineesPaginated implements AjaxActionCommand {

		private final int entriesPerPage = 10;
		private int pageNumber;
		private int currentDirectionId;

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
			responseBuilder.append("<trainees>");
			
			Map<Direction, List<Trainee>> directionMap = new HashMap<>();
		    HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		    
		    try {
		    	List<Direction> directions = DirectionService.getDirectionsByHRId(hr.getId());
		    	String tableNumber = request.getParameter("table_number");
				if(tableNumber == null)
					setPageNumber(1);
				else
					setPageNumber(Integer.valueOf(tableNumber));
				
				String currentDirection = request.getParameter("direction");
				if(currentDirection == null || currentDirection.equals(""))
					setCurrentDirectionId(directions.get(0).getId());
				else
					setCurrentDirectionId(Integer.valueOf(currentDirection));
				
			    for(Direction direction : directions) {
			    	int i = 1; 
			    	if(direction.getId() == currentDirectionId) {
			    		i = pageNumber * entriesPerPage - (entriesPerPage - 1);
			    	}
			    	
				    List<Trainee> trainees = TraineeService.getPortionOfTraineesByApplicationDirectionId(direction.getId(), i - 1, entriesPerPage);
				    LinkedHashSet<Trainee> lhs = new LinkedHashSet<Trainee>();
				    lhs.addAll(trainees);
				    trainees.clear();
				    trainees.addAll(lhs);
				    directionMap.put(direction, trainees);
			    }
			    builder.append("");
			    builder.append("<div class=\"row\">");
			    builder.append("<div class=\" col s8 offset-s2 \">");
			    builder.append("<div class=\"card-panel white\">");
			    builder.append("<div class=\"row\">");
			    builder.append("<div class=\"row\">");
			    builder.append("<div class=\"col s12\">");
			    builder.append("<ul class=\"tabs\">");
			    for(Direction d : directionMap.keySet()) {
			    	builder.append("<li id=\"paginatedTab\" value=\"");
				    builder.append(d.getId());
				    builder.append("\" class=\"tab col s3 super\"><a href=\"#id");
			    	builder.append(d.getId());
			    	builder.append("\">");
				    builder.append(d.getName());
				    builder.append("</a></li>");
			    }
			    builder.append("</ul>");
			    builder.append("</div>");
			    
			    for(Direction d : directionMap.keySet()) {
			    	int activePage = 1;
			    	int lastPage = 1;
			    	int traineeListAmount = TraineeService.getTraineesAmountByApplicationDirectionId(d.getId()); //витягувати з бази щоразу
			    	int pageCount = traineeListAmount/entriesPerPage;
			    	if (pageCount * entriesPerPage != traineeListAmount) {
			    		pageCount++;
			    	}
			    	lastPage = pageCount; //last
			    	if(d.getId() == currentDirectionId) {
			    		activePage = pageNumber; //active, only for current Direction
			    	}
					
				    builder.append("<div id=\"id");
				    builder.append(d.getId());
				    builder.append("\" class=\"col s12\">");
				    if(!directionMap.get(d).isEmpty()) {
						Paginator.getPages(builder, activePage, lastPage);
					}
				    builder.append("<ul class=\"collapsible\" data-collapsible=\"accordion\">");
				    if(directionMap.get(d).isEmpty()) {
				    	builder.append("<h6 class=\"center-align blue-text text-darken-2\">");
					    builder.append("No Trainees applicant to this direction");
					    builder.append("</h6>");
				    }
				    for(Trainee t : directionMap.get(d)) {
				    	builder.append("<li id=\"");
					    builder.append(t.getId());
					    builder.append("\">");
					    builder.append("<div class=\"collapsible-header\">");
					    builder.append("<i class=\"mdi-action-account-circle\"></i>");
					    builder.append(t.getLastName());
					    builder.append(" ");
					    builder.append(t.getFirstName());
					    builder.append(" ");
					    builder.append(t.getParentName());
					    builder.append("</div>");
					    builder.append("<div class=\"collapsible-body\">");
				    	
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s12\">");
						builder.append("<div class=\"card\">");
						builder.append("<div class=\"card-image\"");
						builder.append("style=\"width: 240px; display: block; margin-left: auto; margin-right: auto;\">");
						builder.append("<img src=\"");
//						builder.append("${pageContext.request.contextPath}");
						builder.append("/images?type=users&id=${trainee.id}\">");
						builder.append("</div>");
						builder.append("<div class=\"card-content\">");
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">Email</div>");
						builder.append("<div class=\"col s8\">");
						builder.append(t.getEmail());
						builder.append("</div>");
						builder.append("</div>");
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">");
						builder.append("Skills");
						builder.append("</div>");
						builder.append("<div class=\"col s8\">");
						builder.append(t.getCv().getSkills());
						builder.append("</div>");
						builder.append("</div>");
	
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">");
						builder.append("English Level");
						builder.append("</div>");
						builder.append("<div class=\"col s8\">");
						builder.append(t.getCv().getEnglishLevel());
						builder.append("</div>");
						builder.append("</div>");
	
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">");
						builder.append("Phone");
						builder.append("</div>");
						builder.append("<div class=\"col s8\">");
						builder.append(t.getCv().getPhone());
						builder.append("</div>");
						builder.append("</div>");
	
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">");
						builder.append("Additional Info");
						builder.append("</div>");
						builder.append("<div class=\"col s4\">");
						builder.append(t.getCv().getAdditionalInfo());
						builder.append("</div>");
						builder.append("</div>");
	
						builder.append("<div class=\"row\">");
						builder.append("<div class=\"col s3\">");
						builder.append("Education");
						builder.append("</div>");
						builder.append("<div class=\"col s4\">");
						builder.append(t.getCv().getEducation());
						builder.append("</div>");
						builder.append("</div>");
						builder.append("</div>");
						builder.append("<div class=\"card-action\">");
						builder.append("<a class=\"waves-effect waves-light btn modal-trigger\" href=\"#sendMessageModal\" onclick=\"processUserInfo()\">");
						builder.append("Send Message");
						builder.append("</a>");
						builder.append("</div>");
						builder.append("</div>");
						builder.append("</div>");
						builder.append("</div>");
						builder.append("</div>");
						builder.append("</li>");
				    }
				    builder.append("</ul>");
				    builder.append("</div>");
			    }
			    builder.append("</div>");
			    builder.append("</div>");
			    builder.append("</div>");
			    builder.append("</div>");
			    builder.append("</div>");
			    
			    String contentPortion = builder.toString();
				String lowerThan = contentPortion.replaceAll("([<])", "&lt;");
				String greaterThan = lowerThan.replaceAll("([>])", "&gt;");
				responseBuilder.append("<contentPortion>");
				responseBuilder.append(greaterThan);
				responseBuilder.append("</contentPortion>");
				
		    } catch(Exception e) {
		    	e.printStackTrace();
		    	result = 2;
		    }
			
			responseBuilder.append("<status>");
			responseBuilder.append(result);
			responseBuilder.append("</status>");
			responseBuilder.append("</trainees>");
			return responseBuilder.toString();
		}


		public int getPageNumber() {
			return pageNumber;
		}
		
		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getCurrentDirectionId() {
			return currentDirectionId;
		}

		public void setCurrentDirectionId(int currentDirectionId) {
			this.currentDirectionId = currentDirectionId;
		}
}
