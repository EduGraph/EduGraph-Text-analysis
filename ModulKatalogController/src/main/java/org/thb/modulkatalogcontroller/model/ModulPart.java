package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;

/**
 * The ModulParts Class represents the different Parts of one Modultext. Actually it is not implemented. It ist still difficult
 * to extract the specific Parts of one Modul.
 * @author Manuel Raddatz
 *
 */
public class ModulPart implements Comparable<ModulPart>, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7649812528627452916L;

	private String part;
	
	private String value;
	
	private int startIndex;
	private int endIndex;
	
	private String text;

	/**
	 * Getter for returning the Text which is given by the extraction of the Part. The ControlFormItem defines
	 * a ModulPart.
	 * @return String
	 */
	public String getText()
	{
		return text;
	}

	/** 
	 * @param String
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	public ModulPart()
	{
		super();
	}

	public ModulPart(String part, String value)
	{
		super();
		this.part = part;
		this.value = value;
	}

	public String getPart()
	{
		return part;
	}

	public void setPart(String part)
	{
		this.part = part;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public int getStartIndex()
	{
		return startIndex;
	}

	public void setStartIndex(int startIndex)
	{
		this.startIndex = startIndex;
	}

	public int getEndIndex()
	{
		return endIndex;
	}

	public void setEndIndex(int endIndex)
	{
		this.endIndex = endIndex;
	}

	@Override
	public int compareTo(ModulPart o)
	{
		 return Integer.compare(this.getStartIndex(), o.startIndex);
	}
	
}
