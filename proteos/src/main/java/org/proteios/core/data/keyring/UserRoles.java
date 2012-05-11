/*
	$Id: UserRoles.java 3207 2009-04-09 06:48:11Z gregory $

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
	Class for mapping the <code>UserRoles</code> table.
	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public final class UserRoles
	implements Serializable
{
	private int userId;
	private int roleId;
	
	/**
		Create a new <code>UserRoles</code> object.
	*/
	public UserRoles()
	{}

	/**
		Get the id of the user.
	*/
	public int getUserId()
	{
		return userId;
	}
	/**
		Set the id of the user.
	*/
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	/**
		Get the id of the role.
	*/
	public int getRoleId()
	{
		return roleId;
	}
	/**
		Set the id of the role.
	*/
	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	/**
		Check if this object is equal to another <code>UserRoles</code>
		object. They are considered to be the same if the user id and the
		role id are the same.
	*/
	@Override
	public boolean equals(Object o)
	{
		if ((o == null) || (getClass() != o.getClass())) return false;
		UserRoles ur = (UserRoles)o;
		return (ur.userId == this.userId) && (ur.roleId == this.roleId);
	}
	
	/**
		Calculate the hash code for the object.
	*/
	@Override
	public int hashCode()
	{
		return (31*userId + 17*roleId);
	}
}
