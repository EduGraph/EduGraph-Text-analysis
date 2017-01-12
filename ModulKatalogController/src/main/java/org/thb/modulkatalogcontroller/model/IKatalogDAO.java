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
	
	/**
	 * TODO: Need to change the response. it is needed a transfer object which has the structure of the database document 
	 * @param id
	 * @return
	 */
	public KatalogDTO getKatalogById(String id);
	
	public String updateKatalog(KatalogDTO katalog);
	
	public String deleteKatalog(String katalogId);
	
	public String addKatalog(KatalogDTO katalog);

	public void deleteTopTwoECTSModule(Katalog katalog);

}
