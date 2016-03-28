package edu.epam.persistance;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import edu.epam.model.Lesson;
import edu.epam.service.LessonService;

public class AutoCheckLessonTurnOnThread implements Runnable{
	
    private boolean timeToQuit=false;
    
	@Override
	public void run() {
		
        if ( !timeToQuit ) {
        	checkOpenLessonForGroup();
        	System.out.println("inside THREAD!!!");
        } 
        
	}
	
	public void stopRunning() { 
		System.out.println("STOP thread!!!!!!");
        timeToQuit=true;
  }
	
	private void checkOpenLessonForGroup(){
		Date now = new Date();
		List<Lesson> listOfAutoOpenLessons = null;
		
		try {
			listOfAutoOpenLessons = LessonService.getAutoTurnOnLessons();
			boolean result = false;
			if(listOfAutoOpenLessons != null){
				for(Lesson lesson : listOfAutoOpenLessons){
					if(lesson.getLessonTime().compareTo(now) >= 0){
						result = LessonService.setLessonActive(lesson.getId(), true);
					}
				}
			}
		} catch (SQLException e) {
			//Написати на пошту  і телефон !!!!Log4j
			System.out.println("error in thread!");
			e.printStackTrace();
		}
		
		
	}

}
