/*
	$Id: ItemQuery.java 3207 2009-04-09 06:48:11Z gregory $
	
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
import java.util.List;

/**
	An implementation of the {@link Query} interface that returns item
	objects. This type of query is used for all items except those that
	are batchable, for example reporters and raw data. The result of a
	query can be returned as a list or an iterator.

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class ItemQuery<I extends BasicItem>
	extends AbstractEntityQuery
{

	/**
		The class of the item objects that are returned.
	*/
	private final Class<I> itemClass;

	/**
		The data layer class of the item objects that are returned.
	*/
	private final Class<? extends BasicData> dataClass;

	/**
		Create a new query for the specified item, using the default optional
		runtime filter.
		@param itemClass The class of the item objects that are returned
	*/
	ItemQuery(Class<I> itemClass)
	{
		super(Item.fromItemClass(itemClass), null, false);
		this.itemClass = itemClass;
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	this.dataClass = getItemType().getDataClass();
	}
//
//	/**
//		Create a new query for the specified item, using a non-default optional
//		runtime filter.
//		@param itemClass The class of the item objects that are returned
//		@param optionalFilter A runtime filter replacing the default optional filter
//			or null to not use any optional filter
//	*/
//	ItemQuery(Class<I> itemClass, QueryRuntimeFilter optionalFilter)
//	{
//		super(Item.fromItemClass(itemClass), null, false, optionalFilter);
//		this.itemClass = itemClass;
//		this.dataClass = getItemType().getDataClass();
//	}
//
//	/**
//		Execute the query and return the results as a list.
//		@param dc The <code>DbControl</code> used to access the database
//			and check permissions
//		@throws BaseException If there is an error
//	*/
//	public ItemResultList<I> list(DbControl dc)
//		throws BaseException
//	{
//		enableFilters(dc);
//
//		List<? extends BasicData> result = HibernateUtil.loadList(dataClass, getMainHqlQuery(dc));
//		int totalCount = result.size();
//
//		// Load the total count if needed and requested
//		if ((getFirstResult() >= 0 || getMaxResults() > 0) && isReturningTotalCount())
//		{
//			totalCount = HibernateUtil.loadData(Integer.class, getCountHqlQuery(dc));
//		}
//
//		disableFilters(dc);
//		return new ItemResultList<I>(result, dc, itemClass, totalCount);
//	}
//
//	/**
//		Execute the query and return the results as an iterator.
//		@param dc The <code>DbControl</code> used to access the database
//			and check permissions
//		@throws BaseException If there is an error
//	*/
//	public ItemResultIterator<I> iterate(DbControl dc)
//		throws BaseException
//	{
//		enableFilters(dc);
//		int totalCount = -1;
//
//		// Load the total count if it is requested
//		if (isReturningTotalCount())
//		{
//			totalCount = HibernateUtil.loadData(Integer.class, getCountHqlQuery(dc));
//		}
//
//		ScrollIterator<? extends BasicData> result = HibernateUtil.loadIterator(dataClass, getMainHqlQuery(dc));
//		disableFilters(dc);
//		return new ItemResultIterator<I>(result, dc, itemClass, totalCount);
//	}

}
