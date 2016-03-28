package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.ITopicDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Topic;

public class TopicDAO implements ITopicDAO {


	private static final String SQL_GET_TOPIC_BY_ID = "SELECT * FROM topics t WHERE t.id = ? ";

	private static final String SQL_DELETE_LESSONS_OF_TOPIC = "DELETE FROM lessons WHERE topic_id=?";

	private static String SQL_CREATE_NEW_TOPIC="INSERT INTO topics (title,content,teacher_id,direction_id) VALUES(?,?,?,?)";
	private static String SQL_GET_TOPIC_FOR_TEACHER="SELECT * FROM topics WHERE teacher_id=?";
	private static String SQL_DELETE_ATTACHMENTS_OF_TOPIC="DELETE FROM attachments WHERE topic_id=?";
	private static String SQL_DELETE_TOPIC="DELETE FROM topics WHERE id=?";
	private static String SQL_UPDATE_TOPIC="UPDATE topics SET title = ?, content = ?, teacher_id = ?, direction_id = ? WHERE id = ?";
	private static String SQL_GET_TEACHER_TOPIC_BY_DIRECTION = "SELECT * FROM topics WHERE teacher_id=? AND direction_id=?";
	
	private static TopicDAO instance;

	public static TopicDAO getInstance() {

		if (instance == null) {
			instance = new TopicDAO();
		}
		return instance;
	}
	@Override
	public Integer createTopic(Topic topic) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_CREATE_NEW_TOPIC,PreparedStatement.RETURN_GENERATED_KEYS)) {
		
		st1.setString(1, topic.getTitle());
		st1.setString(2, topic.getContent());
		st1.setInt(3, topic.getTeacherId());
		st1.setInt(4, topic.getDirectionId());
		
		  Integer updated=  st1.executeUpdate();
		   if(updated>0){
				ResultSet generatedKeys = st1.getGeneratedKeys();
				if(generatedKeys.next()){
					return generatedKeys.getInt(1);
				}
		   }
		return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	public List<Topic> getTeacherTopics(Integer teacherId) throws SQLException {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Topic> listOfTopics = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TOPIC_FOR_TEACHER)) {
			st.setInt(1, teacherId);
			ResultSet rs = st.executeQuery();
			
			listOfTopics = Transformer.getListOfInstances(rs,
						Topic.class, Topic.class);
			
			return listOfTopics;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}


		
	public boolean deleteTopic(Integer topicId) throws SQLException{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_DELETE_TOPIC);
				PreparedStatement st2 = conn.prepareStatement(SQL_DELETE_ATTACHMENTS_OF_TOPIC);
				PreparedStatement st3 = conn.prepareStatement(SQL_DELETE_LESSONS_OF_TOPIC)) {
			conn.setAutoCommit(false);
		
		st2.setInt(1,topicId);
			
		st1.setInt(1,topicId);
		
		st3.setInt(1,topicId);

		  st3.executeUpdate();
		  st2.executeUpdate();
		  
		  Integer updated2=  st1.executeUpdate();
		   if(updated2>0){
			   conn.commit();
			   conn.setAutoCommit(true);
				return true;
				}
		return false;
		} catch (Exception e) {
			   conn.rollback();
			   conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	public Topic getTopicById(Integer topicId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Topic topic=new Topic();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TOPIC_BY_ID)) {
			st.setInt(1, topicId);
			ResultSet rs = st.executeQuery();
			topic = Transformer.getInstance(rs,
						Topic.class, Topic.class);
			
			return topic;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	public boolean updateTopic(Topic topic) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_UPDATE_TOPIC)) {
		st1.setString(1, topic.getTitle());
		st1.setString(2, topic.getContent());
		st1.setInt(3, topic.getTeacherId());
		st1.setInt(4, topic.getDirectionId());
		st1.setInt(5,topic.getId());	
		
		
		  Integer updated=  st1.executeUpdate();
		   if(updated>0){
				return true;
		   }
		return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Topic> getTeacherTopicsByDirection(int teacherId, int directionId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Topic> topicList = null;
		
		try(PreparedStatement statement = conn.prepareStatement(SQL_GET_TEACHER_TOPIC_BY_DIRECTION)) {
			statement.setInt(1, teacherId);
			statement.setInt(2, directionId);
			ResultSet resultSet = statement.executeQuery();
			topicList = Transformer.getListOfInstances(resultSet, Topic.class, Topic.class);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return topicList;
	}
}
