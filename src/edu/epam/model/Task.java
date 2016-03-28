package edu.epam.model;

import java.util.Date;
import java.util.List;

import edu.epam.annotations.Column;
import edu.epam.constants.TypeOfTasks;

public class Task {
	@Column("id")
	private int id;
	@Column("title")
	private String title;
	@Column("task_body")
	private String taskBody;
	@Column("deadline")
	private Date deadline;
	@Column("type")
	private TypeOfTasks typeOfTasks;
	@Column("group_id")
	private Integer groupId;
	
	
	private List<GradesOfTask> gradesOfTask;
	private List<StudentAttachment> listOfStudentAttachmentsTasks;
	
	
	
	public Task(String title, String taskBody, Date deadline,
			TypeOfTasks typeOfTasks,int groupId) {

		this.title = title;
		this.taskBody = taskBody;
		this.deadline = deadline;
		this.typeOfTasks = typeOfTasks;
		this.groupId = groupId;
	}

	public Task(String title, String taskBody, Date deadline,
			TypeOfTasks typeOfTasks) {

		this.title = title;
		this.taskBody = taskBody;
		this.deadline = deadline;
		this.typeOfTasks = typeOfTasks;
	}

	public Task() {
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

	public String getTaskBody() {
		return taskBody;
	}

	public void setTaskBody(String taskBody) {
		this.taskBody = taskBody;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public TypeOfTasks getTypeOfTasks() {
		return typeOfTasks;
	}

	public void setTypeOfTasks(String typeOfTasks) {
		this.typeOfTasks = TypeOfTasks.valueOf(typeOfTasks.toUpperCase());
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public List<GradesOfTask> getGradesOfTask() {
		return gradesOfTask;
	}
	
	public void setGradesOfTask(List<GradesOfTask> gradesOfTask) {
		this.gradesOfTask = gradesOfTask;
	}
	
	public List<StudentAttachment> getListOfStudentAttachmentsTasks() {
		return listOfStudentAttachmentsTasks;
	}
	
	public void setListOfStudentAttachmentsTasks(
			List<StudentAttachment> listOfStudentAttachmentsTasks) {
		this.listOfStudentAttachmentsTasks = listOfStudentAttachmentsTasks;
	}
	
	
	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", taskBody=" + taskBody
				+ ", deadline=" + deadline + ", typeOfTasks=" + typeOfTasks
				+ ", groupId=" + groupId + "]";
	}



}
