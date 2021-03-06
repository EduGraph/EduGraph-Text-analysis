package org.thb.modulkatalogcontroller.factory;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.thb.modulkatalogcontroller.ICalculationService;

/**
 * The CalculationFactory returns the specific service depends on the profileid.
 * @author ManuelRaddatz
 *
 */
public class CalculationFactory
{
	/**
	 * The Factory Method is giving back the right Implementation to the Profile-ID
	 * @param profileID
	 * @return
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static ICalculationService getCalculationService(String profileID) throws FileNotFoundException, IllegalArgumentException, IOException
	{
		ICalculationService iCalc =	null;
		if(profileID.equalsIgnoreCase("aws")){
			iCalc = new org.thb.modulkatalogcontroller.profileAWS.LambdaCalculationServiceImpl();
		}else if(profileID.equalsIgnoreCase("dev")){
			iCalc = new org.thb.modulkatalogcontroller.profileTHB.CalculationServiceImpl();
		}else if(profileID.equalsIgnoreCase("thb")){
			iCalc = new org.thb.modulkatalogcontroller.profileTHB.CalculationServiceImpl();
		}
		return iCalc;
	}

}
