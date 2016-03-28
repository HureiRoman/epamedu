package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Message;

public class PersonalMessageService {

	public static List<Message> getMessagesWithOffsetForDialog(Integer userId,
			Integer collocutorId, Integer count, Integer offset) {
		return AbstractDAOFactory
				.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.getMessagesWithOffsetForDialog(userId, collocutorId, count,
						offset);
	}

	public static boolean addChatMessageToDB(Message message) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO().addChatMessageToDB(message);
	}

	public static void sendMessegeToInterviewFollowers(int hrId,
			int interviewId, String messageText) throws SQLException {
		AbstractDAOFactory
				.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.sendMessageToInterviewFollowers(hrId, interviewId, messageText);
	}

	public static void sendMessageToNewsSubscribersByDirectionId(int hrId,
													   int directionId, String messageText) throws SQLException {
		AbstractDAOFactory
				.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.sendMessageToNewsSubscribersByDirectionId(hrId, directionId, messageText);
	}


	public static int getAllUnreadMessageCount(int user_id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.getAllUnreadMessageCount(user_id);
	}

	public static Integer getCountOfUnreadMessages(Integer userId,
			Integer colocutorId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.getCountOfUnreadMessages(userId, colocutorId);
	}

	public static Message getLastMessageForDialog(Integer userId,
			Integer colocutorId) throws Exception {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO()
				.getLastMessageForDialog(userId, colocutorId);
	}

	public static List<Integer> getListOfCollocutorsForUser(Integer id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO().getListOfColocutorsForUser(id);
	}

	public static boolean markMessagesAsReaded(Integer user, Integer collocutor) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getPersonalMessageDAO().markMessagesAsReaded(user,collocutor);
		
	}

}
