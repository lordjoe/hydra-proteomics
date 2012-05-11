/*
	$Id: AbstractEntityQuery.java 3207 2009-04-09 06:48:11Z gregory $
	
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

//import org.proteios.core.query.EntityQuery;
//import org.proteios.core.query.Expression;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.QueryType;
//import org.proteios.core.query.Restriction;
//import org.proteios.core.query.Select;
//import org.proteios.core.query.Selects;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
	An abstract implementation of the {@link EntityQuery} interface. This class
	doesn't support adding selections, groups or having restrictions.
	<p>
	{@link Include} options and permission control are implemented using 
	Hibernate filters. See {@link QueryRuntimeFilterFactory}.

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
abstract class AbstractEntityQuery
//	extends AbstractQuery
//	implements EntityQuery
{

	/**
		Log all parameter bindings to prepared statement.
	*/
	private static final org.apache.log4j.Logger logParam = 
		org.apache.log4j.LogManager.getLogger("org.proteios.core.query.bind");
		
	/**
		So we don't always have to call logParam.debug()
	*/
	private static final boolean debugEnabled = logParam.isDebugEnabled();

	/**
		The item type returned by the query.
	*/
	private final Item itemType;
	
	/**
		If the query should be executed in a stateless session or not.
	*/
	private final boolean stateless;
//
//	/**
//		The required runtime filter.
//	*/
//	private final QueryRuntimeFilter requiredFilter;
//
//	/**
//		Optional runtime filter.
//	*/
//	private final QueryRuntimeFilter optionalFilter;
//
//	/**
//		The manager of the runtime query filters.
//	*/
//	private QueryRuntimeFilterManager filterManager;
//
//	/**
//		The most recent DbControl used to execute the main query.
//	*/
//	private DbControl lastMainDc;
//
//	/**
//		The most recent DbControl used to execute the count query.
//	*/
//	private DbControl lastCountDc;
//
//	/**
//		The most recent main query.
//	*/
//	private org.hibernate.Query lastMainQuery;
//
//	/**
//		The most recent count query.
//	*/
//	private org.hibernate.Query lastCountQuery;
//
//	/**
//		If the results should be cached or not.
//	*/
//	private boolean cacheResults;
//
//	/**
//		Flags specifying which items to include.
//	*/
//	private EnumSet<Include> includes;
	
	/**
		Create a new query returning items of the specified item type
		using the default optional runtime filter.
		@param itemType The type of items that are returned
		@param stateless TRUE if the stateless Hibernate session should be used, FALSE
			if the regular Hibernate session should be used
	*/
	AbstractEntityQuery(Item itemType, String entityName, boolean stateless)
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	this(itemType, entityName, stateless, QueryRuntimeFilterFactory.getOptionalFilter(itemType));
	}
	
