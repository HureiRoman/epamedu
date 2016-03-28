package edu.epam.servlet.command;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.GradesOfTask;
import edu.epam.model.Task;
import edu.epam.role.Student;
import edu.epam.service.GradesOfTaskService;
import edu.epam.service.StudentService;
import edu.epam.service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by fastforward on 10/07/15.
 */
@UserPermissions({RoleType.TEACHER})
public class ShowGradesOfTasksCommand implements ActionCommand{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        Map<Student, List<Integer>> studentsGrades = new HashMap<>();
        String groupIdString = request.getParameter("group_id");
        int badLevelCount = 0;
        int mediumLevelCount = 0;
        int goodLevelCount = 0;
        int perfectLevelCount = 0;
        try {
            int groupId = Integer.parseInt(groupIdString);
            List<Task> tasks = TaskService.getTasksForGroup(groupId);
            List<Student> students = StudentService.getListOfStudentsOfGroup(groupId);
            for(Student student : students) {
                List<Integer> results = new ArrayList<>();
                int total = 0;
                for(Task task : tasks) {
                    int result;
                    GradesOfTask grade = GradesOfTaskService.getGradeBystudentId(student.getId(), task.getId());
                    if(grade == null) {
                        result = 0;
                    } else {
                        result = grade.getGrade();
                    }
                    total += result;
                    results.add(result);
                }
                if(total < tasks.size() * 100 / 4) {
                    badLevelCount++;
                } else if(total < tasks.size() * 100 / 2) {
                    mediumLevelCount++;
                } else if(total < tasks.size() * 100 * 3 / 4) {
                    goodLevelCount++;
                } else if(total < tasks.size() * 100) {
                    perfectLevelCount++;
                }
                results.add(total);
                studentsGrades.put(student, results);
            }

            request.setAttribute("group_id" , groupId);
            request.setAttribute("bad_level", (double)badLevelCount / students.size() * 100);
            request.setAttribute("good_level", (double)goodLevelCount / students.size() * 100);
            request.setAttribute("medium_level", (double)mediumLevelCount / students.size() * 100);
            request.setAttribute("perfect_level", (double)perfectLevelCount / students.size() * 100);
            request.setAttribute("tasks", tasks);
            request.setAttribute("students_grades", studentsGrades);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ConfigurationManager.getInstance().getProperty(
                ConfigurationManager.TEACHER_SHOW_GRADES_OF_TASKS);
    }
}
