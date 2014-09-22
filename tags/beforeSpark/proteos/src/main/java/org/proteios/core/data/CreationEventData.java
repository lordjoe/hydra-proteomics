/*
 $Id: CreationEventData.java 3456 2009-10-22 15:46:29Z fredrik $

 Copyright (C) 2006, 2007 Gregory Vincic, Olle Mansson

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

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for creation events like pooling of biomaterials.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.BioMaterialEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/creationevent.html">CreationEvent
 *      overview</a>
 * @base.modified $Date: 2009-10-22 08:46:29 -0700 (Thu, 22 Oct 2009) $
 * @hibernate.subclass discriminator-value="0"
 */
public class CreationEventData
		extends BioMaterialEventData
{
	public CreationEventData()
	{}

	private Set<MeasuredBioMaterialData> createdMeasuredBioMaterials;


	/**
	 * @hibernate.set table="`BioMaterials`" lazy="true" inverse="true" cascade="all"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.MeasuredBioMaterialData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`creationEventId`"
	 * @return Returns the createdMeasuredBioMaterial(s).
	 */
	public Set<MeasuredBioMaterialData> getCreatedMeasuredBioMaterials()
	{
		if (createdMeasuredBioMaterials == null)
		{
			createdMeasuredBioMaterials = new HashSet<MeasuredBioMaterialData>();
		}
		return createdMeasuredBioMaterials;
	}


	/**
	 * @param createdMeasuredBioMaterials The createdMeasuredBioMaterials to set.
	 */
	public void setCreatedMeasuredBioMaterials(
			Set<MeasuredBioMaterialData> createdMeasuredBioMaterials)
	{
		this.createdMeasuredBioMaterials = createdMeasuredBioMaterials;
	}
}