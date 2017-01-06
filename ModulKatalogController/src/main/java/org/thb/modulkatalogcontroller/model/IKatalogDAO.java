package org.thb.modulkatalogcontroller.model;

import java.util.List;

/**
 * DAO Interface of DAO Pattern. Each Katalog Object is saved as Document in MongoDB.
 * @author Manuel Raddatz
 *
 */
public interface IKatalogDAO
{
	public IKatalogDAO instance(String databaseServer, Integer databasePort, String databaseName, String databaseCollectionName);
	
	public List<Object> getAllKatalogs();
	
	public Object getKatalogByName(String katalogName);
	
	public String updateKatalog(Katalog katalog);
	
	public String deleteKatalog(Katalog katalog);
	
	public String addKatalog(Katalog katalog);
	
	public String getAllNormalizedScores(String pillar);

}
