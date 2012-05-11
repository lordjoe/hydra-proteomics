/*
	$Id: ProjectPermission.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
	Class for holding a project id and a permission value.
	It implements the <code>Comparable</code> interface to
	be able to sort a <code>List</code> by the <code>projectId</code> value.
	<p>
	Note! The <code>compareTo</code> method of this class
	is not consistent with the <code>equals</code> method of
	any of the subclasses. Ie. The <code>compareTo</code>
	method may return 0, while the <code>equals</code>
	method returns FALSE. This is because we need to compare
	subclasses to each other while sorting a <code>List</code>.

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public abstract class ProjectPermission
	implements Comparable<ProjectPermission>
{
	int projectId;
	int permission;
	
	/**
		Create a new <code>ProjectPermission</code> object.
	*/
	public ProjectPermission()
	{}

	/**
		Get the id of the project.
	*/
	public final int getProjectId()
	{
		return projectId;
	}
	/**
		Set the id of the project.
	*/
	public final void setProjectId(int projectId)
	{
		this.projectId = projectId;
	}
	/**
		Get the permission.
	*/
	public final int getPermission()
	{
		return permission;
	}
	/**
		Set the permission.
	*/
	public final void setPermission(int permission)
	{
		this.permission = permission;
	}

	/**
		Compare the projectId of this object to the projectId of 
		another <code>ProjectPermission</code> object.
	*/
	public final int compareTo(ProjectPermission other)
	{
		return this.projectId - other.projectId;
	}
	
}
