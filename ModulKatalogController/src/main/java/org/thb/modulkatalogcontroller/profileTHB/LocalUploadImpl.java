package org.thb.modulkatalogcontroller.profileTHB;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.thb.modulkatalogcontroller.IUploadService;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.KatalogDTO;

public class LocalUploadImpl implements IUploadService
{

	@Override
	public boolean uploadCatalog(String profileID, Katalog katalog)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCatalog(KatalogDTO katalogDTO)
	{
		try{
			
			Files.delete(Paths.get(katalogDTO.getKatalogFile()));
			
			return true;
		}catch(Exception ex){
			System.err.println("Could not delete Katalogfile..."+ex.getMessage());
			return false;
		}
	}

}
