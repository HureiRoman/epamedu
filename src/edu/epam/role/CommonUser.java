package edu.epam.role;

import java.util.Date;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.epam.annotations.Column;
import edu.epam.annotations.RegexCheck;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.model.TestResults;

public abstract class CommonUser {

	@Column("id")
	protected int id;
	@RegexCheck(regex = "^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$")
	@Column("fname")
	protected String firstName;

	@RegexCheck(regex = "^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$")
	@Column("lname")
	protected String lastName;

	@RegexCheck(regex = "^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$")
	@Column("pname")
	protected String parentName;

	@Column("email")
	@RegexCheck(regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
	protected String email;

	@Column("role")
	protected RoleType roleType;
	@XStreamOmitField
	@RegexCheck(regex = "^.[^<>]{4,35}$")
	@Column("password")
	protected String password;

	@Column("isActive")
	protected boolean isActive;

	@Column("language")
	protected String lang;

	@Column("confirm_key")
	protected String confirm_key;

	@Column("reg_date")
	protected Date registration_date;

	@Column("vk")
	protected String vkToken;

	@Column("fb")
	protected String fbToken;


	

	private int newMessages;

	protected Map<Direction, TestResults> testResults;

	public Map<Direction, TestResults> getTestResults() {
		return testResults;
	}

	public void setTestResults(Map<Direction, TestResults> testResults) {
		this.testResults = testResults;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public String getConfirm_key() {
		return confirm_key;
	}

	public void setConfirm_key(String confirm_key) {
		this.confirm_key = confirm_key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = RoleType.valueOf(roleType.toUpperCase());
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = Boolean.valueOf(isActive);
	}

	public int getNewMessages() {
		return newMessages;
	}

	public void setNewMessages(int newMessages) {
		this.newMessages = newMessages;
	}


	public String getVkToken() {
		return vkToken;
	}

	public void setVkToken(String vkToken) {
		this.vkToken = vkToken;
	}

	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CommonUser that = (CommonUser) o;

		if (id != that.id)
			return false;
		return email.equals(that.email);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + email.hashCode();
		return result;
	}
}
