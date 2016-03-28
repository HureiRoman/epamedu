package edu.epam.factory;

import edu.epam.constants.EFactoryType;
import edu.epam.dao.realization.*;

public abstract class AbstractDAOFactory {
	public abstract UserDAO getUserDAO();

	public abstract AdminDAO getAdminDAO();

	public abstract GraduateDAO getGraduateDAO();

	public abstract HrDAO getHrDAO();

	public abstract StudentDAO getStudentDAO();

	public abstract TeacherDAO getTeacherDAO();

	public abstract TraineeDAO getTraineeDAO();

	public abstract TestDAO getTestDAO();

	public abstract NewsDAO getNewsDAO();

	public abstract GroupDAO getGroupDAO();

	public abstract ApplicationDAO getApplicationDAO();

	public abstract InterviewDAO getInterviewDAO();

	public abstract DirectionDAO getDirectionDAO();

	public abstract InterviewResultDAO getInterviewResultDAO();

	public abstract StudentVisitingDAO getStudentVisitingDAO();
	
	public abstract TaskDAO getTaskDAO();
	public abstract TopicDAO getTopicDAO();

	public abstract AdvertisementDAO getAdvertisementDAO();
	
	public abstract AttachmentDAO getAttachmentDAO();
	
	public abstract LessonDAO getLessonsDAO();

	public abstract PersonalMessageDAO getPersonalMessageDAO();
	
	public static AbstractDAOFactory getFactory(EFactoryType factoryType) {
		if (factoryType.equals(EFactoryType.MySQL)) {
			return MySQLDAOFactory.getInstance();
		} else {
			return null;
		}
	}

	public  abstract ChatMessageDAO getChatMessageDAO();

	public abstract GradesOfTaskDAO getGradeOfTaskDAO();



	

}
