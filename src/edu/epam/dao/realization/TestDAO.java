package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.html.HtmlEscapers;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.ITestDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Direction;
import edu.epam.model.Test;
import edu.epam.role.CommonUser;

public class TestDAO implements ITestDAO {
	private static final String SQL_GET_TESTS_BY_DIRECTION_ID = "SELECT * FROM tests WHERE direction_id=?";
	private static final String SQL_GET_TESTS_PORTION_BY_DIRECTION_ID = "SELECT * FROM tests WHERE direction_id=? ORDER BY id LIMIT ? OFFSET ?";
	private static final String SQL_GET_DIRECTION_ID_BY_NAME = "SELECT id FROM directions WHERE name=?";
	private static final String SQL_GET_ALL_DIRECTIONS = "SELECT * FROM directions";
	private static final String SQL_GET_TESTS_BY_ID = "SELECT * FROM tests WHERE id=? ";
	private static final String SQL_GET_USER_POSITION_BY_DIRECTION = "SELECT rank FROM (SELECT @rank:=@rank+1 AS rank, direction_id, user_id FROM test_results, (SELECT @rank := 0) r WHERE direction_id=? ORDER BY points/test_count DESC ) t WHERE t.user_id=?";
	private static final String SQL_CREATE_NEW_TEST = "INSERT INTO tests (question, code, answer1, answer2, answer3, answer4, correct, direction_id) VALUES (?,?,?,?,?,?,?,?)";
	private static final String SQL_EDIT_TEST = "UPDATE tests SET question=?, code=?, answer1=?, answer2=?, answer3=?, answer4=?, correct=?, direction_id=? WHERE id=?";
	private static final String SQL_REMOVE_TEST = "DELETE FROM tests WHERE id=?";
	
	private static TestDAO instance;

	private TestDAO() {

	}

	public static TestDAO getInstance() {
		if (instance == null) {
			instance = new TestDAO();
		}
		return instance;
	}
	
	@Override
	public List<Test> getTestsForDirection(String direction)
			throws SQLException {
		List<Test> resultList = null;
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st1 = conn.prepareStatement(SQL_GET_DIRECTION_ID_BY_NAME);
				PreparedStatement st2 = conn.prepareStatement(SQL_GET_TESTS_BY_DIRECTION_ID);){
			st1.setString(1, direction);
			ResultSet rs1 = st1.executeQuery();
			if (rs1.next()) {
				st2.setInt(1, rs1.getInt("id"));
				ResultSet resultSet = st2.executeQuery();
				try {
					resultList = Transformer.getListOfInstances(resultSet, Test.class, Test.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new SQLException();
				}
			}

		} finally {
			cm.freeConnection(conn);
		}
		return resultList;
	}

