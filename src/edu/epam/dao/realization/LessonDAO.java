package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.ILessonDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Lesson;
import edu.epam.model.StudentAttachment;
import edu.epam.model.Task;

public class LessonDAO implements ILessonDAO {

	private static final String SQL_GET_LESSONS_FOR_GROUP = "SELECT * FROM lessons l WHERE l.group_id=?  AND l.isVisible='true';";
	private static final String SQL_GET_ALL_LESSONS_FOR_GROUP = "SELECT * FROM lessons l WHERE l.group_id=?";
	private static final String SQL_SET_LESSON_ACTIVE = "UPDATE lessons SET isVisible=?, autoTurnOn='false' WHERE id=?";
	private static final String SQL_ADD_NEW_LESSON = "INSERT INTO lessons (group_id, topic_id, task_id, lessonTime) VALUES (?,?,?,?)";
	private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE id=?";
	private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET task_id=? WHERE id=?";
	private static final String SQL_SET_LESSON_AUTO_TURN_ON = "UPDATE lessons SET autoTurnOn=? WHERE id=?";
	private static final String SQL_GET_LESSON_AUTO_TURN_ON = "SELECT * FROM lessons WHERE autoTurnOn='true'";
	private static final String SQL_GET_GROUP_TASKS = "SELECT * FROM tasks WHERE type='TASK' AND group_id=?";
	private static final String SQL_GET_STUDENT_TASKS = "SELECT * FROM student_attachments WHERE task_id IN (SELECT id FROM tasks WHERE group_id=?) AND task_id=?";
	private static final String SQL_GET_ATTACHMENT_MARK = "SELECT grade FROM grades_of_tasks WHERE student_id=? AND task_id=?";
	private static final String SQL_GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE id = ?";
	private static final String SQL_GET_AMOUNT_PAST_LESSONS_FOR_GROUP = "SELECT COUNT(*) FROM lessons L WHERE L.group_id = ?  " +
			" AND lessonTime <= NOW()";

	private static LessonDAO instance;

	private LessonDAO() {
	}

	public static LessonDAO getInstance() {
		if (instance == null) {
			instance = new LessonDAO();
		}
		return instance;
	}

