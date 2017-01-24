package org.thb.modulkatalogcontroller.profileAWS;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.IUploadService;
import org.thb.modulkatalogcontroller.model.Katalog;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

			s3File = new File(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.AWSCREDENTIALSPROPERTIESFILE));
			
			pc = new PropertiesCredentials(s3File);
			
			awsS3 = new AmazonS3Client(pc);
			region = Region.getRegion(Regions.fromName(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.AWSREGION)));
			awsS3.setRegion(region);
			
		}catch(Exception e){
			throw new AmazonClientException("could not create AmazonS3Client",e);
		}
	}
	
	
	@Override
	public boolean uploadCatalog(String profileID, Katalog katalog)
	{
		boolean result = false;
		try{
		 File file = null;
		 try{
			 if(katalog.getFilePath()!=null){
				 file = new File(katalog.getFilePath().getFileName().toString());
				 awsS3.putObject(new PutObjectRequest(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.S3BUCKET), katalog.getFilePath().getFileName().toString(), file));
			 }else{
				 file = new File(katalog.getAwsPath().substring(katalog.getAwsPath().lastIndexOf("/"), katalog.getAwsPath().length()));
				 
				 InputStream stream = new ByteArrayInputStream(katalog.getFileContent());
		         ObjectMetadata meta = new ObjectMetadata();
		         meta.setContentLength(katalog.getFileContent().length);
		         meta.setContentType("application/pdf");
		       		           
		         awsS3.putObject(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.S3BUCKET), katalog.getAwsPath().substring(katalog.getAwsPath().lastIndexOf("/"), katalog.getAwsPath().length()), stream, meta);
		         awsS3.setObjectAcl(ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.S3BUCKET),  katalog.getAwsPath().substring(katalog.getAwsPath().lastIndexOf("/"),katalog.getAwsPath().length()), CannedAccessControlList.PublicRead);
			 
			 } 
		 }catch(Exception ex){
			 System.err.println("Error while creating File from katalogObject: "+ex.getMessage());
		 }
 
         result = true;
        
      } catch (AmazonServiceException ase) {
         System.err.println("AmazonServiceException-Error Message:    " + ase.getMessage());
         System.err.println("HTTP Status Code: " + ase.getStatusCode());
         System.err.println("AWS Error Code:   " + ase.getErrorCode());
         System.err.println("Error Type:       " + ase.getErrorType());
         System.err.println("Request ID:       " + ase.getRequestId());
     } catch (AmazonClientException ace) {
    	 
         System.err.println("AmazonClientException-Error Message: " + ace.getMessage());
     }	
		return result;
	}

}
