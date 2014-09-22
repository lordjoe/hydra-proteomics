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

import org.proteios.core.Type;
 
/**
 * @author gregory
 */
public class TypeUtil
{
	public Object parse(Class<?> cls, String s)
	{
		Object to = null;
		if (isInt(cls))
			to = Integer.parseInt(s);
		else if (isFloat(cls))
			to = Float.parseFloat(s);
		else if (isDouble(cls))
			to = Double.parseDouble(s);
		else if (isLong(cls))
			to = Long.parseLong(s);
		else if (isBoolean(cls))
			to = Boolean.parseBoolean(s);
		else if (isString(cls))
			to = s;
		else if (cls.isEnum())
			to = Integer.parseInt(s);
		return to;
	}


	public Type getType(Class<?> cls)
	{
		Type to;
		if (Integer.class.isAssignableFrom(cls))
			to = Type.INT;
		else if (isFloat(cls))
			to = Type.FLOAT;
		else if (isDouble(cls))
			to = Type.DOUBLE;
		else if (isLong(cls))
			to = Type.LONG;
		else if (isBoolean(cls))
			to = Type.BOOLEAN;
		else if (isString(cls))
			to = Type.STRING;
		else if (isEnum(cls))
			to = Type.INT;
		else
			to = null;
		return to;
	}


	/**
	 * @param cls
	 * @return true if cls is a String
	 */
	public boolean isString(Class<?> cls)
	{
		return String.class.isAssignableFrom(cls);
	}


	/**
	 * @param cls
	 * @return true if cls is an enum
	 */
	public boolean isEnum(Class<?> cls)
	{
		return cls.isEnum();
	}


	/**
	 * @param cls
	 * @return true if cls is Boolean or boolean
	 */
	public boolean isBoolean(Class<?> cls)
	{
		return Boolean.class.isAssignableFrom(cls) || boolean.class
			.isAssignableFrom(cls);
	}


	/**
	 * @param cls to check
	 * @return true if cls is an Integer or int
	 */
	public boolean isInt(Class<?> cls)
	{
		return Integer.class.isAssignableFrom(cls) || int.class
			.isAssignableFrom(cls);
	}


	/**
	 * @param cls
	 * @return true if cls is a Float or float
	 */
	public boolean isFloat(Class<?> cls)
	{
		return Float.class.isAssignableFrom(cls) || float.class
			.isAssignableFrom(cls);
	}


	/**
	 * @param cls
	 * @return true if cls is a Double or double
	 */
	public boolean isDouble(Class<?> cls)
	{
		return Double.class.isAssignableFrom(cls) || double.class
			.isAssignableFrom(cls);
	}


	/**
	 * @param cls
	 * @return true if cls is a Long or long
	 */
	public boolean isLong(Class<?> cls)
	{
		return Long.class.isAssignableFrom(cls) || long.class
			.isAssignableFrom(cls);
	}
}
