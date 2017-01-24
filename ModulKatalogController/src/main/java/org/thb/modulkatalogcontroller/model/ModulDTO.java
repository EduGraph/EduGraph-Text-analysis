package org.thb.modulkatalogcontroller.model;

/**
 * This Class represents the Modul from a DatabaseQuery to Calculate the PillarValues
 * @author Manuel
 *
 */
public class ModulDTO
{
	
	private int ects;
	
	private double infScoreNormalized;
	
	private double bwlScoreNormalized;
	
	private double wiScoreNormalized;
	
	private double nnScoreNormalized;

	public ModulDTO()
	{
		super();
	}
	
	public ModulDTO(Modul modul)
	{
		this(modul.getEcts(), modul.getInfScoreNormalized(), modul.getBwlScoreNormalized(), modul.getWiScoreNormalized(), modul.getNnScoreNormalized());
	}

	public ModulDTO(int ects, double infScoreNormalized, double bwlScoreNormalized, double wiScoreNormalized,
			double nnScoreNormalized)
	{
		super();
		this.ects = ects;
		this.infScoreNormalized = infScoreNormalized;
		this.bwlScoreNormalized = bwlScoreNormalized;
		this.wiScoreNormalized = wiScoreNormalized;
		this.nnScoreNormalized = nnScoreNormalized;
	}

	/**
	 * @return the ects
	 */
	public int getEcts()
	{
		return ects;
	}

	/**
	 * @param ects the ects to set
	 */
	public void setEcts(int ects)
	{
		this.ects = ects;
	}

	/**
	 * @return the infScoreNormalized
	 */
	public double getInfScoreNormalized()
	{
		return infScoreNormalized;
	}

	/**
	 * @param infScoreNormalized the infScoreNormalized to set
	 */
	public void setInfScoreNormalized(double infScoreNormalized)
	{
		this.infScoreNormalized = infScoreNormalized;
	}

	/**
	 * @return the bwlScoreNormalized
	 */
	public double getBwlScoreNormalized()
	{
		return bwlScoreNormalized;
	}

	/**
	 * @param bwlScoreNormalized the bwlScoreNormalized to set
	 */
	public void setBwlScoreNormalized(double bwlScoreNormalized)
	{
		this.bwlScoreNormalized = bwlScoreNormalized;
	}

	/**
	 * @return the wiScoreNormalized
	 */
	public double getWiScoreNormalized()
	{
		return wiScoreNormalized;
	}

	/**
	 * @param wiScoreNormalized the wiScoreNormalized to set
	 */
	public void setWiScoreNormalized(double wiScoreNormalized)
	{
		this.wiScoreNormalized = wiScoreNormalized;
	}

	/**
	 * @return the nnScoreNormalized
	 */
	public double getNnScoreNormalized()
	{
		return nnScoreNormalized;
	}

	/**
	 * @param nnScoreNormalized the nnScoreNormalized to set
	 */
	public void setNnScoreNormalized(double nnScoreNormalized)
	{
		this.nnScoreNormalized = nnScoreNormalized;
	}

	@Override
	public String toString()
	{
		return "ects=" + ects + ", infScoreNormalized=" + infScoreNormalized + ", bwlScoreNormalized="
				+ bwlScoreNormalized + ", wiScoreNormalized=" + wiScoreNormalized + ", nnScoreNormalized="
				+ nnScoreNormalized;
	}	
}
