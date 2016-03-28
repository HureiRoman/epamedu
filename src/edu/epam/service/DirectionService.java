package edu.epam.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.CourseComment;
import edu.epam.model.Direction;
import edu.epam.model.TestResults;

public class DirectionService {

	public static Integer createDirection(Direction direction)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().createDirection(direction);
	}


	public static boolean deleteDirection(int id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().deleteDirection(id);
	}

	public static List<Direction> getAllDirections() throws SQLException {

		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getAllDirections();
	}
	
	public static List<Direction> getAllDirectionsForAdmin() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getAllDirectionsForAdmin();
	}
	public static List<Direction> getDirectionsByHRId(int hrId) throws SQLException {

		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getDirectionsByHRId(hrId);
	}
	public static boolean setDirectionActive(Integer directionId, Boolean active) {

		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().setDirectionActive(directionId,active);
	}

	public static Map<Direction, List<TestResults>> getTop10Map() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getTop10Map();
	}

	public static Direction getDirectionById(Integer id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getDirectionById(id);
	}

	public static boolean updateDirectionData(Direction direction) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().updateDirectionData(direction);
	}


	public static boolean setDirectionHR(Integer directionId, Integer hrId) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().setDirectionHR(directionId,hrId);
	}

 
	public static List<Direction> getTeacherDirections(Integer teacherId) throws SQLException  {
	return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
			.getDirectionDAO().getTeacherDirections(teacherId);
	} 
	
	public static List<Direction> getAllDirectionsWithInterview() throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getAllDirectionsWithInterview();
	}
	public static List<CourseComment> getCourseComments(Integer directionId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getDirectionDAO().getCourseComments(directionId);
	}
}
