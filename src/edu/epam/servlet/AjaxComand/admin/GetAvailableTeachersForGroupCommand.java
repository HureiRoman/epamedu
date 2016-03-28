package edu.epam.servlet.AjaxComand.admin;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.role.Teacher;
import edu.epam.service.TeacherService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({ RoleType.ADMIN })
public class GetAvailableTeachersForGroupCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String groupIdStr = request.getParameter("id");
		Integer groupId = Integer.parseInt(groupIdStr);
		List<Teacher> teachers = TeacherService.getAvailableTeachersForGroup(groupId);
		XStream stream = new XStream(new StaxDriver());
		stream.alias("teacher", Teacher.class);
		String xml = stream.toXML(teachers);
		return xml;
	}

}
