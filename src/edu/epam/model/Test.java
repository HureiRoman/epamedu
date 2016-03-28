package edu.epam.model;

import edu.epam.annotations.Column;

public class Test {
	@Column("id")
	private Integer id;
	@Column("question")
	private String question;
	@Column("code")
	private String code;
	@Column("answer1")
	private String answer1;
	@Column("answer2")
	private String answer2;
	@Column("answer3")
	private String answer3;
	@Column("answer4")
	private String answer4;
	@Column("correct")
	private Integer correct;
	@Column("direction_id")
	private Integer directionId;
	private final String answer0 = "";
	private Integer userAnswer = null;

	public String getAnswer0() {
		return answer0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public Integer getCorrect() {
		return correct;
	}

	public void setCorrect(Integer correct) {
		this.correct = correct;
	}

	public Integer getDirectionId() {
		return directionId;
	}

	public void setDirectionId(Integer directionId) {
		this.directionId = directionId;
	}

	public Integer getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(Integer userAnswer) {
		this.userAnswer = userAnswer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
