package com.cv.analyzer.test;

import com.cv.analyzer.ArticleAnalysisList;
import com.cv.analyzer.alchemy.AlchemyService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.junit.*;

public class AlchemyTests {
	private static AlchemyService alchemy = new AlchemyService();
	private static ObjectMapper jsonMapper = new ObjectMapper();
	
	@Test
	public void testAlchemyService() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		ArticleAnalysisList articleAnalysisList = new ArticleAnalysisList();
		alchemy.getAlchemyArticleAnalysis(articleAnalysisList.get(0));
		
		System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(articleAnalysisList.get(0)));
	}
}
