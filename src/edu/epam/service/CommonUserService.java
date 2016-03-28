package edu.epam.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.epam.constants.EFactoryType;
import edu.epam.constants.RoleType;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.exceptions.NoSuchUser;
import edu.epam.exceptions.YouAreNotConfirmed;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.CV;
import edu.epam.model.Message;
import edu.epam.role.CommonUser;
import edu.epam.role.User;

public class CommonUserService {

	public static CommonUser checkLogin(String email, String password)
			throws SQLException, IncorrectPasswordInput, YouAreNotConfirmed,
			NoSuchUser {

		RoleType role = AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getUserDAO().checkUser(email, password);
		switch (role) {
		case TRAINEE: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
					.getTraineeDAO().enterTrainee(email);
		}
		case STUDENT: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
					.getStudentDAO().enterStudent(email);
		}
		case GRADUATE: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
					.getGraduateDAO().enterGraduate(email);
		}
		case ADMIN: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
					.getAdminDAO().enterAdmin(email);
		}
		case HR: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getHrDAO()
					.enterHr(email);
		}
		case TEACHER: {
			return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
					.getTeacherDAO().enterTeacher(email);
		}
		}
		return null;
	}

	public static boolean isUserActive(String email) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.isUserActive(email);
	}

	public static boolean isKeyUsed(String key) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.isKeyUsed(key);
	}

	public static int userActivation(String email, String key)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.userActivation(email, key);
	}

	public static boolean isEmailExist(String email) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.isEmailExist(email);
	}

	public static Integer getIdByEmail(String email) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getIdByEmail(email);
	}

	public static boolean sendMessage(Message message) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.sendMessage(message);

	}

	public static boolean deleteUsers(String[] id_students) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.deleteUsers(id_students);
	}

	public static List<CommonUser> showAllUsers(String role)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.showAllUsers(role);
	}

	public static void deleteNotConfirmUsersOlderThenOneDay()
			throws SQLException {
		AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.deleteNotConfirmUsersOlderThenOneDay();
	}

	public static Map<Integer, Integer> getUnreadMessageCount(
			String[] hr_id_list, int user_id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getUnreadMessageCount(hr_id_list, user_id);
	}

	public static void changePassword(int id, String password)
			throws SQLException {
		AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.changePassword(id, password);
	}

	public static List<CommonUser> getAllEmployees() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getAllEmployees();
	}

	public static boolean addEmployee(String email, RoleType roleType,
			String uuid) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.addEmployee(email, roleType, uuid);
	}

	public static boolean setUserActive(Integer userId, Boolean active) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.setUserActive(userId, active);

	}

	public static Integer updateEmployeeProfile(User user) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.updateEmployeeProfile(user);
	}

	public static User getUserById(Integer userId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getUserById(userId);
	}

	public static boolean updateUserWithCV(CommonUser user, CV cv)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.updateUserWithCV(user, cv);
	}

	public static boolean updateUser(CommonUser user) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.updateUser(user);
	}

	public static boolean insertDataForPasswordRetrieval(String confirmKey,
			String email) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.insertDataForPasswordRetrieval(confirmKey, email);
	}

	public static boolean setKeyUsedByEmail(String email) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.setKeyUsedByEmail(email);
	}

	public static boolean checkKey(String key, String email)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.checkKey(key, email);
	}

	public static List<CommonUser> getAllUsers() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getAllUsers();
	}

	public static boolean leaveReview(String message, Integer userId,
			Integer directionId) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.leaveReview(message, userId, directionId);
	}

	public static boolean deleteReview(Integer commentId) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.deleteReview(commentId);
	}

	public static boolean setLanguage(String lang, Integer userId) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.setLanguage(lang, userId);
	}

	public static Integer loginWithVk(Integer vkSocialID) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.loginWithVk(vkSocialID); // TODO Auto-generated method stub

	}

	public static List<CommonUser> getAllUsersForAdmin() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.getAllUsersForAdmin();
	}

	public static Integer loginWithFB(Long  fbSocialID) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getUserDAO()
				.loginWithFB(fbSocialID);
	}

}
