package org.thb.modulkatalogcontroller.solr;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

import org.thb.modulkatalogcontroller.ApplicationProperties;
import org.thb.modulkatalogcontroller.ApplicationPropertiesKeys;
import org.thb.modulkatalogcontroller.model.Modul;
import org.thb.modulkatalogcontroller.model.Term;
import org.thb.modulkatalogcontroller.model.Vector;

/**
 * The SolrConnection is responsible for indexing the modultext and getting back the Vector of the indexed Text.
 * @author Manuel
 *
 */
public class SolrConnection implements Serializable
{
	private static final long serialVersionUID = -5005185994656363899L;
	
	private SolrClient solrClient;
	private SolrQuery solrQuery;
	
	/**
	 * Constructor of SolrConnection with init-Method
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public SolrConnection()
	{
		super();	
	}

	/**
	 * This Methods provides the 4 Vectors of the indexed Key-Classes. 
	 * @return List of Vector
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public List<Vector> setUpKeyVectors() throws SolrServerException, IOException
	{
		solrClient = new HttpSolrClient(ApplicationProperties.getInstance().getApplicationProperty((ApplicationPropertiesKeys.SOLRURLKEYCLASSString)));
		solrQuery = new SolrQuery();
		solrQuery.setRequestHandler("/tvrh?");
		solrQuery.setParam("fl", "content");
		solrQuery.setParam("q", "content:* TO *");
		solrQuery.setParam("tv.tf", "true");
		solrQuery.setParam("tv.df", "true");
		solrQuery.setParam("tv.tf_idf", "true");
		solrQuery.setRows(Integer.MAX_VALUE);
		solrQuery.setTerms(true);
		
		QueryResponse response = solrClient.query(solrQuery);

		NamedList<Object> first = response.getResponse();
		Vector v = null;
		List<Vector> vectorList = new ArrayList<>();
		NamedList<?> termvectors = (NamedList<?>) first.get("termVectors");
		for (int i = 0; i < termvectors.size(); i++)
		{
			NamedList<?> docVector = (NamedList<?>) termvectors.getVal(i);
			for (int j = 0; j < docVector.size(); j++)
			{
				if(docVector.getName(j).equals("uniqueKey")){
					v = new Vector();
					List<Term> vectorTerme = new ArrayList<>();
					v.setModulName(docVector.getVal(j).toString());
					v.setTerms(vectorTerme);
				}else{
					NamedList<?> index = (NamedList<?>) docVector.get("content");
					for (int k = 0; k < index.size(); k++)
					{
						Term t = new Term();
						t.setWord(index.getName(k));
						String[] values = index.getVal(k).toString().replaceAll("[{}]", "").split(",");
						t.setTermFrequency(Double.valueOf(values[0].split("=")[1]));
						t.setDocFrequency(Double.valueOf(values[1].split("=")[1]));
						t.setInverseTermFrequency(Double.valueOf(values[2].split("=")[1]));
						v.getTerms().add(t);
					}	
					vectorList.add(v);
				}
			}
		}
		solrClient.close();
		return vectorList;
	}

	/**
	 * Providing every Modultext of the given Catalog to Solr and indexing the Modultext as a sperate Document.
	 * @param moduls
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void indexing(List<Modul> moduls) throws SolrServerException, IOException{	
		
		solrClient = new HttpSolrClient(ApplicationProperties.getInstance().getApplicationProperty((ApplicationPropertiesKeys.SOLRURLSTRING)));
		for(Modul x : moduls){
			SolrInputDocument input = new SolrInputDocument();		
			if(x.getCleanText()!=null & x.getUniversityName()!=null & x.getModulName()!=null){
				input.setField("id",x.getUniversityName()+x.getModulName());
				input.setField("content", x.getCleanText());
				UpdateResponse updateResponse = solrClient.add(input);;
				solrClient.commit();
				NamedList<?> header = updateResponse.getResponseHeader();
				
				for(int i=0; i<header.size(); i++){
					if(header.getName(i).equals("status")){
						if(header.getVal(i)!= Integer.valueOf(0)){
							System.out.println("Error while indexing Modul: "+x.getUniversityName()+"-"+x.getModulName());
						}
					}	
				}
				
			}else{
				System.out.println("No INDEX!");
			}
		}
		solrClient.close();
	}
	
	/**
	 * Adding the IndexVector to every Modul of the given List of Moduls.
	 * @param moduls
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void getIndex(List<Modul> moduls) throws SolrServerException, IOException{
		
		solrClient = new HttpSolrClient(ApplicationProperties.getInstance().getApplicationProperty((ApplicationPropertiesKeys.SOLRURLSTRING)));
		solrQuery = new SolrQuery();
		solrQuery.setRequestHandler("/tvrh?");
		solrQuery.setParam("fl", "content");
		solrQuery.setParam("q", "content:* TO *");
		solrQuery.setParam("tv.tf", "true");
		solrQuery.setParam("tv.df", "true");
		solrQuery.setParam("tv.tf_idf", "true");
		solrQuery.setRows(Integer.MAX_VALUE);
		solrQuery.setTerms(true);
		
		QueryResponse response = solrClient.query(solrQuery);

		NamedList<Object> first = response.getResponse();
		Vector v = null;
		List<Vector> vectorList = new ArrayList<>();
		NamedList<?> termvectors = (NamedList<?>) first.get("termVectors");
		for (int i = 0; i < termvectors.size(); i++)
		{
			NamedList<?> docVector = (NamedList<?>) termvectors.getVal(i);
			for (int j = 0; j < docVector.size(); j++)
			{
				if(docVector.getName(j).equals("uniqueKey")){
					v = new Vector();
					List<Term> vectorTerme = new ArrayList<>();
					v.setModulName(docVector.getVal(j).toString());
					v.setTerms(vectorTerme);
				}else{
					NamedList<?> index = (NamedList<?>) docVector.get("content");
					for (int k = 0; k < index.size(); k++)
					{
						Term t = new Term();
						t.setWord(index.getName(k));
						String[] values = index.getVal(k).toString().replaceAll("[{}]", "").split(",");
						t.setTermFrequency(Double.valueOf(values[0].split("=")[1]));
						t.setDocFrequency(Double.valueOf(values[1].split("=")[1]));
						t.setInverseTermFrequency(Double.valueOf(values[2].split("=")[1]));
						v.getTerms().add(t);
					}	
					vectorList.add(v);
				}
			}
		}		

		for (Modul m : moduls)
		{
			for(int h = 0; h<vectorList.size(); h++ )
			{
				if((m.getUniversityName()+m.getModulName()).trim().equals(vectorList.get(h).getModulName().trim()))
				{
					m.setDocumentVector(vectorList.get(h));
				}
			}		
		}
		solrClient.close();
	}
	
	/**
	 * If the result is unsatisfied, it has to be possible to delete the indexed moduls.
	 * IT I STILL TODO
	 * @throws IOException
	 * @throws SolrServerException 
	 */
	public void deleteIndex(List<Modul> moduls) throws IOException, SolrServerException{	
		solrClient = new HttpSolrClient(ApplicationProperties.getInstance().getApplicationProperty((ApplicationPropertiesKeys.SOLRURLSTRING)));	
		List<String> ids = new ArrayList<>();
		for(Modul m : moduls){
			String id = m.getUniversityName()+m.getModulName();
			ids.add(id);
		}
		UpdateResponse updateResponse = solrClient.deleteById(ids, 1000);
		if(updateResponse.getStatus()!=0){
			System.out.println("Error deleting Index for University:"+updateResponse.getStatus());
		}
		solrClient.close();
	}

}
