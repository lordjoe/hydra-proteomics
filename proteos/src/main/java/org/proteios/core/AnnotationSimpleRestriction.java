/*
	$Id: AnnotationSimpleRestriction.java 3207 2009-04-09 06:48:11Z gregory $

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
//import org.proteios.core.query.Query;

/**
	Restricts a query using annotation values with a simple expression:
	<code>annotation operator value</code>.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.query.Annotations
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class AnnotationSimpleRestriction
	extends AnnotationRestriction
{
	private final Operator operator;
	private final Object value;

	/**
		Create a new annotation restriction using a simple expression:
		<code>annotation operator value</code>.
		
		@param annotationType The annotation type to use in the query
		@param operator The operator, it must be one of the expression
			operators, EQ, NEQ, etc., not a boolean operator, AND, OR, etc.
		@param value The value to use in the query, it must be of the 
			correct value type for the annotation as defined by the 
			{@link AnnotationType#getValueType()} property
		@param includeInheriting If items inherting the annotation should be returned
			by the query or not
		@throws InvalidDataException If any of the parameters are null
			or not follow the rules above.
	*/
	public AnnotationSimpleRestriction(AnnotationType annotationType, Operator operator, Object value, boolean includeInheriting)
		throws InvalidDataException
	{
		super(annotationType, includeInheriting);
		if (operator == null) throw new InvalidUseOfNullException("operator");
		if (value == null) throw new InvalidUseOfNullException("value");
		if (!operator.isExpressionOperator())
		{
			throw new InvalidDataException("Invalid operator for expression: "+operator);
		}
		if (!valueType.isCorrectType(value))
		{
			throw new InvalidDataException("Value '"+value+"' is a "+
				value.getClass()+", not a "+valueType);
		}
		this.operator = operator;
		this.value = value;
	}

	public AnnotationSimpleRestriction(int annotationTypeId, Type valueType, Operator operator, Object value, boolean includeInheriting)
		throws InvalidDataException
	{
		super(annotationTypeId, valueType, includeInheriting);
		if (operator == null) throw new InvalidUseOfNullException("operator");
		if (value == null) throw new InvalidUseOfNullException("value");
		if (!operator.isExpressionOperator())
		{
			throw new InvalidDataException("Invalid operator for expression: "+operator);
		}
		if (!valueType.isCorrectType(value))
		{
			throw new InvalidDataException("Value '"+value+"' is a "+
				value.getClass()+", not a "+valueType);
		}
		this.operator = operator;
		this.value = value;
	}
	
	/*
		From the AnnotationRestriction class
		-------------------------------------------
	*/
//	@Override
//	String getRestrictionSql(Query query, DbControl dc)
//		throws BaseException
//	{
//		org.hibernate.dialect.Dialect dialect = HibernateUtil.getDialect();
//		return "v."+dialect.openQuote()+"value"+dialect.closeQuote()+" "+operator.getSqlSymbol()+" :value";
//	}
//
//	@Override
//	void setRestrictionParameters(SQLQuery query)
//	{
//		query.setParameter("value", value);
//	}
	// -----------------------------------------

}

