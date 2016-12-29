package org.thb.modulkatalogcontroller.profileTHB;

import org.thb.modulkatalogcontroller.ICalculationService;
import org.thb.modulkatalogcontroller.model.Modul;

public class CalculationServiceImpl implements ICalculationService
{

	
	@Override
	public void calculateNormalizedScore(Modul modul)
	{
		double sum = 0;
		
		sum = modul.getBwlScore()+ modul.getInfScore()+modul.getNnScrore()+modul.getWiScore();
		
		if(modul.getBwlScore()!=0){
			modul.setBwlScoreNormalized((modul.getBwlScore()/sum));
		}else{
			modul.setBwlScoreNormalized(0.0);
		}
		
		if(modul.getInfScore()!=0){
			modul.setInfScoreNormalized((modul.getInfScore()/sum));
		}else{
			modul.setInfScoreNormalized(0.0);
		}
		
		if(modul.getWiScore()!=0){
			modul.setWiScoreNormalized((modul.getWiScore()/sum));
		}else{
			modul.setWiScoreNormalized(0.0);
		}
		
		if(modul.getNnScrore()!=0){
			modul.setNnScoreNormalized((modul.getNnScrore()/sum));
		}else{
			modul.setNnScoreNormalized(0.0);
		}

	}

}
