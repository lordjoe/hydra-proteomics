/*
	$Id: ProjectKeys.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core.data.keyring;
import java.io.Serializable;

/**
	Class for mapping the <code>ProjectKeys</code> table.
	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public final class ProjectKeys
	extends KeyPermission
	implements Serializable
{
	private int projectId;
	
	/**
		Create a new <code>ProjectKeys</code> object.
	*/
	public ProjectKeys()
	{
		super();
	}

	/**
		Get the id of the project.
	*/
	public int getProjectId()
	{
		return projectId;
	}
	/**
		Set the id of the project.
	*/
	public void setProjectId(int projectId)
	{
		this.projectId = projectId;
	}

	/**
		Check if this object is equal to another <code>ProjectKeys</code>
		object. They are considered to be the same if the project id and the
		key id are the same. The permissions may be different.
	*/
	@Override
	public boolean equals(Object o)
	{
		if ((o == null) || (getClass() != o.getClass())) return false;
		ProjectKeys pk = (ProjectKeys)o;
		return (pk.projectId == this.projectId) && (pk.keyId == this.keyId);
	}
	
	/**
		Calculate the hash code for the object.
	*/
	@Override
	public int hashCode()
	{
		return (31*keyId + 17*projectId);
	}
}
