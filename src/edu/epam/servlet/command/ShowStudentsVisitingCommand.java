package edu.epam.servlet.command;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.Constants;
import edu.epam.constants.RoleType;
import edu.epam.manager.ConfigurationManager;
import edu.epam.model.Direction;
import edu.epam.model.Group;
import edu.epam.model.Lesson;
import edu.epam.model.StudentVisiting;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;
import edu.epam.service.GroupService;
import edu.epam.service.LessonService;
import edu.epam.service.StudentService;
import edu.epam.service.StudentVisitingSevice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fastforward on 08/07/15.
 */
@UserPermissions({RoleType.TEACHER})
public class ShowStudentsVisitingCommand  implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, Exception {

        String lessonIdString = request.getParameter("lesson_id");
        if (lessonIdString != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<students>");
                int lessonId = Integer.parseInt(lessonIdString);
                Lesson lesson = LessonService.getLessonById(lessonId);
                Map<Student, String> studentsMap = new HashMap<>();

                List <Student> students = StudentService.getListOfStudentsOfGroup(lesson.getGroupId());
                List <Student> visitedStudents = StudentVisitingSevice.getVisitorsByLessonId(lessonId);
                for(Student student : students) {
                    boolean isPresent = false;
                    if(visitedStudents.contains(student)) {
                        isPresent = true;
                    }
                    studentsMap.put(student, String.valueOf(isPresent));

                    request.setAttribute("students", studentsMap);
                    request.setAttribute("lesson", lesson);
                    request.setAttribute("group_id", lesson.getGroupId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.TEACHER_SHOW_STUDENTS_VISITING_PAGE);
    }
}
