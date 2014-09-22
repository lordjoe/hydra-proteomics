/*
 $Id: XTandemParameterSetStorageData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * @see org.proteios.core.XTandemParameterSetStorage
 * @see <a
 *      href="../../../../../../../development/overview/data/xtandemparametersetstorage.html">XTandemParameterSetStorage
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`XTandemParameterSetStorage`" lazy="true"
 */
public class XTandemParameterSetStorageData
		extends AnnotatedData
{
	public XTandemParameterSetStorageData()
	{}

	/**
	 * XTandem parameter file. {@link FileData},
	 */
	private FileData parameterFile;


	/**
	 * Get the XTandem parameter file that this XTandem Input was generated from
	 * 
	 * @hibernate.many-to-one column="`parameterFile`" not-null="false"
	 * @return FileData File data for the XTandem parameter file
	 */
	public FileData getParameterFile()
	{
		return this.parameterFile;
	}


	/**
	 * Set the XTandem parameter file
	 * 
	 * @param parameterFile FileData The XTandem parameter File this XTandem input was generated from
	 */
	public void setParameterFile(FileData parameterFile)
	{
		this.parameterFile = parameterFile;
	}
}
