package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Application;



public class ApplicationService {
    public static List<Application> getAllApplications() throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getApplicationDAO().getAllApplications();
    }

    public static Application getApplicationByTraineeIdAndInterviewId(int traineeId, int interviewId) throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getApplicationDAO().getApplicationByTraineeIdAndInterviewId(traineeId, interviewId);
    }

    public static void deleteApplicationsByInterviewId(int interviewId) throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getApplicationDAO().deleteApplicationsByInterviewId(interviewId);
    }
    public static List<String> getEmailsOfFollowers(int interviewId) throws SQLException {
		 return  AbstractDAOFactory.getFactory(EFactoryType.MySQL).getApplicationDAO().getEmailsOfFollowers(interviewId);
	}
}
