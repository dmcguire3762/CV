package com.cv.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import com.cv.analyzer.ArticleAnalysis;
import com.cv.analyzer.ArticleAnalysisList;
import com.cv.analyzer.Keyword;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class Engine {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static final String companyListFile = "src/main/resources/companyList.json";
	private static StockList stockList;
	
	// Maps to hold get stocks quicker
	private static HashMap<String, Stock> tickerStockMap = new HashMap<String, Stock>();
	private static HashMap<String, Stock> companyNameStockMap = new HashMap<String, Stock>();
	private static HashMap<String, Stock> sectorStockMap = new HashMap<String, Stock>();
	private static HashMap<String, Stock> industryStockMap = new HashMap<String, Stock>();
	static{
		try {
			jsonMapper.setAnnotationIntrospector(new IgnoreThreadIntrospector());
			stockList = jsonMapper.readValue(new File(companyListFile), StockList.class);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
		initMaps();
	}
	
	private static class IgnoreThreadIntrospector extends JacksonAnnotationIntrospector{
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m){
			return m.getDeclaringClass() == Thread.class || super.hasIgnoreMarker(m);
		}
	}
	
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		stateMachine();
	}
	
	private static void initMaps() {
		for(Stock stock : stockList){
			tickerStockMap.put(stock.getTicker(), stock);
			companyNameStockMap.put(stock.getCompanyName(), stock);
			sectorStockMap.put(stock.getSector(), stock);
			industryStockMap.put(stock.getIndustry(), stock);
		}
	}

	/**
	 * 1. Get new articles
	 * 2. Populate stocks with the articles
	 * 3. evaluate scores of the stocks
	 * 4. publish results
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private static void stateMachine() throws JsonParseException, JsonMappingException, IOException, InterruptedException{
		while(true){
			// Get new articles
			CVDB article_analysis_db = new CVDB("article_analysis");
			ArticleAnalysisList articles = new ArticleAnalysisList();
			for(String article : article_analysis_db.findObjects()){
				articles.add(jsonMapper.readValue(article, ArticleAnalysis.class));
			}
			
			System.out.println("Applying articles to stocks");
			// apply articles to stocks
			for(ArticleAnalysis article : articles){
				applyArticleToStocks(article);
			}
			
			// begin stock evaluation
			System.out.println("Evaluating " + stockList.size() + " stocks...");
			evaluateStocks();
			
			
			
			System.out.println("Publishing Stocks...");
			// publish stocks to db
			publishStocks();
			
			
			System.out.println("Sleeping until next run...");
			// Sleep for a while until new articles are gathered. (5 mins).
			Thread.sleep(1000 * 60 * 5);
		}
	}


	/**
	 * Check for:
	 * 1. Keyword is ticker
	 * 2. Keyword is company name
	 * 3. Keyword is sector
	 * 4. Keyword is industry
	 * 5. Title contains ticker
	 * 6. Title contains company name
	 * 7. Title contains sector
	 * 8. Title contains industry
	 * @param article
	 */
	private static void applyArticleToStocks(ArticleAnalysis article) {
		List<Stock> applicableStocks = new ArrayList<Stock>();
		for(Keyword keyword : article.getKeywords()){
			applicableStocks.addAll(getStocksForKeyword(keyword));
		}
		
		String articleTitle = article.getTitle().toLowerCase();
		for(Stock stock : stockList){
			/*if(articleTitle.contains(stock.getTicker().toLowerCase())){
				applicableStocks.add(stock);
				continue;
			}
			
			if(articleTitle.contains(stock.getCompanyName().toLowerCase())){
				applicableStocks.add(stock);
				continue;
			}*/
			
			if(articleTitle.contains(stock.getSector().toLowerCase())){
				applicableStocks.add(stock);
				continue;
			}
			
			if(articleTitle.contains(stock.getIndustry().toLowerCase())){
				applicableStocks.add(stock);
				continue;
			}
		}
		
		for(Stock stock : applicableStocks){
			stock.addArticle(article);
		}
	}

	private static List<Stock> getStocksForKeyword(Keyword keyword) {
		List<Stock> applicableStocks = new ArrayList<Stock>();
		Stock applicableStock = tickerStockMap.get(keyword);
		if(applicableStock != null && !applicableStocks.contains(applicableStock)){
			applicableStocks.add(applicableStock);
		}
		
		applicableStock = companyNameStockMap.get(keyword);
		if(applicableStock != null && !applicableStocks.contains(applicableStock)){
			applicableStocks.add(applicableStock);
		}
		
		applicableStock = sectorStockMap.get(keyword);
		if(applicableStock != null && !applicableStocks.contains(applicableStock)){
			applicableStocks.add(applicableStock);
		}
		
		applicableStock = industryStockMap.get(keyword);
		if(applicableStock != null && !applicableStocks.contains(applicableStock)){
			applicableStocks.add(applicableStock);
		}

		return applicableStocks;
	}
	
	private static void publishStocks() throws JsonGenerationException, JsonMappingException, IOException{
		// sort the stocks by score
		Collections.sort(stockList, new StockScoreSorter());
		
		CVDB stocks_db = new CVDB("stocks");
		for(Stock stock : stockList){
			if(stock.getScore() > 0.0){
				System.out.println(jsonMapper.writeValueAsString(stock));
				//	stocks_db.addToBatch(jsonMapper.writeValueAsString(stock), CVDB.Operation.insert);
			}
		}
		
		stocks_db.executeBatch(CVDB.Operation.insert);
	}


	private static void evaluateStocks() throws InterruptedException {
		Thread[] stockThreads = new Thread[stockList.size()];
		for(int i = 0; i < stockThreads.length; i++){
			stockThreads[i] = new Thread(stockList.get(i));
			stockThreads[i].start();
		}
		
		for(int i = 0; i < stockThreads.length; i++){
			stockThreads[i].join();
		}
	}
	
	private static class StockScoreSorter implements Comparator<Stock>{

		@Override
		public int compare(Stock arg0, Stock arg1) {
			if(Math.abs(arg0.getScore()) > Math.abs(arg1.getScore())){
				return 1;
			} else if(Math.abs(arg0.getScore()) < Math.abs(arg1.getScore())){
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	
}
