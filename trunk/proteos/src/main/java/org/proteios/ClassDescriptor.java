/*
 $Id: code_templates.xml 1916 2007-08-31 09:54:00Z jari $
 
 Copyright (C) 2006, 2007 Gregory Vincic
 
 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.
 
 This file is part of Proteios.
 Available at http://www.proteios.org/
 
 Proteios is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 Proteios is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 */
package org.proteios;

import org.proteios.core.Nameable;
import org.proteios.core.data.FileData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Through the ClassDescriptor you can find methods by defined attributes.
 * 
 * @author gregory
 * @see AttributeDefinition
 */
public class ClassDescriptor
{
	private Class<?> describedClass;
	private TypeUtil util;


	/**
	 * @param cls to describe
	 */
	public ClassDescriptor(Class<?> cls)
	{
		this.describedClass = cls;
		this.util = new TypeUtil();
	}


	public Method getReadMethod(AttributeDefinition atr)
			throws SecurityException, NoSuchMethodException
	{
		Method method = null;
		String name = atr.getReadMethodName();
		method = describedClass.getMethod(name);
		return method;
	}


	public Method getWriteMethod(AttributeDefinition atr)
			throws SecurityException, NoSuchMethodException
	{
		Method method = null;
		String name = atr.getWriteMethodName();
		method = describedClass.getMethod(name, atr.getAttributeType());
		return method;
	}


	/**
	 * @param filter to apply
	 * @return a list of attributes filtered through the above filter
	 */
	public List<AttributeDefinition> getAttributes(AttributeFilter filter)
	{
		return sort(getAttributes(), filter.getDefaultOrder());
	}


	/**
	 * Returns a filtered list of attributes.
	 * 
	 * @param attributes List of attribute definitions to be sorted.
	 * @param order List of column key strings in desired sort order.
	 * @return List<AttributeDefinition> Sorted list of attribute definitions.
	 */
	public List<AttributeDefinition> sort(List<AttributeDefinition> attributes,
			List<String> order)
	{
		List<AttributeDefinition> sorted = new ArrayList<AttributeDefinition>(
			attributes.size());
		for (String key : order)
		{
			for (int i = attributes.size() - 1; i >= 0; i--)
			{
				AttributeDefinition ad = attributes.get(i);
				if (ad.getKey().equals(key))
				{
					sorted.add(ad);
				}
			}
		}
		// Remove the sorted ones from the rest
		for (AttributeDefinition ad : sorted)
		{
			int i = attributes.indexOf(ad);
			if (i != -1)
				attributes.remove(i);
		}
		// Now append the rest of the attributes to the sorted ones
		sorted.addAll(attributes);
		return sorted;
	}


	/**
	 * Currently supports Primitive, Nameable and Enumerated attributes
	 * 
	 * @return list of readable attributes, those with 'get' or 'is' prefixes,
	 *         or null if none are found.
	 */
	public List<AttributeDefinition> getAttributes()
	{
		List<AttributeDefinition> result = null;
		Method[] methods = describedClass.getMethods();
		for (Method method : methods)
		{
			String name = method.getName();
			int keyIndex = 0;
			if (method.getParameterTypes().length == 0 && method
				.getReturnType() != null)
			{
				// No parameters to this method, we assume it's a read method
				Class<?> cls = method.getReturnType();
				AttributeDefinition atr = null;
				if (util.isString(cls) || util.isBoolean(cls) || util
					.isDouble(cls) || util.isFloat(cls) || util.isInt(cls) || util
					.isLong(cls))
				{
					String writePrefix = "set";
					String readPrefix = null;
					if (name.startsWith("get"))
					{
						keyIndex = 3;
						readPrefix = "get";
					}
					else if (name.startsWith("is"))
					{
						keyIndex = 2;
						readPrefix = "is";
					}
					if (keyIndex != 0)
					{
						String key = name.substring(keyIndex);
						atr = new AttributeDefinition(describedClass, cls, key,
							readPrefix, writePrefix);
					}
				}
				else if (Nameable.class.isAssignableFrom(cls))
				{
					// Assume that the attributes are always get/set methods
					String key = name.substring(3);
                    throw new UnsupportedOperationException("Fix This"); // ToDo
//					NameReader reader = new NameReader();
//					atr = new AttributeDefinition(describedClass, cls, key,
//						reader);
				}
				else if (FileData.class.isAssignableFrom(cls))
				{
					// Assume that the attributes are always get/set methods
					String key = name.substring(3);
                    throw new UnsupportedOperationException("Fix This"); // ToDo
//					NameReader reader = new NameReader();
//					atr = new AttributeDefinition(describedClass, cls, key,
//						reader);
				}
				else if (cls != null && cls.isEnum())
				{
					String key = name.substring(3);
                    throw new UnsupportedOperationException("Fix This"); // ToDo
//					EnumReader reader = new EnumReader();
//					atr = new AttributeDefinition(describedClass, cls, key,
//						reader);
				}
				else if (Date.class.isAssignableFrom(cls))
				{
					String key = name.substring(3);
                    throw new UnsupportedOperationException("Fix This"); // ToDo
//					DateReader reader = new DateReader();
//					atr = new AttributeDefinition(describedClass, cls, key,
//						reader);
				}
				if (atr != null)
				{
					if (result == null)
						result = new ArrayList<AttributeDefinition>();
					result.add(atr);
				}
			}
		}
		return result;
	}


	public Class<?> getDescribedClass()
	{
		return describedClass;
	}


	/**
	 * @param obj to read the attribute of
	 * @param attributeDefinition describing the attribute
	 * @return the value of that attribute or null
	 */
	public Object readValue(Object obj, AttributeDefinition attributeDefinition)
	{
		Object value = null;
		if (obj.getClass().equals(describedClass))
		{
			try
			{
				Method method = getReadMethod(attributeDefinition);
				value = method.invoke(obj);
			}
			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}
}
