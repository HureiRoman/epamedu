package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.epam.model.CourseComment;
import edu.epam.model.Direction;
import edu.epam.model.TestResults;

public interface IDirectionDAO {
	

	Integer createDirection(Direction direction) throws SQLException;

	boolean deleteDirection(int id) throws SQLException;
	List<Direction> getAllDirections() throws SQLException;

	List<Direction> getDirectionsByHRId(int hrId) throws SQLException;

	Direction getDirectionByName(String name) throws SQLException;

	boolean setDirectionActive(Integer directionId, Boolean active);

	Direction getDirectionById(Integer id) throws SQLException;

	boolean updateDirectionData(Direction direction) throws SQLException;

	List<Direction> getAllDirectionsWithInterview() throws SQLException;
	
	List<Direction> getTeacherDirections(Integer teacherId) throws SQLException;

	boolean setDirectionHR(Integer directionId, Integer hrId);

	Map<Direction, List<TestResults>> getTop10Map() throws SQLException;

	List<CourseComment> getCourseComments(Integer directionId)
			throws SQLException;
	
	List<Direction> getAllDirectionsForAdmin() throws SQLException;
}
