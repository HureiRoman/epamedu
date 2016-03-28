package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.epam.connection.ConnectionManager;
import edu.epam.constants.RoleType;
import edu.epam.dao.interfaces.ITraineeDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Application;
import edu.epam.model.CV;
import edu.epam.model.Direction;
import edu.epam.model.Interview;
import edu.epam.model.Message;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;

public class TraineeDAO implements ITraineeDAO {
	
	private static TraineeDAO instance;
	
	private final String SQL_GET_TRAINEE_BY_EMAIL = "SELECT * FROM users u LEFT JOIN trainee t ON u.id = t.id WHERE email=? ";
	private final String SQL_INSERT_USER_DATA = "INSERT INTO users (fname, lname, pname, email, role, password, language, reg_date, confirm_key) VALUES (?,?,?,?,?,?,?,?,?)";
	private final String SQL_APPLY_TO_TESTS = "INSERT INTO applications (user_id, interview_id) VALUES (?,?)";
	private final String SQL_SET_TRAINEE_IS_APPLICANT = "UPDATE trainee SET is_aplicant='true' WHERE id=?";
//	private final String SQL_GET_CLEAR_OLD_APPLICATIONS = "DELETE FROM applications WHERE interview_id IN (SELECT interview_id FROM interviews WHERE date_of_testing<CURRENT_TIMESTAMP) ";
	private final String SQL_GET_CLOSEST_TEST_INTERVIEW = "SELECT * FROM interviews i WHERE i.isActive='true' AND i.date_of_testing>CURRENT_TIMESTAMP AND i.interview_id NOT IN (SELECT i2.interview_id FROM interviews i2 WHERE i2.direction_id IN (SELECT direction_id FROM interviews WHERE interview_id IN (SELECT interview_id FROM applications WHERE user_id=?)));";
	private final String SQL_CANCEL_TRAINEE_APPLICATION_ON_TEST = "DELETE FROM applications WHERE interview_id IN (SELECT interview_id FROM interviews WHERE direction_id=?) AND user_id=?";
	private final String SQL_GET_ALL_TRAINEE = "SELECT * FROM epamlab.users u  LEFT JOIN epamlab.trainee t ON t.id=u.id LEFT JOIN epamlab.cv cv ON cv.id=u.id WHERE role='TRAINEE' ";
	private final String SQL_GET_TRAINEE_BY_ID = "SELECT * FROM epamlab.users u  LEFT JOIN epamlab.trainee t ON t.id=u.id LEFT JOIN epamlab.cv cv ON cv.id=u.id WHERE u.role='TRAINEE' AND u.id=? ";
	
	private final String SQL_CHANGE_ROLE_TO_STUDENT="UPDATE users SET role='STUDENT' WHERE role='TRAINEE' AND id=?";
	private final String SQL_DELETE_USER_FROM_TRAINEE="DELETE FROM trainee WHERE id=?";
	private final String SQL_ADD_NEW_STUDENT="INSERT INTO students (id,group_id) VALUES(?,?)";
	private final String SQL_DELETE_DATA_FROM_APPLICATIONS="DELETE FROM applications WHERE user_id=?";
	private final String SQL_GET_LIST_OF_APPLIED_TEST_EVENTS = " SELECT * FROM interviews i WHERE i.interview_id IN (SELECT interview_id FROM applications WHERE user_id=?)";

	private final String SQL_INSERT_FB_TOKEN = "UPDATE users SET fb = ? WHERE id = ?";
	private final String SQL_INSERT_VK_TOKEN = "UPDATE users SET vk = ? WHERE id = ?";

