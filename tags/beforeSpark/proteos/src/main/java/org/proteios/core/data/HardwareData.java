/*
  $Id: HardwareData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information about a Hardware.
	
	@author enell
	@version 2.0
	@see org.proteios.core.Hardware
	@see <a href="../../../../../../../development/overview/data/wares.html">Hardware and software overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`Hardware`" lazy="true"
*/
public class HardwareData
	extends CommonData
{
	public static final int MAX_VERSIONSTRING_LENGTH = 255;
	
	private HardwareTypeData hardwareType;
	/**
		@hibernate.many-to-one column="`hardwaretype_id`" not-null="true" outer-join="false"
	*/
	public HardwareTypeData getHardwareType()
	{
		return this.hardwareType;
	}
	public void setHardwareType(HardwareTypeData hardwareType)
	{
		this.hardwareType = hardwareType;
	}
	
	private String versionString;
	/**
		@hibernate.property column="`version_string`" type="string" length="255" not-null="false"
	*/
	public String getVersionString()
	{
		return versionString;
	}
	public void setVersionString(String versionString)
	{
		this.versionString = versionString;
	}
}
