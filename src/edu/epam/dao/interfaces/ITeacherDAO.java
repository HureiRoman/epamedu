package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Topic;
import edu.epam.role.CommonUser;
import edu.epam.role.Teacher;

public interface ITeacherDAO {
	CommonUser enterTeacher(String email) throws SQLException;
	List<Teacher> getTeachersForGroup(Integer groupId) throws SQLException;
	List<Teacher> getAllTeachers() throws SQLException;
	List<Teacher> getAvailableTeachersForGroup(Integer groupId)	throws SQLException;
	boolean updateTeacher(Teacher teacher) throws SQLException;
	List<Topic> getTeachersTopics(int teacherId, int directionId) throws SQLException;
	boolean changeTopicInLesson(int idLesson,int idTopic) throws SQLException;
}
