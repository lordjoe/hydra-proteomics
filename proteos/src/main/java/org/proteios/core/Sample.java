/*
 $Id: Sample.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.SampleData;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
import java.util.Set;

/**
 * This class is used to represent sample items. A sample is the actual sample
 * taken from a {@link BioSource}. A sample can also be created by combining
 * other samples into a new sample. A process is known as pooling. It is also
 * possible to create standalone samples, with no reference to a biosource or to
 * other samples.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class Sample
		extends MeasuredBioMaterial<SampleData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#SAMPLE
	 * @see #getType()
	 */
	public static final Item TYPE = Item.SAMPLE;


	/**
	 * Get an <code>Sample</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for permission
	 *        checking and database access.
	 * @param id The id of the item to load
	 * @return The <code>Sample</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission to the item
	 * @throws BaseException If there is another error
	 */
//	public static Sample getById(DbControl dc, int id)
//			throws ItemNotFoundException, PermissionDeniedException,
//			BaseException
//	{
//		Sample s = dc.loadItem(Sample.class, id);
//		if (s == null)
//			throw new ItemNotFoundException("Sample[id=" + id + "]");
//		return s;
//	}
//

	/**
	 * Get a query that returns samples.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public static ItemQuery<Sample> getQuery()
	{
		return new ItemQuery<Sample>(Sample.class);
	}


	Sample(SampleData sampleData)
	{
		super(sampleData);
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
	/*
	 * From the Annotatable interface
	 * -------------------------------------------
	 */
	/**
	 * Get the biosource or pooled samples.
	 */
	public Set<Annotatable> getAnnotatableParents()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		Set<Annotatable> annotatable = new HashSet<Annotatable>();
//		try
//		{
//			if (isPooled())
//			{
//				annotatable.addAll(getCreationEvent().getSources().list(
//					getDbControl()));
//			}
//			else if (getData().getParent() != null)
//			{
//				annotatable.add(getBioSource());
//			}
//		}
//		catch (PermissionDeniedException ex)
//		{}
//		return annotatable;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no {@link Extract}:s has been created from this item
	 * <li>no {@link Sample}:s has been created from this item
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"COUNT_EXTRACTS_FOR_SAMPLE");
//		/*
//		 * SELECT count(*) FROM ExtractData e WHERE e.parent = :sample
//		 */
//		query.setEntity("sample", this.getData());
//		boolean used = HibernateUtil.loadData(Integer.class, query) > 0;
//		return used || super.isUsed();
	}


	// -------------------------------------------
	/**
	 * Get the {@link BioSource} that is the parent of this sample.
	 * 
	 * @return A <code>BioSource</code> object or null if this is a pooled or
	 *         standalone sample
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission for the biosource
	 * @throws BaseException If there is another error
	 */
	public BioSource getBioSource()
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return getDbControl().getItem(BioSource.class, getData().getParent());
	}


	/**
	 * Set the {@link BioSource} item that is the the parent of this sample.
	 * 
	 * @param bioSource The parent <code>BioSource</code> item
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission for this sample or use permission for the
	 *         biosource
	 * @throws InvalidDataException If this is a pooled sample
	 * @throws BaseException If there is another error
	 */
	public void setBioSource(BioSource bioSource)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		if (bioSource != null)
			bioSource.checkPermission(Permission.USE);
		if (isPooled())
		{
			throw new InvalidDataException(
				"A pooled sample can't have a parent biosource");
		}
		getData().setParent(bioSource == null ? null : bioSource.getData());
	}

	/**
	 * Get a query that returns all extracts created from this sample.
	 * 
	 * @return An {@link ItemQuery} object
	 */
//	public ItemQuery<Extract> getExtracts()
//			throws BaseException
//	{
//		ItemQuery<Extract> query = Extract.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("parent"), Hql
//			.entity(this)));
//		return query;
//	}
}
