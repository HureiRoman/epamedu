package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.InterviewResult;

/**
 * Created by fastforward on 20/06/15.
 */
public class InterviewResultService {
    public static List<InterviewResult> getAllInterviewResults() throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewResultDAO().getAllInterviewResults();
    }

    public static void insertInterviewResult(InterviewResult interviewResult) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewResultDAO().insertInterviewResult(interviewResult);
    }

    public static void updateInterviewResult(InterviewResult interviewResult) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewResultDAO().updateInterviewResult(interviewResult);
    }

    public static InterviewResult getInterviewResultByUserIdAndInterviewId(int userId, int interviewId) throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewResultDAO().getInterviewResultByUserIdAndInterviewId(userId, interviewId);
    }

    public static boolean isInterviewResultPresent(int traineeId, int interviewId) throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewResultDAO().isInterviewResultPresent(traineeId, interviewId);
    }
}
