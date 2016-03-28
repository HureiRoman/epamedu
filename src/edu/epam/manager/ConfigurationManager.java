package edu.epam.manager;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ConfigurationManager {

	static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class);

	private static ConfigurationManager instance;
	private ResourceBundle resourceBundle;
	private static final String BUNDLE_NAME = "configurations.config";
	public static final String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME";
	public static final String DATABASE_URL = "DATABASE_URL";
	public static final String MAIN_PAGE_PATH = "MAIN_PAGE_PATH";
	public static final String ERROR_PAGE_PATH = "ERROR_PAGE_PATH";
	public static final String ADMIN_PANEL_PAGE = "ADMIN_PANEL_PAGE";
	public static final String TEACHER_PANEL_PAGE = "TEACHER_PANEL_PAGE";
	public static final String TESTS_PAGE = "testPage";
	public static final String TESTS_RESULT_PAGE = "TESTS_RESULT_PAGE";
	public static final String VIEW_STUDENTS_PAGE="VIEW_STUDENTS_PAGE";
	public static final String VIEW_GROUPS_PAGE = "VIEW_GROUPS_PAGE";
	public static final String GROUP_INFO_PAGE = "GROUP_INFO_PAGE";
	public static final String ADMIN_PANEL_NEWS_PAGE = "ADMIN_PANEL_NEWS_PAGE";
	public static final String ADMIN_PANEL_EMPLOYEE_PAGE = "ADMIN_PANEL_EMPLOYEE_PAGE";
	public static final String ADMIN_PANEL_COURSES_PAGE = "ADMIN_PANEL_COURSES_PAGE";
	public static final String TRAINEE_HR_MESSAGE = "trainee_hr_message";
	public static final String STUDENT_PANEL_PAGE = "STUDENT_PANEL_PAGE";
	public static final String VIEW_TRAINEES_PAGE = "VIEW_TRAINEES_PAGE";
	public static final String TRAINEE_CABINET_PAGE = "TRAINEE_CABINET_PAGE";
	public static final String HR_INTERVIEWS_PAGE = "HR_INTERVIEWS_PAGE";
	public static final String TOPICS_PAGE="TOPICS_PAGE";
	public static final String ADVERTISEMENT_PAGE="ADVERTISEMENT_PAGE";
	public static final String HR_INTERVIEW_RESULTS_PAGE = "HR_INTERVIEW_RESULTS_PAGE";
	public static final String ADMIN_EMPLOYEES_MANAGEMENT = "ADMIN_EMPLOYEES_MANAGEMENT";
	public static final String EMP_REGCONFIRM_PAGE = "EMP_REGCONFIRM_PAGE";
	public static final String STUDENT_GROUP_PAGE = "STUDENT_GROUP_PAGE";
	public static final String RETRIEVE_PASSWORD_PAGE="RETRIEVE_PASSWORD_PAGE";
	public static final String STUDENT_DIALOG_PAGE = "STUDENT_DIALOG_PAGE";
	public static final String TEACHER_GROUP_PAGE = "TEACHER_GROUP_PAGE";
	public static final String EDIT_TEACHER_PROFILE = "EDIT_TEACHER_PROFILE";
	public static final String HR_SHOW_STUDENTS_PAGE = "HR_SHOW_STUDENTS_PAGE";
	public static final String TEACHER_SHOW_STUDENTS_VISITING_PAGE = "TEACHER_SHOW_STUDENTS_VISITING_PAGE";
	public static final String TEACHER_SHOW_GRADES_OF_TASKS = "TEACHER_SHOW_GRADES_OF_TASKS";
	public static final String STUDENT_DIALOGS_PAGE = "STUDENT_DIALOGS_PAGE";

	public static final String TEACHER_DIALOGS_PAGE = "TEACHER_DIALOGS_PAGE";

	public static final String TEACHER_DIALOG_PAGE = "TEACHER_DIALOG_PAGE";
 
	public static final String TRAINEE_DIALOG_PAGE = "TRAINEE_DIALOG_PAGE";

	public static final String HR_DIALOG_PAGE = "HR_DIALOG_PAGE";

	public static final String TRAINEE_DIALOGS_PAGE = "TRAINEE_DIALOGS_PAGE";

	public static final String HR_DIALOGS_PAGE = "HR_DIALOGS_PAGE";

	public static final String TEACHER_MANAGE_TESTS = "TEACHER_MANAGE_TESTS";

	public static final String TEACHER_MANAGE_TESTS_BY_DIRECTION = "TEACHER_MANAGE_TESTS_BY_DIRECTION";

	public static final String ADMIN_USERS_MANAGEMENT = "ADMIN_USERS_MANAGEMENT";

	public static final String TEACHER_SHOW_VISITING_STATISTIC = "TEACHER_SHOW_VISITING_STATISTIC";

	public static final String GET_TOKEN_PAGE = "GET_TOKEN_PAGE";
	public static final String HR_CABINET = "hr_cabinet";

	public static final String REGISTRATION = "registrationPage";

	

	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManager();
			instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		}
		return instance;
	}

	public String getProperty(String key) {
		System.out.println("ConfigurationManager get Object by key = " + key);
		return (String) resourceBundle.getObject(key);
	}
}
