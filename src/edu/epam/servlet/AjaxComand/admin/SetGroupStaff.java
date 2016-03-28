package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.GroupService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({ RoleType.ADMIN })
public class SetGroupStaff implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		Integer groupId = null;
		String groupIdStr = request.getParameter("group_id"); 
		if(groupIdStr!=null){
			groupId = Integer.parseInt(groupIdStr);
		}
		String[] teachersIdsArray = request.getParameterValues("teachers[]");
		
		ArrayList<Integer> teachersIds = new ArrayList<Integer>();
		
		if(teachersIdsArray!=null){
			for (String teacherIdStr : teachersIdsArray) {
				teachersIds.add(Integer.parseInt(teacherIdStr));
			}	
		}
		
		boolean updateGroupStaff = GroupService.updateGroupStaff(groupId, teachersIds);
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(updateGroupStaff?"1":"0");
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

}
