package edu.epam.model;

import edu.epam.annotations.Column;

public class GradesOfTask {
	@Column("grade_id")
	private Integer id;
	@Column("student_id")
	private Integer studentId;
	@Column("task_id")
	private Integer taskId;
	@Column("teacher_id")
	private Integer teacherId;
	@Column("grade")
	private Integer grade;

	
	public GradesOfTask() {
		
	}
	public GradesOfTask(Integer studentId, Integer taskId, Integer teacherId, Integer grade) {
		this.studentId = studentId;
		this.taskId = taskId;
		this.teacherId = teacherId;
		this.grade = grade;
	}

	public Integer getId() {
		return id;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "GradesOfTask [id=" + id + ", studentId=" + studentId
				+ ", taskId=" + taskId + ", teacherId=" + teacherId
				+ ", grade=" + grade + "]";
	}
	
}
