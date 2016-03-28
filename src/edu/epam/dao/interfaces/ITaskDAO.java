package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.TypeOfTasks;
import edu.epam.model.Task;



public interface ITaskDAO {
	List<Task> getHomeWorkForGroup(Integer groupId) throws SQLException;

	Task getTaskById(Integer taskId, TypeOfTasks typeOfTasks)
			throws SQLException;
	
	 int addHomework(Task hw) throws SQLException;
	 
	 boolean updateHomework(Task hw) throws SQLException;
	 
	 boolean deleteHomework(int lessonId) throws SQLException;
	 
	 boolean deleteHomeworkTask(int idTask) throws SQLException;
	 
	 int addNewTaskHomework(Task task) throws SQLException;
	 
	  boolean updateTask(Task task) throws SQLException;

	List<Task> getTasksForGroup(int groupId) throws SQLException;
}
