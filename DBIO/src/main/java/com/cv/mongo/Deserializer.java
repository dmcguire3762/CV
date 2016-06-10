package com.cv.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Deserializer<T> extends Thread{
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private final String jsonString;
	private T object;
	private final Class<T> typeClass;
	
	public T getObject(){
		return object;
	}
	
	public Deserializer(String jsonString, Class<T> typeClass){
		this.jsonString = jsonString;
		this.typeClass = typeClass;
	}
	
	@Override
	public void run(){
		try {
			object = jsonMapper.readValue(jsonString, typeClass);
		} catch (Exception e){
			object = null;
		}
	}
}
