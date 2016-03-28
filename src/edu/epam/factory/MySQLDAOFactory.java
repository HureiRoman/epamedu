package edu.epam.factory;

import edu.epam.dao.realization.*;

public class MySQLDAOFactory extends AbstractDAOFactory {
	private static MySQLDAOFactory instance;

	private MySQLDAOFactory() {
	}

	public static MySQLDAOFactory getInstance() {
		if (instance == null) {
			instance = new MySQLDAOFactory();
		}
		return instance;
	}

	@Override
	public UserDAO getUserDAO() {
		return UserDAO.getInstance();
	}

	@Override
	public AdminDAO getAdminDAO() {
		return AdminDAO.getInstance();
	}

	@Override
	public GraduateDAO getGraduateDAO() {
		return GraduateDAO.getInstance();
	}

	@Override
	public HrDAO getHrDAO() {
		return HrDAO.getInstance();
	}

	@Override
	public StudentDAO getStudentDAO() {
		return StudentDAO.getInstance();
	}

	@Override
	public TeacherDAO getTeacherDAO() {
		return TeacherDAO.getInstance();
	}

	@Override
	public TraineeDAO getTraineeDAO() {
		return TraineeDAO.getInstance();
	}

	@Override
	public TestDAO getTestDAO() {
		return TestDAO.getInstance();
	}

	@Override
	public NewsDAO getNewsDAO() {
		return NewsDAO.getInstance();
	}

	@Override
	public GroupDAO getGroupDAO() {
		return GroupDAO.getInstance();
	}

	@Override
	public ApplicationDAO getApplicationDAO() {
		return ApplicationDAO.getInstance();
	}

	@Override
	public DirectionDAO getDirectionDAO() {
		return DirectionDAO.getInstance();
	}

	@Override
	public TaskDAO getTaskDAO() {
		return TaskDAO.getInstance();
	}

	@Override
	public InterviewDAO getInterviewDAO() {
		return InterviewDAO.getInstance();
	}
	public TopicDAO getTopicDAO(){
		return TopicDAO.getInstance();
	}

	@Override
	public AdvertisementDAO getAdvertisementDAO() {
		return AdvertisementDAO.getInstance();
	}

	@Override
	public AttachmentDAO getAttachmentDAO() {
		return AttachmentDAO.getInstance();
	}

	@Override
	public InterviewResultDAO getInterviewResultDAO() {
		return InterviewResultDAO.getInstance();
	}

	@Override
	public StudentVisitingDAO getStudentVisitingDAO() {
		return StudentVisitingDAO.getInstance();
	}

	@Override
	public LessonDAO getLessonsDAO() {
		return LessonDAO.getInstance();
	}

	@Override
	public ChatMessageDAO getChatMessageDAO() {
		return ChatMessageDAO.getInstance();
	}

	@Override
	public PersonalMessageDAO getPersonalMessageDAO() {
		return PersonalMessageDAO.getInstance();
	}

	@Override
	public GradesOfTaskDAO getGradeOfTaskDAO() {
		return GradesOfTaskDAO.getInstance();
	}

	
}
