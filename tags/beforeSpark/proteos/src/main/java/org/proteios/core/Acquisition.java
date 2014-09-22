/*
 $Id: Acquisition.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

import org.proteios.core.data.AcquisitionData;
import org.proteios.core.data.InstrumentConfigurationData;

/**
 * This class represent acquisition items. An acquisition item has information
 * about a scan in a mass spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-06-02 12:33:12Z $
 */
public class Acquisition
		extends CommonItem<AcquisitionData>
{
	/**
	 * @param commonData
	 */
	Acquisition(AcquisitionData commonData)
	{
		super(commonData);
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_ACQUISITION
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_ACQUISITION;


	/**
	 * Get a query that returns acquisition items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<Acquisition> getQuery()
	{
		return new ItemQuery<Acquisition>(Acquisition.class);
	}


	// -------------------------------------------
	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this acquisition
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * The {@link InstrumentConfiguration} item acquisitionInstrumentInfo
	 * contains required instrument configuration information. The variable is
	 * set to an instance of a class extending {@link InstrumentConfiguration},
	 * e.g. {@link HardwareConfiguration} or {@link SoftwareConfiguration}.
	 */
	/**
	 * Get the {@link InstrumentConfiguration} acquisitionInstrumentInfo.
	 * 
	 * @return An <code>InstrumentConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public InstrumentConfiguration getAcquisitionInstrumentInfo()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(InstrumentConfiguration.class,
//			getData().getAcquisitionInstrumentInfo());
	}


	/**
	 * Set the {@link InstrumentConfiguration} acquisitionInstrumentInfo.
	 * 
	 * @param acquisitionInstrumentInfo The new
	 *        <code>InstrumentConfiguration</code> item
	 * @throws InvalidDataException If the acquisitionInstrumentInfo is null
	 * @throws BaseException If there is another error
	 */
	public void setAcquisitionInstrumentInfo(
			InstrumentConfiguration acquisitionInstrumentInfo)
			throws InvalidDataException
	{
		if (acquisitionInstrumentInfo == null)
			throw new InvalidUseOfNullException("acquisitionInstrumentInfo");
		getData().setAcquisitionInstrumentInfo(
			(InstrumentConfigurationData) acquisitionInstrumentInfo.getData());
	}


	/**
	 * Get the acquisition number.
	 * 
	 * @return An <code>int</code> holding the acquisition number
	 */
	public int getAcqNumber()
	{
		return getData().getAcqNumber();
	}


	/**
	 * Set the acquisition number
	 * 
	 * @param acqNumber The int acquisition number value
	 */
	public void setAcqNumber(int acqNumber)
	{
		getData().setAcqNumber(acqNumber);
	}


	/**
	 * The creator PeakList item. The variable is set to an instance of class
	 * {@link PeakList}.
	 */
	/**
	 * Get the {@link PeakList} this acquisition is created from.
	 * 
	 * @return The <code>PeakList</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public PeakList getPeakList()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(PeakList.class, getData().getPeakList());
	}


	/**
	 * Set the {@link PeakList} this acquisition is created from.
	 * 
	 * @param peakList The new <code>PeakList</code> item
	 * @throws BaseException If there is another error
	 */
	public void setPeakList(PeakList peakList)
	{
		getData().setPeakList(peakList.getData());
	}
}
