package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.MessageManager;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.role.CommonUser;
import edu.epam.service.DirectionService;
import edu.epam.service.TestService;
@UserPermissions({RoleType.TRAINEE,RoleType.STUDENT,RoleType.TEACHER,RoleType.HR,RoleType.GRADUATE})
public class GetAllDirectionsCommand implements AjaxActionCommand {
	private CommonUser logined_user;
	private List<Integer> listOfSubscribedDirections;
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");   
		int result = 0;
		
		List<Direction> list_of_directions = DirectionService.getAllDirectionsWithInterview();
		System.out.println("LIST OF DIRECTIONS = " + list_of_directions);
		
		logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		if(logined_user != null){
			int user_id = logined_user.getId();
			
			List<Interview> list_of_applied_test_events = TestService.getTraineeAppliedTestEvents(user_id);
			listOfSubscribedDirections = TestService.getListOfSubscribedToNewsDirections(user_id);
			
				System.out.println(" APPLIED TESTS = " + list_of_applied_test_events);
			Map<Integer,Interview> mapIdDirectionInterview = null;
				if(list_of_applied_test_events!=null){
					mapIdDirectionInterview = new HashMap<>();
					for(Interview interview : list_of_applied_test_events){
						mapIdDirectionInterview.put(interview.getDirectionInfo().getId(), interview);
					}
			}
			responseBuilder.append("<directions_main>");
			
			if(list_of_directions != null){
				result = 1; //All OK!!!
				System.out.println("List not empty");
				writeAllDirections(responseBuilder,list_of_directions,mapIdDirectionInterview, locale);
				
			}else{
				result = 2; //Empty list!!!
				
			}
		}
		responseBuilder.append("<status>");
		responseBuilder.append(result); 
		responseBuilder.append("</status>");
		responseBuilder.append("</directions_main>");
		return responseBuilder.toString();
	}
	
	
	
	private void writeAllDirections(StringBuilder responseBuilder, List<Direction> list_of_directions,Map<Integer,Interview> mapIdDirectionInterview, Locale locale ){
		
		responseBuilder.append("<directions>");
		for(Direction direction : list_of_directions){	
			responseBuilder.append("<direction>");
				responseBuilder.append("<direction_id>");
					responseBuilder.append(direction.getId()); 
				responseBuilder.append("</direction_id>"); 
				responseBuilder.append("<direction_name>");
					responseBuilder.append(direction.getName()); 
				responseBuilder.append("</direction_name>"); 
				responseBuilder.append("<recruter_id>");
					responseBuilder.append(direction.getRecruter_id()); 
				responseBuilder.append("</recruter_id>"); 
				responseBuilder.append("<is_active>");
					responseBuilder.append(direction.getIsActive()); 
				responseBuilder.append("</is_active>"); 
				responseBuilder.append("<description>");
					responseBuilder.append(direction.getDescription()); 
				responseBuilder.append("</description>");
				if(mapIdDirectionInterview != null){
					Interview item = mapIdDirectionInterview.get(direction.getId());
					if(item != null){
						responseBuilder.append("<direction_status>");
						responseBuilder.append(2);//Direction status : trainee applied on this direction 
						responseBuilder.append("</direction_status>");
						

						responseBuilder.append("<date_of_testing>");
//						String date = new SimpleDateFormat(MessageManager.getInstance().getProperty(MessageManager.TESTS_WILL_BE, locale)
//								+ " dd-MMMM-yyyy; HH:mm "+
//								MessageManager.getInstance().getProperty(MessageManager.HOURS, locale)
//								,locale).format(item.getDateOfTesting());
						String dateFormat = new SimpleDateFormat("dd-MMMM-yyyy; HH:mm",locale).format(item.getDateOfTesting());
						String date = MessageManager.getInstance().getProperty(MessageManager.TESTS_WILL_BE, locale) + dateFormat + MessageManager.getInstance().getProperty(MessageManager.HOURS, locale);
						
						responseBuilder.append(date);
						responseBuilder.append("</date_of_testing>"); 
						responseBuilder.append("<place_of_testing>");
							responseBuilder.append(item.getPlace()); 
						responseBuilder.append("</place_of_testing>"); 
						
						responseBuilder.append("<interview_description>");
							responseBuilder.append(item.getDescription()); 
						responseBuilder.append("</interview_description>"); 
						
					}else if(direction.getInterview() != null){
						responseBuilder.append("<direction_status>");
							responseBuilder.append(3);//Direction status : trainee can apply on this direction 
						responseBuilder.append("</direction_status>");
						responseBuilder.append("<interview_id>");
							responseBuilder.append(direction.getInterview().getId()); //Id of interview on which TRAINNE can apply
						responseBuilder.append("</interview_id>");
						
						
					}else{
						responseBuilder.append("<direction_status>");
						responseBuilder.append(1);//Direction status : no application on this direction 
						responseBuilder.append("</direction_status>");
						
					}
					
				}else if(direction.getInterview() == null){
					responseBuilder.append("<direction_status>");
					responseBuilder.append(1);//Direction status : no application on this direction 
					responseBuilder.append("</direction_status>");
					
					
				}else if(direction.getInterview() != null){
					responseBuilder.append("<direction_status>");
					responseBuilder.append(3);//Direction status : trainee can apply on this direction 
				responseBuilder.append("</direction_status>");
				responseBuilder.append("<interview_id>");
					responseBuilder.append(direction.getInterview().getId()); //Id of interview on which TRAINNE can apply
				responseBuilder.append("</interview_id>");
				}
				boolean subscribed = false;
				if(listOfSubscribedDirections != null && listOfSubscribedDirections.contains(direction.getId())){
					subscribed = true;
				}
				responseBuilder.append("<isSubscribed>");
				responseBuilder.append(subscribed);
				responseBuilder.append("</isSubscribed>");
			responseBuilder.append("</direction>"); 
		}
		responseBuilder.append("</directions>");
		
	
	}
	
}
