/*
 $Id: PrecursorSelectedIon.java 3277 2009-05-18 14:43:48Z olle $

 Copyright (C) 2009 Gregory Vincic

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
 * This class contains spectrum precursor selected ion information
 * in a spectrum file.
 * 
 * @author olle
 */
public class PrecursorSelectedIon
{
	/*
	 * Spectrum Data
	 */
	private Double MassToChargeRatio;
	private Double Intensity;
	private Integer Charge;
	private List<StringPairInterface> extraDataList = null;


	/**
	 * @return Returns the massToChargeRatio.
	 */
	public Double getMassToChargeRatio()
	{
		return MassToChargeRatio;
	}


	/**
	 * @param massToChargeRatio The massToChargeRatio to set.
	 */
	public void setMassToChargeRatio(Double massToChargeRatio)
	{
		MassToChargeRatio = massToChargeRatio;
	}


	/**
	 * @return Returns the intensity.
	 */
	public Double getIntensity()
	{
		return Intensity;
	}


	/**
	 * @param intensity The intensity to set.
	 */
	public void setIntensity(Double intensity)
	{
		Intensity = intensity;
	}


	/**
	 * @return Returns the charge.
	 */
	public Integer getCharge()
	{
		return Charge;
	}


	/**
	 * @param charge The charge to set.
	 */
	public void setCharge(Integer charge)
	{
		Charge = charge;
	}


	/**
	 * Get the spectrum precursor selected ion extra data list.
	 * 
	 * @return List<StringPairInterface> The spectrum precursor selected ion extra data list.
	 */
	public List<StringPairInterface> getExtraDataList()
	{
		return this.extraDataList;
	}


	/**
	 * Add spectrum precursor selected ion extra data.
	 * 
	 * @param extraData StringPairInterface The spectrum precursor selected ion extra data to add.
	 */
	public void addExtraData(StringPairInterface extraData)
	{
		if (this.extraDataList == null)
		{
			this.extraDataList = new ArrayList<StringPairInterface>();
		}
		this.extraDataList.add(extraData);
	}
}
