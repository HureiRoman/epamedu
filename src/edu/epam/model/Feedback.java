package edu.epam.model;

public class Feedback {
	private int id;
	private int userId;
	private String message;
	private int directionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getDirectionId() {
		return directionId;
	}
	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}
	
	

}
