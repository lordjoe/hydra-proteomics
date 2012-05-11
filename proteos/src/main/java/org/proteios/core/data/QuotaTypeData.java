/*
  $Id: QuotaTypeData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information about different QuotaTypes.
	
	@author enell
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/quota.html">Quota overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`QuotaTypes`" lazy="false"
*/
public class QuotaTypeData
	extends BasicData
	implements NameableData, SystemData
{
	
	public QuotaTypeData()
	{}
	
	/*
		From the Nameable interface
		-------------------------------------------
	*/
	private String name;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	private String description;
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	// -------------------------------------------
	
	/*
		From the SystemData interface
		-------------------------------------------
	*/
	private String systemId;
	public String getSystemId()
	{
		return systemId;
	}
	public void setSystemId(String systemId)
	{
		this.systemId = systemId;
	}
	// -------------------------------------------

	private boolean secondaryLocation;
	/**
		If this quota type can use disk quota in the secondary location or not.
		
		@hibernate.property column="`secondary_location`" type="boolean" not-null="true" update="false"
	*/
	public boolean getSecondaryLocation()
	{
		return secondaryLocation;
	}
	public void setSecondaryLocation(boolean secondaryLocation)
	{
		this.secondaryLocation = secondaryLocation;
	}
	
}
