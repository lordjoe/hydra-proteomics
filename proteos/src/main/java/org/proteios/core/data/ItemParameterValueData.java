/*
	$Id: ItemParameterValueData.java 3207 2009-04-09 06:48:11Z gregory $

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
	Item parameter value. The <code>Proteios</code> system expect that the values are
	of the type {@link org.proteios.core.BasicItem}. Internally the objects will 
	be converted to {@link BasicData} objects.
	
	@author Nicklas, Samuel
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/parameters.html">Parameters overview</a>
	@hibernate.subclass discriminator-value="10"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class ItemParameterValueData
	extends ParameterValueData<BasicData>
{
	public ItemParameterValueData()
	{}

	public ItemParameterValueData(BasicData... values)
	{
		super(values);
	}

	private List<BasicData> values;
	/**
		@hibernate.bag table="`ItemValues`" lazy="true"
		@hibernate.collection-key column="`id`"
		@hibernate.many-to-any id-type="int" meta-type="string"
		@hibernate.many-to-any-column name="`data_class`" not-null="true" length="255"
		@hibernate.many-to-any-column name="`data_class_id`" not-null="true"
	*/
	@Override
	public List<BasicData> getValues()
	{
		if (values == null)
		{
			values = new ArrayList<BasicData>();
		}
		return values;
	}
	@Override
	void setValues(List<BasicData> values)
	{
		this.values = values;
	}

}
