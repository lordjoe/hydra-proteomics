/*
 $Id: AnnotationType.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

import org.proteios.core.data.AnnotationTypeData;
import org.proteios.core.data.ParameterValueData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Objects of this class defines an annotation type. An annotation type is
 * always of a specific {@link Type}, ie. a string, integer, etc. Limitations
 * on the values can be specified, ie. min and max values, max string length,
 * etc. It is also possible to use a predefined list of allowed values, ie an
 * enumeration.
 * <p>
 * An annotation type also specifies which types of items it can be used on.
 * 
 * @author Nicklas
 * @version 2.0
 * @see Annotatable
 * @see Annotation
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class AnnotationType
		extends CommonItem<AnnotationTypeData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#ANNOTATIONTYPE
	 * @see #getType()
	 */
	public static final Item TYPE = Item.ANNOTATIONTYPE;


	/**
	 * Get a {@link ItemQuery} that returns annotation types. If the itemType
	 * parameter is null, annotation types for all item types will be returned
	 * 
	 * @param itemType Optional, limit the annotation types to those that are
	 *        enabled for the specified item
	 * @return An {@link ItemQuery} object
	 */
	public static org.proteios.core.ItemQuery<AnnotationType> getQuery(Item itemType)
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<AnnotationType> query = new ItemQuery<AnnotationType>(
//			AnnotationType.class);
//		if (itemType != null)
//		{
//			query.joinPermanent(Hql.innerJoin("itemTypes", "itemType"));
//			query.restrictPermanent(Restrictions.eq(Hql.alias("itemType"),
//				Expressions.integer(itemType.getValue())));
//		}
//		return query;
	}


	AnnotationType(AnnotationTypeData annotationTypeData)
	{
		super(annotationTypeData);
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
	 * Checks if:
	 * <ul>
	 * <li>Annotations of this type exists
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"COUNT_ANNOTATIONS_FOR_ANNOTATIONTYPE");
//		/*
//		 * SELECT count(*) FROM AnnotationData a WHERE a.annotationType =
//		 * :annotationType
//		 */
//		query.setEntity("annotationType", this.getData());
//		return HibernateUtil.loadData(Integer.class, query) > 0;
	}


	// -------------------------------------------
	/**
	 * Get the value type for this annotation type. It can't be change once the
	 * object has been created.
	 */
	public  Type getValueType()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return Path.Type.fromValue(getData().getValueType());
	}


	/**
	 * Get the multiplicity. The multiplicity tells the core how many valus an
	 * annotation of this annotation type may contain. The default is one. Zero
	 * denotes unlimited values.
	 */
	public int getMultiplicity()
	{
		return getData().getMultiplicity();
	}


	/**
	 * Set the multiplicity. The multiplicity tells the core how many valus an
	 * annotation of this annotation type may contain. The default is one. Zero
	 * denotes unlimited values.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the multiplicit is lower than zero
	 */
	public void setMultiplicity(int multiplicity)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setMultiplicity(
			IntegerUtil.checkMin(multiplicity, "multiplicity", 0));
	}


	/**
	 * Check an annotation of this annotation type required for an item to be
	 * compliant with the MIAME recommendation.
	 * 
	 * @see <a href="http://www.mged.org/Workgroups/MIAME/miame.html">MIAME</a>
	 */
	public boolean isRequiredForMiame()
	{
		return getData().isRequiredForMiame();
	}


	/**
	 * Set if an annotation of this annotation type is required for an item to
	 * be compliant with the MIAME recommendation.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @see <a href="http://www.mged.org/Workgroups/MIAME/miame.html">MIAME</a>
	 */
	public void setRequiredForMiame(boolean required)
			throws PermissionDeniedException
	{
		checkPermission(Permission.WRITE);
		getData().setRequiredForMiame(required);
	}


	/**
	 * Check if this annotation type provides a list of predefined allowed
	 * values.
	 */
	public boolean isEnumeration()
	{
		return getData().isEnumeration();
	}


	/**
	 * Set if this annotation type should provide a list of predefined allowed
	 * values.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the value type doesn't support
	 *         enumerations, see {@link Type#canEnumerate}
	 */
	public void setEnumeration(boolean isEnumeration)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (isEnumeration && !getValueType().canEnumerate())
		{
			throw new InvalidDataException(
				getValueType() + " annotation type doesn't support enumerations.");
		}
		getData().setEnumeration(isEnumeration);
	}


	/**
	 * Check if the enumeration values should be displayed as a selection list
	 * or as radio buttons/checkboxes by the client application. The client
	 * application may decide to use some other display format that better suits
	 * the situation.
	 */
	public boolean getDisplayAsList()
	{
		return getBooleanOption("displayAsList", true);
	}


	/**
	 * Set if the enumeration values should be displayed as a selection list or
	 * as radio buttons/checkboxes by the client application. The client
	 * application may decide to use some other display format that better suits
	 * the situation.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException Never
	 */
	public void setDisplayAsList(boolean displayAsList)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		setOption("displayAsList", Boolean.valueOf(displayAsList).toString());
	}


	/**
	 * Get the recommended height in characters a client application should use
	 * to render an input field for annotations of this annotation type. A
	 * client application is not required to use this value if it is not
	 * appropriate for the situation.
	 */
	public int getHeight()
	{
		return getData().getHeight();
	}


	/**
	 * Set the recommended height in characters a client application should use
	 * to render an input field for annotations of this annotation type. A
	 * client application is not required to use this value if it is not
	 * appropriate for the situation.
	 * 
	 * @param height The height in characters
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the height is less than one
	 */
	public void setHeight(int height)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setHeight(IntegerUtil.checkMin(height, "height", 1));
	}


	/**
	 * Get the recommended width in characters a client application should use
	 * to render an input field for annotations of this annotation type. A
	 * client application is not required to use this value if it is not
	 * appropriate for the situation.
	 */
	public int getWidth()
	{
		return getData().getWidth();
	}


	/**
	 * Set the recommended width in characters a client application should use
	 * to render an input field for annotations of this annotation type. A
	 * client application is not required to use this value if it is not
	 * appropriate for the situation.
	 * 
	 * @param width The width in characters
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the width is less than one
	 */
	public void setWidth(int width)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setWidth(IntegerUtil.checkMin(width, "width", 1));
	}

	/**
	 * The maximum length of the default value.
	 */
	public static final int MAX_DEFAULT_VALUE_LENGTH = AnnotationTypeData.MAX_DEFAULT_VALUE_LENGTH;


	/**
	 * Get the recommended default value for new annotations of this annotation
	 * type. It is not certain that the string represents a value that can be
	 * converted to the correct value type as specified by the
	 * {@link #getValueType()} setting.
	 */
	public String getDefaultValue()
	{
		return getData().getDefaultValue();
	}


	/**
	 * Set the recommended default value for new annotation of this annotation
	 * type. It is not checked that the string can be converted to the correct
	 * value type as specified by the {@link #getValueType()} setting.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the default value is longer than
	 *         {@link #MAX_DEFAULT_VALUE_LENGTH}
	 */
	public void setDefaultValue(String defaultValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setDefaultValue(
			StringUtil.setNullableString(defaultValue, "defaultValue",
				MAX_DEFAULT_VALUE_LENGTH));
	}


	/**
	 * Check if this annotation type can be used to annotate item of the
	 * specified type.
	 * 
	 * @param itemType The {@link Item} type
	 * @return TRUE if annotations are possible, FALSE otherwise
	 */
	public boolean isEnabledForItem(Item itemType)
	{
		if (itemType == null)
			return false;
		return getData().getItemTypes().contains(itemType.getValue());
	}


	/**
	 * Enable this annotation type to be used for items of the specified type.
	 * 
	 * @param itemType The {@link Item} type to enable this annotation type for
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the item type is null, or not represents
	 *         an annotatable item
	 */
	public void enableForItem(Item itemType)
			throws PermissionDeniedException, InvalidDataException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		checkPermission(Permission.WRITE);
