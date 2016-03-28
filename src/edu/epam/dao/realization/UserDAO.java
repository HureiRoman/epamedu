package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.epam.connection.ConnectionManager;
import edu.epam.constants.RoleType;
import edu.epam.dao.interfaces.IUserDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.exceptions.NoSuchUser;
import edu.epam.exceptions.YouAreNotConfirmed;
import edu.epam.model.CV;
import edu.epam.model.Message;
import edu.epam.persistance.HashUtils;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;
import edu.epam.role.Teacher;
import edu.epam.role.User;

public class UserDAO implements IUserDAO {
	private static UserDAO instance;
	private static final String SQL_GET_ALL_EMPLOYEES = "Select * From users WHERE role='TEACHER' OR role='HR' ";
	private static final String SQL_GET_ID_BY_EMAIL = "Select id From users WHERE email=? ";
	private static final String SQL_ADD_NEW_EMPLOYEE = "INSERT INTO users (email,role,confirm_key) VALUES (?,?,?); ";
	private static final String SQL_UPDATE_EMPLOYEE_PROFILE = "UPDATE users SET fname=?, lname=?, pname=?, password=?, language = ?, isActive = ?, key_used = ? WHERE email = ?; ";
	private static final String SQL_GET_PASSWORD = "SELECT id, role, password, isActive FROM users WHERE email=?";
	private static final String SQL_USER_ACTIVATION = "UPDATE users SET isActive = 'true', key_used = 'true' WHERE email = ? and confirm_key = ?";
	private static final String SQL_CHECK_ACTIVATION = "SELECT * FROM users WHERE email = ? AND isActive = 'true'";
	private static final String SQL_CHECK_KEY_USED = "SELECT * FROM users WHERE confirm_key = ? AND key_used = 'true'";
	private static final String SQL_SEND_MESSAGE = "INSERT INTO messages (sender,receiver, text,isRead, message_time)"
			+ " VALUES(?,?,?,?,?)";
	private static final String SQL_GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
	private static final String SQL_DELETE_USER = "UPDATE users SET isActive='false' WHERE id=? AND role!='ADMIN'";
	private static final String SQL_SHOW_ALL_USERS = "Select * From users WHERE role=?";
	private static final String SQL_GET_USER_BY_CONFIRM_KEY_AND_EMAIL = "SELECT * FROM users WHERE email = ? AND confirm_key = ?";
	private static final String SQL_DELETE_NOT_CONFIRM_USERS_OLDER_THEN_ONE_DAY = "DELETE U, C FROM users U JOIN cv C WHERE U.id = C.id"
			+ " AND reg_date < DATE_SUB(NOW(), INTERVAL 24 HOUR) AND isActive = 'false' AND key_used = 'false'";
	private static final String SQL_CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";

	private static final String SQL_GET_UNREAD_MESSAGE_COUNT = "UPDATE messages SET isRead='false' WHERE sender=? AND receiver=? AND isRead='false'";
	private static final String SQL_SET_USER_ACTIVE = "UPDATE users u  SET u.isActive= ? WHERE u.id=? ; ";
	private static final String SQL_GET_USER_BY_ID = "SELECT *  FROM users u  WHERE  u.id = ?";
	private static final String SQL_UPDATE_USER_DATA = "UPDATE users SET fname = ?, lname = ?, pname = ? WHERE id = ?";
	private static final String SQL_UPDATE_CV_ENTRY = "UPDATE cv SET phone = ?, birth = ?, objective = ?, skills = ?,"
			+ " additional_info = ?, education = ?, english_level = ? WHERE id = ?";

	private static final String SQL_INSERT__DATA_FOR_PASSWORD_RETRIEVAL = "UPDATE users SET key_used='false',confirm_key=? WHERE email=? ";
	private static final String SQL_SET_KEY_USED = "UPDATE users SET key_used='true' WHERE email=?";
	private static final String SQL_CHECK_KEY_BY_EMAIL = "SELECT * FROM users WHERE confirm_key=? AND email=?";
	private static final String SQL_GET_ALL_USERS = "SELECT *  FROM users u";
	private static final String SQL_LEAVE_REVIEW = "INSERT  INTO feedbacks (user_id, message, direction_id)  VALUES (?,?,?);  ";
	private static final String SQL_REMOVE_REVIEW = "DELETE FROM feedbacks WHERE id= ?";
	private static final String SQL_SET_LANG_FOR_USER = "UPDATE users u  SET u.language =  ? WHERE u.id = ?;";

