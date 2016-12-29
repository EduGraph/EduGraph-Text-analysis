package org.thb.modulkatalogcontroller.profileAWS;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AmazonS3Access
{

	private AmazonS3 awsS3;
	private Region region;
	
	private File s3File;
	private PropertiesCredentials pc;
	
	
	public AmazonS3Access(){	
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
	
	public AmazonS3 getAwsS3()
	{
		return awsS3;
	}

	public void setAwsS3(AmazonS3 awsS3)
	{
		this.awsS3 = awsS3;
	}

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}

	
}
