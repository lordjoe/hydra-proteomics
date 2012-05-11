/*
	$Id: AnnotationRestriction.java 3207 2009-04-09 06:48:11Z gregory $

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

//import org.hibernate.SQLQuery;
//import org.hibernate.dialect.Dialect;
//import org.proteios.core.query.Query;
//import org.proteios.core.query.Restriction;

/**
	Base class for creating restrictions based on annotations.
	Subclasses must implent the {@link #getRestrictionSql(Query, DbControl)}
	and {@link #setRestrictionParameters(SQLQuery)}
	method.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.query.Annotations
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
abstract class AnnotationRestriction
//	implements Restriction
{
	/**
		Log all SQL statements.
	*/
	private static final org.apache.log4j.Logger logSql = 
		org.apache.log4j.LogManager.getLogger("org.proteios.core.query.ql");
	
	/**
		So we don't always have to call logSql.debug()
	*/
	private static final boolean debugSqlEnabled = logSql.isDebugEnabled();
	
	final Type valueType;
	final int annotationTypeId;
	final boolean includeInheriting;

	/**
		Create a new annotation restriction for an annotation type.
		@param annotationType The annotation type to use in the query
	*/
	AnnotationRestriction(AnnotationType annotationType, boolean includeInheriting)
		throws InvalidDataException
	{
		if (annotationType == null) throw new InvalidUseOfNullException("annotationType");
		this.annotationTypeId = annotationType.getId();
		this.valueType = annotationType.getValueType();
		this.includeInheriting = includeInheriting;
	}

	/**
		Create a new annotation restriction given the id and value type of
		an annotation. It is not verified that there exists an annotation
		type with the specified id or that it's value type is the one specified.
		The only thing that will happen is that the restriction will not match
		any annotations.
		@param annotationTypeId The id of the annotation type
		@param valueType The type of values for annotions
	*/
	AnnotationRestriction(int annotationTypeId, Type valueType, boolean includeInheriting)
		throws InvalidDataException
	{
		if (valueType == null) throw new InvalidUseOfNullException("valueType");
		this.annotationTypeId = annotationTypeId;
		this.valueType = valueType;
		this.includeInheriting = includeInheriting;
	}

	/*
		From the Restriction interface
		-------------------------------------------
	*/
