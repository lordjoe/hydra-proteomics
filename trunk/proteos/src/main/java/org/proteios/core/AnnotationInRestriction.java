/*
	$Id: AnnotationInRestriction.java 3207 2009-04-09 06:48:11Z gregory $

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
import java.util.Arrays;
import java.util.List;

/**
	Restricts a query using annotation values that exists in a set
	of given values.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.query.Annotations
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class AnnotationInRestriction
	extends AnnotationRestriction
{
	private final List<?> values;

	/**
		Create a new annotation restriction.
		
		@param annotationType The annotation type to use in the query
		@param includeInheriting If items inherting the annotation should be returned
			by the query or not
		@param values An array of values, each value must be of the 
			correct value type for the annotation as defined by the 
			{@link AnnotationType#getValueType()} property
		@throws InvalidDataException If any of the parameters are null
			or the array is empty or not follow the rules above.
	*/
	public AnnotationInRestriction(AnnotationType annotationType, boolean includeInheriting, Object... values)
		throws InvalidDataException
	{
		super(annotationType, includeInheriting);
		if (values == null || values.length == 0) throw new InvalidUseOfNullException("values");
		for (int i = 0; i < values.length; ++i)
		{
			Object value = values[i];
			if (!valueType.isCorrectType(value))
			{
				throw new InvalidDataException("Value["+i+"] '"+value+"' is a "+
					value.getClass()+", not a "+valueType);
			}
		}
		this.values = Arrays.asList(values);
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
//		return "v."+dialect.openQuote()+"value"+dialect.closeQuote()+" in (:values)";
//	}
//
//	@Override
//	void setRestrictionParameters(SQLQuery query)
//	{
//		query.setParameterList("values", values);
//	}
	// -----------------------------------------

}

