package edu.epam.servlet.AjaxComand.trainee;

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
import edu.epam.model.Message;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;
import edu.epam.service.HrService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TRAINEE, RoleType.GRADUATE})
public class GetChattingTraineeHR implements AjaxActionCommand {
	
//	private final String MESSAGES_OBJECTS = "messages";
	private final String HR_OBJECT = "hr_obj";
	private int logined_user_id;
	private String logined_user_name;
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		
		
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");   
		int result = 0;
		
		CommonUser logined_user = (CommonUser) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		logined_user_id = logined_user.getId();
		logined_user_name = logined_user.getFirstName() + " " + logined_user.getLastName();
		String hrId = request.getParameter("hr_id");
		System.out.println("hrId = " + hrId);
		int hr_id = 0;
		HR hr_obj = null;
		
		if(hrId == null){
			hr_obj = HrService.getRandomHR();
			System.out.println("get random HR");
			if(hr_obj != null){
				hr_id = hr_obj.getId();
				System.out.println(" HR not null");
			}else{
				// No HR in databsase
				System.out.println("No HR in database");
				result = 3; // STATUS : no HR in Database
				System.out.println("status = " + result);
				responseBuilder.append("<status>");
					responseBuilder.append(result);
				responseBuilder.append("</status>");
				return responseBuilder.toString();
			}
		}else{
			System.out.println(" HR not equals null");
			hr_id = Integer.valueOf(hrId);
			hr_obj = HrService.getHRbyId(hr_id);
		}
		
		List<Message> listOfMessages = TraineeService.getChattingWithHR(logined_user_id,hr_id);
		request.setAttribute(HR_OBJECT, hr_obj);
		
		if(listOfMessages !=null && listOfMessages.size()>0){
			System.out.println(listOfMessages);
//			request.setAttribute(MESSAGES_OBJECTS, list_of_messages);
//			request.setAttribute(HR_OBJECT, hr_obj);
			responseBuilder.append("<messages>");
				getMessageInfo(responseBuilder, listOfMessages, hr_obj, locale);
			responseBuilder.append("</messages>");
		}else{
			result = 2; // STATUS : list is empty
			responseBuilder.append("<messages>");
			responseBuilder.append("<status>");
			System.out.println("status = " + result);
			responseBuilder.append(result);
			responseBuilder.append("</status>");
			responseBuilder.append("<logined_user_id>");
			responseBuilder.append(logined_user_id);
			responseBuilder.append("</logined_user_id>");
			responseBuilder.append("<hr_id>");
			responseBuilder.append(hr_id);
			responseBuilder.append("</hr_id>");
			responseBuilder.append("<last_mess_id>");
			responseBuilder.append(0);
			responseBuilder.append("</last_mess_id>");
			responseBuilder.append("</messages>");
		}
		
		return responseBuilder.toString();
	}
	
	
	private void getMessageInfo(StringBuilder responseBuilder, List<Message> list_of_messages,HR hr_obj, Locale locale ){
		int last_massage_id = list_of_messages.get(list_of_messages.size()-1).getId();
		list_of_messages.sort(new Comparator<Message>() {

			@Override
			public int compare(Message o1, Message o2) {
				return o1.getMessageTime().compareTo(o2.getMessageTime());
			}
		});
		
		responseBuilder.append("<logined_user_id>");
		responseBuilder.append(logined_user_id);
		responseBuilder.append("</logined_user_id>");
		responseBuilder.append("<hr_id>");
		responseBuilder.append(hr_obj.getId());
		responseBuilder.append("</hr_id>");
		responseBuilder.append("<last_mess_id>");
		responseBuilder.append(last_massage_id);
		responseBuilder.append("</last_mess_id>");
		
		
		for(Message message :list_of_messages ){
			responseBuilder.append("<message>");
				responseBuilder.append("<sender>");
				responseBuilder.append(message.getSender());
				responseBuilder.append("</sender>");
				responseBuilder.append("<message_id>");
				responseBuilder.append(message.getId());
				responseBuilder.append("</message_id>");
				responseBuilder.append("<text>");
				responseBuilder.append(message.getText());
				responseBuilder.append("</text>");
				responseBuilder.append("<isRead>");
				responseBuilder.append(message.getIsRead());
				responseBuilder.append("</isRead>");
				responseBuilder.append("<time>");
				SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy : k:mm",locale);
				String formattedDate = formatter.format(message.getMessageTime());
				responseBuilder.append(formattedDate);
				responseBuilder.append("</time>");
				responseBuilder.append("<hr_name>");
				responseBuilder.append(hr_obj.getFirstName() + " " + hr_obj.getLastName());
				responseBuilder.append("</hr_name>");
				responseBuilder.append("<user_name>");
				responseBuilder.append(logined_user_name);
				responseBuilder.append("</user_name>");
			responseBuilder.append("</message>");
			
			
		}
		
	}

}
