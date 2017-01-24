package org.thb.modulkatalogcontroller;

import org.thb.modulkatalogcontroller.model.Katalog;

public interface IUploadService
{
	
	public boolean uploadCatalog(String profileID, Katalog katalog);

}
