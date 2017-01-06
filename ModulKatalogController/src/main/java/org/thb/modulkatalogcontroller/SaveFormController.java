package org.thb.modulkatalogcontroller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.thb.modulkatalogcontroller.factory.DatabaseConnectionFactory;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.solr.SolrConnection;

/**
 * This Controller is responsible saving the modulcatalog in the given database.
 * @author Manuel Raddatz
 *
 */
@Named
@javax.enterprise.context.SessionScoped
public class SaveFormController implements Serializable
{
	private static final long serialVersionUID = 1L;
	private SolrConnection solrConnect;

	/**
	 * Constructor of the SaveFormController.
	 */
	public SaveFormController()
	{
		init();
	}

	/**
	 * The init method is initializing the solrConnection Object
	 */
	private void init()
	{
		solrConnect = new SolrConnection();
	}

	/**
	 * The save method putting the file to the upload directory and the contents to the Database. After action the page
	 * return is the index page.
	 * @param katalog
	 * @return String
	 */
	public String save(Katalog katalog)
	{

		IKatalogDAO instance = DatabaseConnectionFactory.getDatabaseConnection(
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEPROVIDER),
				ApplicationProperties.getInstance()
						.getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTSTRING),
				Integer.parseInt(ApplicationProperties.getInstance()
						.getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTPORTSTRING)),
				ApplicationProperties.getInstance()
						.getApplicationProperty(ApplicationPropertiesKeys.DATABASENAMESTRING),
				ApplicationProperties.getInstance()
						.getApplicationProperty(ApplicationPropertiesKeys.DATABASEKATALOGCOLLECTIONSTRING));

		String result = instance.addKatalog(katalog);
		
		if(result.equals(DaoReturn.KATALOGinDATABASE)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,	"WARNING, Katalog is already in Database. PLEASE TRY AGAIN WITH ANOTHER CATALOG", null));
			nextTry(katalog);
			return "";
		}else if(result.equals(DaoReturn.ERROR)){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while adding Katalog in DB. PLEASE TRY AGAIN", null));
			nextTry(katalog);
			return "";
		}

		saveCatalogToUploadDirectory(katalog);

		nextTry(katalog);
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		return Pages.INDEX;
	}

	/**
	 * The nextry deletes the solr index for the given catalog for avoiding
	 * errors in getting the right documents for analyzing the catalog.
	 * @return String
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public String nextTry(Katalog katalog)
	{
		try
		{
			solrConnect.deleteIndex(katalog.getModuls());
			deleteTempFile(katalog.getFilePath());
		} catch (SolrServerException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Solrexception while deleting index.", e.getLocalizedMessage()));
			return "";
		} catch (IOException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"IOException deleting index and deleting tempFile", e.getLocalizedMessage()));
			return "";
		}
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		return Pages.INDEX;
	}

	/**
	 * Saving the Katalogfile to the given uploaddirectory from the properties
	 * file.
	 * @param katalog
	 * @return boolean
	 */
	private boolean saveCatalogToUploadDirectory(Katalog katalog)
	{

		byte[] bytes = katalog.getFileContent();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try
		{
			FileOutputStream out = new FileOutputStream(new File(katalog.getFilePath().toString()));
			IOUtils.copy(in, out);
			in.close();
			out.close();
		} catch (FileNotFoundException e)
		{
			System.out.println("FileNotFoundError saving KatalogFile: " + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("IOError saving KatalogFile: " + e.getMessage());
		}
		return true;
	}

	/**
	 * During reading and uploading the catalogfile, a temporary File is created and before trying a next attempt, the file
	 * is deleted.
	 * @param path
	 * @return boolean
	 * @throws IOException
	 */
	private boolean deleteTempFile(Path path) throws IOException{
		
		Files.delete(path);
		return true;
		
	}
}
