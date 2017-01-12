package org.thb.modulkatalogcontroller;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.primefaces.model.UploadedFile;
import org.thb.modulkatalogcontroller.model.ControlFormItems;
import org.thb.modulkatalogcontroller.model.FormFieldNames;
import org.thb.modulkatalogcontroller.model.FormItemTypEnum;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.model.Modul;

/**
 * The ControlFormController controls the Modulextraction of the given Catalog. Through the GUI the user
 * provides a Catalog which should be analyzed. The Controller uses the given ControlFormItems and
 * the Value of each item to extract the Moduls and their Parts. 
 * @author Manuel Raddatz
 *
 */
@Named
@javax.enterprise.context.SessionScoped
public class ControlFormController implements Serializable
{
	private static final long serialVersionUID = 6740189066350549423L;
		
	private Katalog katalog;
	private FormItemTypEnum itemEnum;
	private List<ControlFormItems> formItems;
	private List<Modul> moduls;
	private ControlFormItems item;
	private String fieldname;
	private UploadedFile katalogFile;
	
	/**
	 * Constructor of CDI Bean. During Call the used components are initialized.
	 */
	public ControlFormController(){
		init();
	}
	/**
	 * Getter for return the Enumtyp of the Formfield.
	 * @return FormItemTypEnum 
	 */
	public FormItemTypEnum getItemEnum()
	{
		return itemEnum;
	}

	/**
	 * Setter for changing and setting the Enumtyp of the Formfield.
	 * @param itemEnum
	 */
	public void setItemEnum(FormItemTypEnum itemEnum)
	{
		this.itemEnum = itemEnum;
	}

	/**
	 * Getter for return the hole Controlitem of the Formfield. That item holds the name, the value and the type of the Formfield. 
	 * @return ControlFormItems
	 */
	public ControlFormItems getItem()
	{
		return item;
	}

	/**
	 * Setter for changing and setting the ControlFormItem of the Formfield.
	 * @param item
	 */
	public void setItem(ControlFormItems item)
	{
		this.item = item;
	}

	/**
	 * Getting the Fieldname of a Formfield.
	 * @return String
	 */
	public String getFieldname()
	{
		return fieldname;
	}

	/**
	 * Setting of Changigng the Name of Field.
	 * @param fieldname
	 */
	public void setFieldname(String fieldname)
	{
		this.fieldname = fieldname;
	}

	/**
	 * Getting the uploadedfile of the Katalogfile.
	 * @return UploadedFile
	 */
	public UploadedFile getKatalogFile()
	{
		return katalogFile;
	}

	/**
	 * Setting or Changing the actual Katalogfile.
	 * @param katalogFile
	 */
	public void setKatalogFile(UploadedFile katalogFile)
	{
		this.katalogFile = katalogFile;
	}

	/**
	 * Getting the actual Katalog
	 * @return Katalog
	 */
	public Katalog getKatalog()
	{
		return katalog;
	}

	/**
	 * Setting or Changing the actual Katalog.
	 * @param katalog
	 */
	public void setKatalog(Katalog katalog)
	{
		this.katalog = katalog;
	}

	/**
	 * private Helper Method for initializing the Components of the ControlFormController
	 */
	private void init(){
		
		System.setProperty("file.encoding","UTF-8");
		Field charset;
		try
		{
			charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1)
		{
			System.err.println("Error CharSet: "+e1.getMessage());
		}
	
		katalog = new Katalog();
		moduls = new ArrayList<>();
		
		formItems = new ArrayList<>();
		ControlFormItems nameHS = new ControlFormItems();
		nameHS.setFieldname(FormFieldNames.HOCHSCHULNAME);
		nameHS.setType(FormItemTypEnum.STRING);
		formItems.add(nameHS);
		
		ControlFormItems idHS = new ControlFormItems();
		idHS.setFieldname(FormFieldNames.HOCHSCHULID);
		idHS.setType(FormItemTypEnum.STRING);
		formItems.add(idHS);
		
		ControlFormItems katalogItem = new ControlFormItems();
		katalogItem.setFieldname(FormFieldNames.KATALOGDATEI);
		katalogItem.setType(FormItemTypEnum.FILE);
		formItems.add(katalogItem);

		ControlFormItems einleitung = new ControlFormItems();
		einleitung.setFieldname(FormFieldNames.MODULERKENNUNG);
		einleitung.setType(FormItemTypEnum.STRING);
		formItems.add(einleitung);
		
		ControlFormItems zuordnung = new ControlFormItems();
		zuordnung.setFieldname(FormFieldNames.ZUORDNUNG);
		zuordnung.setType(FormItemTypEnum.STRING);
		formItems.add(zuordnung);
		
		ControlFormItems startSeite = new ControlFormItems();
		startSeite.setFieldname(FormFieldNames.STARTSEITE);
		startSeite.setType(FormItemTypEnum.NUMBER);
		formItems.add(startSeite);
		
		ControlFormItems endSeite = new ControlFormItems();
		endSeite.setFieldname(FormFieldNames.ENDSEITE);
		endSeite.setType(FormItemTypEnum.NUMBER);
		formItems.add(endSeite);
		
		ControlFormItems ects = new ControlFormItems();
		ects.setFieldname(FormFieldNames.INDIKATOR_ECTS);
		ects.setType(FormItemTypEnum.STRING);
		formItems.add(ects);

		katalog.setControlItems(formItems);
	}

	/**
	 * The save() Method is invoke by pressing the "Module und Parts extrahieren" Button. The file and the controlitems where given to
	 * the specific Extractor. The method will also provide a view change.
	 * @return String newPage
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public String save() throws IOException, SolrServerException{

		byte[] fileContent = null;
		
		String extension = FilenameUtils.getExtension(katalogFile.getFileName());
			
		String filename =FilenameUtils.getBaseName(katalogFile.getFileName());
		
		Path folder = Paths.get(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.UPLOADDIRECTORY));
				
		Path file = Files.createTempFile(folder, filename + "-", "." + extension);
		
		katalog.setFilePath(file);

		fileContent = katalogFile.getContents();
		
		katalog.setFileContent(fileContent);
		 
		for (ControlFormItems item : katalog.getControlItems())
		{
			if(item.getFieldname().equals(FormFieldNames.KATALOGDATEI)){
				System.out.println(katalogFile.getFileName());
				item.setFieldValue(katalogFile.getFileName());
			}
		} 
		 
		katalog.setModuls(moduls);
		
		KatalogTextExtractor katalogExtractor;

		try
		{		
			katalogExtractor = new KatalogTextExtractor(katalog, moduls, fileContent);
			katalogExtractor.extractModultext();
		} catch (IOException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while reading the uploaded file during extraction.",e.getLocalizedMessage()));
			return "";
		} catch (SolrServerException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while trying to connect to Solr. Please confirm Connection to Solr Server.",e.getLocalizedMessage()));
			return "";
		}catch (PatternSyntaxException e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Regex Error. Please check the syntax of the regularexpression.",e.getLocalizedMessage()));
			return "";
		}
		
	    return Pages.DOCUMENTCONFIRM;
	}

	/**
	 * Getter of the TypEnums
	 * @return
	 */
	public FormItemTypEnum[] getStatuses() {
        return FormItemTypEnum.values();
    }
	
}
