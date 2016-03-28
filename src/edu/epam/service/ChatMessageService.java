package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.chat.ChatMessage;
import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;

public class ChatMessageService {

	public static List<ChatMessage> getMessagesWithOffsetForGroup(
			Integer groupId, Integer offset , Integer count) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getChatMessageDAO()
				.getMessagesWithOffsetForGroup(groupId, offset,count);
	}
	
	
	public static boolean addChatMessageToDB(ChatMessage message){
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getChatMessageDAO()
				.addChatMessageToDB(message);
	}
	
	
	
	public static boolean sendMessageToGroupOfStudents(int teacherId, int group_id,String text) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getChatMessageDAO()
				.sendMessageToGroupOfStudents(teacherId, group_id,text);
		
	}
	
}
