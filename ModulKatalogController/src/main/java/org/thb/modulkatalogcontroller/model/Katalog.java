package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Katalog class represents the given Modulkatalog of an University. One Katalog has different Moduls and the each Modul has different
 * Parts. The Katalog is represented through a uploaded file. The ControlFormItems controls the extraction of the moduls and the parts
 * @author Manuel Raddatz
 *
 */
public class Katalog implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4019072842676984978L;

	private String file;

	/*
	 * TODO: Maybe change to Map to avoid looping
	 */
	private List<ControlFormItems> controlItems;

	private List<Modul> moduls;
	
	/**
	 * Constructor of the Katalog Class. During Instantiation an empty List of Moduls the SolrConnection and the keyClassVectors are
	 * initialized. 
	 */
	public Katalog(){
		moduls = new ArrayList<>();
	}

	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	public List<ControlFormItems> getControlItems()
	{
		return controlItems;
	}

	public void setControlItems(List<ControlFormItems> controlItems)
	{
		this.controlItems = controlItems;
	}

	public List<Modul> getModuls()
	{
		return moduls;
	}

	public void setModuls(List<Modul> moduls)
	{
		this.moduls = moduls;
	}
}