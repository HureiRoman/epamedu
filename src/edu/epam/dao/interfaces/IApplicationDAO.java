package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Application;

/**
 * Created by fastforward on 20/06/15.
 */
public interface IApplicationDAO {
    List<Application> getAllApplications() throws SQLException;
    Application getApplicationById(int id) throws SQLException;
    Application getApplicationByTraineeIdAndInterviewId(int traineeId, int interviewId) throws SQLException;
	void insertApplication(Application application) throws SQLException;
    void deleteApplicationsByInterviewId(int interviewId) throws SQLException;
    public List<String> getEmailsOfFollowers(int interviewId) throws SQLException;
}
