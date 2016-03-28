package edu.epam.servlet.AjaxComand;

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

@UserPermissions({RoleType.GUEST})
public class PasswordRetrievalCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String uuid = UUID.randomUUID().toString();
		String email=request.getParameter("email");
		System.out.println("email+"+email+" uuid "+uuid);
		
		String successed=null;
		if(CommonUserService.getIdByEmail(email)!=null)
		{
			if(sendMail(request, email, uuid))
			{
				CommonUserService.insertDataForPasswordRetrieval(uuid, email);
			successed="successfully sended";
			}
			else successed ="sending was failed";
		}
		else successed="This address does not register";

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(successed);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}
	private boolean sendMail(HttpServletRequest request, String recipient, String uuid) {
		ServletContext context = request.getSession().getServletContext();
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");
		String subject = "Retrieval password on epam.edu";
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		String ip = inetAddress.getHostAddress();
		String content = "<a href=\"http://"+ip+":8080/EpamEducationalProject/Controller?command=setNewPassword&key=" + uuid
				+ "&user=" + recipient + "\">Confirm</a>";
		try {
			EmailMessenger.sendEmail(host, port, user, pass, recipient, subject, content);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
