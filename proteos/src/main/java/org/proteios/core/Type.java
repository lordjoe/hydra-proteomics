/*
	$Id: Type.java 3207 2009-04-09 06:48:11Z gregory $

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

//import org.hibernate.Hibernate;
//import org.hibernate.HibernateException;
//import org.hibernate.type.NullableType;
import org.proteios.core.data.BooleanParameterValueData;
import org.proteios.core.data.DateParameterValueData;
import org.proteios.core.data.DoubleParameterValueData;
import org.proteios.core.data.FloatParameterValueData;
import org.proteios.core.data.IntegerParameterValueData;
import org.proteios.core.data.LongParameterValueData;
import org.proteios.core.data.ParameterValueData;
import org.proteios.core.data.StringParameterValueData;
import org.proteios.core.data.TextParameterValueData;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
	This class defines constants for supported value types in Proteios.
	@author Samuel, Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public enum Type
{

	/**
		Integer type.
	*/
	INT(/* Hibernate.INTEGER,*/   1, Integer.class, "int", "Integer", true, true)
	{
		@Override
		ParameterValueData<Integer> newParameterValueData()
		{
			return new IntegerParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 4;
		}
	},

	/**
		Long integer type.
	*/
	LONG(/* Hibernate.LONG,*/  2, Long.class, "long", "Long", true, true)
	{
		@Override
		ParameterValueData<Long> newParameterValueData()
		{
			return new LongParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 8;
		}
	},

	/**
		Float type.
	*/
	FLOAT(/* Hibernate.FLOAT,*/  3, Float.class, "float", "Float", true, true)
	{
		@Override
		ParameterValueData<Float> newParameterValueData()
		{
			return new FloatParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 4;
		}
	},

	/**
		Double type.
	*/
	DOUBLE(/* Hibernate.DOUBLE,*/  4, Double.class, "double", "Double", true, true)
	{
		@Override
		ParameterValueData<Double> newParameterValueData()
		{
			return new DoubleParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 8;
		}
	},

	/**
		String type where maximum length allowed is 255 characters.
		@see #TEXT
	*/
	STRING(/* Hibernate.STRING,*/  5, String.class, "string", "String", true, false)
	{
		@Override
		ParameterValueData<String> newParameterValueData()
		{
			return new StringParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : value.toString().length();
		}
	},

	/**
		Text blob type.
		@see #STRING
	*/
	TEXT(/* Hibernate.TEXT,*/  6, String.class, "text", "Text", false, false)
	{
		@Override
		ParameterValueData<String> newParameterValueData()
		{
			return new TextParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : value.toString().length();
		}
	},

	/**
		Boolean type.
	*/
	BOOLEAN(/* Hibernate.BOOLEAN,*/  7, Boolean.class, "boolean", "Boolean", false, false)
	{
		@Override
		ParameterValueData<Boolean> newParameterValueData()
		{
			return new BooleanParameterValueData();
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 1;
		}
	},

	/**
		The parameter is a date value.
	*/
	DATE(/* Hibernate.DATE,*/ 8, Date.class, "date", "Date", true, false)
	{
		@Override
		ParameterValueData<Date> newParameterValueData()
		{
			return new DateParameterValueData();
		}
		@Override
		public Object parseString(String value)
			throws InvalidDataException
		{
			return DateUtil.parseString(value);
		}
		@Override
		public int sizeOf(Object value)
		{
			return value == null ? 0 : 8;
		}
	};

	private static final Map<Integer, Type> valueMapping = new HashMap<Integer, Type>();
	private static final Map<String, Type> stringMapping = new HashMap<String, Type>();
	static
	{
		for(Type t : Type.values())
		{
			Type tt = valueMapping.put(t.getValue(), t);
			assert tt == null : "Another type with the value "+t.getValue()+" already exists";
			tt = stringMapping.put(t.getStringValue(), t);
			assert tt == null : "Another type with the string value "+t.getStringValue()+" already exists";
		}
	}
	


//	private final NullableType hibernateType;
	private final int value;
	private final Class<?> valueClass;
	private final String stringValue;
	private final String displayValue;
	private final boolean canEnumerate;
	private final boolean isNumerical;

	private Type(/*NullableType hibernateType,*/ int value, Class<?> valueClass,
		String stringValue, String displayValue, boolean canEnumerate, boolean isNumerical)
	{
	//	this.hibernateType = hibernateType;
		this.value = value;
		this.valueClass = valueClass;
		this.stringValue = stringValue;
		this.displayValue = displayValue;
		this.canEnumerate = canEnumerate;
		this.isNumerical = isNumerical;
	}

	@Override
	public String toString()
	{
		return displayValue;
	}
	
