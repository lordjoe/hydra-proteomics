/*
 $Id: IPGData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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
 * This represents an IPG (Immobilized pH Gradient) which is a starting point of
 * a separationmethod.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.IPG
 * @see <a href="../../../../../../../development/overview/data/ipg.html">IPGs
 *      overview</a>
 * @proteios.modified $Date: 2006-09-19 14:31:54Z $
 * @hibernate.subclass discriminator-value="-1"
 */
public class IPGData
		extends SeparationMethodData
{
	public IPGData()
	{}

	// -------------------------------------------
	/**
	 * The length value.
	 */
	private int length = 0;


	/**
	 * Get the Integer length value
	 * 
	 * @hibernate.property column="`length`" type="int" not-null="false"
	 * @return the Integer length value
	 */
	public int getLengthInCentiMeters()
	{
		return length;
	}


	/**
	 * Set the length
	 * 
	 * @param length The int length value to set.
	 */
	public void setLengthInCentiMeters(int length)
	{
		this.length = length;
	}

	/**
	 * The piStart value.
	 */
	private int piStart = 0;


	/**
	 * Get the int piStart value
	 * 
	 * @hibernate.property column="`piStart`" type="int" not-null="false"
	 * @return the int piStart value
	 */
	public int getPiStart()
	{
		return piStart;
	}


	/**
	 * Set the piStart
	 * 
	 * @param piStart The int piStart value to set.
	 */
	public void setPiStart(int piStart)
	{
		this.piStart = piStart;
	}

	/**
	 * The piEnd value.
	 */
	private int piEnd = 0;


	/**
	 * Get the int piEnd value
	 * 
	 * @hibernate.property column="`piEnd`" type="int" not-null="false"
	 * @return the int piEnd value
	 */
	public int getPiEnd()
	{
		return piEnd;
	}


	/**
	 * Set the piEnd
	 * 
	 * @param piEnd The int piEnd value to set.
	 */
	public void setPiEnd(int piEnd)
	{
		this.piEnd = piEnd;
	}
}
