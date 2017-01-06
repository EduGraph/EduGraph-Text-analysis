package org.thb.modulkatalog.junit;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.thb.modulkatalogcontroller.model.Modul;
import org.thb.modulkatalogcontroller.profileTHB.CalculationServiceImpl;

public class CalculationTestUT
{
	
	private static Modul modul;
	private static CalculationServiceImpl calcService;
	
	/**
	 * Before calculating the normalized Score the scores from comparing the vectors are needed.
	 */
	@BeforeClass
	public static void initializeModul(){
		modul = new Modul();
		calcService = new CalculationServiceImpl();
		modul.setModulName("TestModul");
		modul.setUniversityName("TH Brandenburg");
		
		modul.setBwlScore(10.0);
		modul.setInfScore(3.5);
		modul.setWiScore(18.5);
		modul.setNnScrore(20.0);
	}
	
	@Test
	public void testCalculationBWLNormalizedScores(){
		System.out.println("RUNNING CalculationServiceTest ........ for BWL");
		calcService.calculateNormalizedScore(modul);
		/**
		 * sum = 10 + 3.5 + 18.5 + 20
		 * sum = 52.0
		 * bwlnormalized = 10.0 / 52.0 = 0,1923
		 */
		System.out.println("Expected: "+(10/52) + " - Receive: "+modul.getBwlScoreNormalized());
		assertEquals(modul.getBwlScoreNormalized(),Double.valueOf((10.0/52.0)));
		System.out.println("-------------------------------------------------------------------");
		System.out.println();
	}
	
	@Test
	public void testCalculationINFNormalizedScores(){
		System.out.println("RUNNING CalculationServiceTest ........ for INF");
		calcService.calculateNormalizedScore(modul);
		/**
		 * sum = 10 + 3.5 + 18.5 + 20
		 * sum = 52.0
		 * infnormalized = 3.5 / 52.0 = 0,067
		 */
		System.out.println("Expected: "+(3.5/52) + " - Receive: "+modul.getInfScoreNormalized());
		assertEquals(modul.getInfScoreNormalized(),Double.valueOf((3.5/52.0)));
		System.out.println("-------------------------------------------------------------------");
		System.out.println();
	}

}
