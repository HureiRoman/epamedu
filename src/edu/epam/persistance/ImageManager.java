package edu.epam.persistance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import edu.epam.constants.ImageType;

public class ImageManager {
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 10MB
	
	public static boolean loadImage(HttpServletRequest request, FileItem item, ImageType type,Integer id) throws Exception {
		if(!checkPhoto(item)){
			return false;
		}		
//		String serverPath = System.getenv("CATALINA_HOME");
		ServletContext context = request.getSession().getServletContext();
		String realContextPath = context.getRealPath(""); 
		File imageFolder = new File(realContextPath+"/storage/images/"+type.name().toLowerCase());
		if(!imageFolder.exists()){
			imageFolder.mkdirs();
		}
		File imageFile = new File(realContextPath+"/storage/images/"+type.name().toLowerCase()+"/image"+id+".jpg");
		item.write(imageFile);
		return imageFile.getTotalSpace()>100;
	}
	private static  boolean checkPhoto(FileItem item) {
		if(item!=null) {
			String fileName = item.getName();
			long fileSize = item.getSize();
			List<String> extensions = new ArrayList<>();
			extensions.add("jpg");
			extensions.add("jpeg");
			extensions.add("gif");
			extensions.add("png");
			extensions.add("bmp");
			extensions.add("JPG");
			extensions.add("JPEG");
			extensions.add("GIF");
			extensions.add("PNG");
			extensions.add("BMP");
			String extension = "";
			int i = fileName.lastIndexOf('.');
			if (i > 0) {
				extension = fileName.substring(i+1);
			}
			if(!extensions.contains(extension)||fileSize > MAX_FILE_SIZE) {
				return false;
			} 
		}
		return true;
	}
}
