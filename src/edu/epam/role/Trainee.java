package edu.epam.role;

import edu.epam.annotations.Column;
import edu.epam.model.CV;

public class Trainee extends CommonUser {

	private CV cv;

	@Column("is_aplicant")
	private String isAplicant;
	@Column("receive_news")
	private boolean receiveNews;
	
	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public String getIsAplicant() {
		return isAplicant;
	}

	public void setIsAplicant(String isAplicant) {
		this.isAplicant = isAplicant;
	}

	public boolean getReceiveNews() {
		return receiveNews;
	}

	public void setReceiveNews(String receiveNews) {
		this.receiveNews = Boolean.valueOf(receiveNews);
	}

	@Override
	public String toString() {
		return "Trainee [cv=" + cv + ", testResults=" + testResults
				+ ", isAplicant=" + isAplicant + ", receiveNews=" + receiveNews
				+ ", id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", parentName=" + parentName + ", email=" + email
				+ ", roleType=" + roleType + ", password=" + password
				+ ", isActive=" + isActive + ", lang=" + lang
				+ ", confirm_key=" + confirm_key + ", registration_date="
				+ registration_date + "]";
	}
	
	
	

}
