/*
	$Id: Ownable.java 3207 2009-04-09 06:48:11Z gregory $

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
	An <code>Ownable</code> item is an item which has a {@link User} as its owner.
	The {@link OwnableUtil} class provides methods that includes data validation 
	to make it easy to implement this interface.
	<p>

	<b>Reference implementation</b><br>
	<pre class="code">
public User getOwner()
   throws PermissionDeniedException, BaseException
{
   return getDbControl().getItem(User.class, getData().getOwner());
}
public void setOwner(User owner)
   throws PermissionDeniedException, InvalidDataException
{
   checkPermission(Permission.SET_OWNER);
   OwnableUtil.setOwner(getData(), owner);
}
</pre>

	@author Nicklas
	@version 2.0
	@see OwnedItem
	@see OwnableUtil
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Ownable
	extends Identifiable
{
	/**
		Get the {@link User} that is the owner of the item.
		@return The owner of the item
		@throws PermissionDeniedException If the logged in user doesn't 
			have read permission to the owner
		@throws BaseException If there is another error
	*/
	public User getOwner()
		throws PermissionDeniedException, BaseException;

	/**
		Change the owner of the item.
		@param owner The new owner of the item
		@throws PermissionDeniedException If the logged in user doesn't 
			have permission to change the owner on the item
		@throws InvalidDataException If the owner is null
	*/
	public void setOwner(User owner)
		throws PermissionDeniedException, InvalidDataException;

}
