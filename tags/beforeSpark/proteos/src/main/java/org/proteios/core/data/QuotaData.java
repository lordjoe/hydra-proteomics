/*
  $Id: QuotaData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashMap;
import java.util.Map;

/**
	This file holds information about a qouta.
	
	@author enell
	@version 2.0
	@see org.proteios.core.Quota
	@see <a href="../../../../../../../development/overview/data/quota.html">Quota overview</a>
	@hibernate.class table="`Quota`" lazy="true"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class QuotaData
	extends BasicData
	implements RemovableData, NameableData, SystemData
{

	public QuotaData()
	{}
	
	/*
		From the RemovableData interface
		-------------------------------------------
	*/
	private boolean removed;
	public boolean isRemoved()
	{
		return removed;
	}
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
	// -------------------------------------------
	
	/*
		From the SystemItem interface
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

	/*
		From the NameableData interface
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

	private Map<QuotaIndex, Long> quotaValues;
	/**
		@hibernate.map table="`QuotaValues`" lazy="true"
		@hibernate.collection-key column="`quota_id`"
		@hibernate.collection-composite-index class="org.proteios.core.data.QuotaIndex"
		@hibernate.collection-element column="`max_bytes`" type="long" not-null="true"
	*/
	public Map<QuotaIndex, Long> getQuotaValues()
	{
		if(quotaValues == null)
		{
			quotaValues = new HashMap<QuotaIndex,Long>();
		}
		return quotaValues;
	}
	void setQuotaValues(Map<QuotaIndex, Long> quotaValues)
	{
		this.quotaValues = quotaValues;
	}
}
