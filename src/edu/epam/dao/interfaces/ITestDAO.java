package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.role.CommonUser;

public interface ITestDAO {

	List<Test> getTestsForDirection(String direction) throws SQLException;

	Integer removeTest(Test test) throws SQLException;

	Test editTest(Test test) throws SQLException;

	Integer insertNewTest(Test test) throws SQLException;

	Integer getUserPosition(CommonUser user, Direction direction)
			throws SQLException;

	Test getTestById(int id) throws SQLException;

	List<Test> getTestsById(List<Integer> testsInt) throws SQLException;

	List<Direction> getAllDirections() throws SQLException;

}
