package edu.epam.servlet.command;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User.Education;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.UserDataFromSocialNetwork;
import edu.epam.network.AjaxComandExecutor;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;
import edu.epam.service.StudentService;
import edu.epam.service.TraineeService;

@UserPermissions({ RoleType.GUEST })
public class SignInBySocialNetworkCommand implements ActionCommand {
	private SimpleDateFormat dateFormat;

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		String type = request.getParameter("type");
		if (type.equals("VK")) {
			String access_token = request.getParameter("access_token");
			Integer userID = Integer.parseInt(request.getParameter("user_id"));
			System.out.println("at = " + access_token);
			Integer loginWithVkID = CommonUserService.loginWithVk(userID);
			if (loginWithVkID != null) {
				User user = CommonUserService.getUserById(loginWithVkID);
				session.setAttribute(Constants.SESSION_PARAM_NAME_USER, user);
				switch (user.getRoleType()) {
				case STUDENT:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.STUDENT_PANEL_PAGE);
				case TEACHER:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.TEACHER_PANEL_PAGE);
				case TRAINEE:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.TRAINEE_CABINET_PAGE);
				case HR:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.HR_CABINET);
				default:
					break;
				}
			} else {
				String jsonResponse = AjaxComandExecutor.newBuilder()
						.setUrl("https://api.vk.com/method/users.get")
						.addParameter("fields", "bdate,contacts,education")
						.addParameter("name_case", "nom")
						.addParameter("access_token", access_token).build();
				JsonObject responseObject = Json.createReader(
						new StringReader(jsonResponse)).readObject();
				System.out.println(jsonResponse);

				UserDataFromSocialNetwork dataFromSocialNetwork = new UserDataFromSocialNetwork();
				JsonArray jsonArrayOfUsers = responseObject
						.getJsonArray("response");
				JsonObject jsonDataOfUser = null;
				if (jsonArrayOfUsers.size() > 0) {
					jsonDataOfUser = jsonArrayOfUsers.getJsonObject(0);
				}
				if (jsonDataOfUser != null) {

					String firstName = jsonDataOfUser.getString("first_name");
					String lastName = jsonDataOfUser.getString("last_name");
					Long vkId = (long) jsonDataOfUser.getInt("uid");
					if (!jsonDataOfUser.containsKey("mobile_phone")) {
						String phone = jsonDataOfUser.getString("mobile_phone");
						dataFromSocialNetwork.setPhone(phone);
					} else {
						dataFromSocialNetwork.setPhone("");
					}
					if (jsonDataOfUser.containsKey("university_name")
							&& jsonDataOfUser.containsKey("faculty_name")) {
						String educationUniversity = jsonDataOfUser
								.getString("university_name");
						String educationFaculty = jsonDataOfUser
								.getString("faculty_name");
						dataFromSocialNetwork.setEducation(educationUniversity
								+ " " + educationFaculty);
					} else {
						dataFromSocialNetwork.setEducation("");
					}
					if (jsonDataOfUser.containsKey("bdate")) {
						String bdate = jsonDataOfUser.getString("bdate");
						Date birthDate = null;
						if (bdate != null) {
							try {
								SimpleDateFormat birthDateSdf = new SimpleDateFormat(
										"dd.M.yyyy");
								birthDate = birthDateSdf.parse(bdate);

								dateFormat = new SimpleDateFormat(
										"dd MMMM, yyyy", Locale.US);
								String birthDateString = dateFormat
										.format(birthDate);

								dataFromSocialNetwork
										.setUserBirth(birthDateString);
							} catch (ParseException e) {
								e.printStackTrace();
								dataFromSocialNetwork.setUserBirth("");
							}
						}
					}
					dataFromSocialNetwork.setSocialId(vkId);
					dataFromSocialNetwork.setEmail("");
					dataFromSocialNetwork.setDataFrom(type);
					dataFromSocialNetwork.setFirstName(firstName);
					dataFromSocialNetwork.setLastName(lastName);

				}
				request.setAttribute("userData", dataFromSocialNetwork);
				return ConfigurationManager.getInstance().getProperty(
						ConfigurationManager.REGISTRATION);
			}
		} else {
			UserDataFromSocialNetwork dataFromSocialNetwork = new UserDataFromSocialNetwork();
			String fbIdString = request.getParameter("fbId").trim();
			Long fbId = Long.parseLong(fbIdString);
			Integer loginWithFBID = CommonUserService.loginWithFB(fbId);
			if (loginWithFBID != null) {
				User user = CommonUserService.getUserById(loginWithFBID);
				RoleType roleType = user.getRoleType();
				if(roleType == RoleType.TRAINEE) {
					session.setAttribute(Constants.SESSION_PARAM_NAME_USER, TraineeService.getTraineeById(loginWithFBID));
				} else if(roleType == RoleType.STUDENT) {
					session.setAttribute(Constants.SESSION_PARAM_NAME_USER, StudentService.getDataAboutStudentById(loginWithFBID));
				}

				switch (user.getRoleType()) {
				case STUDENT:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.STUDENT_PANEL_PAGE);
				case TEACHER:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.TEACHER_PANEL_PAGE);
				case TRAINEE:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.TRAINEE_CABINET_PAGE);
				case HR:
					return ConfigurationManager.getInstance().getProperty(
							ConfigurationManager.HR_CABINET);
				default:
					break;
				}
			} else {
				dataFromSocialNetwork.setDataFrom(type);
				dataFromSocialNetwork.setSocialId(fbId);
				request.setAttribute("userData", dataFromSocialNetwork);
				return ConfigurationManager.getInstance().getProperty(
						ConfigurationManager.REGISTRATION);
			}
		}
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.MAIN_PAGE_PATH);
	}

}
