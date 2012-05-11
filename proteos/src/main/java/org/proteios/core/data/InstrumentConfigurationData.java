/*
	$Id: InstrumentConfigurationData.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

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
	This class is the root class for instrumentconfigurations.

	@author Olle
	@version 2.0
	@see org.proteios.core.InstrumentConfiguration
	@see <a href="../../../../../../../development/overview/data/instrumentconfiguration.html">InstrumentConfigurations overview</a>
	@proteios.modified $Date: 2006-05-31 14:31:54Z $
	@hibernate.class table="`InstrumentConfigurations`" lazy="false" discriminator-value="0"
	@hibernate.discriminator column="`discriminator`" type="int"
*/
public abstract class InstrumentConfigurationData
	extends AnnotatedData
	implements FileAttachableData
{

	public InstrumentConfigurationData()
	{}
	
	/**
	 	The maximum length of the dateTime string that can be stored in the database.
	 	@see #setDateTime(String)
	*/
	public static final int MAX_DATETIME_LENGTH = 255;
	private String dateTime;
	/**
	 	Get the dateTime string for the instrumentconfiguration
	 	@hibernate.property column="`date_time`" type="string" length="255" not-null="false"
	*/
	public String getDateTime()
	{
		return dateTime;
	}
	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}

	// -------------------------------------------
	/*
	 	From the FileAttachableData interface
	 	-------------------------------------------
	*/
	private FileData file;
	/**
	 	Get the protocol file
	 	@hibernate.many-to-one column="`protocol`" not-null="false" outer-join="false"
	 	@return the FileData
	 */
	public FileData getFile()
	{
		return this.file;
	}

	/**
	 	Set the protocol file
	 	@param file The protocol File
	 */
	public void setFile(FileData file)
	{
		this.file = file;
	}
	// -------------------------------------------

}