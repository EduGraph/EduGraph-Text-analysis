package org.thb.modulkatalogcontroller.model;

import java.io.Serializable;
import java.nio.file.Path;
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
	private static final long serialVersionUID = 4019072842676984978L;
	
	private Path filePath;
	
	private byte[] fileContent;
	
	private List<ControlFormItems> controlItems;
	
	private List<Modul> moduls;

	/**
	 * Returning the content of the given Katalogfile.
	 * @return byte[]
	 */
	public byte[] getFileContent()
	{
		return fileContent;
	}

	/**
	 * Setting the filecontent of the Katalogobject.
	 * @param fileContent
	 */
	public void setFileContent(byte[] fileContent)
	{
		this.fileContent = fileContent;
	}

	/**
	 * Returning the Filepath of the 
	 * @return Path
	 */
	public Path getFilePath()
	{
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(Path filePath)
	{
		this.filePath = filePath;
	}
	
	/**
	 * Constructor of the Katalog Class. During Instantiation an empty List of Moduls the SolrConnection 
	 * and the keyClassVectors are
	 * initialized. 
	 */
	public Katalog(){
		moduls = new ArrayList<>();
	}

	/**
	 * Returning the List of the controlFormItems
	 * @return List
	 */
	public List<ControlFormItems> getControlItems()
	{
		return controlItems;
	}

	/**
	 * Setting the List of controlFormItems
	 * @param controlItems
	 */
	public void setControlItems(List<ControlFormItems> controlItems)
	{
		this.controlItems = controlItems;
	}

	/**
	 * Returning all Moduls of the Katalog
	 * @return List<Modul>
	 */
	public List<Modul> getModuls()
	{
		return moduls;
	}

	/**
	 * Setting the List of all Moduls
	 * @param moduls List
	 */
	public void setModuls(List<Modul> moduls)
	{
		this.moduls = moduls;
	}
}
