package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IInterviewResultDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.InterviewResult;

/**
 * Created by fastforward on 20/06/15.
 */
public class InterviewResultDAO implements IInterviewResultDAO {


    private static final String SQL_GET_ALL_INTERVIEW_RESULTS = "SELECT * FROM interview_results";
    private static final String SQL_INSERT_INTERVIEW_RESULT = "INSERT INTO interview_results (rating, application_id) VALUES (?, ?)";
    private static final String SQL_UPDATE_INTERVIEW_RESULT = "UPDATE interview_results SET rating = ? WHERE application_id = ?";
    private static final String SQL_GET_INTERVIEW_RESULT_BY_USER_ID_AND_INTERVIEW_ID = "SELECT I.id, application_id, rating, interview_id FROM interview_results I JOIN applications A ON" +
            " I.application_id = A.id WHERE user_id = ? AND interview_id = ?";


    private static InterviewResultDAO instance;
    private InterviewResultDAO() {
    }

    public static InterviewResultDAO getInstance() {
        if (instance == null) {
            instance = new InterviewResultDAO();
        }
        return instance;
    }

    @Override
    public void insertInterviewResult(InterviewResult interviewResult) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT_INTERVIEW_RESULT);) {
            preparedStatement.setInt(1, interviewResult.getRating());
            preparedStatement.setInt(2, interviewResult.getApplicationId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public void updateInterviewResult(InterviewResult interviewResult) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE_INTERVIEW_RESULT);) {
            preparedStatement.setInt(1, interviewResult.getRating());
            preparedStatement.setInt(2, interviewResult.getApplicationId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public List<InterviewResult> getAllInterviewResults() throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<InterviewResult> interviewResults = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_ALL_INTERVIEW_RESULTS);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                rs.previous();
                InterviewResult interview = Transformer.getInstance(rs, InterviewResult.class, InterviewResult.class);
                interviewResults.add(interview);
            }
            return interviewResults;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }

    @Override
    public InterviewResult getInterviewResultByUserIdAndInterviewId(int userId, int interviewId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        InterviewResult interviewResult = null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_INTERVIEW_RESULT_BY_USER_ID_AND_INTERVIEW_ID);) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, interviewId);
            ResultSet resultSet = preparedStatement.executeQuery();
            interviewResult = Transformer.getInstance(resultSet, InterviewResult.class, InterviewResult.class);
            return interviewResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
    }
    @Override
    public boolean isInterviewResultPresent(int userId, int interviewId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        InterviewResult interviewResult = null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_INTERVIEW_RESULT_BY_USER_ID_AND_INTERVIEW_ID);) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, interviewId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
        return false;
    }
}
