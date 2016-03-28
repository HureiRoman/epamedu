package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Topic;
import edu.epam.role.Teacher;

public class TeacherService {

	public static List<Teacher> getTeachersForGroup(Integer groupId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().getTeachersForGroup(groupId);
	}

	public static List<Teacher> getAllTeachers() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().getAllTeachers();

	}

	public static List<Teacher> getAvailableTeachersForGroup(Integer groupId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().getAvailableTeachersForGroup(groupId);
	}
	
	
	public static List<Topic> getTeachersTopics(int teacherId, int directionId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().getTeachersTopics(teacherId,directionId);
	}
	public static boolean updateTeacher(Teacher teacher) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().updateTeacher(teacher);
	}
	
	public static boolean changeTopicInLesson(int idLesson,int idTopic) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getTeacherDAO().changeTopicInLesson(idLesson, idTopic);
	}

}
