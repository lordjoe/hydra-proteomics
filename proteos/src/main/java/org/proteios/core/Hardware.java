/*
 $Id: Hardware.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.HardwareData;

/**
 * This class is used to represent individual hardware items and information
 * about them. In the current version of Proteios the only type of hardware we
 * keep information about is scanners.
 * 
 * @author enell
 * @version 2.0
 * @see HardwareType HardwareType
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class Hardware
		extends CommonItem<HardwareData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#HARDWARE
	 * @see #getType()
	 */
	public static final Item TYPE = Item.HARDWARE;
	/**
	 * The maximum length of the version variable that can be stored in the
	 * database. Check the length against this value before calling the
	 * {@link #setVersionString(String)} method to avoid exceptions.
	 */
	public static final int MAX_VERSIONSTRING_LENGTH = HardwareData.MAX_VERSIONSTRING_LENGTH;


	/**
	 * Get a query configured to retrieve hardware.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public static ItemQuery<Hardware> getQuery()
			throws BaseException
	{
		return new ItemQuery<Hardware>(Hardware.class);
	}


	/**
	 * Creates a new hardware item.
	 * 
	 * @param data the data
	 */
	Hardware(HardwareData data)
	{
		super(data);
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
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check if:
	 * <ul>
	 * <li>a Scan is linked to this hardware
	 * </ul>
	 * 
	 * @return TRUE if this hardware is used.
	 * @throws BaseException if there is any error.
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		boolean used = false;
		return used;
	}


	// -------------------------------------------
	/**
	 * Get the associated {@link HardwareType} item. All <code>Hardware</code>
	 * items must have a type.
	 * 
	 * @return The <code>HardwareType</code> item
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#READ } permission to the item
	 * @throws BaseException If there is any error
	 */
	public HardwareType getHardwareType()
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(HardwareType.class,
//			getData().getHardwareType());
	}


	/**
	 * Set the {@link HardwareType} of this <code>Hardware</code> item. This
	 * parameter mustn't be <code>null</code>.
	 * 
	 * @param hardwareType The new <code>HardwareType</code>
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#WRITE} permission for the hardware or
	 *         {@link Permission#USE} permission for the hardware type
	 * @throws InvalidUseOfNullException If the hardware type is null
	 */
	public void setHardwareType(HardwareType hardwareType)
			throws PermissionDeniedException, InvalidUseOfNullException
	{
		checkPermission(Permission.WRITE);
		if (hardwareType == null)
			throw new InvalidUseOfNullException("hardwareType");
		hardwareType.checkPermission(Permission.USE);
		getData().setHardwareType(hardwareType.getData());
	}


	/**
	 * Get the versionstring of this <code>Hardware</code> item.
	 * 
	 * @return A string with the version of this item, or null if not known
	 */
	public String getVersionString()
	{
		return getData().getVersionString();
	}


	/**
	 * Set the versionstring for this <code>Hardware</code> item. The value
	 * must not be longer than the value specified by the
	 * {@link #MAX_VERSIONSTRING_LENGTH} constant.
	 * 
	 * @param versionString The new version for this item, or null if not known
	 * @throws StringTooLongException If versionString is longer then
	 *         {@link #MAX_VERSIONSTRING_LENGTH}
	 * @throws PermissionDeniedException If the logged in user don't have write
	 *         permission on this item
	 */
	public void setVersionString(String versionString)
			throws StringTooLongException, PermissionDeniedException
	{
		checkPermission(Permission.WRITE);
		getData().setVersionString(
			StringUtil.setNullableString(versionString, "versionString",
				MAX_VERSIONSTRING_LENGTH));
	}
}
