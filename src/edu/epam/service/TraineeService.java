package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Application;
import edu.epam.model.Direction;
import edu.epam.model.Message;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;

public class TraineeService {

	/**
	 * @return
	 * @throws SQLException
	 */
	public static Integer createTrainee(Trainee trainee) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().insertNewTrainee(trainee);
	}

	public static boolean applyToTest(Application application) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().applyToTest(application);
	}
	public static boolean doStudent(Integer traineeId,Integer groupId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().doStudent(traineeId, groupId);
	}

	public static void insertAdditionalInfo(int id, boolean isApplicant, boolean receiveNews) throws SQLException {
		AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().insertAdditionalInfoInTrainee(id, isApplicant, receiveNews);
	}
	public static Trainee getTrainee(String email) throws SQLException {
		return (Trainee)AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().enterTrainee(email);
	}

	public static List<Message> getChattingWithHR(int logined_user, int hr_id) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getChattingWithHR(logined_user, hr_id);
	}
	
	public static List<Message> getNewMessagesTraineeHR(int last_message_id, int user_id, int hr_id) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getNewMessagesTraineeHR(last_message_id,user_id,hr_id);
	}
	public static boolean passOnlineTest(CommonUser user, Direction direction) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().passNewTest(user, direction);
	}

	public static List<Trainee> getTraineesByApplicationDirectionId(int directionId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineesByApplicationDirectionId(directionId);
	}

	public static Integer getTraineesAmountByApplicationDirectionId(int directionId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineesAmountByApplicationDirectionId(directionId);
	}
	
	public static List<Trainee> getTraineesByInterviewId(int interviewId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineesByInterviewId(interviewId);
	}
	
	public static boolean cancelApplicationOnTest(int directionId, int userId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().cancelApplicationOnTest(directionId,userId);
	}
	
	public static List<Trainee> getAllTrainee () throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getAllTrainee();
	}
	
	public static List<Trainee> searchTrainee(String chooseBy) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().searchTrainee(chooseBy);
	}
	
	public static Trainee getTraineeById (int trainee_id) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineeById(trainee_id);
	}

	
	public static boolean subscribeToGetNews(int trainee_id,int idDirection, boolean active) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().subscribeToGetNews(trainee_id,idDirection, active);
	}


	public static List<Trainee> getTraineesSubscribedToDirection(int directionId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineesSubscribedToDirection(directionId);
	}

	public static List<Trainee> getPortionOfTraineesByApplicationDirectionId(
			int directionId, int start, int quantity) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getPortionOfTraineesByApplicationDirectionId(directionId, start, quantity);
	}

	public static void setVKId(Integer traineeId, String vkId) throws SQLException {
		AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().setVKId(traineeId, vkId);
	}

	public static void setFBId(Integer traineeId, String fbId) throws SQLException {
		AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().setFBId(traineeId, fbId);
	}

}