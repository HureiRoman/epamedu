package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IPersonalMessageDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Message;

public class PersonalMessageDAO implements IPersonalMessageDAO {
	
	private static final String SQL_GET_MESSAGES_WITH_OFFSET = "SELECT * FROM "
			+ "(SELECT * FROM messages m  WHERE  (m.sender = ? AND m.receiver= ?) OR  (m.sender = ? AND m.receiver= ?)"
			+ " ORDER BY m.id DESC  LIMIT ? OFFSET ?) AS r ORDER BY r.id;";
	private static final String SQL_ADD_MESSAGE = "INSERT INTO messages (sender, receiver, text, message_time) "
			+ "VALUES (?,?,?,?);";
	private static final String SQL_SEND_MESSAGE_TO_INTERVIEW_FOLLOWERS = "INSERT INTO messages (sender, text, message_time, receiver)\n" +
			"SELECT ?, ?, ?, user_id FROM applications WHERE interview_id = ?";
	private static final String SQL_GET_COLOCUTORS_FOR_USER = "  SELECT DISTINCT *  FROM "
			+ "( (SELECT sender,message_time FROM messages m WHERE m.receiver = ? ORDER BY 2 DESC ) "
			+ "  UNION ALL ( SELECT receiver,message_time FROM messages m WHERE m.sender = ? ORDER BY 2 DESC) )"
			+ " AS allColocutors   GROUP BY 1 ORDER BY 2 DESC ; ";
	private static final String SQL_GET_UNREAD_MESSAGEES_COUNT = "SELECT COUNT(id) FROM messages WHERE sender = ? AND receiver=? AND isRead='false'";
	private static final String SQL_GET_LAST_MESSAGE_FOR_DIALOG = "SELECT * FROM messages m WHERE  (m.sender = ? AND m.receiver = ?)  OR (m.sender = ? AND m.receiver = ?) ORDER BY m.message_time DESC LIMIT 1;";
	private static final String SQL_GET_ALL_UNREAD_MESSAGE_COUNT = "SELECT COUNT(id) FROM messages WHERE receiver=? AND isRead='false'";
	private static final String SQL_MARK_MESSAGE_READED = "UPDATE messages m SET m.isRead = 'true' WHERE  m.receiver = ? AND m.sender=? AND m.isRead='false';";
	private static final String SQL_SEND_MESSAGE_TO_NEWS_SUBSCRIBERS_BY_DIRECTION_ID = "INSERT INTO messages (sender, text, message_time, receiver) "
			+ "SELECT ?, ?, ?, trainee_id FROM trainee_subscribe_news WHERE direction_id = ?";

	private static PersonalMessageDAO instance;

	private PersonalMessageDAO() {
	}

	public static PersonalMessageDAO getInstance() {
		if (instance == null) {
			instance = new PersonalMessageDAO();
		}
		return instance;
	}
	
	
	@Override
	public boolean addChatMessageToDB(Message message) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement pStatement = conn.prepareStatement(SQL_ADD_MESSAGE);) {
			pStatement.setInt(1, message.getSender());
			pStatement.setInt(2, message.getReceiver());
			pStatement.setString(3, message.getText()); 
		    pStatement.setTimestamp(4, new Timestamp(message.getMessageTime().getTime()));
		    int added = pStatement.executeUpdate();
		    return added>0;
		} catch (SQLException e) {
		e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
		return false;
	}
	
	@Override
	public List<Message> getMessagesWithOffsetForDialog(Integer userId,
			Integer collocutorId, Integer count , Integer offset) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Message> messages = new ArrayList<Message>();
		try (PreparedStatement pStatement = conn.prepareStatement(SQL_GET_MESSAGES_WITH_OFFSET);) {
			pStatement.setInt(1, userId);
			pStatement.setInt(2, collocutorId);
			pStatement.setInt(3, collocutorId);
			pStatement.setInt(4, userId);
			pStatement.setInt(5, count);
			pStatement.setInt(6, offset);
			ResultSet resultSet = pStatement.executeQuery();
			messages = Transformer.getListOfInstances(resultSet, Message.class, Message.class);
		} catch (Exception e) {
		e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return messages;
	}
	@Override
	public void sendMessageToInterviewFollowers(int hrId, int interviewId, String messageText) throws SQLException
	{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_SEND_MESSAGE_TO_INTERVIEW_FOLLOWERS);) {
			preparedStatement.setInt(1, hrId);
			preparedStatement.setString(2, messageText);
			preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			preparedStatement.setInt(4, interviewId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public void sendMessageToNewsSubscribersByDirectionId(int hrId, int directionId, String messageText) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_SEND_MESSAGE_TO_NEWS_SUBSCRIBERS_BY_DIRECTION_ID);) {
			preparedStatement.setInt(1, hrId);
			preparedStatement.setString(2, messageText);
			preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			preparedStatement.setInt(4, directionId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			cm.freeConnection(conn);
		}
	}


	@Override
	public List<Integer> getListOfColocutorsForUser(Integer id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Integer> result = new ArrayList<Integer>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_COLOCUTORS_FOR_USER);) {
	    	st.setInt(1, id);
	    	st.setInt(2, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}
	
	@Override
	public Integer getCountOfUnreadMessages(Integer userId,Integer colocutorId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int result = 0;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_UNREAD_MESSAGEES_COUNT);) {
	    	st.setInt(1, colocutorId);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();
			rs.next();
			result = rs.getInt(1);
		} finally {
			cm.freeConnection(conn);
		}

		return result;
	}
	@Override
	public Message getLastMessageForDialog(Integer userId, Integer colocutorId) throws Exception {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Message result = new Message();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_LAST_MESSAGE_FOR_DIALOG);) {
	    	st.setInt(1, userId);
	    	st.setInt(2, colocutorId);
			st.setInt(3, colocutorId);
			st.setInt(4, userId);
			ResultSet rs = st.executeQuery();
			result = Transformer.getInstance(rs, Message.class, Message.class);
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}

	
	
	@Override
	public int getAllUnreadMessageCount(int user_id) throws SQLException {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();

		int result = 0;

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ALL_UNREAD_MESSAGE_COUNT);) {
			st.setInt(1, user_id);
			ResultSet rs = st.executeQuery();
			rs.next();
			result = rs.getInt(1);
		} finally {
			cm.freeConnection(conn);
		}

		return result;
	}

	public boolean markMessagesAsReaded(Integer user, Integer collocutor) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		int result = 0;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_MARK_MESSAGE_READED);) {
			st.setInt(1, user);
			st.setInt(2, collocutor);
			result = st.executeUpdate();
		} finally {
			cm.freeConnection(conn);
		}
		return result>0;
	}
	

}
