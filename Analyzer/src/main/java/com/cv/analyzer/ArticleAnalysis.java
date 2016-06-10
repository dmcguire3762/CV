package com.cv.analyzer;
import java.util.List;

import com.cv.aggregator.NewsArticle;

public class ArticleAnalysis extends NewsArticle{
	private KeywordList keywords;

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(KeywordList keywords) {
		this.keywords = keywords;
	}
	
	
}
