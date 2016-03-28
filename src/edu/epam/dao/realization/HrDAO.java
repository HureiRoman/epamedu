package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IHrDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Direction;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;

public class HrDAO implements IHrDAO {
	private static final String SQL_GET_HR_FOR_DIRECTION = "SELECT * FROM users u  WHERE u.id = (SELECT recruter_id FROM directions d WHERE d.id=?)";
	private final String SQL_GET_HR_BY_EMAIL = "SELECT * FROM users WHERE email=? ";
	private final String SQL_GET_HR_BY_ID = "SELECT * FROM users WHERE id=?";
	private final String SQL_SELECT_RANDOM_HR = "SELECT * FROM epamlab.users WHERE role='HR' LIMIT 1";
	private final String SQL_SELECT_ALL_HR = "SELECT * FROM epamlab.users WHERE role='HR' AND isActive='true'";
	private final String SQL_GET_ALL_HR_DIRECTIONS = "SELECT * FROM directions WHERE recruter_id=?";
	
	
	private static HrDAO instance;

	private HrDAO() {
	}

	public static HrDAO getInstance() {
		if (instance == null) {
			instance = new HrDAO();
		}
		return instance;
	}

	@Override
	public CommonUser enterHr(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		HR hr = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_HR_BY_EMAIL);) {
		
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		hr = Transformer.getInstance(rs, HR.class, CommonUser.class);
		
		 } catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return hr;
	}

	@Override
	public HR getHRbyId(int hr_id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		HR hr = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_HR_BY_ID);) {
		
		st.setInt(1, hr_id);
		ResultSet rs = st.executeQuery();
		hr = Transformer.getInstance(rs, HR.class, CommonUser.class);
		
		 } catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return hr;
	}

	@Override
	public HR getRandomHr() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		HR hr = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_SELECT_RANDOM_HR);) {
		
		ResultSet rs = st.executeQuery();
		hr = Transformer.getInstance(rs, HR.class, CommonUser.class);
		
		 } catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return hr;
	}

	@Override
	public List<HR> getAllHr() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<HR> hr_list = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_SELECT_ALL_HR);) {
		
		ResultSet rs = st.executeQuery();
		if(rs.next()){
			rs.previous();
			hr_list = Transformer.getListOfInstances(rs, HR.class, CommonUser.class);
		}
		 } catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return hr_list;
	}
	@Override
	public HR getHRofDirection(Integer directionId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		HR hr = null;
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_HR_FOR_DIRECTION);) {
		st.setInt(1, directionId);
		ResultSet rs = st.executeQuery();
		hr = Transformer.getInstance(rs, HR.class, CommonUser.class);
		 } catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return hr;
	}

	@Override
	public List<Direction> getAllHrDirections(int hr_id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		 List<Direction> list_of_direction = null;
		
		 try(PreparedStatement statement = conn.prepareStatement(SQL_GET_ALL_HR_DIRECTIONS);) {
			 	statement.setInt(1, hr_id);
				ResultSet resultSet = statement.executeQuery();
				list_of_direction = Transformer.getListOfInstances(resultSet, Direction.class, Direction.class);
				
			 } catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
			}finally{
				cm.freeConnection(conn);
			}
		return list_of_direction;
		 
	}
}
