package org.thb.modulkatalogcontroller;

import javax.inject.Named;
import java.io.Serializable;


@Named
@javax.enterprise.context.SessionScoped
public class IndexPageController implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String goToCreationPage(){
		return Pages.CONTROLFORM;
	}
	
	public String goToViewPage(){
		return Pages.VIEWPAGE;
	}
	
	public String goToTestProgressBarPage(){
		return Pages.TESTPROGRESSBAR;
	}
}
