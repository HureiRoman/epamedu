package edu.epam.servlet.AjaxComand.hr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.model.Message;
import edu.epam.persistance.EmailMessenger;
import edu.epam.role.CommonUser;
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.PersonalMessageService;
import edu.epam.service.TraineeService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.HR})
public class DoStudentsCommand implements AjaxActionCommand  {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws ServletException, IOException,
			Exception {
		int result = 0;
		HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);


		String traineeIdsString[] = request.getParameterValues("trainee_ids[]");
		String groupIdString = request.getParameter("group_id");
		String interviewIdString = request.getParameter("interview_id");
		String informOthers = request.getParameter("inform");


		String studentSubject = "congratulations";
		String traineeSubject = "sorry";
		String studentMessageText = "Congratulations, You Added to Students";
		String traineeMessageText = "Sorry, You at reserv";

		Message message = new Message();
		message.setSender(hr.getId());
		message.setMessageTime(new Date());
		message.setText(studentMessageText);

		if (traineeIdsString != null && groupIdString != null && informOthers != null) {
			try {
				Integer groupId = Integer.parseInt(groupIdString);
				int interviewId = Integer.parseInt(interviewIdString);

				List<Trainee> futureStudents = new ArrayList<>();
				for (String id : traineeIdsString) {
					int traineeId = Integer.parseInt(id.substring(7));

					Trainee trainee = TraineeService.getTraineeById(traineeId);
					futureStudents.add(trainee);
					message.setReceiver(trainee.getId());
					PersonalMessageService.addChatMessageToDB(message);
					TraineeService.doStudent(traineeId, groupId);

				}
				sendMail(request, futureStudents, studentSubject, studentMessageText);

				System.out.println("informOthers " + informOthers);
				if (informOthers.equals("true")) {
					List<Trainee> trainees = TraineeService.getTraineesByInterviewId(interviewId);
					message.setText(traineeMessageText);
					for(Trainee trainee : trainees) {
						message.setReceiver(trainee.getId());
						PersonalMessageService.addChatMessageToDB(message);
					}
					sendMail(request, trainees, traineeSubject, traineeMessageText);
				}

			} catch (Exception e) {
                e.printStackTrace();
                result = 2;
            }
		} else {
			result = 2;
		}



		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
		responseBuilder.append(" <status>");
		responseBuilder.append(result);
		responseBuilder.append("</status>");
		return responseBuilder.toString();
	}

	private boolean sendMail(HttpServletRequest request, List<? extends CommonUser> toUsers, String subject, String content) {
		ServletContext context = request.getSession().getServletContext();
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");

		try {
			EmailMessenger.sendEmailToGroupOfUsers(host, port, user, pass, toUsers, subject, content);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

