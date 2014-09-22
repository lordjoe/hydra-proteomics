/*
	$Id: Shareable.java 3207 2009-04-09 06:48:11Z gregory $

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
	An <code>Shareable</code> item is an item which can be shared to
	other {@link User}:s, {@link Group}:s and {@link Project}:s. Sharing information 
	is stored in {@link ItemKey} and {@link ProjectKey} objects.
	The {@link ShareableUtil} class provides methods that includes data validation to make 
	it easy to implement this interface.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
public ItemKey getItemKey()
   throws PermissionDeniedException, BaseException
{
   return getDbControl().getItem(ItemKey.class, getData().getItemKey());
}
public void setItemKey(ItemKey itemKey)
   throws PermissionDeniedException
{
   checkPermission(Permission.SET_PERMISSION);
   ShareableUtil.setItemKey(getData(), itemKey);
}
public ProjectKey getProjectKey()
   throws PermissionDeniedException, BaseException
{
   return getDbControl().getItem(ProjectKey.class, getData().getProjectKey());
}
public void setProjectKey(ProjectKey projectKey)
   throws PermissionDeniedException
{
   checkPermission(Permission.SET_PERMISSION);
   ShareableUtil.setProjectKey(getData(), projectKey);
}
public boolean isShared()
{
   return getData().getItemKey() != null || getData().getProjectKey() != null;
}
</pre>

	@author Nicklas
	@version 2.0
	@see SharedItem
	@see ShareableUtil
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Shareable
	extends Ownable
{
	/**
		Get the {@link ItemKey} that is used to share this item to users
		and groups.
		
		@return The <code>ItemKey</code> object
		@throws PermissionDeniedException If the logged in user doesn't 
			have read permission to the key
		@throws BaseException If there is another error
	*/
	public ItemKey getItemKey()
		throws PermissionDeniedException, BaseException;

	/**
		Set the {@link ItemKey} for this item.
		@param itemKey The new item key for the item, or null to stop
			sharing this item
		@throws PermissionDeniedException If the logged in user doesn't 
			have permission to change the key on the item
	*/
	public void setItemKey(ItemKey itemKey)
		throws PermissionDeniedException;

	/**
		Get the {@link ProjectKey} that is used to share this item to projects.
		
		@return The <code>ProjectKey</code> object
		@throws PermissionDeniedException If the logged in user doesn't 
			have read permission to the key
		@throws BaseException If there is another error
	*/
	public ProjectKey getProjectKey()
		throws PermissionDeniedException, BaseException;

	/**
		Set the {@link ProjectKey} for this item.
		@param projectKey The new project key for the item, or null to stop
			sharing this item
		@throws PermissionDeniedException If the logged in user doesn't 
			have permission to change the key on the item
	*/
	public void setProjectKey(ProjectKey projectKey)
		throws PermissionDeniedException;

	/**
		Check if this item has been shared or not. 
		@return TRUE if this item has been shared to a user, group or project
	*/
	public boolean isShared();
}
