package org.thb.modulkatalogcontroller;

import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.KatalogDTO;

public interface IUploadService
{
	
	public boolean uploadCatalog(String profileID, Katalog katalog);
	
	public boolean deleteCatalog(KatalogDTO katalogDTO);

}
