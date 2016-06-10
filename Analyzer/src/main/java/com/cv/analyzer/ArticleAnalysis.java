package com.cv.analyzer;
import java.util.List;

import com.cv.aggregator.NewsArticle;

public class ArticleAnalysis extends NewsArticle{
	private List<Keyword> keywords;

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
}
