package edu.epam.model;

import edu.epam.annotations.Column;

public class Topic {
	@Column("id")
	private int id;
	@Column("title")
	private String title;
	@Column("content")
	private String content;
	@Column("direction_id")
	private Integer directionId;
	@Column("teacher_id")
	private Integer teacherId;

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

	public String getContent() {
		return content;
	}

	public int getDirectionId() {
		return directionId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getTeacherId() {
		return teacherId;
	}
}