	@Override
	public List<Lesson> getLessonsForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Lesson> lessons = new ArrayList<Lesson>();
		try (java.sql.PreparedStatement st = conn
				.prepareStatement(SQL_GET_LESSONS_FOR_GROUP)) {
			st.setInt(1, groupId);
			ResultSet rs = st.executeQuery();
			Lesson lesson = new Lesson();
			while (rs.next()) {
				rs.previous();
				lesson = Transformer.getInstance(rs, Lesson.class, Lesson.class);
				lessons.add(lesson);
			}
			return lessons;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Lesson> getAllLessonsForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Lesson> lessons = new ArrayList<Lesson>();
		try (java.sql.PreparedStatement st = conn
				.prepareStatement(SQL_GET_ALL_LESSONS_FOR_GROUP)) {
			st.setInt(1, groupId);
			ResultSet rs = st.executeQuery();
			Lesson lesson = new Lesson();
			while (rs.next()) {
				rs.previous();
				lesson = Transformer.getInstance(rs, Lesson.class, Lesson.class);
				lessons.add(lesson);
			}
			return lessons;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean setLessonActive(int lessonId, boolean active) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		try (PreparedStatement st = conn.prepareStatement(SQL_SET_LESSON_ACTIVE)) {
			st.setString(1, String.valueOf(active));
			st.setInt(2, lessonId);
			int rs = st.executeUpdate();
			if(rs == 1){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean addLesson(Lesson lesson) throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement statement = conn.prepareStatement(SQL_ADD_NEW_LESSON)) {
			statement.setInt(1, lesson.getGroupId());
			statement.setInt(2, lesson.getTopicId());
			statement.setInt(3, lesson.getTaskId());
			statement.setTimestamp(4, new Timestamp(lesson.getLessonTime().getTime()));
			int resultRows = statement.executeUpdate();
			
			return resultRows > 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean deleteLesson(int lessonId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement statement = conn.prepareStatement(SQL_DELETE_LESSON)) {
			statement.setInt(1, lessonId);
			int resultRows = statement.executeUpdate();
			return resultRows > 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean updateLesson(Lesson lessonWithNewHW) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement statement = conn.prepareStatement(SQL_UPDATE_LESSON)) {
			statement.setInt(1, lessonWithNewHW.getTaskId());
			statement.setInt(2, lessonWithNewHW.getId());
			int resultRows = statement.executeUpdate();
			return resultRows > 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean setLessonAutoTurnOn(int lessonId, boolean active)
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		try (PreparedStatement statement = conn.prepareStatement(SQL_SET_LESSON_AUTO_TURN_ON)) {
			statement.setString(1, String.valueOf(active));
			statement.setInt(2, lessonId);
			int resultSet = statement.executeUpdate();
			if(resultSet == 1){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}

	@Override
	public List<Lesson> getAutoTurnOnLessons() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Lesson> listOfAutoOpenLessons = null;
		try (PreparedStatement statement = conn.prepareStatement(SQL_GET_LESSON_AUTO_TURN_ON)) {
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				resultSet.previous();
				listOfAutoOpenLessons = Transformer.getListOfInstances(resultSet, Lesson.class, Lesson.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return listOfAutoOpenLessons;
	}

	@Override
	public List<Task> getGroupTasks(int groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Task> listOfTasks = null;
		try (PreparedStatement statement = conn.prepareStatement(SQL_GET_GROUP_TASKS);
				PreparedStatement statementGetStudentAttachment = conn.prepareStatement(SQL_GET_STUDENT_TASKS);
					PreparedStatement getAttachmentMark = conn.prepareStatement(SQL_GET_ATTACHMENT_MARK);) {
			conn.setAutoCommit(false);
			
			Task task = null;
			statement.setInt(1, groupId);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				resultSet.previous();
				listOfTasks = new ArrayList<>();
				while(resultSet.next()){
					resultSet.previous();
					task = Transformer.getInstance(resultSet, Task.class, Task.class);

					statementGetStudentAttachment.setInt(1, groupId);
					statementGetStudentAttachment.setInt(2, task.getId());
					
					ResultSet studentAttachmentsTasks = statementGetStudentAttachment.executeQuery();
					if(studentAttachmentsTasks.next()){
						studentAttachmentsTasks.previous();
						List<StudentAttachment> listOfStudentAttachmentsTasks = Transformer.getListOfInstances(studentAttachmentsTasks, StudentAttachment.class, StudentAttachment.class);
						for(StudentAttachment attachment : listOfStudentAttachmentsTasks){
							getAttachmentMark.setInt(1, attachment.getStudentId());
							getAttachmentMark.setInt(2, attachment.getTaskId());
							ResultSet attachmentMark = getAttachmentMark.executeQuery();
							if(attachmentMark.next()){
								attachment.setMark(attachmentMark.getInt(1));
							}
						}
						task.setListOfStudentAttachmentsTasks(listOfStudentAttachmentsTasks);
					}
					listOfTasks.add(task);
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return listOfTasks;
	}

	@Override
	public Lesson getLessonById(int lessonId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		Lesson lesson = null;
		try {PreparedStatement preparedStatement = connect
					.prepareStatement(SQL_GET_LESSON_BY_ID);
			preparedStatement.setInt(1, lessonId);
			ResultSet resultSetOfNewsItem = preparedStatement.executeQuery();
			lesson = Transformer.getInstance(resultSetOfNewsItem, Lesson.class, Lesson.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(connect);
		}
		return lesson;
	}

	@Override
	public int getAmountOfPastLessonsForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int result = 0;
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_GET_AMOUNT_PAST_LESSONS_FOR_GROUP)) {
			preparedStatement.setInt(1, groupId);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}


}
