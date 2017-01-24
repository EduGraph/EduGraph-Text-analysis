package org.thb.modulkatalogcontroller.profileAWS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.DaoReturn;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.KatalogDTO;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The aws profile uses a DynamoDB as persistence layer. DynamoDB is similar to MongoDB. It is a document oriented DB.
 * @author ManuelRaddatz
 *
 */
public class KatalogDAODynamoDBImpl implements IKatalogDAO
{
	private File s3CredentialsFile;
	private AmazonDynamoDBClient amazonDynamoDBClient;
	private DynamoDB dynamoDB;
	private Region region;
	private Table table;
	
	public KatalogDAODynamoDBImpl(String databaseServer, Integer databasePort, String databaseName,
			String databaseCollectionName)
	{
		init();
		table = dynamoDB.getTable(databaseCollectionName);
	}
	
	private void init(){
		
		try
		{
			s3CredentialsFile = new File(ApplicationProperties.getInstance()
					.getApplicationProperty(ApplicationPropertiesKeys.AWSCREDENTIALSPROPERTIESFILE));
			PropertiesCredentials pc = new PropertiesCredentials(s3CredentialsFile);
			amazonDynamoDBClient = new AmazonDynamoDBClient(pc);
			region = Region.getRegion(Regions.fromName(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.AWSREGION)));
			amazonDynamoDBClient.setRegion(region);
			dynamoDB = new DynamoDB(amazonDynamoDBClient);
		} catch (FileNotFoundException e)
		{
			System.err.println("Error (FileNotFound) while Reading PropertiesFile for AWS Credentials: "+e.getMessage());
		} catch (IllegalArgumentException e)
		{
			System.err.println("Error (IllegalArgument) while Reading PropertiesFile for AWS Credentials: "+e.getMessage());
		} catch (IOException e)
		{
			System.err.println("Error (IOException) while Reading PropertiesFile for AWS Credentials: "+e.getMessage());
		}
	}
	
	@Override
	public IKatalogDAO instance(String databaseServer, Integer databasePort, String databaseName,
			String databaseCollectionName)
	{
		return new KatalogDAODynamoDBImpl(databaseServer, databasePort, databaseName, databaseCollectionName);
	}

	@Override
	public List<Object> getAllKatalogs()
	{
		return null;
	}

	@Override
	public KatalogDTO getKatalogById(String id)
	{
        KatalogDTO dto = null;
		try {

           Item item = table.getItem(new PrimaryKey().addComponent("id", id));
           ObjectMapper mapper = new ObjectMapper();
           dto = mapper.readValue(item.toJSON(), KatalogDTO.class);
        } catch (Exception e) {
            System.err.println("Error while querying DynamoDB: "+e.getMessage());
        }
		return dto;
	}

	@Override
	public String addKatalog(KatalogDTO katalog)
	{
		
		Item item = new Item();
		Item queryItem = table.getItem(new PrimaryKey().addComponent("id", katalog.getId()));
		
		if(queryItem !=null){
			return DaoReturn.KATALOGinDATABASE;
		}
			
		katalog.setKatalogFile(katalog.getKatalogFile());
		@SuppressWarnings("static-access")
		Item databaseObject = item.fromJSON(katalog.toString()).withPrimaryKey("id", katalog.getId());		
		
		table.putItem(databaseObject);
		
		return DaoReturn.OK;
	}

	@Override
	public void deleteTopTwoECTSModule(Katalog katalog)
	{

	}

	@Override
	public String updateKatalog(KatalogDTO katalog)
	{
		return null;
	}



	@Override
	public String deleteKatalog(String katalogId)
	{
		try{
			DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(new PrimaryKey("id", katalogId));
			DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
			if(outcome.getDeleteItemResult().getSdkHttpMetadata().getHttpStatusCode()==200){
				return DaoReturn.OK;
			}else{
				return DaoReturn.ERROR;
			}	
		}catch(Exception ex){
			System.err.println("Error while deleting the Katalog by given id: "+ex.getMessage());
			return DaoReturn.ERROR;
		}
	}

}
