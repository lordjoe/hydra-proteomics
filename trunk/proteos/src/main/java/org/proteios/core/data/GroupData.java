/*
	$Id: GroupData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashSet;
import java.util.Set;

/**
	This class holds information about a group.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.Group
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`Groups`" lazy="false"
*/
public class GroupData
	extends BasicData
	implements NameableData, RemovableData, SystemData
{

	public GroupData()
	{}

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

	private Set<UserData> users;
	/**
		Get the set that manages which users are members of this group.
		@hibernate.set table="`UserGroups`" lazy="true"
		@hibernate.collection-key column="`group_id`"
		@hibernate.collection-many-to-many column="`user_id`" class="org.proteios.core.data.UserData"
	*/
	public Set<UserData> getUsers()
	{
		if (users == null) users = new HashSet<UserData>();
		return users;
	}
	void setUsers(Set<UserData> users)
	{
		this.users = users;
	}

	private Set<GroupData> children;
	/**
		Get the set that manages which other groups are members of this group.
		@hibernate.set table="`GroupGroups`" lazy="true"
		@hibernate.collection-key column="`parent_id`"
		@hibernate.collection-many-to-many column="`child_id`" class="org.proteios.core.data.GroupData"
	*/
	public Set<GroupData> getChildren()
	{
		if (children == null) children = new HashSet<GroupData>();
		return children;
	}
	void setChildren(Set<GroupData> children)
	{
		this.children = children;
	}
	
	private Set<GroupData> parents;
	/**
		This is the inverse end.
		@see #getChildren()
		@hibernate.set table="`GroupGroups`" lazy="true"
		@hibernate.collection-key column="`child_id`"
		@hibernate.collection-many-to-many column="`parent_id`" class="org.proteios.core.data.GroupData"
	*/
	Set<GroupData> getParents()
	{
		return parents;
	}
	void setParents(Set<GroupData> parents)
	{
		this.parents = parents;
	}

	private Set<ProjectData> projects;
	/**
		This is the inverse end.
		@see ProjectData#getGroups()
		@hibernate.set table="`GroupProjects`" lazy="true"
		@hibernate.collection-key column="`group_id`"
		@hibernate.collection-many-to-many column="`project_id`" class="org.proteios.core.data.ProjectData"
	*/
	Set<ProjectData> getProjects()
	{
		return projects;
	}
	void setProjects(Set<ProjectData> projects)
	{
		this.projects = projects;
	}

	private Set<ItemKeyData> itemKeys;
	/**
		This is the inverse end.
		@see ItemKeyData#getGroups()
		@hibernate.set table="`GroupKeys`" lazy="true"
		@hibernate.collection-key column="`group_id`"
		@hibernate.collection-many-to-many column="`key_id`" class="org.proteios.core.data.ItemKeyData"
	*/
	Set<ItemKeyData> getItemKeys()
	{
		return itemKeys;
	}
	void setItemKeys(Set<ItemKeyData> itemKeys)
	{
		this.itemKeys = itemKeys;
	}

	private QuotaData quota;
	/**
		Get the quota object which holds quota information for this group.
		@hibernate.many-to-one column="`quota_id`" not-null="false" outer-join="false"
	*/
	public QuotaData getQuota()
	{
		return quota;
	}
	public void setQuota(QuotaData quota)
	{
		this.quota = quota;
	}
}
