package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.epam.chat.ChatMessage;
import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IChatMessageDAO;
import edu.epam.dao.transfer.Transformer;

public class ChatMessageDAO implements IChatMessageDAO {
	private static final String SQL_ADD_CHAT_MESSAGE = "INSERT INTO chat_messages ( sender, text,groupId,received)  VALUES (?, ?, ?, ?);";
	private static final String SQL_GET_CHAT_MESSAGES_WITH_OFFSET = "SELECT * FROM (SELECT * FROM chat_messages cm WHERE cm.groupId = ? ORDER BY cm.id DESC  LIMIT ? OFFSET ?) AS r ORDER BY r.id;";
	
	private static final String SQL_GET_STUDENT_ID_OF_GROUP = "SELECT id FROM students WHERE group_id = ?";
	private static final String SQL_SEND_MESSAGE_TO_GROUP_OF_STUDENTS = "INSERT INTO messages (sender, receiver, text, message_time) VALUES (?,?,?,?)";
	
	private static ChatMessageDAO instance;

	private ChatMessageDAO() {
	} 

	public static ChatMessageDAO getInstance() {
	        if (instance == null) {
	            instance = new ChatMessageDAO();
	        }
	        return instance;
	    }

	@Override
	public boolean addChatMessageToDB(ChatMessage message) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement pStatement = conn.prepareStatement(SQL_ADD_CHAT_MESSAGE);) {
			pStatement.setInt(1, message.getSender());
			pStatement.setString(2, message.getMessage()); 
			pStatement.setInt(3, message.getGroupId());
		    pStatement.setTimestamp(4, new Timestamp(message.getReceived().getTime()));
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
	public List<ChatMessage> getMessagesWithOffsetForGroup(Integer groupId,
			Integer offset, Integer count ) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		try (PreparedStatement pStatement = conn.prepareStatement(SQL_GET_CHAT_MESSAGES_WITH_OFFSET);) {
			pStatement.setInt(1, groupId);
			pStatement.setInt(2, count);
			pStatement.setInt(3, offset);
			ResultSet resultSet = pStatement.executeQuery();
			messages = Transformer.getListOfInstances(resultSet, ChatMessage.class, ChatMessage.class);
		} catch (Exception e) {
		e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return messages;
	
	}

	@Override
	public boolean sendMessageToGroupOfStudents(int teacherId, int group_id, String text)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		boolean result = false;
		try (PreparedStatement pStatement1 = conn.prepareStatement(SQL_GET_STUDENT_ID_OF_GROUP);
				PreparedStatement pStatement2 = conn.prepareStatement(SQL_SEND_MESSAGE_TO_GROUP_OF_STUDENTS);) {
			conn.setAutoCommit(false);
			
			pStatement1.setInt(1, group_id);
			ResultSet resultSet1 = pStatement1.executeQuery();
			
			while(resultSet1.next()){
				pStatement2.setInt(1, teacherId);
				pStatement2.setInt(2, resultSet1.getInt(1));
				pStatement2.setString(3, text);
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(new Date());
				pStatement2.setString(4, currentTime);
				pStatement2.executeUpdate();
			}
			result = true;
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			result = false;
		} finally {
			cm.freeConnection(conn);
		}
		return result;
	}

}
