package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Application;
import edu.epam.model.Interview;
import edu.epam.model.Message;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;

public interface ITraineeDAO {
	
	
	CommonUser enterTrainee(String email) throws SQLException;
	
	boolean applyToTest(Application application) throws SQLException;
	
	List<Interview> getClosestTests(int user_id) throws SQLException;

	void insertAdditionalInfoInTrainee(int id, boolean isApplicant, boolean receiveNews) throws SQLException;
	
	boolean doStudent(Integer traineeId,Integer groupId) throws SQLException;
	
	List<Interview> getTraineeAppliedTestsEvents(int user_id) throws SQLException;
	
	public List<Message> getChattingWithHR(int logined_user, int hr_id) throws SQLException;
	
	public List<Message> getNewMessagesTraineeHR(int last_mess_id, int user_id, int hr_id) throws SQLException;

	public List<Trainee> getTraineesByApplicationDirectionId(int directionId) throws SQLException;
	
	boolean cancelApplicationOnTest(int directionId, int userId) throws SQLException;

	List<Trainee> getTraineesByInterviewId(int interviewId) throws SQLException;
	List<Trainee> getAllTrainee() throws SQLException;
	
	Trainee getTraineeById(int trainee_id) throws SQLException;
	
	List<Integer> getListOfSubscribedToNewsDirections(int userId) throws SQLException;
	
	boolean subscribeToGetNews(int trainee_id, int idDirection, boolean active) throws SQLException;


	List<Trainee> getTraineesSubscribedToDirection(int directionId) throws SQLException;

	void setVKId(Integer traineeId, String vkId) throws SQLException;

	void setFBId(Integer traineeId, String fbId) throws SQLException;
	
	 List<Trainee> searchTrainee(String chooseBy) throws SQLException;
}
