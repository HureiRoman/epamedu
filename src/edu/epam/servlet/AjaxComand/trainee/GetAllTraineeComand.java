package edu.epam.servlet.AjaxComand.trainee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Trainee;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.HR})
public class GetAllTraineeComand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
	
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		
		int result = 0;
		
		List<Trainee> list_of_trainee = TraineeService.getAllTrainee();
		
		if(list_of_trainee!=null){
			responseBuilder.append("<traineeList>");
			System.out.println("TRAINEE LIST size = " + list_of_trainee.size());
				getTraineeInfo(responseBuilder, list_of_trainee, locale);
			responseBuilder.append("</traineeList>");
		}else{
			responseBuilder.append("<status>");
			result = 3; // STATUS : Emty list
				responseBuilder.append(result);
			responseBuilder.append("</status>");
		}	
		return responseBuilder.toString();
		
	}
	
	
	private void getTraineeInfo(StringBuilder responseBuilder, List<Trainee> list_of_trainee, Locale locale ) throws SQLException{
		
		for(Trainee trainee : list_of_trainee){
			responseBuilder.append("<trainee>");
				responseBuilder.append("<trainee_id>");
					responseBuilder.append(trainee.getId());
				responseBuilder.append("</trainee_id>");
				responseBuilder.append("<trainee_name>");
					responseBuilder.append(trainee.getFirstName() );
				responseBuilder.append("</trainee_name>");
				responseBuilder.append("<trainee_lastname>");
					responseBuilder.append(trainee.getLastName());
				responseBuilder.append("</trainee_lastname>");
				responseBuilder.append("<trainee_email>");
					responseBuilder.append(trainee.getEmail());
				responseBuilder.append("</trainee_email>");
				responseBuilder.append("<trainee_phone>");
					responseBuilder.append(trainee.getCv().getPhone());
				responseBuilder.append("</trainee_phone>");
				
			responseBuilder.append("</trainee>");
		}
		
		
	}

}
