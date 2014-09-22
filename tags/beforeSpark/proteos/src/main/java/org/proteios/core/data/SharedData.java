/*
	$Id: SharedData.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
	This class extends the {@link OwnedData} class and implements the
	{@link ShareableData} interface. Ie. by extending this class a data item 
	gets an owner and it will be possible to share the item to other users.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.SharedItem
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public abstract class SharedData
	extends OwnedData
	implements ShareableData
{

	public SharedData()
	{}
	
	/*
		From the ShareableData interface
		-------------------------------------------
	*/
	private ItemKeyData itemKey;
	public ItemKeyData getItemKey()
	{
		return itemKey;
	}
	public void setItemKey(ItemKeyData itemKey)
	{
		this.itemKey = itemKey;
	}
	private ProjectKeyData projectKey;
	public ProjectKeyData getProjectKey()
	{
		return projectKey;
	}
	public void setProjectKey(ProjectKeyData projectKey)
	{
		this.projectKey = projectKey;
	}
	// -------------------------------------------

}