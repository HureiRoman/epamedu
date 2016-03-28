package edu.epam.servlet.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import edu.epam.role.HR;
import edu.epam.role.Trainee;
import edu.epam.service.DirectionService;
import edu.epam.service.TraineeService;

/**
 * Created by fastforward on 18/06/15.
 */
@UserPermissions({RoleType.HR})
public class ViewTraineesCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        Map<Direction, List<Trainee>> directionMap = new HashMap<>();
        HR hr = (HR)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
        for(Direction direction : DirectionService.getDirectionsByHRId(hr.getId())) {
            List<Trainee> trainees = TraineeService.getTraineesByApplicationDirectionId(direction.getId());
            LinkedHashSet<Trainee> lhs = new LinkedHashSet<Trainee>();
            lhs.addAll(trainees);
            trainees.clear();
            trainees.addAll(lhs);
            directionMap.put(direction, trainees);
        }
        request.setAttribute("directions", directionMap);
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.VIEW_TRAINEES_PAGE);
    }
}
