package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;

public class StudentAttachment {
	@Column("id")
	private Integer id;
	@Column("task_id")
	private Integer taskId;
	@Column("student_id")
	private Integer studentId;
	@Column("title")
	private String title;
	@Column("extension")
	private String extension;
	@Column("date_of_addition")
	private Date dateAdded;
	
	private Integer mark;
	
	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

}
