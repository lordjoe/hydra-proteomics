/*
	$Id: Nameable.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.NameableData;

/**
	A <code>Nameable</code> item is an item that has
	a <code>name</code> and <code>description</code>.
	The {@link NameableUtil} class provides methods that
	includes data validation to make it easy to implement
	this interface.
	<p>

	<b>Reference implementation</b><br>
	<pre class="code">
public String getName()
{
   return getData().getName();
}
public void setName(String name)
   throws PermissionDeniedException, InvalidDataException
{
   checkPermission(Permission.WRITE);
   NameableUtil.setName(getData(), name);
}
public String getDescription()
{
   return getData().getDescription();
}
public void setDescription(String description)
   throws PermissionDeniedException, InvalidDataException
{
   checkPermission(Permission.RESTRICTED_WRITE);
   NameableUtil.setDescription(getData(), description);
}
</pre>
	
	@author Nicklas
	@version 2.0
	@see NameableUtil
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Nameable
	extends Identifiable
{

	/**
		The maximum length of the name of the item that can be
		stored in the database. Check the name length against
		this value before calling the {@link #setName(String)}
		method to avoid exceptions.
		@see #setName(String)
	*/
	public static int MAX_NAME_LENGTH = NameableData.MAX_NAME_LENGTH;

	/**
		Get the name of the item.
		@return A <code>String</code> with the name of the item
	*/
	public String getName();

	/**
		Set the name of the item. The name cannot be null and mustn't
		be longer than the value specified by the
		{@link #MAX_NAME_LENGTH} constant.

		@param name The new name for the item
		@throws PermissionDeniedException If the logged in user doesn't 
			have write permission
		@throws InvalidDataException If the name is null or longer
			than specified by the {@link #MAX_NAME_LENGTH} constant
	*/
	public void setName(String name)
		throws PermissionDeniedException, InvalidDataException;

	/**
		The maximum length of the description. Check the description length 
		against this value before calling the {@link #setDescription(String)}
		method to avoid exceptions.
		@see #setDescription(String)
	*/
	public static int MAX_DESCRIPTION_LENGTH = NameableData.MAX_DESCRIPTION_LENGTH;

	/**
		Get the description for the item.
		@return A <code>String</code> with a description of the item
	*/
	public String getDescription();

	/**
		Set the description for the item. The description can be null but
		mustn't be longer than the value specified by the
		{@link #MAX_DESCRIPTION_LENGTH} constant.
		
		@param description The new description for the item
		@throws PermissionDeniedException If the logged in user doesn't 
			have write permission
		@throws InvalidDataException If the description longer
			than specified by the {@link #MAX_DESCRIPTION_LENGTH} constant
	*/
	public void setDescription(String description)
		throws PermissionDeniedException, InvalidDataException;

}
