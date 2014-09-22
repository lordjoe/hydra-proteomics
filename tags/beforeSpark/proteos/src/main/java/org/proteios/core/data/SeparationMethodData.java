/*
 $Id: SeparationMethodData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the root class for separation methods. The class describes the
 * object that is the result of the method.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.SeparationMethod
 * @see <a
 *      href="../../../../../../../development/overview/data/separationmethod.html">SeparationMethods
 *      overview</a>
 * @proteios.modified $Date: 2006-09-19 14:31:54Z $
 * @hibernate.class table="`SeparationMethods`" lazy="false"
 *                  discriminator-value="0"
 * @hibernate.discriminator column="`discriminator`" type="int"
 */
public class SeparationMethodData
		extends SharedData
{
	public SeparationMethodData()
	{}

	/**
	 * The maximum length of the external id that can be stored in the database.
	 * 
	 * @see #setExternalId(String)
	 */
	public static final int MAX_EXTERNAL_ID_LENGTH = 255;
	private String externalId;


	/**
	 * Get the external id for the separation method
	 * 
	 * @hibernate.property column="`external_id`" type="string" length="255"
	 *                     not-null="true" unique="true"
	 */
	public String getExternalId()
	{
		return externalId;
	}


	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	private Set<BioMaterialEventData> bioMaterialEvents = new HashSet<BioMaterialEventData>();


	/**
	 * @hibernate.set table="`BioMaterialEvents`" cascade="delete" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-key column="`separation_method_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.BioMaterialEventData"
	 *                                   not-null="false"
	 * @return Returns the bioMaterialEvents.
	 */
	public Set<BioMaterialEventData> getBioMaterialEvents()
	{
		return bioMaterialEvents;
	}


	/**
	 * @param bioMaterialEvents The BioMaterialEvents to set.
	 */
	public void setBioMaterialEvents(Set<BioMaterialEventData> bioMaterialEvents)
	{
		this.bioMaterialEvents = bioMaterialEvents;
	}

}
