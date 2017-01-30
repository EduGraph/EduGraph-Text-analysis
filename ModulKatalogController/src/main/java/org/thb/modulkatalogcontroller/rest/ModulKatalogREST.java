package org.thb.modulkatalogcontroller.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.DaoReturn;
import org.thb.modulkatalogcontroller.IUploadService;
import org.thb.modulkatalogcontroller.PillarCalculationServiceImpl;
import org.thb.modulkatalogcontroller.factory.DatabaseConnectionFactory;
import org.thb.modulkatalogcontroller.factory.UploadFactory;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.KatalogDTO;

/**
 * The ModulKatalogREST Class is the Service Class for providing the data an functionality of the ModulKatalogController
 * to other Applications
 * @author ManuelRaddatz
 *
 */
@Path("/ModulKatalogREST")
public class ModulKatalogREST
{
	IKatalogDAO instance;
	
	IUploadService katalogFileService;
	
	public ModulKatalogREST()
	{
		instance = DatabaseConnectionFactory.getDatabaseConnection(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEPROVIDER),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTSTRING),
				Integer.parseInt(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASECLIENTPORTSTRING)),
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASENAMESTRING), 
				ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.DATABASEKATALOGCOLLECTIONSTRING));
	
		katalogFileService = UploadFactory.getUploadService(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.PROFILEID));
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("Kataloge")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKataloge()
	{
		try
		{
			List<Object> objects = instance.getAllKatalogs();

			return Response.status(200) 
					.header("Access-Control-Allow-Origin", "*")
		            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		            .header("Access-Control-Allow-Credentials", "true")
		            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		            .header("Access-Control-Max-Age", "1209600")
		            .entity(objects)
		            .build();
		} catch (Exception e)
		{
			System.err.println("Error while querrying the database."+e.getMessage());
		}
		System.err.println("Error while querrying the database for all saved Katalogs.");
		return Response.status(500)
				.header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
				.entity("Internal Error").build();	
	}
	
	@GET
	@Path("Kataloge/{katalogID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPillar(@PathParam("katalogID") String katalogID)
	{		
		try
		{		
			KatalogDTO dto = (KatalogDTO) instance.getKatalogById(katalogID);
			
			if(dto.getId()!=null){
				IPillarCalculationService pillarCalcService = new PillarCalculationServiceImpl();
				Map<String, Double> resultMap = pillarCalcService.getPillar(dto.getModulDTOs());
				if(resultMap!=null){
					return Response.status(200) 
							.header("Access-Control-Allow-Origin", "*")
				            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				            .header("Access-Control-Allow-Credentials", "true")
				            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				            .header("Access-Control-Max-Age", "1209600")
				            .entity(resultMap).build();
				}else{
					System.err.println("Error while calculating the final Result for the pillars.");
					return Response.status(500)
							.header("Access-Control-Allow-Origin", "*")
				            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				            .header("Access-Control-Allow-Credentials", "true")
				            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				            .header("Access-Control-Max-Age", "1209600")
							.entity("Error while Calculating the Results").build();
				}
				
			}else{
				System.err.println("Error! No Katalog found to given ID.");
				return Response.status(404)
						.header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
						.entity("Katalog not Found").build();
			}			
		} catch (Exception e)
		{
			System.err.println("Error while querrying the database."+e.getMessage());
			return Response.status(500)
					.header("Access-Control-Allow-Origin", "*")
		            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		            .header("Access-Control-Allow-Credentials", "true")
		            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		            .header("Access-Control-Max-Age", "1209600")
					.entity("Internal Error").build();
		}		
	}
	
	@DELETE
    @Path("Kataloge/{katalogID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteKatalog(@PathParam("katalogID") String katalogID){
		
		
		KatalogDTO dto = (KatalogDTO) instance.getKatalogById(katalogID);
		boolean res = katalogFileService.deleteCatalog(dto);
		String result = null;
		if(res){
			result = instance.deleteKatalog(katalogID);
		}
		if(result != null){
			if(result.equals(DaoReturn.OK)){
				return Response.status(200)
						.header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
			            .entity("Katalog with ID: "+katalogID+ " deleted")
			            .build();
			}else{
				System.err.println("Error while deleting Katalog from database.");
				return Response.status(500)
						.header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
						.entity("Could not delete: "+katalogID+ " from Database").build();
			}
		}else{
			System.err.println("Error while deleting Katalog from Filesystem.");
			return Response.status(500)
					.header("Access-Control-Allow-Origin", "*")
		            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		            .header("Access-Control-Allow-Credentials", "true")
		            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		            .header("Access-Control-Max-Age", "1209600")
					.entity("File containing to Katalog with ID: "+katalogID+ " could not deleted").build();
		}
	}
}
