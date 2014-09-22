/*
 $Id: IdentificationFileInterface.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.List;

/**
 * This file API should be used to read files that contain identification search
 * results.
 * 
 * @author gregory
 */
public interface IdentificationFileInterface
{
	/**
	 * @return name of file used as input for this search result or null. 
	 * The filename will be without path. 
	 */
	public String getInputFileName();


	/**
	 * @return list of hits or empty list if no hits are found
	 */
	public List<HitInterface> listAllHits();


}
