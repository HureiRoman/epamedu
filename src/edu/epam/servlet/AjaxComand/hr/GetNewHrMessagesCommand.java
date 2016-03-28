package edu.epam.servlet.AjaxComand.hr;

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
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.HR})
public class GetNewHrMessagesCommand implements AjaxActionCommand {

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
		
		HR logined_user = (HR) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		logined_user_id = logined_user.getId();
		logined_user_name = logined_user.getFirstName() + " " + logined_user.getLastName();
		
		if(request.getParameter("last_message_id") != null){
			Integer last_message_id = Integer.valueOf(request.getParameter("last_message_id"));
			System.out.println("last_message_id = " + last_message_id);
			Integer idTrainee = Integer.valueOf(request.getParameter("trainee_id"));
			System.out.println("hr_id = " + idTrainee);
			
			
			List<Message> listOfMessages = TraineeService.getNewMessagesTraineeHR(last_message_id, logined_user_id, idTrainee);
			
			if(listOfMessages !=null && listOfMessages.size()>0){
				Trainee traineeObj = TraineeService.getTraineeById(idTrainee);
				System.out.println(listOfMessages);
				responseBuilder.append("<messages>");
					getMessageInfo(responseBuilder, listOfMessages, traineeObj, locale);
				responseBuilder.append("</messages>");
			}else{
				
				responseBuilder.append("<messages>");
				responseBuilder.append("<status>");
				responseBuilder.append(2); //STATUS :  list empty
				responseBuilder.append("</status>");
				responseBuilder.append("<logined_user_id>");
				responseBuilder.append(logined_user_id);
				responseBuilder.append("</logined_user_id>");
				responseBuilder.append("<trainee_id>");
				responseBuilder.append(idTrainee);
				responseBuilder.append("</trainee_id>");
				responseBuilder.append("<last_mess_id>");
				responseBuilder.append(last_message_id);
				responseBuilder.append("</last_mess_id>");
				responseBuilder.append("</messages>");
			}
		
		
		
		}
		return responseBuilder.toString();
	}
	
	private void getMessageInfo(StringBuilder responseBuilder, List<Message> list_of_messages,Trainee traineeObj, Locale locale ){
		int last_massage_id = list_of_messages.get(list_of_messages.size()-1).getId();
		list_of_messages.sort(new Comparator<Message>() {

			@Override
			public int compare(Message o1, Message o2) {
				return o2.getMessageTime().compareTo(o1.getMessageTime());
			}
		});
		
		responseBuilder.append("<logined_user_id>");
		responseBuilder.append(logined_user_id);
		responseBuilder.append("</logined_user_id>");
		responseBuilder.append("<hr_id>");
		responseBuilder.append(traineeObj.getId());
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
				responseBuilder.append(traineeObj.getFirstName() + " " + traineeObj.getLastName());
				responseBuilder.append("</hr_name>");
				responseBuilder.append("<user_name>");
				responseBuilder.append(logined_user_name);
				responseBuilder.append("</user_name>");
			responseBuilder.append("</message>");
			
			
		}
		
	}

}
