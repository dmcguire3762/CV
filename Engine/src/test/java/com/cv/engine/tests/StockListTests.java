package com.cv.engine.tests;

import java.io.File;
import java.io.IOException;

import org.junit.*;

import com.cv.engine.Stock;
import com.cv.engine.StockList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockListTests {
	private static final String companyListFile = "src/main/resources/companyList.json";
	private static ObjectMapper jsonMapper = new ObjectMapper();
	
	@Test
	public void testReadCompanyList() throws JsonParseException, JsonMappingException, IOException{
		StockList stocks = jsonMapper.readValue(new File(companyListFile), StockList.class);
		for(Stock stock : stocks){
			System.out.println(stock.getCompanyName() + "/");
		}
	}
}
