/*
	$Id: QuotaIndex.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.io.Serializable;

/**
	This is a helper class for the {@link QuotaData} item to
	help with the Hibernate mapping between quota and
	quota type/location/max bytes. This class holds quota type/location
	pairs used as keys in the {@link QuotaData#getQuotaValues()}
	map.

	@author enell
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/quota.html">Quota overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public class QuotaIndex
	implements Serializable
{
	QuotaIndex()
	{}
	
	public QuotaIndex(QuotaTypeData quotaType, int location)
	{
		this.quotaType = quotaType;
		this.location = location;
	}

	private int location;

	/**
	 	@hibernate.property column="`location`" type="int"
	*/
	public int getLocation()
	{
		return location;
	}

	void setLocation(int location)
	{
		this.location = location;
	}

	private QuotaTypeData quotaType;

	/**
		@hibernate.many-to-one column="`quotaType_id`" outer-join="false"
	*/
	public QuotaTypeData getQuotaType()
	{
		return quotaType;
	}

	void setQuotaType(QuotaTypeData quotaType)
	{
		this.quotaType = quotaType;
	}

	/**
		Check if this object is equal to another <code>QuotaIndex</code>
		object. They are equal if both have the same quotatype id and location.
	*/
	@Override
	public boolean equals(Object o)
	{
		if ((o == null) || (getClass() != o.getClass()))
		{
			return false;
		}
		QuotaIndex qi = (QuotaIndex) o;
		return (this.quotaType.getId() == quotaType.getId()) && (this.location == qi.location);
	}

	/**
		Calculate the hash code for the object.
	*/
	@Override
	public int hashCode()
	{
		return (53 * quotaType.getId() + 17 * location);
	}
}