	private final String SQL_INSERT_ADDITIONAL_INFO_IN_TRAINEE = "INSERT INTO trainee (id, is_aplicant, receive_news) VALUES(?, ?, ?)";
	private final String SQL_GET_MESSAGES_TRAINEE_HR = "SELECT * FROM epamlab.messages m WHERE (m.sender = ? AND m.receiver = ?) OR (m.receiver = ? AND m.sender = ?)";
	private final String SQL_SET_MESSAGES_TRAINEE_HR_READED = "UPDATE epamlab.messages SET isRead='true' WHERE id=? AND sender=?";
	private final String SQL_GET_NEW_MESSAGES_TRAINEE_HR = "SELECT * FROM epamlab.messages m WHERE m.id>? AND "
												+ "((m.sender = ? AND m.receiver = ?) OR (m.receiver = ? AND m.sender = ?))";
//	cv table
	private final String SQL_GET_CV_BY_ID = "SELECT * FROM cv WHERE id=? ";
	private final String SQL_INSERT_CV_ENTRY = "INSERT INTO cv (id, phone, birth, objective, skills, additional_info, education, english_level) VALUES (?,?,?,?,?,?,?,?)";
	
//	test_results table
	private final String SQL_PASS_ONE_MORE_TEST = "UPDATE test_results SET test_count=?, points=? WHERE direction_id=? AND user_id=?";
	private final String SQL_PASS_NEW_TEST = "INSERT INTO test_results (direction_id, user_id, test_count, points) VALUES (?,?,?,?)";
	private final String SQL_INITIALIZE_TESTS_FOR_USER = "SELECT * FROM test_results WHERE user_id=?";
	
// tests table
	private final String SQL_CHECK_IF_USER_PASSED_TEST_BEFORE = "SELECT * FROM test_results WHERE direction_id=? AND user_id=?";
	
// directions table
	private final String SQL_GET_DIRECTION_BY_ID = "SELECT * FROM directions WHERE id=?";
	private final String SQL_GET_TRAINEES_BY_APLICATION_DIRECTION_ID = "SELECT * FROM users u " +
			"LEFT JOIN trainee t ON t.id=u.id " +
			"LEFT JOIN cv cv ON cv.id=u.id " +
			"WHERE u.id IN ( SELECT user_id FROM applications a JOIN interviews i on a.interview_id = i.interview_id" +
			" WHERE  u.role = 'TRAINEE' AND i.direction_id = ?)";
	
	private final String SQL_GET_TRAINEES_AMOUNT_BY_APLICATION_DIRECTION_ID = "SELECT COUNT(*) FROM users u " +
			"LEFT JOIN trainee t ON t.id=u.id " +
			"LEFT JOIN cv cv ON cv.id=u.id " +
			"WHERE u.id IN ( SELECT user_id FROM applications a JOIN interviews i on a.interview_id = i.interview_id" +
			" WHERE  u.role = 'TRAINEE' AND i.direction_id = ?)";
	
	private final String SQL_GET_TRAINEES_PORTION_BY_APLICATION_DIRECTION_ID = "SELECT * FROM users u " +
			"LEFT JOIN trainee t ON t.id=u.id " +
			"LEFT JOIN cv cv ON cv.id=u.id " +
			"WHERE u.id IN ( SELECT user_id FROM applications a JOIN interviews i on a.interview_id = i.interview_id" +
			" WHERE  u.role = 'TRAINEE' AND i.direction_id = ?) ORDER BY id LIMIT ? OFFSET ?";


	private final String SQL_GET_TRAINEES_BY_INTERVIEW_ID = "SELECT U.id as id, interview_id, fname, lname, pname, email, " +
			"role, password, isActive, 'language', confirm_key, reg_date, key_used, is_aplicant, receive_news, " +
			"phone, birth, objective, skills, additional_info, education, english_level, vk, fb FROM applications A join users U " +
			"on A.user_id = U.id join trainee T on U.id = T.id JOIN cv C ON U.id = C.id WHERE  U.role = 'TRAINEE' AND interview_id = ?";

	private final String SQL_GET_TRAINEE_SUBSCRIBES_TO_NEWS = "SELECT direction_id FROM trainee_subscribe_news WHERE trainee_id=?";
	private final String SQL_SET_TRAINEE_SUBSCRIBE_GET_NEWS = "INSERT INTO trainee_subscribe_news (trainee_id,direction_id) VALUES(?,?)";
	private final String SQL_REMOVE_TRAINEE_SUBSCRIBE_GET_NEWS = "DELETE FROM trainee_subscribe_news WHERE trainee_id = ? AND direction_id = ?";
	private final String SQL_GET_DRECTION_ID_BY_INTERVIEW = "SELECT direction_id FROM interviews WHERE interview_id=?";


