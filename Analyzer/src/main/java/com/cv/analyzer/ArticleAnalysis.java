package com.cv.analyzer;

import com.cv.aggregator.NewsArticle;

public class ArticleAnalysis extends NewsArticle{
	private KeywordList keywords;

	public KeywordList getKeywords() {
		if(keywords == null){
			keywords = new KeywordList();
		}
		return keywords;
	}

	public void setKeywords(KeywordList keywords) {
		this.keywords = keywords;
	}
	
	
}
