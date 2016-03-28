package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.InterviewResult;
import edu.epam.service.ApplicationService;
import edu.epam.service.InterviewResultService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

/**
 * Created by fastforward on 23/06/15.
 */
@UserPermissions({RoleType.HR})
public class SetTraineeInterviewResultsCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 0;

        String ratingString = request.getParameter("value");
        int userId = Integer.parseInt(request.getParameter("id"));
        int interviewId = Integer.parseInt(request.getParameter("interview_id"));
        int applicationId = ApplicationService.getApplicationByTraineeIdAndInterviewId(userId, interviewId).getId();

        if (ratingString != "") {
            int rating = Integer.parseInt(ratingString);
            InterviewResult interviewResult = new InterviewResult();
            interviewResult.setRating(rating);
            interviewResult.setApplicationId(applicationId);

            if(InterviewResultService.isInterviewResultPresent(userId, interviewId)){
                interviewResult = InterviewResultService.getInterviewResultByUserIdAndInterviewId(userId, interviewId);
                interviewResult.setRating(rating);
                InterviewResultService.updateInterviewResult(interviewResult);
            } else {
                InterviewResultService.insertInterviewResult(interviewResult);
            }
            return "" + rating;
        }
        return "0";
    }
}

/*String results[] = request.getParameterValues("results[]");
        List<InterviewResult> interviewResults = new ArrayList<>();

        Pattern patternId = Pattern.compile("<id>(.+?)</id>");
        Pattern patternResult = Pattern.compile("<result>(.+?)</result>");

        for(String str : results) {
            InterviewResult interviewResult = new InterviewResult();
            Matcher matcher = patternId.matcher(str);
            matcher.find();
            interviewResult.setTraineeId(Integer.parseInt(matcher.group(1)));
            matcher = patternResult.matcher(str);
            matcher.find();
            interviewResult.setRating(Integer.parseInt(matcher.group(1)));
            System.out.println("interview result: id " + interviewResult.getTraineeId() + " rating " + interviewResult.getRating());
        }*/