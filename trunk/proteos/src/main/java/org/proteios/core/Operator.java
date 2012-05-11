/*
	$Id: Operator.java 3207 2009-04-09 06:48:11Z gregory $

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

//import org.proteios.core.query.Expression;
//import org.proteios.core.query.Restriction;
//import org.proteios.core.query.Restrictions;
import java.util.HashMap;
import java.util.Map;

/**
	@author Nicklas
	@version 2.0
*/
public enum Operator
{
	AND(1, "AND", "AND", false)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			throw new InvalidDataException("Not allowed");
//		}
//	}
    ,

	OR(2, "OR", "OR", false)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			throw new InvalidDataException("Not allowed");
//		}
//	}
    ,

	NOT(3, "NOT", "NOT", false)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			throw new InvalidDataException("Not allowed");
//		}
//	}
   ,

	EQ(4, "=", "=", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.eq(lvalue, rvalue);
//		}
//	}
    ,

	NEQ(5, "!=", "!=", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.neq(lvalue, rvalue);
//		}
//	}
    ,

	LT(6, "<", "<", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.lt(lvalue, rvalue);
//		}
//	}
    ,

	LTEQ(7, "<=", "<=", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.lteq(lvalue, rvalue);
//		}
//	}
    ,

	GT(8, ">", ">", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.gt(lvalue, rvalue);
//		}
//	}
    ,

	GTEQ(9, ">=", ">=", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.gteq(lvalue, rvalue);
//		}
//	}
    ,

	LIKE(10, "LIKE", "LIKE", true)
//	{
//		@Override
//		public Restriction getRestriction(Expression lvalue, Expression rvalue)
//			throws InvalidDataException
//		{
//			return Restrictions.like(lvalue, rvalue);
//		}
//	}
    ;

	private static final Map<Integer, Operator> valueMapping = new HashMap<Integer, Operator>();
	
	static
	{
		for(Operator op : Operator.values())
		{
			Operator o = valueMapping.put(op.getValue(), op);
			assert o == null : "Another operator with the value "+op.getValue()+" already exists";
		}
	}

	private final int value;
	private final String symbol;
	private final String sqlSymbol;
	private final boolean expressionOperator;

	private Operator(int value, String symbol, String sqlSymbol, boolean expressionOperator)
	{
		this.value = value;
		this.symbol = symbol;
		this.sqlSymbol = sqlSymbol;
		this.expressionOperator = expressionOperator;
	}

	/**
		Get the integer value that is used when storing an operator in the database.
	*/
	public int getValue()
	{
		return value;
	}
	
	/**
		Get the symbol for this operator, useful for display in client applications.
	*/
	public String getSymbol()
	{
		return symbol;
	}

	/**
		Get the SQL symbol for this operator. This can be used when creating 
		SQL/HQL queries.
	*/
	public String getSqlSymbol()
	{
		return sqlSymbol;
	}
	
	public boolean isExpressionOperator()
	{
		return expressionOperator;
	}
	
//	public abstract Restriction getRestriction(Expression lvalue, Expression rvalue)
//		throws InvalidDataException;

	/**
		Get the <code>Operator</code> object when you know the integer code.
	*/
	public static Operator fromValue(int value)
	{
		Operator op = valueMapping.get(value);
		assert op != null : "operator == null for value "+value;
		return op;
	}

}
