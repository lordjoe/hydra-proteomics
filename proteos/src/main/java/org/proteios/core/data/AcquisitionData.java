/*
 $Id: AcquisitionData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This represents an acquisition. An acquisition item has information about a
 * scan in a mass spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.Peak
 * @see <a
 *      href="../../../../../../../development/overview/data/acquisition.html">Acquisitions
 *      overview</a>
 * @proteios.modified $Date: 2006-06-07 14:31:54Z $
 * @hibernate.class table="`Acquisitions`" lazy="true"
 */
public class AcquisitionData
		extends CommonData
{
	public AcquisitionData()
	{}

	// -------------------------------------------
	/**
	 * The {@link InstrumentConfigurationData} item acquisitionInstrumentInfo
	 * contains required instrument configuration information. The variable is
	 * set to an instance of a class extending
	 * {@link InstrumentConfigurationData}, e.g.
	 * {@link HardwareConfigurationData} or {@link SoftwareConfigurationData}.
	 */
	private InstrumentConfigurationData acquisitionInstrumentInfo;


	/**
	 * Get the acquisitionInstrumentInfo
	 * 
	 * @hibernate.many-to-one column="`acquisitionInstrumentInfo_id`"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link InstrumentConfigurationData} acquisitionInstrumentInfo
	 */
	public InstrumentConfigurationData getAcquisitionInstrumentInfo()
	{
		return this.acquisitionInstrumentInfo;
	}


	/**
	 * Set the acquisitionInstrumentInfo
	 * 
	 * @param acquisitionInstrumentInfo The {@link InstrumentConfigurationData}
	 *        acquisitionInstrumentInfo
	 */
	public void setAcquisitionInstrumentInfo(
			InstrumentConfigurationData acquisitionInstrumentInfo)
	{
		this.acquisitionInstrumentInfo = acquisitionInstrumentInfo;
	}

	/**
	 * The acquisition number.
	 */
	private int acqNumber;


	/**
	 * Get the acquisition number.
	 * 
	 * @hibernate.property column="`acq_number`" type="int" not-null="false"
	 * @return the acquisition number value
	 */
	public int getAcqNumber()
	{
		return acqNumber;
	}


	/**
	 * Set the acquisition number
	 * 
	 * @param acqNumber The int acquisition number value
	 */
	public void setAcqNumber(int acqNumber)
	{
		this.acqNumber = acqNumber;
	}

	/**
	 * The creator PeakList item.
	 */
	private PeakListData peakList;


	/**
	 * Get the {@link PeakListData} this acquisition is created from.
	 * 
	 * @hibernate.many-to-one column="`peakListId`" not-null="false" outer-join="false"
	 * @return The <code>PeakListData</code> item or null if not known
	 */
	public PeakListData getPeakList()
	{
		return peakList;
	}


	/**
	 * Set the {@link PeakListData} this acquisition is created from.
	 * 
	 * @param peakList The {@link PeakListData} creator peakList
	 */
	public void setPeakList(PeakListData peakList)
	{
		this.peakList = peakList;
	}
}
