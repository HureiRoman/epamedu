package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.GradesOfTask;
import edu.epam.service.GradesOfTaskService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({RoleType.TEACHER})
public class SetTaskMarkCommand implements AjaxActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
      
        try{
	        String ratingString = request.getParameter("value");
	        int studentId = Integer.parseInt(request.getParameter("student_id"));
	        int taskId = Integer.parseInt(request.getParameter("task_id"));
	        int teacherId = Integer.parseInt(request.getParameter("teacher_id"));
	        System.out.println("!!!!!task id = " + taskId + "; STUDENT = " + studentId + " ; TEACHER = " + teacherId);
	        
	        if (ratingString != "" ) {
	            int rating = Integer.parseInt(ratingString);
	            GradesOfTask taskGrade = new GradesOfTask(studentId, taskId, teacherId, rating);
	            boolean resulOfAdding = GradesOfTaskService.setGradeOfTask(taskGrade);
	            
	            if(resulOfAdding == true) return "" + rating;
	        }
	        
	        
        }catch(Exception e){
        	e.printStackTrace();
        }
        return "0";
    }
}
