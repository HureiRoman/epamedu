package edu.epam.persistance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import edu.epam.constants.AttachmentType;
import edu.epam.constants.RoleType;

public class AttachmentManager {

	private static final int MAX_FILE_SIZE = 1024 * 1024 * 100; // 100 MB
	
	public static boolean addAttachment(HttpServletRequest request, FileItem item, RoleType ownerRole,
			Integer ownerId, AttachmentType attachmentType,
			Integer attachmentTargetId,Integer attachmentId) throws Exception {

		if(!checkAttachment(item)){
			return false;
		}
		
//		String serverPath = System.getenv("CATALINA_HOME");
		ServletContext context = request.getSession().getServletContext();
		String realContextPath = context.getRealPath(""); 
		File filesFolder = new File(realContextPath + "/storage/files/"
				+ ownerRole.name().toLowerCase() + "s/"
				+ ownerRole.name().toLowerCase() + "" + ownerId + "/"
				+ attachmentType.name().toLowerCase() + "s/"
				+ attachmentType.name().toLowerCase() + "" + attachmentTargetId
				+ "/");

		if (!filesFolder.exists()) {
			filesFolder.mkdirs();
		}

//		String filesFolderPath = filesFolder.getAbsolutePath();
		
		File filePath = new File(filesFolder + "/attachment"+attachmentId+"." + getExtension(item));
		
		if("docx".equals(getExtension(item))){
			System.out.println("doc(x .equals  getExtension(item) ");
			File pdfFilePath = new File(filesFolder + "/attachment"+attachmentId+".pdf");
			Thread convertDocIntoPdf = new Thread(new Runnable() {
				
				@Override
				public void run() {
					transformWordIntoPDF(item,pdfFilePath);
					
				}
			});
			convertDocIntoPdf.setDaemon(true);
			convertDocIntoPdf.start();
			
		}
		
		System.out.println("paTH "+filePath.getAbsolutePath());
		item.write(filePath);
		
		return filePath.getTotalSpace()>100;
	}

	private static String getExtension(FileItem item) {
		String fileName = item.getName();
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	public static boolean checkAttachment(FileItem item) {
		String fileName = item.getName();
		long fileSize = item.getSize();
		List<String> extensions = new ArrayList<>();
		extensions.add("doc");
		extensions.add("ppt");
		extensions.add("pptx");
		extensions.add("pdf");
		extensions.add("docx");
		extensions.add("zip");
		extensions.add("rar");
		extensions.add("7z");
		extensions.add("jar");
		extensions.add("txt");
		extensions.add("xsl");
		extensions.add("xslx");
		extensions.add("jpg");
		extensions.add("jpeg");
		extensions.add("png");
		extensions.add("gif");
		extensions.add("sql");

		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		if (!extensions.contains(extension.toLowerCase()) || fileSize > MAX_FILE_SIZE) {
			return false;
		}
		return true;
	}
	
	private static boolean transformWordIntoPDF(FileItem item, File filePath){
		 try {
	            long start = System.currentTimeMillis();
	 
	            // 1) Load DOCX into WordprocessingMLPackage
	            InputStream is = item.getInputStream();
	            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(is);
	 
	            // 2) Prepare Pdf settings
	            PdfSettings pdfSettings = new PdfSettings();
	 
	            // 3) Convert WordprocessingMLPackage to Pdf
	            OutputStream out = new FileOutputStream(filePath);
	            PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
	                    wordMLPackage);
	            converter.output(out, pdfSettings);
	 
	            System.err.println("Generate pdf with " + (System.currentTimeMillis() - start) + "ms");
	 
	        } catch (Throwable e) {
	            e.printStackTrace();
	            return false;
	        }
		
		return true;
		
	}


}
