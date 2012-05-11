/*
 $Id: MeasuredBioMaterial.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;


import org.proteios.core.data.MeasuredBioMaterialData;

/**
 * This class is the base class for measured biomaterial items. A measured item
 * is an item which can have original and remaining quantities associated with
 * them.
 * <p>
 * The core keeps track of the remaining quantity whenever an item is used to
 * create other items. As it is, an item can be used when creating another item
 * of the same type (pooling) or when creating an item of the appropriate
 * subtype, ie. <code>Sample -> Extract -> LabeledExtract</code>.
 * <p>
 * It is also possible to add a note using a {@link BioMaterialEvent} saying
 * that the quantity has been changed and why.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public abstract class MeasuredBioMaterial<D extends MeasuredBioMaterialData>
		extends BioMaterial<D>
{
	MeasuredBioMaterial(D measuredBioMaterialData)
	{
		super(measuredBioMaterialData);
	}


	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no pooled biomaterial has been created from this item
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"COUNT_SOURCEEVENTS_FOR_BIOMATERIAL");
//		/*
//		 * SELECT count(*) FROM BioMaterialEventData bme JOIN bme.sources src
//		 * WHERE index(src) = :bioMaterial
//		 */
//		query.setEntity("bioMaterial", this.getData());
//		return HibernateUtil.loadData(Integer.class, query) > 0;
	}


	/**
	 * Make sure the creation event is created at the same time this item is
	 * created and that source biomaterials get their used quantities back if
	 * this item is deleted. All events are deleted by cascade mapping in
	 * Hibernate.
	 */
//	@Override
//	void onBeforeCommit(Transactional.Action action)
//			throws BaseException
//	{
//		super.onBeforeCommit(action);
//		if (action == Transactional.Action.CREATE && getCreationEvent() != null)
//		{
//			getDbControl().saveItem(getCreationEvent());
//		}
//		else if (action == Transactional.Action.DELETE && getCreationEvent() != null)
//		{
//			getCreationEvent().clearSources();
//		}
//	}


