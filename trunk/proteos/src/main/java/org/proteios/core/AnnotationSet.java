/*
 $Id: AnnotationSet.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander, Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

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

import org.proteios.core.data.AnnotationData;
import org.proteios.core.data.AnnotationSetData;
import org.proteios.core.data.AnnotationTypeData;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * An annotation set is the container for all annotations on an item. An item
 * can have one annotation of each annotation type that is defined for it.
 * <p>
 * An item can also inherit annotation from it's parents. It is possible to
 * inherit all annotations or only specific ones. In the first case this means
 * that if the parent gets new annotations those are automatically inherited. In
 * the second case the inherticane is lost if the annotation is deleted. If a
 * new annotation of the same annotation type is creted later, it is NOT
 * automatically inherited again.
 * 
 * @author Nicklas
 * @version 2.0
 * @see AnnotationType
 * @see Annotation
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class AnnotationSet
		extends BasicItem<AnnotationSetData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#ANNOTATIONSET
	 * @see #getType()
	 */
	public static final Item TYPE = Item.ANNOTATIONSET;


	/**
	 * Get a {@link ItemQuery} object configured to retrieve
	 * <code>AnnotationSet</code> items. This query may return items which the
	 * logged in user doesn't have read permission to. At the moment there is no
	 * way to solve this problem.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	static ItemQuery<AnnotationSet> getQuery()
	{
		return new ItemQuery<AnnotationSet>(AnnotationSet.class);
	}

	/**
	 * The item this annotation set belongs to.
	 */
	private Annotatable item;


	/**
	 * Regular constructor if the item is not known beforehand.
	 */
	AnnotationSet(AnnotationSetData annotationSetData)
	{
		super(annotationSetData);
	}


	/**
	 * Special constructor to avoid {@link #getItem()} to hit the database when
	 * the item is known beforehand.
	 * 
	 * @see AnnotatedItem#getAnnotationSet()
	 */
	AnnotationSet(AnnotationSetData annotationSetData, Annotatable item)
	{
		super(annotationSetData);
		this.item = item;
	}


	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Always return TRUE. An annotation set always has a one-to-one relation to
	 * some item. The annotation set is automatically deleted if that item is
	 * deleted.
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return true;
	}


	/**
	 * READ permission is granted if the logged in user has READ permission on
	 * the associated item. CREATE, WRITE and DELETE permissions are granted if
	 * the logged in user has WRITE permission on the associated item.
	 */
	@Override
	void initPermissions(int granted, int denied)
			throws BaseException
	{
		Annotatable tmp = getItem();
		if (item instanceof Identifiable)
		{
			Identifiable item = (Identifiable) tmp;
			if (item.hasPermission(Permission.WRITE))
			{
				granted |= Permission.grant(Permission.CREATE, Permission.READ,
					Permission.WRITE, Permission.DELETE);
			}
			else if (item.hasPermission(Permission.READ))
			{
				granted |= Permission.grant(Permission.READ);
			}
			super.initPermissions(granted, denied);
		}
	}


	// -------------------------------------------
	/**
	 * Get the item this annotation set belongs to.
	 * 
	 * @return The <code>Annotatable</code> item
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission to the item
	 * @throws BaseException If there is another error
	 */
	public Annotatable getItem()
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		if (item == null)
//		{
//			Item type = getItemType();
//			DbControl dc = getDbControl();
//			String hql = "SELECT item" + " FROM " + type.getDataClass()
//				.getName() + " item" + " WHERE item.annotationSet = :annotationSet";
//			org.hibernate.Query query = HibernateUtil.createQuery(dc
//				.getHibernateSession(), hql);
//			query.setEntity("annotationSet", this.getData());
//			item = (Annotatable) dc.getItem(type.getItemClass(), HibernateUtil
//				.loadData(type.getDataClass(), query));
//		}
//		return item;
	}


	/**
	 * Set the item this annotation set belongs to.
	 */
	void setItem(Annotatable item)
	{
		this.item = item;
	}


	/**
	 * Get the {@link Item} type of the item this annotation set belongs to.
	 */
	public Item getItemType()
	{
		return Item.fromValue(getData().getItemType());
	}


	/**
	 * Check if the annotation set contains an annotation of the specified type.
	 * 
	 * @param annotationType An <code>AnnotationType</code> object
	 * @return TRUE if the annotation set contains an annotation of the
	 *         specified type, FALSE otherwise
	 */
	public boolean hasAnnotation(AnnotationType annotationType)
	{
		if (annotationType == null)
			return false;
		return getData().getAnnotations().containsKey(annotationType.getData());
	}


	/**
	 * Delete the annotation of the specified annotation type.
	 * 
	 * @param annotationType The type of the annotation to delete
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the annotation type is null
	 * @throws BaseException If there is another error
	 */
	public void removeAnnotation(AnnotationType annotationType)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		checkPermission(Permission.WRITE);
