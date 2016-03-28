package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Message;

public interface IPersonalMessageDAO {

	boolean addChatMessageToDB(Message message);

	List<Message> getMessagesWithOffsetForDialog(Integer userId,
			Integer collocutorId, Integer offset, Integer count);

	void sendMessageToInterviewFollowers(int hrId, int interviewId, String messageText) throws SQLException;

	void sendMessageToNewsSubscribersByDirectionId(int hrId, int directionId, String messageText) throws SQLException;

	int getAllUnreadMessageCount(int user_id) throws SQLException;

	Message getLastMessageForDialog(Integer userId, Integer colocutorId)
			throws Exception;

	Integer getCountOfUnreadMessages(Integer userId, Integer colocutorId)
			throws SQLException;

	List<Integer> getListOfColocutorsForUser(Integer id) throws SQLException;
	
	
}
