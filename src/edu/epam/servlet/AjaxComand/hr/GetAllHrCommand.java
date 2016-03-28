package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.role.HR;
import edu.epam.service.HrService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TRAINEE,RoleType.GRADUATE,RoleType.ADMIN})
public class GetAllHrCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		
		int result = 0;
		
		List<HR> list_of_hr = HrService.getAllHr();
		
		if(list_of_hr!=null){
			responseBuilder.append("<hrList>");
				getHrInfo(responseBuilder, list_of_hr, locale);
			responseBuilder.append("</hrList>");
		}else{
			responseBuilder.append("<status>");
			result = 3; // STATUS : Emty list
				responseBuilder.append(result);
			responseBuilder.append("</status>");
		}	
		return responseBuilder.toString();
	}
	
	
	private void getHrInfo(StringBuilder responseBuilder, List<HR> list_of_hr, Locale locale ) throws SQLException{
		
		for(HR hr : list_of_hr){
			List<Direction> list_of_hr_directions =  HrService.getAllHrDirections(hr.getId());
			responseBuilder.append("<hr>");
				responseBuilder.append("<hr_id>");
					responseBuilder.append(hr.getId());
				responseBuilder.append("</hr_id>");
				responseBuilder.append("<hr_name>");
					responseBuilder.append(hr.getFirstName() );
				responseBuilder.append("</hr_name>");
				responseBuilder.append("<hr_lastname>");
				responseBuilder.append(hr.getLastName());
			responseBuilder.append("</hr_lastname>");
				responseBuilder.append("<hr_email>");
					responseBuilder.append(hr.getEmail());
				responseBuilder.append("</hr_email>");
				
				responseBuilder.append("<hr_directions>");
					for(Direction direction : list_of_hr_directions){
						responseBuilder.append("<direction_name>");
							responseBuilder.append(direction.getName());
						responseBuilder.append("</direction_name>");
					}
				responseBuilder.append("</hr_directions>");
			
			responseBuilder.append("</hr>");
		}
		
		
	}

}
