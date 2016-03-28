package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Topic;

public interface ITopicDAO {
	Integer createTopic(Topic topic) throws SQLException;
	List<Topic> getTeacherTopics(Integer teacherId) throws SQLException;
	boolean deleteTopic(Integer topic_id) throws SQLException;
	Topic getTopicById(Integer topicId) throws SQLException;
	List<Topic> getTeacherTopicsByDirection(int teacherId, int directionId) throws SQLException;
}
