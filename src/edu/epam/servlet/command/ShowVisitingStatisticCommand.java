package edu.epam.servlet.command;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.role.Student;
import edu.epam.service.GroupService;
import edu.epam.service.LessonService;
import edu.epam.service.StudentService;
import edu.epam.service.StudentVisitingSevice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fastforward on 09/07/15.
 */
@UserPermissions({RoleType.TEACHER})
public class ShowVisitingStatisticCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {
        String groupIdString = request.getParameter("group_id");

        if(groupIdString != null) {
            try {
                Map<Student, Integer> statistic = new HashMap<>();
                int groupId = Integer.parseInt(groupIdString);
                int amountOfAllLessons = LessonService.getAmountOfPastLessonsForGroup(groupId);
                //System.out.println("amount of lessons " + amountOfLessons);
                List<Student> students = StudentService.getListOfStudentsOfGroup(groupId);

                for(Student student : students) {
                    int amountOfVisitingLessons = StudentVisitingSevice.getAmountOfVisitedLessonsForStudentByGroupId(student.getId(), groupId);
                    statistic.put(student, amountOfVisitingLessons);
                }

                request.setAttribute("statistic", statistic);
                request.setAttribute("amount_of_all_lessons", amountOfAllLessons);
                request.setAttribute("group_id", groupId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.TEACHER_SHOW_VISITING_STATISTIC);
    }
}
