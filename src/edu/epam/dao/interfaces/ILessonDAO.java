package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Lesson;
import edu.epam.model.Task;

public interface ILessonDAO {

	List<Lesson> getLessonsForGroup(Integer groupId) throws SQLException;

	List<Lesson> getAllLessonsForGroup(Integer groupId) throws SQLException;
	
	boolean setLessonActive(int groupId, boolean active) throws SQLException;
	
	boolean addLesson(Lesson lesson) throws SQLException;
	
	boolean deleteLesson(int lessonId) throws SQLException;
	
	boolean updateLesson(Lesson lessonWithNewHW) throws SQLException;
	
	boolean setLessonAutoTurnOn(int groupId,boolean active) throws SQLException;
	
	List<Lesson> getAutoTurnOnLessons() throws SQLException;
	 
	List<Task> getGroupTasks(int groupId) throws SQLException;

	Lesson getLessonById(int lessonId) throws SQLException;

	int getAmountOfPastLessonsForGroup(Integer groupId) throws SQLException;
}
