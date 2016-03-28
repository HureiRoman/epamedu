package edu.epam.controllerServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;
import edu.epam.service.AttachmentService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER,RoleType.STUDENT})
public class GetAttachmentLocation implements AjaxActionCommand{
	
	private String resultPath = "";
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
					throws ServletException, IOException, Exception {
	

		System.out.println("inside get attachments");
//		String serverPath = System.getenv("CATALINA_HOME");
		CommonUser user = (CommonUser) request.getSession().getAttribute(
				Constants.SESSION_PARAM_NAME_USER);
		
		String owner_id = request.getParameter("owner");
		
		if (user != null ) {
			String role = user.getRoleType().name().toLowerCase(); // role of
																	// user
			Integer userId = user.getId(); // user for which we get attachment

			String target = request.getParameter("target").toLowerCase(); // target
																			// attachment
			if(target.equalsIgnoreCase("topic")){
				role= "teacher";
			}else if(target.equalsIgnoreCase("HOMEWORK")){
				role= "student";
			}else if(target.equalsIgnoreCase("TASK")){
				role= "student";
			}
			Integer targetId = Integer.parseInt(request
					.getParameter("target_id")); // id of target
			Integer attachmentId = Integer.parseInt(request
					.getParameter("attachment_id"));

			System.out.println("get attachment for " + role + " with id = "
					+ owner_id + " , target = " + target + "   id of target = "
					+ targetId + " , attachment id = " + attachmentId);

			String filePath = request.getContextPath() + "/storage/files/" + role
					+ "s/" + role + owner_id + "/" + target + "s/" + target
					+ targetId + "/attachment"+attachmentId+".pdf";

			JsonObject topicJsonObject = Json.createObjectBuilder().add("path", filePath).build();
			
			resultPath = topicJsonObject.toString();

		}else{
			request.getRequestDispatcher(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH)).forward(request, response); // file not found
		}
		return resultPath;
	}

	private static String getExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}
	
	private void getPathForStudentHomework(HttpServletRequest request, CommonUser loginedUser){
		Integer ownerId = Integer.parseInt(request.getParameter("owner"));
		String target = request.getParameter("target").toLowerCase();
		Integer targetId = Integer.parseInt(request.getParameter("target_id"));
		Integer attachmentId = Integer.parseInt(request
				.getParameter("attachment_id"));
		    RoleType  userRole = loginedUser.getRoleType();
		    RoleType ownerRole = null ;
           
//		    switch (userRole) {
//			case TEACHER:
//				ownerRole=RoleType.STUDENT;
//				break;
//			case STUDENT:
//				ownerRole = RoleType.TEACHER;
//				break;
//			default:
//				break;
//			}
		    if(target.equalsIgnoreCase("HOMEWORK")){
		    	ownerRole=RoleType.STUDENT;
			}
			String filePath = request.getContextPath() + "/storage/files/"
					+ ownerRole.name().toLowerCase() + "s/" + ownerRole.name().toLowerCase() + "" + ownerId
					+ "/" + target + "s/" + target + targetId + "/attachment" + attachmentId+".pdf";
			
			JsonObject topicJsonObject = Json.createObjectBuilder().add("path", filePath).build();
			resultPath = topicJsonObject.toString();
	}
}

