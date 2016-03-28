package edu.epam.servlet.command;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Group;
import edu.epam.role.HR;
import edu.epam.service.DirectionService;
import edu.epam.service.GroupService;

@UserPermissions({RoleType.HR})
public class ShowStudentsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        HR hr = (HR) request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
        Map<Direction, List<Group>> directionMap = new HashMap<>();
        List<Direction> directions = DirectionService.getDirectionsByHRId(hr.getId());
        for(Direction direction : directions) {
            directionMap.put(direction, GroupService.getListOfActiveGroupsForDirection(direction.getId()));
        }
        request.setAttribute("directions", directionMap);
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.HR_SHOW_STUDENTS_PAGE);
    }
}
