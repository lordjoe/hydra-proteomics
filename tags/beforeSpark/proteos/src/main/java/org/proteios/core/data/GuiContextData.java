/*
	$Id: GuiContextData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.io.Serializable;

/**
	This is a helper component for the {@link PluginDefinitionData} item to
	store the contexts where a plugin can be used in a client application.
	A context is an item type and context type code. The context type code can currently
	take two values: 0 = list; 1 = item

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/plugins.html">Plugins ovreview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public class GuiContextData
	implements Serializable
{
	GuiContextData()
	{}
	
	public GuiContextData(int itemType, int contextType)
	{
		this.itemType = itemType;
		this.contextType = contextType;
	}

	private int itemType;
	/**
	 	@hibernate.property column="`item_type`" type="int"
	*/
	public int getItemType()
	{
		return itemType;
	}
	void setItemType(int itemType)
	{
		this.itemType = itemType;
	}

	private int contextType;
	/**
	 	@hibernate.property column="`context_type`" type="int"
	*/
	public int getContextType()
	{
		return contextType;
	}
	void setContextType(int contextType)
	{
		this.contextType = contextType;
	}

	/**
		Check if this object is equal to another <code>GuiContext</code>
		object. They are equal if both have the same item type and context type
	*/
	@Override
	public boolean equals(Object o)
	{
		if ((o == null) || (getClass() != o.getClass()))
		{
			return false;
		}
		GuiContextData gc = (GuiContextData)o;
		return (this.itemType == gc.itemType) && (this.contextType == gc.contextType);
	}

	/**
		Calculate the hash code for the object.
	*/
	@Override
	public int hashCode()
	{
		return (53 * itemType + 17 * contextType);
	}
}
