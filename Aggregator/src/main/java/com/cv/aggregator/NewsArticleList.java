package com.cv.aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewsArticleList extends ArrayList<NewsArticle>{
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5679988694872432171L;

	
	public NewsArticleList(JSONObject XIgniteHeadlines) throws JsonParseException, JsonMappingException, IOException{
		JSONArray headlinesArray = XIgniteHeadlines.getJSONArray("Headlines");
		for(int i = 0; i < headlinesArray.length(); i++)
		{
			JSONObject headline = (JSONObject)headlinesArray.get(i);
			this.add(jsonMapper.readValue(headline.toString(), NewsArticle.class));
		}
	}
	
	public NewsArticleList(Collection<String> stringArticles) throws JsonParseException, JsonMappingException, IOException{
		for(String article : stringArticles){
			this.add(jsonMapper.readValue(article, NewsArticle.class));
		}
	}
}
