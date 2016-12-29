package org.thb.modulkatalogcontroller.model;

import java.io.File;
import java.io.Serializable;

/**
 * This Class represents one ControlFormItem of the View for analyzing the catalog. 
 * The ControlFormItem has a name, a value, and a type. The Types are combined in the FormItemTypEnum.
 * @author Manuel Raddatz
 *
 */
public class ControlFormItems implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4601222625003470419L;
	private String fieldname;
	private Enum<FormItemTypEnum> type;
	private String fieldValue;
	private File file;

	
	/**
	 * Getter for the File Text of one ControlFormItem. It is the Catalogfile.
	 * @return file File
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Setter for changing and setting the File of one ControlFormItem. It is the Catalogfile.
	 * @param file
	 */
	public void setFile(File file)
	{
		this.file = file;
	}

	/**
	 * Getter for the Fieldname of one ControlFormItem. It returns only the additional Fields.
	 * @return fieldName String
	 */
	public String getFieldname()
	{
		return fieldname;
	}

	/**
	 * Setter for changing and setting Fieldname of one ControlFormItem.
	 * @param String
	 */
	public void setFieldname(String fieldname)
	{
		this.fieldname = fieldname;
	}

	/**
	 * Getter for the Fieldvalue (String) of the ControlFormItem. 
	 * @return String
	 */
	public String getFieldValue()
	{
		return fieldValue;
	}

	/**
	 * Setter for changing and setting the Fieldvalue of one ControlFormItem.
	 * @param String
	 */
	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}

	/**
	 * Getter for the EnumTyp of the ControlFormItem. 
	 * @return Enum<FormItemTypEnum>
	 */
	public Enum<FormItemTypEnum> getType()
	{
		return type;
	}

	/**
	 * Setter for changing or setting the Typ of a ControlFormItem.
	 * @param <FormItemTypEnum> Enum 
	 */
	public void setType(Enum<FormItemTypEnum> type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return "ControlFormItems [fieldname=" + fieldname + ", type=" + type + ", fieldValue=" + fieldValue + "]";
	}
	
	
}
