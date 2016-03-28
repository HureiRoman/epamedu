package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IAdminDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.role.Admin;
import edu.epam.role.CommonUser;

public class AdminDAO implements IAdminDAO {
	private final String SQL_GET_ADMIN_BY_EMAIL = "SELECT * FROM users WHERE email=? ";

	private static AdminDAO instance;

	private AdminDAO() {
	}

	public static AdminDAO getInstance() {
		if (instance == null) {
			instance = new AdminDAO();
		}
		return instance;
	}

	@Override
	public CommonUser enterAdmin(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Admin admin = null;

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ADMIN_BY_EMAIL);) {

			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			admin = Transformer.getInstance(rs, Admin.class, CommonUser.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return admin;
	}

}
