package com.cv.aggregator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsArticle {
	private String date;
	private String time;
	private String title;
	private String url;
	private String source;
	
	public String getDate() {
		return date;
	}
	@JsonProperty("Date")
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	@JsonProperty("Time")
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	@JsonProperty("Title")
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	@JsonProperty("Url")
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	@JsonProperty("Source")
	public void setSource(String source) {
		this.source = source;
	}
}
