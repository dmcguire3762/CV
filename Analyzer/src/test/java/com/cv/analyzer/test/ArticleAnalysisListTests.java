package com.cv.analyzer.test;

import java.io.IOException;

import org.junit.*;

import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;
import com.cv.analyzer.alchemy.AlchemyService;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleAnalysisListTests {
	private ObjectMapper jsonMapper = new ObjectMapper();
	private CVDB article_db = new CVDB("articles");
	private CVDB analysis_db = new CVDB("article_analysis");
	private CVDB archive_analysis_db = new CVDB("archive_article_analysis");
	private static AlchemyService alchemy = new AlchemyService();
	
	@Test
	public void testArticleAnalysisList() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		ArticleAnalysisList analysisList = new ArticleAnalysisList();
		for(String article : article_db.findObjects()){
			analysisList.add(jsonMapper.readValue(article, ArticleAnalysis.class));
		}

		for(ArticleAnalysis article : analysisList){
			alchemy.getAlchemyArticleAnalysis(article);
			analysis_db.addToBatch(jsonMapper.writeValueAsString(article), CVDB.Operation.insert);
			archive_analysis_db.addToBatch(jsonMapper.writeValueAsString(article), CVDB.Operation.insert);
		}
		
		analysis_db.executeBatch(CVDB.Operation.insert);
		archive_analysis_db.executeBatch(CVDB.Operation.insert);
		article_db.clearCollection();
	}
}
