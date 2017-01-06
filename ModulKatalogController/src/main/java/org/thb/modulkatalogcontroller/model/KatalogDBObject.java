package org.thb.modulkatalogcontroller.model;

import java.util.Map;

/**
 * TODO: Not all of the Katalog Object and Modul Object is saved. The Object should contains the necessary Data.
 * @author Manuel
 *
 */
public class KatalogDBObject
{
	
	private String hochschulname;
	
	private String savedKatalogFile;
	
	private Integer ects;
	
	private Map<String, Map<String, Double>> scores;
	
	private Map<String, Map<String, Double>> normalizedScores;

}