	private final String SQL_GET_TRAINEES_SUBSCRIBED_TO_DIRECTION = "SELECT * FROM trainee_subscribe_news S LEFT JOIN users U ON S.trainee_id = U.id JOIN trainee T ON T.id = U.id" +
			" JOIN cv C ON C.id = U.id WHERE direction_id = ?;";
	
	private final String SQL_SEARCH_TRAINEE = "SELECT * FROM epamlab.users u  LEFT JOIN epamlab.trainee t ON t.id=u.id LEFT JOIN epamlab.cv cv ON cv.id=u.id WHERE role='TRAINEE' AND (u.fname REGEXP ? OR  u.lname REGEXP ?)";

	private TraineeDAO() {
		
	}

	public static TraineeDAO getInstance() {
		if (instance == null) {
			instance = new TraineeDAO();
		}
		return instance;
	}


	@Override
	public CommonUser enterTrainee(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Trainee trainee = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_TRAINEE_BY_EMAIL);
			PreparedStatement st2 = conn.prepareStatement(SQL_GET_CV_BY_ID);
			PreparedStatement st3 = conn.prepareStatement(SQL_INITIALIZE_TESTS_FOR_USER);
			PreparedStatement st4 = conn.prepareStatement(SQL_GET_DIRECTION_BY_ID);) {
		conn.setAutoCommit(false);
			
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		trainee = Transformer.getInstance(rs, Trainee.class, Trainee.class, CommonUser.class);
		
		st2.setInt(1, trainee.getId());
		ResultSet rs2 = st2.executeQuery();
		 System.out.println("user id = " + trainee.getId());
		CV cv = Transformer.getInstance(rs2, CV.class, CV.class);
		trainee.setCv(cv);
		
		st3.setInt(1, trainee.getId());
		ResultSet rs3 = st3.executeQuery();
		Map<Direction, TestResults> testsMap = new HashMap<Direction, TestResults>();
		Direction direction = null;
		while(rs3.next()) { //resultset зі всіма тестами юзера
			st4.setInt(1, rs3.getInt("direction_id"));
			ResultSet rs4 = st4.executeQuery(); // TODO use batch? - get direction by id
			if (rs4.next()) {
				rs4.previous();
				direction = Transformer.getInstance(rs4, Direction.class, Direction.class);
				rs3.previous();
				TestResults results = Transformer.getInstance(rs3, TestResults.class, TestResults.class);
				testsMap.put(direction, results);
			}
		}
		trainee.setTestResults(testsMap);
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
		return trainee;
	}

	public void insertAdditionalInfoInTrainee(int id, boolean isApplicant, boolean receiveNews) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st = conn.prepareStatement(SQL_INSERT_ADDITIONAL_INFO_IN_TRAINEE);){
			st.setInt(1, id);
			st.setString(2, Boolean.toString(isApplicant));
			st.setString(3, Boolean.toString(receiveNews));
			st.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
	}



	public Integer insertNewTrainee(Trainee trainee) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer newUserId=null;
		try(PreparedStatement st1 = conn.prepareStatement(SQL_INSERT_USER_DATA,PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement st2 = conn.prepareStatement(SQL_INSERT_CV_ENTRY)) {
		conn.setAutoCommit(false);

		// Insert to first table
		st1.setString(1, trainee.getFirstName());
		st1.setString(2, trainee.getLastName());
		st1.setString(3, trainee.getParentName());
		st1.setString(4, trainee.getEmail());
		st1.setString(5, RoleType.TRAINEE.toString());
		st1.setString(6, trainee.getPassword());
		st1.setString(7, trainee.getLang());
		st1.setTimestamp(8, new Timestamp(trainee.getRegistration_date().getTime()));
		st1.setString(9, trainee.getConfirm_key());

		if (st1.executeUpdate() == 1) {
			ResultSet generatedKeys = st1.getGeneratedKeys();
			if(generatedKeys.next()){
				newUserId  = generatedKeys.getInt(1);
			}
			st2.setInt(1, newUserId); // TODO де моделька trainee отримує id? Це треба робити тут 
			st2.setString(2, trainee.getCv().getPhone());
			st2.setDate(3, new Date(trainee.getCv().getBirth().getTime()));
			st2.setString(4, trainee.getCv().getObjective());
			st2.setString(5, trainee.getCv().getSkills());
			st2.setString(6, trainee.getCv().getAdditionalInfo());
			st2.setString(7, trainee.getCv().getEducation());
			st2.setString(8, String.valueOf(trainee.getCv().getEnglishLevel()));
			if (st2.executeUpdate() == 1) {
				conn.commit();
				conn.setAutoCommit(true);
				return newUserId;
			}else{
				conn.rollback();// ПОясніть мені код пацани!!!!
				conn.setAutoCommit(true);
			}
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

		conn.rollback();
		conn.setAutoCommit(true);
		return newUserId;
	}

	@Override
	public boolean applyToTest(Application application) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result_of_sending = false;
		try(PreparedStatement st = conn.prepareStatement(SQL_APPLY_TO_TESTS);
				PreparedStatement st2 = conn.prepareStatement(SQL_SET_TRAINEE_IS_APPLICANT);
				PreparedStatement st3 = conn.prepareStatement(SQL_GET_DRECTION_ID_BY_INTERVIEW);
					PreparedStatement st4 = conn.prepareStatement(SQL_REMOVE_TRAINEE_SUBSCRIBE_GET_NEWS);) {
			conn.setAutoCommit(false);
			st.setInt(1, application.getUserId());
			st.setInt(2, application.getInterviewId());
			st.executeUpdate();	
			
			st2.setInt(1, application.getUserId());
			st2.executeUpdate();
			//DELETE FROM Subscribe news table
			st3.setInt(1, application.getInterviewId());
			ResultSet resultSet = st3.executeQuery();
			if(resultSet.next()){
				int directionId = resultSet.getInt(1);
				st4.setInt(1, application.getUserId());
				st4.setInt(2, directionId);
				st4.executeUpdate();
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			result_of_sending = true;
		}catch(SQLException e){
			e.printStackTrace();
			conn.rollback();
			conn.setAutoCommit(true);
			throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		
		return result_of_sending;
	}

	@Override
	public List<Interview> getClosestTests(int user_id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Interview> list_of_interview = null;
		System.out.println("here");
//		PreparedStatement statement1 = conn.prepareStatement(SQL_GET_CLEAR_OLD_APPLICATIONS)
		try(PreparedStatement statement2 = conn.prepareStatement(SQL_GET_CLOSEST_TEST_INTERVIEW);
				PreparedStatement statement3 = conn.prepareStatement(SQL_GET_DIRECTION_BY_ID);) {
			conn.setAutoCommit(false);
			
//			statement1.executeUpdate();
			
			statement2.setInt(1, user_id);
			ResultSet resultSet = statement2.executeQuery();
			if(resultSet.next()){
				resultSet.previous();
				list_of_interview = new ArrayList<>();
				System.out.println("creating array Inteviews");
				
				while(resultSet.next()){
					resultSet.previous();
					Interview interview;
					interview = Transformer.getInstance(resultSet, Interview.class, Interview.class);
					
					statement3.setInt(1, interview.getDirectionId());
					ResultSet resultSet3 = statement3.executeQuery();
					Direction direction = Transformer.getInstance(resultSet3, Direction.class, Direction.class);
					
					interview.setDirectionInfo(direction);
					list_of_interview.add(interview);
				}
			}	
			conn.commit();
			conn.setAutoCommit(true);	
		}catch(Exception e){
			e.printStackTrace();
			conn.rollback();
			conn.setAutoCommit(true);
			throw new SQLException();
			
		}finally{
			cm.freeConnection(conn);
		}
		
		return list_of_interview;
	}

	
	public boolean doStudent(Integer traineeId,Integer groupId) throws SQLException {
		
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
			try(PreparedStatement st = conn.prepareStatement(SQL_CHANGE_ROLE_TO_STUDENT);
			 PreparedStatement st2 = conn.prepareStatement(SQL_DELETE_USER_FROM_TRAINEE);
			PreparedStatement st3 = conn.prepareStatement(SQL_ADD_NEW_STUDENT);)
			{
			
				conn.setAutoCommit(false);
				
					st.setInt(1,traineeId);
					
					st2.setInt(1,traineeId);

					st3.setInt(1,traineeId);
					st3.setInt(2,groupId);
				
				st.executeUpdate();
				st2.executeUpdate();
				st3.executeUpdate();
			
				conn.commit();
				conn.setAutoCommit(true);	
				return true;
			 } catch (Exception e) {
					conn.rollback();
					conn.setAutoCommit(true);
					 e.printStackTrace();
						throw new SQLException();
				}finally{
					cm.freeConnection(conn);
				}
	}
	
	@Override
	public List<Interview> getTraineeAppliedTestsEvents(int user_id)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Interview> list_of_interview = null;
//		PreparedStatement statement1 = conn.prepareStatement(SQL_GET_CLEAR_OLD_APPLICATIONS);
		try(PreparedStatement statement2 = conn.prepareStatement(SQL_GET_LIST_OF_APPLIED_TEST_EVENTS);
				PreparedStatement statement3 = conn.prepareStatement(SQL_GET_DIRECTION_BY_ID);) {
			conn.setAutoCommit(false);
			
//			statement1.executeUpdate();
			
			statement2.setInt(1, user_id);
			ResultSet resultSet = statement2.executeQuery();
			
			if(resultSet.next()){
				resultSet.previous();
				list_of_interview = new ArrayList<>();
				System.out.println("creating array Applied Inteviews");
				
				while(resultSet.next()){
					resultSet.previous();
					Interview interview;
					interview = Transformer.getInstance(resultSet, Interview.class, Interview.class);
					
					statement3.setInt(1, interview.getDirectionId());
					ResultSet resultSet3 = statement3.executeQuery();
					Direction direction = Transformer.getInstance(resultSet3, Direction.class, Direction.class);
					
					interview.setDirectionInfo(direction);
					list_of_interview.add(interview);
				}
			}	
			
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();	
			
		} finally {
			cm.freeConnection(conn);
		}
		
		return list_of_interview;
	}


	@Override
	public List<Message> getChattingWithHR(int logined_user, int hr_id)
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Message> list_of_messages = null;
		
			try(PreparedStatement st = conn.prepareStatement(SQL_GET_MESSAGES_TRAINEE_HR);
					PreparedStatement st2 = conn.prepareStatement(SQL_SET_MESSAGES_TRAINEE_HR_READED);) {
				conn.setAutoCommit(false);
				st.setInt(1, logined_user);
				st.setInt(2, hr_id);
				st.setInt(3, logined_user);
				st.setInt(4, hr_id);
				ResultSet rs = st.executeQuery();
				
				try {
					list_of_messages = Transformer.getListOfInstances(rs, Message.class, Message.class);
					
					for(Message message : list_of_messages){
						st2.setInt(1, message.getId());
						st2.setInt(2, hr_id);
						st2.addBatch();
					}
					st2.executeBatch();
					
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
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
		
		
		return list_of_messages;
	}


	public boolean passNewTest(CommonUser user, Direction direction) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		try(PreparedStatement st1 = conn.prepareStatement(SQL_CHECK_IF_USER_PASSED_TEST_BEFORE);
				PreparedStatement st2 = conn.prepareStatement(SQL_PASS_ONE_MORE_TEST);
				PreparedStatement st3 = conn.prepareStatement(SQL_PASS_NEW_TEST);) {
			st1.setInt(1, direction.getId());
			st1.setInt(2, user.getId());
			ResultSet rs = st1.executeQuery();
			if (rs.next()) {
				st2.setInt(1, user.getTestResults().get(direction).getTestCount());
				st2.setInt(2, user.getTestResults().get(direction).getPoints());
				st2.setInt(3, direction.getId());
				st2.setInt(4, user.getId());
				st2.executeUpdate();	
				result = true;
			}
			else {
				st3.setInt(1, direction.getId());
				st3.setInt(2, user.getId());
				st3.setInt(3, user.getTestResults().get(direction).getTestCount());
				st3.setInt(4, user.getTestResults().get(direction).getPoints());
				
				st3.executeUpdate();	
				result = true;
			}
		} finally {
			cm.freeConnection(conn);
		}
		
		return result;
	}


	@Override
	public List<Message> getNewMessagesTraineeHR(int last_message_id, int user_id, int hr_id )
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Message> list_of_messages = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_NEW_MESSAGES_TRAINEE_HR);
				PreparedStatement st2 = conn.prepareStatement(SQL_SET_MESSAGES_TRAINEE_HR_READED);) {
			conn.setAutoCommit(false);
			st.setInt(1, last_message_id);
			st.setInt(2, user_id);
			st.setInt(3, hr_id);
			st.setInt(4, user_id);
			st.setInt(5, hr_id);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				rs.previous();
				try {
					list_of_messages = Transformer.getListOfInstances(rs, Message.class, Message.class);
					
					for(Message message : list_of_messages){
						st2.setInt(1, message.getId());
						st2.setInt(2, hr_id);
						st2.addBatch();
					}
					st2.executeBatch();
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
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
		
		return list_of_messages;
	}


	public List<Trainee> getTraineesByApplicationDirectionId(int directionId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> trainees = new ArrayList<>();
		try(PreparedStatement ps = conn.prepareStatement(SQL_GET_TRAINEES_BY_APLICATION_DIRECTION_ID);) {
			ps.setInt(1, directionId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				rs.previous();
				Trainee trainee = Transformer.getInstance(rs, Trainee.class, Trainee.class, CommonUser.class);
				rs.previous();
				CV cv = Transformer.getInstance(rs, CV.class, CV.class);
				trainee.setCv(cv);
				trainees.add(trainee);
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return trainees;
	}
	
	public Integer getTraineesAmountByApplicationDirectionId(int directionId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement ps = conn.prepareStatement(SQL_GET_TRAINEES_AMOUNT_BY_APLICATION_DIRECTION_ID);) {
			ps.setInt(1, directionId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		
		return 0;
	}

	public List<Trainee> getTraineesSubscribedToDirection(int directionId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> trainees = new ArrayList<>();
		try(PreparedStatement ps = conn.prepareStatement(SQL_GET_TRAINEES_SUBSCRIBED_TO_DIRECTION);) {
			ps.setInt(1, directionId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				rs.previous();
				Trainee trainee = Transformer.getInstance(rs, Trainee.class, Trainee.class, CommonUser.class);
				rs.previous();
				CV cv = Transformer.getInstance(rs, CV.class, CV.class);
				trainee.setCv(cv);
				trainees.add(trainee);
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return trainees;
	}

	@Override
	public void setVKId(Integer traineeId, String vkId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st = conn.prepareStatement(SQL_INSERT_VK_TOKEN);){
			st.setString(1, vkId);
			st.setInt(2, traineeId);
			st.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public void setFBId(Integer traineeId, String fbId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		try(PreparedStatement st = conn.prepareStatement(SQL_INSERT_FB_TOKEN);){
			st.setString(1, fbId);
			st.setInt(2, traineeId);
			st.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public boolean cancelApplicationOnTest(int directionId, int userId)
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_CANCEL_TRAINEE_APPLICATION_ON_TEST);) {
			statement.setInt(1, directionId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		
			result=true;
		} catch(Exception e){
			e.printStackTrace();
			return result;

		} finally {
			cm.freeConnection(conn);
		}
		
		return result;
	}


	public List<Trainee> getTraineesByInterviewId(int interviewId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> trainees = new ArrayList<>();
		try(PreparedStatement ps = conn.prepareStatement(SQL_GET_TRAINEES_BY_INTERVIEW_ID);) {
			ps.setInt(1, interviewId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				rs.previous();
				Trainee trainee = Transformer.getInstance(rs, Trainee.class, Trainee.class, CommonUser.class);
				rs.previous();
				CV cv = Transformer.getInstance(rs, CV.class, CV.class);
				trainee.setCv(cv);
				trainees.add(trainee);
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return trainees;
	}

	@Override
	public List<Trainee> getAllTrainee() throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> list_of_trainee = null;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_GET_ALL_TRAINEE);) {
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet .next()){
				list_of_trainee = new ArrayList<Trainee>();
				resultSet.previous();
				
				while(resultSet.next()){
					resultSet.previous();
					
					Trainee trainee = Transformer.getInstance(resultSet, Trainee.class, Trainee.class, CommonUser.class);
						resultSet.previous();
					CV cv = Transformer.getInstance(resultSet, CV.class, CV.class);
					trainee.setCv(cv);
					list_of_trainee.add(trainee);
				}
				
			}
		} catch(Exception e){
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		
		
		return list_of_trainee;
	}

	@Override
	public Trainee getTraineeById(int trainee_id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Trainee trainee = null;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_GET_TRAINEE_BY_ID);) {
			
			statement.setInt(1, trainee_id);
			ResultSet resultSet = statement.executeQuery();
			
			trainee = Transformer.getInstance(resultSet, Trainee.class, Trainee.class, CommonUser.class);
			resultSet.previous();
			CV cv = Transformer.getInstance(resultSet, CV.class, CV.class);
			trainee.setCv(cv);
				
		} catch(Exception e){
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		return trainee;
	}

	@Override
	public List<Integer> getListOfSubscribedToNewsDirections(int userId)
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Integer> listOfSubscribe = null;
		try(PreparedStatement preparedstatement = conn.prepareStatement(SQL_GET_TRAINEE_SUBSCRIBES_TO_NEWS);) {
			preparedstatement.setInt(1, userId);
			ResultSet resultSet = preparedstatement.executeQuery();
			if(resultSet.next()){
				resultSet.previous();
				listOfSubscribe = new ArrayList<Integer>();
				while (resultSet.next()) {
					listOfSubscribe.add(resultSet.getInt(1));
				}
			}	
		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return listOfSubscribe;
	}

	@Override
	public boolean subscribeToGetNews(int trainee_id,int idDirection, boolean active)
			throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		String statement = null;
		
		if(active == true){
			statement = SQL_SET_TRAINEE_SUBSCRIBE_GET_NEWS;
			System.out.println("SQL_SET_TRAINEE_SUBSCRIBE_GET_NEWS");
		}else{
			statement = SQL_REMOVE_TRAINEE_SUBSCRIBE_GET_NEWS;
			System.out.println("SQL_REMOVE_TRAINEE_SUBSCRIBE_GET_NEWS");
		}
		System.out.println("trainee id = " + trainee_id + " direction id " + " active = " + active);
		try(PreparedStatement preparedstatement = conn.prepareStatement(statement);) {
			preparedstatement.setInt(1, trainee_id);
			preparedstatement.setInt(2, idDirection);
			int effectedRows = preparedstatement.executeUpdate();
			if(effectedRows > 0){
				result = true;
			}	
		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}
	
	public List<Trainee> getPortionOfTraineesByApplicationDirectionId(
			int directionId, int start, int quantity) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> trainees = new ArrayList<>();
		try(PreparedStatement ps = conn.prepareStatement(SQL_GET_TRAINEES_PORTION_BY_APLICATION_DIRECTION_ID);) {
			ps.setInt(1, directionId);
			ps.setInt(2, quantity);
			ps.setInt(3, start);
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				rs.previous();
				Trainee trainee = Transformer.getInstance(rs, Trainee.class, Trainee.class, CommonUser.class);
				rs.previous();
				CV cv = Transformer.getInstance(rs, CV.class, CV.class);
				trainee.setCv(cv);
				trainees.add(trainee);
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new SQLException();

		} finally {
			cm.freeConnection(conn);
		}
		return trainees;
	}

	@Override
	public List<Trainee> searchTrainee(String chooseBy) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Trainee> list_of_trainee = null;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_SEARCH_TRAINEE);) {
			
			statement.setString(1, chooseBy);
			statement.setString(2, chooseBy);
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet .next()){
				list_of_trainee = new ArrayList<Trainee>();
				resultSet.previous();
				
				while(resultSet.next()){
					resultSet.previous();
					
					Trainee trainee = Transformer.getInstance(resultSet, Trainee.class, Trainee.class, CommonUser.class);
						resultSet.previous();
					CV cv = Transformer.getInstance(resultSet, CV.class, CV.class);
					trainee.setCv(cv);
					list_of_trainee.add(trainee);
				}
				
			}
		} catch(Exception e){
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		
		
		return list_of_trainee;
	}
}
