package edu.epam.controllerServlet;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.epam.servlet.AjaxComand.AjaxActionCommand;
import edu.epam.servlet.AjaxComand.ChangeLanguageCommand;
import edu.epam.servlet.AjaxComand.ChangePasswordCommand;
import edu.epam.servlet.AjaxComand.CheckEmailCommand;
import edu.epam.servlet.AjaxComand.ExitComand;
import edu.epam.servlet.AjaxComand.GetAllAddsForGroupCommand;
import edu.epam.servlet.AjaxComand.GetAllDirectionsCommand;
import edu.epam.servlet.AjaxComand.GetAllDirectionsWithTestsCommand;
import edu.epam.servlet.AjaxComand.GetAllUserDataByIdCommand;
import edu.epam.servlet.AjaxComand.GetChatMessagesWithOffsetCommand;
import edu.epam.servlet.AjaxComand.GetChatParticipantsCommand;
import edu.epam.servlet.AjaxComand.GetCountOfUnreadedMessagesCommand;
import edu.epam.servlet.AjaxComand.GetDialogsForUser;
import edu.epam.servlet.AjaxComand.GetDirectionDataCommand;
import edu.epam.servlet.AjaxComand.GetLatestTestsCommand;
import edu.epam.servlet.AjaxComand.GetLoginedUserDataCommand;
import edu.epam.servlet.AjaxComand.GetNewMessagesCommand;
import edu.epam.servlet.AjaxComand.GetPersonalMessagesWithOffsetCommand;
import edu.epam.servlet.AjaxComand.GetStudentsForTeacherByGroupCommand;
import edu.epam.servlet.AjaxComand.GetUreadMessCountCommand;
import edu.epam.servlet.AjaxComand.GetUserGroupsCommand;
import edu.epam.servlet.AjaxComand.LeaveReviewCommand;
import edu.epam.servlet.AjaxComand.LoginCommand;
import edu.epam.servlet.AjaxComand.PasswordRetrievalCommand;
import edu.epam.servlet.AjaxComand.ProcessMultipleGradAndGrop;
import edu.epam.servlet.AjaxComand.RegistrationCommand;
import edu.epam.servlet.AjaxComand.RegistrationEmployeeCommand;
import edu.epam.servlet.AjaxComand.SaveNewPasswordCommand;
import edu.epam.servlet.AjaxComand.SendMessageCommand;
import edu.epam.servlet.AjaxComand.UpdateUserInfoCommand;
import edu.epam.servlet.AjaxComand.UpdateUserInfoWithCVCommand;
import edu.epam.servlet.AjaxComand.admin.AddNewDirectionCommand;
import edu.epam.servlet.AjaxComand.admin.AddNewEmployeeCommand;
import edu.epam.servlet.AjaxComand.admin.AddNewGroupCommand;
import edu.epam.servlet.AjaxComand.admin.AddNewsItemCommand;
import edu.epam.servlet.AjaxComand.admin.DeleteNewsItemCommand;
import edu.epam.servlet.AjaxComand.admin.DeleteReviewCommand;
import edu.epam.servlet.AjaxComand.admin.GetAllTeachersCommand;
import edu.epam.servlet.AjaxComand.admin.GetAvailableTeachersForGroupCommand;
import edu.epam.servlet.AjaxComand.admin.GetGroupDataCommand;
import edu.epam.servlet.AjaxComand.admin.GetGroupsForDirectionCommand;
import edu.epam.servlet.AjaxComand.admin.GetHrDataForDirectionCommand;
import edu.epam.servlet.AjaxComand.admin.GetNewsItemDataCommand;
import edu.epam.servlet.AjaxComand.admin.GetTeachersForGroupCommand;
import edu.epam.servlet.AjaxComand.admin.SetDirectionHRCommand;
import edu.epam.servlet.AjaxComand.admin.SetDirectonActiveCommand;
import edu.epam.servlet.AjaxComand.admin.SetGroupActiveCommand;
import edu.epam.servlet.AjaxComand.admin.SetGroupStaff;
import edu.epam.servlet.AjaxComand.admin.SetNewsItemArchivedCommand;
import edu.epam.servlet.AjaxComand.admin.SetUserActiveCommand;
import edu.epam.servlet.AjaxComand.admin.UpdateDirectionDataCommand;
import edu.epam.servlet.AjaxComand.admin.UpdateGroupDataCommand;
import edu.epam.servlet.AjaxComand.admin.UpdateNewsItemCommand;
import edu.epam.servlet.AjaxComand.hr.DeleteStudentsHRCommand;
import edu.epam.servlet.AjaxComand.hr.DoStudentsCommand;
import edu.epam.servlet.AjaxComand.hr.GetAllHrCommand;
import edu.epam.servlet.AjaxComand.hr.GetChattingHrTraineeCommand;
import edu.epam.servlet.AjaxComand.hr.GetGroupsByInterviewCommand;
import edu.epam.servlet.AjaxComand.hr.GetInterviewsByDirectionIdHRCommand;
import edu.epam.servlet.AjaxComand.hr.GetNewHrMessagesCommand;
import edu.epam.servlet.AjaxComand.hr.GetStudentsByGroupIdCommand;
import edu.epam.servlet.AjaxComand.hr.GetTraineesHrCommand;
import edu.epam.servlet.AjaxComand.hr.InterviewActionsCommand;
import edu.epam.servlet.AjaxComand.hr.InterviewResultsCommand;
import edu.epam.servlet.AjaxComand.hr.SearchTraineeCommand;
import edu.epam.servlet.AjaxComand.hr.SendMessageToInterviewFollowers;
import edu.epam.servlet.AjaxComand.hr.SetTraineeInterviewResultsCommand;
import edu.epam.servlet.AjaxComand.student.AddAttachmentOfStudentCommand;
import edu.epam.servlet.AjaxComand.student.DeleteStudentAttachmentCommand;
import edu.epam.servlet.AjaxComand.teacher.AddLessonCommand;
import edu.epam.servlet.AjaxComand.teacher.AddNewAdvetisementCommand;
import edu.epam.servlet.AjaxComand.teacher.AddNewStudentTaskCommand;
import edu.epam.servlet.AjaxComand.teacher.AddNewTestCommand;
import edu.epam.servlet.AjaxComand.teacher.AddNewTopicCommand;
import edu.epam.servlet.AjaxComand.teacher.AutoTurnOnLessonCommand;
import edu.epam.servlet.AjaxComand.teacher.ChangeTopicInLesson;
import edu.epam.servlet.AjaxComand.teacher.CreateAdvertisementComand;
import edu.epam.servlet.AjaxComand.teacher.DeleteAdvertisementComand;
import edu.epam.servlet.AjaxComand.teacher.DeleteAttachmentCommand;
import edu.epam.servlet.AjaxComand.teacher.DeleteHomeTaskCommand;
import edu.epam.servlet.AjaxComand.teacher.DeleteHomeWorkCommand;
import edu.epam.servlet.AjaxComand.teacher.DeleteLessonCommand;
import edu.epam.servlet.AjaxComand.teacher.DeleteTopicCommand;
import edu.epam.servlet.AjaxComand.teacher.DoGraduatesCommand;
import edu.epam.servlet.AjaxComand.teacher.EditHomeWorkCommand;
import edu.epam.servlet.AjaxComand.teacher.EditTaskCommand;
import edu.epam.servlet.AjaxComand.teacher.EditTestCommand;
import edu.epam.servlet.AjaxComand.teacher.EditTopicCommand;
import edu.epam.servlet.AjaxComand.teacher.GetAdvertisementDataCommand;
import edu.epam.servlet.AjaxComand.teacher.GetAllTopicsForTeacherCommand;
import edu.epam.servlet.AjaxComand.teacher.GetAttachmentsAndDataForTopicCommand;
import edu.epam.servlet.AjaxComand.teacher.GetDataAboutStudentByIdCommand;
import edu.epam.servlet.AjaxComand.teacher.GetTaskByIdCommand;
import edu.epam.servlet.AjaxComand.teacher.GetTopicAttachmentsCommand;
import edu.epam.servlet.AjaxComand.teacher.SendMessageToGroupStudentsCommand;
import edu.epam.servlet.AjaxComand.teacher.SetLessonActiveCommand;
import edu.epam.servlet.AjaxComand.teacher.SetStudentVisitingCommand;
import edu.epam.servlet.AjaxComand.teacher.SetTaskMarkCommand;
import edu.epam.servlet.AjaxComand.teacher.TestEditPaginated;
import edu.epam.servlet.AjaxComand.teacher.UpdateAdvertisementComand;
import edu.epam.servlet.AjaxComand.trainee.ApplyToTestsCommand;
import edu.epam.servlet.AjaxComand.trainee.CancelApplicationOnTestCommand;
import edu.epam.servlet.AjaxComand.trainee.GetAllTraineeComand;
import edu.epam.servlet.AjaxComand.trainee.GetChattingTraineeHR;
import edu.epam.servlet.AjaxComand.trainee.SubscribeToGetNews;

