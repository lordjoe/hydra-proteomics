/*
 $Id: PrecursorData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander, Gregory Vincic, Olle Mansson
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
 * This represents a precursor. A precursor item has information about the
 * precursor in a 2-D mass spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.Precursor
 * @see <a
 *      href="../../../../../../../development/overview/data/precursor.html">Precursors
 *      overview</a>
 * @proteios.modified $Date: 2006-06-02 14:31:54Z $
 * @hibernate.class table="`Precursors`" lazy="true"
 */
public class PrecursorData
		extends BasicData
{
	public PrecursorData()
	{}

	// -------------------------------------------
	/**
	 * The {@link InstrumentConfigurationData} item activation contains
	 * information on the configuration of the intrument used. The variable is
	 * set to an instance of a class extending
	 * {@link InstrumentConfigurationData}, e.g.
	 * {@link HardwareConfigurationData} or {@link SoftwareConfigurationData}.
	 */
	private InstrumentConfigurationData activation;


	/**
	 * Get the activation
	 * 
	 * @hibernate.many-to-one column="`activation_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link InstrumentConfigurationData} activation
	 */
	public InstrumentConfigurationData getActivation()
	{
		return this.activation;
	}


	/**
	 * Set the activation
	 * 
	 * @param activation The {@link InstrumentConfigurationData} activation
	 */
	public void setActivation(InstrumentConfigurationData activation)
	{
		this.activation = activation;
	}

	/**
	 * The chargeState value indicates the charge of the ion.
	 */
	private int chargeState = 1;


	/**
	 * Get the chargeState value
	 * 
	 * @hibernate.property column="`chargeState`" type="int" not-null="true"
	 * @return the chargeState value
	 */
	public int getChargeState()
	{
		return chargeState;
	}


	/**
	 * Set the chargeState
	 * 
	 * @param chargeState The chargeState value
	 */
	public void setChargeState(int chargeState)
	{
		this.chargeState = chargeState;
	}

	/**
	 * The {@link InstrumentConfigurationData} item ionSelection contains
	 * information on the configuration of the intrument used. The variable is
	 * set to an instance of a class extending
	 * {@link InstrumentConfigurationData}, e.g.
	 * {@link HardwareConfigurationData} or {@link SoftwareConfigurationData}.
	 */
	private InstrumentConfigurationData ionSelection;


	/**
	 * Get the ionSelection
	 * 
	 * @hibernate.many-to-one column="`ionSelection_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link InstrumentConfigurationData} ionSelection
	 */
	public InstrumentConfigurationData getIonSelection()
	{
		return this.ionSelection;
	}


	/**
	 * Set the ionSelection
	 * 
	 * @param ionSelection The {@link InstrumentConfigurationData} ionSelection
	 */
	public void setIonSelection(InstrumentConfigurationData ionSelection)
	{
		this.ionSelection = ionSelection;
	}

	/**
	 * The mass to charge ratio.
	 */
	private Float massToChargeRatio;


	/**
	 * Get the massToChargeRatio.
	 * 
	 * @hibernate.property column="`mass_to_charge_ratio`" type="float"
	 *                     not-null="false"
	 * @return the massToChargeRatio value
	 */
	public Float getMassToChargeRatio()
	{
		return massToChargeRatio;
	}


	/**
	 * Set the massToChargeRatio
	 * 
	 * @param massToChargeRatio The Float massToChargeRatio value
	 */
	public void setMassToChargeRatio(Float massToChargeRatio)
	{
		this.massToChargeRatio = massToChargeRatio;
	}

	/**
	 * The precursor intensity.
	 */
	private Float intensity;


	/**
	 * Get the precursor intensity.
	 * 
	 * @hibernate.property column="`intensity`" type="float"
	 *                     not-null="false"
	 * @return the intensity value
	 */
	public Float getIntensity()
	{
		return intensity;
	}


	/**
	 * Set the precursor intensity
	 * 
	 * @param intensity The Float intensity value
	 */
	public void setIntensity(Float intensity)
	{
		this.intensity = intensity;
	}

	/**
	 * The msLevel value indicates the level of the mass spectra data.
	 */
	private int msLevel = 1;


	/**
	 * Get the msLevel value
	 * 
	 * @hibernate.property column="`mslevel`" type="int" not-null="true"
	 * @return the msLevel value
	 */
	public int getMsLevel()
	{
		return msLevel;
	}


	/**
	 * Set the msLevel
	 * 
	 * @param msLevel The msLevel value
	 */
	public void setMsLevel(int msLevel)
	{
		this.msLevel = msLevel;
	}

	/**
	 * Spectrum Ref.
	 */
	private int spectrumRef;


	/**
	 * Get the spectrumRef value
	 * 
	 * @hibernate.property column="`spectrum_ref`" type="int" not-null="false"
	 * @return the spectrumRef value
	 */
	public int getSpectrumRef()
	{
		return spectrumRef;
	}


	/**
	 * Set the spectrumRef
	 * 
	 * @param spectrumRef The int spectrumRef value
	 */
	public void setSpectrumRef(int spectrumRef)
	{
		this.spectrumRef = spectrumRef;
	}

	/**
	 * The creator PeakList item.
	 */
	private PeakListData peakList;


	/**
	 * Get the {@link PeakListData} this precursor is created from.
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
	 * Set the {@link PeakListData} this precursor is created from. param
	 * 
	 * @param peakList The {@link PeakListData} creator peakList
	 */
	public void setPeakList(PeakListData peakList)
	{
		this.peakList = peakList;
	}
}
