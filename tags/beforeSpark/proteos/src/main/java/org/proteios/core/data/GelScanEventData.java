/*
 $Id: GelScanEventData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This class represent GelScanEvent items. A GelScanEvent has information about
 * a gel scanning. One event will results in one sourceFile = raw picture file.
 * BioMaterial is used to link to the extract that was used for the gel. For a
 * DiGE gel there will be one scan event per color.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.GelScanEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/gelscanevent.html">GelScanEvent
 *      overview</a>
 * @proteios.modified $Date: 2006-05-31 14:31:54Z $
 * @hibernate.subclass discriminator-value="2"
 */
public class GelScanEventData
		extends BioMaterialEventData
{
	public GelScanEventData()
	{}

	// -------------------------------------------
	/**
	 * Image file.
	 */
	private FileData imageFile;


	/**
	 * Using the source file column for the raw image file
	 * 
	 * @hibernate.many-to-one column="`source_file_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 */
	public FileData getImageFile()
	{
		return this.imageFile;
	}


	/**
	 * Use sourceFile for the raw image file instead
	 */
	public void setImageFile(FileData imageFile)
	{
		this.imageFile = imageFile;
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
