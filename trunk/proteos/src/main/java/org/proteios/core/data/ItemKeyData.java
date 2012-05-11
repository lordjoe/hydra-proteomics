/*
	$Id: ItemKeyData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information access privileges for users and groups.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.ItemKey
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="1"
*/
public class ItemKeyData
	extends KeyData
{

	public ItemKeyData()
	{}

	private Map<UserData,Integer> users;
	/**
		Get the map that manages which users that have permissions 
		for this key.

		@hibernate.map table="`UserKeys`" lazy="true"
		@hibernate.collection-key column="`key_id`"
		@hibernate.index-many-to-many column="`user_id`" class="org.proteios.core.data.UserData"
		@hibernate.collection-element column="`permission`" type="int" not-null="true"
	*/
	public Map<UserData,Integer> getUsers()
	{
		if (users == null) users = new HashMap<UserData,Integer>();
		return users;
	}
	void setUsers(Map<UserData,Integer> users)
	{
		this.users = users;
	}

	private Map<GroupData,Integer> groups;
	/**
		Get the map that manages which groups that have permissions 
		for this key.

		@hibernate.map table="`GroupKeys`" lazy="true"
		@hibernate.collection-key column="`key_id`"
		@hibernate.index-many-to-many column="`group_id`" class="org.proteios.core.data.GroupData"
		@hibernate.collection-element column="`permission`" type="int" not-null="true"
	*/
	public Map<GroupData,Integer> getGroups()
	{
		if (groups == null) groups = new HashMap<GroupData,Integer>();
		return groups;
	}
	void setGroups(Map<GroupData,Integer> groups)
	{
		this.groups = groups;
	}

}
