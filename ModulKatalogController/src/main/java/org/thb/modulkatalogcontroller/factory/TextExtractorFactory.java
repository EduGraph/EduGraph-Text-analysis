package org.thb.modulkatalogcontroller.factory;

import org.thb.modulkatalogcontroller.model.IKatalogTextExtract;

public class TextExtractorFactory
{
	
	public static IKatalogTextExtract getTextExtractor(String profileID)
	{
		IKatalogTextExtract iKatalogTextExtract = new org.thb.modulkatalogcontroller.LocalTextExtraction();

		return iKatalogTextExtract;
	}

}
