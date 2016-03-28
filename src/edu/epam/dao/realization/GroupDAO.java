package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IGroupDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.CV;
import edu.epam.model.Direction;
import edu.epam.model.Group;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;

public class GroupDAO implements IGroupDAO {

	private static final String SQL_GET_GROUPS_BY_DIRECTION_ID = "SELECT * FROM groups WHERE direction_id=? ";
	private static final String SQL_GET_ACTIVE_GROUPS_BY_DIRECTION_ID = "SELECT * FROM groups WHERE direction_id=? AND date_of_graduation IS NULL";
	private static final String SQL_GET_STUDENTS_OF_GROUP = "SELECT * FROM users u LEFT JOIN students s ON s.id=u.id WHERE u.id IN (SELECT id FROM students WHERE group_id=?);";
	private static final String SQL_SET_GROUP_ACTIVE = "UPDATE groups g SET g.is_active= ? WHERE g.group_id=? ; ";
	private static final String SQL_GET_GROUPS_BY_TEACHER_ID = "SELECT * FROM groups WHERE group_id IN (SELECT group_id FROM teachers_of_groups WHERE teacher_id=?)  AND is_active = 'true'";
	private static final String SQL_GET_DIRECTIONS_FOR_TEACHER = " SELECT DISTINCT * FROM directions INNER JOIN groups ON directions.id=groups.direction_id INNER JOIN teachers_of_groups tg ON tg.group_id=groups.group_id WHERE teacher_id=? AND groups.is_active = 'true' AND directions.is_active = 'true' GROUP BY name";
	private static final String SQL_CREATE_GROUP = "INSERT INTO groups (title, direction_id)"
			+ " VALUES(?, ?) ";
	private static final String SQL_GET_GROUP_BY_ID = "SELECT * FROM groups WHERE group_id=? AND is_active = 'true'";
	private static final String SQL_UPDATE_GROUP = "UPDATE groups g  SET g.title=? WHERE g.group_id= ? ";
	
	private static final String SQL_DELETE_STAFF = " DELETE FROM teachers_of_groups WHERE group_id = ? ";
	private static final String SQL_INSERT_STAFF = " INSERT INTO teachers_of_groups  (group_id,teacher_id) VALUES  (?,?) ";

	private static final String SQL_GET_STUDENTS_CV = "SELECT * FROM cv WHERE id=?";


	private static GroupDAO instance;

	private GroupDAO() {
	}

