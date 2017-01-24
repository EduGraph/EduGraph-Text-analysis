package org.thb.modulkatalogcontroller.factory;

import org.thb.modulkatalogcontroller.IUploadService;
import org.thb.modulkatalogcontroller.model.Katalog;

public class UploadFactory
{

	public static IUploadService getUploadService(String profileID, Katalog katalog)
	{
		IUploadService iUploadService = null;
		if(profileID.equalsIgnoreCase("aws")){
			iUploadService = new org.thb.modulkatalogcontroller.profileAWS.AmazonS3UploadImpl();
		}else if(profileID.equalsIgnoreCase("dev")){
			iUploadService = new org.thb.modulkatalogcontroller.profileTHB.LocalUploadImpl();
		}
		return iUploadService;
	}
	
}
