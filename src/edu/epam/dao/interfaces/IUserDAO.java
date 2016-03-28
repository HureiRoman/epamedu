package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.epam.constants.RoleType;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.exceptions.NoSuchUser;
import edu.epam.exceptions.YouAreNotConfirmed;
import edu.epam.model.Message;
import edu.epam.role.CommonUser;

public interface IUserDAO {
	
	List<CommonUser> getAllUsers() throws SQLException;
	
	RoleType checkUser(String login, String password) throws SQLException, IncorrectPasswordInput, YouAreNotConfirmed, NoSuchUser;
	boolean isEmailExist(String email) throws SQLException;

	int userActivation(String email, String key) throws SQLException;
	boolean isUserActive(String email) throws SQLException;

	void deleteNotConfirmUsersOlderThenOneDay() throws SQLException;
	boolean sendMessage(Message message) throws SQLException;
	
	boolean deleteUsers(String[] id_students) throws SQLException;

	void changePassword(int id, String pass) throws SQLException;
	
	Map<Integer, Integer> getUnreadMessageCount(String[] hr_id_list, int user_id) throws SQLException;
	

	List<CommonUser> getAllEmployees() throws SQLException;

	Integer getIdByEmail(String email) throws SQLException;
	
	public boolean insertDataForPasswordRetrieval(String confirmKey,String email) 	throws SQLException;
	
	public boolean setKeyUsedByEmail(String email)  throws SQLException;
	
	public boolean checkKey(String key,String email) throws SQLException;

	boolean isKeyUsed(String email) throws SQLException;
	
	public List<CommonUser> getAllUsersForAdmin() throws SQLException;
}
