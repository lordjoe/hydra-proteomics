/*
 $Id: PickFileInterface.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Gregory Vincic

 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios-2.x is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 Proteios is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 */
package org.proteios.io;

import org.proteios.XYCoordinate;
import java.util.List;

/**
 * This file API should be used to read files that contain pick list
 * information. Pick list information contains at least a spotId and a position
 * of that spot as x,y values.
 * 
 * @author gregory
 */
public interface PickFileInterface
{
	/**
	 * @param position
	 * @return a spot id if the position is found, null if not
	 */
	public String getSpotId(XYCoordinate position);


	/**
	 * @param positions
	 * @return list of spot ids if the positions are found. If one XYCoordinate
	 *         is not found a null values should be in that position. The
	 *         returning list size should be the same as that of the positions
	 *         list.
	 */
	public List<String> getSpotId(List<XYCoordinate> positions);


	/**
	 * @param spotId
	 * @return position of that spot id, null if no spot is found at that
	 *         position
	 */
	public XYCoordinate getCoordinate(String spotId);


	/**
	 * @param spotIds
	 * @return list of positions, the resulting list size should be the same as
	 *         that of the spotIds list.
	 */
	public List<XYCoordinate> getCoordinate(List<String> spotIds);
}
