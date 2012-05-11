/*
  $Id: PropertyFilterData.java 4054 2010-12-06 13:57:40Z olle $

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
	This class holds context information to help client applications
	with storing common settings for tables.
	
	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/clients.html">Session and client overview</a>
	@base.modified $Date: 2010-12-06 05:57:40 -0800 (Mon, 06 Dec 2010) $
*/
public class PropertyFilterData
{
	public PropertyFilterData()
	{}

	private int operator;
	/**
		Get the operator used for this filter.
		@hibernate.property column="`operator`" type="int" not-null="true"
	*/
	public int getOperator()
	{
		return operator;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}

	private int valueType;
	/**
		Get the type of value user for this filter.
		@hibernate.property column="`value_type`" type="int" not-null="true"
	*/
	public int getValueType()
	{
		return valueType;
	}
	public void setValueType(int valueType)
	{
		this.valueType = valueType;
	}

	/**
		The maximum length of the value that can be stored in the database.
		@see #setValue(String)
	*/
	public static final int MAX_VALUE_LENGTH = 65535;
	private String value;
	/**
		Get the value used in this filter.
		@hibernate.property column="`value`" type="text" not-null="false"
	*/
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}

}