	private static final String SQL_GET_USER_WITH_VK_AT = "SELECT id FROM users u WHERE vk = ?";

	private static final String SQL_GET_ALL_USERS_FOR_ADMIN = "SELECT *  FROM users WHERE role!='ADMIN' AND role!='HR' AND role!='TEACHER' " ;
	private static final String SQL_GET_USER_WITH_FB_AT = "SELECT id FROM users u WHERE fb = ?";


	private UserDAO() {
	}

	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	public boolean insertDataForPasswordRetrieval(String confirmKey,
			String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int updated = 0;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_INSERT__DATA_FOR_PASSWORD_RETRIEVAL)) {

			st.setString(1, confirmKey);
			st.setString(2, email);
			updated = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return updated > 0;
	}

	@Override
	public List<CommonUser> getAllUsers() throws SQLException {
		List<CommonUser> listOfUsers = new ArrayList<CommonUser>();
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_ALL_USERS)) {
			ResultSet rs = st.executeQuery();
			CommonUser user;
			while (rs.next()) {
				rs.previous();
				user = Transformer
						.getInstance(rs, User.class, CommonUser.class);
				listOfUsers.add(user);
			}
			return listOfUsers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public RoleType checkUser(String email, String password)
			throws SQLException, IncorrectPasswordInput, YouAreNotConfirmed,
			NoSuchUser {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		RoleType role = null;
		System.out.println("entered email = " + email + "; password = "
				+ password);
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_PASSWORD);) {
			st.setString(1, email);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				// if (!password.equals(rs.getString("password"))) {
				// System.out.println("incorreect password");
				// throw new IncorrectPasswordInput();
				// }

				if (!HashUtils.passIsCorrect(password,
						rs.getString("password"), email)) {
					System.out.println("incorreect password");
					throw new IncorrectPasswordInput();
				}

				if ("false".equalsIgnoreCase(rs.getString("isActive"))) {
					System.out.println("you are not confirmed");
					throw new YouAreNotConfirmed();
				}

				role = RoleType.valueOf(rs.getString("role").toUpperCase());
				System.out.println("Gonna return ROLE::::::" + role);
				
				return role;
			}
		} finally {
			cm.freeConnection(conn);
		}

		throw new NoSuchUser();
	}

	@Override
	public boolean isEmailExist(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_GET_USER_BY_EMAIL);) {
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
		} finally {
			cm.freeConnection(conn);
		}

		return true;
	}

	@Override
	public Integer getIdByEmail(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_GET_ID_BY_EMAIL);) {
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				return resultSet.getInt("id");
			else return null;
		} finally {
			cm.freeConnection(conn);
		}

	}

	public boolean isUserActive(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_CHECK_ACTIVATION);) {
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				return false;
			}

		} finally {
			cm.freeConnection(conn);
		}
		return true;
	}

	@Override
	public boolean isKeyUsed(String key) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_CHECK_KEY_USED);) {
			preparedStatement.setString(1, key);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				return false;
			}

		} finally {
			cm.freeConnection(conn);
		}
		return true;
	}

	public void changePassword(int id, String password) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_CHANGE_PASSWORD);) {
			preparedStatement.setString(1, password);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
	}

	// RETURN USER ID
	public int userActivation(String email, String key) throws SQLException {
		int result = -1;
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_USER_ACTIVATION);
				PreparedStatement preparedStatement_2 = conn
						.prepareStatement(SQL_GET_USER_BY_CONFIRM_KEY_AND_EMAIL)) {
			preparedStatement_2.setString(1, email);
			preparedStatement_2.setString(2, key);
			ResultSet resultSet = preparedStatement_2.executeQuery();

			if (resultSet.next()) {
				result = Integer.parseInt(resultSet.getString("id"));
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, key);
				preparedStatement.execute();
			}

		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean sendMessage(Message message) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try (PreparedStatement st = conn.prepareStatement(SQL_SEND_MESSAGE);) {

			st.setInt(1, message.getSender());
			st.setInt(2, message.getReceiver());
			st.setString(3, message.getText());
			st.setString(4, String.valueOf(message.getIsRead()).toLowerCase());
			System.out.println("sending data is read = " + message.getIsRead());
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(message.getMessageTime());
			st.setString(5, currentTime);
			System.out.println("is read only = " + conn.isReadOnly());
			System.out.println("time = " + currentTime);
			int countOfMessage = st.executeUpdate();

			return countOfMessage == 1;

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	@Override
	public boolean deleteUsers(String[] id_students) throws SQLException {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_DELETE_USER)) {

			conn.setAutoCommit(false);

			for (int i = 0; i < id_students.length; i++) {
				st.setString(1, id_students[i]);
				st.addBatch();
			}

			st.executeBatch();

			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public List<CommonUser> showAllUsers(String role) throws SQLException {
		List<CommonUser> list_of_users = new ArrayList<CommonUser>();

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_SHOW_ALL_USERS)) {
			st.setString(1, role);
			ResultSet rs = st.executeQuery();
			CommonUser person = null;
			while (rs.next()) {
				rs.previous();
				person = Transformer.getInstance(rs, HR.class, HR.class,
						CommonUser.class);
				if (person != null)
					list_of_users.add(person);
			}
			return list_of_users;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public void deleteNotConfirmUsersOlderThenOneDay() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn
				.prepareStatement(SQL_DELETE_NOT_CONFIRM_USERS_OLDER_THEN_ONE_DAY);) {
			preparedStatement.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public Map<Integer, Integer> getUnreadMessageCount(String[] hr_id_list,
			int user_id) throws SQLException {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		Map<Integer, Integer> mapHrCountUnreadMessages = null;

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_UNREAD_MESSAGE_COUNT);) {

			for (String hr_id : hr_id_list) {
				st.setString(1, hr_id);
				st.setInt(2, user_id);
				st.addBatch();
			}
			int[] counted_rows = st.executeBatch();

			mapHrCountUnreadMessages = new HashMap<>(hr_id_list.length);
			for (int i = 0; i < hr_id_list.length; i++) {
				System.out.println(" hr id = " + hr_id_list[i]
						+ "; Counted rows = " + counted_rows[i]);
				mapHrCountUnreadMessages.put(Integer.valueOf(hr_id_list[i]),
						counted_rows[i]);
			}
		} finally {
			cm.freeConnection(conn);
		}

		return mapHrCountUnreadMessages;
	}

	@Override
	public List<CommonUser> getAllEmployees() throws SQLException {
		List<CommonUser> listOfUsers = new ArrayList<CommonUser>();
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ALL_EMPLOYEES)) {
			ResultSet rs = st.executeQuery();
			CommonUser user;
			while (rs.next()) {
				rs.previous();
				user = Transformer.getInstance(rs, Teacher.class,
						CommonUser.class);
				listOfUsers.add(user);
			}
			return listOfUsers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

	}

	public boolean addEmployee(String email, RoleType roleType, String uuid)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int updated = 0;
		try (PreparedStatement st = conn.prepareStatement(SQL_ADD_NEW_EMPLOYEE)) {

			st.setString(1, email);
			st.setString(2, roleType.name());
			st.setString(3, uuid);
			updated = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return updated > 0;
	}

	public Integer updateEmployeeProfile(User user) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int rowsAffected = 0;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_UPDATE_EMPLOYEE_PROFILE)) {

			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getParentName());
			st.setString(4, user.getPassword());
			st.setString(5, user.getLang() ); 
			st.setString(6, ""+user.getIsActive() );
			st.setString(7, ""+user.getIsKeyUsed() );
			st.setString(8, user.getEmail());
			

			rowsAffected = st.executeUpdate();
			return rowsAffected;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public boolean setUserActive(Integer userId, Boolean active) {
		Connection connect = ConnectionManager.getInstance().getConnection();
		int rowsUpdated = 0;
		PreparedStatement pStatement = null;
		try {
			pStatement = connect.prepareStatement(SQL_SET_USER_ACTIVE);
			pStatement.setString(1, active.toString());
			pStatement.setInt(2, userId);
			rowsUpdated = pStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		ConnectionManager.getInstance().freeConnection(connect);
		return rowsUpdated > 0;
	}

	public User getUserById(Integer userId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		User user = null;
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_USER_BY_ID)) {
			st.setInt(1, userId);
			ResultSet rs1 = st.executeQuery();
			if (rs1.next()) {
				rs1.previous();
				user = Transformer.getInstance(rs1, User.class, User.class,
						CommonUser.class);
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public boolean updateUserWithCV(CommonUser user, CV cv) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_UPDATE_USER_DATA);
				PreparedStatement st2 = conn
						.prepareStatement(SQL_UPDATE_CV_ENTRY)) {
			conn.setAutoCommit(false);

			// Insert to first table
			st1.setString(1, user.getFirstName());
			st1.setString(2, user.getLastName());
			st1.setString(3, user.getParentName());
			st1.setInt(4, user.getId());
			if (st1.executeUpdate() == 1) {
				st2.setString(1, cv.getPhone());
				st2.setDate(2, new Date(cv.getBirth().getTime()));
				st2.setString(3, cv.getObjective());
				st2.setString(4, cv.getSkills());
				st2.setString(5, cv.getAdditionalInfo());
				st2.setString(6, cv.getEducation());
				st2.setString(7, String.valueOf(cv.getEnglishLevel()));
				st2.setInt(8, user.getId());
				if (st2.executeUpdate() == 1) {
					conn.commit();
					conn.setAutoCommit(true);
					return true;
				} else {
					throw new SQLException();
				}
			}

		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	public boolean updateUser(CommonUser user) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_UPDATE_USER_DATA);) {
			// Insert to first table
			st1.setString(1, user.getFirstName());
			st1.setString(2, user.getLastName());
			st1.setString(3, user.getParentName());
			st1.setInt(4, user.getId());
			if (st1.executeUpdate() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	public boolean setKeyUsedByEmail(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn.prepareStatement(SQL_SET_KEY_USED);) {
			// Insert to first table
			st1.setString(1, email);
			if (st1.executeUpdate() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	public boolean checkKey(String key, String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_CHECK_KEY_BY_EMAIL)) {

			st1.setString(1, key);
			st1.setString(2, email);
			ResultSet rs1 = st1.executeQuery();
			if (rs1.next()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	public boolean leaveReview(String message ,Integer userId, Integer directionId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_LEAVE_REVIEW)) {
			st1.setInt(1, userId);
			st1.setString(2, message);
			st1.setInt(3, directionId);
			int updatedRows;
			updatedRows = st1.executeUpdate();
			return updatedRows>0;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}

	public boolean deleteReview(Integer commentId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_REMOVE_REVIEW)) {
			st1.setInt(1, commentId);
			int updatedRows = st1.executeUpdate();
			return updatedRows>0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}
	
	public boolean setLanguage(String lang,Integer userId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_SET_LANG_FOR_USER)) {
			st1.setString(1, lang);
			st1.setInt(2, userId);
			int updatedRows = st1.executeUpdate();
			return updatedRows>0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}
	@Override
	public List<CommonUser> getAllUsersForAdmin() throws SQLException {
		List<CommonUser> listOfUsers = new ArrayList<CommonUser>();
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_ALL_USERS_FOR_ADMIN)) {
			ResultSet rs = st.executeQuery();
			CommonUser user;
			while (rs.next()) {
				rs.previous();
				user = Transformer
						.getInstance(rs, User.class, CommonUser.class);
				listOfUsers.add(user);
			}
			return listOfUsers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	public  Integer loginWithVk(Integer vkSocialID) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer userId = null;
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_GET_USER_WITH_VK_AT)) {
			st1.setInt(1, vkSocialID);
			ResultSet resultSetWithId = st1.executeQuery();
			if(resultSetWithId.next()){
				userId = resultSetWithId.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return userId;
	}

	public Integer loginWithFB(Long fbSocialID) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer userId = null;
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_GET_USER_WITH_FB_AT)) {
			st1.setLong(1, fbSocialID);
			ResultSet resultSetWithId = st1.executeQuery();
			if(resultSetWithId.next()){
				userId = resultSetWithId.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return userId;
	}

}
