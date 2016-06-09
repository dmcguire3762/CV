package com.cv.aggregator.test;

import java.io.IOException;

import org.junit.Test;

import com.cv.aggregator.NewsArticle;
import com.cv.aggregator.NewsArticleList;
import com.cv.aggregator.XIgniteService;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestNewsCRUD {
	private static XIgniteService xignite = new XIgniteService();
	private static CVDB db = new CVDB("ArticleTests");
	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testInsert() throws JsonParseException, JsonMappingException, IOException{
		NewsArticleList newsArticles = new NewsArticleList(xignite.getTopMarketHeadlines());
		for(NewsArticle article : newsArticles){
			db.addToBatch(mapper.writeValueAsString(article), CVDB.Operation.insert);
		}
		
		db.executeBatch(CVDB.Operation.insert);
		
		NewsArticleList articlesFromDB = new NewsArticleList(db.findObjects());
		for(NewsArticle article : articlesFromDB){
			System.out.println(mapper.writeValueAsString(article));
		}
	}
}
