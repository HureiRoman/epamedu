package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IInterviewDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Interview;

/**
 * Created by fastforward on 19/06/15.
 */
public class InterviewDAO implements IInterviewDAO {

    private static final String SQL_INSERT_INTERVIEW = "INSERT INTO interviews (direction_id, date_of_testing, place, description_interview) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_ALL_INTERVIEWS = "SELECT * FROM interviews WHERE isActive = 'true'";
    private static final String SQL_UPDATE_INTERVIEW = "UPDATE interviews SET direction_id = ?, date_of_testing = ?, place = ?, description_interview = ? WHERE interview_id = ?";
    private static final String SQL_DELETE_INTERVIEW = "UPDATE interviews SET isActive = 'false' WHERE interview_id = ?";
    private static final String SQL_GET_FUTURE_INTERVIEWS = "SELECT * FROM interviews WHERE isActive = 'true' AND date_of_testing > NOW() AND direction_id IN (SELECT id FROM directions WHERE is_active='true') ";
    private static final String SQL_GET_PAST_INTERVIEWS = "SELECT * FROM interviews WHERE isActive = 'true' AND date_of_testing < NOW() ";
    private static final String SQL_GET_INTERVIEW_BY_ID = "SELECT * FROM interviews WHERE interview_id = ?";
    private static final String SQL_GET_FUTURE_INTERVIEWS_BY_DIRECTION_ID = "SELECT * FROM interviews WHERE isActive = 'true' AND date_of_testing > NOW() AND direction_id = ?";
    private static InterviewDAO instance;
    private InterviewDAO() {
    }

    public static InterviewDAO getInstance() {
        if (instance == null) {
            instance = new InterviewDAO();
        }
        return instance;
    }

    @Override
    public void insertInterview(Interview interview) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try(PreparedStatement st = conn.prepareStatement(SQL_INSERT_INTERVIEW);){
            st.setInt(1, interview.getDirectionId());
            st.setTimestamp(2, new Timestamp(interview.getDateOfTesting().getTime()));
            st.setString(3, interview.getPlace());
            st.setString(4, interview.getDescription());
            st.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public void updateInterview(Interview interview) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try(PreparedStatement st = conn.prepareStatement(SQL_UPDATE_INTERVIEW);){
            st.setInt(1, interview.getDirectionId());
            st.setTimestamp(2, new Timestamp(interview.getDateOfTesting().getTime()));
            st.setString(3, interview.getPlace());
            st.setString(4, interview.getDescription());
            st.setInt(5, interview.getId());
            st.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public Interview getInterviewById(int id) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_INTERVIEW_BY_ID);){
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            Interview interview = Transformer.getInstance(rs, Interview.class, Interview.class);
            return interview;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
    }
    @Override
    public List<Interview> getAllInterviews() throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Interview> interviews = new ArrayList<>();

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_ALL_INTERVIEWS);
            while (rs.next()) {
                rs.previous();
                Interview interview = Transformer.getInstance(rs, Interview.class, Interview.class);
                interviews.add(interview);
            }
            return interviews;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }
    @Override
    public List<Interview> getFutureInterviews() throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Interview> interviews = new ArrayList<>();

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_FUTURE_INTERVIEWS);
            while (rs.next()) {
                rs.previous();
                Interview interview = Transformer.getInstance(rs, Interview.class, Interview.class);
                interviews.add(interview);
            }
            return interviews;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }

    @Override
    public List<Interview> getFutureInterviewsByDirectionId(int directionId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Interview> interviews = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_FUTURE_INTERVIEWS_BY_DIRECTION_ID)) {
            preparedStatement.setInt(1, directionId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                rs.previous();
                Interview interview = Transformer.getInstance(rs, Interview.class, Interview.class);
                interviews.add(interview);
            }
            return interviews;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }

    @Override
    public List<Interview> getPastInterviews() throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Interview> interviews = new ArrayList<>();

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_GET_PAST_INTERVIEWS);
            while (rs.next()) {
                rs.previous();
                Interview interview = Transformer.getInstance(rs, Interview.class, Interview.class);
                interviews.add(interview);
            }
            return interviews;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }
    @Override
    public void deleteInterview(int interviewId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE_INTERVIEW)) {
            preparedStatement.setInt(1, interviewId);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }

	
}
