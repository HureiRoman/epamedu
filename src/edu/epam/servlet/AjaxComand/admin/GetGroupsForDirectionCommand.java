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
import edu.epam.model.Group;
import edu.epam.service.GroupService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;
@UserPermissions({RoleType.ADMIN})
public class GetGroupsForDirectionCommand implements AjaxActionCommand {
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {		
		String directionIdStr = request.getParameter("id");
		Integer directionId = Integer.parseInt(directionIdStr);
		List<Group> groups = GroupService.getListOfGroupsForDirection(directionId);
		XStream stream = new XStream(new StaxDriver());
		stream.alias("group", Group.class);
		String xml = stream.toXML(groups);
		return xml;
	}
}
