package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IGradesOfTaskDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.GradesOfTask;

public class GradesOfTaskDAO implements IGradesOfTaskDAO {
	
	private static final String SQL_GET_GRADE_BY_STUDENT_ID="SELECT * FROM grades_of_tasks WHERE student_id=? AND task_id=?";
	private static final String SQL_SET_GRADE_OF_TASK = "INSERT INTO grades_of_tasks (student_id,task_id,teacher_id,grade) VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_GRADE_OF_TASK = "UPDATE grades_of_tasks SET grade = ? WHERE grade_id=?";
	
	private static GradesOfTaskDAO instance;

	public static GradesOfTaskDAO getInstance() {

		if (instance == null) {
			instance = new GradesOfTaskDAO();
		}
		return instance;
	}

	@Override
	public GradesOfTask getGradeBystudentId(int studentId,int taskId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		PreparedStatement pStatement = null;
		GradesOfTask grade =null;
		try {
			pStatement = connect
					.prepareStatement(SQL_GET_GRADE_BY_STUDENT_ID);
			pStatement.setInt(1, studentId);
			pStatement.setInt(2, taskId);
			ResultSet resultSet = pStatement.executeQuery();
			grade = Transformer.getInstance(resultSet, GradesOfTask.class,GradesOfTask.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return grade;
	}

	@Override
	public boolean setGradeOfTask(GradesOfTask taskGrade) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		boolean result = false;
		try {PreparedStatement checkGradePresents = connect.prepareStatement(SQL_GET_GRADE_BY_STUDENT_ID);
				PreparedStatement setNewGrade = connect.prepareStatement(SQL_SET_GRADE_OF_TASK);
					PreparedStatement updateNewGrade = connect.prepareStatement(SQL_UPDATE_GRADE_OF_TASK);
			
				checkGradePresents.setInt(1, taskGrade.getStudentId());
				checkGradePresents.setInt(2, taskGrade.getTaskId());
				ResultSet resultSet = checkGradePresents.executeQuery();
				int effectedRows = 0;
				if(resultSet.next()){
					updateNewGrade.setInt(1, taskGrade.getGrade());
					updateNewGrade.setInt(2, resultSet.getInt("grade_id"));
					effectedRows = updateNewGrade.executeUpdate();
				}else{
					setNewGrade.setInt(1, taskGrade.getStudentId());
					setNewGrade.setInt(2, taskGrade.getTaskId());
					setNewGrade.setInt(3, taskGrade.getTeacherId());
					setNewGrade.setInt(4, taskGrade.getGrade());
					effectedRows = setNewGrade.executeUpdate();
				}
				if(effectedRows > 0){
					result = true;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return result;
	}

}
