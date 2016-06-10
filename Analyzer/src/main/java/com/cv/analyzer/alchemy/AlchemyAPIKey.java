package com.cv.analyzer.alchemy;

public class AlchemyAPIKey {
	private final String key;
	private int transactionCount = 0;
	
	public AlchemyAPIKey(String key){
		this.key = key;
	}
	
	public void addTransactions(int num){
		transactionCount += num;
	}
	
	public int getTransactionCount(){
		return transactionCount;
	}
	
	public void setTransactionCount(int count) { 
		transactionCount = count; 
	}
	
	public String getKey(){
		return key;
	}
}
