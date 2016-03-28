package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Attachment;
import edu.epam.model.StudentAttachment;

public class AttachmentService {
	public static Integer createNewAttachment(Attachment attachment) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().createNewAttachment(attachment);
	}
	public static String getAttachmentTitleById(Integer attachmentId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getAttachmentTitleById(attachmentId);
	}

	public static List<Attachment> getTeacherAttachments(Integer teacherId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getTeacherAttachments(teacherId);
	}
	public static List<Attachment> getAttachmentsByTopicId(Integer topicId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getAttachmentsByTopicId(topicId);
	}
	public static StudentAttachment getAttachmentForStudentByTaskId(Integer taskId,
			Integer userId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getAttachmentForStudentByLessonId(taskId,userId);
	}
	public static boolean deleteAttachmentById(Integer attachmentId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().deleteAttachmentById(attachmentId);
	}
	public static String getStudentAttachmentTitleById(Integer attachmentId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getStudentAttachmentTitleById(attachmentId);
	}
	public static Integer addStudentAttachment(StudentAttachment attachment) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().addStudentAttachment(attachment);
	}

	public static boolean deleteStudentAttachmentById(Integer attachmentId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().deleteStudentAttachmentById(attachmentId);
	}
	

	public static List<StudentAttachment> getStudentAttachmentsForTopic(int taskId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAttachmentDAO().getStudentAttachmentsForTopic(taskId);
	}
	
	
	

}
