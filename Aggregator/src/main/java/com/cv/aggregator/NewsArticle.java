package com.cv.aggregator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"_id"})
public class NewsArticle {
	private String date;
	private String time;
	private String title;
	private String url;
	private String source;
	private String text;
	
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
		readUrl();
	}
	public String getSource() {
		return source;
	}
	@JsonProperty("Source")
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getText(){
		return text;
	}
	
	private void readUrl(){
		if(url == null){
			return;
		}
		
		String html = "";
		InputStream in = null;
		try{
			in = new URL(url).openStream();
			html = IOUtils.toString(in);
			
			text = PlainTextParser.html2PlainText(html);
		}catch(Exception e){
			text = null;
		}finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	
}
