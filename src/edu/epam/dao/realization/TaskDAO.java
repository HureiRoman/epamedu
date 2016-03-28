package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.constants.TypeOfTasks;
import edu.epam.dao.interfaces.ITaskDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Task;

public class TaskDAO implements ITaskDAO {

	private static final String SQL_GET_TASK_BY_ID_AND_TYPE = "SELECT * FROM tasks t WHERE t.id = ? AND t.type = ?";

	private static final String GET_TASKS_FOR_GROUP_BY_STUDENT_ID = "SELECT * FROM tasks WHERE group_id IN (SELECT group_id FROM students WHERE id=?)";

	private static String SQL_GET_HOMEWORK_FOR_GROUP = "SELECT * FROM tasks INNER JOIN lessons ON lessons.id=tasks.lesson_id "
			+ " WHERE tasks.lesson_id IN (SELECT lesson_id FROM homeworks) AND group_id=?";
	
	private static String SQL_INSERT_NEW_HW = "INSERT INTO tasks (title,task_body,deadline,type) VALUES (?,?,?,?)";
	private static String SQL_INSERT_NEW_TASK = "INSERT INTO tasks (title,task_body,deadline,type,group_id) VALUES (?,?,?,?,?)";
	private static String SQL_UPDATE_HOMEWORK = "UPDATE tasks SET title=?, task_body=?, deadline=?, type=? WHERE id=?;";
	private static String SQL_DELETE_HOMEWORK = "UPDATE lessons SET task_id = 0 WHERE id=?";
	private static String SQL_TASKS_DELETE = "DELETE FROM tasks WHERE id=?";
	private static final String SQL_GET_TASKS_FOR_GROUP = "SELECT * FROM tasks WHERE group_id = ? AND type = 'TASK'";
	
	private static TaskDAO instance;

	public static TaskDAO getInstance() {

		if (instance == null) {
			instance = new TaskDAO();
		}
		return instance;
	}
	@Override
	public List<Task> getHomeWorkForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Task> homeWork = new ArrayList<>();
		Task task = new Task();

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_HOMEWORK_FOR_GROUP);) {
			st.setInt(1, groupId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				rs.previous();
				task = Transformer.getInstance(rs, Task.class, Task.class);
				homeWork.add(task);
			}

			return homeWork;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public Task getTaskById(Integer taskId, TypeOfTasks typeOfTasks) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Task task = new Task();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TASK_BY_ID_AND_TYPE);) {
			st.setInt(1, taskId);
			st.setString(2, typeOfTasks.name());
			ResultSet rs = st.executeQuery();
			task = Transformer.getInstance(rs, Task.class, Task.class);
			return task;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public int addHomework(Task hw) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int generatedKey = 0;
		try (PreparedStatement statement = conn.prepareStatement(SQL_INSERT_NEW_HW,Statement.RETURN_GENERATED_KEYS);) {
			statement.setString(1, hw.getTitle());
			statement.setString(2, hw.getTaskBody());
			statement.setTimestamp(3, new Timestamp(hw.getDeadline().getTime()));
			statement.setString(4, hw.getTypeOfTasks().toString());
			int result = statement.executeUpdate();
			
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
                System.out.println("Key returned from getGeneratedKeys():"
                        + rs.getInt(1));
                generatedKey = rs.getInt(1);
            } 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return generatedKey;
	}

	@Override
	public boolean updateHomework(Task hw) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean resultOfUpdate = false;
		try (PreparedStatement statement = conn.prepareStatement(SQL_UPDATE_HOMEWORK);) {
			statement.setString(1, hw.getTitle());
			statement.setString(2, hw.getTaskBody());
			statement.setTimestamp(3, new Timestamp(hw.getDeadline().getTime()));
			statement.setString(4, hw.getTypeOfTasks().toString());
			statement.setInt(5, hw.getId());
			
			int result = statement.executeUpdate();
			
			if (result > 0) {
				resultOfUpdate = true;
            } 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return resultOfUpdate;
	}

	@Override
	public boolean deleteHomework(int lessonId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean resultOfDelete = false;
		try (PreparedStatement statement = conn.prepareStatement(SQL_DELETE_HOMEWORK);) {
			statement.setInt(1, lessonId);
			
			int result = statement.executeUpdate();
			
			if (result > 0) {
				resultOfDelete = true;
            } 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return resultOfDelete;
	}
	public List<Task> getTasksForGroupByUserId(int userId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Task> tasks = new ArrayList<>();
		Task task = new Task();

		try (PreparedStatement st = conn
				.prepareStatement(GET_TASKS_FOR_GROUP_BY_STUDENT_ID);) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			tasks= Transformer.getListOfInstances(rs, Task.class, Task.class);
			
			return tasks;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public boolean deleteHomeworkTask(int idTask) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean resultOfDeleting = false;

		try (PreparedStatement deleteStatement = conn.prepareStatement(SQL_TASKS_DELETE);) {
			deleteStatement.setInt(1, idTask);
			int effectedRows = deleteStatement.executeUpdate();
			if(effectedRows > 0){
				resultOfDeleting = true;
			}
			
			return resultOfDeleting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public int addNewTaskHomework(Task task) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int generatedKey = 0;
		try (PreparedStatement statement = conn.prepareStatement(SQL_INSERT_NEW_TASK,Statement.RETURN_GENERATED_KEYS);) {
			statement.setString(1, task.getTitle());
			statement.setString(2, task.getTaskBody());
			statement.setTimestamp(3, new Timestamp(task.getDeadline().getTime()));
			statement.setString(4, task.getTypeOfTasks().toString());
			statement.setInt(5, task.getGroupId());
			statement.executeUpdate();
			
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
                System.out.println("Key returned from getGeneratedKeys():"
                        + rs.getInt(1));
                generatedKey = rs.getInt(1);
            } 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return generatedKey;
	}
	@Override
	public boolean updateTask(Task task) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean resultOfUpdate = false;
		try (PreparedStatement statement = conn.prepareStatement(SQL_UPDATE_HOMEWORK);) {
			statement.setString(1, task.getTitle());
			statement.setString(2, task.getTaskBody());
			statement.setTimestamp(3, new Timestamp(task.getDeadline().getTime()));
			statement.setString(4, task.getTypeOfTasks().toString());
			statement.setInt(5, task.getId());
			
			int result = statement.executeUpdate();
			
			if (result > 0) {
				resultOfUpdate = true;
            } 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return resultOfUpdate;
	}

	@Override
	public List<Task> getTasksForGroup(int groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Task> tasks = new ArrayList<>();
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_TASKS_FOR_GROUP);) {
			Task task = null;
			preparedStatement.setInt(1, groupId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				resultSet.previous();
				task = Transformer.getInstance(resultSet, Task.class, Task.class);
				tasks.add(task);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

		return tasks;
	}
}