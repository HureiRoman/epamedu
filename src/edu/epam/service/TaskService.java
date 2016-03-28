package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.constants.TypeOfTasks;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Task;

public class TaskService {

	public static List<Task> getHomeWorkForGroup(Integer groupId)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO()
				.getHomeWorkForGroup(groupId);
	}

	public static Task getTaskById(Integer taskId, TypeOfTasks typeOfTasks) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO()
				.getTaskById(taskId, typeOfTasks);

	}
	
	public static int addHomework(Task hw) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().addHomework(hw);
	}
	public static int addNewTaskHomework(Task task) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().addNewTaskHomework(task);
	}
	
	public static boolean updateHomework(Task hw) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().updateHomework(hw);
		
	}
	
	public static boolean deleteHomework(int lessonId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().deleteHomework(lessonId);
	}

	public static List<Task> getTasksForGroupByUserId(int userId) throws SQLException  {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().getTasksForGroupByUserId(userId);
	}
	
	public static boolean deleteHomeworkTask(int idTask) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().deleteHomeworkTask(idTask);
	}

	public static boolean updateTask(Task task) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().updateTask(task);
	}

	public static List<Task> getTasksForGroup(int groupId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTaskDAO().getTasksForGroup(groupId);
	}
}
