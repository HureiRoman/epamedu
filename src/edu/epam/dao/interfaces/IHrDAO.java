package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Direction;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;

public interface IHrDAO {
	CommonUser enterHr(String email) throws SQLException;
	
	HR getHRbyId(int hr_id) throws SQLException;
	
	HR getRandomHr() throws SQLException;
	
	List<HR> getAllHr() throws SQLException;

	HR getHRofDirection(Integer directionId) throws SQLException;
	
	List<Direction> getAllHrDirections(int hr_id) throws SQLException;
	
}
