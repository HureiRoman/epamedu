package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IStudentDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.CV;
import edu.epam.model.Direction;
import edu.epam.model.StudentAttachment;
import edu.epam.model.TestResults;
import edu.epam.role.CommonUser;
import edu.epam.role.Student;

public class StudentDAO implements IStudentDAO {
	
	private static final String SQL_GET_STUDENT_BY_EMAIL = "SELECT * FROM users u LEFT JOIN students s ON u.id = s.id WHERE email=? ";
	private static final String SQL_GET_CV_BY_ID = "SELECT * FROM cv WHERE id=? ";
	private static final String SQL_SET_ROLE_GRADUATE="UPDATE users SET role='GRADUATE' WHERE users.id IN (SELECT id FROM students WHERE group_id=?)";
	private static final String SQL_DELETE_STUDENT_BY_GROUP_ID="DELETE FROM students WHERE group_id=?";
	private static final String SQL_INSERT_GRADUATE_INTO_TABLE="INSERT INTO graduates (user_id,group_id) VALUES(?,?)";
	private static final String SQL_DELETE_ALL_INFO_ABOUT_GROUP="DELETE lessons FROM lessons INNER JOIN groups "
			+ "ON lessons.group_id=groups.group_id WHERE groups.group_id=?";
	private static final String SQL_SET_GROUP_IS_ACTIVE_FALSE="UPDATE groups SET is_active='false',date_of_graduation=CURRENT_TIMESTAMP WHERE group_id=?";
	private static final String SQL_DELETE_STUDENT="DELETE FROM students WHERE id=?";
	private static final String SQL_SET_ROLE_TRAINEE="UPDATE users SET role='TRAINEE' WHERE id=? AND role='STUDENT'";
	private static final String SQL_DELETE_STUDENTS_ATTACHMENTS="DELETE FROM student_attachments WHERE student_id IN (SELECT id FROM students WHERE group_id=?)";
	private static final String SQL_GET_ALL_STUDENTS_BY_TEACHER_ID="SELECT * FROM users INNER JOIN students ON users.id=students.id "
			+ "INNER JOIN groups ON groups.group_id=students.group_id INNER JOIN cv ON users.cv_id=cv.id WHERE teacher_id=? AND students.group_id=?";
	private static final String SQL_GET_COUNT_OF_STUDENTS=" SELECT COUNT(*) AS countS FROM students WHERE group_id=? AND isGraduate='false'";
	private static final String SQL_GET_STUDENTS_OF_GROUP="SELECT * FROM users INNER JOIN students s ON users.id = s.id INNER JOIN cv ON cv.id=users.id WHERE s.group_id=?";
	private static final String SQL_GET_STUDENT_DATA_BY_ID = "SELECT * FROM users INNER JOIN students s ON users.id = s.id INNER JOIN cv On cv.id=users.id WHERE users.id=?";
	private static final String SQL_INITIALIZE_TESTS_FOR_USER = "SELECT * FROM test_results WHERE user_id=?";
	private static final String SQL_GET_DIRECTION_BY_ID = "SELECT * FROM directions WHERE id=?";
	private static final String SQL_INSERT_TRAINEE_INTO_TABLE = "INSERT INTO trainee (id) VALUES (?)";
	private static final String SQL_DELETE_STUDENT_ATTACHMENTS = "DELETE FROM student_attachments WHERE student_id = ?";
	private static final String SQL_GET_STUDENT_TASKS_ATTACHMENT = "SELECT * FROM student_attachments WHERE student_id=? AND task_id IN (SELECT id FROM tasks WHERE type='TASK')";
	private static final String SQL_GET_STUDENT_TASK_MARK = "SELECT grade FROM grades_of_tasks WHERE student_id=? AND task_id=?";
	
	
	private static StudentDAO instance;

	private StudentDAO() {
	}

