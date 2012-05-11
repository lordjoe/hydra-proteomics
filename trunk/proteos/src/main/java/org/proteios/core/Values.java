/*
	$Id: Values.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.BasicData;
import org.proteios.core.data.BatchableData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
	Utility class to handle the conversion between data objects and
	item objects. Only item objects are safe to be exposed to client
	applications and plugins, but the database contains data objects.
	
	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
final class Values
{

	/**
		Convert a a data value to a item value. Ie. 
		{@link BasicData} objects will be converted to their correspoding 
		{@link BasicItem} objects, <code>Date</code>:s will be copied
		since they are mutable, etc. This method is needed for
		example by the {@link Annotation#getValues()} method to make
		sure that the database can't be modified without the proper permissions.
		<p>
		Objects implementing the {@link BatchableData} interface are
		not converted since these objects are safe for use with clients
		if they are evicted from the Hibernate session.
	
		@param dc The DbControl to use for item conversion and eviction
		@param value The value to convert
		@throws PermissionDeniedException If the value is an item
			which the logged in user doesn't have read permission for
		@throws BaseException If there is another error
	*/
//	static Object getItemValue(DbControl dc, Object value)
//		throws PermissionDeniedException, BaseException
//	{
//		if (value instanceof Date)
//		{
//			value = DateUtil.copy((Date)value);
//		}
//		else if (value instanceof BasicData)
//		{
//			if (value instanceof BatchableData)
//			{
//				HibernateUtil.evictData(dc.getHibernateSession(), (BasicData)value);
//			}
//			else
//			{
//				value = dc.getItem(BasicItem.class, (BasicData)value);
//			}
//		}
//		return value;
//	}

//	/**
//		Convert a list of data values to item values.
//		@see #getItemValue(DbControl, Object)
//	*/
//	static List<Object> getItemValues(DbControl dc, List<?> dataValues)
//		throws PermissionDeniedException, ItemNotFoundException, BaseException
//	{
//		// Many-to-any mapping in ItemParameter will not create checked foreign keys.
//		try
//		{
//			List<Object> itemValues = new ArrayList<Object>(dataValues.size());
//			for (int i = 0; i < dataValues.size(); i++)
//			{
//				itemValues.add(i, getItemValue(dc, dataValues.get(i)));
//			}
//			return itemValues;
//		}
//		catch (org.hibernate.ObjectNotFoundException ex)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(ex.getEntityName())+
//				"[id="+ex.getIdentifier()+"]");
//		}
//	}

	/**
		Convert an item values to a data value. Ie. 
		{@link BasicItem} objects will be converted to their correspoding 
		{@link BasicData} objects, <code>Date</code>:s will be copied
		since they are mutable, etc.
	
		@param value The value to convert
	*/
	static Object getDataValue(Object value)
	{
		if (value instanceof Date)
		{
			value = DateUtil.copy((Date)value);
		}
		else if (value instanceof BasicItem)
		{
			value = ((BasicItem)value).getData();
		}
		return value;
	}
	
	/**
		Convert a list of item values to data values. 
		@see #getDataValue(Object)
	*/
	static List<Object> getDataValues(List<?> itemValues)
	{
		List<Object> dataValues = new ArrayList<Object>(itemValues.size());
		for (int i = 0; i < itemValues.size(); i++)
		{
			dataValues.add(i, getDataValue(itemValues.get(i)));
		}
		return dataValues;
	}
}
