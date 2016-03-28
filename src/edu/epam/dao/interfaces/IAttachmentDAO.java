package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Attachment;
import edu.epam.model.StudentAttachment;

public interface IAttachmentDAO {
	Integer createNewAttachment(Attachment attachment) throws SQLException;
	String getAttachmentTitleById(Integer attachmentId) throws SQLException;
	List<Attachment> getTeacherAttachments(Integer teacherId) throws SQLException;
	List<Attachment> getAttachmentsByTopicId(Integer topicId) throws SQLException;
	boolean deleteAttachmentById(Integer attachmentId) throws SQLException;
	List<StudentAttachment> getStudentAttachmentsForTopic(int taskId) throws SQLException;
	StudentAttachment getAttachmentForStudentByLessonId(Integer taskId,
			Integer userId) throws SQLException;
	boolean deleteAttachment(Integer topicId) throws SQLException;
	String getStudentAttachmentTitleById(Integer attachmentId)
			throws SQLException;
	Integer addStudentAttachment(StudentAttachment attachment)
			throws SQLException;
	boolean deleteStudentAttachmentById(Integer attachmentId)
			throws SQLException;
}
