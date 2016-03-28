package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
import edu.epam.service.PersonalMessageService;

@UserPermissions({RoleType.TRAINEE, RoleType.HR,RoleType.GRADUATE})
public class GetUreadMessCountCommand implements AjaxActionCommand{

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");   
		int result = 0;
					
		
		String isAllUnreadMEssages = request.getParameter("allUnread");	
		CommonUser logined_user = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		 int totalUnreadMessCount = logined_user.getNewMessages();
		
		responseBuilder.append("<unread_mes_count>");
		if(isAllUnreadMEssages != null && isAllUnreadMEssages.equalsIgnoreCase("true")){
			int count = PersonalMessageService.getAllUnreadMessageCount(logined_user.getId());
			Boolean playSound = false;
			if(count > totalUnreadMessCount){
				playSound = true;
			}
			result = 1; // STATUS : ALL OK!
			logined_user.setNewMessages(count);
			request.getSession().setAttribute(Constants.SESSION_PARAM_NAME_USER, logined_user);
			responseBuilder.append("<isPlaySound>");
				responseBuilder.append(playSound);
			responseBuilder.append("</isPlaySound>");
			responseBuilder.append("<amount_of_messages>");
				responseBuilder.append(count);
			responseBuilder.append("</amount_of_messages>");
		}else{
			String[] id_list = request.getParameterValues("id_list[]");
			for(String s : id_list){
				System.out.println("id" + s);
			}
			System.out.println("THIS COMMAND!!!!");
			Map<Integer, Integer> map_result_HR_COUNT =  CommonUserService.getUnreadMessageCount(id_list, logined_user.getId());
			
			if(map_result_HR_COUNT != null){
				result = 1; // STATUS : ALL OK!
				for(Entry<Integer, Integer> entry : map_result_HR_COUNT.entrySet()){
					responseBuilder.append("<info>");
						responseBuilder.append("<id>");
							responseBuilder.append(entry.getKey());
						responseBuilder.append("</id>");
						responseBuilder.append("<count_mess>");
							responseBuilder.append(entry.getValue());
						responseBuilder.append("</count_mess>");
					responseBuilder.append("</info>");
				}
				
			}else{
				result = 2; // STATUS: Map is empty
			}
		}
		
		responseBuilder.append("<status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		responseBuilder.append("</unread_mes_count>");
		return responseBuilder.toString();
	}

}
