package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Lesson;
import edu.epam.model.Task;

public class LessonService {

	public static List<Lesson> getLessonsForGroup(Integer groupId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getLessonsForGroup(groupId);
	}
	
	public static List<Lesson> getAllLessonsForGroup(Integer groupId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getAllLessonsForGroup(groupId);
	}
	
	public static boolean setLessonActive(int lessonId, boolean active) throws SQLException{
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().setLessonActive(lessonId, active);
	}
	
	public static boolean addLesson(Lesson lesson) throws SQLException{
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().addLesson(lesson);
	}
	
	public static boolean deleteLesson(int lessonId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().deleteLesson(lessonId);
	}
	
	public static boolean updateLesson(Lesson lessonWithNewHW) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().updateLesson(lessonWithNewHW);
	}
	
	public static boolean setLessonAutoTurnOn(int lessonId,boolean active) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().setLessonAutoTurnOn(lessonId, active);
	}
	
	public static List<Lesson> getAutoTurnOnLessons() throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getAutoTurnOnLessons();
	}
	
	public static List<Task> getGroupTasks(int groupId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getGroupTasks(groupId);
	}

	public static Lesson getLessonById(int lessonId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getLessonById(lessonId);
	}

	public static int getAmountOfPastLessonsForGroup(int groupId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getLessonsDAO().getAmountOfPastLessonsForGroup(groupId);
	}
}
