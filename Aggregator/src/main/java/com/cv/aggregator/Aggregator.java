package com.cv.aggregator;


import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalTime;
import com.cv.mongo.CVDB;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Aggregator {
	private static CVDB articleDB = new CVDB("articles");
	private static CVDB articleArchiveDB = new CVDB("archive_articles");
	private static XIgniteService xignite = new XIgniteService();
	private static ObjectMapper mapper = new ObjectMapper();
	private static Set<String> articleBlacklist = new HashSet<String>();
	static{
		initArticleBlackList();
	}
	
	public Aggregator(){
		
	}
	
	private static void initArticleBlackList() {
		articleBlacklist = new HashSet<String>();
		articleBlacklist.add("feeds.bizjournals.com");
	}

	public static void main(String[] args){
		while(true){
			
			// Get news articles and publish them to the db & to the archive
			try {
				System.out.println("Running...");
				NewsArticleList newsArticles = new NewsArticleList(xignite.getTopMarketHeadlines());
				System.out.println("Retrieved " + newsArticles.size() + " from xignite");
				System.out.println("Publishing articles to db...");
				for(NewsArticle article : newsArticles){
					//article.parseUrl();
					if(isArticleBlacklisted(article)){
						continue;
					}
					articleDB.addToBatch(mapper.writeValueAsString(article), CVDB.Operation.insert);
					articleArchiveDB.addToBatch(mapper.writeValueAsString(article), CVDB.Operation.insert);
				}
				
				articleDB.executeBatch(CVDB.Operation.insert);
				articleArchiveDB.executeBatch(CVDB.Operation.insert);
				System.out.println("Done");
			} catch (Exception e) {
				System.out.println("Failed to gather news articles - bailing out");
				throw new RuntimeException(e);
			}
			
			try {
				// Sleep for 2 hrs
				System.out.println("Sleeping for 2 hours - next run at " + LocalTime.now().plusHours(2));
				Thread.sleep(1000 * 60 * 120);
			} catch (InterruptedException e) {
				System.out.println("Wait time was interrupted - bailing out");
				throw new RuntimeException(e);
			}
		}
	}

	private static boolean isArticleBlacklisted(NewsArticle article) {
		for(String blacklistedStr : articleBlacklist){
			if(article.getUrl().contains(blacklistedStr)){
				return true;
			}
		}
		
		return false;
	}
}
