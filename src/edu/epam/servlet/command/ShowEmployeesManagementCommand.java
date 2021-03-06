package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.ADMIN})
public class ShowEmployeesManagementCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		
		List<CommonUser> employees = CommonUserService.getAllEmployees();
		
		request.setAttribute("employees", employees);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.ADMIN_EMPLOYEES_MANAGEMENT);
	}

}
