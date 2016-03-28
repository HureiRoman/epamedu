package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.model.Test;
import edu.epam.role.CommonUser;

public class TestService {

	// онлайн тести
	public static List<Test> getTestsForDirection(String direction)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getTestsForDirection(direction);
	}
	
	public static List<Direction> getAllDirections() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getAllDirections();
	}
	
	public static List<Test> getTestsById(List<Integer> testsInt) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getTestsById(testsInt);
	}
	
	public static Test getTestById(int id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getTestById(id);
	}
	
	public static Direction getDirection(String name) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getDirectionDAO().getDirectionByName(name);
	}
	
	public static Integer getUserPosition(CommonUser user, Direction direction) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getUserPosition(user, direction);
	}
	
	public static Integer addNewTest(Test test) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().insertNewTest(test);
	}
	
	public static Test editTest(Test test) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().editTest(test);
	}

	public static Integer removeTest(Test test) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().removeTest(test);
	}
	
	public static List<Test> getTestsPortionForDirection(String direction, int start,
			int quantity) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTestDAO().getTestsPortionForDirection(direction, start, quantity);
	}
	
	// тестування на курси
	public static List<Interview> getClosestTestMeeting(int user_id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getClosestTests(user_id);
	}
	public static List<Interview> getTraineeAppliedTestEvents(int user_id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getTraineeAppliedTestsEvents(user_id);
	}
	
	public static List<Integer> getListOfSubscribedToNewsDirections(int userId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTraineeDAO().getListOfSubscribedToNewsDirections(userId);
	}

}
