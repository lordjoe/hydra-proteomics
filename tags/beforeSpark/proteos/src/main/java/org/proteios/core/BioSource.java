/*
 $Id: BioSource.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.BioSourceData;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
import java.util.Set;

/**
 * This class represent biosource items. A biosource has information about the
 * source of material used in an experiment, such as a cell line or patient. A
 * biosource can be used to create {@link Sample}:s.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class BioSource
		extends BioMaterial<BioSourceData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#BIOSOURCE
	 * @see #getType()
	 */
	public static final Item TYPE = Item.BIOSOURCE;


	/**
	 * Get a query that returns biosource items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<BioSource> getQuery()
	{
		return new ItemQuery<BioSource>(BioSource.class);
	}


	BioSource(BioSourceData bioSourceData)
	{
		super(bioSourceData);
	}


	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/**
	 * Always null.
	 */
	public Set<Annotatable> getAnnotatableParents()
			throws BaseException
	{
		return null;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no {@link Sample} has been created from this biosource
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"COUNT_SAMPLES_FOR_BIOSOURCE");
//		/*
//		 * SELECT count(*) FROM SampleData s WHERE s.parent = :biosource
//		 */
//		query.setEntity("biosource", this.getData());
//		return HibernateUtil.loadData(Integer.class, query) > 0;
	}


	// -------------------------------------------
	/**
	 * Get a query that returns all samples created from this biosource.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public ItemQuery<Sample> getSamples()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<Sample> query = Sample.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("parent"), Hql
//			.entity(this)));
//		return query;
	}
}
