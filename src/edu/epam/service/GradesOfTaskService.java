package edu.epam.service;

import java.sql.SQLException;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.GradesOfTask;

public class GradesOfTaskService {

	public static GradesOfTask getGradeBystudentId(int studentId,int taskId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGradeOfTaskDAO()
				.getGradeBystudentId(studentId,taskId);

	}
	
	public static boolean setGradeOfTask(GradesOfTask taskGrade) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGradeOfTaskDAO().setGradeOfTask(taskGrade);
	}

}