//	public String toQl(Query query, DbControl dc)
//		throws BaseException
//	{
//		String valueTable = null;
//		if (valueType == Type.INT)
//		{
//			valueTable = "IntegerValues";
//		}
//		else if (valueType == Type.LONG)
//		{
//			valueTable = "LongValues";
//		}
//		else if (valueType == Type.FLOAT)
//		{
//			valueTable = "FloatValues";
//		}
//		else if (valueType == Type.DOUBLE)
//		{
//			valueTable = "DoubleValues";
//		}
//		else if (valueType == Type.STRING)
//		{
//			valueTable = "StringValues";
//		}
//		else if (valueType == Type.TEXT)
//		{
//			valueTable = "TextValues";
//		}
//		else if (valueType == Type.DATE)
//		{
//			valueTable = "DateValues";
//		}
//		else if (valueType == Type.BOOLEAN)
//		{
//			valueTable = "BooleanValues";
//		}
//		else
//		{
//			throw new AssertionError("Invalid value type: "+valueType);
//		}
//
//		Dialect dialect = HibernateUtil.getDialect();
//		char oq = dialect.openQuote();
//		char cq = dialect.closeQuote();
//
//		String select = includeInheriting ? "a."+oq+"annotationset_id"+cq+", a."+oq+"id"+cq : "a."+oq+"annotationset_id"+cq;
//		String sql = "SELECT "+select+
//			" FROM "+oq+"Annotations"+cq+" a, "+oq+valueTable+cq+" v"+
//			" WHERE a."+oq+"value_id"+cq+" = v."+oq+"id"+cq+" "+
//			" AND a."+oq+"annotationtype_id"+cq+" = :annotationType"+
//			" AND "+getRestrictionSql(query, dc);
//
//		org.hibernate.Session session = dc.getHibernateSession();
//		SQLQuery sqlQuery = HibernateUtil.createSqlQuery(session, sql);
//		sqlQuery.addScalar("annotationset_id", org.hibernate.Hibernate.INTEGER);
//		if (includeInheriting)
//		{
//			sqlQuery.addScalar("id", org.hibernate.Hibernate.INTEGER);
//		}
//		sqlQuery.setInteger("annotationType", annotationTypeId);
//		setRestrictionParameters(sqlQuery);
//
//		String annotationSetIds = null;
//		if (includeInheriting)
//		{
//			if (debugSqlEnabled) logSql.debug("Executing annotation query: " + sql);
//			List<Object[]> twoIds = HibernateUtil.loadList(Object[].class, sqlQuery);
//			StringBuilder annotationSets = new StringBuilder("0");
//			StringBuilder annotations = new StringBuilder("0");
//			for (Object[] id : twoIds)
//			{
//				annotationSets.append(",").append(id[0]);
//				annotations.append(",").append(id[1]);
//			}
//			annotationSetIds = annotationSets.toString();
//			String annotationIds = annotations.toString();
//
//			if (twoIds.size() > 0)
//			{
//				sql = "SELECT ia."+oq+"annotationset_id"+cq+" AS sid"+
//					" FROM "+oq+"InheritedAnnotations"+cq+" ia"+
//					" WHERE ia."+oq+"annotation_id"+cq+" IN ("+annotationIds+")"+
//					" UNION"+
//					" SELECT ias."+oq+"annotationset_id"+cq+" AS sid"+
//					" FROM "+oq+"InheritedAnnotationSets"+cq+" ias"+
//					" WHERE ias."+oq+"inherited_id"+cq+" IN ("+annotationSetIds+")";
//
//				sqlQuery = HibernateUtil.createSqlQuery(session, sql);
//				sqlQuery.addScalar("annotationset_id", org.hibernate.Hibernate.INTEGER);
//				if (debugSqlEnabled) logSql.debug("Executing annotation query: " + sql);
//				List<Integer> ids = HibernateUtil.loadList(Integer.class, sqlQuery);
//				annotationSets = new StringBuilder(annotationSetIds);
//				for (Integer id : ids)
//				{
//					annotationSets.append(",").append(id);
//				}
//				annotationSetIds = annotationSets.toString();
//			}
//		}
//		else
//		{
//			if (debugSqlEnabled) logSql.debug("Executing annotation query: " + sql);
//			List<Integer> ids = HibernateUtil.loadList(Integer.class, sqlQuery);
//			StringBuilder annotationSets = new StringBuilder("0");
//			for (Integer id : ids)
//			{
//				annotationSets.append(",").append(id);
//			}
//			annotationSetIds = annotationSets.toString();
//		}
//		String hql = query.getRootAlias()+".annotationSet IN ("+annotationSetIds+")";
//		return hql;
//	}
	// -----------------------------------------

	/**
		Get an SQL fragment that includes the actual restriction.
		Use 'v.value' to access the annotation value. Avoid including
		literal values originating from a user, especially strings, since it may lead 
		to security holes unless the values are checked
		for dangerous characters. Use a parameter placeholder instead. A parameter
		placeholder is a colon (:) followed by a name.
		Example:
		<pre class="code">
v.value = :theValue
</code>
		The <code>setRestrictionParameters</code> method is called to 
		allow the subclass to set the value of the parameters specified
		in the SQL fragment.

		@param query The query object we are about to execute
		@return An SQL fragment
		@see #setRestrictionParameters(SQLQuery)
	*/
//	abstract String getRestrictionSql(Query query, DbControl dc)
//		throws BaseException;
//
//	/**
//		Set the value for all parameters defined in the SQL fragment
//		returned by {@link #getRestrictionSql(Query, DbControl)}.
//		@param query The Hibernate <code>SQLQuery</code> object which is
//			used to query the database
//	*/
//	abstract void setRestrictionParameters(SQLQuery query);
	
}

