package org.thb.modulkatalogcontroller.factory;

import org.thb.modulkatalogcontroller.model.IKatalogDAO;


public class DatabaseConnectionFactory
{
	
	public static IKatalogDAO getDatabaseConnection(String dbType, String databaseServer, Integer port, String databaseName, String databaseCollectionName)
	{
		IKatalogDAO iKatalogDAO = null;
		if(dbType.equalsIgnoreCase("mongodb")){
			iKatalogDAO = new org.thb.modulkatalogcontroller.profileTHB.KatalogDAOMongoDBImpl(databaseServer, port, databaseName, databaseCollectionName);
		}else if(dbType.equalsIgnoreCase("dynamodb")){
			iKatalogDAO = new org.thb.modulkatalogcontroller.profileAWS.KatalogDAODynamoDBImpl(databaseServer, port, databaseName, databaseCollectionName);
		}
		return iKatalogDAO;
	}
}
