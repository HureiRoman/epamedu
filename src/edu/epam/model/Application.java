package edu.epam.model;

import edu.epam.annotations.Column;

public class Application {
	@Column("id")
	private Integer id = null;
	@Column("user_id")
	private Integer userId = null;
	@Column("interview_id")
	private Integer interviewId = null;
	@Column("will_attend")
	private String willAttend;
	
	public Application() {
		
	}
	public Application(int userId, int interviewId) {
		
		this.userId = userId;
		this.interviewId = interviewId;
	}

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
	

	public int getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(int interviewId) {
		this.interviewId = interviewId;
	}

	public String getWillAttend() {
		return willAttend;
	}

	public void setWillAttend(String willAttend) {
		this.willAttend = willAttend;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
	public void setInterviewId(Integer interviewId) {
		this.interviewId = interviewId;
	}
}
