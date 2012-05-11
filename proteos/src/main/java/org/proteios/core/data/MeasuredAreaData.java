/*
 $Id: MeasuredAreaData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This represents a measured area. A measured area has information about a
 * measured area in a gel analysis experiment.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.MeasuredArea
 * @see <a
 *      href="../../../../../../../development/overview/data/measuredarea.html">MeasuredItems
 *      overview</a>
 * @proteios.modified $Date: 2007-02-20 14:31:54Z $
 * @hibernate.subclass discriminator-value="5" lazy="false"
 */
public class MeasuredAreaData
		extends MeasuredBioMaterialData
{
	public MeasuredAreaData()
	{}

	// -------------------------------------------
	/**
	 * The intensity.
	 */
	private int intensity = 0;


	/**
	 * Get the intensity.
	 * 
	 * @hibernate.property column="`intensity`" type="int" not-null="false"
	 * @return the int intensity value
	 */
	public int getIntensity()
	{
		return intensity;
	}


	/**
	 * Set the intensity.
	 * 
	 * @param intensity The int intensity value
	 */
	public void setIntensity(int intensity)
	{
		this.intensity = intensity;
	}


	/**
	 * The local background.
	 */
	private Float local_background = Float.valueOf(0);


	/**
	 * Get the local_background value.
	 * 
	 * @hibernate.property column="`local_background`" type="float"
	 *                     not-null="false"
	 * @return the Float local_background value
	 */
	public Float getLocal_background()
	{
		return local_background;
	}


	/**
	 * Set the local_background value.
	 * 
	 * @param local_background The Float local background value
	 */
	public void setLocal_background(Float local_background)
	{
		this.local_background = local_background;
	}

	
	/**
	 * Normalized volume.
	 */
	private Float normalized_volume = Float.valueOf(0);


	/**
	 * Get the normalized_volume value.
	 * 
	 * @hibernate.property column="`normalized_volume`" type="float" not-null="false"
	 * @return the Float normalized_volume value
	 */
	public Float getNormalized_volume()
	{
		return normalized_volume;
	}


	/**
	 * Set the normalized_volume value.
	 * 
	 * @param normalized_volume The Float normalized_volume value
	 */
	public void setNormalized_volume(Float normalized_volume)
	{
		this.normalized_volume = normalized_volume;
	}

	
	/**
	 * The normalized flag.
	 */
	private boolean normalized;


	/**
	 * Get the normalized flag.
	 * 
	 * @hibernate.property column="`normalized`" type="boolean"
	 *                     not-null="false"
	 * @return the normalized flag value
	 */
	public boolean isNormalized()
	{
		return normalized;
	}


	/**
	 * Set the normalized flag
	 * 
	 * @param normalized The boolean normalized flag value
	 */
	public void setNormalized(boolean normalized)
	{
		this.normalized = normalized;
	}


	/**
	 * Pixel radius.
	 */
	private int pixel_radius = 0;


	/**
	 * Get the pixel_radius value.
	 * 
	 * @hibernate.property column="`pixel_radius`" type="int" not-null="false"
	 * @return the int pixel_radius value
	 */
	public int getPixel_radius()
	{
		return pixel_radius;
	}


	/**
	 * Set the pixel_radius value.
	 * 
	 * @param pixel_radius The int pixel_radius value
	 */
	public void setPixel_radius(int pixel_radius)
	{
		this.pixel_radius = pixel_radius;
	}


	/**
	 * Pixel x coordinate.
	 */
	private int pixel_x_coord = 0;


	/**
	 * Get the pixel_x_coord value.
	 * 
	 * @hibernate.property column="`pixel_x_coord`" type="int" not-null="false"
	 * @return the int pixel_x_coord value
	 */
	public int getPixel_x_coord()
	{
		return pixel_x_coord;
	}


	/**
	 * Set the pixel_x_coord value.
	 * 
	 * @param pixel_x_coord The int pixel_x_coord value
	 */
	public void setPixel_x_coord(int pixel_x_coord)
	{
		this.pixel_x_coord = pixel_x_coord;
	}


	/**
	 * Pixel y coordinate.
	 */
	private int pixel_y_coord = 0;


	/**
	 * Get the pixel_y_coord value.
	 * 
	 * @hibernate.property column="`pixel_y_coord`" type="int" not-null="false"
	 * @return the int pixel_y_coord value
	 */
	public int getPixel_y_coord()
	{
		return pixel_y_coord;
	}


	/**
	 * Set the pixel_y_coord value.
	 * 
	 * @param pixel_y_coord The int pixel_y_coord value
	 */
	public void setPixel_y_coord(int pixel_y_coord)
	{
		this.pixel_y_coord = pixel_y_coord;
	}


	/**
	 * Volume.
	 */
	private Float volume = Float.valueOf(0);


	/**
	 * Get the volume value.
	 * 
	 * @hibernate.property column="`volume`" type="float" not-null="false"
	 * @return the Float volume value
	 */
	public Float getVolume()
	{
		return volume;
	}


	/**
	 * Set the volume value.
	 * 
	 * @param volume The Float volume value
	 */
	public void setVolume(Float volume)
	{
		this.volume = volume;
	}


	/**
	 * Apparent Mass.
	 */
	private Float apparentMass = Float.valueOf(0);


	/**
	 * Get the apparent mass value.
	 * 
	 * @hibernate.property column="`apparent_mass`" type="float" not-null="false"
	 * @return the Float apparent mass value
	 */
	public Float getApparentMass()
	{
		return apparentMass;
	}


	/**
	 * Set the apparent mass value.
	 * 
	 * @param apparentMass The Float apparent mass value
	 */
	public void setApparentMass(Float apparentMass)
	{
		this.apparentMass = apparentMass;
	}


	/**
	 * A Lane item.
	 */
	private int lane;


	/**
	 * Get the lane value.
	 * 
	 * @hibernate.property column="`lane`" type="int" not-null="false"
	 * @return the int lane value
	 */
	public int getLane()
	{
		return lane;
	}


	/**
	 * Set the lane value.
	 * 
	 * @param lane The int lane value
	 */
	public void setLane(int lane)
	{
		this.lane = lane;
	}


	/**
	 * Apparent pI.
	 */
	private Float apparentPI = Float.valueOf(0);


	/**
	 * Get the apparent pI value.
	 * 
	 * @hibernate.property column="`apparent_pi`" type="float" not-null="false"
	 * @return the Float apparent pI value
	 */
	public Float getApparentPI()
	{
		return apparentPI;
	}


	/**
	 * Set the apparent pI value.
	 * 
	 * @param apparentPI The Float apparent pI value
	 */
	public void setApparentPI(Float apparentPI)
	{
		this.apparentPI = apparentPI;
	}


	/**
	 * Lane index.
	 */
	private int laneIndex = 0;


	/**
	 * Get the laneIndex value.
	 * 
	 * @hibernate.property column="`lane_index`" type="int" not-null="false"
	 * @return the int laneIndex value
	 */
	public int getLaneIndex()
	{
		return laneIndex;
	}


	/**
	 * Set the laneIndex value.
	 * 
	 * @param laneIndex The int laneIndex value
	 */
	public void setLaneIndex(int laneIndex)
	{
		this.laneIndex = laneIndex;
	}



}
