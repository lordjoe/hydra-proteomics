/*
	$Id: StringParameterValueData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.ArrayList;
import java.util.List;

/**
	String parameter value. The maximum length of string values are 255.

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/parameters.html">Parameters overview</a>
	@hibernate.subclass discriminator-value="5"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class StringParameterValueData
	extends ParameterValueData<String>
{
	public StringParameterValueData()
	{}
	
	public StringParameterValueData(String... values)
	{
		super(values);
	}
	
	private List<String> values;
	/**
		@hibernate.bag table="`StringValues`" lazy="true" cascade="all"
		@hibernate.collection-key column="`id`"
		@hibernate.collection-element column="`value`" type="string" length="255" not-null="true"
	*/
	@Override
	public List<String> getValues()
	{
		if (values == null)
		{
			values = new ArrayList<String>();
		}
		return values;
	}
	@Override
	void setValues(List<String> values)
	{
		this.values = values;
	}

	@Override
	public String toString()
	{
		return values.toString();
	}	
}