//		if (annotationType == null)
//			throw new InvalidUseOfNullException("annotationType");
//		DbControl dc = getDbControl();
//		Annotation a = dc.getItem(Annotation.class, getData().getAnnotations()
//			.remove(annotationType.getData()));
//		if (a != null)
//		{
//			dc.deleteItem(a);
//		}
	}


	/**
	 * Get a query that reurns all direct annotations in this annotation set.
	 * 
	 * @return An <code>ItemQuery</code> object
	 */
	public ItemQuery<Annotation> getAnnotations()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<Annotation> query = Annotation.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("annotationSet"),
//			Hql.entity(this)));
//		return query;
	}


	/**
	 * Inherit an annotation.
	 * 
	 * @param annotation The annotation to inherit
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for this annotation set or use permission for
	 *         the annotation
	 * @throws InvalidDataException If the annotation is null
	 */
	public void inheritAnnotation(Annotation annotation)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (annotation == null)
			throw new InvalidUseOfNullException("annotation");
		annotation.checkPermission(Permission.USE);
		getData().getInherited().add(annotation.getData());
	}


	/**
	 * Remove an inherited an annotation.
	 * 
	 * @param annotation The annotation to remove
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for this annotation set
	 * @throws InvalidDataException If the annotation is null
	 */
	public void removeInheritedAnnotation(Annotation annotation)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (annotation == null)
			throw new InvalidUseOfNullException("annotation");
		getData().getInherited().remove(annotation.getData());
	}


	/**
	 * Check if an annotation is inherited or not by this annotation set.
	 * 
	 * @param annotation The annotation to check
	 * @throws InvalidDataException If the annotation is null
	 */
	public boolean isInherited(Annotation annotation)
			throws InvalidDataException
	{
		if (annotation == null)
			throw new InvalidUseOfNullException("annotation");
		return getData().getInherited().contains(annotation.getData());
	}


	/**
	 * Get a query that returns all inherited annotations in this annotation
	 * set.
	 * 
	 * @return An <code>ItemQuery</code> object
	 */
	public ItemQuery<Annotation> getInheritedAnnotations()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<Annotation> query = Annotation.getQuery();
