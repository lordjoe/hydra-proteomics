/*
 $Id: PeakData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Gregory Vincic

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Proteios is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 02111-1307, USA.
 */
package org.proteios.core.data;

/**
 * This represents a peak. A peak item has information about a peak in a mass
 * spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.Peak
 * @see <a href="../../../../../../../development/overview/data/peak.html">Peaks
 *      overview</a>
 * @proteios.modified $Date: 2006-06-02 14:31:54Z $
 * @hibernate.class table="`Peaks`" lazy="true"
 */
public class PeakData
		extends BasicData
		implements BatchableData, AnnotatableData
{
	public PeakData()
	{}

	// -------------------------------------------
	/**
	 * The massToChargeRatio.
	 */
	private Double massToChargeRatio;


	/**
	 * Get the massToChargeRatio.
	 * 
	 * @hibernate.property column="`mass_to_charge_ratio`" type="double"
	 *                     not-null="false"
	 * @return the massToChargeRatio value
	 */
	public Double getMassToChargeRatio()
	{
		return massToChargeRatio;
	}


	/**
	 * Set the massToChargeRatio
	 * 
	 * @param massToChargeRatio The Double massToChargeRatio value
	 */
	public void setMassToChargeRatio(Double massToChargeRatio)
	{
		this.massToChargeRatio = massToChargeRatio;
	}

	/**
	 * The intensity.
	 */
	private Double intensity;


	/**
	 * Get the intensity.
	 * 
	 * @hibernate.property column="`intensity`" type="double" not-null="false"
	 * @return the intensity value
	 */
	public Double getIntensity()
	{
		return intensity;
	}


	/**
	 * Set the intensity
	 * 
	 * @param intensity The Double intensity value
	 */
	public void setIntensity(Double intensity)
	{
		this.intensity = intensity;
	}

	/**
	 * The creator PeakList item.
	 */
	private PeakListData peakList;


	/**
	 * Get the {@link PeakListData} this peak is created from.
	 * 
	 * @hibernate.many-to-one column="`peakListId`" not-null="false"
	 *                        outer-join="false"
	 * @return The <code>PeakListData</code> item or null if not known
	 */
	public PeakListData getPeakList()
	{
		return peakList;
	}


	/**
	 * Set the {@link PeakListData} this peak is created from.
	 * 
	 * @param peakList The creator peakList
	 */
	public void setPeakList(PeakListData peakList)
	{
		this.peakList = peakList;
	}

	private AnnotationSetData annotationSet;


	public AnnotationSetData getAnnotationSet()
	{
		return annotationSet;
	}


	public void setAnnotationSet(AnnotationSetData annotationSet)
	{
		this.annotationSet = annotationSet;
	}
}
