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
	
	public void updateKatalog(Katalog katalog);
	
	public void deleteKatalog(Katalog katalog);
	
	public void addKatalog(Katalog katalog);

}