//		query.joinPermanent(Hql.innerJoin("inheritingSets", Item.ANNOTATIONSET
//			.getAlias()));
//		query.restrictPermanent(Restrictions.eq(Hql.alias(Item.ANNOTATIONSET
//			.getAlias()), Hql.entity(this)));
//		return query;
	}


	/**
	 * Inherit an annotation set.
	 * 
	 * @param annotationSet The annotation set to inherit
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for this annotation set or use permission for
	 *         the annotation set to inherit
	 * @throws InvalidDataException If the annotation set is null
	 */
	public void inheritAnnotationSet(AnnotationSet annotationSet)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (annotationSet == null)
			throw new InvalidUseOfNullException("annotationSet");
		annotationSet.checkPermission(Permission.USE);
		getData().getInheritedSets().add(annotationSet.getData());
	}


	/**
	 * Remove an inherited an annotation set.
	 * 
	 * @param annotationSet The annotation set to remove
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for this annotation set
	 * @throws InvalidDataException If the annotation set is null
	 */
	public void removeInheritedAnnotationSet(AnnotationSet annotationSet)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (annotationSet == null)
			throw new InvalidUseOfNullException("annotationSet");
		getData().getInheritedSets().remove(annotationSet.getData());
	}


	/**
	 * Check if an annotation set is inherited or not by this annotation set.
	 * 
	 * @param annotationSet The annotation set to check
	 * @throws InvalidDataException If the annotation set is null
	 */
	public boolean isInherited(AnnotationSet annotationSet)
			throws InvalidDataException
	{
		if (annotationSet == null)
			throw new InvalidUseOfNullException("annotationSet");
		return getData().getInheritedSets().contains(annotationSet.getData());
	}


	/**
	 * Get a query that returns all inherited annotation sets in this annotation
	 * set.
	 * 
	 * @return An <code>ItemQuery</code> object
	 * @throws BaseException If there is an error
	 */
	public ItemQuery<AnnotationSet> getInheritedAnnotationSets()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<AnnotationSet> query = AnnotationSet.getQuery();
//		query.joinPermanent(Hql.innerJoin("inheritingSets", "i"));
//		query.restrictPermanent(Restrictions.eq(Hql.alias("i"), Hql
//			.entity(this)));
//		return query;
	}


	/**
	 * Get a query that returns all annotation sets inheriting from this
	 * annotation set.
	 * 
	 * @return An <code>ItemQuery</code> object
	 */
	public ItemQuery<AnnotationSet> getInheritingAnnotationSets()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<AnnotationSet> query = AnnotationSet.getQuery();
//		query.joinPermanent(Hql.innerJoin("inheritedSets", "i"));
//		query.restrictPermanent(Restrictions.eq(Hql.alias("i"), Hql
//			.entity(this)));
//		return query;
	}


	/**
	 * Compare the AnnotationSet with another. To be considered as having equal
	 * content, the two annotationsets must annotate the same item type, contain
	 * the same number of annotations and all of the annotations must be equal
	 * as defined by the Annotation.equals() function. TODO: Testing. The
	 * function will only work properly if both annotationsets are in the
	 * database
	 * 
	 * @param as The AnnotationSet to compare with
	 * @return true if equal, otherwise false;
	 */
	public boolean contentEquals(AnnotationSet as)
	{
		boolean equal = false;
		if (as.getItemType() == this.getItemType())
		{
			Map<AnnotationTypeData, AnnotationData> thismap = getData()
				.getAnnotations();
			Map<AnnotationTypeData, AnnotationData> thatmap = getData()
				.getAnnotations();
			if (thismap.size() == thatmap.size())
			{
				Set<Entry<AnnotationTypeData, AnnotationData>> thisEntries = thismap
					.entrySet();
				Set<Entry<AnnotationTypeData, AnnotationData>> thatEntries = thatmap
					.entrySet();
				Iterator<Entry<AnnotationTypeData, AnnotationData>> thisiterator = thisEntries
					.iterator();
				equal = true;
				while (equal && thisiterator.hasNext())
				{
					Entry<AnnotationTypeData, AnnotationData> at = thisiterator
						.next();
					boolean foundInThat = false;
					Iterator<Entry<AnnotationTypeData, AnnotationData>> thatiterator = thatEntries
						.iterator();
					while (!foundInThat && thatiterator.hasNext())
					{
						Entry<AnnotationTypeData, AnnotationData> athat = thatiterator
							.next();
						if (athat.getKey().equals(at.getKey()) && athat
							.getValue().equals(at.getValue()))
						{
							foundInThat = true;
						}
					}
				}
			}
		}
		return equal;
	}
}
