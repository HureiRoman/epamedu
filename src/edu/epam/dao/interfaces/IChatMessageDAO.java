package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.chat.ChatMessage;

public interface IChatMessageDAO {

	boolean addChatMessageToDB(ChatMessage message);

	List<ChatMessage> getMessagesWithOffsetForGroup(Integer groupId,
			Integer offset, Integer count);
	
	boolean sendMessageToGroupOfStudents(int teacherId, int group_id,String text) throws SQLException;
}