	public static StudentDAO getInstance() {
		if (instance == null) {
			instance = new StudentDAO();
		}
		return instance;
	}
    @Override
	public Integer getCountOfStudents(Integer groupId) throws SQLException{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer count;
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_COUNT_OF_STUDENTS)) {
				
				st.setInt(1,groupId);
				
				ResultSet rs1 = st.executeQuery();
			
					rs1.next();
					return rs1.getInt("countS");
	 } catch (Exception e) {
			 e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
	}
	
	@Override
	public CommonUser enterStudent(String email) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Student student = null;
		
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_STUDENT_BY_EMAIL);
			PreparedStatement st2 = conn.prepareStatement(SQL_GET_CV_BY_ID);
			PreparedStatement st3 = conn.prepareStatement(SQL_INITIALIZE_TESTS_FOR_USER);
			PreparedStatement st4 = conn.prepareStatement(SQL_GET_DIRECTION_BY_ID);) {
		conn.setAutoCommit(false);
			
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		student = Transformer.getInstance(rs, Student.class, Student.class, CommonUser.class);
		
		st2.setInt(1, student.getId());
		ResultSet rs2 = st2.executeQuery();
		 System.out.println("user id = " + student.getId());
		CV cv = Transformer.getInstance(rs2, CV.class, CV.class);
		student.setCv(cv);
		
		st3.setInt(1, student.getId());
		ResultSet rs3 = st3.executeQuery();
		Map<Direction, TestResults> testsMap = new HashMap<Direction, TestResults>();
		Direction direction = null;
		while(rs3.next()) { //resultset зі всіма тестами юзера
			st4.setInt(1, rs3.getInt("direction_id"));
			ResultSet rs4 = st4.executeQuery(); // TODO use batch? - get direction by id
			if (rs4.next()) {
				rs4.previous();
				direction = Transformer.getInstance(rs4, Direction.class, Direction.class);
				rs3.previous();
				TestResults results = Transformer.getInstance(rs3, TestResults.class, TestResults.class);
				testsMap.put(direction, results);
			}
		}
		student.setTestResults(testsMap);
		
		conn.commit();
		conn.setAutoCommit(true);
		 } catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			 e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
		return student;
	}

	@Override
	public boolean deleteStudent(Integer studentId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		try (PreparedStatement st = conn.prepareStatement(SQL_SET_ROLE_TRAINEE);
			 PreparedStatement st2 = conn.prepareStatement(SQL_DELETE_STUDENT);
			 PreparedStatement st3 = conn.prepareStatement(SQL_INSERT_TRAINEE_INTO_TABLE);
			 PreparedStatement st4 = conn.prepareStatement(SQL_DELETE_STUDENT_ATTACHMENTS);) {

			conn.setAutoCommit(false);

			st.setInt(1, studentId);
			int rs = st.executeUpdate();

			st4.setInt(1, studentId);
			int rs4 = st4.executeUpdate();

			st3.setInt(1, studentId);
			int rs3 = st3.executeUpdate();

			st2.setInt(1, studentId);
			int rs2 = st2.executeUpdate();

			if (rs == 0 || rs2 == 0 || rs3 == 0) throw new SQLException();

			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
	
	@Override
	public boolean doGraduate(Integer groupId) throws SQLException {
		
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
			try(PreparedStatement st = conn.prepareStatement(SQL_SET_ROLE_GRADUATE);
			 PreparedStatement st2 = conn.prepareStatement(SQL_DELETE_STUDENT_BY_GROUP_ID);
					 PreparedStatement st3=conn.prepareStatement(SQL_DELETE_ALL_INFO_ABOUT_GROUP);
					PreparedStatement st4=conn.prepareStatement(SQL_SET_GROUP_IS_ACTIVE_FALSE);
					PreparedStatement st5=conn.prepareStatement(SQL_GET_STUDENTS_OF_GROUP);
					PreparedStatement st6=conn.prepareStatement(SQL_INSERT_GRADUATE_INTO_TABLE);
					 PreparedStatement st7 = conn.prepareStatement(SQL_DELETE_STUDENTS_ATTACHMENTS);) {
			
		conn.setAutoCommit(false);
		
		st.setInt(1, groupId);
		int rs = st.executeUpdate();
		
		st3.setInt(1, groupId);
		int rs3 = st3.executeUpdate();
		
		st4.setInt(1, groupId);
		int rs4 = st4.executeUpdate();
		
		st7.setInt(1, groupId);
		int rs7 = st7.executeUpdate();
		
		st5.setInt(1, groupId);
		ResultSet rs5 = st5.executeQuery();
		List<Student> students = Transformer.getListOfInstances(rs5, Student.class, Student.class, CommonUser.class);
		
		for(int i=0;i<students.size();i++)
			{ st6.setInt(1,students.get(i).getId());
			st6.setInt(2,groupId);
			st6.addBatch();
					}
					st6.executeBatch();
					
					st2.setInt(1, groupId);
					int rs2 = st2.executeUpdate();
		
		if(rs==0||rs2==0|| rs4==0) throw new SQLException();

		conn.commit();
		conn.setAutoCommit(true);	
		return true;
	 } catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			 e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
	}
	@Override
	public List<Student> getStudentsForTeacherByGroup(Integer teacherId,Integer groupId) throws SQLException{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Student> listOfStudent = new ArrayList<>();
		try(PreparedStatement st = conn.prepareStatement(SQL_GET_ALL_STUDENTS_BY_TEACHER_ID)) {
				
				st.setInt(1, teacherId);
				st.setInt(2,groupId);
				
				ResultSet rs1 = st.executeQuery();
				
				while(rs1.next()){
					rs1.previous();
					Student student = Transformer.getInstance(rs1, Student.class, Student.class,CommonUser.class);
					rs1.previous();
					CV cv = Transformer.getInstance(rs1, CV.class, CV.class);
					student.setCv(cv);
					listOfStudent.add(student);
				}
		return listOfStudent;
	 } catch (Exception e) {
			 e.printStackTrace();
				throw new SQLException();
		}finally{
			cm.freeConnection(conn);
		}
	}
	
	@Override
	public List<Student> getListOfStudentsOfGroup(Integer groupId) 	throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Student> listOfStudents = new ArrayList<>();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_STUDENTS_OF_GROUP)) {
			st.setInt(1, groupId);
			ResultSet rs1 = st.executeQuery();
			while(rs1.next()){
				rs1.previous();
				Student student = Transformer.getInstance(rs1, Student.class, Student.class,CommonUser.class);
				rs1.previous();
				CV cv = Transformer.getInstance(rs1, CV.class, CV.class);
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

	@Override
	public Student getDataAboutStudentById(Integer studentId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Student student = new Student();
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_STUDENT_DATA_BY_ID)) {
			st.setInt(1, studentId);
			ResultSet rs1 = st.executeQuery();
			if(rs1.next()){
				rs1.previous();
				 student = Transformer.getInstance(rs1, Student.class, Student.class,CommonUser.class);
				rs1.previous();
				CV cv = Transformer.getInstance(rs1, CV.class, CV.class);
				student.setCv(cv);
				}
			return student;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}

	@Override
	public List<Student> getListOfStudentsOfGroupWithTasks(Integer groupId)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Student> listOfStudents = new ArrayList<>();
		try (PreparedStatement st = conn.prepareStatement(SQL_GET_STUDENTS_OF_GROUP);
				PreparedStatement st2 = conn.prepareStatement(SQL_GET_STUDENT_TASKS_ATTACHMENT);
					PreparedStatement getTaskMark = conn.prepareStatement(SQL_GET_STUDENT_TASK_MARK)) {
			conn.setAutoCommit(false);
			
			st.setInt(1, groupId);
			ResultSet rs1 = st.executeQuery();
			while(rs1.next()){
				rs1.previous();
				Student student = Transformer.getInstance(rs1, Student.class, Student.class,CommonUser.class);
				rs1.previous();
				CV cv = Transformer.getInstance(rs1, CV.class, CV.class);
				student.setCv(cv);
				
				st2.setInt(1, student.getId());
				ResultSet taskAttachments = st2.executeQuery();
				List<StudentAttachment> studentAttachments = null;
				if(taskAttachments.next()){
					taskAttachments.previous();
					studentAttachments = new ArrayList<>();
					while(taskAttachments.next()){
						taskAttachments.previous();
						StudentAttachment studentTaskAttachment = Transformer.getInstance(taskAttachments, StudentAttachment.class, StudentAttachment.class);
						
						getTaskMark.setInt(1, student.getId());
						getTaskMark.setInt(2, studentTaskAttachment.getId());
						ResultSet taskMark = getTaskMark.executeQuery();
						if(taskMark.next()){
							studentTaskAttachment.setMark(taskMark.getInt(1));
						}
						
						studentAttachments.add(studentTaskAttachment);
					}
				}
				
				listOfStudents.add(student);
			}
			conn.commit();
			conn.setAutoCommit(true);
			return listOfStudents;
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
	}
}
