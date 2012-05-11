/*
  $Id: AnnotationTypeData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
	This class holds information about an annotation type.
 
	@author Nicklas
	@version 2.0
	@see org.proteios.core.AnnotationType
	@see <a href="../../../../../../../development/overview/data/annotations.html">Annotations overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`AnnotationTypes`" lazy="false"
*/
public class AnnotationTypeData
	extends CommonData
{

	public AnnotationTypeData()
	{}

	private int multiplicity;
	/**
		How many values are allowed for this annotations of this type. 0 = no limit
		@hibernate.property column="`multiplicity`" type="int" not-null="true"
	*/
	public int getMultiplicity()
	{
		return multiplicity;
	}
	public void setMultiplicity(int multiplicity)
	{
		this.multiplicity = multiplicity;
	}

	private boolean requiredForMiame;
	/**
		If an annotation of this type must be present for an annotatable item to be
		compliant with MIAME.
		@hibernate.property column="`required_for_miame`" type="boolean" not-null="true"
	*/
	public boolean isRequiredForMiame()
	{
		return requiredForMiame;
	}
	public void setRequiredForMiame(boolean requiredForMiame)
	{
		this.requiredForMiame = requiredForMiame;
	}

	private boolean isEnumeration;
	/**
		If this annotation type defines a list of allowed values or not.
		@hibernate.property column="`is_enumeration`" type="boolean" not-null="true"
	*/
	public boolean isEnumeration()
	{
		return isEnumeration;
	}
	public void setEnumeration(boolean isEnumeration)
	{
		this.isEnumeration = isEnumeration;
	}
	
	private int height;
	/**
		The recommended height (lines) to display input fields for this 
		annotation in client applications.
		@hibernate.property column="`height`" type="int" not-null="true"
	*/
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}

	private int width;
	/**
		The recommended width (characters) to display input fields for this 
		annotation in client applications.
		@hibernate.property column="`width`" type="int" not-null="true"
	*/
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}

	private String defaultValue;
	public static final int MAX_DEFAULT_VALUE_LENGTH = 255;
	/**
		The default value of new annotations.
		@hibernate.property column="`default_value`" type="string" length="255" not-null="false"
	*/
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	private int valueType;
	/**
		The code for the value type.
		@hibernate.property column="`value_type`" type="int" not-null="true" update="false"
	*/
	public int getValueType()
	{
		return valueType;
	}
	public void setValueType(int valueType)
	{
		this.valueType = valueType;
	}

	private Set<Integer> itemTypes;
	/**
		The item code for all items this annotation type can be applied to.
		@hibernate.set table="`AnnotationTypeItems`" lazy="true"
		@hibernate.collection-key column="`annotationtype_id`"
		@hibernate.collection-element column="`item_type`" type="int"
	*/
	public Set<Integer> getItemTypes()
	{
		if (itemTypes == null)
		{
			itemTypes = new HashSet<Integer>();
		}
		return itemTypes;
	}
	void setItemTypes(Set<Integer> itemTypes)
	{
		this.itemTypes = itemTypes;
	}
	
	public static final int MAX_OPTION_LENGTH = 255;
	private Map<String, String>  options;
	/**
		Additional options depending on the value type.
		@hibernate.map table="`AnnotationTypeOptions`" lazy="true"
		@hibernate.collection-key column="`annotationtype_id`"
		@hibernate.collection-index column="`name`" type="string" length="255" not-null="true"
		@hibernate.collection-element column="`value`" type="string" length="255" not-null="true"
	*/
	public Map<String, String> getOptions()
	{
		if (options == null)
		{
			options = new HashMap<String, String>();
		}
		return options;
	}
	void setOptions(Map<String, String> options)
	{
		this.options = options;
	}

	private ParameterValueData<?> enumerationValues;
	/**
		The allowed values if this is an enumeration.
		@hibernate.many-to-one column="`enumerationvalues_id`" not-null="false" cascade="all" unique="true"
	*/
	public ParameterValueData<?> getEnumerationValues()
	{
		return enumerationValues;
	}
	public void setEnumerationValues(ParameterValueData<?> enumerationValues)
	{
		this.enumerationValues = enumerationValues;
	}

	private String addedParameterType = null;
	public static final int MAX_ADDEDPARAMETERTYPE_LENGTH = 255;
	/**
	*	Get the addedParameterType value.
	*	An added parameter is a triplet of String Type, Name, and Value,
	*	representing a specific parameter.
	*	An example is a cvParam XML block, that contains String data
	*	for cvLabel, accession, name, and value. The name will be stored
	*	as the name of the annotationType and the value as the annotation
	*	value. The annotation is marked as being a cvParam annotation
	*	by having the following additional parameters set to non-null:
	*	addedParameterType = "cvParam"
	*	addedParameterName = cvLabel (the value from the cvParam XML tag)
	*	addedParameterValue = accession (the value from the cvParam XML tag)
	*
	*	@hibernate.property column="`addedParameterType`" type="string" length="255" not-null="false"
	*	@return String the addedParameterType value.
	*/
	public String getAddedParameterType()
	{
		return addedParameterType;
	}
	
	/**
	*	Set the addedParameterType value.
	*	An added parameter is a triplet of String Type, Name, and Value,
	*	representing a specific parameter.
	*	An example is a cvParam XML block, that contains String data
	*	for cvLabel, accession, name, and value. The name will be stored
	*	as the name of the annotationType and the value as the annotation
	*	value. The annotation is marked as being a cvParam annotation
	*	by having the following additional parameters set to non-null:
	*	addedParameterType = "cvParam"
	*	addedParameterName = cvLabel (the value from the cvParam XML tag)
	*	addedParameterValue = accession (the value from the cvParam XML tag)
	*
	*	@param addedParameterType String the addedParameterType value to set.
	*/
	public void setAddedParameterType(String addedParameterType)
	{
		this.addedParameterType = addedParameterType;
	}

	private String addedParameterName = null;
	public static final int MAX_ADDEDPARAMETERNAME_LENGTH = 255;
	/**
	*	Get the addedParameterName value.
	*
	*	@hibernate.property column="`addedParameterName`" type="string" length="255" not-null="false"
	*	@return String the addedParameterName value.
	*/
	public String getAddedParameterName()
	{
		return addedParameterName;
	}
	
	/**
	*	Set the addedParameterName value.
	*
	*	@param addedParameterName String the addedParameterName value to set.
	*/
	public void setAddedParameterName(String addedParameterName)
	{
		this.addedParameterName = addedParameterName;
	}

	private String addedParameterValue = null;
	public static final int MAX_ADDEDPARAMETERVALUE_LENGTH = 255;
	/**
	*	Get the addedParameterValue value.
	*
	*	@hibernate.property column="`addedParameterValue`" type="string" length="255" not-null="false"
	*	@return String the addedParameterValue value.
	*/
	public String getAddedParameterValue()
	{
		return addedParameterValue;
	}
	
	/**
	*	Set the addedParameterValue value.
	*
	*	@param addedParameterValue String the addedParameterValue value to set.
	*/
	public void setAddedParameterValue(String addedParameterValue)
	{
		this.addedParameterValue = addedParameterValue;
	}
}
