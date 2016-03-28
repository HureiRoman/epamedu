package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.InterviewResult;

/**
 * Created by fastforward on 20/06/15.
 */
public interface IInterviewResultDAO {
    void insertInterviewResult(InterviewResult interview) throws SQLException;
    List<InterviewResult> getAllInterviewResults() throws SQLException;
    void updateInterviewResult(InterviewResult interviewResult) throws SQLException;
    InterviewResult getInterviewResultByUserIdAndInterviewId(int userId, int interviewId) throws SQLException;
    boolean isInterviewResultPresent(int traineeId, int interviewId) throws SQLException;

}
