package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IAttachmentDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Attachment;
import edu.epam.model.StudentAttachment;

public class AttachmentDAO implements IAttachmentDAO {

	private static final String SQL_GET_ATTACHMENT_BY_ID = "SELECT * FROM student_attachments sa WHERE sa.task_id = ? AND sa.student_id  = ?;";
	private static final String SQL_GET_ATTACHMENT_BY_TASK_ID = "SELECT * FROM student_attachments sa WHERE sa.task_id = ?";
	private static final String SQL_GET_TITLE_STUDENT_ATTACHMENT_BY_ID = "SELECT title FROM student_attachments WHERE id=?";
	private static final String SQL_ADD_STUDENT_ATTACHMENT = "INSERT INTO student_attachments (task_id,student_id,title,extension,date_of_addition) VALUES(?,?,?,?,?)";
	private static final String SQL_DELETE_STUDENTS_ATTACHMENT_BY_ID = "DELETE FROM student_attachments WHERE id=?";
	private static String SQL_CREATE_NEW_ATTACHMENT = "INSERT INTO attachments (topic_id,direction_id,title,extension) VALUES(?,?,?,?)";
	private static String SQL_GET_TITLE_BY_ID = "SELECT title FROM attachments WHERE id=?";

	private static String SQL_GET_TEACHER_ATTACHMENTS = "SELECT DISTINCT * FROM attachments WHERE topic_id IN (SELECT id FROM topics WHERE teacher_id=?)";
	private static String SQL_DELETE_ATTACHMENTS_OF_TOPIC = "DELETE FROM attachments WHERE topic_id=?";
	private static String SQL_GET_ATTACHMENTS_BY_TOPIC_ID = "SELECT * FROM attachments WHERE topic_id=?";
	private static String SQL_DELETE_ATTACHMENT_BY_ID="DELETE FROM attachments WHERE id=?";

	private static AttachmentDAO instance;

	public static AttachmentDAO getInstance() {

		if (instance == null) {
			instance = new AttachmentDAO();
		}
		return instance;
	}

	@Override
	public Integer createNewAttachment(Attachment attachment)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn.prepareStatement(
				SQL_CREATE_NEW_ATTACHMENT,
				PreparedStatement.RETURN_GENERATED_KEYS)) {

			// Insert to first table
			st1.setInt(1, attachment.getTopicId());
			st1.setInt(2, attachment.getDirectionId());
			st1.setString(3, attachment.getTitle());
			st1.setString(4, attachment.getExtension());
			Integer updated = st1.executeUpdate();
			if (updated > 0) {
				ResultSet generatedKeys = st1.getGeneratedKeys();
				if (generatedKeys.next()) {
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
	@Override
	public String getAttachmentTitleById(Integer attachmentId)
			throws SQLException {
		System.out.println("getting title for "+attachmentId);
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_TITLE_BY_ID)) {
			System.out.println(st);
			st.setInt(1, attachmentId);
			ResultSet rs1 = st.executeQuery();
			if(rs1.next())
			return rs1.getString("title");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public StudentAttachment getAttachmentForStudentByLessonId(Integer taskId,
			Integer userId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ATTACHMENT_BY_ID)) {
			st.setInt(1, taskId);
			st.setInt(2, userId);
			StudentAttachment attachment;
			ResultSet rs = st.executeQuery();
			attachment = Transformer.getInstance(rs, StudentAttachment.class,
					StudentAttachment.class);
			
			return attachment;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Attachment> getTeacherAttachments(Integer teacherId)
			throws SQLException {

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Attachment> listOfAttachments = new ArrayList<>();

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TEACHER_ATTACHMENTS)) {
			st.setInt(1, teacherId);
			ResultSet rs = st.executeQuery();

			listOfAttachments = Transformer.getListOfInstances(rs,
					Attachment.class, Attachment.class);

			return listOfAttachments;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public boolean deleteAttachment(Integer topicId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_DELETE_ATTACHMENTS_OF_TOPIC)) {
			st1.setInt(1, topicId);
			Integer updated = st1.executeUpdate();
			if (updated > 0) {
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
	public List<Attachment> getAttachmentsByTopicId(Integer topicId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Attachment> listOfAttachments = new ArrayList<>();

		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ATTACHMENTS_BY_TOPIC_ID)) {
			st.setInt(1, topicId);
			ResultSet rs = st.executeQuery();
			listOfAttachments = Transformer.getListOfInstances(rs,
					Attachment.class, Attachment.class);

			return listOfAttachments;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public boolean deleteAttachmentById(Integer attachmentId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try(PreparedStatement st1 = conn.prepareStatement(SQL_DELETE_ATTACHMENT_BY_ID)) {
		
		st1.setInt(1,attachmentId);
	
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
	public String getStudentAttachmentTitleById(Integer attachmentId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_TITLE_STUDENT_ATTACHMENT_BY_ID)) {
			st.setInt(1, attachmentId);
			ResultSet rs1 = st.executeQuery();
			rs1.next();
			return rs1.getString("title");
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	@Override
	public Integer addStudentAttachment(StudentAttachment attachment)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn.prepareStatement(
				SQL_ADD_STUDENT_ATTACHMENT,
				PreparedStatement.RETURN_GENERATED_KEYS)) {

			st1.setInt(1, attachment.getTaskId());
			st1.setInt(2, attachment.getStudentId());
			st1.setString(3, attachment.getTitle());
			st1.setString(4, attachment.getExtension());
			st1.setTimestamp(5, new java.sql.Timestamp(attachment.getDateAdded()
					.getTime()));
			
			Integer updated = st1.executeUpdate();
			if (updated > 0) {
				ResultSet generatedKeys = st1.getGeneratedKeys();
				if (generatedKeys.next()) {
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
	@Override
	public boolean deleteStudentAttachmentById(Integer attachmentId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st1 = conn
				.prepareStatement(SQL_DELETE_STUDENTS_ATTACHMENT_BY_ID)) {
			st1.setInt(1, attachmentId);
			Integer updated = st1.executeUpdate();
			if (updated > 0) {
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
	public List<StudentAttachment> getStudentAttachmentsForTopic(int taskId) throws SQLException {
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<StudentAttachment> listOfAttachments = null;
		
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_ATTACHMENT_BY_TASK_ID)) {
			
			st.setInt(1, taskId);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				rs.previous();
				listOfAttachments = new ArrayList<StudentAttachment>();
				while(rs.next()){
					rs.previous();
					StudentAttachment attachment;
					attachment = Transformer.getInstance(rs, StudentAttachment.class,StudentAttachment.class);
					listOfAttachments.add(attachment);
				}
			}	
			} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		
		return listOfAttachments;
	}
}
