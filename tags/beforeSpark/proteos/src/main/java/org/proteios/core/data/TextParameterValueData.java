/*
	$Id: TextParameterValueData.java 3207 2009-04-09 06:48:11Z gregory $

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
	Text parameter value.

	@author Samuel
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/parameters.html">Parameters overview</a>
	@hibernate.subclass discriminator-value="6"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class TextParameterValueData
	extends ParameterValueData<String>
{
	public TextParameterValueData()
	{}
	
	public TextParameterValueData(String... values)
	{
		super(values);
	}
	
	private List<String> values;
	/**
		@hibernate.bag table="`TextValues`" lazy="true" cascade="all"
		@hibernate.collection-key column="`id`"
		@hibernate.collection-element column="`value`" type="text" not-null="true"
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
