package org.thb.modulkatalogcontroller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrServerException;
import org.thb.modulkatalogcontroller.factory.CalculationFactory;
import org.thb.modulkatalogcontroller.factory.TextExtractorFactory;
import org.thb.modulkatalogcontroller.model.ControlFormItems;
import org.thb.modulkatalogcontroller.model.FormFieldNames;
import org.thb.modulkatalogcontroller.model.IKatalogTextExtract;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.Modul;
import org.thb.modulkatalogcontroller.model.ModulPart;
import org.thb.modulkatalogcontroller.model.Term;
import org.thb.modulkatalogcontroller.model.Vector;
import org.thb.modulkatalogcontroller.solr.SolrConnection;

/**
 * The Textextrator is the part of extracting the text and moduls from the given Catalog.  
 * @author Manuel
 *
 */
public class KatalogTextExtractor {
	
	private Katalog katalog;
	private List<Modul> moduls;
	private SolrConnection solrConnect;
	private List<Vector> keyClassVectors;
	private List<ControlFormItems> controlItems;
	private String file;

	/**
	 * Contructor for Textextraction
	 * @param katalog
	 * @param moduls
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public KatalogTextExtractor(Katalog katalog, List<Modul> moduls) throws IOException, SolrServerException {
		super();
		this.katalog = katalog;
		this.moduls=moduls;
		init();
	}
	
	/**
	 * Initializing the needed components and functions during extraction.
	 * @param katalog 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	private void init() throws IOException, SolrServerException{
		solrConnect = new SolrConnection();
		keyClassVectors=solrConnect.setUpKeyVectors();
		controlItems = katalog.getControlItems();
		file=katalog.getFile();
	}

	public Katalog getKatalog() {
		return katalog;
	}

	public void setKatalog(Katalog katalog) {
		this.katalog = katalog;
	}
	
	/**
	 * Central Method for Starting the EXTRACTION of the ModulText of the given Catalogfile. Additional the extracted text of each modul
	 * is send to solr and the vector is comming beck.
	 * TODO: IN ADVANCE IT IS TO RESOLVE IF THE CATALOG IS PDF OR DOC
	 * @throws IOException
	 * @throws SolrServerException 
	 */
	public void extractModultext() throws IOException, SolrServerException  //String inputFile //array<String>
	{
		ArrayList<Integer> modulStarts = new ArrayList<>();
		File f = new File(file);
		
		Map<String, Integer> controls = new HashMap<>();
		String universityName =null;
		String regexPatternString =null;
		
		for (ControlFormItems item : controlItems)
		{
			if(item!=null){
			
				if (item.getFieldname().equals(FormFieldNames.STARTSEITE))
				{
					controls.put(FormFieldNames.STARTSEITE, Integer.parseInt(item.getFieldValue()));
				} else if (item.getFieldname().equals(FormFieldNames.ENDSEITE)){
					//stripper.setEndPage(Integer.parseInt(item.getFieldValue()));
					controls.put(FormFieldNames.ENDSEITE, Integer.parseInt(item.getFieldValue()));
				}else if(item.getFieldname().equals(FormFieldNames.MODULERKENNUNG)){
					regexPatternString = item.getFieldValue();
				}else if(item.getFieldname().equals(FormFieldNames.HOCHSCHULNAME)){
					universityName = item.getFieldValue();
				}
			}
		}

		/**
		 * Depending on the environment, the specific textextractor is loaded.
		 */
		IKatalogTextExtract iText = TextExtractorFactory.getTextExtractor(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.PROFILEID));
		
		String pageText = iText.extractKatalogText(f, controls);
		
		String trim = pageText.replaceAll("\\r\\n|\\r|\\n", " ");
		String clearText = trim.replaceAll("\\s+", " ");
		
		Pattern pattern = Pattern.compile(regexPatternString);
		Matcher matcher = pattern.matcher(clearText);

