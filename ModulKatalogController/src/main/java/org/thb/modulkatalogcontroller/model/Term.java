package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;

public class Term implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3362159611691988258L;
	private String word;
	private Double termFrequency;
	private Double docFrequency;
	private Double inverseTermFrequency;
	
	private String modulName; // the modul is important for getting the specific vector of the model

	public Term()
	{
		super();
	}
		
	public Term(String modulName)
	{
		super();
		this.modulName = modulName;
	}

	public Term(String word, Double termFrequency, Double docFrequency, Double inverseTermFrequency, String modulName)
	{
		super();
		this.word = word;
		this.termFrequency = termFrequency;
		this.docFrequency = docFrequency;
		this.inverseTermFrequency = inverseTermFrequency;
		this.modulName = modulName;
	}

	public Term(String word, Double termFrequency)
	{
		super();
		this.word = word;
		this.termFrequency = termFrequency;
	}

	public String getWord()
	{
		return word;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

	public Double getTermFrequency()
	{
		return termFrequency;
	}

	public void setTermFrequency(Double termFrequency)
	{
		this.termFrequency = termFrequency;
	}

	public Double getDocFrequency()
	{
		return docFrequency;
	}

	public void setDocFrequency(Double docFrequency)
	{
		this.docFrequency = docFrequency;
	}

	public Double getInverseTermFrequency()
	{
		return inverseTermFrequency;
	}

	public void setInverseTermFrequency(Double inverseTermFrequency)
	{
		this.inverseTermFrequency = inverseTermFrequency;
	}
	
	public String getModulName()
	{
		return modulName;
	}

	public void setModulName(String modulName)
	{
		this.modulName = modulName;
	}
	
}
