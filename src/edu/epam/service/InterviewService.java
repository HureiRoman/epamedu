package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Interview;

/**
 * Created by fastforward on 19/06/15.
 */
public class InterviewService {
    public static List<Interview> getAllInterviews() throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().getAllInterviews();
    }

    public static List<Interview> getFutureInterviews() throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().getFutureInterviews();
    }

    public static List<Interview> getFutureInterviewsByDirectionId(int directionId) throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().getFutureInterviewsByDirectionId(directionId);
    }

    public static List<Interview> getPastInterviews() throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().getPastInterviews();
    }

    public static void insertInterview(Interview interview) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().insertInterview(interview);
    }

    public static void updateInterview(Interview interview) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().updateInterview(interview);
    }

    public static void deleteInterview(int interviewId) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().deleteInterview(interviewId);
    }

    public static Interview getInterviewById(int interviewId) throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getInterviewDAO().getInterviewById(interviewId);
    }


}
