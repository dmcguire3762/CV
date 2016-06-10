package com.cv.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleAnalysisList extends ArrayList<ArticleAnalysis>{
	private static final long serialVersionUID = 275866695847518882L;
	private static ObjectMapper jsonMapper = new ObjectMapper();
	
	public ArticleAnalysisList() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		CVDB db = new CVDB("articles");
		System.out.println("Retrieving articles from mongoDB...");
		List<String> articleStrings = db.findObjects();
		System.out.println("De-Serializing articles...");
		for(String articleString : articleStrings){
			this.add(jsonMapper.readValue(articleString, ArticleAnalysis.class));
		}
	}
}
