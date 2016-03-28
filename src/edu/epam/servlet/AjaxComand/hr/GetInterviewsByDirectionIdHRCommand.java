package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Interview;
import edu.epam.role.Trainee;
import edu.epam.service.InterviewService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 02/07/15.
 */
@UserPermissions({ RoleType.HR})
public class GetInterviewsByDirectionIdHRCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        String directionIdString = request.getParameter("direction_id");


        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <interviews>");

        if(directionIdString != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                int directionId = Integer.parseInt(directionIdString.substring(5));

                List<Interview> interviews = InterviewService.getFutureInterviewsByDirectionId(directionId);

                for(Interview interview : interviews) {
                    responseBuilder.append("<interview>");
                    responseBuilder.append("<id>" + interview.getId() + "</id>");
                    responseBuilder.append("<date>" + simpleDateFormat.format(interview.getDateOfTesting()) + "</date>");
                    responseBuilder.append("<place>" + interview.getPlace() + "</place>");
                    responseBuilder.append("<description>" + interview.getDescription() + "</description>");
                    List<Trainee> listOfSubscribers = TraineeService.getTraineesByInterviewId(interview.getId());
                    int count = 0;
                    if(listOfSubscribers != null && listOfSubscribers.size()>0){
                    	count = listOfSubscribers.size();
                    }
                    responseBuilder.append("<subscribersCount>" + count + "</subscribersCount>");
                    responseBuilder.append("</interview>");
                }
                responseBuilder.append("</interviews>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(responseBuilder.toString());
        return responseBuilder.toString();
    }
}
