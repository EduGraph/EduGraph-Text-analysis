package org.thb.modulkatalog.junit;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UnitTestDemoModulKatalog
{
	
	@Deployment
	public static JavaArchive createDeployment(){
		return ShrinkWrap.create(JavaArchive.class)
	            .addClass(CheckSystemProperty.class)
	            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	CheckSystemProperty checkProps;
	
	@Test
	public void checkSolrSystemProperty(){
        Assert.assertEquals("http://localhost:8983/solr/LSA", checkProps.checkSolrProperty());
        System.out.println(checkProps.checkSolrProperty());
	}

}
