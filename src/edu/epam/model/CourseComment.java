package edu.epam.model;

import edu.epam.annotations.Column;

public class CourseComment {
	@Column("id")
	private Integer id;
	@Column("user_id")
	private Integer author;
	@Column("message")
	private String comment;
	
	
	public Integer getAuthor() {
		return author;
	}
	public void setAuthor(Integer author) {
		this.author = author;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
