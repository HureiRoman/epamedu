package edu.epam.dao.interfaces;

import edu.epam.model.StudentVisiting;
import edu.epam.role.Student;
import edu.epam.validation.Validator;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fastforward on 08/07/15.
 */
public interface IStudentVisitingDAO {
    void insertStudentVisiting(StudentVisiting studentVisiting) throws SQLException;
    boolean isStudentVisitingPresent(StudentVisiting studentVisiting) throws SQLException;
    void deleteStudentVisiting(StudentVisiting studentVisiting) throws SQLException;
    void updateStudentVisiting(StudentVisiting studentVisiting) throws SQLException;
    List<Student> getVisitorsByLessonId(int lessonId) throws SQLException;
    int getAmountOfVisitedLessonsForStudentByGroupId(int studentId, int groupId) throws SQLException;
}
