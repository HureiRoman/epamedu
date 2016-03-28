package edu.epam.dao.interfaces;

import java.sql.SQLException;

import edu.epam.role.CommonUser;

public interface IAdminDAO {
	CommonUser enterAdmin(String email) throws SQLException;

}
