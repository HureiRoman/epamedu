package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IDirectionDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.CourseComment;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.role.User;

public class DirectionDAO implements IDirectionDAO {

	private static final String SQL_GET_ALL_DIRECTIONS = "SELECT * FROM directions WHERE is_active='true'";
	private static final String SQL_GET_ALL_DIRECTIONS_FOR_ADMIN = "SELECT * FROM directions";
	private static final String SQL_GET_DIRECTION_INTERVIEW = "SELECT * FROM interviews WHERE direction_id = ? AND date_of_testing > CURRENT_TIMESTAMP AND isActive='true'";
	private static final String SQL_CREATE_DIRECTION = "INSERT INTO directions(name, recruter_id, description, code_language) VALUES(?, ?, ?, ?) ";
	private static final String SQL_DELETE_DIRECTION = "UPDATE directions SET isActive='false' WHERE id=? ";
	private static final String SQL_UPDATE_DIRECTION = " UPDATE directions d  SET d.name=?, d.description=?, d.code_language=? WHERE d.id = ?";
	private static final String SQL_GET_DIRECTION_BY_NAME = "SELECT * FROM directions WHERE name=?";
	private static final String SQL_SET_DIRECTION_ACTIVE = "UPDATE directions d SET d.is_active= ? WHERE d.id=? ; ";
	private static final String SQL_GET_TOP10_BY_DIRECTION = "SELECT * FROM test_results WHERE test_count > 4 AND direction_id=? ORDER BY points/test_count DESC LIMIT 10";
	private static final String SQL_GET_USERS_BY_ID = "SELECT * FROM users WHERE id IN (?";
	private static final String SQL_GET_DIRECTION_BY_ID = "SELECT * FROM directions d  WHERE d.id=?";

	private static final String SQL_GET_DIRECTIONS_BY_HR_ID = "SELECT * FROM directions WHERE recruter_id = ?";
	private static final String SQL_SET_DIRECTION_HR = "UPDATE directions d SET d.recruter_id = ? WHERE d.id=?";
	private static final String SQL_GET_DIRECTION_FOR_TEACHER=" SELECT DISTINCT * FROM directions WHERE id IN (SELECT direction_id FROM topics WHERE teacher_id=?) AND directions.is_active='true'";
	private static final String SQL_GET_COMMENTS_FOR_DIRECTION = "SELECT * FROM feedbacks f WHERE f.direction_id = ?" ;

	private static DirectionDAO instance;

	public static DirectionDAO getInstance() {

		if (instance == null) {
			instance = new DirectionDAO();
		}
		return instance;
	}

