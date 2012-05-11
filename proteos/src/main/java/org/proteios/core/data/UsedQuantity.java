/*
	$Id: UsedQuantity.java 3207 2009-04-09 06:48:11Z gregory $

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
	Hibernate will not save entries in the sources map {@link BioMaterialEventData#getSources()}
	where the element is null, so we have to use a composite element instead. This class
	maps the <code>used_quantity</code> column in the <code>BioMaterialEventSources<code> table.
	
	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/biomaterial.html">Biomaterials overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
@SuppressWarnings("serial")
public class UsedQuantity
	implements Serializable
{

	/**
		The used quantity value.
	*/
	private Float usedQuantity = null;

	/**
		Used by Hibernate to create an instance.
	*/
	UsedQuantity()
	{}

	/**
		Create a new instance.
		@param usedQuantity The used quantity
	*/
	public UsedQuantity(Float usedQuantity)
	{
		this.usedQuantity = usedQuantity;
	}

	/**
		Get the used quantity.
		@hibernate.property column="`used_quantity`" not-null="false" type="float"
	*/
	public Float getUsedQuantityInMicroLiters()
	{
		return usedQuantity;
	}
	void setUsedQuantityInMicroLiters(Float usedQuantity)
	{
		this.usedQuantity = usedQuantity;
	}
	
	/**
		Dummy non-null value to force Hibernate to save the link.
		@hibernate.property column="`dummy`" type="int" not-null="true"
	*/
	int getDummy()
	{
		return 1;
	}
	void setDummy(int dummy)
	{}

	/**
		Check if this object is equal to another <code>UsedQuantity</code>
		object. They are considered to be the same if the contains the same
		non-null value.
	*/
	@Override
	public boolean equals(Object o)
	{
		if ((o == null) || (getClass() != o.getClass())) return false;
		UsedQuantity uq = (UsedQuantity)o;
		return this.usedQuantity == null ? false : this.usedQuantity.equals(uq.usedQuantity);
	}

	/**
		Calculate the hash code for the object.
	*/
	@Override
	public int hashCode()
	{
		return usedQuantity == null ? 0 : usedQuantity.hashCode();
	}
	/**
		Convert the value to a string.
	*/
	@Override
	public String toString()
	{
		return usedQuantity == null ? "null" : usedQuantity.toString();
	}

}

