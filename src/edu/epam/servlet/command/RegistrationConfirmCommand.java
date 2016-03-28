package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.Trainee;
import edu.epam.service.CommonUserService;
import edu.epam.service.TraineeService;

/**
 * Created by fastforward on 10/06/15.
 */
@UserPermissions({RoleType.GUEST})
public class RegistrationConfirmCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {

        CommonUserService.deleteNotConfirmUsersOlderThenOneDay();

        String key = request.getParameter("key");
        String email = request.getParameter("user");
        if(key != null && email != null) {
            if(!CommonUserService.isEmailExist(email)) {
                System.out.println("mail doesn't exist");
            }
            else if(CommonUserService.isKeyUsed(key)) {
                System.out.println("user allready activated");
            }
            else {
                int id = CommonUserService.userActivation(email, key);
                System.out.println(id);
                if(id != -1) {
                    TraineeService.insertAdditionalInfo(id, false, true); // modified table by Roman Bodak
                    Trainee trainee = TraineeService.getTrainee(email);
                    request.getSession().setAttribute(
                            Constants.SESSION_PARAM_NAME_USER, trainee);
                    System.out.println("user activated");
                }
            }
        }
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.TRAINEE_CABINET_PAGE);
    }
}
