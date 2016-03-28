package edu.epam.controllerServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import edu.epam.constants.Constants;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.CommonUser;
import edu.epam.service.AttachmentService;

public class GetAttachmentsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("inside get attachments");
//		String serverPath = System.getenv("CATALINA_HOME");
		ServletContext context = request.getSession().getServletContext();
		String realContextPath = context.getRealPath(""); 
		CommonUser user = (CommonUser) request.getSession().getAttribute(
				Constants.SESSION_PARAM_NAME_USER);

		if (user != null) {
			String role = user.getRoleType().name().toLowerCase(); // role of
																	// user
			Integer userId = user.getId(); // user for which we get attachment

			String target = request.getParameter("target").toLowerCase(); // target
																			// which
																			// contains
																			// attachment
			Integer targetId = Integer.parseInt(request
					.getParameter("target_id")); // id of target
			Integer attachmentId = Integer.parseInt(request
					.getParameter("attachment_id"));

			System.out.println("get attachment for " + role + " with id = "
					+ userId + " , target = " + target + "   id of target = "
					+ targetId + " , attachment id = " + attachmentId);

			File filePath = new File(realContextPath + "/storage/files/" + role
					+ "s/" + role + userId + "/" + target + "s/" + target
					+ targetId + "/");

			System.out.println("path of retrieving file "
					+ filePath.getAbsolutePath());
			String fileExtension = null;
			File[] listOfFiles = null;
			File file = null;
			if (filePath.exists()) {
				listOfFiles = filePath.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.startsWith("attachment" + attachmentId
								+ ".");
					}
				});
			}

			String title = null;

			switch (user.getRoleType()) {
			case TEACHER:
				try {
					title = AttachmentService
							.getAttachmentTitleById(attachmentId);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case STUDENT:
				try {

					title = AttachmentService
							.getStudentAttachmentTitleById(attachmentId);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
             title = URLEncoder.encode(title,"UTF-8"); 
			if (listOfFiles != null && listOfFiles.length>=1) {
				file = listOfFiles[0];
				if (file != null) {
					fileExtension = getExtension(file.getName());
					InputStream fileStream = new FileInputStream(file);
					byte[] fileToSend = IOUtils.toByteArray(fileStream);
					response.reset();
					response.setContentType("application/octet-stream; charset=UTF-8;");
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Disposition",
							"attachment; filename=\"" + title + "."
									+ fileExtension + "\"");
					response.getOutputStream().write(fileToSend, 0,
							fileToSend.length);
					response.getOutputStream().flush();
				}else{
					request.getRequestDispatcher(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH)).forward(request, response); // file not found
				}
			}else{
				request.getRequestDispatcher(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH)).forward(request, response); // file not found
			}
		}else{
			request.getRequestDispatcher(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH)).forward(request, response); // file not found
		}
	}

	private static String getExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}
}
