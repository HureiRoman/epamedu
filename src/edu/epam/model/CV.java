package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;
import edu.epam.annotations.RegexCheck;
import edu.epam.constants.EnglishLevel;

public class CV {
	@Column("id")
	private Integer id;
	
	@RegexCheck(regex="\\(\\d{3}\\)-\\d{3}-\\d{4}")
	@Column("phone")
	private String phone;
	
	@Column("birth")
	private Date birth; //??
	
	@RegexCheck(regex="^[^<>]{0,250}$")
	@Column("objective")
	private String objective;
	
	@RegexCheck(regex="^[^<>]{0,250}$")
	@Column("skills")
	private String skills;
	
	@RegexCheck(regex="^[^<>]{0,250}$")
	@Column("additional_info")
	private String additionalInfo;
	
	@RegexCheck(regex="^[^<>]{0,250}$")
	@Column("education")
	private String education;
	
	@RegexCheck(regex="[A-C][1-2]$")
	@Column("english_level")
	private EnglishLevel englishLevel;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public EnglishLevel getEnglishLevel() {
		return englishLevel;
	}
	public void setEnglishLevel(String englishLevel) {
		this.englishLevel = EnglishLevel.valueOf(englishLevel.toUpperCase());
	}
	@Override
	public String toString() {
		return "CV [id=" + id + ", phone=" + phone + ", birth=" + birth
				+ ", objective=" + objective + ", skills=" + skills
				+ ", additionalInfo=" + additionalInfo + ", education="
				+ education + ", englishLevel=" + englishLevel + "]";
	}
	
}
