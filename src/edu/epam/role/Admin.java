package edu.epam.role;

public class Admin extends CommonUser {

	@Override
	public String toString() {
		return "Admin [getId()=" + getId() + ", getFirstName()="
				+ getFirstName() + ", getLastName()=" + getLastName()
				+ ", getParentName()=" + getParentName() + ", getLang()="
				+ getLang() + ", getEmail()=" + getEmail() + ", getRoleType()="
				+ getRoleType() + ", getPassword()=" + getPassword()
				+ ", getIsActive()=" + getIsActive() +  "]";
	}

}
