package edu.epam.servlet.AjaxComand;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.service.CommonUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by fastforward on 07/07/15.
 */
@UserPermissions({RoleType.GUEST, RoleType.ADMIN})
public class CheckEmailCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 0;
        String email = request.getParameter("email");

        if(email != null) {
            if (CommonUserService.isEmailExist(email)) {
                result = 1;
            }
        }

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append("<status>");
        responseBuilder.append(result);
        responseBuilder.append("</status>");
        return responseBuilder.toString();
    }
}