//	/**
//		Get the hibernate type of this parameter
//		@return The hibernate type of the parameter
//	*/
//	public NullableType getHibernateType()
//	{
//		return hibernateType;
//	}
//
	/**
		Get the integer value that is used when storing a type in the database.
	*/
	public int getValue()
	{
		return value;
	}

	/**
		Get the class of objects that can be used as values for this type.
		@see #isCorrectType(Object)
	*/
	public Class<?> getValueClass()
	{
		return valueClass;
	}
	
	/**
		Get the string value representation of this type.
	*/
	public String getStringValue()
	{
		return stringValue;
	}
	
	/**
		Get the type of this parameter as specified by the
		<code>java.sql.Types</code> class.
		@return The type-code of the parameter
	*/
	public int getSQLType()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return hibernateType.sqlType();
	}


	/**
		If it makes sense to use values of this type as an enumeration
		to choose from. For example, the <code>BOOLEAN</code> and <code>TEXT</code>
		type can't be enumerated.
	*/
	public boolean canEnumerate()
	{
		return canEnumerate;
	}
	
	/**
		If the values of this type are numerical values.
		@return TRUE if the values are numerical, FALSE otherwise
	*/
	public boolean isNumerical()
	{
		return isNumerical;
	}

	/**
		Check if an object is a value of the correct type. Null is allowed.
		@throws InvalidDataException If the value isn't of the correct type
	*/
	public void validate(Object value)
		throws InvalidDataException
	{
		if (value != null && !isCorrectType(value))
		{
			throw new InvalidDataException("Value '"+value+"' is a '"+value.getClass()+"', not a '"+this+"'");
		}
	}

	/**
		Check if a list of values contain only objects of the correct type.
		@throws InvalidDataException If the list contains one or more nulls or 
			values of incorrect object type
	*/
	public void validate(List<?> values)
		throws InvalidDataException
	{
		if (values == null || values.size() == 0) return;
		int i = 0;
		for (Object value : values)
		{
			if (value == null) throw new InvalidUseOfNullException("values["+i+"]");
			if (!isCorrectType(value))
			{
				throw new InvalidDataException("Value '"+value+"' is a '"+value.getClass()+"', not a '"+this+"'");
			}
			i++;
		}
	}

	/**
		Check if the value is an object of the correct type.
	*/
	public boolean isCorrectType(Object value)
	{
		return valueClass.isInstance(value);
	}

	/**
		Create a new {@link ParameterValueData} subclass which is used to
		store values of this value type in the database.
	*/
	abstract ParameterValueData<?> newParameterValueData();

	/**
		Get the size in bytes an object of this type requires when
		stored in the database. If the value if null, 0 is returned, 
		otherwise the actual value is only used if it
		affects the size, ie. it is a string.
		@param value The value to get the size of
	*/
	public abstract int sizeOf(Object value);
	
	/**
		Parse a string and return a value of the correct type.
	*/
	public Object parseString(String value)
		throws InvalidDataException
	{
		if (value == null)
		{
			return null;
		}
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			return getHibernateType().fromStringValue(value);
//		}
//		catch (final HibernateException ex)
//		{
//			throw new InvalidDataException(ex);
//		}
	}
	
	/**
		Parse an array of strings and try to convert them to values
		of the correct object type.
		@return An array of objects, or null of the array is null
		@throws InvalidDataException If at least one of the strings could not 
			be parsed to the correct type
	*/
	public Object[] parseStrings(String[] values)
		throws InvalidDataException
	{
		if (values == null) return null;
		Object[] o = new Object[values.length];
		for (int i = 0; i < values.length; ++i)
		{
			o[i] = parseString(values[i]);
		}
		return o;
	}

	/**
		Get the <code>Type</code> object when you know the integer code.
	*/
	public static Type fromValue(int value)
	{
		Type t = valueMapping.get(value);
		return t;
	}

	/**
		Get the <code>Type</code> object when you know the string value.
	*/
	public static Type fromValue(String value)
	{
		Type t = stringMapping.get(value);
		return t;
	}

}
