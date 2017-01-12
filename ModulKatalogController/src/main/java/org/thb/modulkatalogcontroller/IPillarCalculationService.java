package org.thb.modulkatalogcontroller;

import java.util.List;
import java.util.Map;

import org.thb.modulkatalogcontroller.model.ModulDTO;

public interface IPillarCalculationService
{
	
	public Map<String, Double> getPillar(List<ModulDTO> moduls); 

}
