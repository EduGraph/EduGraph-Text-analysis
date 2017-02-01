package org.thb.modulkatalog.arquillian;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.factory.DatabaseConnectionFactory;
import org.thb.modulkatalogcontroller.model.IKatalogDAO;
import org.thb.modulkatalogcontroller.model.Katalog;
import org.thb.modulkatalogcontroller.profileTHB.KatalogDAOMongoDBImpl;
import org.thb.modulkatalogcontroller.rest.ModulKatalogREST;
import org.thb.modulkatalogcontroller.solr.SolrConnection;

@RunWith(Arquillian.class)
public class ConnectionTestAWSIT
{
	@ArquillianResource
    private URL deploymentURL;
	
    @Deployment(testable = false)
    public static WebArchive create()
    {
        return ShrinkWrap.create(WebArchive.class, "resttest.war")
        		.addClass(ModulKatalogREST.class)
        		.addClass(DatabaseConnectionFactory.class)
        		.addClass(Katalog.class)
        		.addClass(IKatalogDAO.class)
        		.addClass(KatalogDAOMongoDBImpl.class)
        		.addClass(ApplicationProperties.class)
        		.addClass(ApplicationPropertiesKeys.class)
        		.addClass(SolrConnection.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
          
    }
    
    
    @Test
    public void testSolrConnection(){
    	System.out.println("Running Integration Test on Solr........");
    	
    	String solrUrl = "http://54.191.87.72:8983/solr/catalogs";
    	String solrKeyClassUrl="http://54.191.87.72:8983/solr/keyCore";
    	
    	SolrConnection solr = new SolrConnection(solrUrl, solrKeyClassUrl);
    	SolrClient solrClient = solr.getSolrClient();
    	SolrPingResponse response =	null;
		try
		{
			response = solrClient.ping();
		} catch (SolrServerException e)
		{
			System.err.println("SolrException during Integrationtest on Solr: "+e.getLocalizedMessage());
		} catch (IOException e)
		{
			System.err.println("IOException during Integrationtest on Solr: "+e.getLocalizedMessage());
		}
    	assertNotNull(response); 
    }
}
