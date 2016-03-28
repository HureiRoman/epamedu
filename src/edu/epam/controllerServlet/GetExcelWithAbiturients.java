package edu.epam.controllerServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableWorkbook;

import org.apache.commons.io.IOUtils;

import edu.epam.constants.Constants;
import edu.epam.model.Interview;
import edu.epam.role.CommonUser;
import edu.epam.role.Trainee;
import edu.epam.service.InterviewService;
import edu.epam.service.TraineeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;



public class GetExcelWithAbiturients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		String serverPath = System.getenv("CATALINA_HOME");
		ServletContext context = request.getSession().getServletContext();
		String realContextPath = context.getRealPath(""); 
		String interviewId = request.getParameter("interviewId");
		if(interviewId != null && !interviewId.equals("")){
			int idInterview = Integer.parseInt(interviewId);
			try {
				List<Trainee> listOfTrainee = TraineeService.getTraineesByInterviewId(idInterview);
				CommonUser hr = (CommonUser)request.getSession().getAttribute(Constants.SESSION_PARAM_NAME_USER);
				 WritableWorkbook wworkbook;
				 File xmlFolder = new File(realContextPath + "/storage/xml/");
			      if (!xmlFolder.exists()) {
			    	  xmlFolder.mkdirs();
			      }
			      File xmlFilePath = new File(xmlFolder + "/abiturientsList.xls");
			      wworkbook = Workbook.createWorkbook(xmlFilePath);
			      
			      // Create cell font and format
			      WritableFont cellFont = new WritableFont(WritableFont.COURIER, 16);
			      cellFont.setBoldStyle(WritableFont.BOLD);
			      
			      WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			      
			      WritableSheet wsheet = wworkbook.createSheet("Список абітурієнтів", 0);
			      Label colNumb = new Label(0, 0, "#",cellFormat);
			      wsheet.setColumnView(0, 3);
			      wsheet.addCell(colNumb);
			      
			      Label fname = new Label(1, 0, "Ім'я",cellFormat);
			      wsheet.setColumnView(1, 15);
			      wsheet.addCell(fname);
			      
			      Label lname = new Label(2, 0, "Прізвище",cellFormat);
			      wsheet.setColumnView(2, 20);
			      wsheet.addCell(lname);
			      
			      Label phone = new Label(3, 0, "Телефон",cellFormat);
			      wsheet.setColumnView(3, 20);
			      wsheet.addCell(phone);
			      
			      Label email = new Label(4, 0, "Email",cellFormat);
			      wsheet.setColumnView(4, 30);
			      wsheet.addCell(email);
			      
			      if(listOfTrainee != null){
			    	  for (int i = 1; i <= listOfTrainee.size(); i++) {
			    		  Label colNumber = new Label(0, i, ""+i);
					      wsheet.addCell(colNumber);
			    		  Label traineeFname = new Label(1, i, listOfTrainee.get(i-1).getFirstName());
					      wsheet.addCell(traineeFname);
					      Label traineeLname = new Label(2, i, listOfTrainee.get(i-1).getLastName());
					      wsheet.addCell(traineeLname);
					      Label traineePhone = new Label(3, i, listOfTrainee.get(i-1).getCv().getPhone());
					      wsheet.addCell(traineePhone);
					      Label traineeEmail = new Label(4, i, listOfTrainee.get(i-1).getEmail());
					      wsheet.addCell(traineeEmail);
					}
			    	  
			      }
			      
			      wworkbook.write();
			      wworkbook.close();

			      InputStream fileStream = new FileInputStream(xmlFilePath);
					byte[] xml = IOUtils.toByteArray(fileStream);
					
					
					response.setHeader("Content-Disposition",
							"attachment; filename=\"ListOfTrainees.xls" 
									+ "\"");
					response.setContentType("application/xml; charset=UTF-8;");
					response.setCharacterEncoding("UTF-8");
					response.getOutputStream().write(xml, 0,
							xml.length);
					response.getOutputStream().flush();
					
//					xmlFilePath.delete();
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
