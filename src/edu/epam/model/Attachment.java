package edu.epam.model;

import edu.epam.annotations.Column;

public class Attachment {
	@Column("id")
	private Integer id;
	@Column("topic_id")
	private Integer topicId;
	@Column("id")
	private Integer directionId;
	@Column("title")
	private String title;
	@Column("extension")
	private String extension;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDirectionId() {
		return directionId;
	}

	public String getExtension() {
		return extension;
	}

	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	public Attachment() {

	}

	public void setExtension(String extension) {
		this.extension = extension;
	}



	public void setId(Integer id) {
		this.id = id;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public void setDirectionId(Integer directionId) {
		this.directionId = directionId;
	}
	
	
	
}
