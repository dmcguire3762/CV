package com.cv.aggregator.test;

import com.cv.aggregator.NewsArticle;
import com.cv.aggregator.NewsArticleList;
import com.cv.aggregator.XIgniteService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

import org.junit.*;

public class ParserTests {
	private static XIgniteService xignite = new XIgniteService();

	
	@Test
	public void testReadAndParse() throws JsonParseException, JsonMappingException, IOException{
		NewsArticleList newsArticles = new NewsArticleList(xignite.getTopMarketHeadlines());
		NewsArticle article = newsArticles.get(0);
		System.out.println(article.getUrl());
		System.out.println(article.getText());
	}
}
