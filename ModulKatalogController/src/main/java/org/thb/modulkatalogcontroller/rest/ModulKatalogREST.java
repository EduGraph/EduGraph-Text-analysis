package org.thb.modulkatalogcontroller.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.factory.DatabaseConnectionFactory;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;

/**
 * 
 * @author ManuelRaddatz
 *
 */
@Path("/ModulKatalogREST")
public class ModulKatalogREST
{
	IKatalogDAO instance;
	
	public ModulKatalogREST()
	{
		instance = DatabaseConnectionFactory.getDatabaseConnection(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEPROVIDER),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTSTRING),
				Integer.parseInt(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTPORTSTRING)),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASENAMESTRING), 
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEKATALOGCOLLECTIONSTRING));
	
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("AllKataloge")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKataloge()
	{
		//@QueryParam("w") int w, @QueryParam("h") int h, @QueryParam("it") int it
		try
		{
			List<Object> objects = instance.getAllKatalogs();
			return Response.status(200).entity(objects).build();
		} catch (Exception e)
		{
			System.err.println("Error while querrying the database."+e.getMessage());
		}
		return Response.status(400).entity("Errromessage").build();
		

	}
	
	@GET
	@Path("{katalogID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPillar(@PathParam("katalogID") String katalogID)
	{
		return null;		
	}

}
