/*
	$Id: AnnotationBetweenRestriction.java 3207 2009-04-09 06:48:11Z gregory $

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
	Restricts a query using annotation values between a low and high
	value.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.query.Annotations
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class AnnotationBetweenRestriction
	extends AnnotationRestriction
{
	private final Object lowValue;
	private final Object highValue;

	/**
		Create a new annotation restriction.
		
		@param annotationType The annotation type to use in the query
		@param lowValue The low value to use in the query, it must be of the 
			correct value type for the annotation as defined by the 
			{@link AnnotationType#getValueType()} property
		@param highValue The high value to use in the query, it must be of the 
			correct value type for the annotation as defined by the 
			{@link AnnotationType#getValueType()} property
		@param includeInheriting If items inherting the annotation should be returned
			by the query or not
		@throws InvalidDataException If any of the parameters are null
			or not follow the rules above.
	*/
	public AnnotationBetweenRestriction(AnnotationType annotationType, Object lowValue, Object highValue, boolean includeInheriting)
		throws InvalidDataException
	{
		super(annotationType, includeInheriting);
		if (lowValue == null) throw new InvalidUseOfNullException("lowValue");
		if (highValue == null) throw new InvalidUseOfNullException("highValue");
		if (!valueType.isCorrectType(lowValue))
		{
			throw new InvalidDataException("Low value '"+lowValue+"' is a "+
				lowValue.getClass()+", not a "+valueType);
		}
		if (!valueType.isCorrectType(highValue))
		{
			throw new InvalidDataException("Hig value '"+highValue+"' is a "+
				highValue.getClass()+", not a "+valueType);
		}
		this.lowValue = lowValue;
		this.highValue = highValue;
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
//		return "v."+dialect.openQuote()+"value"+dialect.closeQuote()+" between :lowValue and :highValue";
//	}
//
//	@Override
//	void setRestrictionParameters(SQLQuery query)
//	{
//		query.setParameter("lowValue", lowValue);
//		query.setParameter("highValue", highValue);
//	}
	// -----------------------------------------


}

