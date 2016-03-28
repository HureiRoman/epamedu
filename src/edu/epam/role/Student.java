package edu.epam.role;

import java.util.List;

import edu.epam.annotations.Column;
import edu.epam.annotations.InnerObject;
import edu.epam.model.CV;
import edu.epam.model.StudentAttachment;

public class Student extends CommonUser {
	@InnerObject(tableName="cv",thisObjectKey="id",wrapperObjectField="id")
	private CV cv;
	
	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	@Column("group_id")
	private int groupId;
	
	private List<StudentAttachment> studentTask;


	public List<StudentAttachment> getStudentTask() {
		return studentTask;
	}

	public void setStudentTask(List<StudentAttachment> studentTask) {
		this.studentTask = studentTask;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "Student [cv=" + cv + ", groupId=" + groupId + ", id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", parentName=" + parentName
				+ ", email=" + email + ", roleType=" + roleType + ", password="
				+ password + ", isActive=" + isActive + ", lang=" + lang
				+ ", confirm_key=" + confirm_key + ", registration_date="
				+ registration_date + "]";
	}


}
