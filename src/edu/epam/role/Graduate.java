package edu.epam.role;

import edu.epam.annotations.Column;

public class Graduate extends Student {
	@Column("graduate_id")
	private Integer graduateId;
	@Column("student_id")
	private Integer studentId;

	public Integer getGraduateId() {
		return graduateId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setGraduateId(Integer graduateId) {
		this.graduateId = graduateId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "Graduate [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", parentName=" + parentName
				+ ", email=" + email + ", roleType=" + roleType + ", password="
				+ password + ", isActive=" + isActive + ", lang=" + lang
				+ ", confirm_key=" + confirm_key + ", registration_date="
				+ registration_date + "]";
	}

}