//	/**
//		Create a new query returning items of the specified type with a non-default
//		optional runtime filter.
//		@param itemType The type of items that are returned
//		@param stateless TRUE if the stateless Hibernate session should be used, FALSE
//			if the regular Hibernate session should be used
//		@param optionalFilter A runtime filter replacing the default optional filter
//			or null to not use any optional filter
//	*/
//	AbstractEntityQuery(Item itemType, String entityName, boolean stateless, QueryRuntimeFilter optionalFilter)
//	{
//		super(entityName == null ? itemType.getDataClass().getName() : entityName);
//		this.itemType = itemType;
//		this.stateless = stateless;
//		this.requiredFilter = QueryRuntimeFilterFactory.getRequiredFilter(itemType);
//		this.optionalFilter = optionalFilter;
//		super.selectPermanent(Selects.expression(Hql.alias(itemType.getAlias()), null));
//		this.includes = EnumSet.of(Include.NOT_REMOVED, Include.MINE, Include.IN_PROJECT);
//	}
//
//
//	/*
//		From the Query interface
//		-------------------------------------------
//	*/
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void select(Select select)
//	{
//		throw new UnsupportedOperationException();
//	}
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void selectPermanent(Select select)
//	{
//		throw new UnsupportedOperationException();
//	}
//
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void group(Expression expression)
//	{
//		throw new UnsupportedOperationException();
//	}
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void groupPermanent(Expression expression)
//	{
//		throw new UnsupportedOperationException();
//	}
//
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void having(Restriction restriction)
//	{
//		throw new UnsupportedOperationException();
//	}
//	/**
//		Not supported.
//		@throws UnsupportedOperationException Always
//	*/
//	@Override
//	public void havingPermanent(Restriction restriction)
//	{
//		throw new UnsupportedOperationException();
//	}
//
//	/**
//		@return {@link QueryType#HQL}
//	*/
//	public QueryType getQueryType()
//	{
//		return QueryType.HQL;
//	}
//
//	/**
//		The alias of the item that is returned by this query.
//		@see Item#getAlias()
//	*/
//	public String getRootAlias()
//	{
//		return itemType.getAlias();
//	}
//
//	public boolean isReadonly()
//	{
//		return lastMainQuery != null;
//	}
//
//	/**
//		Reset all non-permanent query elements of the query and
//		clear cached queries.
//	*/
//	@Override
//	public void reset()
//	{
//		super.reset();
//		lastMainQuery = null;
//		lastCountQuery = null;
//		lastMainDc = null;
//		lastCountDc = null;
//	}
//	public int count(DbControl dc)
//		throws BaseException
//	{
//		// Base uses long here instead of int
//		enableFilters(dc);
//	 	int count = HibernateUtil.loadData(Integer.class, getCountHqlQuery(dc));
//	 	disableFilters(dc);
//	 	return  count;
//
//	}
//
//	// -------------------------------------------
//	/*
//		From the HqlQuery interface
//		-------------------------------------------
//	*/
//	public void setCacheResult(boolean flag)
//	{
//		this.cacheResults = flag;
//	}
//	public boolean isCachingResult()
//	{
//		return cacheResults;
//	}
//	// -------------------------------------------
//
//	/*
//		From the EntityQuery interface
//		-------------------------------------------
//	*/
//	public Item getItemType()
//	{
//		return itemType;
//	}
//	public void include(Include... includes)
//	{
//		for (Include i : includes)
//		{
//			this.includes.add(i);
//		}
//	}
//	public void exclude(Include... excludes)
//	{
//		for (Include i : excludes)
//		{
//			this.includes.remove(i);
//		}
//	}
//	public boolean isIncluded(Include... includes)
//	{
//		boolean isIncluded = true;
//		for (Include i : includes)
//		{
//			isIncluded = isIncluded && this.includes.contains(i);
//		}
//		return isIncluded;
//	}
//	// -------------------------------------------
//
//	/**
//		Build the main query and set parameter values for it. If the query has been
//		executed with the same <code>DbControl</code> before and hasn't been {@link #reset()}
//		the cached query is returned. If the parameters values have been changed the new
//		values are used.
//
//		@param dc The DbControl to use for executing the query
//		@return A <code>org.hibernate.Query</code> object
//	*/
//	org.hibernate.Query getMainHqlQuery(DbControl dc)
//	{
//		if (lastMainQuery == null || lastMainDc != dc)
//		{
//			// If we are using the same DbControl as before we don't have to rebuild the query
//			String mainHql = getMainQuery(dc);
//			if (stateless)
//			{
//				lastMainQuery =  HibernateUtil.createQuery(dc.getStatelessSession(), mainHql);
//			}
//			else
//			{
//				lastMainQuery =  HibernateUtil.createQuery(dc.getHibernateSession(), mainHql);
//			}
//		}
//		lastMainDc = dc;
//		lastMainQuery.setCacheable(isCachingResult());
//		setParameters(lastMainQuery, getPermanentParameters());
//		setParameters(lastMainQuery, getParameters());
//		if (getFirstResult() >= 0) lastMainQuery.setFirstResult(getFirstResult());
//		if (getMaxResults() > 0) lastMainQuery.setMaxResults(getMaxResults());
//		return lastMainQuery;
//	}
//
//	/**
//		Build the count query and set parameter values for it. If the query has been
//		executed with the same <code>DbControl</code> before and hasn't been {@link #reset()}
//		the cached query is returned. If the parameters values have been changed the new
//		values are used.
//
//		@param dc The DbControl to use for executing the query
//		@return A <code>org.hibernate.Query</code> object
//	*/
//	org.hibernate.Query getCountHqlQuery(DbControl dc)
//	{
//		if (lastCountQuery == null || lastCountDc != dc)
//		{
//			// If we are using the same DbControl as before we don't have to rebuild the query
//			String countHql = getCountQuery(dc);
//			if (stateless)
//			{
//				lastCountQuery =  HibernateUtil.createQuery(dc.getStatelessSession(), countHql);
//			}
//			else
//			{
//				lastCountQuery =  HibernateUtil.createQuery(dc.getHibernateSession(), countHql);
//			}
//		}
//		lastCountDc = dc;
//		lastCountQuery.setCacheable(true);
//		setParameters(lastCountQuery, getPermanentParameters());
//		setParameters(lastCountQuery, getParameters());
//		return lastCountQuery;
//	}
//
//	/**
//		Enable runtime query filters for the query.
//		@see #disableFilters(DbControl)
//	*/
//	void enableFilters(DbControl dc)
//	{
//		if (requiredFilter != null || optionalFilter != null)
//		{
//			filterManager = new QueryRuntimeFilterManager(dc);
//			if (requiredFilter != null) requiredFilter.enableFilters(filterManager, this, dc);
//			if (optionalFilter != null) optionalFilter.enableFilters(filterManager, this, dc);
//		}
//	}
//
//	/**
//		Disable all enabled runtime query filters for the query.
//		@see #enableFilters(DbControl)
//	*/
//	void disableFilters(DbControl dc)
//	{
//		if (filterManager != null)
//		{
//			filterManager.disableAll();
//			filterManager = null;
//		}
//	}
//
//	private static final Set<Integer> ZERO_SET = Collections.singleton(0);
//
//	/**
//		Set parameter values on a query.
//		@param query The Hibernate query object
//		@param parameters A map containing parameter names and values
//	*/
//	private void setParameters(org.hibernate.Query query, Map<String, Object> parameters)
//	{
//		if (parameters == null) return;
//		if (debugEnabled)
//		{
//			logParam.debug("Binding parameters to query: " + query.getQueryString());
//		}
//		for (Map.Entry<String, Object> entry : parameters.entrySet())
//		{
//			String name = entry.getKey();
//			Object value = entry.getValue();
//			Type valueType = getParameterType(name);
//			if (value instanceof Collection)
//			{
//				Collection c = (Collection)value;
//				if (debugEnabled)
//				{
//					logParam.debug("Binding collection '"+value+"' to parameter :"+name);
//				}
//				if (valueType == null)
//				{
//					query.setParameterList(name, c.size() == 0 ? ZERO_SET : c);
//				}
//				else
//				{
//					query.setParameterList(name, c.size() == 0 ? ZERO_SET : c, valueType.getHibernateType());
//				}
//			}
//			else
//			{
//				if (debugEnabled)
//				{
//					logParam.debug("Binding '"+value+"' to parameter :"+name);
//				}
//				if (valueType == null)
//				{
//					query.setParameter(name, value);
//				}
//				else
//				{
//					query.setParameter(name, value, valueType.getHibernateType());
//				}
//			}
//		}
//	}

}
