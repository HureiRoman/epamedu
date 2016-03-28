package edu.epam.dao.interfaces;

import java.sql.SQLException;

import edu.epam.role.CommonUser;

public interface IGraduateDAO {
	CommonUser enterGraduate(String email) throws SQLException;
}
