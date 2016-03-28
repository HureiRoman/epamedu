package edu.epam.model;



import java.util.Date;

import edu.epam.annotations.Column;

public class NewsItem {
	@Column("id")
	private int id;
	
	@Column("content")
	private String content;
	
	@Column("message_date")
	private Date messageDate;
	
	@Column("description")
	private String description;
	
	@Column("title")
	private String title;
	
	@Column("isArchived")
	private Boolean isArchived;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(String isArchived) {
		this.isArchived = Boolean.valueOf(isArchived);
	}

	@Override
	public String toString() {
		return "NewsItem [id=" + id + ", content=" + content + ", messageDate="
				+ messageDate + ", description=" + description + ", title="
				+ title + ", isArchived=" + isArchived + "]";
	} 
	
	

}
