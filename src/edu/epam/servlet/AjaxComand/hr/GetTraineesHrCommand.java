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
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 04/07/15.
 */
@UserPermissions({ RoleType.HR})
public class GetTraineesHrCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        String directionIdString = request.getParameter("direction_id");

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <trainees>");

        if(directionIdString != null) {
            try {

                int directionId = Integer.parseInt(directionIdString.substring(5));

                List<Trainee> trainees = TraineeService.getTraineesByApplicationDirectionId(directionId);
                for(Trainee trainee : trainees) {
                    responseBuilder.append("<trainee>");
                    responseBuilder.append("<id>" + trainee.getId() + "</id>");
                    responseBuilder.append("<fname>" + trainee.getFirstName() + "</fname>");
                    responseBuilder.append("<lname>" + trainee.getLastName() + "</lname>");
                    responseBuilder.append("<pname>" + trainee.getParentName() + "</pname>");
                    responseBuilder.append("<email>" + trainee.getEmail() + "</email>");
                    responseBuilder.append("<phone>" + trainee.getCv().getPhone() + "</phone>");
                    responseBuilder.append("<additional-info>" + trainee.getCv().getAdditionalInfo() + "</additional-info>");
                    responseBuilder.append("<education>" + trainee.getCv().getEducation() + "</education>");
                    responseBuilder.append("<english-level>" + trainee.getCv().getEnglishLevel().toString() + "</english-level>");
                    responseBuilder.append("<skills>" + trainee.getCv().getSkills() + "</skills>");
                    responseBuilder.append("</trainee>");
                }
                responseBuilder.append("</trainees>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(responseBuilder.toString());
        return responseBuilder.toString();
    }
}
