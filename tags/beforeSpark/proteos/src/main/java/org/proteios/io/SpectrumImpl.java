/*
 $Id: SpectrumImpl.java 3365 2009-07-15 10:31:32Z olle $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios-2.x is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 Proteios is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 */
package org.proteios.io;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the SpectrumInterface that defines methods to access an
 * array of mass or intensity values for mass spectrometry peak lists.
 * 
 * @author olle
 */
public class SpectrumImpl
		implements SpectrumInterface
{

    private static Class<? extends SpectrumImpl> gSpectrumClass;

    public static Class<? extends SpectrumImpl> getSpectrumClass() {
        return gSpectrumClass;
    }

    /**
     * should have a null constructor - might need to be public
     * @param pSpectrunClass
     */
    public static void setSpectrumClass(final Class<? extends SpectrumImpl> pSpectrunClass) {
        gSpectrumClass = pSpectrunClass;
    }

    /**
     * replaced the public constructor with this factory method allowing substitution of another class
     * slewis
     * @return
     */
    public static SpectrumImpl buildSpectrum()
    {
        Class spectrumClass =  getSpectrumClass();
        if(spectrumClass == null)
             return new SpectrumImpl();
        try {
            return (SpectrumImpl)spectrumClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
    }
	/*
	 * Spectrum Data
	 */
	private double[] dataMassArray;
	private double[] dataIntensityArray;
	private Double retentionTimeInMinutes = null;
	private List<SpectrumPrecursor> precursors = null;
	private List<StringPairInterface> extraDataList = null;

    // added slewis to track spectrum
    // made protected 
    protected SpectrumImpl() {
    }

    /**
	 * Set the mass array.
	 * 
	 * @param dataMassArray double[] array of mass values to set
	 */
	public void setMass(double[] dataMassArray)
	{
		this.dataMassArray = dataMassArray;
	}


	/**
	 * Set the intensity array.
	 * 
	 * @param dataIntensityArray double[] array of intensity values to set
	 */
	public void setIntensities(double[] dataIntensityArray)
	{
		this.dataIntensityArray = dataIntensityArray;
	}


	/**
	 * Set the retention time in minutes.
	 * 
	 * @param retentionTimeInMinutes Double
	 */
	public void setRetentionTimeInMinutes(Double retentionTimeInMinutes)
	{
		this.retentionTimeInMinutes = retentionTimeInMinutes;
	}


	public void setPrecursors(List<SpectrumPrecursor> precursors)
	{
		this.precursors = precursors;
	}


	public void addPrecursor(SpectrumPrecursor precursor)
	{
		if (precursors == null)
		{
			precursors = new ArrayList<SpectrumPrecursor>();
		}
		precursors.add(precursor);
	}


	/**
	 * Set the spectrum extra data list.
	 * 
	 * @param extraDataList List<StringPairInterface> The spectrum extra data list to set.
	 */
	public void setExtraDataList(List<StringPairInterface> extraDataList)
	{
		this.extraDataList = extraDataList;
	}


	/**
	 * Add spectrum extra data.
	 * 
	 * @param extraData StringPairInterface The spectrum extra data to add.
	 */
	public void addExtraData(StringPairInterface extraData)
	{
		if (this.extraDataList == null)
		{
			this.extraDataList = new ArrayList<StringPairInterface>();
		}
		this.extraDataList.add(extraData);
	}


	// -------------------------------------------
	/*
	 * From the SpectrumInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get the mass array.
	 * 
	 * @return dataMassArray double[] array of mass values
	 */
	public double[] listMass()
	{
		return this.dataMassArray;
	}


	/**
	 * Get the intensity array.
	 * 
	 * @return dataIntensityArray double[] array of intensity values
	 */
	public double[] listIntensities()
	{
		return this.dataIntensityArray;
	}


	/**
	 * Get the retention time in minutes.
	 * 
	 * @return retentionTimeInMinutes Double
	 */
	public Double getRetentionTimeInMinutes()
	{
		return this.retentionTimeInMinutes;
	}


	public List<SpectrumPrecursor> getPrecursors()
	{
		return this.precursors;
	}


	/**
	 * Get the spectrum extra data list.
	 * 
	 * @return List<StringPairInterface> The spectrum extra data list.
	 */
	public List<StringPairInterface> getExtraDataList()
	{
		return this.extraDataList;
	}
}
