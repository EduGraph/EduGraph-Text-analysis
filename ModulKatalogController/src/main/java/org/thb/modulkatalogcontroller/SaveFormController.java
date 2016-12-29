package org.thb.modulkatalogcontroller;

import java.io.Serializable;

import javax.inject.Named;

import org.thb.modulkatalogcontroller.factory.DatabaseConnectionFactory;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;


/**
 * This Controller is responsible saving the modulcatalog in the given database.
 * @author Manuel Raddatz
 *
 */
@Named
@javax.enterprise.context.SessionScoped
public class SaveFormController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public String save(Katalog katalog){
		
		IKatalogDAO instance = DatabaseConnectionFactory.getDatabaseConnection(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEPROVIDER),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTSTRING),
				Integer.parseInt(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTPORTSTRING)),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASENAMESTRING), 
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEKATALOGCOLLECTIONSTRING));
		
		instance.addKatalog(katalog);
		return Pages.INDEX;
	}
	
	public String cancel(){
		return Pages.INDEX;
	}
}
