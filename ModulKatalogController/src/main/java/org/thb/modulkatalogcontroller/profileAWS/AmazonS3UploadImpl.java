package org.thb.modulkatalogcontroller.profileAWS;

import java.io.File;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.IUploadService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AmazonS3UploadImpl implements IUploadService
{

	private AmazonS3 awsS3;
	private Region region;
	
	private File s3File;
	private PropertiesCredentials pc;
	
	public AmazonS3UploadImpl()
	{
		try{

			s3File = new File("config/credentials.properties");
			
			pc = new PropertiesCredentials(s3File);
			
			awsS3 = new AmazonS3Client(pc);
			region = Region.getRegion(Regions.US_WEST_2);
			awsS3.setRegion(region);
			
		}catch(Exception e){
			throw new AmazonClientException("could not create AmazonS3Client",e);
		}
	}
	
	
	@Override
	public boolean uploadCatalog(String profileID, String fileName)
	{
		boolean result = false;
		try{
			
		 System.out.println("Uploading a new object to S3 from a file\n");
         File file = new File(fileName);
         
         awsS3.putObject(new PutObjectRequest(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.S3BUCKET), fileName, file));
         result = true;
      } catch (AmazonServiceException ase) {
         System.out.println("Error Message:    " + ase.getMessage());
         System.out.println("HTTP Status Code: " + ase.getStatusCode());
         System.out.println("AWS Error Code:   " + ase.getErrorCode());
         System.out.println("Error Type:       " + ase.getErrorType());
         System.out.println("Request ID:       " + ase.getRequestId());
     } catch (AmazonClientException ace) {
    	 
         System.out.println("Error Message: " + ace.getMessage());
     }
		
		return result;
	}

}