	private void checkForPunctuation(Test test) {
		test.setQuestion(test.getQuestion().replaceAll("([<])", "&#60;"));
		test.setQuestion(test.getQuestion().replaceAll("([>])", "&#62;"));
	    test.setQuestion(test.getQuestion().replaceAll("&", "&amp;"));
		test.setQuestion(test.getQuestion().replaceAll("\"", "&quot;"));
		if (test.getCode() != null) {
			test.setCode(test.getCode().replaceAll("([<])", "&#60;"));
			test.setCode(test.getCode().replaceAll("([>])", "&#62;"));
			test.setCode(test.getCode().replaceAll("&", "&amp;"));
			test.setCode(test.getCode().replaceAll("\"", "&quot;"));
		}
		test.setAnswer1(test.getAnswer1().replaceAll("([<])", "&#60;"));
		test.setAnswer1(test.getAnswer1().replaceAll("([>])", "&#62;"));
		test.setAnswer1(test.getAnswer1().replaceAll("&", "&amp;"));
		test.setAnswer1(test.getAnswer1().replaceAll("\"", "&quot;"));
		test.setAnswer2(test.getAnswer2().replaceAll("([<])", "&#60;"));
		test.setAnswer2(test.getAnswer2().replaceAll("([>])", "&#62;"));
		test.setAnswer2(test.getAnswer2().replaceAll("&", "&amp;"));
		test.setAnswer2(test.getAnswer2().replaceAll("\"", "&quot;"));
		test.setAnswer3(test.getAnswer3().replaceAll("([<])", "&#60;"));
		test.setAnswer3(test.getAnswer3().replaceAll("([>])", "&#62;"));
		test.setAnswer3(test.getAnswer3().replaceAll("&", "&amp;"));
		test.setAnswer3(test.getAnswer3().replaceAll("\"", "&quot;"));
		test.setAnswer4(test.getAnswer4().replaceAll("([<])", "&#60;"));
		test.setAnswer4(test.getAnswer4().replaceAll("([>])", "&#62;"));
		test.setAnswer4(test.getAnswer4().replaceAll("&", "&amp;"));
		test.setAnswer4(test.getAnswer4().replaceAll("\"", "&quot;"));
	}
	
	
	@Override
	public List<Direction> getAllDirections() throws SQLException {
		List<Direction> resultList = null;
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st = conn.prepareStatement(SQL_GET_ALL_DIRECTIONS);){
			ResultSet rs = st.executeQuery();
			resultList = Transformer.getListOfInstances(rs, Direction.class, Direction.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return resultList;
	}
	@Override
	public List<Test> getTestsById(List<Integer> testsInt) throws SQLException {
		List<Test> resultList = null;
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < testsInt.size(); i++) {
			builder.append("OR id=? ");
		}
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_TESTS_BY_ID + builder.toString());){
			for(int i = 1; i <= testsInt.size(); i++) {
				st.setInt(i, testsInt.get(i - 1));
			}
			ResultSet rs = st.executeQuery();
			resultList = Transformer.getListOfInstances(rs, Test.class, Test.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return resultList;
	}
	@Override
	public Test getTestById(int id) throws SQLException {
		Test result = null;
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_TESTS_BY_ID);){
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			result = Transformer.getInstance(rs, Test.class, Test.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}
	@Override
	public Integer getUserPosition(CommonUser user, Direction direction) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement pStatement = conn.prepareStatement(SQL_GET_USER_POSITION_BY_DIRECTION)) {
			pStatement.setInt(1, direction.getId());
			pStatement.setInt(2, user.getId());
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return null;
	}
	@Override
	public Integer insertNewTest(Test test) throws SQLException {
		checkForPunctuation(test);
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_CREATE_NEW_TEST, PreparedStatement.RETURN_GENERATED_KEYS)) {
		
			st1.setString(1, test.getQuestion());
			st1.setString(2, test.getCode());
			st1.setString(3, test.getAnswer1());
			st1.setString(4, test.getAnswer2());
			st1.setString(5, test.getAnswer3());
			st1.setString(6, test.getAnswer4());
			st1.setInt(7, test.getCorrect());
			st1.setInt(8, test.getDirectionId());
			
			Integer updated = st1.executeUpdate();
			   if(updated > 0) {
					ResultSet generatedKeys = st1.getGeneratedKeys();
					if(generatedKeys.next()){
						return generatedKeys.getInt(1);
					}
			   }
			   return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public Test editTest(Test test) throws SQLException {
		checkForPunctuation(test);
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_EDIT_TEST)) {
		
			st1.setString(1, test.getQuestion());
			st1.setString(2, test.getCode());
			st1.setString(3, test.getAnswer1());
			st1.setString(4, test.getAnswer2());
			st1.setString(5, test.getAnswer3());
			st1.setString(6, test.getAnswer4());
			st1.setInt(7, test.getCorrect());
			st1.setInt(8, test.getDirectionId());
			st1.setInt(9, test.getId());
			
			Integer updated = st1.executeUpdate();
			   if(updated > 0) {
				   return test;
			   }
			   return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public Integer removeTest(Test test) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_REMOVE_TEST)) {
			st1.setInt(1, test.getId());
			return st1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public List<Test> getTestsPortionForDirection(String direction, int start,
			int quantity) throws SQLException {
		List<Test> resultList = null;
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st1 = conn.prepareStatement(SQL_GET_DIRECTION_ID_BY_NAME);
				PreparedStatement st2 = conn.prepareStatement(SQL_GET_TESTS_PORTION_BY_DIRECTION_ID);){
			st1.setString(1, direction);
			ResultSet rs1 = st1.executeQuery();
			if (rs1.next()) {
				st2.setInt(1, rs1.getInt("id"));
				st2.setInt(2, quantity);
				st2.setInt(3, start);
				ResultSet resultSet = st2.executeQuery();
				try {
					resultList = Transformer.getListOfInstances(resultSet, Test.class, Test.class);
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}

		} finally {
			cm.freeConnection(conn);
		}
		return resultList;
	}
}
