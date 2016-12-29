package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;
import java.util.List;

/**
 * A Vector ist the Result of tvrh Query to Solr. Each indexd Modul of a katalog has a vector.
 * @author Manuel
 *
 */
public class Vector implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6739193930151839124L;
	private List<Term> terms;
	private String modulName;

	public Vector()
	{
		super();
	}
	public Vector(List<Term> terms, String modulName)
	{
		super();
		this.terms = terms;
		this.modulName = modulName;
	}
	public List<Term> getTerms()
	{
		return terms;
	}
	public void setTerms(List<Term> terms)
	{
		this.terms = terms;
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
