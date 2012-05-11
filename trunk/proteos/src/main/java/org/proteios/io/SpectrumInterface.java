/*
 $Id: SpectrumInterface.java 3270 2009-05-12 11:59:26Z olle $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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

import java.util.List;

/**
 * This interface defines methods to access an array of mass or intensity values
 * for mass spectrometry peak lists.
 * 
 * @author olle
 */
public interface SpectrumInterface
{
	public double[] listMass();


	public double[] listIntensities();


	public Double getRetentionTimeInMinutes();


	/**
	 * @return a list of precursor or null if there are none associated with the
	 *         spectrum
	 */
	public List<SpectrumPrecursor> getPrecursors();


	/**
	 * Get the spectrum extra data list.
	 * 
	 * @return List<StringPairInterface> The spectrum extra data list.
	 */
	public List<StringPairInterface> getExtraDataList();
}
