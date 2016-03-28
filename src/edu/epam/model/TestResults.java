package edu.epam.model;

import java.text.DecimalFormat;

import edu.epam.annotations.Column;

public class TestResults {
	
	@Column("direction_id")
	private Integer direction;
	@Column("user_id")
	private Integer userId;
	@Column("test_count")
	private Integer testCount;
	@Column("points")
	private Integer points;
	private Integer position = null;
	private String top10UserName;
	private double currentRate;
	
	public TestResults() {
		
	}
	
	public TestResults(Integer direction, Integer userId, Integer testCount, Integer points) {
		setDirection(direction);
		setUserId(userId);
		setTestCount(testCount);
		setPoints(points);
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Integer getTestCount() {
		return testCount;
	}
	public void setTestCount(Integer testCount) {
		this.testCount = testCount;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getRate() {
		DecimalFormat numberFormat = new DecimalFormat("#0.00");
		double rate = (double) (points * 10) / (testCount * 12);
		return numberFormat.format(rate);
	}
	
	public void passNewTest(Integer points) {
		testCount++;
		this.points += points;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getTop10UserName() {
		return top10UserName;
	}

	public void setTop10UserName(String top10UserName) {
		this.top10UserName = top10UserName;
	}

	public double getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(double currentRate) {
		this.currentRate = currentRate;
	}
}
