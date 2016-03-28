package edu.epam.role;

public class HR extends CommonUser {

	@Override
	public String toString() {
		return "HR [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", parentName=" + parentName + ", email=" + email
				+ ", roleType=" + roleType + ", password=" + password
				+ ", isActive=" + isActive + ", lang=" + lang
				+ ", confirm_key=" + confirm_key + ", registration_date="
				+ registration_date + "]";
	}



}