public class AjaxActionFactory {

	static final Logger LOGGER = Logger.getLogger(ActionFactory.class);
	private static final String PARAM_NAME_AJAX_COMMAND = "command";
	private static AjaxActionFactory instance = null;
	HashMap<String, AjaxActionCommand> commands = new HashMap<String, AjaxActionCommand>();

	private AjaxActionFactory() {
		LOGGER.info("Put commands to Map");
		commands.put("login", new LoginCommand());
		commands.put("send_message", new SendMessageCommand());
		commands.put("apply_to_test", new ApplyToTestsCommand());
		commands.put("get_latest_test_event", new GetLatestTestsCommand());
		commands.put("registration", new RegistrationCommand());
		commands.put("doGraduates", new DoGraduatesCommand());
		commands.put("getAllStudentsByTeacherId", new GetStudentsForTeacherByGroupCommand());
		commands.put("doStudent", new DoStudentsCommand());
		commands.put("addNewsItem", new AddNewsItemCommand());
		commands.put("getMessagesTraineeHr", new GetChattingTraineeHR());
		commands.put("getMessagesHrTrainee", new GetChattingHrTraineeCommand());
		commands.put("getNewMessagesForTraineeHR", new GetNewMessagesCommand());
		commands.put("getNewMessagesForHRTrainee", new GetNewHrMessagesCommand());
		commands.put("setNewsItemArchived", new SetNewsItemArchivedCommand());
		commands.put("getNewsItemData", new GetNewsItemDataCommand());
		commands.put("updateNewsItem", new UpdateNewsItemCommand());
		commands.put("getAllHr", new GetAllHrCommand());
		commands.put("updateUserInfoWithCV", new UpdateUserInfoWithCVCommand());
		commands.put("changePassword", new ChangePasswordCommand());
		commands.put("getDirections", new GetAllDirectionsWithTestsCommand());
		commands.put("getGroupsForDirection", new GetGroupsForDirectionCommand());
        commands.put("getTeachersForGroup", new GetTeachersForGroupCommand());
        commands.put("getHrDataForDirection", new GetHrDataForDirectionCommand());
        commands.put("addNewDirection", new AddNewDirectionCommand());
        commands.put("addNewGroup", new AddNewGroupCommand());
        commands.put("getUreadMessCount", new GetUreadMessCountCommand());
        commands.put("getAllTeachers", new GetAllTeachersCommand());
        commands.put("setDirectonActive",new SetDirectonActiveCommand());
        commands.put("createAdvertisement", new CreateAdvertisementComand());
        commands.put("updateAdvertisement", new UpdateAdvertisementComand());
        commands.put("deleteAdvertisement", new DeleteAdvertisementComand());
        commands.put("setGroupActive", new SetGroupActiveCommand());
        commands.put("addNewTopic", new AddNewTopicCommand()); 
        commands.put("getAllDirections", new GetAllDirectionsCommand());
        commands.put("getDirectionData", new GetDirectionDataCommand());
        commands.put("getGroupData", new GetGroupDataCommand());
        commands.put("updateDirection", new UpdateDirectionDataCommand());
        commands.put("updateGroup", new UpdateGroupDataCommand());
		commands.put("interviewAction", new InterviewActionsCommand());
        commands.put("addNewEmployee", new AddNewEmployeeCommand());
        commands.put("setUserActive", new SetUserActiveCommand());
        commands.put("setDirectionHR", new SetDirectionHRCommand());
        commands.put("getAvailableTeachersForGroup", new GetAvailableTeachersForGroupCommand());
        commands.put("setGroupStaff", new SetGroupStaff());
        commands.put("cancelApplicationOnTest", new CancelApplicationOnTestCommand());
		commands.put("interviewResults", new InterviewResultsCommand());
		commands.put("setInterviewResults", new SetTraineeInterviewResultsCommand());
        commands.put("exit", new ExitComand());
        commands.put("getAllTrainee", new GetAllTraineeComand());
        commands.put("getAdvertisementData", new GetAdvertisementDataCommand());
        commands.put("deleteTopic", new DeleteTopicCommand());
        commands.put("getAttachmentsAndDataForTopic", new GetAttachmentsAndDataForTopicCommand());
        commands.put("addAttachmentOfStudent", new AddAttachmentOfStudentCommand());
		commands.put("sendMessageToInterviewFollowers", new SendMessageToInterviewFollowers());
		commands.put("passwordRetrival", new PasswordRetrievalCommand());
        commands.put("getDataAboutStudentById", new GetDataAboutStudentByIdCommand());
        commands.put("deleteAttachment", new DeleteAttachmentCommand());
        commands.put("saveEditionOfTopic", new EditTopicCommand());
        commands.put("registrationEmployee", new RegistrationEmployeeCommand());
        commands.put("deleteStudentAttachment", new DeleteStudentAttachmentCommand());
        commands.put("getChatMessagesWithOffset",  new GetChatMessagesWithOffsetCommand());
        commands.put("getPersonalMessagesWithOffset", new GetPersonalMessagesWithOffsetCommand());
        commands.put("updateUserInfo", new UpdateUserInfoCommand());
        commands.put("getAllTopics", new GetAllTopicsForTeacherCommand());
        commands.put("setLessonActive", new SetLessonActiveCommand());
        commands.put("addNewTest", new AddNewTestCommand());
        commands.put("editTest", new EditTestCommand());
        commands.put("getTopicFiles", new GetTopicAttachmentsCommand());
        commands.put("addLesson", new AddLessonCommand());
        commands.put("sendMessageToGroupStudents", new SendMessageToGroupStudentsCommand());
        commands.put("deleteLesson", new DeleteLessonCommand());
		commands.put("getGroupsByInterviewId", new GetGroupsByInterviewCommand());
		commands.put("getInterviewsByDirectionId", new GetInterviewsByDirectionIdHRCommand());
		commands.put("getStudentsByGroupId", new GetStudentsByGroupIdCommand());
		commands.put("deleteStudent", new DeleteStudentsHRCommand());
		commands.put("retrievePassword", new SaveNewPasswordCommand());
		commands.put("changeTopicInLesson", new ChangeTopicInLesson());
		commands.put("getPaginatedPortionForTestEdit", new TestEditPaginated());
		commands.put("editHomeWork", new EditHomeWorkCommand());
		commands.put("deleteHomeWork", new DeleteHomeWorkCommand());
		commands.put("getTraineesByDirectionId", new GetTraineesHrCommand());
		commands.put("setStudentVisit", new SetStudentVisitingCommand());
		
		commands.put("subscribeTraineeToGetNews", new SubscribeToGetNews());
		commands.put("autoTurnOnLesson", new AutoTurnOnLessonCommand());
		
		commands.put("leaveReview", new LeaveReviewCommand());
		commands.put("deleteReview", new DeleteReviewCommand());
		
		commands.put("getCountOfUnreadedMessages", new GetCountOfUnreadedMessagesCommand());
		commands.put("setTestMark", new SetTaskMarkCommand());

		commands.put("deleteHomeTask", new DeleteHomeTaskCommand());
		commands.put("addNewStudentTask", new AddNewStudentTaskCommand());

		commands.put("changeLanguage", new ChangeLanguageCommand());
		commands.put("getChatParticipants", new GetChatParticipantsCommand());

		commands.put("getUserData", new GetLoginedUserDataCommand());

		commands.put("checkEmail", new CheckEmailCommand());
		commands.put("getTaskById", new GetTaskByIdCommand());
		commands.put("saveTaskEdition", new EditTaskCommand());
		commands.put("processMultipleGradAndGrop",new ProcessMultipleGradAndGrop());
		commands.put("getAllUserDataById", new GetAllUserDataByIdCommand());
		commands.put("getUserGroups", new GetUserGroupsCommand());
		commands.put("documentPreviewPath", new GetAttachmentLocation());
		commands.put("getDialogsForUser", new GetDialogsForUser());
		commands.put("getAllAdsForGroup", new GetAllAddsForGroupCommand());
		commands.put("addNewAdvertisement", new AddNewAdvetisementCommand());
		
		commands.put("deleteNewsItem", new DeleteNewsItemCommand());
		
		commands.put("searchTraineeCommand", new SearchTraineeCommand());

	}
	public AjaxActionCommand getCommand(HttpServletRequest request) {
		String action = request.getParameter(PARAM_NAME_AJAX_COMMAND);
		AjaxActionCommand command = commands.get(action);
		LOGGER.info("Chosen command = " + command.getClass());
		return command;
	}

	public static AjaxActionFactory getInstance() {
		if (instance == null) {
			instance = new AjaxActionFactory();
		}
		LOGGER.info("Get instance of AjaxActionFactory");
		return instance;
	}

}
