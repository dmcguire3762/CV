package com.cv.engine;

import java.util.Collection;
import java.util.HashMap;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;

public class Stock implements Runnable{
	private String ticker;
	private String companyName;
	private String industry;
	private String sector;
	private ArticleAnalysisList articles;
	private HashMap<LocalDate, TradingDayAnalysis> dayAnalysisMap = new HashMap<LocalDate, TradingDayAnalysis>(); 
	private double score = 0.0;
	
	public ArticleAnalysisList getArticles() {
		return articles;
	}
	
	public double getScore(){
		return score;
	}
	
	public Collection<TradingDayAnalysis> getTradingDays(){
		return dayAnalysisMap.values();
	}
	
	public void addArticle(ArticleAnalysis article){
		if(articles == null){
			articles = new ArticleAnalysisList();
		}
		
		articles.add(article);
		
		LocalDate articleDate = LocalDate.parse(article.getDate(), DateTimeFormat.forPattern("mm/dd/yyyy"));
		TradingDayAnalysis dayAnalysis = dayAnalysisMap.get(articleDate);
		if(dayAnalysis == null){
			dayAnalysis = new TradingDayAnalysis(articleDate);
			dayAnalysisMap.put(articleDate, dayAnalysis);
		}
		
		dayAnalysis.addArticle(article);

	}
	
	public void setArticles(ArticleAnalysisList articles) {
		this.articles = articles;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	
	@Override
	public void run(){
		score = 0.0;
		for(TradingDayAnalysis tradingDay : dayAnalysisMap.values()){
			tradingDay.evaluate();
			score += tradingDay.getScore();
		}
	}
}
