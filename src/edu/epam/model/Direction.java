package edu.epam.model;

import java.util.List;

import edu.epam.annotations.Column;

public class Direction {
	@Column("id")
	private int id;
	@Column("name")
	private String name;
	@Column("recruter_id")
	private int recruter_id;
	@Column("is_active")
	private Boolean isActive;
	@Column("description")
	private String description;
	@Column("code_language")
	private String codeLang;
	
	private Interview interview;
	
	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Direction(String name, int recruter_id) {
		this.name = name;
		this.recruter_id = recruter_id;
	}

	public Direction(int id, String name, int recruter_id) {
		this.id = id;
		this.name = name;
		this.recruter_id = recruter_id;
	}

	public Direction() {
		// TODO Auto-generated constructor stub
	}

	private List<Group> groups;

	public int getRecruter_id() {
		return recruter_id;
	}

	public void setRecruter_id(int recruter_id) {
		this.recruter_id = recruter_id;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = Boolean.valueOf(isActive);
	}

	public String getCodeLang() {
		return codeLang;
	}

	public void setCodeLang(String codeLang) {
		this.codeLang = codeLang;
	}



}
