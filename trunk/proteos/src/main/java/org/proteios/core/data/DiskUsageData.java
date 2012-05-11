/*
 $Id: DiskUsageData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This class holds information about a users and/or groups disk usage.
 * 
 * @author enell
 * @version 2.0
 * @see org.proteios.core.DiskUsage
 * @see <a
 *      href="../../../../../../../development/overview/data/quota.html">Quota
 *      overview</a>
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`DiskUsage`" lazy="true"
 */
public class DiskUsageData
		extends BasicData
{
	/**
	 * Creates a new DiskUsageData.
	 */
	public DiskUsageData()
	{}

	private int location;


	/**
	 * Get the location of the {@link DiskConsumable} item.
	 * 
	 * @see org.proteios.core.Location
	 * @hibernate.property column="`location`" type="int" not-null="true"
	 */
	public int getLocation()
	{
		return location;
	}


	public void setLocation(int location)
	{
		this.location = location;
	}

	private long bytes;


	/**
	 * Get the number of bytes the {@link DiskConsumable} item is using.
	 * 
	 * @hibernate.property column="`bytes`" type="long" not-null="true"
	 */
	public long getBytes()
	{
		return bytes;
	}


	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	private QuotaTypeData quotaType;


	/**
	 * Get the {@link QuotaTypeData} for this DiskUsageData.
	 * 
	 * @hibernate.many-to-one column="`quotatype_id`" not-null="true"
	 *                        outer-join="false"
	 */
	public QuotaTypeData getQuotaType()
	{
		return quotaType;
	}


	public void setQuotaType(QuotaTypeData quotaType)
	{
		this.quotaType = quotaType;
	}

	private GroupData group;


	/**
	 * Get the {@link GroupData} that owns the {@link DiskConsumable} item.
	 * 
	 * @hibernate.many-to-one column="`group_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public GroupData getGroup()
	{
		return group;
	}


	public void setGroup(GroupData group)
	{
		this.group = group;
	}

	private UserData user;


	/**
	 * Get the user that owns the {@link DiskConsumable} item.
	 * 
	 * @hibernate.many-to-one column="`user_id`" not-null="true"
	 *                        outer-join="false"
	 */
	public UserData getUser()
	{
		return user;
	}


	public void setUser(UserData user)
	{
		this.user = user;
	}
}
