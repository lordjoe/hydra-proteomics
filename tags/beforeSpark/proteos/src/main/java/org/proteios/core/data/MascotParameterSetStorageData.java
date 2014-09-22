/*
 $Id: MascotParameterSetStorageData.java 3294 2009-06-05 11:43:04Z olle $

 Copyright (C) 2008 Olle Mansson

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
 * This represents the top element of a protein identification search.
 * 
 * @author olle
 * @version 2.0
 * @see org.proteios.core.MascotParameterSetStorage
 * @see <a
 *      href="../../../../../../../development/overview/data/mascotparametersetstorage.html">MascotParameterSetStorage
 *      overview</a>
 * @proteios.modified $Date: 2009-06-05 04:43:04 -0700 (Fri, 05 Jun 2009) $
 * @hibernate.class table="`MascotParameterSetStorage`" lazy="true"
 */
public class MascotParameterSetStorageData
		extends AnnotatedData
{
	public MascotParameterSetStorageData()
	{}

	/**
	 * Mascot parameter file. {@link FileData},
	 */
	private FileData parameterFile;


	/**
	 * Get the Mascot parameter file that this Mascot Input was generated from
	 * 
	 * @hibernate.many-to-one column="`parameterFile`" not-null="false"
	 * @return FileData File data for the Mascot parameter file
	 */
	public FileData getParameterFile()
	{
		return this.parameterFile;
	}


	/**
	 * Set the Mascot parameter file
	 * 
	 * @param parameterFile FileData The Mascot parameter File this Mascot input was generated from
	 */
	public void setParameterFile(FileData parameterFile)
	{
		this.parameterFile = parameterFile;
	}
}
