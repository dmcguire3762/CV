package com.cv.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import com.jayway.restassured.RestAssured;

public class AlchemyService {
	private final static String alchemyBaseUrl = "http://gateway-a.watsonplatform.net/calls/url/";
	private final static String alchemyKeywordServiceUrl = "URLGetRankedKeywords";
	private final static int transactionLimit = 1000;
	
	private static List<AlchemyAPIKey> alchemyAPIKeys = new ArrayList<AlchemyAPIKey>();
	static{
		alchemyAPIKeys.add(new AlchemyAPIKey("72a5f8e6c0e85a47397086542b9e845024bf04bd"));
		alchemyAPIKeys.add(new AlchemyAPIKey("b808316798b5d32ce4d463dd572b916160ec72d8"));
		alchemyAPIKeys.add(new AlchemyAPIKey("0eb3f26b83a2786e9610d19e8135cc9bf5302ee4"));
		alchemyAPIKeys.add(new AlchemyAPIKey("2fc96c1ac120cd2a49959a1eb5f15efec8b06959"));
		alchemyAPIKeys.add(new AlchemyAPIKey("7e7eff43fc2178fd5cb2ab801903809bf45fb30f"));
		alchemyAPIKeys.add(new AlchemyAPIKey("932525c6b28f7da2632ec37e916f3da7f0681a5d"));
		alchemyAPIKeys.add(new AlchemyAPIKey("81ea8486b9ccc67744857e8c2d0a21f6386dfd03"));
		
		for(Iterator<AlchemyAPIKey> iter = alchemyAPIKeys.iterator(); iter.hasNext();){
			AlchemyAPIKey apiKey = iter.next();
			int tranCount = requestTransactionCount(apiKey.getKey());
			if(tranCount > transactionLimit){
				iter.remove();
			} else {
				apiKey.setTransactionCount(tranCount);
			}
		}
	}

	private static int requestTransactionCount(String key) {
		String url = "http://access.alchemyapi.com/calls/info/GetAPIKeyInfo?apikey=" + key + "&outputMode=json";
		JSONObject response = new JSONObject(RestAssured.get(url).body().asString());
		int transactions = response.getInt("consumedDailyTransactions");
		return transactions;
	}
	
	public AlchemyService(){
		
	}
	
	private String getAlchemyUrl(String articleUrl){
		String formattedUrl = alchemyBaseUrl;
		formattedUrl += alchemyKeywordServiceUrl + "?outputMode=json&";
		
		String key = getAPIKey();
		if(key == null){
			return null;
		}
		
		formattedUrl += "apikey="+ key + "&";
		formattedUrl += "url=" + articleUrl + "&";
		formattedUrl += "sentiment=1";
		
		return formattedUrl;
	}
	
	private String getAPIKey(){
		AlchemyAPIKey apiKey;
		if(alchemyAPIKeys.size() > 0){
			apiKey = alchemyAPIKeys.get(0);
			apiKey.addTransactions(2);
			if(apiKey.getTransactionCount() > transactionLimit){
				alchemyAPIKeys.remove(0);
			}
			
			return apiKey.getKey();
		} else {
			return null;
		}
	}
	
	public void getAlchemyArticleAnalysis(ArticleAnalysis articleAnalysis){
		String formattedURL = getAlchemyUrl(articleAnalysis.getUrl());
		String alchemyResponse = RestAssured.get(formattedURL).body().asString();
		System.out.println(alchemyResponse);
	}
}
