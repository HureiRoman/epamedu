package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.ITeacherDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Group;
import edu.epam.model.Topic;
import edu.epam.role.CommonUser;
import edu.epam.role.Teacher;

public class TeacherDAO implements ITeacherDAO {

	private static final String SQL_GET_TEACHERS_BY_GROUP_ID = "SELECT  * FROM  users u  WHERE  u.id IN  (SELECT teacher_id FROM teachers_of_groups g WHERE g.group_id = ?) ";
// 13.07.2015 Saniok change it because of students table	private static  final String SQL_GET_TEACHER_BY_EMAIL = "SELECT * FROM users u LEFT JOIN students s ON u.id = s.id WHERE email=? ";
	private static  final String SQL_GET_TEACHER_BY_EMAIL = "SELECT * FROM users u  WHERE email=? ";
	private static final String SQL_GET_LIST_OF_GROUPS_BY_ID = "SELECT * FROM groups WHERE group_id IN (SELECT group_id FROM teachers_of_groups WHERE teacher_id=?)  AND is_active = 'true'";
	private static final String SQL_GET_ALL_TEACHERS = "SELECT  * FROM  users u  WHERE u.role='TEACHER' ";
	private static final String SQL_GET_AVAILABLE_TEACHERS_FOR_GROUP_BY_GROUP_ID = "SELECT  * FROM  users u  WHERE  u.id  NOT IN  (SELECT teacher_id FROM teachers_of_groups g WHERE g.group_id = ?) AND u.role='TEACHER' ";
	private static final String SQL_UPDATE_TEACHER = "UPDATE users SET fname=?, lname=?, pname=? WHERE id=?";
	private static final String SQL_GET_TEACHERS_TOPICS = "SELECT * FROM topics WHERE teacher_id=? AND direction_id=?;";
	private static final String SQL_CHANGE_LESSON_TOPIC = "UPDATE lessons SET topic_id=? WHERE id=?";
	
	
	private static TeacherDAO instance;

	private TeacherDAO() {
	}

	public static TeacherDAO getInstance() {
		if (instance == null) {
			instance = new TeacherDAO();
		}
		return instance;
	}

	@Override
	public CommonUser enterTeacher(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Teacher teacher = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_TEACHER_BY_EMAIL);
			 PreparedStatement st2 = conn.prepareStatement(SQL_GET_LIST_OF_GROUPS_BY_ID)) {
		conn.setAutoCommit(false);
			
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		teacher = Transformer.getInstance(rs, Teacher.class, CommonUser.class);
		
		st2.setInt(1, teacher.getId());
		ResultSet rs2 = st2.executeQuery();
		 System.out.println("user id = " + teacher.getId());
		List<Group> listOfGroups = Transformer.getListOfInstances(rs2, Group.class, Group.class);
		teacher.setListOfGroups(listOfGroups);
		
		conn.commit();
		conn.setAutoCommit(true);
		 } catch (Exception e) {
			 conn.rollback();
			 conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return teacher;
	}
	@Override
	public List<Teacher> getTeachersForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Teacher> listOfTeachers = new ArrayList<>(); 
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TEACHERS_BY_GROUP_ID)) {
			st.setInt(1, groupId);
			ResultSet rs1 = st.executeQuery();
			listOfTeachers  = Transformer.getListOfInstances(rs1, Teacher.class, CommonUser.class);
			return listOfTeachers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Teacher> getAllTeachers() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Teacher> listOfTeachers = new ArrayList<>(); 
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ALL_TEACHERS)) {
			ResultSet rs1 = st.executeQuery();
			listOfTeachers  = Transformer.getListOfInstances(rs1, Teacher.class, CommonUser.class);
			return listOfTeachers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Teacher> getAvailableTeachersForGroup(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Teacher> listOfTeachers = new ArrayList<>(); 
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_AVAILABLE_TEACHERS_FOR_GROUP_BY_GROUP_ID)) {
			st.setInt(1, groupId);
			ResultSet rs1 = st.executeQuery();
			listOfTeachers  = Transformer.getListOfInstances(rs1, Teacher.class, CommonUser.class);
			return listOfTeachers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public List<Topic> getTeachersTopics(int teacherId, int directionId) throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Topic> listOfTopics = new ArrayList<>(); 
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_TEACHERS_TOPICS)) {
			st.setInt(1, teacherId);
			st.setInt(2, directionId);
			ResultSet rs1 = st.executeQuery();
			listOfTopics  = Transformer.getListOfInstances(rs1, Topic.class, Topic.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return listOfTopics;
	}
	@Override
	public boolean updateTeacher(Teacher teacher) throws SQLException{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_UPDATE_TEACHER);) {
			statement.setString(1, teacher.getFirstName());
			statement.setString(2, teacher.getLastName());
			statement.setString(3, teacher.getParentName());
			statement.setInt(4, teacher.getId());
			if(statement.executeUpdate()>0)
			result=true;
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			cm.freeConnection(conn);
		}
		
		return result;
	}

	@Override
	public boolean changeTopicInLesson(int idLesson, int idTopic)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_CHANGE_LESSON_TOPIC);) {
			statement.setInt(1, idTopic);
			statement.setInt(2, idLesson);
			if(statement.executeUpdate()>0){
				result=true;
			}
		} catch(Exception e){
			e.printStackTrace();
			return result;
		} finally {
			cm.freeConnection(conn);
		}
		
		return result;
	}

}
