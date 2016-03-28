package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Interview;
import edu.epam.role.CommonUser;
import edu.epam.service.TestService;
@UserPermissions({RoleType.TRAINEE, RoleType.GRADUATE})
public class GetLatestTestsCommand implements AjaxActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {		
	    response.setContentType("application/xml");
	    response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");    
		int result = 0;
		/*коментар Бодака Романа*/
		CommonUser logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		
			int user_id = logined_user.getId();
			List<Interview> list_of_interview = TestService.getClosestTestMeeting(user_id);
				System.out.println("list of directions = " + list_of_interview);
			List<Interview> list_of_applied_test_events = TestService.getTraineeAppliedTestEvents(user_id);
				System.out.println(" APPLIED TESTS = " + list_of_applied_test_events);
			if(null != list_of_interview && list_of_interview.size()>0){
				result = 1; // All Ok
			}else{
				result = 2; // The list is empty
			}	
			if(result == 1){
				responseBuilder.append("<directions>");
				fillXMLbyDirections(responseBuilder, list_of_interview, locale);
				if(list_of_applied_test_events != null){
					System.out.println("applied directions not null");
					fillXMLbyAppliedDirections(responseBuilder, list_of_applied_test_events, locale);
				}
				responseBuilder.append("</directions>");
			}else{
				responseBuilder.append("<directions>");
					responseBuilder.append("<status>");
					System.out.println("status = " + result);
						responseBuilder.append(result);
					responseBuilder.append("</status>");
					if(list_of_applied_test_events != null){
						fillXMLbyAppliedDirections(responseBuilder, list_of_applied_test_events, locale);
					}
				responseBuilder.append("</directions>");
			}
		
		System.out.println("return ");
		return responseBuilder.toString();
		
	}
	
	
	private StringBuilder fillXMLbyAppliedDirections(StringBuilder responseBuilder, 
			List<Interview> list_of_applied_test_events,Locale  locale){
		for(Interview interview : list_of_applied_test_events){	
			System.out.println("Applied interview = " + interview);
			responseBuilder.append("<appliedDirection>");
				responseBuilder.append("<id>");
				responseBuilder.append(interview.getId());
				responseBuilder.append("</id>");
				responseBuilder.append("<name>");
				responseBuilder.append(interview.getDirectionInfo().getName());
				responseBuilder.append("</name>");
				responseBuilder.append("<recruter_id>");
				responseBuilder.append(interview.getDirectionInfo().getRecruter_id());
				responseBuilder.append("</recruter_id>");
				responseBuilder.append("<date_of_testing>");
				
				String dateFormated = new SimpleDateFormat("dd-MMMM-yyyy HH:mm",locale).format(interview.getDateOfTesting());
				String date = MessageManager.getInstance().getProperty(MessageManager.TESTS_WILL_BE, locale) + 
						" " + dateFormated + " " + MessageManager.getInstance().getProperty(MessageManager.HOURS, locale);
				
				responseBuilder.append(date);
				responseBuilder.append("</date_of_testing>");
				responseBuilder.append("<place_of_testing>");
				responseBuilder.append(interview.getPlace());
				responseBuilder.append("</place_of_testing>");
			
			responseBuilder.append("</appliedDirection>");
		}
		return responseBuilder;
	}
	
	
	private StringBuilder fillXMLbyDirections(StringBuilder responseBuilder, List<Interview> list_of_directions, Locale locale){
		
		list_of_directions.sort(new Comparator<Interview>() {

			@Override
			public int compare(Interview o1, Interview o2) {
				
				return o1.getDateOfTesting().compareTo(o2.getDateOfTesting());
			}
		});
		for(Interview interview : list_of_directions){	
			System.out.println("interview = " + interview);
			responseBuilder.append("<directionInfo>");
				responseBuilder.append("<id>");
				responseBuilder.append(interview.getId());
				responseBuilder.append("</id>");
				responseBuilder.append("<name>");
				responseBuilder.append(interview.getDirectionInfo().getName());
				responseBuilder.append("</name>");
				responseBuilder.append("<recruter_id>");
				responseBuilder.append(interview.getDirectionInfo().getRecruter_id());
				responseBuilder.append("</recruter_id>");
				responseBuilder.append("<date_of_testing>");
			
				
				String dateFormated = new SimpleDateFormat("dd-MMMM-yyyy HH:mm",locale).format(interview.getDateOfTesting());
				String date = MessageManager.getInstance().getProperty(MessageManager.TESTS_WILL_BE, locale) + 
						" " + dateFormated + " " + MessageManager.getInstance().getProperty(MessageManager.HOURS, locale);
				
				responseBuilder.append(date);
				responseBuilder.append("</date_of_testing>");
				responseBuilder.append("<place_of_testing>");
				responseBuilder.append(interview.getPlace());
				responseBuilder.append("</place_of_testing>");
			responseBuilder.append("</directionInfo>");
		}
		return responseBuilder;
	}

}
