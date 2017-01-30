package org.thb.modulkatalogcontroller.profileTHB;

import org.thb.modulkatalogcontroller.DaoReturn;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.KatalogDTO;
import org.thb.modulkatalogcontroller.model.Modul;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
			    	System.out.println("Document from DB: "+document.getString("_id"));
			    	katalogs.add(document.getString("_id"));
				}
			
			return katalogs;
		}

		/**
		 * Returning the saved Katalog as Object. Actually it is saved as JSON.
		 */
		@Override
		public KatalogDTO getKatalogById(String id)
		{
			Document bson = new Document();
			bson.append("message", "Katalog not found");
			
			FindIterable<Document> cursor = dbCollection.find();
			ObjectMapper mapper = new ObjectMapper();
			KatalogDTO dto = new KatalogDTO();
		    for (Document document : cursor)
			{				
		    	if(document.getString("_id").equalsIgnoreCase(id)){
		    		try
					{
		    			dto = mapper.readValue(document.toJson(), KatalogDTO.class);
						return dto;
					} catch (IOException e)
					{
						System.err.println("Error while mapping BsonDocument to KatalogDTO: "+e.getMessage());
					}
		    	}
			}	    
		    KatalogDTO errorKatalog = new KatalogDTO();
			try
			{
				errorKatalog = mapper.readValue(bson.toJson(), KatalogDTO.class);
			} catch (IOException e)
			{
				System.err.println("IOException: "+e.getMessage());
			}
		    return errorKatalog;
		}

		@Override
		public String updateKatalog(KatalogDTO katalog)
		{
			return DaoReturn.OK;
			
		}

		/**
		 * Delete the Katalog with the corresponding document from filesystem.
		 */
		@Override
		public String deleteKatalog(String katalogId)
		{	
			KatalogDTO dto = getKatalogById(katalogId);
			String katalogFile = dto.getKatalogFile();
			if(katalogFile!=null){
				try{
					File f = new File(katalogFile);
					f.delete();
				}catch(Exception ex){
					System.err.println("Error while deleting KatalogFile."+ ex.getMessage());
				}
			}
			
			Document query = new Document();
			query.append("_id", katalogId);

			DeleteResult result = dbCollection.deleteOne(query);
			long numResult = result.getDeletedCount();
			if(numResult==1){
				return DaoReturn.OK;
			}
			return DaoReturn.ERROR;
		}

		@Override
		public void deleteTopTwoECTSModule(Katalog katalog)
		{
			List<Modul> moduls = katalog.getModuls();
			Collections.sort(moduls);
			
			if(moduls.size()>1){
				moduls.remove(0);
				moduls.remove(1);
			}
		}

		@SuppressWarnings("static-access")
		@Override
		public String addKatalog(KatalogDTO katalog)
		{
			
			Document basicObject = new Document();

			FindIterable<Document> cursor = dbCollection.find();

			if(cursor != null){
				for(Document d :cursor){
					if(d!=null | !d.isEmpty()){
						if(d.getString("_id")!=null | !d.getString("_id").isEmpty()){
							if(d.getString("_id").equalsIgnoreCase(katalog.getId())){
								System.out.println("University allready in DB");
								return DaoReturn.KATALOGinDATABASE;
							}
						}
					}
				}
			}
			Document doc = null;
			if(katalog!=null){
				doc = basicObject.parse(katalog.toString());
				doc.put("_id", katalog.getId());
			}else{
				return DaoReturn.ERROR;
			}

			dbCollection.insertOne(doc);

			return DaoReturn.OK;
		}
	}
	


