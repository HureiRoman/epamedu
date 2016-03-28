package edu.epam.dao.interfaces;

import java.sql.SQLException;

import edu.epam.model.GradesOfTask;

public interface IGradesOfTaskDAO {
	public GradesOfTask getGradeBystudentId(int studentId,int taskId) throws SQLException;
	
	boolean setGradeOfTask(GradesOfTask taskGrade) throws SQLException;
}
