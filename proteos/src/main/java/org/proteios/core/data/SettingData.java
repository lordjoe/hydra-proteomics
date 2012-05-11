/*
  $Id: SettingData.java 3207 2009-04-09 06:48:11Z gregory $

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
	@author enell
	@version 2.0
	@see org.proteios.core.Setting
	@see <a href="../../../../../../../development/overview/data/clients.html">Session and client overview</a>
*/
public abstract class SettingData
	extends BasicData
{
	public SettingData()
	{}

	/**
		The maximum length of the name of the setting that can be
		stored in the database.
		@see #setName(String)
	*/
	public static int MAX_NAME_LENGTH = 255;
	private String name;
	/** 
		Used by Hibernate to link with setting name.
		@hibernate.property type="string"
		@hibernate.column name="`name`" length="255" not-null="true" unique-key="uniquesetting"
	*/
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	/**
		The maximum length of the value of the setting that can be
		stored in the database.
		@see #setValue(String)
	*/
	public static int MAX_VALUE_LENGTH = 65535;
	private String value;
	/**
		Get the value of this setting.
		@hibernate.property column="`value`" type="text" not-null="true"
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
