package edu.epam.persistance;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CheckSwitchOnLessonListener  implements ServletContextListener {

    private AutoCheckLessonTurnOnThread myThread = null;
    private Thread threadAutoSwitchOn = null;
    
    private CheckDeleteNotNeededFiles myThreadCheckToDeleteFiles = null;
    private Thread threadDeleteFiles = null;
    
    private ScheduledExecutorService scheduledExecutorService = null;
	@Override
    public void contextInitialized(ServletContextEvent event) {
        if ((threadAutoSwitchOn == null) || (!threadAutoSwitchOn.isAlive())) {
            myThread = new AutoCheckLessonTurnOnThread();
            threadAutoSwitchOn = new Thread(myThread);
           
            scheduledExecutorService = Executors.newScheduledThreadPool(2);
            
            if ((threadDeleteFiles == null) || (!threadDeleteFiles.isAlive())) {
            	String projectPath = event.getServletContext().getRealPath("");
	            myThreadCheckToDeleteFiles = new CheckDeleteNotNeededFiles(projectPath);
	            threadDeleteFiles = new Thread(myThreadCheckToDeleteFiles);
	            scheduledExecutorService.scheduleWithFixedDelay(threadDeleteFiles, 0, 1, TimeUnit.MINUTES);
            }
            scheduledExecutorService.scheduleWithFixedDelay(threadAutoSwitchOn, 0, 1, TimeUnit.MINUTES);
            
        }
    }
	@Override
    public void contextDestroyed(ServletContextEvent event){
        try {
        	System.out.println("Stop Thread");
        	 myThread.stopRunning();
        	 threadAutoSwitchOn.interrupt();
        	 
        	 myThreadCheckToDeleteFiles.stopRunning();
        	 threadDeleteFiles.interrupt();
        	 
        	 System.out.println("1");
        	 if(scheduledExecutorService != null){
            	 System.out.println("2");
        		 scheduledExecutorService.shutdownNow();
            	 System.out.println("schedulade thread shut down = " + scheduledExecutorService.isShutdown());

        	 }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }

}