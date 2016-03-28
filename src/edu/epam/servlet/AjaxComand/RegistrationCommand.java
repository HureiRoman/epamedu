package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.EnglishLevel;
import edu.epam.constants.RoleType;
import edu.epam.model.CV;
import edu.epam.persistance.EmailMessenger;
import edu.epam.persistance.HashUtils;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;
import edu.epam.service.CommonUserService;
import edu.epam.service.TraineeService;
import edu.epam.validation.Validator;

@UserPermissions({RoleType.GUEST})
public class RegistrationCommand implements AjaxActionCommand {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, Locale locale)
            throws ServletException, IOException, Exception {

        int result = 1;

        CommonUserService.deleteNotConfirmUsersOlderThenOneDay();


        String language = locale.getLanguage();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String pname = request.getParameter("pname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password_repeat");
        String phone = request.getParameter("phone");
        String birth = request.getParameter("birth");
        String objective = request.getParameter("objective");
        String skills = request.getParameter("skills");
        String additionalInfo = request.getParameter("additional_info");
        String education = request.getParameter("education");
        String englishLevel = request.getParameter("english_level");
        String uuid = UUID.randomUUID().toString();
        String dataFrom = request.getParameter("data_from");
        String socialIDString = request.getParameter("socialID");


        //validation
        if (fname == null || lname == null ||
                pname == null || email == null ||
                password == null || passwordRepeat == null || phone == null ||
                birth == null || objective == null ||
                skills == null || additionalInfo == null ||
                education == null || englishLevel == null ||
                dataFrom == null || socialIDString == null ||
                fname.length() > 16 || lname.length() > 16 ||
                pname.length() > 16 || password.length() > 30 ||
                passwordRepeat.length() > 30 || objective.length() > 250 ||
                skills.length() > 250 || education.length() > 250 ||
                additionalInfo.length() > 250 || email.length() > 30) {
            result = 8;
        } else if (CommonUserService.isEmailExist(email)) {
            result = 2;//user already exist
        } else if (!passwordIsCorrect(request, password, passwordRepeat)) {
            result = 3; //passwords dont equal
        } else if (!birth.equals("")) {
            try {

                Trainee trainee = new Trainee();
                trainee.setFirstName(fname);

                trainee.setLastName(lname);
                trainee.setParentName(pname);
                trainee.setPassword(HashUtils.getMD5(password, email));
                trainee.setConfirm_key(uuid);
                trainee.setRegistration_date(new Date());
                trainee.setEmail(email);
                trainee.setLang(language);

                CV cv = new CV();
                cv.setPhone(phone);

                java.util.Date date = simpleDateFormat.parse(birth);
                cv.setBirth(date);
                cv.setObjective(objective);
                cv.setSkills(skills);
                cv.setAdditionalInfo(additionalInfo);
                cv.setEducation(education);

                if (englishLevelContains(englishLevel)) {
                    cv.setEnglishLevel(englishLevel);
                }

                trainee.setCv(cv);
                if (Validator.isRegexValid(trainee.getCv()) && Validator.isRegexValid((CommonUser) trainee)) {
                    sendMail(request, trainee.getEmail(), uuid);
                    int traineeId = TraineeService.createTrainee(trainee);
                    if(!dataFrom.equals("")) {
                        if(dataFrom.equals("FB")) {
                            TraineeService.setFBId(traineeId, socialIDString);
                        } else if (dataFrom.equals("VK")) {
                            TraineeService.setVKId(traineeId, socialIDString);
                        }
                    }
                } else {

                    result = 4;//some value is invalid
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = 4;//some value is invalid
            }
        } else {
            result = 4;
        }
        //pop-up window to validate the account via email
        //return сторінку, де юзер реєструвався
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <status>");
        responseBuilder.append(result);
        responseBuilder.append("</status>");
        return responseBuilder.toString();
    }


    private boolean passwordIsCorrect(HttpServletRequest request, String password, String passwordRepeat) {
        if (password == null || passwordRepeat == null) {
            if (password == null) {
                request.setAttribute("errorMessage", "You should create new password.");
            } else {
                request.setAttribute("errorMessage", "Passwords do not match.");
            }
        } else
            return (password.equals(passwordRepeat));
        return false;
    }

    //true if sended, false if not sended
    private boolean sendMail(HttpServletRequest request, String recipient, String uuid) {
        ServletContext context = request.getSession().getServletContext();
        String host = context.getInitParameter("host");
        String port = context.getInitParameter("port");
        String user = context.getInitParameter("user");
        String pass = context.getInitParameter("pass");
        String subject = "Registration on epam.edu";
        InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
        String ip = inetAddress.getHostAddress();
        String content = "<a href=\"http://"+ip+":8080/EpamEducationalProject/Controller?command=regconfirm&key=" + uuid
                + "&user=" + recipient + "\">Confirm registration on epam.edu</a>";
        try {
            EmailMessenger.sendEmail(host, port, user, pass, recipient, subject, content);

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean englishLevelContains(String englishLevel) {

        for (EnglishLevel level : EnglishLevel.values()) {
            if (level.name().equals(englishLevel)) {
                return true;
            }
        }
        return false;
    }
}
