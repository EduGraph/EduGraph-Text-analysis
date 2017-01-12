package org.thb.modulkatalogcontroller.profileAWS;

import java.util.List;

import org.thb.modulkatalogcontroller.DaoReturn;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.KatalogDTO;

/**
 * The aws profile uses a DynamoDB as persistence layer. DynamoDB is similar to MongoDB. It ist a document oriented DB.
 * @author ManuelRaddatz
 *
 */
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
		return null;
	}

	@Override
	public List<Object> getAllKatalogs()
	{
		return null;
	}

	@Override
	public KatalogDTO getKatalogById(String id)
	{
		return null;
	}

	@Override
	public String addKatalog(KatalogDTO katalog)
	{
		return DaoReturn.OK;
	}

	@Override
	public void deleteTopTwoECTSModule(Katalog katalog)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String updateKatalog(KatalogDTO katalog)
	{
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String deleteKatalog(String katalogId)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