	@Override
	public List<Direction> getAllDirections() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> listOfDirection = new ArrayList<>();

		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(SQL_GET_ALL_DIRECTIONS);
			while (rs.next()) {
				rs.previous();
				Direction direction = Transformer.getInstance(rs,
						Direction.class, Direction.class);
				listOfDirection.add(direction);
			}
			return listOfDirection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

	}
	
	@Override
	public List<Direction> getAllDirectionsForAdmin() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> listOfDirection = new ArrayList<>();

		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(SQL_GET_ALL_DIRECTIONS_FOR_ADMIN);
			while (rs.next()) {
				rs.previous();
				Direction direction = Transformer.getInstance(rs,
						Direction.class, Direction.class);
				listOfDirection.add(direction);
			}
			return listOfDirection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

	}
	
	@Override
	public Integer createDirection(Direction direction) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate
		Integer directionId = null;
		try (PreparedStatement st = conn.prepareStatement(SQL_CREATE_DIRECTION,
				Statement.RETURN_GENERATED_KEYS);) {
			st.setString(1, direction.getName());
			st.setInt(2, direction.getRecruter_id());
			st.setString(3, direction.getDescription());
			st.setString(4, direction.getCodeLang());
			rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet generatedKeys = st.getGeneratedKeys();
				if (generatedKeys.next()) {
					directionId = generatedKeys.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return directionId;
	}

	@Override
	public boolean deleteDirection(int id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeDelete
		try (PreparedStatement st = conn.prepareStatement(SQL_DELETE_DIRECTION);) {
			st.setInt(1, id);
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return (rowsAffected > 0);
	}

	@Override
	public Direction getDirectionByName(String name) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_DIRECTION_BY_NAME);) {
			st.setString(1, name);
			ResultSet rs = st.executeQuery();
			return Transformer
					.getInstance(rs, Direction.class, Direction.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean setDirectionActive(Integer directionId, Boolean active) {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		int rowsUpdated = 0;
		PreparedStatement pStatement = null;
		try {
			pStatement = connect.prepareStatement(SQL_SET_DIRECTION_ACTIVE);
			pStatement.setString(1, active.toString());
			pStatement.setInt(2, directionId);
			rowsUpdated = pStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			cm.freeConnection(connect);
		}
		return rowsUpdated > 0;
	}
	@Override
	public List<Direction> getDirectionsByHRId(int hrId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> listOfDirection = new ArrayList<>();

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_DIRECTIONS_BY_HR_ID);) {
			st.setInt(1, hrId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				rs.previous();
				Direction direction = Transformer.getInstance(rs,
						Direction.class, Direction.class);
				listOfDirection.add(direction);
			}
			return listOfDirection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public Direction getDirectionById(Integer id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_DIRECTION_BY_ID)) {
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			return Transformer
					.getInstance(rs, Direction.class, Direction.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean updateDirectionData(Direction direction) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate
		try (PreparedStatement st = conn.prepareStatement(SQL_UPDATE_DIRECTION);) {
			st.setString(1, direction.getName());
			st.setString(2, direction.getDescription());
			st.setString(3, direction.getCodeLang());
			st.setInt(4, direction.getId());
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return (rowsAffected > 0);
	}
	@Override
	public boolean setDirectionHR(Integer directionId, Integer hrId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;// for executeUpdate
		try (PreparedStatement st = conn.prepareStatement(SQL_SET_DIRECTION_HR);) {
			st.setInt(1, hrId);
			st.setInt(2, directionId);
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (rowsAffected > 0);
	}
	@Override
	public Map<Direction, List<TestResults>> getTop10Map() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Map<Direction, List<TestResults>> resultMap = new HashMap<Direction, List<TestResults>>();

		try (PreparedStatement pStatement1 = conn.prepareStatement(SQL_GET_ALL_DIRECTIONS);
				PreparedStatement pStatement2 = conn.prepareStatement(SQL_GET_TOP10_BY_DIRECTION);) {
			ResultSet rs1 = pStatement1.executeQuery();
			List<Direction> directions = Transformer.getListOfInstances(rs1, Direction.class, Direction.class);
			
			for(Direction d : directions) {
				System.out.println("Direction ID = " + d.getId());
				pStatement2.setInt(1, d.getId());
				ResultSet rs2 = pStatement2.executeQuery();
				List<TestResults> list = Transformer.getListOfInstances(rs2, TestResults.class, TestResults.class);
				if(list.isEmpty()) continue;
				System.out.println("Є чуваки в рейтингу з " + d.getName());
				StringBuilder builder = new StringBuilder();
				for(int i = 1; i < list.size(); i++) {
					builder.append(",?");
				}
				builder.append(")");
				System.out.println(SQL_GET_USERS_BY_ID + builder.toString());
				try(PreparedStatement pStatement3 = conn.prepareStatement(SQL_GET_USERS_BY_ID + builder.toString())) {
					for (int i = 0; i < list.size(); i++) {
						pStatement3.setInt((i + 1), list.get(i).getUserId());
					}
					ResultSet rs3 = pStatement3.executeQuery(); // ліст самих чуваків
					List<User> users = Transformer.getListOfInstances(rs3, User.class, CommonUser.class);
					for(TestResults test : list) {
						for (CommonUser user : users) {
							if(test.getUserId().intValue() == user.getId()) {
								test.setTop10UserName(user.getLastName() + " " + user.getFirstName());
								test.setCurrentRate((test.getPoints() * 10) / (test.getTestCount() * 12));
								break;
							}
						}
					}
				}
				resultMap.put(d, list);
			}
			System.out.println("result map directions: " + resultMap.keySet());
			
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Direction> getTeacherDirections(Integer teacherId) throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> listOfDirection = new ArrayList<>();

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_DIRECTION_FOR_TEACHER);) {
			st.setInt(1, teacherId);
			ResultSet rs = st.executeQuery();
			
				listOfDirection = Transformer.getListOfInstances(rs,
						Direction.class, Direction.class);
			
			return listOfDirection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public List<Direction> getAllDirectionsWithInterview() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Direction> listOfDirections = new ArrayList<>();

		try (PreparedStatement statement = conn.prepareStatement(SQL_GET_ALL_DIRECTIONS);
				PreparedStatement statement2 = conn.prepareStatement(SQL_GET_DIRECTION_INTERVIEW)) {
			conn.setAutoCommit(false);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				rs.previous();
				Direction direction = Transformer.getInstance(rs, Direction.class, Direction.class);
				statement2.setInt(1, direction.getId());
				ResultSet resultSet2 = statement2.executeQuery();
				Interview interview = null;
				if(resultSet2.next()){
					resultSet2.previous();
					interview = Transformer.getInstance(resultSet2, Interview.class, Interview.class);
				}
				
				direction.setInterview(interview);
				listOfDirections.add(direction);
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
		return listOfDirections;
	}
	
	@Override
	public List<CourseComment> getCourseComments(Integer directionId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<CourseComment> listOfDirection = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_COMMENTS_FOR_DIRECTION);) {
			st.setInt(1, directionId);
			ResultSet rs = st.executeQuery();
				listOfDirection = Transformer.getListOfInstances(rs,
						CourseComment.class, CourseComment.class);
			
			return listOfDirection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

}
