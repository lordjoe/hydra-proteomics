/*
 $Id: BioMaterialEventData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to register events for measured biomaterials.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.BioMaterialEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/biomaterial.html">Biomaterials
 *      overview</a>
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`BioMaterialEvents`" lazy="false"
 *                  discriminator-value="-1"
 * @hibernate.discriminator column="`discriminator`" type="int"
 */
public abstract class BioMaterialEventData
		extends BasicData
{
	public BioMaterialEventData()
	{}

	private MeasuredBioMaterialData bioMaterial;


	/**
	 * Get the single biomaterial used by this event.
	 * 
	 * @hibernate.many-to-one column="`biomaterial_id`" not-null="false"
	 *                        outer-join="false" update="false"
	 */
	public MeasuredBioMaterialData getBioMaterial()
	{
		return bioMaterial;
	}


	public void setBioMaterial(MeasuredBioMaterialData bioMaterial)
	{
		this.bioMaterial = bioMaterial;
	}

	private Float usedQuantity;


	/**
	 * Get the used quantity of the biomaterial affected by this event,
	 * measured in microliters. This can be both positive and negative.
	 * 
	 * @hibernate.property column="`used_quantity`" type="float"
	 *                     not-null="false"
	 */
	public Float getUsedQuantityInMicroLiters()
	{
		return usedQuantity;
	}


	public void setUsedQuantityInMicroLiters(Float usedQuantity)
	{
		this.usedQuantity = usedQuantity;
	}

	private UserData user;


	/**
	 * Get the user that is responsible for this event, typically this is the
	 * user that entered the information in the database.
	 * 
	 * @hibernate.many-to-one column="`user_id`" not-null="false"
	 *                        outer-join="false" update="false"
	 */
	public UserData getUser()
	{
		return user;
	}


	public void setUser(UserData user)
	{
		this.user = user;
	}

	private ProtocolData protocol;


	/**
	 * Get the protocol used in this event.
	 * 
	 * @hibernate.many-to-one column="`protocol_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public ProtocolData getProtocol()
	{
		return protocol;
	}


	public void setProtocol(ProtocolData protocol)
	{
		this.protocol = protocol;
	}

	private Date entryDate;


	/**
	 * Get the date this event was added to the database.
	 * 
	 * @hibernate.property column="`entry_date`" not-null="true"
	 *                     update="false"
	 */
	public Date getEntryDate()
	{
		return entryDate;
	}


	public void setEntryDate(Date entryDate)
	{
		this.entryDate = entryDate;
	}

	private Date eventDate;


	/**
	 * Get the date this event happened in the lab.
	 * 
	 * @hibernate.property column="`event_date`" not-null="false"
	 */
	public Date getEventDate()
	{
		return eventDate;
	}


	public void setEventDate(Date eventDate)
	{
		this.eventDate = eventDate;
	}

	/**
	 * The maximum length of the comment about this event.
	 */
	public static final int MAX_COMMENT_LENGTH = 65535;
	private String comment;


	/**
	 * Get the comment about this event.
	 * 
	 * @hibernate.property column="`comment`" type="text" not-null="false"
	 */
	public String getComment()
	{
		return comment;
	}


	public void setComment(String comment)
	{
		this.comment = comment;
	}

	private Map<MeasuredBioMaterialData, UsedQuantity> sources;


	/**
	 * Get a map containing the source biomaterials and the used quantity for
	 * this event.
	 * 
	 * @hibernate.map table="`BioMaterialEventSources`" lazy="true"
	 *                cascade="delete"
	 * @hibernate.collection-key column="`event_id`"
	 * @hibernate.index-many-to-many column="`biomaterial_id`"
	 *                               class="org.proteios.core.data.MeasuredBioMaterialData"
	 * @hibernate.collection-composite-element class="org.proteios.core.data.UsedQuantity"
	 */
	public Map<MeasuredBioMaterialData, UsedQuantity> getSources()
	{
		if (sources == null)
			sources = new HashMap<MeasuredBioMaterialData, UsedQuantity>();
		return sources;
	}


	void setSources(Map<MeasuredBioMaterialData, UsedQuantity> sources)
	{
		this.sources = sources;
	}

	/**
	 * Separation method information.
	 */
	private SeparationMethodData separationMethod;


	/**
	 * @hibernate.many-to-one column="`separation_method_id`" cascade="delete"
	 *                        not-null="false" outer-join="false"
	 */
	public SeparationMethodData getSeparationMethod()
	{
		return this.separationMethod;
	}


	/**
	 */
	public void setSeparationMethod(SeparationMethodData separationMethod)
	{
		this.separationMethod = separationMethod;
	}

	private BioMaterialEventData previousBioMaterialEvent;


	/**
	 * This function returns the previous event that is directly linked to this
	 * event. It can be used for linking series of separation events and
	 * possibly more.
	 * 
	 * @hibernate.many-to-one column="`previousBioMaterialEventId`"
	 *                        not-null="false"
	 * @return Returns the previousBioMaterialEvent.
	 */
	public BioMaterialEventData getPreviousBioMaterialEvent()
	{
		return previousBioMaterialEvent;
	}


	/**
	 * This function sets the previous event that is directly linked to this event. 
	 * It can be used for linking series of separation events and possibly more
	 * 
	 * @param previousBioMaterialEvent The previousBioMaterialEvent to set.
	 */
	public void setPreviousBioMaterialEvent(
			BioMaterialEventData previousBioMaterialEvent)
	{
		this.previousBioMaterialEvent = previousBioMaterialEvent;
	}
}