//	// -------------------------------------------
//	/**
//	 * Get the event that represents the creation of this biomaterial. Return
//	 * null if no creation event registered.
//	 *
//	 * @return A {@link CreationEvent} data.
//	 */
//	public CreationEvent getCreationEvent()
//			throws PermissionDeniedException, BaseException
//	{
//		return getDbControl().getItem(CreationEvent.class,
//			getData().getCreationEvent());
//	}
//
//
//	public void setCreationEvent(CreationEvent creationEvent)
//	{
//		getData().setCreationEvent(creationEvent.getData());
//	}


	/**
	 * Get the concentration (in mg protein / ml) of the biomaterial.
	 * 
	 * @return A <code>Float</code> holding concentration, or null if not
	 *         known
	 */
	public Float getConcentrationInGramsPerLiter()
	{
		return getData().getConcentrationInGramsPerLiter();
	}


	/**
	 * Set the concentration (in mg protein / ml) of the biomaterial.
	 * 
	 * @param concentration A <code>Float</code> holding concentration, or null if not known
	 */
	public void setConcentrationInGramsPerLiter(Float concentration)
	{
		getData().setConcentrationInGramsPerLiter(concentration);
	}


	/**
	 * Get the original quantity of the biomaterial measured in microliters.
	 * 
	 * @return A <code>Float</code> holding the original quantity, or null if
	 *         not known
	 */
	public Float getOriginalQuantityInMicroLiters()
	{
		return getData().getOriginalQuantityInMicroLiters();
	}


	/**
	 * Set the original quantity of the biomaterial measured in microliters.
	 * If the remaining quantity is null, it's value is also set to the same
	 * as the original value.
	 * 
	 * @param originalQuantity The new value for the original quantity, or null
	 *        if not known
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the original quantity is below zero
	 */
	public void setOriginalQuantityInMicroLiters(Float originalQuantity)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{
		checkPermission(Permission.WRITE);
		checkPermission(Permission.USE);
		if (originalQuantity != null && originalQuantity < 0.0f)
		{
			throw new NumberOutOfRangeException("originalQuantity",
				originalQuantity, 0.0f, false);
		}
		getData().setOriginalQuantityInMicroLiters(originalQuantity);
		if ((getRemainingQuantityInMicroLiters() == null) && (originalQuantity != null))
		{
			getData().setRemainingQuantityInMicroLiters(originalQuantity);
		}
		/*
		 * This is done by setSource CreationEvent ev = getCreationEvent(); if
		 * (ev != null) { ev .setUsedQuantityInMicroLiters(originalQuantity == null ? null :
		 * -originalQuantity); }
		 */
	}


	/**
	 * Get the remaining quantity of the biomaterial measured in microliters.
	 * The remaining quantity cannot be modified directly. It is calculated
	 * based on the events for this biomaterial.
	 * 
	 * @return A <code>Float</code> holding the remaining quantity or null if
	 *         not known
	 */
	public Float getRemainingQuantityInMicroLiters()
	{
		return getData().getRemainingQuantityInMicroLiters();
	}


	/**
	 * Set the remaining quantity of the biomaterial measured in microliters.
	 * 
	 * @param remainingQuantity The new value for the remaining quantity, or null
	 *        if not known
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         use permission
	 * @throws InvalidDataException If the remaining quantity is below zero
	 */
	public void setRemainingQuantityInMicroLiters(Float remainingQuantity)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{		
		checkPermission(Permission.USE);
		if (remainingQuantity != null && remainingQuantity < 0.0f)
		{
			throw new NumberOutOfRangeException("remainingQuantity",
				remainingQuantity, 0.0f, false);
		}
		getData().setRemainingQuantityInMicroLiters(remainingQuantity);
	}


	/**
	 * Update the remaining quantity of a biomaterial measured in microliters.
	 * This method does nothing if the <code>usedQuantity</code> parameter is
	 * null or if the remaining quantity is null.
	 */
	static void updateRemainingQuantityInMicroLiters(MeasuredBioMaterialData bioMaterial,
			Float usedQuantity)
	{
		if (usedQuantity == null)
			return;
		Float remain = bioMaterial.getRemainingQuantityInMicroLiters();
		if (remain == null)
		{
			if (bioMaterial.getOriginalQuantityInMicroLiters() == null)
			{
				return;
			}
			remain = bioMaterial.getRemainingQuantityInMicroLiters();
		}
		bioMaterial.setRemainingQuantityInMicroLiters(remain - usedQuantity);
	}


	/**
	 * Check if this biomaterial was created by pooling or not.
	 */
	public boolean isPooled()
	{
		return getData().isPooled();
	}


	/**
	 * Specify if this biomaterial was created by pooling or not.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws BaseException If there is another error
	 * @see BioMaterialEvent#getSources()
	 */
	public void setPooled(boolean pooled)
			throws PermissionDeniedException, BaseException
	{
		checkPermission(Permission.WRITE);
		getData().setPooled(pooled);
	}


//	/**
//	 * Get the events that have used this biomaterial.
//	 */
//	@SuppressWarnings("unchecked")
//	public Set<BioMaterialEvent> getEvents()
//	{
//		Set<BioMaterialEvent> events = new HashSet<BioMaterialEvent>();
//		try
//		{
//			Iterator<BioMaterialEventData> it = getData().getEvents()
//				.iterator();
//			while (it.hasNext())
//			{
//				events.add(getDbControl().getItem(BioMaterialEvent.class,
//					it.next()));
//			}
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
//		return events;
//	}
//
//
//	/**
//	 * @return a Query for all events that used this extract
//	 */
//	@SuppressWarnings("unchecked")
//	public ItemQuery<BioMaterialEvent> getEventQuery()
//	{
//		return BioMaterialEvent.getQuery(this);
//	}
}
