package com.cv.aggregator;

import org.json.JSONObject;

import com.jayway.restassured.RestAssured;

public class XIgniteService {
	private static final String apiToken = "41BB3AEBDF9340E5BA5280EB277B0F64";
	private static final String topMarketHeadlinesUrl = "http://globalnews.xignite.com/xGlobalNews.json/GetTopMarketHeadlines?Count=10000&_fields=Headlines.Title,Headlines.Date,Headlines.Time,Headlines.Source,Headlines.Url";
	
	public XIgniteService(){
		
	}
	
	public JSONObject getTopMarketHeadlines(){
		String url = getFormattedUrl(topMarketHeadlinesUrl);
		return new JSONObject(RestAssured.get(url).body().asString());
	}
	
	private String getFormattedUrl(String url){
		return url + "&_token=" + apiToken;
	}
}
