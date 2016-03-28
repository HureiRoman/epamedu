package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Group;
import edu.epam.model.Interview;
import edu.epam.service.GroupService;
import edu.epam.service.InterviewService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 26/06/15.
 */
@UserPermissions({ RoleType.HR})
public class GetGroupsByInterviewCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        String interviewIdString = request.getParameter("interview_id");
        int interviewId = Integer.parseInt(interviewIdString);
        Interview interview = InterviewService.getInterviewById(interviewId);
        System.out.println(interview.getDirectionId());
        List<Group> groups = GroupService.getListOfGroupsForDirection(interview.getDirectionId());

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <groups>");
        for (Group group : groups) {
            System.out.println(group.getTitle());
//saniok code added
            if (group.getDate_of_graduation()== null || group.getDate_of_graduation().after(new Date())) {
                responseBuilder.append("<group>");
                responseBuilder.append("<id>");
                responseBuilder.append(group.getId());
                responseBuilder.append("</id>");
                responseBuilder.append("<title>");
                responseBuilder.append(group.getTitle());
                responseBuilder.append("</title>");
                responseBuilder.append("</group>");
            }
        }

        responseBuilder.append("</groups>");
        System.out.println(responseBuilder.toString());

        return responseBuilder.toString();
    }
}
