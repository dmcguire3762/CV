package com.cv.mongo;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class CVDB{
	private final String dbUrl = "localhost";
	private final int dbPort = 27017;
	private final String dbName = "crowdvest";
	private final String collectionName;
	
	private List<Document> insertObjects = new ArrayList<Document>();
	private List<Document> updateObjects = new ArrayList<Document>();
	private List<Document> deleteObjects = new ArrayList<Document>();
	
	public enum Operation{
		insert,
		update,
		delete
	}
	
	public CVDB(String collectionName){
		this.collectionName = collectionName;
	}
	
	public void addToBatch(List<String> jsonObjects, Operation op){
		for(String jsonObject : jsonObjects){
			addToBatch(jsonObject, op);
		}	
	}
	
	public void addToBatch(String jsonObject, Operation op){
		Document doc = Document.parse(jsonObject);
		switch(op){
			case insert:
				insertObjects.add(doc);
				break;
			case update:
				updateObjects.add(doc);
				break;
			case delete:
				deleteObjects.add(doc);
				break;
		}
	}
	
	public void executeBatch(Operation op){
		try(MongoClient mongo = new MongoClient(dbUrl, dbPort)){
			MongoDatabase db = mongo.getDatabase(dbName);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			
			switch(op){
				case insert:
					collection.insertMany(insertObjects);
					insertObjects.clear();
					break;
				default:
					break;
			}
		}
	}
	
	public List<String> findObjects(){
		try(MongoClient mongo = new MongoClient(dbUrl, dbPort)){
			MongoDatabase db = mongo.getDatabase(dbName);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			
			List<String> objects = new ArrayList<String>();
			try(MongoCursor<Document> cursor = collection.find().iterator()){
				while(cursor.hasNext()){
					objects.add(cursor.next().toJson());
				}
			}

			return objects;
		}
	}
}
