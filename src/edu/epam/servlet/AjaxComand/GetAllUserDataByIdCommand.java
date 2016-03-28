package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.CV;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;
import edu.epam.service.CommonUserService;
import edu.epam.service.StudentService;
@UserPermissions({ RoleType.ANY })
public class GetAllUserDataByIdCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yy");
		
		Integer targetUserId = Integer.parseInt(request.getParameter("id"));
		
		CommonUser user = CommonUserService.getUserById(targetUserId);
		
		JsonObjectBuilder cvBuilder = Json.createObjectBuilder();
		CV cv = null;
		if(user.getRoleType() == RoleType.STUDENT){
			Student studentById = StudentService.getDataAboutStudentById(targetUserId);
			cv = studentById.getCv();
		}
		
		if(cv!=null){
			cvBuilder.add("phone", cv.getPhone())
			.add("english", cv.getEnglishLevel().name())
			.add("education", cv.getEducation())
			.add("addInfo", cv.getAdditionalInfo())
			.add("objective", cv.getObjective())
			.add("skills", cv.getSkills())
			.add("birth", dateFormat.format(cv.getBirth()));			
		}
		
		return Json.createObjectBuilder()
				 .add("email",user.getEmail())
				.add("firstName", user.getFirstName())
				.add("lastName", user.getLastName())
				.add("parentName", user.getParentName())
				.add("id", user.getId())
				.add("role", user.getRoleType().toString())
				.add("cv", cvBuilder.build())
				.build().toString();
	}

}
