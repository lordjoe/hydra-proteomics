/*
 $Id: MeasuredArea.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

import org.proteios.core.data.MeasuredAreaData;

/**
 * This class represent measured areas. A measured area has information about
 * the measured area in a gel analysis experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2007-02-20 12:33:12Z $
 */
public class MeasuredArea
		extends MeasuredBioMaterial<MeasuredAreaData>
{
	public MeasuredArea(MeasuredAreaData measuredAreaData)
	{
		super(measuredAreaData);
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_MEASUREDAREA
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_MEASUREDAREA;


	/**
	 * Get a query that returns measuredArea items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<MeasuredArea> getQuery()
	{
		return new ItemQuery<MeasuredArea>(MeasuredArea.class);
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
	 * <li>no item has been created from this measuredArea
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the intensity.
	 * 
	 * @return An <code>int</code> holding the intensity
	 */
	public int getIntensity()
	{
		return getData().getIntensity();
	}


	/**
	 * Set the intensity
	 * 
	 * @param intensity The int intensity value
	 * @throws InvalidDataException If intensity is below zero
	 */
	public void setIntensity(int intensity)
			throws InvalidDataException, BaseException
	{
		getData().setIntensity(IntegerUtil.checkMin(intensity, "intensity", 0));
	}


	/**
	 * Get the local_background.
	 * 
	 * @return A <code>Float</code> holding the local_background
	 */
	public Float getLocal_background()
	{
		return getData().getLocal_background();
	}


	/**
	 * Set the local_background
	 * 
	 * @param local_background The Float local_background value
	 * @throws InvalidDataException If local_background is below zero
	 */
	public void setLocal_background(Float local_background)
			throws InvalidDataException, BaseException
	{
		if (local_background != null && local_background < 0.0f)
		{
			throw new NumberOutOfRangeException("local_background",
				local_background, 0.0f, false);
		}
		getData().setLocal_background(local_background);
	}


	/**
	 * Get the Float normalized_volume value.
	 * 
	 * @return the Float normalized_volume value
	 */
	public Float getNormalized_volume()
	{
		return getData().getNormalized_volume();
	}


	/**
	 * Set the Float normalized_volume value.
	 * 
	 * @param normalized_volume The Float normalized_volume value to set.
	 * @throws InvalidDataException If the normalized_volume is lower than zero
	 */
	public void setNormalized_volume(Float normalized_volume)
			throws InvalidDataException
	{
		if (normalized_volume != null && normalized_volume < 0.0f)
		{
			throw new NumberOutOfRangeException("normalized_volume",
				normalized_volume, 0.0f, false);
		}
		getData().setNormalized_volume(normalized_volume);
	}


	/**
	 * Get the normalized flag of this <code>MeasuredArea</code>.
	 * 
	 * @return the boolean normalized flag of this measured area
	 */
	public boolean isNormalized()
	{
		return getData().isNormalized();
	}


	/**
	 * Set the normalized flag for this <code>MeasuredArea</code> item.
	 * 
	 * @param normalized The new boolean normalized flag for this item
	 */
	public void setNormalized(boolean normalized)
	{
		getData().setNormalized(normalized);
	}


	/**
	 * Get the int pixel_radius value.
	 * 
	 * @return the int pixel_radius value
	 */
	public int getPixel_radius()
	{
		return getData().getPixel_radius();
	}


	/**
	 * Set the int pixel_radius value.
	 * 
	 * @param pixel_radius The int pixel_radius value to set.
	 * @throws InvalidDataException If the pixel_radius is lower than zero
	 */
	public void setPixel_radius(int pixel_radius)
			throws InvalidDataException
	{
		getData().setPixel_radius(
			IntegerUtil.checkMin(pixel_radius, "pixel_radius", 0));
	}


	/**
	 * Get the int pixel_x_coord value.
	 * 
	 * @return the int pixel_x_coord value
	 */
	public int getPixel_x_coord()
	{
		return getData().getPixel_x_coord();
	}


	/**
	 * Set the int pixel_x_coord value.
	 * 
	 * @param pixel_x_coord The int pixel_x_coord value to set.
	 * @throws InvalidDataException If the pixel_x_coord is lower than zero
	 */
	public void setPixel_x_coord(int pixel_x_coord)
			throws InvalidDataException
	{
		getData().setPixel_x_coord(
			IntegerUtil.checkMin(pixel_x_coord, "pixel_x_coord", 0));
	}


	/**
	 * Get the int pixel_y_coord value.
	 * 
	 * @return the int pixel_y_coord value
	 */
	public int getPixel_y_coord()
	{
		return getData().getPixel_y_coord();
	}


	/**
	 * Set the int pixel_y_coord value.
	 * 
	 * @param pixel_y_coord The int pixel_y_coord value to set.
	 * @throws InvalidDataException If the pixel_y_coord is lower than zero
	 */
	public void setPixel_y_coord(int pixel_y_coord)
			throws InvalidDataException
	{
		getData().setPixel_y_coord(
			IntegerUtil.checkMin(pixel_y_coord, "pixel_y_coord", 0));
	}


	/**
	 * Get the Float volume value.
	 * 
	 * @return the Float volume value
	 */
	public Float getVolume()
	{
		return getData().getVolume();
	}


	/**
	 * Set the Float volume value.
	 * 
	 * @param volume The Float volume value to set.
	 * @throws InvalidDataException If the volume is lower than zero
	 */
	public void setVolume(Float volume)
			throws InvalidDataException
	{
		if (volume != null && volume < 0.0f)
		{
			throw new NumberOutOfRangeException("volume", volume, 0.0f, false);
		}
		getData().setVolume(volume);
	}


	/**
	 * Get the Float apparent mass value.
	 * 
	 * @return the Float apparent mass value
	 */
	public Float getApparentMass()
	{
		return getData().getApparentMass();
	}


	/**
	 * Set the Float apparent mass value.
	 * 
	 * @param apparentMass The Float apparent mass value to set.
	 * @throws InvalidDataException If the apparent mass is lower than zero
	 */
	public void setApparentMass(Float apparentMass)
			throws InvalidDataException
	{
		if (apparentMass != null && apparentMass < 0.0f)
		{
			throw new NumberOutOfRangeException("apparentMass", apparentMass,
				0.0f, false);
		}
		getData().setApparentMass(apparentMass);
	}


	/**
	 * Get the int lane value.
	 * 
	 * @return the int lane value
	 */
	public int getLane()
	{
		return getData().getLane();
	}


	/**
	 * Set the int lane value.
	 * 
	 * @param lane int The lane value to set.
	 * @throws InvalidDataException If the lane is lower than zero
	 */
	public void setLane(int lane)
			throws InvalidDataException
	{
		getData().setLane(IntegerUtil.checkMin(lane, "lane", 0));
	}


	/**
	 * Get the Float apparent pI value.
	 * 
	 * @return the Float apparent pI value
	 */
	public Float getApparentPI()
	{
		return getData().getApparentPI();
	}


	/**
	 * Set the Float apparent pI value.
	 * 
	 * @param apparentPI The Float apparent pI value to set.
	 * @throws InvalidDataException If the apparent pI is lower than zero
	 */
	public void setApparentPI(Float apparentPI)
			throws InvalidDataException
	{
		if (apparentPI != null && apparentPI < 0.0f)
		{
			throw new NumberOutOfRangeException("apparentPI", apparentPI, 0.0f,
				false);
		}
		getData().setApparentPI(apparentPI);
	}


	/**
	 * Get the int laneIndex value.
	 * 
	 * @return the int laneIndex value
	 */
	public int getLaneIndex()
	{
		return getData().getLaneIndex();
	}


	/**
	 * Set the int laneIndex value.
	 * 
	 * @param laneIndex int The laneIndex value to set.
	 * @throws InvalidDataException If the laneIndex is lower than zero
	 */
	public void setLaneIndex(int laneIndex)
			throws InvalidDataException
	{
		getData().setLaneIndex(IntegerUtil.checkMin(laneIndex, "laneIndex", 0));
	}

}
