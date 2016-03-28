package edu.epam.dao.realization;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IStudentVisitingDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.CV;
import edu.epam.model.StudentVisiting;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fastforward on 08/07/15.
 */
public class StudentVisitingDAO implements IStudentVisitingDAO {

    private static final String SQL_INSERT_STUDENT_VISITING = "INSERT INTO students_visiting (lesson_id, student_id, is_present)" +
            " VALUES (?, ?, ?)";
    private static final String SQL_IS_STUDENT_VISITING_PRESENT = "SELECT * FROM students_visiting WHERE lesson_id = ? AND student_id = ?";
    private static final String SQL_DELETE_STUDENT_VISITING = "DELETE FROM students_visiting WHERE lesson_id = ? AND student_id = ?";
    private static final String SQL_GET_VISITORS_BY_LESSON_ID = "SELECT * FROM students_visiting V JOIN students S ON" +
            " V.student_id = S.id JOIN users U ON U.id = S.id JOIN cv C ON C.id = U.id " +
            "WHERE lesson_id = ? AND is_present = 'true'";
    private static final String SQL_UPDATE_STUDENT_VISITING = "UPDATE students_visiting SET is_present = ? WHERE lesson_id = ? AND student_id = ?";
    private static final String SQL_GET_AMOUNT_OF_VISITED_LESSONS_FOR_STUDENT_BY_GROUP_ID = "SELECT COUNT(*) FROM students_visiting V JOIN lessons L" +
            " ON L.id = V.lesson_id JOIN groups G ON L.group_id = G.group_id WHERE L.group_id = ? AND is_present = 'true' AND student_id = ?";

    private static StudentVisitingDAO instance;
    private StudentVisitingDAO() {
    }

    public static StudentVisitingDAO getInstance() {
        if (instance == null) {
            instance = new StudentVisitingDAO();
        }
        return instance;
    }
    
    @Override
    public void insertStudentVisiting(StudentVisiting studentVisiting) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT_STUDENT_VISITING);) {
            preparedStatement.setInt(1, studentVisiting.getLessonId());
            preparedStatement.setInt(2, studentVisiting.getStudentId());
            preparedStatement.setString(3, String.valueOf(studentVisiting.isPresent()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public boolean isStudentVisitingPresent(StudentVisiting studentVisiting) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_IS_STUDENT_VISITING_PRESENT);) {
            preparedStatement.setInt(1, studentVisiting.getLessonId());
            preparedStatement.setInt(2, studentVisiting.getStudentId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
        return false;
    }

    @Override
    public void deleteStudentVisiting(StudentVisiting studentVisiting) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE_STUDENT_VISITING);) {
            preparedStatement.setInt(1, studentVisiting.getLessonId());
            preparedStatement.setInt(2, studentVisiting.getStudentId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public void updateStudentVisiting(StudentVisiting studentVisiting) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE_STUDENT_VISITING);) {
            preparedStatement.setString(1, String.valueOf(studentVisiting.isPresent()));
            preparedStatement.setInt(2, studentVisiting.getLessonId());
            preparedStatement.setInt(3, studentVisiting.getStudentId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cm.freeConnection(conn);
        }
    }

    @Override
    public List<Student> getVisitorsByLessonId(int lessonId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        List<Student> listOfStudents = new ArrayList<>();
        try (PreparedStatement preparedStatement = conn
                .prepareStatement(SQL_GET_VISITORS_BY_LESSON_ID)) {
            preparedStatement.setInt(1, lessonId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                rs.previous();
                Student student = Transformer.getInstance(rs, Student.class, Student.class, CommonUser.class);
                rs.previous();
                CV cv = Transformer.getInstance(rs, CV.class, CV.class);
                student.setCv(cv);
                listOfStudents.add(student);
            }
            return listOfStudents;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
    }

    public int getAmountOfVisitedLessonsForStudentByGroupId(int studentId, int groupId) throws SQLException {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = cm.getConnection();
        int result = 0;
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_AMOUNT_OF_VISITED_LESSONS_FOR_STUDENT_BY_GROUP_ID);) {
            preparedStatement.setInt(1, groupId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            cm.freeConnection(conn);
        }
        return result;
    }
}
