package edu.epam.servlet.AjaxComand;

import java.io.IOException;
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
import edu.epam.constants.RoleType;
import edu.epam.persistance.HashUtils;
import edu.epam.role.CommonUser;
import edu.epam.role.User;
import edu.epam.service.CommonUserService;

@UserPermissions({ RoleType.GUEST,RoleType.HR,RoleType.TEACHER })
public class RegistrationEmployeeCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

		int result = -1;

		String language = locale.getLanguage();

		String email = null;
		String fname = null;
		String lname = null;
		String pname = null;
		String password = null;
		String passwordRepeat = null;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 20);
		ServletFileUpload upload = new ServletFileUpload(factory);

		List<FileItem> items = upload.parseRequest(request);
		//FileItem img = null;
		
		for (FileItem item : items) {
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				switch (fieldName) {
				case "email":
					email = item.getString("UTF-8").trim();
					break;
				case "fname":
					fname = item.getString("UTF-8").trim();
					break;
				case "lname":
					lname = item.getString("UTF-8").trim();
					break;
				case "pname":
					pname = item.getString("UTF-8").trim();
					break;
				case "password":
					password = item.getString("UTF-8").trim();
					break;
				case "password_repeat":
					passwordRepeat = item.getString("UTF-8").trim();
					break;
				}
			}
		}
		Integer id = CommonUserService.getIdByEmail(email);	
		System.out.println("ідшка = " +  id);
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		StringBuilder responseBuilder = new StringBuilder();
		if (password.equals(passwordRepeat)) {
			try {
				User employee = new User();
				employee.setFirstName(fname);
				employee.setLastName(lname);
				employee.setParentName(pname);
				employee.setPassword(HashUtils.getMD5(password, email));
				employee.setEmail(email);
				employee.setId(id);
				employee.setIsActive("true");
				employee.setLang(language);
				employee.setIsKeyActive("true");
//				employee.set
				result = CommonUserService.updateEmployeeProfile(employee);
				
			} catch (Exception e) {
				e.printStackTrace();
				result = 3;// some value is invalid
			}
			
			
			CommonUser logined_employee= null;
			logined_employee = CommonUserService.checkLogin(email, password);
			String direction ="";
			
			
			request.setAttribute(Constants.SESSION_PARAM_NAME_LOCALE, language);
			request.getSession().setAttribute(
					Constants.SESSION_PARAM_NAME_USER, logined_employee);
			
			RoleType roleType = logined_employee.getRoleType();
			System.out.println("ROLE = "+ logined_employee.getRoleType());
			switch (roleType) {
			case HR:
				  direction="hr_cabinet";
				break;
			case TEACHER:
				  direction="teacher_cabinet";
				break;
			}
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!DIRECTION = " + direction);
			responseBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
			responseBuilder.append("<data>");
			responseBuilder.append("<cabinet_url>");
			responseBuilder.append(direction );
			responseBuilder.append("</cabinet_url>");
		
		
		} else {
			result = 2;
		}
		
		responseBuilder.append("<status>");
		responseBuilder.append(result>0?"1":"2");
		responseBuilder.append("</status>");
		responseBuilder.append("</data>");
		return responseBuilder.toString();
	}

}
