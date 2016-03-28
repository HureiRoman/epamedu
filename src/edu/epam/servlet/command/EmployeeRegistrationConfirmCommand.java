package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.service.CommonUserService;
@UserPermissions({RoleType.ANY})
public class EmployeeRegistrationConfirmCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String key = null;
		key = request.getParameter("key");
		
		
		if(!CommonUserService.isKeyUsed(key)){
			return ConfigurationManager.getInstance().getProperty(ConfigurationManager.EMP_REGCONFIRM_PAGE);
		}else{
			request.setAttribute("error", "403");
			return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH); 
		}
		 
	}

}
