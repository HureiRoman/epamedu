package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.exceptions.IncorrectPasswordInput;
import edu.epam.persistance.EmailMessenger;
import edu.epam.persistance.HashUtils;
import edu.epam.role.CommonUser;
import edu.epam.service.CommonUserService;

/**
 * Created by fastforward on 17/06/15.
 */
@UserPermissions({RoleType.TRAINEE,RoleType.HR,RoleType.TEACHER, RoleType.STUDENT, RoleType.ADMIN,RoleType.GRADUATE})
public class ChangePasswordCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 1;

        String oldPass = request.getParameter("old_password");
        String newPass = request.getParameter("new_password");
        String newPassRepeat = request.getParameter("confirm_new_password");

        CommonUser user = null;
        CommonUser loginedUser = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);

        System.out.println(loginedUser);


        if(oldPass != null && newPass != null && newPassRepeat != null) {
            if(newPass.equals(newPassRepeat)){
                if (newPass.length() >= 4) {
                    try {
                        user = CommonUserService.checkLogin(loginedUser.getEmail(), oldPass);
                        if (null != user) {
                            CommonUserService.changePassword(loginedUser.getId(), HashUtils.getMD5(newPass, loginedUser.getEmail()));
                            sendMessage(request, user.getEmail(), "you password was changed");
                        }
                    } catch (IncorrectPasswordInput e) {
                        result = 4;//bad pass
                    }
                } else {
                    result = 5;//bad length
                }
            } else {
                result = 3;//pass not equal
            }
        } else {
            result = 2;//enter all data
        }


        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <status>");
        responseBuilder.append(result);
        responseBuilder.append("</status>");
        return responseBuilder.toString();
    }

    private boolean sendMessage(HttpServletRequest request, String recipient, String content) {
        ServletContext context = request.getSession().getServletContext();
        String host = context.getInitParameter("host");
        String port = context.getInitParameter("port");
        String user = context.getInitParameter("user");
        String pass = context.getInitParameter("pass");
        String subject = "Changing password";

        try {
            EmailMessenger.sendEmail(host, port, user, pass, recipient, subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
