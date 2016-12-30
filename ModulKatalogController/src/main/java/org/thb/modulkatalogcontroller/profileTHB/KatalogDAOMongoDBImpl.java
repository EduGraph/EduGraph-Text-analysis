package org.thb.modulkatalogcontroller.profileTHB;

import org.thb.modulkatalogcontroller.model.ControlFormItems;
import org.thb.modulkatalogcontroller.model.FormFieldNames;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.Modul;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.*;

public class KatalogDAOMongoDBImpl implements IKatalogDAO
{
		private MongoClient dbClient;
		private MongoDatabase db;
		private MongoCollection<Document> dbCollection;
	
		/**
		 * Public Constructor with the given Information for initializing the MongoClient
		 * @param databaseServer
		 * @param databasePort
		 * @param databaseName
		 * @param databaseCollectionName
		 */
		public KatalogDAOMongoDBImpl(String databaseServer, Integer databasePort, String databaseName,
				String databaseCollectionName){
			
			this.dbClient = new MongoClient(databaseServer, databasePort);
			this.db = dbClient.getDatabase(databaseName);
			this.dbCollection = db.getCollection(databaseCollectionName);
		}
		
		/**
		 * The instance method returning an Instance of the object 
		 */
		@Override
		public IKatalogDAO instance(String databaseServer, Integer databasePort, String databaseName,
				String databaseCollectionName) {
			
			return new KatalogDAOMongoDBImpl(databaseServer, databasePort, databaseName, databaseCollectionName);
		}

		@Override
		public List<Object> getAllKatalogs()
		{
			List<Object> katalogs = new ArrayList<>();
			 FindIterable<Document> cursor = dbCollection.find();
			    for (Document document : cursor)
				{				
			    	katalogs.add(document.getString("hochschulname"));
				}
			
			return katalogs;
		}

		/**
		 * Returning the saved Katalog as Object. Actually it is saved as JSON.
		 */
		@Override
		public Object getKatalogByName(String katalogName)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void updateKatalog(Katalog katalog)
		{
			// TODO Auto-generated method stub
			
		}

		/**
		 * Delete the Katalog with the corresponding document from filesystem.
		 */
		@Override
		public void deleteKatalog(Katalog katalog)
		{
			// TODO Auto-generated method stub
			
		}

		/**
		 * Adding the given Katalog to the Database. Not all Information are saved.
		 */
		@Override
		public void addKatalog(Katalog katalog)
		{
			
			Document basicObject = new Document();
			
			for (ControlFormItems item : katalog.getControlItems()) {
				if (item.getFieldname().equals(FormFieldNames.HOCHSCHULNAME)) {
					basicObject.put("id", item.getFieldValue());
					basicObject.put("hochschulname", item.getFieldValue());
					basicObject.put("date", Calendar.getInstance().getTime().toString());
				}
			}
					
			ArrayList<BasicDBObject> moduls = new ArrayList<>();
			for (Modul m : katalog.getModuls()) {
				BasicDBObject detailObject = new BasicDBObject();
				detailObject.append("id", m.getModulName());
				detailObject.append("ects", m.getEcts());
				detailObject.append("bwlScore", m.getBwlScore());
				detailObject.append("infScore", m.getInfScore());
				detailObject.append("wiScore", m.getWiScore());
				detailObject.append("bwlScoreNormalized", m.getBwlScoreNormalized());
				detailObject.append("infScoreNormalized", m.getInfScoreNormalized());
				detailObject.append("wiScoreNormalized", m.getWiScoreNormalized());
				
				ArrayList<BasicDBObject>vector = new ArrayList<>();
				for (org.thb.modulkatalogcontroller.model.Term t : m.getDocumentVector().getTerms()) {
					BasicDBObject vectorObject = new BasicDBObject();
					vectorObject.append(t.getWord(), t.getTermFrequency());
					vector.add(vectorObject);
				}
				detailObject.append("vector", vector);
				moduls.add(detailObject);	
			}
			basicObject.append("moduls", moduls);

			dbCollection.insertOne(basicObject);
		}
	}
	


