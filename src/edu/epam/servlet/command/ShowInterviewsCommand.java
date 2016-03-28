package edu.epam.servlet.command;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.role.HR;
import edu.epam.service.DirectionService;

/**
 * Created by fastforward on 19/06/15.
 */

@UserPermissions({ RoleType.HR})
public class ShowInterviewsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        HR hr = (HR) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);

        List<Direction> directions = DirectionService.getDirectionsByHRId(hr.getId());

        request.setAttribute("directions", directions);
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.HR_INTERVIEWS_PAGE);
    }
}
