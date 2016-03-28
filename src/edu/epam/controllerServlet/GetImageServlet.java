package edu.epam.controllerServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class GetImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		String serverPath = System.getenv("CATALINA_HOME");
		ServletContext context = request.getSession().getServletContext();
		String realContextPath = context.getRealPath(""); 
		
		String type = request.getParameter("type");
		File file;
		
		if(type.equals("extensions")){
			String extension = request.getParameter("extension");
			extension = extension.substring(1);
			file = new File(realContextPath + "/storage/images/" + type
					+ "/"+extension+".png");
		}else{
			Integer id = Integer.parseInt(request.getParameter("id"));
			file = new File(realContextPath + "/storage/images/" + type
					+ "/image" + id + ".jpg");
		}
		
		if(!file.exists()){
			file= new File(realContextPath+"/storage/images/noimage.png");
		}

		InputStream fileStream = new FileInputStream(file);
		byte[] image = IOUtils.toByteArray(fileStream);

		response.reset();
		response.setContentType("image/png");
		response.getOutputStream().write(image, 0, image.length);
		response.getOutputStream().flush();
	}

}
