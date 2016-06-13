package com.cv.engine;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;
import com.cv.analyzer.Keyword;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class TradingDayAnalysis {
	@JsonIgnore()
	private ArticleAnalysisList articlesForDay = new ArticleAnalysisList();
	private final LocalDate date;
	@JsonIgnore()
	private final double relevanceThreshold = 0.0;
	private double score = 0.0;
	
	public TradingDayAnalysis(LocalDate date){
		this.date = date;
	}
	
	public void addArticle(ArticleAnalysis article){
		articlesForDay.add(article);
	}
	
	public double getScore(){
		return score;
	}

	public void evaluate() {
		double totalSentiment = 0;
		int numKeywords = 0;
		for(ArticleAnalysis article : articlesForDay){
			for(Keyword keyword: article.getKeywords()){
				if(keyword.getRelevance() > relevanceThreshold){
					totalSentiment += keyword.getSentiment().getScore();
					numKeywords++;
				}
			}
		}
		
		score = totalSentiment / (double)numKeywords;
		adjustScoreForDate();
	}

	/**
	 * Adjust for date.  Older dates are scored lower, because they are less relevant.
	 * Adjusting by 1 / (1 + x) where x is the number of days between today and the trading date.
	 */
	private void adjustScoreForDate() {
		double x = Days.daysBetween(date, LocalDate.now()).getDays();
		score *= 1.0 / (1.0 + x);
	}
	
	
}
