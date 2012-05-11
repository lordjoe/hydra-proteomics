/*
	$Id: PermissionDeniedException.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
	This exception is thrown when trying to load or manipulate an 
	item in the database and you don't have the required permission.

	@author Nicklas
	@version 2.0
*/
@SuppressWarnings("serial")
public class PermissionDeniedException
	extends BaseException
{
	/**
		Creates a new <code>PermissionDeniedException</code>. The error 
		message produced will look like: <code>Permission denied: Not 
		allowed to write</code>

		@param permission The permission requested. Should be one of the 
			values defined in the {@link Permission} class}
	*/
	public PermissionDeniedException(Permission permission)
	{
		super("Permission denied: Not allowed to "+
			(permission == Permission.DENIED ? "access" : permission.toString()));
	}

	/**
		Creates a new <code>PermissionDeniedException</code>. The error 
		message produced will look like: <code>Permission denied: 
		Not allowed to read User[ID=1]</code>

		@param permission The permission requested. Should be one of the 
			values defined in the {@link Permission} class}
		@param what A description of what access is denied to, for
			example: User[ID=1]
	*/
	public PermissionDeniedException(Permission permission, String what)
	{
		super("Permission denied: Not allowed to "+
			(permission == Permission.DENIED ? "access" : permission.toString())+" "+what);
	}

	/**
		Creates a new <code>PermissionDeniedException</code>. The error
		message produced will look like: 
		<code>Permission denied: <i>message</i></code>

		@param message The message explaining why permission was denied
	*/
	public PermissionDeniedException(String message)
	{
		super("Permission denied: "+message);
	}

}



