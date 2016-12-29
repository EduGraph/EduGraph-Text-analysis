package org.thb.modulkatalogcontroller.profileAWS;

import java.util.List;

import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;

public class KatalogDAODynamoDBImpl implements IKatalogDAO
{

	
	public KatalogDAODynamoDBImpl(String databaseServer, Integer databasePort, String databaseName,
			String databaseCollectionName)
	{
		System.out.println("DynamoDB constructor: "+databaseServer);
	}
	
	
	
	@Override
	public IKatalogDAO instance(String databaseServer, Integer databasePort, String databaseName,
			String databaseCollectionName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getAllKatalogs()
	{
		// TODO Auto-generated method stub
		return null;
	}

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

	@Override
	public void deleteKatalog(Katalog katalog)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addKatalog(Katalog katalog)
	{
		// TODO Auto-generated method stub
		
	}

}
