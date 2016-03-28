package edu.epam.manager;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class MessageManager {
	
	static final Logger LOGGER = Logger.getLogger(MessageManager.class);

	
    private static MessageManager instance;
    private static final String BUNDLE_NAME = "/translations/messages";
    public static final String SERVLET_EXCEPTION_ERROR_MESSAGE = "SERVLET_EXCEPTION_ERROR_MESSAGE";
    public static final String IO_EXCEPTION_ERROR_MESSAGE = "IO_EXCEPTION_ERROR_MESSAGE";
    public static final String SQL_ERROR_MESSAGE = "SQL_ERROR_MESSAGE";
    public static final String TOTAL_EXCEPTION_ERROR_MESSAGE = "TOTAL_EXCEPTION_ERROR_MESSAGE";
    public static final String GROUP_NOT_CREATED = "GROUP_NOT_CREATED";
    public static final String GROUP_CREATED = "GROUP_CREATED";
    public static final String ICORRECT_PASSWORD = "ICORRECT_PASSWORD";
    public static final String YOU_ARE_NOT_CONFIRMED = "YOU_ARE_NOT_CONFIRMED";
    public static final String NO_SUCH_USER = "NO_SUCH_USER";
    public static final String TESTS_WILL_BE = "TESTS_WILL_BE";
    public static final String AT = "_AT_";
    public static final String HOURS = "HOURS";
	public static final String FORBIDDEN = "FORBIDDEN";
	public static final String REVIEWS = "REVIEWS";
	public static final String NO_COMMENTS = "NO_COMMENTS";
	public static final String ENTER_YOUR_REVIEW = "ENTER_YOUR_REVIEW";
	public static final String SET_VISITING = "teacher.set_visiting";
	
	
	public static final String SEND_REVIEW = "SEND_REVIEW";
	public static final String WAITING_FOR_REGISTRATION = "WAITING_FOR_REGISTRATION";
	public static final String DETAILS = "DETAILS";
	public static final String LESSON_TIME = "LESSON_TIME";
	public static final String TOPIC = "TOPIC";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String HW = "HW";
	public static final String MY_PERFORMANCE = "MY_PERFORMANCE";
	
	
	public static final String FILES = "FILES";
	public static final String THERE_IS_NO_FILES = "THERE_IS_NO_FILES";
	public static final String DATE = "DATE";
	
	public static final String HOUR = "HOUR";
	public static final String DELETE_LESSON = "DELETE_LESSON";
	public static final String OPEN_TO_GROUP = "OPEN_TO_GROUP";
	public static final String AUTOMATICALLY_TURN_ON = "AUTOMATICALLY_TURN_ON";
	public static final String CHOOSE_TOPIC = "CHOOSE_TOPIC";
	public static final String CHANGE_TOPIC = "CHANGE_TOPIC";
	
	
	
	public static final String ADD_TOPIC = "ADD_TOPIC";
	public static final String NO_ATTACHMENTS = "NO_ATTACHMENTS";
	public static final String STUDENTS_HOMEWORKS = "STUDENTS_HOMEWORKS";
	public static final String ADD_HOMEWORK = "ADD_HOMEWORK";
	public static final String EDIT_HOMEWORK = "EDIT_HOMEWORK";
	public static final String YOU_DIDNOT_GIVE_HOMEWORK = "YOU_DIDNOT_GIVE_HOMEWORK";
	
	
	
	
	public static final String DEADLINE = "DEADLINE";
	public static final String NO_HOMEWORKS = "NO_HOMEWORKS";
	public static final String TITLE = "TITLE";
	public static final String SENDED = "SENDED";
	public static final String DELETE_TASK = "DELETE_TASK";
	public static final String NO_TASK_DOWNLOADED = "NO_TASK_DOWNLOADED";
	public static final String MARK = "MARK";
	public static final String NO_STUDENTS_IN_GROUP = "NO_STUDENTS_IN_GROUP";
	public static final String TASK_BODY = "TASK_BODY";
	public static final String TASK ="TASK";
	public static final String DATE_OF_LOADING = "DATE_OF_LOADING";
	public static final String BY = "BY";
	public static final String MY_EXECUTION = "MY_EXECUTION";
	public static final String NEW = "NEW";
	public static final String TEST = "TEST";
	public static final String QUESTION = "QUESTION";
	public static final String CODE_FRAGMENT = "CODE_FRAGMENT";
	public static final String CORRECT = "CORRECT";
	public static final String OPTIONS = "OPTIONS";
	public static final String ADD = "ADD";
	public static final String ADDED = "ADDED";
	public static final String CANCEL = "CANCEL";
	public static final String EDIT = "EDIT";
	public static final String SUBMIT = "SUBMIT";
	public static final String TEST_REMOVAL = "TEST_REMOVAL";
	public static final String ARE_U_SURE_DELETE_TEST = "ARE_U_SURE_DELETE_TEST";
	public static final String REMOVE = "REMOVE";
	public static final String REMOVED = "REMOVED";
	public static final String ERROR = "ERROR";
	public static final String YOU_HAVE_NOT_TEST_PASSED = "YOU_HAVE_NOT_TEST_PASSED";
	public static final String PASS_TEST = "PASS_TEST";
	public static final String YOUR_RATE = "YOUR_RATE";
	public static final String POSITION = "POSITION";
	public static final String NO_TESTS_TEACHER = "NO_TESTS_TEACHER";
	public static final String NO_TESTS_USER = "NO_TESTS_USER";
	public static final String ADD_TEST_TEACHER = "ADD_TEST_TEACHER";
	public static final String YOU_HAVE_TO_PASS_FIVE_TESTS = "YOU_HAVE_TO_PASS_FIVE_TESTS";
	public static final String NO_RANKING_ARE_AVAILABLE = "NO_RANKING_ARE_AVAILABLE";
	public static final String TRAINEE_NAME = "TRAINEE_NAME";
	public static final String POINTS = "POINTS";
	public static final String YOUR_SCORE = "YOUR_SCORE";
	public static final String YOUR_ANSWER = "YOUR_ANSWER";
	public static final String YOU_DIDNOT_ANSWER = "YOU_DIDNOT_ANSWER";
	public static final String REGISTER = "REGISTER";
	public static final String TO_TEST_RANKING = "TO_TEST_RANKING";
	public static final String WANT_TO_GET_KNOW_MORE = "WANT_TO_GET_KNOW_MORE";
	public static final String JOIN_COMMUNITY = "JOIN_COMMUNITY";
	public static final String NEXT = "NEXT";
	public static final String YOU_HAVE_10_MIN = "YOU_HAVE_10_MIN";
	public static final String REGISTRATION_SUBMITTING = "REGISTRATION_SUBMITTING";

	public static final String EMPTY_QUESTION = "EMPTY_QUESTION";
	public static final String TOO_LONG_QUESTION = "TOO_LONG_QUESTION";
	public static final String TOO_LONG_CODE = "TOO_LONG_CODE";
	public static final String TOO_LONG_ANSWER1 = "TOO_LONG_ANSWER1";
	public static final String TOO_LONG_ANSWER2 = "TOO_LONG_ANSWER2";
	public static final String TOO_LONG_ANSWER3 = "TOO_LONG_ANSWER3";
	public static final String TOO_LONG_ANSWER4 = "TOO_LONG_ANSWER4";


	public static final String noCorrectAnswer = "noCorrectAnswer";


	public static final String specifyAnswer1 = "specifyAnswer1";
	public static final String specifyAnswer2 = "specifyAnswer2";
	public static final String specifyAnswer3 = "specifyAnswer3";
	public static final String specifyAnswer4 = "specifyAnswer4";


	public static final String YOU_HAVE_NOT_CHANGE_ANYTHING = "YOU_HAVE_NOT_CHANGE_ANYTHING";


	public static final String Edited = "Edited";


	public static final String TEST_NOT_FOUND = "TEST_NOT_FOUND";


	public static final String EDIT_TASK = "EDIT_TASK";


	public static final String DISACTIVATED = "DISACTIVATED";
	
    
//    Для прикладу:
//  public static final String CONFIRMATION_ERROR_MESSAGE = "CONFIRMATION_ERROR_MESSAGE";
    
    
    
    
    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }

    public String getProperty(String key, Locale locale) {
        String key_value = (String) ResourceBundle.getBundle(BUNDLE_NAME, locale).getObject(key);
   
       String result_key="";
        try {
        	System.out.println("text in propertie file = " + key_value);
			byte[] key_bytes = key_value.getBytes("ISO-8859-1");
			result_key = new String(key_bytes, "UTF-8");
			System.out.println("text in code  = " + result_key);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("MessageManager getProperty UnsupportedEncodingException " + e.getMessage());
		}
        
        LOGGER.info("MessageManager get Object = " + key_value);
    	return key_value;
    }
}
