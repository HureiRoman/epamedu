package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.role.CommonUser;
import edu.epam.role.Student;

public interface IStudentDAO {
	CommonUser enterStudent(String email) throws SQLException;
	boolean doGraduate(Integer groupID) throws SQLException;
	boolean deleteStudent(Integer studentId) throws SQLException;
	List<Student> getStudentsForTeacherByGroup(Integer teacher_id,Integer group_id) throws SQLException;
	Integer getCountOfStudents(Integer groupId) throws SQLException;
	List<Student> getListOfStudentsOfGroup(Integer groupId) throws SQLException;
	Student getDataAboutStudentById(Integer studentId) throws SQLException;
	List<Student> getListOfStudentsOfGroupWithTasks(Integer groupId) throws SQLException;
} 
