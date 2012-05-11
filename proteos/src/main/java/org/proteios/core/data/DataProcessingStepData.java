/*
	$Id: DataProcessingStepData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Date;

/**
	This represents a data processing step of
	a peak list set.

	@author Olle
	@version 2.0
	@see org.proteios.core.DataProcessingStep
	@see <a href="../../../../../../../development/overview/data/dataprocessingstep.html">DataProcessingSteps overview</a>
	@proteios.modified $Date: 2006-06-02 11:31:54Z $
	@hibernate.class table="`DataProcessingSteps`" lazy="true"
*/
public class DataProcessingStepData
	extends AnnotatedData
{

	public DataProcessingStepData()
	{}

	// -------------------------------------------
	private Date completionTime;
	/**
	 	Get the completionTime date
		@hibernate.property column="`completion_time`" type="timestamp" not-null="false"
		@return the completionTime date
	*/
	public Date getCompletionTime()
	{
		return completionTime;
	}

	/**
 		Set the completionTime date
		@param completionTime The completionTime date
	*/
	public void setCompletionTime(Date completionTime)
	{
		this.completionTime = completionTime;
	}

	public static final int MAX_DESCRIPTION_LENGTH = 255;
	private String description;
	/**
	 	Get the description
		@hibernate.property column="`description`" type="string" length="255" not-null="false"
		@return the description string
	*/
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
 		Set the description
		@param description The description string
	*/
	@Override
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 	The {@link SoftwareData} item
	 	software contains information on the
	 	software used.
	 	The variable is set to an instance of class
	 	{@link SoftwareData}.
	*/
	private SoftwareData software;

	/**
	 	Get the software
		@hibernate.many-to-one column="`software_id`" not-null="false" outer-join="false"
	 	@return The {@link SoftwareData} software
	*/
	public SoftwareData getSoftware()
	{
		return this.software;
	}

	/**
	 	Set the software
	 	@param software The {@link SoftwareData} software
	*/
	public void setSoftware(SoftwareData software)
	{
		this.software = software;
	}

}
