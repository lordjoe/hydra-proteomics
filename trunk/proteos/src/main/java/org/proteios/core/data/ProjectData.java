/*
 $Id: ProjectData.java 3406 2009-09-07 09:06:52Z gregory $

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
import java.util.Set;

/**
 * This class holds information about a project.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.Project
 * @base.modified $Date: 2009-09-07 02:06:52 -0700 (Mon, 07 Sep 2009) $
 * @hibernate.class table="`Projects`" lazy="false"
 */
public class ProjectData
		extends OwnedData
		implements NameableData, RemovableData
{
	public ProjectData()
	{}

	private DirectoryData projectDirectory;


	/**
	 * @hibernate.many-to-one column="`project_directory_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public DirectoryData getProjectDirectory()
	{
		return projectDirectory;
	}


	public void setProjectDirectory(DirectoryData projectDirectory)
	{
		this.projectDirectory = projectDirectory;
	}


				private int projectType = 0;

	/**
  * 0 - Undefined
  * 1 - Gel based Project
  * 2 - Non gel based, this should be more specific
	 * @hibernate.property column="`project_type`" not-null="false"
	 *                        type="int"
	 */
	public int getProjectType()
	{
		return projectType;
	}


	public void setProjectType(int projectType)
	{
		this.projectType = projectType;
	}


	/*
	 * From the NameableData interface
	 * -------------------------------------------
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
	 * From the RemovableData interface
	 * -------------------------------------------
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
	private Map<UserData, Integer> users;


	/**
	 * Get the map that manages which users are members of this project and
	 * their permissions.
	 * 
	 * @hibernate.map table="`UserProjects`" lazy="true"
	 * @hibernate.collection-key column="`project_id`"
	 * @hibernate.index-many-to-many column="`user_id`"
	 *                               class="org.proteios.core.data.UserData"
	 * @hibernate.collection-element column="`permission`" type="int"
	 *                               not-null="true"
	 */
	public Map<UserData, Integer> getUsers()
	{
		if (users == null)
			users = new HashMap<UserData, Integer>();
		return users;
	}


	void setUsers(Map<UserData, Integer> users)
	{
		this.users = users;
	}

	private Map<GroupData, Integer> groups;


	/**
	 * Get the map that manages which users are members of this project and
	 * their permissions.
	 * 
	 * @hibernate.map table="`GroupProjects`" lazy="true"
	 * @hibernate.collection-key column="`project_id`"
	 * @hibernate.index-many-to-many column="`group_id`"
	 *                               class="org.proteios.core.data.GroupData"
	 * @hibernate.collection-element column="`permission`" type="int"
	 *                               not-null="true"
	 */
	public Map<GroupData, Integer> getGroups()
	{
		if (groups == null)
			groups = new HashMap<GroupData, Integer>();
		return groups;
	}


	void setGroups(Map<GroupData, Integer> groups)
	{
		this.groups = groups;
	}

	private Set<ProjectKeyData> projectKeys;


	/**
	 * This is the inverse end.
	 * 
	 * @see ProjectKeyData#getProjects()
	 * @hibernate.set table="`ProjectKeys`" lazy="true"
	 * @hibernate.collection-key column="`project_id`"
	 * @hibernate.collection-many-to-many column="`key_id`"
	 *                                    class="org.proteios.core.data.ProjectKeyData"
	 */
	Set<ProjectKeyData> getProjectKeys()
	{
		return projectKeys;
	}


	void setProjectKeys(Set<ProjectKeyData> projectKeys)
	{
		this.projectKeys = projectKeys;
	}

				boolean closed = false;
	/**
		@return TRUE if the project has been closed
		@hibernate.property column="`closed`" type="boolean" not-null="true"
	*/
	public boolean isClosed()
 {
					return this.closed;
 }

	public void setClosed(boolean closed)
 {
					this.closed = closed;
 }


}
