/*
 $Id: SeparationEventData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This represents a SeparationEvent. Separation events can create objects like
 * Gels or IPGstrips. These objects are described in a SeparationMethod. object.
 * SeparationEvents are normally of type 2
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.SeparationEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/separationevent.html">SeparationEvent
 *      overview</a>
 * @proteios.modified $Date: 2006-05-31 14:31:54Z $
 * @hibernate.subclass discriminator-value="3"
 */
public class SeparationEventData
		extends BioMaterialEventData
{
	public SeparationEventData()
	{}

	// -------------------------------------------
	/**
	 * Source file.
	 */
	private FileData sourceFile;


	/**
	 * @hibernate.many-to-one column="`source_file_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 */
	public FileData getSourceFile()
	{
		return this.sourceFile;
	}


	/**
	 */
	public void setSourceFile(FileData sourceFile)
	{
		this.sourceFile = sourceFile;
	}

	/**
	 * Hardware settings information.
	 */
	private HardwareConfigurationData hardwareSettings;


	/**
	 * @hibernate.many-to-one column="`hardware_settings_id`" cascade="delete"
	 *                        not-null="false" outer-join="false"
	 */
	public HardwareConfigurationData getHardwareSettings()
	{
		return this.hardwareSettings;
	}


	/**
	 */
	public void setHardwareSettings(HardwareConfigurationData hardwareSettings)
	{
		this.hardwareSettings = hardwareSettings;
	}


}
