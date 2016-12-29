
package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Modulclass represents a Modul of the provided Modulcatalog. The Modultext later is send to Solr for getting the index of the Text.
 * Finally the analyzed Data is saved to a Database.
 * @author Manuel Raddatz
 *
 */
public class Modul implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7466094764931257381L;
	private String text;
	private String cleanText;
	private String modulName;
	private String universityName;
	
	private Vector  documentVector;
	
	private Double infScore;
	private Double bwlScore;
	private Double wiScore;
	private Double nnScrore;
	
	private Double infScoreNormalized;
	private Double bwlScoreNormalized;
	private Double wiScoreNormalized;
	private Double nnScoreNormalized;
	
	private Integer ects;
	
	private List<ModulPart> modulParts; //--> für vielleicht Parts

	//private List<ControlFormItems> formItems;
	
	/**
	 * Constructor of the Modul class. 
	 */
	public Modul(){
		modulParts = new ArrayList<>();
	}

	/**
	 * Getter for the clean Text of the Moduldescription. The Cleantext contains no numbers and special signs like - + * or other
	 * @return
	 */
	public String getCleanText()
	{
		return cleanText;
	}

	/**
	 * Method for removing all non-alphanumeric from the Modultext. 
	 * @param text
	 */
	public void setCleanText(String text)
	{
		this.cleanText = text.replaceAll("[^A-Za-zäöü]", " ");		
	}
	
	/**
	 * Getter for given ControlItems of the Modul.
	 * @return List<ControlFormItems> formItems
	 
	public List<ControlFormItems> getFormItems()
	{
		return formItems;
	}
*/
	/**
	 * Setter for changing and setting the ControlItems for the Modul.
	 * @param formItems List<ControlFormItemls>
	
	public void setFormItems(List<ControlFormItems> formItems)
	{
		this.formItems = formItems;
	}
 */
	/**
	 * Getter for the hole Modultext.
	 * @return String text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Setter for changing and setting the Modultext.
	 * @param text String
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * Getter for return the different Parts of the Modul.
	 * @return List<ModulPart> modulParts
	 */
	public List<ModulPart> getModulParts()
	{
		return modulParts;
	}

	/**
	 * Setter for changing and setting the ModulParts of the Modul.
	 * @param List<ModulParts> modulParts
	 */
	public void setModulParts(List<ModulPart> modulParts)
	{
		this.modulParts = modulParts;
	}

	/**
	 * Getter for returning the Name of the Modul.
	 * @return modulName String
	 */
	public String getModulName()
	{
		return modulName;
	}

	/**
	 * Setter for changing and setting the Modulname of the Modul. The Modulname is Part of the uniqueKey in Solr.
	 * @param modulName String
	 */
	public void setModulName(String modulName)
	{
		this.modulName = modulName;
	}

	/**
	 * Getter for returning the Universityname of the Modul.
	 * @return universityName String
	 */
	public String getUniversityName()
	{
		return universityName;
	}

	/**
	 * Setter for changing and setting the Universityname of the Modul. The Universityname is Part of the uniqueKey in Solr.
	 * @param universityName String
	 */
	public void setUniversityName(String universityName)
	{
		this.universityName = universityName;
	}

	/**
	 * Getter for returning the Vector of the Modul.
	 * @return documentVector Vector
	 */
	public Vector getDocumentVector()
	{
		return documentVector;
	}

	/**
	 * Setter for changing and setting the Vector of the Modul.
	 * @param documentVector Vector
	 */
	public void setDocumentVector(Vector documentVector)
	{
		this.documentVector = documentVector;
	}

	public Double getInfScore()
	{
		return infScore;
	}

	public void setInfScore(Double infScore)
	{
		this.infScore = infScore;
	}

	public Double getBwlScore()
	{
		return bwlScore;
	}

	public void setBwlScore(Double bwlScore)
	{
		this.bwlScore = bwlScore;
	}

	public Double getWiScore()
	{
		return wiScore;
	}

	public void setWiScore(Double wiScore)
	{
		this.wiScore = wiScore;
	}

	public Double getNnScrore()
	{
		return nnScrore;
	}

	public void setNnScrore(Double nnScrore)
	{
		this.nnScrore = nnScrore;
	}

	/**
	 * @return the infScoreNormalized
	 */
	public Double getInfScoreNormalized()
	{
		return infScoreNormalized;
	}

	/**
	 * @param infScoreNormalized the infScoreNormalized to set
	 */
	public void setInfScoreNormalized(Double infScoreNormalized)
	{
		this.infScoreNormalized = infScoreNormalized;
	}

	/**
	 * @return the bwlScoreNormalized
	 */
	public Double getBwlScoreNormalized()
	{
		return bwlScoreNormalized;
	}

	/**
	 * @param bwlScoreNormalized the bwlScoreNormalized to set
	 */
	public void setBwlScoreNormalized(Double bwlScoreNormalized)
	{
		this.bwlScoreNormalized = bwlScoreNormalized;
	}

	/**
	 * @return the wiScoreNormalized
	 */
	public Double getWiScoreNormalized()
	{
		return wiScoreNormalized;
	}

	/**
	 * @param wiScoreNormalized the wiScoreNormalized to set
	 */
	public void setWiScoreNormalized(Double wiScoreNormalized)
	{
		this.wiScoreNormalized = wiScoreNormalized;
	}

	/**
	 * @return the nnScoreNormalized
	 */
	public Double getNnScoreNormalized()
	{
		return nnScoreNormalized;
	}

	/**
	 * @param nnScoreNormalized the nnScoreNormalized to set
	 */
	public void setNnScoreNormalized(Double nnScoreNormalized)
	{
		this.nnScoreNormalized = nnScoreNormalized;
	}

	/**
	 * @return the ects
	 */
	public Integer getEcts()
	{		
		for(ModulPart part: modulParts){
			if(part.getPart().equals(FormFieldNames.INDIKATOR_ECTS)){
				ects = Integer.parseInt(part.getPart());
				System.out.println("ECTS in Prts: "+ects);
			}
		}
		return ects;
	}

	/**
	 * @param ects the ects to set
	 */
	public void setEcts(Integer ects)
	{
		this.ects = ects;
	}
	
	
	
}
