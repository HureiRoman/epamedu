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

@UserPermissions({RoleType.GUEST})
public class RedirectToRetrievePasswordPageCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		  String key = request.getParameter("key");
	        String email = request.getParameter("user");
	        if(key != null && email != null) {
	            if(!CommonUserService.isEmailExist(email)) {
	                System.out.println("mail doesn't exist");
	                return ConfigurationManager.getInstance().getProperty(
	    	                ConfigurationManager.ERROR_PAGE_PATH); 
	            }
	            else if(CommonUserService.isKeyUsed(email)) {
	                System.out.println("user allready retrieved his password");
	                return ConfigurationManager.getInstance().getProperty(
	    	                ConfigurationManager.ERROR_PAGE_PATH);
	            }
	            else if(CommonUserService.checkKey(key,email)) {
	            	request.setAttribute("email",email);
	            	CommonUserService.setKeyUsedByEmail(email);
	            	 return ConfigurationManager.getInstance().getProperty(
	     	                ConfigurationManager.RETRIEVE_PASSWORD_PAGE);
	            }
	            //Зміна паролю, перед тим вставити кейконфірм і ісКейЮзед 
	        }
	        return ConfigurationManager.getInstance().getProperty(
	                ConfigurationManager.ERROR_PAGE_PATH);
	       
	    }
}