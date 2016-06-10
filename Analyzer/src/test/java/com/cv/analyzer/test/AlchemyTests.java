package com.cv.analyzer.test;

import com.cv.analyzer.AlchemyService;
import com.cv.analyzer.ArticleAnalysisList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

import org.junit.*;

public class AlchemyTests {
	private static AlchemyService alchemy = new AlchemyService();
	
	@Test
	public void testAlchemyService() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		ArticleAnalysisList articleAnalysisList = new ArticleAnalysisList();
		alchemy.getAlchemyArticleAnalysis(articleAnalysisList.get(0));
	}
}
