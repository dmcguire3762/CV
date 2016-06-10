package com.cv.analyzer.test;

import java.io.IOException;

import org.junit.*;

import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleAnalysisListTests {
	private ObjectMapper jsonMapper = new ObjectMapper();
	private CVDB db = new CVDB("article_analysis");
	
	@Test
	public void testArticleAnalysisList() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		ArticleAnalysisList analysisList = new ArticleAnalysisList();

		for(ArticleAnalysis article : analysisList){
			db.addToBatch(jsonMapper.writeValueAsString(article), CVDB.Operation.insert);
		}
		
		db.executeBatch(CVDB.Operation.insert);
	}
}
