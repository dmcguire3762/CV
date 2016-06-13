package com.cv.analyzer;

import java.io.IOException;
import com.cv.analyzer.alchemy.AlchemyService;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Analyzer {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static CVDB article_db = new CVDB("articles");
	private static CVDB analysis_db = new CVDB("article_analysis");
	private static CVDB archive_analysis_db = new CVDB("archive_article_analysis");
	private static AlchemyService alchemy = new AlchemyService();
	
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		while(true){
			try{
				ArticleAnalysisList analysisList = new ArticleAnalysisList();
				for(String article : article_db.findObjects()){
					analysisList.add(jsonMapper.readValue(article, ArticleAnalysis.class));
				}
		
				for(ArticleAnalysis article : analysisList){
					alchemy.getAlchemyArticleAnalysis(article);
					analysis_db.addToBatch(jsonMapper.writeValueAsString(article), CVDB.Operation.insert);
					archive_analysis_db.addToBatch(jsonMapper.writeValueAsString(article), CVDB.Operation.insert);
				}
			} finally {
				analysis_db.executeBatch(CVDB.Operation.insert);
				archive_analysis_db.executeBatch(CVDB.Operation.insert);
			}
			
			article_db.clearCollection();
			
			System.out.println("Sleeping...");
			Thread.sleep(1000 * 60 * 120);
		}
	}
}
