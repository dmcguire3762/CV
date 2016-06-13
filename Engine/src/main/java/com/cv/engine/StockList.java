package com.cv.engine;

import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StockList extends ArrayList<Stock>{
	private static final long serialVersionUID = 8691653317530583426L;
	
	public StockList() throws JsonParseException, JsonMappingException, IOException{
	}
}
