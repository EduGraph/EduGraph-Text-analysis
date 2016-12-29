package org.thb.modulkatalogcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The ApplicationProperties provide specific Properties for the whole application. Based on the 
 * environment, the properties are changed. The class is loaded once. 
 * @author Manuel Raddatz
 *
 */
public class ApplicationProperties
{
	
	private Map<String, String> properties = new HashMap<>();
	private static ApplicationProperties instance; 
	
	/**
	 * Private Constructor for Singleton Pattern. 
	 */
	private ApplicationProperties(){
		init();
	}
	
	/**
	 * Singleton Pattern. This method is returning the instance of the properties object.
	 * @return the instance
	 */
	public static ApplicationProperties getInstance()
	{
		if(instance!=null){
			return instance;
		}else{
			instance = new ApplicationProperties();
			return instance;
		}
	}

	/**
	 * The init method loads the specific properties from inputstream.
	 */
	private void init()
	{
		Properties propertie = new Properties();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		InputStream input = classLoader.getResourceAsStream("application.properties");
		
		try
		{
			propertie.load(input);
			properties.put(ApplicationPropertiesKeys.PROFILEID, propertie.getProperty("system.environment"));
			properties.put(ApplicationPropertiesKeys.SOLRURLSTRING, propertie.getProperty("solr.url"));
			properties.put(ApplicationPropertiesKeys.SOLRURLKEYCLASSString, propertie.getProperty("solr.keyClass.url"));
			properties.put(ApplicationPropertiesKeys.UPLOADDIRECTORY, propertie.getProperty("upload.directory"));
			properties.put(ApplicationPropertiesKeys.DATABASEPROVIDER, propertie.getProperty("database.provider"));
			properties.put(ApplicationPropertiesKeys.DATABASECLIENTSTRING, propertie.getProperty("database.server"));
			properties.put(ApplicationPropertiesKeys.DATABASECLIENTPORTSTRING, propertie.getProperty("database.port"));
			properties.put(ApplicationPropertiesKeys.DATABASENAMESTRING, propertie.getProperty("database.name"));
			properties.put(ApplicationPropertiesKeys.DATABASEKATALOGCOLLECTIONSTRING, propertie.getProperty("database.collection"));
			properties.put(ApplicationPropertiesKeys.AWSCALCULATIONLAMBDAFUNCTION, propertie.getProperty("aws.lambda.calculation.function"));
			properties.put(ApplicationPropertiesKeys.AWSCREDENTIALSPROPERTIESFILE, propertie.getProperty("aws.credentials.properties.file"));
			properties.put(ApplicationPropertiesKeys.AWSREGION, propertie.getProperty("aws.region"));
			properties.put(ApplicationPropertiesKeys.S3BUCKET, propertie.getProperty("aws.s3bucket"));
			
		} catch (IOException e)
		{
			System.err.println("Error Loading the ApplicationProperties");
		}finally {
			try
			{
				input.close();
			} catch (IOException e)
			{
				System.err.println("Error closing the Inputstream while loading the ApplicationProperties");
			}
		}		
	}
		
	/**
	 * This method is returning all specific application properties of the ModulKatalogController which
	 * where specified in the application.properties file.
	 * @return Map
	 */
	public Map<String, String> getProperties()
	{
		return properties;
	}

	/**
	 * This method is return the specific value of one property for the provided key.
	 * @param key String 
	 * @return value String
	 */
	public String getApplicationProperty(String key)
	{
		return properties.get(key);
	}	
}
