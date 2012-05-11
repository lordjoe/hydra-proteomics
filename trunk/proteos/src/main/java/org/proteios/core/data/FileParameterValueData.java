/*
	$Id: FileParameterValueData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.ArrayList;
import java.util.List;

/**
	File parameter value.

	@author Samuel
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/parameters.html">Parameters overview</a>
	@hibernate.subclass discriminator-value="9"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class FileParameterValueData
	extends ParameterValueData<FileData>
{
	public FileParameterValueData()
	{}
	
	public FileParameterValueData(FileData... values)
	{
		super(values);
	}
	
	private List<FileData> values;
	/**
		@hibernate.bag table="`FileValues`" lazy="true"
		@hibernate.collection-key column="`id`"
		@hibernate.collection-many-to-many column="`value`" class="org.proteios.core.data.FileData" not-null="true"
	*/
	@Override
	public List<FileData> getValues()
	{
		if (values == null)
		{
			values = new ArrayList<FileData>();
		}
		return values;
	}
	@Override
	void setValues(List<FileData> values)
	{
		this.values = values;
	}

}
