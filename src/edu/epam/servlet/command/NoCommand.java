package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;

@UserPermissions({RoleType.ANY})
public class NoCommand implements ActionCommand{
	static final Logger LOGGER = Logger.getLogger(NoCommand.class);


	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response,Locale locale) throws ServletException, IOException {
		LOGGER.info("execute NoCommand");
		//main
		
		return "";
	}
    
}