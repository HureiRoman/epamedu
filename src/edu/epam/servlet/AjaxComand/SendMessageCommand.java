package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Message;
import edu.epam.persistance.EmailMessenger;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
//Відправка приватного повідомлення
@UserPermissions({RoleType.TRAINEE,RoleType.STUDENT,RoleType.TEACHER,RoleType.HR,RoleType.GRADUATE})
public class SendMessageCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		String text = request.getParameter("text");
		
		int result = 0;
		String receiver_id = request.getParameter("receiver_id");
		System.out.println("length ======>>> " + text.trim().length());
		if(text != null && text.trim().length() > 0 && receiver_id != null && !receiver_id.equals("")){
			try{
				int receiverId = Integer.valueOf(receiver_id);
				CommonUser logined_user = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
				int logined_user_id = logined_user.getId();
				Message message = new Message(0,logined_user_id,receiverId,text,false,new Timestamp(Calendar.getInstance().getTimeInMillis()));
				boolean result_of_send = CommonUserService.sendMessage(message);
				
				 String sentToEmail=request.getParameter("sendToEmail");
				if(sentToEmail!= null && sentToEmail.equals("true")) {
					String email=CommonUserService.getUserById(receiverId).getEmail();
					String messageText=request.getParameter("text");
					String subject=request.getParameter("subject");
					if(subject.equals("")) {
						subject = "Epam.edu";
					}
					sendMail(request,email,subject,messageText);
				}
				
				System.out.println("TEXT = " + text + " HR_ID = " + receiverId);
				System.out.println("result of SENDING MESSAGE = " + result_of_send);
				if(result_of_send == true){
					result = 1; // OK status
				}else{
					result = 2; // didnot add to DB
				}
			}catch(Exception e){
				e.printStackTrace();
				result = 2; // TotalError - Message didnot send
			}
		}	else{
			result = 3; // Input text is empty
		}
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}
	private boolean sendMail(HttpServletRequest request, String recipient,String subject,String content) {
		ServletContext context = request.getSession().getServletContext();
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");
			try {
			EmailMessenger.sendEmail(host, port, user, pass, recipient, subject, content);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
