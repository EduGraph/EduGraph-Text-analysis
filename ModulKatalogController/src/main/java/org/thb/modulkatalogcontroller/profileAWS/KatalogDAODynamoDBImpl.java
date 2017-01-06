package org.thb.modulkatalogcontroller.profileAWS;

import java.util.List;

import org.thb.modulkatalogcontroller.DaoReturn;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;

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
	public Object getKatalogByName(String katalogName)
	{
		return null;
	}

	@Override
	public String updateKatalog(Katalog katalog)
	{
		return DaoReturn.OK;
	}

	@Override
	public String deleteKatalog(Katalog katalog)
	{
		return DaoReturn.OK;
	}

	@Override
	public String addKatalog(Katalog katalog)
	{
		return DaoReturn.OK;
	}



	@Override
	public String getAllNormalizedScores(String pillar)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