//		if (itemType == null)
//			throw new InvalidUseOfNullException("itemType");
//		if (!WSEndpointReference.Metadata.getAnnotatableItems().contains(itemType))
//		{
//			throw new InvalidDataException(
//				itemType + " is not an annotatable item type.");
//		}
//		getData().getItemTypes().add(itemType.getValue());
	}


	/**
	 * Disable this annotation type to be used for items of the specified type.
	 * 
	 * @param itemType The {@link Item} type to disable this annotation type for
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the item type is null
	 */
	public void disableForItem(Item itemType)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (itemType == null)
			throw new InvalidUseOfNullException("itemType");
		getData().getItemTypes().remove(itemType.getValue());
	}


	/**
	 * Get the {@link Item} types this annotation type is enabled for.
	 * 
	 * @return A <code>Set</code> of <code>Item</code>:s
	 */
	public Set<Item> getEnabledItems()
	{
		Set<Integer> itemCodes = getData().getItemTypes();
		Set<Item> items = new HashSet<Item>(itemCodes.size());
		for (int itemCode : itemCodes)
		{
			items.add(Item.fromValue(itemCode));
		}
		return items;
	}


	/**
	 * Get the minumum allowed value for an {@link Type#INT} or
	 * {@link Type#LONG} annotation.
	 * 
	 * @return The minimum allowed value or null if none has been specfied or if
	 *         this annotation type hasn't an <code>INT</code> or
	 *         <code>LONG</code> value type
	 */
	public Long getMinValueLong()
	{
		return getValueType() == Type.INT || getValueType() == Type.LONG ? getLongOption(
			"minValue", null) : null;
	}


	/**
	 * Set the minimum allowed value for an {@link Type#INT} or
	 * {@link Type#LONG} annotation.
	 * 
	 * @param minValue The minimum allowed value or null if no limit is
	 *        specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If this annotation type hasn't an
	 *         <code>INT</code> or <code>LONG</code> value type
	 */
	public void setMinValueLong(Long minValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (getValueType() != Type.INT && getValueType() != Type.LONG)
		{
			throw new InvalidDataException(
				"Invalid option for valueType=" + getValueType());
		}
		setOption("minValue", minValue == null ? null : minValue.toString());
	}


	/**
	 * Get the maximum allowed value for an {@link Type#INT} or
	 * {@link Type#LONG} annotation.
	 * 
	 * @return The maximum allowed value or null if none has been specfied or if
	 *         this annotation type hasn't an <code>INT</code> or
	 *         <code>LONG</code> value type
	 */
	public Long getMaxValueLong()
	{
		return getValueType() == Type.INT || getValueType() == Type.LONG ? getLongOption(
			"maxValue", null) : null;
	}


	/**
	 * Set the maximum allowed value for an {@link Type#INT} or
	 * {@link Type#LONG} annotation.
	 * 
	 * @param maxValue The maximum allowed value or null if no limit is
	 *        specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If this annotation type hasn't an
	 *         <code>INT</code> value type
	 */
	public void setMaxValueLong(Long maxValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (getValueType() != Type.INT && getValueType() != Type.LONG)
		{
			throw new InvalidDataException(
				"Invalid option for valueType=" + getValueType());
		}
		setOption("maxValue", maxValue == null ? null : maxValue.toString());
	}


	/**
	 * Get the minumum allowed value for a {@link Type#FLOAT} or
	 * {@link Type#DOUBLE} annotation.
	 * 
	 * @return The minimum allowed value or null if none has been specfied or if
	 *         this annotation type hasn't a <code>FLOAT</code> or
	 *         <code>DOUBLE</code> value type
	 */
	public Double getMinValueDouble()
	{
		return getValueType() == Type.FLOAT || getValueType() == Type.DOUBLE ? getDoubleOption(
			"minValue", null) : null;
	}


	/**
	 * Set the minimum allowed value for a {@link Type#FLOAT} or
	 * {@link Type#DOUBLE} annotation.
	 * 
	 * @param minValue The minimum allowed value or null if no limit is
	 *        specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If this annotation type hasn't a
	 *         <code>FLOAT</code> or <code>DOUBLE</code> value type
	 */
	public void setMinValueDouble(Double minValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (getValueType() != Type.FLOAT && getValueType() != Type.DOUBLE)
		{
			throw new InvalidDataException(
				"Invalid option for valueType=" + getValueType());
		}
		setOption("minValue", minValue == null ? null : minValue.toString());
	}


	/**
	 * Get the maximum allowed value for a {@link Type#FLOAT} or
	 * {@link Type#DOUBLE} annotation.
	 * 
	 * @return The maximum allowed value or null if none has been specfied or if
	 *         this annotation type hasn't a <code>FLOAT</code> or
	 *         <code>DOUBLE</code> value type
	 */
	public Double getMaxValueDouble()
	{
		return getValueType() == Type.FLOAT || getValueType() == Type.DOUBLE ? getDoubleOption(
			"maxValue", null) : null;
	}


	/**
	 * Set the maximum allowed value for a {@link Type#FLOAT} or
	 * {@link Type#DOUBLE} annotation.
	 * 
	 * @param maxValue The maximum allowed value or null if no limit is
	 *        specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If this annotation type hasn't a
	 *         <code>FLOAT</code> or <code>DOUBLE</code> value type
	 */
	public void setMaxValueDouble(Double maxValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (getValueType() != Type.FLOAT && getValueType() != Type.DOUBLE)
		{
			throw new InvalidDataException(
				"Invalid option for valueType=" + getValueType());
		}
		setOption("maxValue", maxValue == null ? null : maxValue.toString());
	}


	/**
	 * Get the maximum allowed string length for a {@link Type#STRING}
	 * annotation.
	 * 
	 * @return The maximum allowed string length or null if none has been
	 *         specfied or if this annotation type hasn't an <code>STRING</code>
	 *         value type
	 */
	public Integer getMaxLength()
	{
		return getValueType() == Type.STRING ? getIntOption("maxLength", null) : null;
	}


	/**
	 * Set the maximum allowed string length for a {@link Type#STRING}
	 * annotation.
	 * 
	 * @param maxLength The maximum allowed value or null if no limit is
	 *        specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If this annotation type hasn't a
	 *         <code>STRING</code> value type
	 */
	public void setMaxLength(Integer maxLength)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (getValueType() != Type.STRING)
			throw new InvalidDataException(
				"Invalid option for valueType=" + getValueType());
		if (maxLength != null && (maxLength > 255 || maxLength < 1))
		{
			throw new NumberOutOfRangeException("maxLength", maxLength, 1, 255);
		}
		setOption("maxLength", maxLength == null ? null : maxLength.toString());
	}


	/**
	 * Get the list of allowed values if this annotation type is an enumeration.
	 * 
	 * @return A <code>List</code> with the allowed values or null if this
	 *         annotation type is not an enumeration or no values has been
	 *         specified
	 * @see #isEnumeration()
	 * @throws BaseException If there is an error
	 */
	public List<?> getValues()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		if (!isEnumeration())
