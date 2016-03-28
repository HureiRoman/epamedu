package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Interview;

/**
 * Created by fastforward on 19/06/15.
 */
public interface IInterviewDAO {
    void insertInterview(Interview interview) throws SQLException;
    List<Interview> getAllInterviews() throws SQLException;
    Interview getInterviewById(int id) throws SQLException;
    void updateInterview(Interview interview) throws SQLException;
    void deleteInterview(int interviewId) throws SQLException;
    List<Interview> getPastInterviews() throws SQLException;
    List<Interview> getFutureInterviews() throws SQLException;
    List<Interview> getFutureInterviewsByDirectionId(int directionId) throws SQLException;
}
