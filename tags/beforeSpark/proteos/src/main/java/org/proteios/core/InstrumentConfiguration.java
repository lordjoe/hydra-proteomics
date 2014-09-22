/*
	$Id: InstrumentConfiguration.java 3207 2009-04-09 06:48:11Z gregory $

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

package org.proteios.core;

import org.proteios.core.data.InstrumentConfigurationData;

/**
	This is the base class for for the two types of
	instrumentconfigurations:
	{@link HardwareConfiguration} and {@link SoftwareConfiguration}.

	@author Olle
	@version 2.0
	@see AnnotatedItem
	@proteios.modified $Date: 2006-05-31 12:33:12Z $
*/
public abstract class InstrumentConfiguration<D extends InstrumentConfigurationData>
	extends AnnotatedItem<D>
//	implements FileAttachable
{
	InstrumentConfiguration(D instrumentConfigurationData)
	{
		super(instrumentConfigurationData);
	}
	
	/**
	 	The maximum length of the dateTime string that can be stored
	 	in the database.
	 	@see #setDateTime(String)
	*/
	public static final int MAX_DATETIME_LENGTH = InstrumentConfigurationData.MAX_DATETIME_LENGTH;

	/**
	 	Get the dateTime string of the instrumentconfiguration.
	 	@return The dateTime string for the instrumentconfiguration
	*/
	public String getDateTime()
	{
		return getData().getDateTime();
	}

	/**
	 	Set the dateTime string of the instrumentconfiguration.
	 	The value may be null but must not be longer than
	 	the value specified by the {@link #MAX_DATETIME_LENGTH}
	 	constant.
	 	@param dateTime The new value for the date and time
	 	@throws InvalidDataException If the string is too long
	*/
	public void setDateTime(String dateTime)
		throws InvalidDataException
	{
		getData().setDateTime(StringUtil.setNullableString(dateTime, "dateTime", MAX_DATETIME_LENGTH));
	}

	/*
		The AnnotatedItem class already supports a 'name' attribute.
	 	@see getName()
	 	@see setName(String)
	*/

	// -------------------------------------------
	/*
	 	From the FileAttachable interface
	 	-------------------------------------------
	*/

//	/**
//	 	Get the protocol {@link File} for the configuration.
//	 	@return A <code>File</code> object
//		@throws BaseException If there is another error
//	 */
//	public File getFile()
//		throws BaseException
//	{
//		return getDbControl().getItem(File.class, getData().getFile());
//	}
//
//	/**
//	 	Set the protocol {@link File} for the configuration.
//	 	@param file The new <code>File</code> item
//  		@throws InvalidDataException If the file is null
//		@throws BaseException If there is another error
//	*/
//	public void setFile(File file)
//		throws InvalidDataException
//	{
//		if (file == null) throw new InvalidUseOfNullException("file");
//		getData().setFile(file.getData());
//	}
	// -------------------------------------------
}
