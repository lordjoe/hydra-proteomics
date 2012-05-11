/*
	$Id: CommonItem.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.CommonData;

/**
	@author Nicklas
	@version 2.0
*/
public abstract class CommonItem<D extends CommonData>
	extends SharedItem<D>
	implements Nameable, Removable
{
	CommonItem(D commonData)
	{
		super(commonData);
	}

	// -------------------------------------------
	/*
		From the Nameable interface
		-------------------------------------------
	*/
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
		checkPermission(Permission.WRITE);
		NameableUtil.setDescription(getData(), description);
	}
	// -------------------------------------------
	/*
		From the Removable interface
		-------------------------------------------
	*/
	public boolean isRemoved()
	{
		return getData().isRemoved();
	}
	public void setRemoved(boolean removed)
		throws PermissionDeniedException
	{
		checkPermission(removed ? Permission.DELETE : Permission.WRITE);
		getData().setRemoved(removed);
	}
}



