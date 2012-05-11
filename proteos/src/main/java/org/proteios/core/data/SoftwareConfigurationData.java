/*
	$Id: SoftwareConfigurationData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This represents a softwareconfiguration which is a
	starting point of an instrumentconfiguration.

	@author Olle
	@version 2.0
	@see org.proteios.core.SoftwareConfiguration
	@see <a href="../../../../../../../development/overview/data/softwareconfiguration.html">Softwareconfigurations overview</a>
	@proteios.modified $Date: 2006-05-31 14:31:54Z $
	@hibernate.subclass discriminator-value="-1"

*/
public class SoftwareConfigurationData
	extends InstrumentConfigurationData
{

	public SoftwareConfigurationData()
	{}

	// -------------------------------------------
	private SoftwareData software;
	/**
	 	Get the software
		@hibernate.many-to-one column="`software_id`" not-null="false" outer-join="false"
		@return the SoftwareData
	*/
	public SoftwareData getSoftware()
	{
		return this.software;
	}

	/**
	 	Set the software
	 	@param software The SoftwareData
	*/
	public void setSoftware(SoftwareData software)
	{
		this.software = software;
	}

}
