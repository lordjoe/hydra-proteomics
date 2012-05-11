/*
 $Id: Annotation.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.AnnotationData;

import java.util.List;

/**
 * This class represents an annotation. Annotations are user-defined properties
 * that can be attached to {@link Annotatable} items. They can be very useful in
 * the filtering and analysis process.
 * <p>
 * An annotation must have an {@link AnnotationType}, which controls the type
 * of and how many values that can be attached to an annotation.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class Annotation
		extends BasicItem<AnnotationData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#ANNOTATION
	 * @see #getType()
	 */
	public static final Item TYPE = Item.ANNOTATION;


	/**
	 * Get a query configured to retrieve annotations. This query may return
	 * items which the logged in user doesn't have read permission to. At the
	 * moment there is no way to solve this problem.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	static ItemQuery<Annotation> getQuery()
	{
		return new ItemQuery<Annotation>(Annotation.class);
	}

	private AnnotationSet annotationSet;


	Annotation(AnnotationData annotationData)
	{
		super(annotationData);
	}


	Annotation(AnnotationData annotationData, AnnotationSet annotationSet)
	{
		super(annotationData);
		this.annotationSet = annotationSet;
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
	 * Always return FALSE.
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
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
		AnnotationSet as = getAnnotationSet();
		if (as.hasPermission(Permission.WRITE))
		{
			granted |= Permission.grant(Permission.CREATE, Permission.READ,
				Permission.WRITE, Permission.DELETE);
		}
		else if (as.hasPermission(Permission.READ))
		{
			granted |= Permission.grant(Permission.READ);
		}
		super.initPermissions(granted, denied);
	}


	// -------------------------------------------
	/**
	 * Get the annotation set this annotation belongs to.
	 * 
	 * @return An <code>AnnotationSet</code>
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission for the annotation set (should never happen)
	 */
	public AnnotationSet getAnnotationSet()
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		if (annotationSet == null)
//		{
//			return getDbControl().getItem(AnnotationSet.class,
//				getData().getAnnotationSet());
//		}
//		return annotationSet;
	}


	public void setAnnotationSet(AnnotationSet set)
	{
		getData().setAnnotationSet(set.getData());
	}


	/**
	 * Get the annotation type of this annotation.
	 * 
	 * @return An <code>AnnotationType</code>
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission for the annotation type
	 */
	public AnnotationType getAnnotationType()
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(AnnotationType.class,
//			getData().getAnnotationType());
	}


	/**
	 * Get the values this annotation contains. The values are of a {@link Type}
	 * appropriate for the given annotation type, ie. Integer object for an
	 * integer annotation type. All objects in the list are of the same type.
	 * 
	 * @see AnnotationType#getValueType()
	 * @return A list of objects
	 * @throws BaseException If there is an error
	 */
	public List<?> getValues()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		List<?> values = Values.getItemValues(getDbControl(), getData()
//			.getValues().getValues());
//		return Collections.unmodifiableList(values);
	}


	/**
	 * Get the version number of the item keeping the values. This is useful
	 * when you need to know if the annotation values has been modified by
	 * another transaction since they were read from the database. Unlike other
	 * items the version number of the annotation item doesn't change when the
	 * values are modified.
	 */
	public int getValuesVersion()
	{
		return getData().getValues().getVersion();
	}


	/**
	 * Set the value of the annotation, replacing any previous values.
	 * 
	 * @param value The new value
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for the annotation or read permission for the
	 *         annotation type
	 * @throws InvalidDataException If the value isn't a valid value for the
	 *         annotation type, see
	 *         {@link AnnotationType#validateAnnotationValue(Object)}
	 * @throws BaseException If there is another error
	 */
	public void setValue(Object value)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{
		checkPermission(Permission.WRITE);
		AnnotationType annotationType = getAnnotationType();
		annotationType.validateAnnotationValue(value);
		getData().getValues().setSingleValue(Values.getDataValue(value));
	}


	/**
	 * Set the values of the annotation, replacing any previous values.
	 * 
	 * @param values A list containing the new values
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for the annotation or read permission for the
	 *         annotation type
	 * @throws InvalidDataException If the number of values are too many
	 *         according to the {@link AnnotationType#getMultiplicity()} setting
	 *         any of the values isn't a valid value for the annotation type,
	 *         see {@link AnnotationType#validateAnnotationValue(Object)}
	 * @throws BaseException If there is another error
	 */
	public void setValues(List<?> values)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{
		checkPermission(Permission.WRITE);
		AnnotationType annotationType = getAnnotationType();
		int multiplicity = annotationType.getMultiplicity();
		if (multiplicity > 0 && values.size() > multiplicity)
		{
			throw new InvalidDataException(
				"Too many values. Max " + multiplicity + " value(s) are allowed for this annotation type");
		}
		for (Object value : values)
		{
			annotationType.validateAnnotationValue(value);
		}
		getData().getValues().replaceValues(Values.getDataValues(values));
	}
}
