/*
 $Id: GelImageAnalysisEventData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This represents a GelImageAnalysisEventData which is a starting point of a MeasuredItem.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.GelImageAnalysisEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/gelimageanalysisevent.html">GelImageAnalysisEvent
 *      overview</a>
 * @proteios.modified $Date: 2006-05-31 14:31:54Z $
 * @hibernate.subclass discriminator-value="1"
 */
public class GelImageAnalysisEventData
		extends BioMaterialEventData
{
	public GelImageAnalysisEventData()
	{}

	// -------------------------------------------
	/**
	 * Annotated file.
	 */
	private FileData annotatedFile;


	/**
	 * @hibernate.many-to-one column="`annotated_file_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 */
	public FileData getAnnotatedFile()
	{
		return this.annotatedFile;
	}


	/**
	 */
	public void setAnnotatedFile(FileData annotatedFile)
	{
		this.annotatedFile = annotatedFile;
	}


	/**
	 * Raw file.
	 */
	private FileData rawFile;

	/**
	 * Using the source file column for the raw image file
	 * @hibernate.many-to-one column="`source_file_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 */
	public FileData getRawFile()
	{
		return this.rawFile;
	}

	/**
	 * Use sourceFile for the raw image file instead
	 */
	public void setRawFile(FileData rawFile)
	{
		this.rawFile = rawFile;
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

	
	/**
	 * Software settings information.
	 */
	private SoftwareConfigurationData softwareSettings;


	/**
	 * @hibernate.many-to-one column="`software_settings_id`" cascade="delete"
	 *                        not-null="false" outer-join="false"
	 */
	public SoftwareConfigurationData getSoftwareSettings()
	{
		return this.softwareSettings;
	}


	/**
	 */
	public void setSoftwareSettings(SoftwareConfigurationData softwareSettings)
	{
		this.softwareSettings = softwareSettings;
	}
}
