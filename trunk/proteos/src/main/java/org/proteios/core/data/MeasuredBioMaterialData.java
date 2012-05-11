/*
 $Id: MeasuredBioMaterialData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the root class for measured biomaterials, ie biomaterials for
 * where the quantity is tracked.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.MeasuredBioMaterial
 * @see <a
 *      href="../../../../../../../development/overview/data/biomaterial.html">Biomaterials
 *      overview</a>
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.subclass discriminator-value="-1" lazy="false"
 */
public abstract class MeasuredBioMaterialData
		extends BioMaterialData
{
	public MeasuredBioMaterialData()
	{}

	private BioMaterialData parent;


	/**
	 * Get the parent biomaterial. Used for non-pooled biomaterials. The core
	 * must add logic to check that only a biomaterial of the parent type is
	 * linked.
	 * 
	 * @hibernate.many-to-one column="`parent_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public BioMaterialData getParent()
	{
		return parent;
	}


	public void setParent(BioMaterialData parent)
	{
		this.parent = parent;
	}

	private Float concentration;


	/**
	 * Get the concentration of the biomaterial. Normally this is the protein concentration in g/l
	 * 
	 * @hibernate.property column="`concentration`" type="float"
	 *                     not-null="false"
	 */
	public Float getConcentrationInGramsPerLiter()
	{
		return concentration;
	}


	public void setConcentrationInGramsPerLiter(Float concentration)
	{
		this.concentration = concentration;
	}

	private Float originalQuantity;


	/**
	 * Get the original quantity of the biomaterial measured in microliters.
	 * 
	 * @hibernate.property column="`original_quantity`" type="float"
	 *                     not-null="false"
	 */
	public Float getOriginalQuantityInMicroLiters()
	{
		return originalQuantity;
	}


	public void setOriginalQuantityInMicroLiters(Float originalQuantity)
	{
		this.originalQuantity = originalQuantity;
	}

	private Float remainingQuantity;


	/**
	 * Get the remaining quantity of the biomaterial measured in microliters.
	 * 
	 * @hibernate.property column="`remaining_quantity`" type="float"
	 *                     not-null="false"
	 */
	public Float getRemainingQuantityInMicroLiters()
	{
		return remainingQuantity;
	}


	public void setRemainingQuantityInMicroLiters(Float remainingQuantity)
	{
		this.remainingQuantity = remainingQuantity;
	}

	private boolean pooled;


	/**
	 * If the biomaterial was created by pooling other biomaterials of the same
	 * type or not.
	 * 
	 * @hibernate.property column="`pooled`" type="boolean" not-null="false"
	 */
	public boolean isPooled()
	{
		return pooled;
	}


	public void setPooled(boolean pooled)
	{
		this.pooled = pooled;
	}

	private CreationEventData creationEvent;


	/**
	 * @hibernate.many-to-one class="org.proteios.core.data.CreationEventData"
	 *                        column="`creationEventId`"
	 * @return Returns the creationEvent.
	 */
	public CreationEventData getCreationEvent()
	{
		return creationEvent;
	}


	/**
	 * @param creationEvent The creationEvent to set.
	 */
	public void setCreationEvent(CreationEventData creationEvent)
	{
		this.creationEvent = creationEvent;
	}

	private Set<BioMaterialEventData> events;


	/**
	 * These are the events that have used this biomaterial. This is the inverse
	 * end.
	 * 
	 * @hibernate.set table="`BioMaterialEventSources`" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-key column="`biomaterial_id`"
	 * @hibernate.collection-many-to-many column="`event_id`"
	 *                                    class="org.proteios.core.data.BioMaterialEventData"
	 */
	public Set<BioMaterialEventData> getEvents()
	{
		if (events == null)
		{
			events = new HashSet<BioMaterialEventData>();
		}
		return events;
	}


	public void setEvents(Set<BioMaterialEventData> events)
	{
		this.events = events;
	}
}