/*
 $Id: HitInterface.java 3835 2010-09-08 09:28:25Z fredrik $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

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

import java.util.Date;
import java.util.List;

/**
 * @author gregory
 */
public interface HitInterface
{
	public abstract Float getRMSError();


	public abstract Double getTotalIntensity();


	public abstract String getPlateId();


	public abstract String getWellPosition();


	public abstract Integer getCharge();


	public abstract Float getCoverage();


	public abstract String getDescription();


	public abstract Float getDeltaMassInDaltons();


	public abstract Float getEValue();


	public abstract Float getExperimentalMassInDaltons();


	public abstract String getExternalId();


	public abstract Integer getMatchedPeaks();


	public abstract Float getMwInDaltons();


	public abstract Float getPi();


	public abstract Float getScore();


	public abstract String getScoreType();


	public abstract Date getSearchDate();


	public abstract Integer getSpotId();


	public abstract Integer getTotalPeaks();


	public abstract Integer getSpectrumId();


	public abstract String getSpectrumStringId();


	public abstract Float getRetentionTimeInMinutes();


	public abstract Boolean isProtein();

	public abstract Float getQuantity();

	/**
	 * Implementation specific method to get an arbitrary value not defined by
	 * the methods in this interface.
	 * 
	 * @param identifier
	 * @return the value of the identifier, null if nothing is found
	 */
	public String getValueOf(String identifier);


	/**
	 * Implementation specific method to get an arbitrary values not defined by
	 * the methods in this interface.
	 * 
	 * @param identifiers
	 * @return list of values of the identifiers, list of null values whenever a
	 *         value is not available. Returned list should always be of the
	 *         same size as the identifiers list.
	 */
	public List<String> getValueOf(List<String> identifiers);
}