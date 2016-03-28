package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IApplicationDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Application;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;

public class ApplicationDAO implements IApplicationDAO {

    public static final String SQL_GET_ALL_APPLICATIONS = "SELECT * FROM applications";
    public static final String SQL_INSERT_APPLICATION = "INSERT INTO applications (user_id, direction_id, interview_id) VALUES (?, ?, ?)";
    public static final String SQL_GET_APPLICATION_BY_TRAINEE_ID_AND_INTERVIEW_ID = "SELECT * FROM applications WHERE user_id = ? AND interview_id = ?";
    public static final String SQL_DELETE_APPLICATIONS_BY_INTERVIEW_ID = "DELETE FROM applications WHERE interview_id = ?";
    private final static String SQL_GET_EMAILS_OF_FOLLOWERS="SELECT * FROM users INNER JOIN applications ON users.id=applications.user_id INNER JOIN trainee ON trainee.id=users.id WHERE applications.interview_id=?";
    
    private static ApplicationDAO instance;
    private ApplicationDAO() {
    }

    public static ApplicationDAO getInstance() {
        if (instance == null) {
            instance = new ApplicationDAO();
        }
        return instance;
    }

    @Override
    public void insertApplication(Application application) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT_APPLICATION);) {
            preparedStatement.setInt(1, application.getUserId());

     //       preparedStatement.setInt(2, application.getDirectionId());
      //      preparedStatement.setInt(2, application.getDirectionId());

            preparedStatement.setInt(3, application.getInterviewId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }


    @Override
    public List<Application> getAllApplications() throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Application> applications = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_ALL_APPLICATIONS);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                rs.previous();
                Application interview = Transformer.getInstance(rs, Application.class, Application.class);
                applications.add(interview);
            }
            return applications;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }

    @Override
    public Application getApplicationById(int id) throws SQLException {
        return null;
    }

    @Override
    public Application getApplicationByTraineeIdAndInterviewId(int traineeId, int interviewId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        Application application = null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_APPLICATION_BY_TRAINEE_ID_AND_INTERVIEW_ID);) {
            preparedStatement.setInt(1, traineeId);
            preparedStatement.setInt(2, interviewId);
            ResultSet rs = preparedStatement.executeQuery();
            application = Transformer.getInstance(rs, Application.class, Application.class);
            return application;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }
    @Override
    public void deleteApplicationsByInterviewId(int interviewId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE_APPLICATIONS_BY_INTERVIEW_ID);) {
            preparedStatement.setInt(1, interviewId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }finally{
            cm.freeConnection(conn);
        }
    }
	public List<String> getEmailsOfFollowers(int interviewId) throws SQLException {
		 ConnectionManager cm = ConnectionManager.getInstance();
	        Connection conn = cm.getConnection();

	        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_EMAILS_OF_FOLLOWERS);) {
	            preparedStatement.setInt(1, interviewId);
	            List<String> emails=new ArrayList<String>();
	            ResultSet resSet=preparedStatement.executeQuery();
	            System.out.println(interviewId);
	            int i=0;
	            while(resSet.next()) {
	            	resSet.previous();
	           Trainee trainee= Transformer.getInstance(resSet, Trainee.class, Trainee.class,CommonUser.class);
	         	  emails.add(trainee.getEmail()); 
	        	  i++;
	            }
	           return emails;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new SQLException();
	        }finally{
	            cm.freeConnection(conn);
	        }
	}
}