//			return null;
//		ParameterValueData<?> pv = getData().getEnumerationValues();
//		return pv == null ? null : Collections.unmodifiableList(Values
//			.getItemValues(getDbControl(), pv.getValues()));
	}


	/**
	 * Set the list of allowed values for an enumerated annotation type.
	 * 
	 * @param values The list of allowed values, or null to clear the values
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the list contains values of the wrong
	 *         type as checked by the {@link Type#validate(List)} method
	 */
	public void setValues(List<?> values)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (!isEnumeration())
			throw new InvalidDataException(
				"Invalid option for non-enumerated type");
		Type valueType = getValueType();
		if (values != null)
			valueType.validate(values);
		ParameterValueData<?> pv = getData().getEnumerationValues();
		if (pv == null)
		{
			pv = valueType.newParameterValueData();
			getData().setEnumerationValues(pv);
		}
		pv.replaceValues(Values.getDataValues(values));
	}


	/**
	 * Get an option.
	 */
	String getOption(String name)
	{
		return getData().getOptions().get(name);
	}


	/**
	 * Get an option and convert the value to an integer. If the option doesn't
	 * exist or the value can't be converted, the default value is returned.
	 */
	Integer getIntOption(String name, Integer defaultValue)
	{
		String option = getOption(name);
		if (option == null)
			return defaultValue;
		try
		{
			return Integer.valueOf(option);
		}
		catch (Exception ex)
		{
			return defaultValue;
		}
	}


	/**
	 * Get an option and convert the value to a long. If the option doesn't
	 * exist or the value can't be converted, the default value is returned.
	 */
	Long getLongOption(String name, Long defaultValue)
	{
		String option = getOption(name);
		if (option == null)
			return defaultValue;
		try
		{
			return Long.valueOf(option);
		}
		catch (Exception ex)
		{
			return defaultValue;
		}
	}


	/**
	 * Get an option and convert the value to a float. If the option doesn't
	 * exist or the value can't be converted, the default value is returned.
	 */
	Float getFloatOption(String name, Float defaultValue)
	{
		String option = getOption(name);
		if (option == null)
			return defaultValue;
		try
		{
			return Float.valueOf(option);
		}
		catch (Exception ex)
		{
			return defaultValue;
		}
	}


	/**
	 * Get an option and convert the value to a double. If the option doesn't
	 * exist or the value can't be converted, the default value is returned.
	 */
	Double getDoubleOption(String name, Double defaultValue)
	{
		String option = getOption(name);
		if (option == null)
			return defaultValue;
		try
		{
			return Double.valueOf(option);
		}
		catch (Exception ex)
		{
			return defaultValue;
		}
	}


	/**
	 * Get an option and convert the value to a boolean. If the option doesn't
	 * exist the default value is returned. If the option contains the string
	 * "true" (case ignored) true is returned, false otherwise.
	 */
	boolean getBooleanOption(String name, boolean defaultValue)
	{
		String option = getOption(name);
		if (option == null)
			return defaultValue;
		return Boolean.parseBoolean(option);
	}

	/**
	 * The maximum length of an option.
	 */
	public static final int MAX_OPTION_LENGTH = AnnotationTypeData.MAX_OPTION_LENGTH;


	/**
	 * Set an option.
	 */
	void setOption(String name, String value)
			throws InvalidDataException
	{
		if (value == null)
		{
			getData().getOptions().remove(name);
		}
		else
		{
			if (value.length() > MAX_OPTION_LENGTH)
				throw new StringTooLongException(name, value, MAX_OPTION_LENGTH);
			getData().getOptions().put(name, value);
		}
	}


	/**
	 * Validate an annotation value. We check the following:
	 * <ul>
	 * <li>The object is of the correct type, ie. <code>Float</code> if
	 * {@link #getValueType()} returns {@link Type#FLOAT}, etc.
	 * <li>The value is within the min and max bouds if specified
	 * <li>A string value is not too long
	 * <li>The value exists in the predefined list if this annotation type is
	 * an enumeration
	 * </ul>
	 */
	public void validateAnnotationValue(Object value)
			throws InvalidDataException
	{
		if (value == null)
			throw new InvalidUseOfNullException("value");
		Type valueType = getValueType();
		valueType.validate(value);
		if (valueType == Type.INT)
		{
			Integer intValue = (Integer) value;
			Integer minValue = getIntOption("minValue", null);
			Integer maxValue = getIntOption("maxValue", null);
			if ((minValue != null && intValue.compareTo(minValue) < 0) || (maxValue != null && intValue
				.compareTo(maxValue) > 0))
			{
				if (minValue != null && maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						intValue.intValue(), minValue.intValue(), maxValue
							.intValue());
				}
				else if (minValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						intValue.intValue(), minValue.intValue(), false);
				}
				else if (maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						intValue.intValue(), maxValue.intValue(), true);
				}
			}
		}
		else if (valueType == Type.LONG)
		{
			Long longValue = (Long) value;
			Long minValue = getLongOption("minValue", null);
			Long maxValue = getLongOption("maxValue", null);
			if ((minValue != null && longValue.compareTo(minValue) < 0) || (maxValue != null && longValue
				.compareTo(maxValue) > 0))
			{
				if (minValue != null && maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						longValue.longValue(), minValue.longValue(), maxValue
							.longValue());
				}
				else if (minValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						longValue.longValue(), minValue.longValue(), false);
				}
				else if (maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						longValue.longValue(), maxValue.longValue(), true);
				}
			}
		}
		else if (valueType == Type.FLOAT)
		{
			Float floatValue = (Float) value;
			Float minValue = getFloatOption("minValue", null);
			Float maxValue = getFloatOption("maxValue", null);
			if ((minValue != null && floatValue.compareTo(minValue) < 0) || (maxValue != null && floatValue
				.compareTo(maxValue) > 0))
			{
				if (minValue != null && maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						floatValue.floatValue(), minValue.floatValue(),
						maxValue.floatValue());
				}
				else if (minValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						floatValue.floatValue(), minValue.floatValue(), false);
				}
				else if (maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						floatValue.floatValue(), maxValue.floatValue(), true);
				}
			}
		}
		else if (valueType == Type.DOUBLE)
		{
			Double doubleValue = (Double) value;
			Double minValue = getDoubleOption("minValue", null);
			Double maxValue = getDoubleOption("maxValue", null);
			if ((minValue != null && doubleValue.compareTo(minValue) < 0) || (maxValue != null && doubleValue
				.compareTo(maxValue) > 0))
			{
				if (minValue != null && maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						doubleValue.doubleValue(), minValue.doubleValue(),
						maxValue.doubleValue());
				}
				else if (minValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						doubleValue.doubleValue(), minValue.doubleValue(),
						false);
				}
				else if (maxValue != null)
				{
					throw new NumberOutOfRangeException(this.toString(),
						doubleValue.doubleValue(), maxValue.doubleValue(), true);
				}
			}
		}
		else if (valueType == Type.STRING)
		{
			String stringValue = (String) value;
			Integer maxLength = getIntOption("maxLength", 255);
			if (stringValue.length() > maxLength)
			{
				throw new StringTooLongException(this.toString(), stringValue,
					maxLength);
			}
		}
		if (isEnumeration())
		{
			ParameterValueData<?> pv = getData().getEnumerationValues();
			if (pv == null || !pv.getValues().contains(value))
			{
				throw new InvalidDataException(
					"Value '" + value + "' is not among the list of allowed values: " + pv
						.getValues());
			}
		}
	}

	/**
	 * The maximum length of the addedParameterType value.
	 */
	public static final int MAX_ADDEDPARAMETERTYPE_LENGTH = AnnotationTypeData.MAX_ADDEDPARAMETERTYPE_LENGTH;


	/**
	 * Get the addedParameterType value. An added parameter is a triplet of
	 * String Type, Name, and Value, representing a specific parameter. An
	 * example is a cvParam XML block, that contains String data for cvLabel,
	 * accession, name, and value. The name will be stored as the name of the
	 * annotationType and the value as the annotation value. The annotation is
	 * marked as being a cvParam annotation by having the following additional
	 * parameters set to non-null: addedParameterType = "cvParam"
	 * addedParameterName = cvLabel (the value from the cvParam XML tag)
	 * addedParameterValue = accession (the value from the cvParam XML tag)
	 * 
	 * @return addedParameterType String the addedParameterType value.
	 */
	public String getAddedParameterType()
	{
		return getData().getAddedParameterType();
	}


	/**
	 * Set the addedParameterTypevalue for new annotation of this annotation
	 * type.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the addedParameterType value is longer
	 *         than {@link #MAX_ADDEDPARAMETERTYPE_LENGTH}
	 */
	public void setAddedParameterType(String addedParameterType)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setAddedParameterType(
			StringUtil.setNullableString(addedParameterType,
				"addedParameterType", MAX_ADDEDPARAMETERTYPE_LENGTH));
	}

	/**
	 * The maximum length of the addedParameterName value.
	 */
	public static final int MAX_ADDEDPARAMETERNAME_LENGTH = AnnotationTypeData.MAX_ADDEDPARAMETERNAME_LENGTH;


	/**
	 * Get the addedParameterName value.
	 * 
	 * @return addedParameterName String the addedParameterName value.
	 */
	public String getAddedParameterName()
	{
		return getData().getAddedParameterName();
	}


	/**
	 * Set the addedParameterName value for new annotation of this annotation
	 * type.
	 * 
	 * @param addedParameterName String the addedParameterName to set.
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the addedParameterName value is longer
	 *         than {@link #MAX_ADDEDPARAMETERNAME_LENGTH}
	 */
	public void setAddedParameterName(String addedParameterName)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setAddedParameterName(
			StringUtil.setNullableString(addedParameterName,
				"addedParameterName", MAX_ADDEDPARAMETERNAME_LENGTH));
	}

	/**
	 * The maximum length of the addedParameterValue value.
	 */
	public static final int MAX_ADDEDPARAMETERVALUE_LENGTH = AnnotationTypeData.MAX_ADDEDPARAMETERVALUE_LENGTH;


	/**
	 * Get the addedParameterValue value.
	 * 
	 * @return addedParameterValue String the addedParameterValue value.
	 */
	public String getAddedParameterValue()
	{
		return getData().getAddedParameterValue();
	}


	/**
	 * Set the addedParameterValue value for new annotation of this annotation
	 * type.
	 * 
	 * @param addedParameterValue String the addedParameterValue to set.
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the addedParameterValue value is longer
	 *         than {@link #MAX_ADDEDPARAMETERVALUE_LENGTH}
	 */
	public void setAddedParameterValue(String addedParameterValue)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setAddedParameterValue(
			StringUtil.setNullableString(addedParameterValue,
				"addedParameterValue", MAX_ADDEDPARAMETERVALUE_LENGTH));
	}


	/**
	 * Check if the AddedParameterType is of a specific type.
	 * 
	 * @param argType String the AddedParameterType to check for.
	 * @return boolean True if addedParameterType equals non-null argType, else
	 *         false.
	 */
	public boolean isAddedParameterType(String argType)
	{
		boolean addedParameterTypeFlag = false;
		if (argType != null && getAddedParameterType() != null)
		{
			if (getAddedParameterType().equals(argType))
			{
				addedParameterTypeFlag = true;
			}
		}
		return addedParameterTypeFlag;
	}


	public void setValueType(Type type)
	{
		getData().setValueType(type.getValue());
	}
}
