package org.thb.modulkatalogcontroller.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The KatalogDTO is used to seperate the needed Data from the KatalogObject
 * @author Manuel
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KatalogDTO
{
	
	private String id;
	
	private String hochschulname;
	
	private String katalogFile;
	
	private String dateTime;
	
	private List<ModulDTO> modulDTOs;
	
	public KatalogDTO()
	{
		super();
	}
	
	public KatalogDTO(Katalog katalog)
	{
		super();
		this.modulDTOs = new ArrayList<>();
		for(ControlFormItems item : katalog.getControlItems()){
			if(item.getFieldname().equals(FormFieldNames.HOCHSCHULNAME))
			{
				this.hochschulname = item.getFieldValue();
			}else if(item.getFieldname().equals(FormFieldNames.HOCHSCHULID))
			{
				this.id= item.getFieldValue();
			}
		}
		this.katalogFile=katalog.getFilePath().toString();
		this.dateTime= Calendar.getInstance().getTime().toString();
		
		for(Modul m : katalog.getModuls()){
			ModulDTO mDTO = new ModulDTO(m);
			this.modulDTOs.add(mDTO);
		}
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the hochschulname
	 */
	public String getHochschulname()
	{
		return hochschulname;
	}

	/**
	 * @param hochschulname the hochschulname to set
	 */
	public void setHochschulname(String hochschulname)
	{
		this.hochschulname = hochschulname;
	}

	/**
	 * @return the katalogFile
	 */
	public String getKatalogFile()
	{
		return katalogFile;
	}

	/**
	 * @param katalogFile the katalogFile to set
	 */
	public void setKatalogFile(String katalogFile)
	{
		this.katalogFile = katalogFile;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime()
	{
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}

	/**
	 * @return the modulDTOs
	 */
	public List<ModulDTO> getModulDTOs()
	{
		return modulDTOs;
	}

	/**
	 * @param modulDTOs the modulDTOs to set
	 */
	public void setModulDTOs(List<ModulDTO> modulDTOs)
	{
		this.modulDTOs = modulDTOs;
	}

	@Override
	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try
		{
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
}
