package edu.epam.controllerServlet;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.epam.servlet.command.ActionCommand;
import edu.epam.servlet.command.AdvertisementComand;
import edu.epam.servlet.command.EditTestsCommand;
import edu.epam.servlet.command.EmployeeRegistrationConfirmCommand;
import edu.epam.servlet.command.GroupInfoCommand;
import edu.epam.servlet.command.InfoAboutStudentsOfGroupCommand;
import edu.epam.servlet.command.ManageAllUsersCommand;
import edu.epam.servlet.command.NoCommand;
import edu.epam.servlet.command.OpenAdminCoursesManagement;
import edu.epam.servlet.command.OpenAdminEmployeeManagement;
import edu.epam.servlet.command.OpenAdminNewsManagement;
import edu.epam.servlet.command.OpenDialogWithUserCommand;
import edu.epam.servlet.command.OpenDialogsListCommand;
import edu.epam.servlet.command.OpenStudentCabinetCommand;
import edu.epam.servlet.command.RedirectCommand;
import edu.epam.servlet.command.RedirectToRetrievePasswordPageCommand;
import edu.epam.servlet.command.RegistrationConfirmCommand;
import edu.epam.servlet.command.ShowAdminCabinetCommand;
import edu.epam.servlet.command.ShowEmployeesManagementCommand;
import edu.epam.servlet.command.ShowGradesOfTasksCommand;
import edu.epam.servlet.command.ShowInterviewResultsCommand;
import edu.epam.servlet.command.ShowInterviewsCommand;
import edu.epam.servlet.command.ShowStudentGroupPage;
import edu.epam.servlet.command.ShowStudentsCommand;
import edu.epam.servlet.command.ShowStudentsVisitingCommand;
import edu.epam.servlet.command.ShowTeacherCabinetCommand;
import edu.epam.servlet.command.ShowTestsRateCommand;
import edu.epam.servlet.command.ShowTraineesCommand;
import edu.epam.servlet.command.ShowVisitingStatisticCommand;
import edu.epam.servlet.command.SignInBySocialNetworkCommand;
import edu.epam.servlet.command.StartWithSocialNetroworkCommand;
import edu.epam.servlet.command.TestsCommand;
import edu.epam.servlet.command.TestsResultCommand;
import edu.epam.servlet.command.TopicsCommand;
import edu.epam.servlet.command.ViewGroupsCommand;
import edu.epam.servlet.command.WrongCommand;

public class ActionFactory {
	static final Logger LOGGER = Logger.getLogger(ActionFactory.class);
	private static final String PARAM_NAME_COMMAND = "command";
	private static ActionFactory instance = null;
	HashMap<String, ActionCommand> commands = new HashMap<String, ActionCommand>();

	private ActionFactory() {
		commands.put("redirect", new RedirectCommand());
		commands.put("tests", new TestsCommand());
		commands.put("test_result", new TestsResultCommand());
		commands.put("regconfirm", new RegistrationConfirmCommand());
		commands.put("showGroups", new ViewGroupsCommand());
		commands.put("teacher", new ShowTeacherCabinetCommand());
		commands.put("groupInfo", new GroupInfoCommand());
		commands.put("infoAboutStudentsOfGroup", new InfoAboutStudentsOfGroupCommand());
		commands.put("topics", new TopicsCommand());
		commands.put("testRate", new ShowTestsRateCommand());
		commands.put("showAdvertisement", new AdvertisementComand());
		commands.put("testsEdit", new EditTestsCommand());
		commands.put("viewStudentsOfGroup", new InfoAboutStudentsOfGroupCommand());
		commands.put("setNewPassword", new RedirectToRetrievePasswordPageCommand());
		
		//for admin:
		commands.put("admin", new ShowAdminCabinetCommand());
		commands.put("manageNews", new OpenAdminNewsManagement());
		commands.put("manageEmployee", new OpenAdminEmployeeManagement());
		commands.put("manageCourses", new OpenAdminCoursesManagement());
		commands.put("viewTrainees", new ShowTraineesCommand());
		commands.put("manageEmoloyees", new ShowEmployeesManagementCommand() );
		commands.put("employeeRegconfirm", new EmployeeRegistrationConfirmCommand());

		commands.put("showVisiting", new ShowStudentsVisitingCommand());
		commands.put("showVisitingStatistic", new ShowVisitingStatisticCommand());
		commands.put("showGradesOfTasks", new ShowGradesOfTasksCommand());
		
		//for hr
		commands.put("manageInterviews", new ShowInterviewsCommand());
		commands.put("interviewResults", new ShowInterviewResultsCommand());
		commands.put("showMyGroup", new ShowStudentGroupPage());
		commands.put("showStudents", new ShowStudentsCommand());

		commands.put("dialogs", new OpenDialogsListCommand());		
		commands.put("dialog", new OpenDialogWithUserCommand());
		//for student
		commands.put("student", new OpenStudentCabinetCommand());
		commands.put("manageAllUsers", new ManageAllUsersCommand());
		commands.put("startWithSocialNetwork", new StartWithSocialNetroworkCommand());
		commands.put("signInBySocialNetwork", new SignInBySocialNetworkCommand());
	}

	public ActionCommand getCommand(HttpServletRequest request) {
		String action = request.getParameter(PARAM_NAME_COMMAND);
		ActionCommand command = commands.get(action);
		if (command == null) {
			if(action == null || action == "") {
				//main
				command = new NoCommand();
			}
			else{
				//404
				command = new WrongCommand();
			}
		}
		return command;
	}

	public static ActionFactory getInstance() {
		if (instance == null) {
			instance = new ActionFactory();
		}
		return instance;
	}
}
