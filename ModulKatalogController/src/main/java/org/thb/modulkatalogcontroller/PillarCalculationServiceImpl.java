package org.thb.modulkatalogcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thb.modulkatalogcontroller.model.ModulDTO;
import org.thb.modulkatalogcontroller.rest.IPillarCalculationService;

/**
 * The PillarCalculationServiceImpl is the Implementation of the
 * CalculationService for calculate the final Pillar results.
 * 
 * @author ManuelRaddatz
 *
 */
public class PillarCalculationServiceImpl implements IPillarCalculationService
{

	@Override
	public Map<String, Double> getPillar(List<ModulDTO> moduls)
	{
		Map<String, Double> result = new HashMap<>();

		Double infPillar = 0.0;
		Double bwlPillar = 0.0;
		Double wiPillar = 0.0;
		Double nnPillar = 0.0;

		double sumECTS = 0.0;

		if (moduls != null | !moduls.isEmpty())
		{
			for (ModulDTO m : moduls)
			{
				sumECTS += m.getEcts();
			}

			for (ModulDTO m : moduls)
			{
				infPillar += (m.getInfScoreNormalized() * (m.getEcts() / sumECTS));
				bwlPillar += (m.getBwlScoreNormalized() * (m.getEcts() / sumECTS));
				wiPillar += (m.getWiScoreNormalized() * (m.getEcts() / sumECTS));
				nnPillar += (m.getNnScoreNormalized() * (m.getEcts() / sumECTS));
			}
			result.put("inf", round(infPillar, 3));
			result.put("bwl", round(bwlPillar, 3));
			result.put("wi", round(wiPillar, 3));
			result.put("nn", round(nnPillar, 3));
		} else
		{
			result.put("inf", 0.0);
			result.put("bwl", 0.0);
			result.put("wi", 0.0);
			result.put("nn", 0.0);
		}
		return result;
	}

	private static Double round(double pillar, int place)
	{
		double rounded = Math.round(pillar * Math.pow(10d, place));
		return rounded / Math.pow(10d, place);
	}

}
