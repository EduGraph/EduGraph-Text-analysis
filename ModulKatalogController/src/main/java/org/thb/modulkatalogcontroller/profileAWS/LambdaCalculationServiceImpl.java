package org.thb.modulkatalogcontroller.profileAWS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.ICalculationService;
import org.thb.modulkatalogcontroller.model.Modul;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Class shows one possibility of invoking a AWS Lambda Function from a Java Class. 
 * @author Manuel Raddatz
 *
 */
public class LambdaCalculationServiceImpl implements ICalculationService
{

	private String functionName;

	private File s3CredentialsFile;

	private AWSLambdaClient lambdaClient;

	private InvokeRequest invokeRequest;

	/**
	 * Constructor for Lambda Invocation Service 
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public LambdaCalculationServiceImpl() throws FileNotFoundException, IllegalArgumentException, IOException
	{
		init();
	}

	/**
	 * Method for initializing the Amazon Components to invoke the Lambda Function
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void init()
	{
		s3CredentialsFile = new File(ApplicationProperties.getInstance()
				.getApplicationProperty(ApplicationPropertiesKeys.AWSCREDENTIALSPROPERTIESFILE));

		PropertiesCredentials pc = null;;
		try
		{
			pc = new PropertiesCredentials(s3CredentialsFile);
			functionName = ApplicationProperties.getInstance().getApplicationProperty(ApplicationPropertiesKeys.AWSCALCULATIONLAMBDAFUNCTION);
			
			lambdaClient = new AWSLambdaClient(pc);
			lambdaClient.configureRegion(Regions.US_WEST_2);

			invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName(functionName);
		} catch (FileNotFoundException e)
		{
			System.err.println("Error (FileNotFound) while getting Propertiesfile for credentials: "+e.getMessage() );
		} catch (IllegalArgumentException e)
		{
			System.err.println("Error (IllegalArgument) while getting Propertiesfile for credentials: "+e.getMessage() );
		} catch (IOException e)
		{
			System.err.println("Error (IOException) while getting Propertiesfile for credentials: "+e.getMessage() );
		}

	}

	/**
	 * Service Method for calculating the normalized Score of the given modul. The Calculation is done by a aws lambda Function.
	 */
	@Override
	public void calculateNormalizedScore(Modul modul)
	{
		try
		{
			Map<String, Double> payload = new HashMap<>();
			payload.put("bwlScore", modul.getBwlScore());
			payload.put("infScore", modul.getInfScore());
			payload.put("wiScore", modul.getWiScore());
			payload.put("nnScore", modul.getNnScrore());

			invokeRequest.setPayload(new ObjectMapper().writeValueAsString(payload));

			@SuppressWarnings("unchecked")
			HashMap<String, Double> result = new ObjectMapper().readValue(
					byteBufferToString(lambdaClient.invoke(invokeRequest).getPayload(), Charset.forName("UTF-8")), HashMap.class);
			System.out.println(result.size());
			
			modul.setBwlScoreNormalized(result.get("bwlScore"));
			modul.setInfScoreNormalized(result.get("infScore"));
			modul.setWiScoreNormalized(result.get("wiScore"));
			modul.setNnScoreNormalized(result.get("nnScore"));
			
		} catch (Exception e)
		{
			System.err.println("ERROR in LambdaCalculationService: " + e.getMessage());
		}
	}

	/**
	 * Converting the returning ByteBuffer from the Lambda Function to String (json)
	 * @param buffer ByteBuffer
	 * @param charset Charset
	 * @return String
	 */
	private String byteBufferToString(ByteBuffer buffer, Charset charset)
	{
		byte[] bytes;
		if (buffer.hasArray())
		{
			bytes = buffer.array();
		} else
		{
			bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
		}
		return new String(bytes, charset);
	}
}
