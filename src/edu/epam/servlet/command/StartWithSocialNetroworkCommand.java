package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;

@UserPermissions({ RoleType.GUEST })
public class StartWithSocialNetroworkCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
        
		String type = request.getParameter("type");
		if(type.equals("FB")){
			System.out.println("FAcebOOK");
			String code = request.getParameter("code");
			request.setAttribute("code", code);
		}
		request.setAttribute("type", type);
		return ConfigurationManager.getInstance().getProperty(
				ConfigurationManager.GET_TOKEN_PAGE);
	}

}
