package com.cv.analyzer.test;

import java.io.IOException;

import org.junit.*;

import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ArticleAnalysisListTests {

	
	@Test
	public void testArticleAnalysisList() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		ArticleAnalysisList analysisList = new ArticleAnalysisList();

		for(ArticleAnalysis article : analysisList){
			System.out.println(article.getTitle());
		}
	}
}
