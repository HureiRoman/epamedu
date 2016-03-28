package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.role.Student;

public class StudentService {

	public static boolean doGraduates(Integer groupId)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().doGraduate(groupId);
	}
	public static boolean deleteStudent(Integer studentId)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().deleteStudent(studentId);
	}
	
	public static List<Student> getStudentsForTeacherByGroup(Integer teacher_id,Integer group_id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().getStudentsForTeacherByGroup(teacher_id,group_id);
	}
	public static Integer getCountOfStudents(Integer groupId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().getCountOfStudents(groupId);
	}
	public static List<Student> getListOfStudentsOfGroup(Integer groupId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().getListOfStudentsOfGroup(groupId);		
	}
	public static Student getDataAboutStudentById(Integer studentId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().getDataAboutStudentById(studentId);		
	}
	
	public static List<Student> getListOfStudentsOfGroupWithTasks(Integer groupId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getStudentDAO().getListOfStudentsOfGroupWithTasks(groupId);		
	}
}
