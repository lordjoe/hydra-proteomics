/*
	$Id: MappingCoordinate.java 4167 2011-03-14 07:28:51Z olle $

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
	This class defines a mapping coordinate, which is a triplet of
	plate number, row and column. A <code>MappingCoordinate</code> object
	is used by a PlateMappingData to specify the source and destination
	coordinates of wells. The <code>Serializable</code> interface must be implemented
	because Hibernate requires it if we use it as a map key.

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/plates.html">Array LIMS - Plates overview</a>
	@base.modified $Date: 2011-03-14 00:28:51 -0700 (Mon, 14 Mar 2011) $
*/
@SuppressWarnings("serial")
public class MappingCoordinate
	implements Serializable
{
	/**
		The plate number.
	*/
	private int plate;
	/**
		The row number.
	*/
	private int row;
	/**
		The column number.
	*/
	private int column;

	/**
		Create new object. Used by Hibernate only.
	*/
	MappingCoordinate()
	{}

	/**
		Create new <code>MappingCoordinate</code> with the specified plate, row and column.
	*/
	public MappingCoordinate(int plate, int row, int column)
	{
		this.plate = plate;
		this.row = row;
		this.column = column;
	}

	/*
		From the Object class
		-------------------------------------------
	*/
	/**
		Check if this object is equal to another <code>MappingCoordinate</code>
		object. They are equal if both have the same plate, row and column.
	*/
	@Override
	public final boolean equals(Object o)
	{
		if (this == o) return true;
		if ((o == null) || (getClass() != o.getClass())) return false;
		MappingCoordinate mc = (MappingCoordinate)o;
		return
			(this.plate == mc.plate) &&
			(this.row == mc.row) &&
			(this.column == mc.column);
	}
	/**
		Calculate the hash code for the object.
	*/
	@Override
	public final int hashCode()
	{
		return (131*plate + 53*row + 17*column);
	}
	/**
		Get the coordinate as [plate, row, column]
	*/
	@Override
	public final String toString()
	{
		return "["+plate+", "+row+", "+column+"]";
	}
	// -------------------------------------------

	/**
		Get the plate number.
	*/
	public int getPlate()
	{
		return plate;
	}
	/**
		Set the plate number.
	*/
	void setPlate(int plate)
	{
		this.plate = plate;
	}

	/**
		Get the row number.
	*/
	public int getRow()
	{
		return row;
	}
	/**
		Set the row number.
	*/
	void setRow(int row)
	{
		this.row = row;
	}

	/**
		Get the column number.
	*/
	public int getColumn()
	{
		return column;
	}
	/**
		Set the location.
	*/
	void setColumn(int column)
	{
		this.column = column;
	}

}

