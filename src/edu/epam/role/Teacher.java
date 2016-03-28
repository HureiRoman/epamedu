package edu.epam.role;

import java.util.List;

import edu.epam.model.Group;

public class Teacher extends CommonUser {

	List<Group> listOfGroups;
	

	public List<Group> getListOfGroups() {
		return listOfGroups;
	}

	public void setListOfGroups(List<Group> listOfGroups) {
		this.listOfGroups = listOfGroups;
	}

	@Override
	public String toString() {
		return "Teacher [listOfGroups=" + listOfGroups + ", id=" + id
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", parentName=" + parentName + ", email=" + email
				+ ", roleType=" + roleType + ", password=" + password
				+ ", isActive=" + isActive + ", lang=" + lang
				+ ", confirm_key=" + confirm_key + ", registration_date="
				+ registration_date + "]";
	}

	
	
	
	
	
	
}
