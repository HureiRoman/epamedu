package edu.epam.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDataFromSocialNetwork {
	private Long socialId;
	private String firstName;
	private String lastName;
	private String userBirth;
	private String education;
	private String phone;
	private String dataFrom;
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDateFormatted() {
		return userBirth;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getSocialId() {
		return socialId;
	}

	public void setSocialId(Long socialId) {
		this.socialId = socialId;
	}

	@Override
	public String toString() {
		return "UserDataFromSocialNetwork [firstName=" + firstName
				+ ", lastName=" + lastName + ", userBirth=" + userBirth
				+ ", education=" + education + ", phone=" + phone + "]";
	}

}
