/*
 $Id: Peak.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

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

import org.proteios.core.data.PeakData;

/**
 * This class represent peak items. A peak item has information about the peak
 * in a mass spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-06-02 12:33:12Z $
 */
public class Peak
		extends BasicItem<PeakData>
		implements Annotatable, Comparable<Peak>
{
	public Peak(PeakData peakData)
	{
		super(peakData);
	}


	public int compareTo(Peak p)
	{
		return getMassToChargeRatio().compareTo(p.getMassToChargeRatio());
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_PEAK
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_PEAK;


	/**
	 * Get a <code>Peak</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for database
	 *        access.
	 * @param id The id of the item to load
	 * @return The <code>Peak</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws BaseException If there is another error
	 */
//	public static Peak getById(DbControl dc, int id)
//			throws ItemNotFoundException, BaseException
//	{
//		Peak pk = dc.loadItem(Peak.class, id);
//		if (pk == null)
//			throw new ItemNotFoundException("Peak[id=" + id + "]");
//		return pk;
//	}
//

	/**
	 * Get a query that returns peak items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<Peak> getQuery()
	{
		return new ItemQuery<Peak>(Peak.class);
	}


	// -------------------------------------------
	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this peak
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the massToChargeRatio.
	 * 
	 * @return A <code>Double</code> holding the massToChargeRatio
	 */
	public Double getMassToChargeRatio()
	{
		return getData().getMassToChargeRatio();
	}


	/**
	 * Set the massToChargeRatio
	 * 
	 * @param massToChargeRatio The Double massToChargeRatio value
	 * @throws InvalidDataException If massToChargeRatio is below zero
	 */
	public void setMassToChargeRatio(Double massToChargeRatio)
			throws InvalidDataException, BaseException
	{
		if (massToChargeRatio != null && massToChargeRatio < 0.0f)
		{
			throw new NumberOutOfRangeException("massToChargeRatio",
				massToChargeRatio, 0.0f, false);
		}
		getData().setMassToChargeRatio(massToChargeRatio);
	}


	/**
	 * Get the intensity.
	 * 
	 * @return A <code>Double</code> holding the intensity
	 */
	public Double getIntensity()
	{
		return getData().getIntensity();
	}


	/**
	 * Set the intensity
	 * 
	 * @param intensity The Double intensity value
	 * @throws InvalidDataException If intensity is below zero
	 */
	public void setIntensity(Double intensity)
			throws InvalidDataException, BaseException
	{
		if (intensity != null && intensity < 0.0f)
		{
			throw new NumberOutOfRangeException("intensity", intensity, 0.0f,
				false);
		}
		getData().setIntensity(intensity);
	}


	/**
	 * The creator PeakList item. The variable is set to an instance of class
	 * {@link PeakList}.
	 */
	/**
	 * Get the {@link PeakList} this peak is created from.
	 * 
	 * @return The <code>PeakList</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public PeakList getPeakList()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(PeakList.class, getData().getPeakList());
	}


	/**
	 * Set the {@link PeakList} this peak is created from.
	 * 
	 * @param peakList The new <code>PeakList</code> item
	 * @throws BaseException If there is another error
	 */
	public void setPeakList(PeakList peakList)
	{
		getData().setPeakList(peakList.getData());
	}
}