	public static GroupDAO getInstance() {
		if (instance == null) {
			instance = new GroupDAO();
		}
		return instance;
	}
	@Override
	public List<Group> getListOfGroupForTeacher(Integer teacher_id)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Group> listOfGroups = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_GROUPS_BY_TEACHER_ID)) {

			st.setInt(1, teacher_id);
			ResultSet rs1 = st.executeQuery();
			listOfGroups = Transformer.getListOfInstances(rs1, Group.class,
					Group.class);

			return listOfGroups;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public Group getGroupById(Integer groupId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Group group = new Group();
		try (PreparedStatement statement = conn.prepareStatement(SQL_GET_GROUP_BY_ID);
				PreparedStatement statement2 = conn.prepareStatement(SQL_GET_STUDENTS_OF_GROUP);
					PreparedStatement statemen3 = conn.prepareStatement(SQL_GET_STUDENTS_CV)) {
			conn.setAutoCommit(false);	
			statement.setInt(1, groupId);
				
			ResultSet rs1 = statement.executeQuery();
			if(rs1.next()){
				rs1.previous();
				group = Transformer.getInstance(rs1, Group.class, Group.class);
				
				statement2.setInt(1, groupId);
				ResultSet resultSet = statement2.executeQuery();
				List<Student> listOfStudents = Transformer.getListOfInstances(resultSet, Student.class, CommonUser.class, Student.class);
				if(listOfStudents != null){
					for(Student student : listOfStudents){
						statemen3.setInt(1, student.getId());
						ResultSet resultSet3 = statemen3.executeQuery();
						CV cv = Transformer.getInstance(resultSet3, CV.class, CV.class);
						student.setCv(cv);
					}
				}
				
				group.setListOfStudents(listOfStudents);
			}
			conn.commit();
			conn.setAutoCommit(true);
			return group;
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Direction> getListOfDirectionsForTeacher(Integer teacher_id)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> list_of_directions = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_DIRECTIONS_FOR_TEACHER)) {

			st.setInt(1, teacher_id);

			ResultSet rs1 = st.executeQuery();

			while (rs1.next()) {
				rs1.previous();
				Direction direction = Transformer.getInstance(rs1,
						Direction.class, Direction.class);
				list_of_directions.add(direction);
			}
			System.out.println(list_of_directions);
			return list_of_directions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean createGroup(Group group) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate

		try (PreparedStatement st = conn.prepareStatement(SQL_CREATE_GROUP,
				Statement.RETURN_GENERATED_KEYS);) {
			st.setString(1, group.getTitle());
			st.setInt(2, group.getDirectionId());
			rowsAffected = st.executeUpdate();
            Integer groupId = null;
			if (rowsAffected > 0) {
				ResultSet generatedKeys = st.getGeneratedKeys();
				if (generatedKeys.next()) {
					groupId = generatedKeys.getInt(1);
				}
			}
		insertTeachers(group.getTeachers(), groupId);
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return rowsAffected > 0;
	}
	
	private int insertTeachers(List<Integer> teachers,Integer groupId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate
		try  {
			for (Integer teacherId : teachers) {
				PreparedStatement st = conn.prepareStatement(SQL_INSERT_STAFF);
				st.setInt(1, groupId);
				st.setInt(2, teacherId);				
				rowsAffected += st.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return rowsAffected ;		
	}



	@Override
	public List<Group> getListOfGroupsForDirection(Integer directionId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Group> listOfGroups = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_GROUPS_BY_DIRECTION_ID)) {
			st.setInt(1, directionId);
			ResultSet rs1 = st.executeQuery();
			listOfGroups = Transformer.getListOfInstances(rs1, Group.class,
					Group.class);
			return listOfGroups;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public List<Group> getListOfActiveGroupsForDirection(Integer directionId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Group> listOfGroups = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ACTIVE_GROUPS_BY_DIRECTION_ID)) {
			st.setInt(1, directionId);
			ResultSet rs1 = st.executeQuery();
			listOfGroups = Transformer.getListOfInstances(rs1, Group.class,
					Group.class);
			return listOfGroups;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean setGroupActive(Integer groupId, Boolean active) {
		Connection connect = ConnectionManager.getInstance().getConnection();
		int rowsUpdated = 0;
		PreparedStatement pStatement = null;
		try {
			pStatement = connect.prepareStatement(SQL_SET_GROUP_ACTIVE);
			pStatement.setString(1, active.toString());
			pStatement.setInt(2, groupId);
			rowsUpdated = pStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		ConnectionManager.getInstance().freeConnection(connect);
		return rowsUpdated > 0;
	}
	@Override
	public boolean updateGroup(Group group) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		int rowsAffected = 0;// for executeUpdate
		try (PreparedStatement st = conn.prepareStatement(SQL_UPDATE_GROUP);) {
			st.setString(1, group.getTitle());
			st.setInt(2, group.getId());
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return rowsAffected > 0;
	}
	@Override
	public boolean updateGroupStaff(Integer groupId,
			ArrayList<Integer> teachersIds) {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate
		try (PreparedStatement st = conn.prepareStatement(SQL_DELETE_STAFF);) {
			st.setInt(1, groupId);
			rowsAffected  += st.executeUpdate();
			rowsAffected  += insertTeachers(teachersIds, groupId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return rowsAffected>0;
	}

	

}
