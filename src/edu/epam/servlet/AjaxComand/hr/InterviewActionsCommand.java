package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import edu.epam.filter.EncodingFilter;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.persistance.EmailMessenger;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.DirectionService;
import edu.epam.service.InterviewService;
import edu.epam.service.PersonalMessageService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 19/06/15.
 */
@UserPermissions({ RoleType.HR})
public class InterviewActionsCommand implements AjaxActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 1;

        HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);

        String action = request.getParameter("action");
        if(action.equals("add")) {

            String place = request.getParameter("place");
            String description = request.getParameter("description");
            String directionIdString = request.getParameter("direction");
            String dateString = request.getParameter("date");
            SimpleDateFormat pattern = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
            java.util.Date date = null;

            if (place != null && description != null && directionIdString != null && dateString != null
                    && place.length() < 250 && description.length() < 250) {
                try {
                    date = pattern.parse(dateString);
                    int directionId = Integer.parseInt(directionIdString);
                    Interview interview = new Interview();
                    interview.setDirectionId(directionId);
                    interview.setDateOfTesting(date);
                    interview.setPlace(place);
                    interview.setDescription(description);

                    List<Trainee> trainees = TraineeService.getTraineesSubscribedToDirection(directionId);

                    String messageText = "New interview added! Date " + dateString + ", Place " + place + " " +
                            ", Description " + description  + "!";
                    String subject = "Added new Interview";


                    InterviewService.insertInterview(interview);

                    PersonalMessageService.sendMessageToNewsSubscribersByDirectionId(hr.getId(), directionId, messageText);

                    sendMail(request, trainees, subject, messageText);
                    sendSms(trainees, messageText);

                } catch (Exception e) {
                    e.printStackTrace();
                    result = 2;//bad value
                }
            } else {
                result = 2;
            }
        } else if(action.equals("edit")) {
            String place = request.getParameter("place");
            String description = request.getParameter("description");
            String directionIdString = request.getParameter("direction");
            String dateString = request.getParameter("date");
            String interviewIdString = request.getParameter("interviewId");
            SimpleDateFormat pattern = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
            java.util.Date date = null;

            if (place != null && description != null && directionIdString != null && dateString != null
                    && place.length() < 250 && description.length() < 250) {
                try {
                    date = pattern.parse(dateString);
                    int directionId = Integer.parseInt(directionIdString.substring(2));
                    int interviewId = Integer.parseInt(interviewIdString);
                    Interview interview = new Interview();
                    interview.setId(interviewId);
                    interview.setDirectionId(directionId);
                    interview.setDateOfTesting(date);
                    interview.setPlace(place);
                    interview.setDescription(description);

                    List<Trainee> trainees = TraineeService.getTraineesSubscribedToDirection(directionId);
                    List<Trainee> interviewFollowers = TraineeService.getTraineesByInterviewId(interviewId);

                    InterviewService.updateInterview(interview);
                    Direction direction = DirectionService.getDirectionById(interview.getDirectionId());

                    String messageText = "Interview on " + direction.getName() + " was changed! " +
                            "New date " + interview.getDateOfTesting() + ", new place " +
                            interview.getPlace() + ", new description " + interview.getDescription();
                    String subject = "Interview Edited";

                    PersonalMessageService.sendMessegeToInterviewFollowers(hr.getId(), interviewId, messageText);
                    PersonalMessageService.sendMessageToNewsSubscribersByDirectionId(hr.getId(), directionId, messageText);

                    trainees.removeAll(interviewFollowers);

                    sendMail(request, interviewFollowers, subject, messageText);
                    sendMail(request, trainees, subject, messageText);
                    sendSms(interviewFollowers, messageText);
                    sendSms(trainees, messageText);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = 2;//bad value
                }
            } else {
                result = 2;
            }           
         } else if(action.equals("delete")) {
            String interviewIdString = request.getParameter("interviewId");
            if(interviewIdString != null) {
                int interviewId = Integer.parseInt(interviewIdString);
                Interview interview = InterviewService.getInterviewById(interviewId);
                InterviewService.deleteInterview(interviewId);
                Direction direction = DirectionService.getDirectionById(interview.getDirectionId());
                String messageText = "Interview on " + direction.getName() + " " + interview.getDateOfTesting() + " was deleted;";
                String subject = "Interview Deleted";

                List<Trainee> trainees = TraineeService.getTraineesSubscribedToDirection(direction.getId());
                List<Trainee> interviewFollowers = TraineeService.getTraineesByInterviewId(interviewId);

                PersonalMessageService.sendMessegeToInterviewFollowers(hr.getId(), interviewId, messageText);
                PersonalMessageService.sendMessageToNewsSubscribersByDirectionId(hr.getId(), direction.getId(), messageText);

                trainees.removeAll(interviewFollowers);

                sendMail(request, interviewFollowers, subject, messageText);
                sendMail(request, trainees, subject, messageText);

                sendSms(interviewFollowers, messageText);
                sendSms(trainees, messageText);
            }
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


    private boolean sendMail(HttpServletRequest request, List<? extends CommonUser> toUsers, String subject, String content) {
        ServletContext context = request.getSession().getServletContext();
        String host = context.getInitParameter("host");
        String port = context.getInitParameter("port");
        String user = context.getInitParameter("user");
        String pass = context.getInitParameter("pass");

        try {
            EmailMessenger.sendEmailToGroupOfUsers(host, port, user, pass, toUsers, subject, content);

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendSms(List<Trainee> trainees, String message) {
        String msg;
        if(message.length() > 160) {
            msg = message.substring(0, 160);
        } else {
            msg = message;
        }
        for(Trainee trainee : trainees) {
            String phone = "38" +  trainee.getCv().getPhone().replaceAll("[()-]", "");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EncodingFilter.sendMessage(phone, msg);
                }
            });
            thread.start();
        }
    }
}
