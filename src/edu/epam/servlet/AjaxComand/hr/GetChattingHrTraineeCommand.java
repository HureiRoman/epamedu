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
public class GetChattingHrTraineeCommand implements AjaxActionCommand {
	
	
	private int logined_user_id;
	private String logined_user_name;
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		System.out.println("ENTER GET CHATTING HR _ TRAINEE");
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");   
		int result = 0;
		
		HR logined_user = (HR) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
		logined_user_id = logined_user.getId();
		logined_user_name = logined_user.getFirstName() + " " + logined_user.getLastName();
		
		String traineeId = request.getParameter("trainee_id");
		System.out.println("trainee id = " + traineeId);
		if(traineeId == null){
			// No TRAINEE in databsase
			System.out.println("No STUDENT in database");
			result = 3; // STATUS : no TRAINEE in Database
			System.out.println("status = " + result);
			responseBuilder.append("<status>");
				responseBuilder.append(result);
			responseBuilder.append("</status>");
			return responseBuilder.toString();
		}else{
			int idTrainee = Integer.valueOf(traineeId);
			List<Message> listOfMessages = TraineeService.getChattingWithHR(logined_user_id,idTrainee);
			System.out.println("lisT LENGTH = !!!!! " + listOfMessages.size());
				
			if(listOfMessages !=null && listOfMessages.size()>0){
				Trainee traineeObj = TraineeService.getTraineeById(idTrainee);
				System.out.println(" !!!!!!!!!" +listOfMessages);
				responseBuilder.append("<messages>");
					getMessageInfo(responseBuilder, listOfMessages, traineeObj, locale);
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
				responseBuilder.append("<trainee_id>");
				responseBuilder.append(idTrainee);
				responseBuilder.append("</trainee_id>");
				responseBuilder.append("<last_mess_id>");
				responseBuilder.append(0);
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
				return o1.getMessageTime().compareTo(o2.getMessageTime());
			}
		});
		
		responseBuilder.append("<logined_user_id>");
		responseBuilder.append(logined_user_id);
		responseBuilder.append("</logined_user_id>");
		responseBuilder.append("<trainee_id>");
		responseBuilder.append(traineeObj.getId());
		responseBuilder.append("</trainee_id>");
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
				responseBuilder.append("<trainee_name>");
				responseBuilder.append(traineeObj.getFirstName() + " " + traineeObj.getLastName());
				responseBuilder.append("</trainee_name>");
				responseBuilder.append("<user_name>");
				responseBuilder.append(logined_user_name);
				responseBuilder.append("</user_name>");
			responseBuilder.append("</message>");
			
		}
		
	}

}
