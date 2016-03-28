package edu.epam.model;

import java.util.Date;
import java.util.List;

import edu.epam.annotations.Column;
import edu.epam.role.Student;

public class Group {
	@Column("group_id")
	private Integer id;
	@Column("title")
	private String title;
	private List<Integer> teachers;
	@Column("direction_id")
	private int directionId;
	@Column("date_of_graduation")
	private Date date_of_graduation;
	@Column("is_active")
	private boolean is_active;

	private List<Student> listOfStudents;
	
	public Group() {

	}
	
	public List<Student> getListOfStudents() {
		return listOfStudents;
	}

	public void setListOfStudents(List<Student> listOfStudents) {
		this.listOfStudents = listOfStudents;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Integer> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Integer> teachers) {
		this.teachers = teachers;
	}

	public int getDirectionId() {
		return directionId;
	}

	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	public Date getDate_of_graduation() {
		return date_of_graduation;
	}

	public void setDate_of_graduation(Date date_of_graduation) {
		this.date_of_graduation = date_of_graduation;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = Boolean.valueOf(is_active);
	}

}
