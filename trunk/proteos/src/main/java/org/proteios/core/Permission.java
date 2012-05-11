/*
	$Id: Permission.java 3207 2009-04-09 06:48:11Z gregory $

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

package org.proteios.core;

import java.util.EnumSet;
import java.util.Set;
/**
	This enumeration defined constants for permissions.
	
	@base.internal
	Permissions are stored in the database as integers, which means that
	a given set of permissions must be combined before they are saved.
	We use the <code>grantValue</code> for this. This value is constructed
	in such a way that some permissions implicitly activates other permissions
	as follows:
	<ul>
	<li>READ -> 
	<li>USE -> READ
	<li>RESTRICTED_WRITE -> USE and READ
	<li>WRITE -> RESTRICTED_WRITE, USE and READ
	<li>DELETE -> WRITE, RESTRICTED_WRITE, USE and READ
	<li>SET_OWNER -> WRITE, RESTRICTED_WRITE, USE and READ
	<li>SET_PERMISSION -> WRITE, RESTRICTED_WRITE, USE and READ
	</ul>
	
	The <code>denyValue</code> is only used by the {@link BasicItem#initPermissions(int, int)}
	method when a subclass needs to deny a permission. This value is constructed 
	in a similiar way:
	<ul>
	<li>READ -> USE, RESTRICTED_WRITE, WRITE, DELETE, SET_OWNER and SET_PERMISSION
	<li>USE -> RESTRICTED_WRITE, WRITE, DELETE, SET_OWNER and SET_PERMISSION
	<li>RESTRICTED_WRITE -> WRITE, DELETE, SET_OWNER and SET_PERMISSION
	<li>WRITE -> DELETE, SET_OWNER and SET_PERMISSION
	<li>DELETE -> 
	<li>SET_OWNER -> 
	<li>SET_PERMISSION -> 
	</ul>

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public enum Permission
{
	/**
		This permission allows a user read information about an item.
	*/
	READ(1, 1+2+4+8+16+32, "read"),
	
	/**
		This permission allows a user to use/link to an item.
	*/
	USE(1+2, 2+4+8+16+32, "use"),
	
	/**
		This permission allows a user to update some information about an item.
	*/
	RESTRICTED_WRITE(1+2+4, 4+8+16+32+64, "restricted write"),

	/**
		This permission allows a user update the information about an item.
	*/
	WRITE(1+2+4+8, 8+16+32+64, "write"),
	
	/**
		This permission allows a user delete an item.
	*/
	DELETE(1+2+4+8+16, 16, "delete"),
	
	/**
		This permission allows a user change the owner of an item.
	*/
	SET_OWNER(1+2+4+8+32, 32, "change owner"),
	
	/**
		This permission allows a user change the access permission to an item.
	*/
	SET_PERMISSION(1+2+4+8+64, 64, "set permissions"),
	
	/**
		This permission allows a user create new items.
	*/
	CREATE(128, 128, "create"),
	
	/**
		This permission denies a user access to an item. This permission can only be given
		to {@link RoleKey}:s.
	*/
	DENIED(256, 256, "denied"),
	
	/**
		This permission allows a user to share an item to the {@link Group#EVERYONE}
		group. This is a system permission and is only meaningful for the role key
		for the {@link Item#SYSTEM} item.
	*/
	SHARE_TO_EVERYONE(512, 512, "share to everyone"),

	/**
		This permission allows a user act as another user using the {@link
		SessionControl#impersonateLogin(int, String)} method. This is a system 
		permission and is only meaningful for the role key for the {@link Item#SYSTEM} 
		item.
	*/
	ACT_AS_ANOTHER_USER(1024, 1024, "act as another user");

	private final int grantValue;
	private final int denyValue;
	private final String displayValue;
	
	/**
		Constructor for Permission enums.
	*/
	private Permission(int grantValue, int denyValue, String displayValue)
	{
		this.grantValue = grantValue;
		this.denyValue = denyValue;
		this.displayValue = displayValue;
	}
	private int grantValue()
	{
		return grantValue;
	}
	private int denyValue()
	{
		return denyValue;
	}

	@Override
	public String toString()
	{
		return displayValue;
	}

	/**
		Checks if the given <code>Permission</code> is granted by the 
		<code>permissions</code> code.
		@param permissions The permission combination
		@param permission The permission to check
	*/
	static boolean hasPermission(int permissions, Permission permission)
	{
		return (permissions & permission.grantValue()) == permission.grantValue();
	}
	
	/**
		Convert the given permission to the integer grant value.
	*/
	static int grant(Permission permission)
	{
		return permission.grantValue();
	}
	
	/**
		Combine the given permissions and convert to the integer grant value.
	*/
	static int grant(Permission... permissions)
	{
		int result = 0;
		for (Permission p : permissions)
		{
			result |= p.grantValue();
		}
		return result;
	}

	/**
		Combine the given permission and convert to the integer grant value.
	*/
	static int grant(Set<Permission> permissions)
	{
		int result = 0;
		if (permissions != null)
		{
			for (Permission p : permissions)
			{
				result |= p.grantValue();
			}
		}
		return result;
	}
	
	/**
		Convert the given permission to the integer deny value.
	*/
	static int deny(Permission permission)
	{
		return permission.denyValue();
	}

	/**
		Combine the given permissions and convert to the integer deny value.
	*/
	static int deny(Permission... permissions)
	{
		int result = 0;
		for (Permission p : permissions)
		{
			result |= p.denyValue();
		}
		return result;
	}

	/**
		Combine the given permission and convert to the integer deny value.
	*/
	static int deny(Set<Permission> permissions)
	{
		int result = 0;
		if (permissions != null)
		{
			for (Permission p : permissions)
			{
				result |= p.denyValue();
			}
		}
		return result;
	}

	/**
		Convert an integer value to a set of permissions.
	*/
	static Set<Permission> fromInt(int permissions)
	{
		EnumSet<Permission> result = EnumSet.noneOf(Permission.class);
		for (Permission p : Permission.values())
		{
			if (hasPermission(permissions, p))
			{
				result.add(p);
			}
		}
		return result;
	}

}

