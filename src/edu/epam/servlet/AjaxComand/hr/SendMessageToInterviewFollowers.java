package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.persistance.EmailMessenger;
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.PersonalMessageService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
/**
 * Created by fastforward on 25/06/15.
 */
@UserPermissions(RoleType.HR)
public class SendMessageToInterviewFollowers implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 1;
        String interviewIdString = request.getParameter("interviewId");
        String messageText = request.getParameter("messageText");
        String sentToEmail=request.getParameter("sendToEmail");
        String subject=request.getParameter("subject");

        HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);

        if (interviewIdString != null && messageText != null && messageText.length() < 250) {
            try {
                int interviewId = Integer.parseInt(interviewIdString);

                PersonalMessageService.sendMessegeToInterviewFollowers(hr.getId(), interviewId, messageText);

                if (sentToEmail.equals("true")) {
                    //List<String> followers = ApplicationService.getEmailsOfFollowers(interviewId);
                    List<Trainee> followers = TraineeService.getTraineesByInterviewId(interviewId);
                    System.out.println(followers);
                    ServletContext context = request.getSession().getServletContext();
                    String host = context.getInitParameter("host");
                    String port = context.getInitParameter("port");
                    String user = context.getInitParameter("user");
                    String pass = context.getInitParameter("pass");
                    if (subject.equals("")) {
                        subject = "Epam.edu";
                    }
                    try {
                        EmailMessenger.sendEmailToGroupOfUsers(host, port, user, pass, followers, subject, messageText);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                } 
             } catch (Exception e) {
                e.printStackTrace();
                result = 2;
            }
        } else {
            result = 2;
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
}
