package edu.epam.servlet.AjaxComand;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.EnglishLevel;
import edu.epam.constants.ImageType;
import edu.epam.constants.RoleType;
import edu.epam.model.CV;
import edu.epam.persistance.ImageManager;
import edu.epam.role.CommonUser;
import edu.epam.role.Graduate;
import edu.epam.role.Student;
import edu.epam.role.Trainee;
import edu.epam.service.CommonUserService;
import edu.epam.validation.Validator;

/**
 * Created by fastforward on 16/06/15.
 */
@UserPermissions({RoleType.TRAINEE,RoleType.GRADUATE,RoleType.STUDENT})
public class UpdateUserInfoWithCVCommand implements AjaxActionCommand {

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 10MB

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        int result = 1;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);

        System.out.println("edit info command");

        String fname = null;
        String lname = null;
        String pname = null;
        String phone = null;
        String birth = null;
        String objective = null;
        String skills = null;
        String additionalInfo = null;
        String education = null;
        String englishLevel = null;
        String role=null;
        String direction = "";
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024 * 20);
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> items = upload.parseRequest(request);
        FileItem img = null;
        Integer traineeId = null;
        for(FileItem item : items) {
            String fieldName = item.getFieldName();
            //System.out.println(fieldName);
            if(item.isFormField()) {
                switch (fieldName) {
                    case "fname" : fname = item.getString("UTF-8").trim(); break;
                    case "lname" : lname = item.getString("UTF-8").trim(); break;
                    case "pname" : pname = item.getString("UTF-8").trim(); break;
                    case "phone" : phone = item.getString("UTF-8").trim(); break;
                    case "birth" : birth = item.getString("UTF-8").trim(); break;
                    case "objective" : objective = item.getString("UTF-8").trim(); break;
                    case "skills" : skills = item.getString("UTF-8").trim(); break;
                    case "additional_info" : additionalInfo = item.getString("UTF-8").trim(); break;
                    case "education" : education = item.getString("UTF-8").trim(); break;
                    case "english_level" : englishLevel = item.getString("UTF-8").trim(); break;
                    case "role" : role=item.getString("UTF-8").trim(); break;
                }
            } else {
                img = item;
            }
        }
        //validation
        if(fname  == null  || lname  == null  ||
                pname  == null  || phone  == null  ||
                birth  == null  ||  englishLevel  == null) {
            result = 8;
        }
        System.out.println("FNAME: "+fname);
        CommonUser user=null;
        
        if(!birth.equals("")) {
            try {
              if(role.equalsIgnoreCase("TRAINEE"))  { user = (Trainee)request.getSession().getAttribute("logined_user"); }
              else  if(role.equalsIgnoreCase("GRADUATE")) { user = (Graduate)request.getSession().getAttribute("logined_user"); }
              else  if(role.equalsIgnoreCase("STUDENT")) { user = (Student)request.getSession().getAttribute("logined_user"); } 
                user.setFirstName(fname);
                user.setLastName(lname);
                user.setParentName(pname);

                CV cv = new CV();
                cv.setPhone(phone);

                java.util.Date date = simpleDateFormat.parse(birth);
                cv.setBirth(date);
                cv.setObjective(objective);
                cv.setSkills(skills);
                cv.setAdditionalInfo(additionalInfo);
                cv.setEducation(education);
                cv.setEnglishLevel(englishLevel);
                if(englishLevelContains(englishLevel)) {
                    cv.setEnglishLevel(englishLevel);
                }

          
                if(Validator.isRegexValid(cv) && Validator.isRegexValid(user)) {
                        if(img != null) {
                            result = checkPhoto(img);
                            if(result == 1) {
                                CommonUserService.updateUserWithCV(user,cv);
//                              loadImage(img, user.getId());
                                ImageManager.loadImage(request, img, ImageType.USERS, user.getId());
                            }
                        } else {
                        	  CommonUserService.updateUserWithCV(user,cv);
                        }
                        if(role.equals("STUDENT"))
                        {  Student student=(Student)user;
                        direction = "student_cabinet";
                        student.setCv(cv); 
                        request.getSession().setAttribute(
                                Constants.SESSION_PARAM_NAME_USER, student);}
                        if(role.equals("TRAINEE"))
                        {  Trainee trainee=(Trainee)user;
                        direction = "TRAINEE_CABINET_PAGE";
                        trainee.setCv(cv); 
                        request.getSession().setAttribute(
                                Constants.SESSION_PARAM_NAME_USER, trainee);}
                        if(role.equals("GRADUATE"))
                        {  Graduate graduate=(Graduate)user;
                        direction = "TRAINEE_CABINET_PAGE";
                        graduate.setCv(cv); 
                        request.getSession().setAttribute(
                                Constants.SESSION_PARAM_NAME_USER, graduate);}
                        }
                else {
                    result = 4;//some value is invalid
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = 4;//some value is invalid
            }
        }
        else {
            result = 4;
        }
        //pop-up window to validate the account via email
        //return сторінку, де юзер реєструвався
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        StringBuilder responseBuilder = new StringBuilder();
       
        responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        responseBuilder.append(" <result>");
	        responseBuilder.append(" <status>");
	        responseBuilder.append(result);
	        responseBuilder.append("</status>");
	        responseBuilder.append("<cabinet_url>");
			responseBuilder.append(direction );
			responseBuilder.append("</cabinet_url>");
		responseBuilder.append(" </result>");
        
        
        
        return responseBuilder.toString();
    }

    private void loadImage(FileItem item, Integer traineeId) throws Exception {
        String serverPath = System.getenv("CATALINA_HOME");
        File usersPhotoFolder = new File(serverPath+"/storage/images/users");

        if(!usersPhotoFolder.exists()){
            usersPhotoFolder.mkdirs();
        }
        File newsItemFile = new File(serverPath+"/storage/images/users/image"+traineeId+".jpg");
        item.write(newsItemFile);
    }

    public int checkPhoto(FileItem item) {
        int result = 1;
        String fileName = item.getName();
        long fileSize = item.getSize();
        List<String> extensions = new ArrayList<>();
        extensions.add("jpg");
        extensions.add("jpeg");
        extensions.add("gif");
        extensions.add("png");
        extensions.add("JPG");
        extensions.add("JPEG");
        extensions.add("GIF");
        extensions.add("PNG");
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        if(!extensions.contains(extension)) {
            result = 5;//incorect image extension
        } else if(fileSize > MAX_FILE_SIZE) {
            result = 6;// incorrect image size
        }
        return result;
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
