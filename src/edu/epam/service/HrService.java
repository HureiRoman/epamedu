package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Direction;
import edu.epam.role.HR;

public class HrService {
	
	public static HR getHRbyId(int hr_id) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO().getHRbyId(hr_id);
	}
	
	public static HR getRandomHR() throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO().getRandomHr();
	}
	
	
	public static List<HR> getAllHr() throws SQLException{
		return  AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO().getAllHr();
	}

	public static HR getHRofDirection(Integer directionId) throws SQLException {
		return  AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO().getHRofDirection(directionId);
	}

	public static List<Direction> getAllHrDirections(int hr_id) throws SQLException{
		return  AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO().getAllHrDirections(hr_id);
	}
	
}
