package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.persistance.EmailMessenger;
import edu.epam.service.CommonUserService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class AddNewEmployeeCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		int result = 0;
		String email = request.getParameter("email");
		if (CommonUserService.isEmailExist(email)) {
            result = 2;
        }
		else{
			RoleType roleType = RoleType.valueOf(request.getParameter("role"));
			String uuid = UUID.randomUUID().toString();
			boolean employeeAddded = CommonUserService.addEmployee(email,roleType,uuid); 
			if(employeeAddded){
				sendMail(request, email, uuid);
			}
			result = employeeAddded ? 1 : 0;
			
		}
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");

		return responseBuilder.toString();
	}
	
	private boolean sendMail(HttpServletRequest request, String recipient, String uuid) {
		ServletContext context = request.getSession().getServletContext();
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");
		String subject = "Welcome to  epam.edu";
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		String ip = inetAddress.getHostAddress();
		String content = "<h5>You are invited to Epam.edu system</h5><a href=\"http://" + ip + ":8080/EpamEducationalProject/Controller?command=employeeRegconfirm&key=" + uuid
				+ "&user=" + recipient + "\">Go to registration</a>";
		try {
			EmailMessenger.sendEmail(host, port, user, pass, recipient, subject, content);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
