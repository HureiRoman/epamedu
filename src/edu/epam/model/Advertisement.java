package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;
import edu.epam.role.Teacher;

public class Advertisement {
	
	@Column("id")
	private int id;
	@Column("title")
	private String title;
	@Column("content")
	private String content;
	@Column("id_group")
	private int idGroup;
	@Column("id_teacher")
	private int idTeacher;
	@Column("is_archived")
	private boolean isArchived;
	@Column("advertisement_date")
	private Date advertisementDate;
	
	private Teacher advertisementOwner;
	
	public Teacher getAdvertisementOwner() {
		return advertisementOwner;
	}
	public void setAdvertisementOwner(Teacher advertisementOwner) {
		this.advertisementOwner = advertisementOwner;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	public int getIdTeacher() {
		return idTeacher;
	}
	public void setIdTeacher(int idTeacher) {
		this.idTeacher = idTeacher;
	}
	public boolean isArchived() {
		return isArchived;
	}
	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}
	public Date getAdvertisementDate() {
		return advertisementDate;
	}
	public void setAdvertisementDate(Date advertisementDate) {
		this.advertisementDate = advertisementDate;
	}
	@Override
	public String toString() {
		return "Advertisement [id=" + id + ", title=" + title + ", content="
				+ content + ", idGroup=" + idGroup + ", idTeacher=" + idTeacher
				+ ", isArchived=" + isArchived + ", advertisementDate="
				+ advertisementDate + "]";
	}

}
