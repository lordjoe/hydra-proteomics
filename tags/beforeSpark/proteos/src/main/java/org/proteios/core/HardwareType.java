/*
 $Id: HardwareType.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

import org.proteios.core.data.HardwareTypeData;

/**
 * This class is used to represent the type of {@link Hardware Hardware} items
 * in Proteios. It is not possible for client applications to create new
 * hardware types or modify existing ones.
 * 
 * @author enell
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @see Hardware Hardware
 */
public class HardwareType
		extends BasicItem<HardwareTypeData>
		implements Nameable, SystemItem
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#FILETYPE ITEM.FILETYPE
	 * @see #getType() getType()
	 */
	public static final Item TYPE = Item.HARDWARETYPE;
	/**
	 * The id for the <code>HardwareType</code> object representing a scanner.
	 */
	public static final String SCANNER = "org.proteios.core.HardwareType.SCANNER";
	/**
	 * The id for the <code>HardwareType</code> object representing a robot.
	 */
	public static final String ROBOT = "org.proteios.core.HardwareType.ROBOT";
	/**
	 * The id for the <code>HardwareType</code> object representing a robot.
	 */
	public static final String MASS_SPECTROMETER = "org.proteios.core.HardwareType.MASS_SPECTROMETER";


	/**
	 * Get a query configured to retrieve hardware types.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public static ItemQuery<HardwareType> getQuery()
	{
		return new ItemQuery<HardwareType>(HardwareType.class);
	}


	/**
	 * Creates a hardwaretype item.
	 * 
	 * @param data the data
	 */
	HardwareType(HardwareTypeData data)
	{
		super(data);
	}


	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check if:
	 * <ul>
	 * <li>A Hardware of this type exists
	 * </ul>
	 * 
	 * @return TRUE if the hardwaretype is used.
	 * @throws BaseException If there is an error.
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"COUNT_HARDWARE_FOR_TYPE");
//		/*
//		 * SELECT count(*) FROM Hardware hw WHERE hw.hardwareType =
//		 * :hardwaretype
//		 */
//		query.setEntity("hardwaretype", this.getData());
//		return HibernateUtil.loadData(Integer.class, query) > 0;
	}


	/**
	 * If this is a system hardware type, delete and create permissions are
	 * revoked.
	 */
	@Override
	void initPermissions(int granted, int denied)
			throws BaseException
	{
		if (isSystemItem())
		{
			denied |= Permission.deny(Permission.DELETE, Permission.CREATE);
		}
		super.initPermissions(granted, denied);
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
	 * From the SystemItem interface -------------------------------------------
	 */
	public String getSystemId()
	{
		return getData().getSystemId();
	}


	public boolean isSystemItem()
	{
		return getSystemId() != null;
	}


	// -------------------------------------------
	/*
	 * From the Nameable interface -------------------------------------------
	 */
	public String getName()
	{
		return getData().getName();
	}


	public void setName(String name)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		NameableUtil.setName(getData(), name);
	}


	public String getDescription()
	{
		return getData().getDescription();
	}


	public void setDescription(String description)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		NameableUtil.setDescription(getData(), description);
	}
}
