package edu.epam.servlet.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.role.HR;
import edu.epam.service.DirectionService;
import edu.epam.service.InterviewService;

@UserPermissions({ RoleType.HR})
public class ShowInterviewResultsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        Map<Direction, List<Interview>> directionMap = new HashMap<>();
        HR hr = (HR) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
        List<Direction> directions = DirectionService.getDirectionsByHRId(hr.getId());
        List<Interview> interviews = InterviewService.getPastInterviews();
        for(Direction direction : directions) {
            System.out.println(direction.getName());
            List<Interview> interviewsByDirection = new ArrayList<>();
            for(Interview interview : interviews) {
                if(interview.getDirectionId() == direction.getId()) {
                    interviewsByDirection.add(interview);
                }
            }

            directionMap.put(direction, interviewsByDirection);
        }
        request.setAttribute("directions", directionMap);
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.HR_INTERVIEW_RESULTS_PAGE);
    }
}
