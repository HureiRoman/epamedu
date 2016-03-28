package edu.epam.service;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.StudentVisiting;
import edu.epam.role.Student;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fastforward on 08/07/15.
 */
public class StudentVisitingSevice {
    public static void insertStudentVisiting(StudentVisiting studentVisiting)
            throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().insertStudentVisiting(studentVisiting);
    }

    public static void deleteStudentVisiting(StudentVisiting studentVisiting)
            throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().deleteStudentVisiting(studentVisiting);
    }

    public static boolean isStudentVisitingPresent(StudentVisiting studentVisiting)
            throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().isStudentVisitingPresent(studentVisiting);
    }

    public static List<Student> getVisitorsByLessonId(int lessonId)
            throws SQLException {
       return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().getVisitorsByLessonId(lessonId);
    }

    public static void updateStudentVisiting(StudentVisiting studentVisiting)
            throws SQLException {
        AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().updateStudentVisiting(studentVisiting);
    }

    public static int getAmountOfVisitedLessonsForStudentByGroupId(int studentId, int groupId)
            throws SQLException {
        return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentVisitingDAO().getAmountOfVisitedLessonsForStudentByGroupId(studentId, groupId);
    }
}
