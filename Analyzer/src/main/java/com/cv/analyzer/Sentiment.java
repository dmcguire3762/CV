package com.cv.analyzer;

public class Sentiment {
	private double score;
	private String type; // positive, neutral, or negative
	private int mixed;
	
	public int getMixed() {
		return mixed;
	}
	public void setMixed(int mixed) {
		this.mixed = mixed;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
