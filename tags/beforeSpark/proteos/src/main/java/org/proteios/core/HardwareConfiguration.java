/*
 $Id: HardwareConfiguration.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.HardwareConfigurationData;
import java.util.Set;

/**
 * This class represent hardware configuration items. A hardware configuration
 * has information about the hardware used in an experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-05-31 12:33:12Z $
 */
public class HardwareConfiguration
		extends InstrumentConfiguration<HardwareConfigurationData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#HARDWARECONFIGURATION
	 * @see #getType()
	 */
	public static final Item TYPE = Item.HARDWARECONFIGURATION;


	/**
	 * Get a query that returns hardwareconfiguration items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<HardwareConfiguration> getQuery()
	{
		return new ItemQuery<HardwareConfiguration>(HardwareConfiguration.class);
	}


	HardwareConfiguration(HardwareConfigurationData hardwareConfigurationData)
	{
		super(hardwareConfigurationData);
	}


	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/**
	 * Always null.
	 */
	public Set<Annotatable> getAnnotatableParents()
			throws BaseException
	{
		return null;
	}


	/*
	 * From the BasicItem class
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this hardwareconfiguration
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the {@link Hardware} that is configured.
	 * 
	 * @return A <code>Hardware</code> object
	 * @throws BaseException If there is another error
	 */
	public Hardware getHardware()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
		//return getDbControl().getItem(Hardware.class, getData().getHardware());
	}


	/**
	 * Set the {@link Hardware} that is configured.
	 * 
	 * @param hardware The new <code>Hardware</code> item
	 * @throws InvalidDataException If the hardware is null
	 */
	public void setHardware(Hardware hardware)
			throws InvalidDataException
	{
		if (hardware == null)
			throw new InvalidUseOfNullException("hardware");
		getData().setHardware(hardware.getData());
	}
}
