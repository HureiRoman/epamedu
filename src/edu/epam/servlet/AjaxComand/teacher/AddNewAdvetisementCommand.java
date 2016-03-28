package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Advertisement;
import edu.epam.role.Teacher;
import edu.epam.service.AdvertisementService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({ RoleType.TEACHER })
public class AddNewAdvetisementCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		String title = null;
		String content = null;
		Integer idGroup = null;
		Integer idTeacher = null;
		
		HttpSession session = request.getSession();
		Teacher teacher = (Teacher) session.getAttribute("logined_user");
		idTeacher = teacher.getId();
		
		title = request.getParameter("title");
		content = request.getParameter("content");
		idGroup = Integer.parseInt(request.getParameter("groupId"));
		
		Advertisement advertisement = new Advertisement();
		advertisement.setTitle(title);
		advertisement.setContent(content);
		advertisement.setIdGroup(idGroup);
		advertisement.setIdTeacher(idTeacher);
		Integer res = AdvertisementService.createNewAdvertisement(advertisement);
		
		return Json.createObjectBuilder().add("result", res>0).build().toString();
	}

}
