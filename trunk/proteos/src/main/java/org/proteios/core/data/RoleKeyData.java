/*
	$Id: RoleKeyData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information access privileges for roles.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.RoleKey
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="3"
*/
public class RoleKeyData
	extends KeyData
{

	public RoleKeyData()
	{}

	private String name;
	/**
		Get the name of the role key.
		@hibernate.property type="string"
		@hibernate.column name="`name`" type="string" length="255" not-null="false" index="name_idx"
	*/
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	private String description;
	/**
		Get the description for the item.
		@return A <code>String</code> with a description of the item
		@hibernate.property column="`description`" type="text" not-null="false"
	*/
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}

	private int itemType;
	/**
		Get the code for the type of items this role key applies to. See the
		{@link org.proteios.core.Item} class for a list of constants.
		@hibernate.property column="`item_type`" type="int" not-null="false" update="false"
	*/
	public int getItemType()
	{
		return itemType;
	}
	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}

	private Map<RoleData,Integer> roles;
	/**
		Get the map that manages which roles that have permissions 
		for this key.

		@hibernate.map table="`RoleKeys`" lazy="true"
		@hibernate.collection-key column="`key_id`"
		@hibernate.index-many-to-many column="`role_id`" class="org.proteios.core.data.RoleData"
		@hibernate.collection-element column="`permission`" type="int" not-null="true"
	*/
	public Map<RoleData,Integer> getRoles()
	{
		if (roles == null) roles = new HashMap<RoleData,Integer>();
		return roles;
	}
	void setRoles(Map<RoleData,Integer> roles)
	{
		this.roles = roles;
	}

}
