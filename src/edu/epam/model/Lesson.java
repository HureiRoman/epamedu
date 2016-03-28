package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;

public class Lesson {
	@Column("id")
	private Integer id;
	@Column("group_id")
	private Integer groupId;
	@Column("topic_id")
	private Integer topicId;
	@Column("task_id")
	private Integer taskId;
	@Column("isVisible")
	private boolean isVisible;
	@Column("lessonTime")
	private Date lessonTime;
	@Column("autoTurnOn")
	private boolean autoTurnOn;
	

	public Lesson(Integer groupId, Integer topicId, Integer taskId,
			boolean isVisible, Date lessonTime) {
		this.groupId = groupId;
		this.topicId = topicId;
		this.taskId = taskId;
		this.isVisible = isVisible;
		this.lessonTime = lessonTime;
	}
	
	
	public Lesson() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setIsVisible(String isVisible) {
		this.isVisible = Boolean.valueOf(isVisible);
	}
	public Date getLessonTime() {
		return lessonTime;
	}
	public void setLessonTime(Date lessonTime) {
		this.lessonTime = lessonTime;
	}
	public boolean isAutoTurnOn() {
		return autoTurnOn;
	}
	
	public void setAutoTurnOn(String autoTurnOn) {
		this.autoTurnOn = Boolean.valueOf(autoTurnOn);
	}
     
 
    
 
}
