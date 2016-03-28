package edu.epam.persistance;

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import edu.epam.model.Lesson;
import edu.epam.service.LessonService;

public class CheckDeleteNotNeededFiles implements Runnable{
	
    private boolean timeToQuit=false;
    private String projectPath = null;
    
    public CheckDeleteNotNeededFiles(String projectPath) {
		if(projectPath != null & !projectPath.equals("")){
			this.projectPath = projectPath;
		}else{
			projectPath = "";
		}
	}
    
	@Override
	public void run() {
		
        if ( !timeToQuit ) {
        	checkToDeleteNotNeededFiles();
        	System.out.println("inside THREAD checkToDeleteNotNeededFiles!!!");
        } 
        
	}
	
	public void stopRunning() { 
		System.out.println("STOP thread checkToDeleteNotNeededFiles!!!!!!");
        timeToQuit=true;
  }
	
	private void checkToDeleteNotNeededFiles(){
		System.out.println("INSIDE!!!!!!!!!!!!!!!!");
		try {
			File xmlFilePath = new File(projectPath + "/storage/xml/abiturientsList.xls");
			xmlFilePath.delete();
//				boolean resultOfDeleting = Files.deleteIfExists(xmlFilePath.toPath());
			
		} catch (Exception e) {
			//Написати на пошту  і телефон !!!!Log4j
			System.out.println("error in thread checkToDeleteNotNeededFiles!");
			e.printStackTrace();
		}
		
		
	}

}