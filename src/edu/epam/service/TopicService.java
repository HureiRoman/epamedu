package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Topic;

public class TopicService {
	public static Integer createTopic(Topic topic) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getTopicDAO().createTopic(topic);
	}

	public static List<Topic> getTeacherTopics(Integer teacherId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getTopicDAO().getTeacherTopics(teacherId);
	}
	public static boolean deleteTopic(Integer topicId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getTopicDAO().deleteTopic(topicId);
	}

	public static Topic getTopicById(Integer topicId) throws SQLException {
	return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
			.getTopicDAO().getTopicById(topicId);
	}

	public static boolean updateTopic(Topic topic) throws SQLException  {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getTopicDAO().updateTopic(topic);
	}
	
	public static List<Topic> getTeacherTopicsByDirection(int teacherId, int directionId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getTopicDAO().getTeacherTopicsByDirection(teacherId, directionId);
	}
}
