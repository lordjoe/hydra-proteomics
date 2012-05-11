/*
	$Id: ParameterValueData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Arrays;
import java.util.List;

/**
	The base class for the different types of parameter values.
	@author Nicklas, Samuel
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/parameters.html">Parameters overview</a>
	@hibernate.class table="`ParameterValues`" discriminator-value="-1" lazy="true" batch-size="10"
	@hibernate.discriminator column="`discriminator`" type="int"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public abstract class ParameterValueData<T>
	extends BasicData
{
	ParameterValueData()
	{}
	
	ParameterValueData(T... values)
	{
		replaceValues(Arrays.asList(values));
	}

	/**
		Get values of this parameter. Hibernate mapped in each subclass 
		to a bag containing all values for the parameter.
		@return Collection of values.
	*/
	public abstract List<T> getValues();
	
	/**
		Set values of this parameter.
		@param values Collection of values.
	*/
	abstract void setValues(List<T> values);
	
	/**
		Replace the values in the database with the new
		values in the specified list. It is expected that
		the list contains only objects of the correct type.
	*/
	@SuppressWarnings("unchecked")
	public void replaceValues(List<?> values)
	{
		List<T> current = getValues();
		current.clear();
		current.addAll((List<T>) values);
	}
	
	/**
		Replace the current list with a single new value.
		It is expected that the value is of the correct type.
	*/
	@SuppressWarnings("unchecked")
	public void setSingleValue(Object value)
	{
		List<T> current = getValues();
		current.clear();
		current.add((T) value);
	}
}
