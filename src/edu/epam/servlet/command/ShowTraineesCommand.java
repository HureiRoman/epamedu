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
 * Created by fastforward on 04/07/15.
 */
@UserPermissions({ RoleType.HR})
public class ShowTraineesCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);

        List<Direction> directions = DirectionService.getDirectionsByHRId(hr.getId());

        System.out.println("directions " + directions);

        request.setAttribute("directions", directions);
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.VIEW_TRAINEES_PAGE);
    }
}
