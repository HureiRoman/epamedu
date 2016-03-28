package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IGraduateDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.CV;
import edu.epam.model.Direction;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.role.Graduate;

public class GraduateDAO implements IGraduateDAO {
	private final String SQL_GET_GRADUATE_BY_EMAIL = "SELECT * FROM users WHERE email=? ";
	private final String SQL_GET_CV_BY_ID = "SELECT * FROM cv WHERE id=? ";
	private static final String SQL_INITIALIZE_TESTS_FOR_USER = "SELECT * FROM test_results WHERE user_id=?";
	private static final String SQL_GET_DIRECTION_BY_ID = "SELECT * FROM directions WHERE id=?";
	
	private static GraduateDAO instance;

	private GraduateDAO() {
	}

	public static GraduateDAO getInstance() {
		if (instance == null) {
			instance = new GraduateDAO();
		}
		return instance;
	}

	@Override
	public CommonUser enterGraduate(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Graduate graduate = null;
		
		System.out.println("enterGraduate");
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_GRADUATE_BY_EMAIL);
			PreparedStatement st2 = conn.prepareStatement(SQL_GET_CV_BY_ID);
			PreparedStatement st3 = conn.prepareStatement(SQL_INITIALIZE_TESTS_FOR_USER);
			PreparedStatement st4 = conn.prepareStatement(SQL_GET_DIRECTION_BY_ID);) {
			conn.setAutoCommit(false);
				
			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			graduate = Transformer.getInstance(rs, Graduate.class, CommonUser.class);
			
			st2.setInt(1, graduate.getId());
			ResultSet rs2 = st2.executeQuery();
			System.out.println("user id = " + graduate.getId());
			CV cv = Transformer.getInstance(rs2, CV.class, CV.class);
			graduate.setCv(cv);
		
		st3.setInt(1, graduate.getId());
		ResultSet rs3 = st3.executeQuery();
		Map<Direction, TestResults> testsMap = new HashMap<Direction, TestResults>();
		Direction direction = null;
		while(rs3.next()) { //resultset зі всіма тестами юзера
			st4.setInt(1, rs3.getInt("direction_id"));
			ResultSet rs4 = st4.executeQuery();
			if (rs4.next()) {
				rs4.previous();
				direction = Transformer.getInstance(rs4, Direction.class, Direction.class);
				rs3.previous();
				TestResults results = Transformer.getInstance(rs3, TestResults.class, TestResults.class);
				testsMap.put(direction, results);
			}
		}
		graduate.setTestResults(testsMap);
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
		return graduate;
	}
}
