package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Trainee;
import edu.epam.service.InterviewResultService;
import edu.epam.service.InterviewService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 22/06/15.
 */
@UserPermissions(RoleType.HR)
public class InterviewResultsCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {

        int interviewId = Integer.parseInt(request.getParameter("interview_id"));
        int directionId = InterviewService.getInterviewById(interviewId).getDirectionId();
        List<Trainee> trainees = TraineeService.getTraineesByInterviewId(interviewId);

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <trainees>");

        for(Trainee trainee : trainees) {
            int result = 0;
            if(InterviewResultService.getInterviewResultByUserIdAndInterviewId(trainee.getId(), interviewId) != null) {
                result = InterviewResultService.getInterviewResultByUserIdAndInterviewId(trainee.getId(), interviewId).getRating();
            }
            responseBuilder.append("<trainee>");
            responseBuilder.append("<id>" + trainee.getId() + "</id>");
            responseBuilder.append("<firstname>" + trainee.getFirstName() + "</firstname>");
            responseBuilder.append("<lastname>" + trainee.getLastName() + "</lastname>");
            responseBuilder.append("<parentname>" + trainee.getParentName() + "</parentname>");
            responseBuilder.append("<result>" + result + "</result>");
            responseBuilder.append("</trainee>");
        }

        responseBuilder.append("</trainees>");
        System.out.println(responseBuilder.toString());

        return responseBuilder.toString();
    }
}