		String modul = null;
		while (matcher.find())
		{
			modulStarts.add(matcher.start());
		}
		for (int i = 0; i < modulStarts.size(); i++)
		{
			if (i + 1 >= modulStarts.size())
			{
				modul = clearText.substring(modulStarts.get(i), clearText.length()).replaceAll(regexPatternString, "");
			} else
			{
				modul = clearText.substring(modulStarts.get(i), modulStarts.get(i + 1)).replaceAll(regexPatternString,"");
			}
			Modul m = new Modul();
			m.setUniversityName(universityName);
			m.setText(modul);
			m.setCleanText(modul);
			m.setModulName(modul.substring(0, 35).replaceAll("\\p{Punct}", ""));
			//m.setVector(vector);
			Integer ects = Integer.parseInt(getECTS(extractParts(m)));
			m.setEcts(ects);
			System.out.println("HIER STEHT DIE ZUORDNUNG: "+getZuordnung(extractParts(m)));
			moduls.add(m);
		}
		if(indexingModuls(moduls)){
			getModulIndex(moduls);
		}
		compareVectors();
	}
	
	/**
	 * Private Helper Method for calculating the scores for each pillar of the EduGraph. This scores were calculated for each Modul of
	 * the Catalog.
	 * TODO: THIS METHOD AND THE CODE HAS TO BE OPTIMIZED
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	private void compareVectors() throws FileNotFoundException, IllegalArgumentException, IOException
	{		
		for(Modul m : moduls){	
			List<Term> modulVectorTerms = m.getDocumentVector().getTerms();
			for(Vector v : keyClassVectors){
				if(v.getModulName().equals(KeyClassVector.INF)){
					Double infScore = 0.0;
					List<Term> keyVectorTerms = v.getTerms();
					for(int i=0; i<keyVectorTerms.size(); i++){
						for(int j=0; j<modulVectorTerms.size(); j++){
							if(keyVectorTerms.get(i).getWord().equals(modulVectorTerms.get(j).getWord())){
								infScore += keyVectorTerms.get(i).getInverseTermFrequency()*modulVectorTerms.get(j).getTermFrequency();
							}
						}
					}
					m.setInfScore(infScore);
				}else if(v.getModulName().equals(KeyClassVector.BWL)){
					Double bwlScore = 0.0;
					List<Term> keyVectorTerms = v.getTerms();
					for(int i=0; i<keyVectorTerms.size(); i++){
						for(int j=0; j<modulVectorTerms.size(); j++){
							if(keyVectorTerms.get(i).getWord().equals(modulVectorTerms.get(j).getWord())){
								bwlScore += keyVectorTerms.get(i).getInverseTermFrequency()*modulVectorTerms.get(j).getTermFrequency();
							}
						}
					}
					m.setBwlScore(bwlScore);
				}else if(v.getModulName().equals(KeyClassVector.WI)){
					Double wiScore = 0.0;
					List<Term> keyVectorTerms = v.getTerms();
					for(int i=0; i<keyVectorTerms.size(); i++){
						for(int j=0; j<modulVectorTerms.size(); j++){
							if(keyVectorTerms.get(i).getWord().equals(modulVectorTerms.get(j).getWord())){
								wiScore += keyVectorTerms.get(i).getInverseTermFrequency()*modulVectorTerms.get(j).getTermFrequency();
							}
						}
					}
					m.setWiScore(wiScore);
				}else{
					Double nnScore = 0.0;
					List<Term> keyVectorTerms = v.getTerms();
					for(int i=0; i<keyVectorTerms.size(); i++){
						for(int j=0; j<modulVectorTerms.size(); j++){
							if(keyVectorTerms.get(i).getWord().equals(modulVectorTerms.get(j).getWord())){
								nnScore += keyVectorTerms.get(i).getInverseTermFrequency()*modulVectorTerms.get(j).getTermFrequency();
							}
						}
					}
					m.setNnScrore(nnScore);
				}
			}
			
			ICalculationService iCalc = CalculationFactory.getCalculationService(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.PROFILEID));
	
			iCalc.calculateNormalizedScore(m);
		}	
	}

	/**
	 * Method for indexing the extracted ModulText. The extracted Moduls before were send to the SolrServer.
	 * @param moduls
	 */
	private boolean indexingModuls(List<Modul> moduls){
		boolean result = false;
		try
		{
			solrConnect.indexing(moduls);
			result = true;
		} catch (SolrServerException | IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Helper Method for setting the Vector of each Modul of the Katalog. 
	 * @param moduls
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	private void getModulIndex(List<Modul> moduls) throws SolrServerException, IOException{
		solrConnect.getIndex(moduls);
	}

	/**
	 * ExtractsParts is a Helper Method for getting the different Parts of the given Modul and Modultext. Especially 
	 * the Credit-Points are important for coming analysis.
	 * TODO: Maybe Change the ModulParts to Map, similar to ControlFormItems to build an one to one mapping for the Item to the Part
	 * @param modul Modul
	 * @return 
	 */
	private List<ModulPart> extractParts(Modul modul)
	{
		String modulText = modul.getText();
		ModulPart part = null;
		List<ModulPart> modulParts = new ArrayList<>();
		for (int j = 0; j < controlItems.size(); j++)
		{
			if (controlItems.get(j).getFieldValue() != null)
			{
				part = new ModulPart();
				/*
				if (controlItems.get(j).getFieldname().equals(FormFieldNames.INDIKATOR_INHALT))
				{
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
				} else if (controlItems.get(j).getFieldname().equals(FormFieldNames.INDIKATOR_ERGEBNIS))
				{
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
				} else if (controlItems.get(j).getFieldname().equals(FormFieldNames.INDIKATOR_LITERATUR))
				{
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
				} else if (controlItems.get(j).getFieldname().equals(FormFieldNames.INDIKATOR_ECTS))
				{
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
				}
				*/
				
				if (controlItems.get(j).getFieldname().equals(FormFieldNames.INDIKATOR_ECTS))
				{
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
					
				}else if (controlItems.get(j).getFieldname().equals(FormFieldNames.ZUORDNUNG))
					part.setStartIndex(pStart(controlItems.get(j), modulText));
					part.setEndIndex(pEnd(controlItems.get(j), modulText));
					part.setPart(controlItems.get(j).getFieldname());
					part.setValue(controlItems.get(j).getFieldValue());
			}
			modulParts.add(part);
		}
		/**
		 * Remove null values from the list of the modulParts. Actually it should be one (ECTS)
		 */
		for (int i = modulParts.size() - 1; i > -1; i--)
		{
			if (modulParts.get(i).getStartIndex() == 0)
			{
				modulParts.remove(i);
			}
		}
		Collections.sort(modulParts);

		/**
		 * Extracting the Number-Value of the ECTS Points from the given String
		 * This method could be removed, if no more parts will be extracted of the given Modultext.
		 */
		for (int i = 0; i < modulParts.size(); i++)
		{
			ModulPart modulPart = modulParts.get(i);

			if (modulPart.getPart().equals(FormFieldNames.INDIKATOR_ECTS))
			{
				StringBuilder sb = new StringBuilder();
				char[] c = modulText.substring(modulPart.getStartIndex(), modulPart.getEndIndex()).toCharArray();
				for (int j = 0; j < c.length; j++)
				{
					if (Character.isDigit(c[j]))
					{
						sb.append(c[j]);
					}
				}
				modulPart.setText(sb.toString());
			} else
			{
				String text = null;
				if (i + 1 >= modulParts.size())
				{
					text = modulText.substring(modulPart.getEndIndex(), modulText.length());
				} else
				{
					text = modulText.substring(modulPart.getEndIndex(), modulParts.get(i + 1).getStartIndex());
				}
				modulPart.setText(text);
			}
		}
		return modulParts;
	}
	/**
	 * Getting the ECTS-Scores 
	 * @param parts
	 * @return
	 */
	private String getECTS(List<ModulPart> parts){
		
		String result = null;
		
		for(ModulPart p : parts){
			if(p.getPart().equals(FormFieldNames.INDIKATOR_ECTS)){
				result = p.getText();
			}
		}
		return result;
	}
	
	/**
	 * Getting the Param if the modul belongs to the studies of wirschaftsinformatik. this param ist optional in the controlForm.
	 * @param parts
	 * @return
	 */
	private String getZuordnung(List<ModulPart> parts){
		
		String result = null;
		
		for(ModulPart p : parts){
			if(p.getPart().equals(FormFieldNames.ZUORDNUNG)){
				result = p.getText();
			}
		}
		return result;
	}

	/**
	 * Helper Method for getting the Startindex of the Regex in the Modultext
	 * @param item
	 * @param modulText
	 * @return
	 */
	private int pStart(ControlFormItems item, String modulText)
	{
		Pattern p = Pattern.compile(item.getFieldValue());
		Matcher m = p.matcher(modulText);

		int start = 0;
		while (m.find())
		{
			start = m.start();
		}
		return start;
	}
	
	/**
	 * Helper Method for getting the Endindex of the Regex in the Modultext
	 * @param item ControlFormItem
	 * @param modulText String
	 * @return
	 */
	private int pEnd(ControlFormItems item, String modulText)
	{
		Pattern p = Pattern.compile(item.getFieldValue());
		Matcher m = p.matcher(modulText);

		int end = 0;
		while (m.find())
		{
			end = m.end();
		}
		return end;
	}

}
