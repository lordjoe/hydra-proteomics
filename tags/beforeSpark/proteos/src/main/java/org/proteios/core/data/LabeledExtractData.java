/*
	$Id: LabeledExtractData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This represents a labeled extract.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.LabeledExtract
	@see <a href="../../../../../../../development/overview/data/biomaterial.html">Biomaterials overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="4" lazy="false"
*/
public class LabeledExtractData
	extends MeasuredBioMaterialData
{

	public LabeledExtractData()
	{}
	
	private LabelData label;
	/**
		The label compound used to label the extract. This property is actually not-null="true", but
		since we are using a common table for all biomaterial and the other types doesn't have a label,
		it must be declared not-null="false". It is up to the core to check that no null values are
		passed to the database.
		@hibernate.many-to-one column="`label_id`" not-null="false" outer-join="false"
	*/
	public LabelData getLabel()
	{
		return label;
	}
	public void setLabel(LabelData label)
	{
		this.label = label;
	}
}