/*
 $Id: PolyPeptide.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

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

import org.proteios.core.data.ObservedModificationData;
import org.proteios.core.data.PolyPeptideData;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is the base class for for the two types of polypeptide: {@link Protein}
 * and {@link Peptide}.
 * 
 * @author Fredrik
 * @version 2.0
 */
public abstract class PolyPeptide<D extends PolyPeptideData>
		extends AnnotatedItem<D>
{
	PolyPeptide(D polyPeptideData)
	{
		super(polyPeptideData);
	}

	/**
	 * The maximum length of the sequence that can be stored in the database.
	 * 
	 * @see #setSequence(String)
	 */
	public static final int MAX_SEQUENCE_LENGTH = PolyPeptideData.MAX_SEQUENCE_LENGTH;


	/**
	 * Get the sequence of the polypeptide.
	 * 
	 * @return The sequence for the polypeptide
	 */
	public String getSequence()
	{
		return getData().getSequence();
	}


	/**
	 * Set the sequecne of the PolyPeptide. The value may be null but must not
	 * be longer than the value specified by the {@link #MAX_SEQUENCE_LENGTH}
	 * constant.
	 * 
	 * @param sequence The new value for the sequence
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the string is too long
	 */
	public void setSequence(String sequence)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setSequence(
			StringUtil.setNullableString(sequence, "sequence",
				MAX_SEQUENCE_LENGTH));
	}

	/**
	 * The maximum length of the lsid that can be stored in the database.
	 * 
	 * @see #setLsid(String)
	 */
	public static final int MAX_LSID_LENGTH = PolyPeptideData.MAX_LSID_LENGTH;


	/**
	 * Get the lsid of the polypeptide.
	 * 
	 * @return The lsid for the polypeptide
	 */
	public String getLsid()
	{
		return getData().getLsid();
	}


	/**
	 * Set the LSID of the polypeptide. The value may be null but must not be
	 * longer than the value specified by the {@link #MAX_LSID_LENGTH} constant.
	 * 
	 * @param lsid The new value for the Lsid
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the string is too long
	 */
	public void setLsid(String lsid)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setLsid(
			StringUtil.setNullableString(lsid, "lsid", MAX_LSID_LENGTH));
	}

	/**
	 * The maximum length of the accessionNumber that can be stored in the
	 * database.
	 * 
	 * @see #setAccessionNumber(String)
	 */
	public static final int MAX_ACCESSION_NUMBER_LENGTH = PolyPeptideData.MAX_ACCESSION_NUMBER_LENGTH;


	/**
	 * Get the accessionNumber of the polypeptide.
	 * 
	 * @return The accessionNumber for the polypeptide
	 */
	public String getAccessionNumber()
	{
		return getData().getAccessionNumber();
	}


	/**
	 * Set the accession number of the polypeptide. The value may be null but
	 * must not be longer than the value specified by the
	 * {@link #MAX_ACCESSION_NUMBER_LENGTH} constant.
	 * 
	 * @param accessionNumber The new value for the accession number
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the string is too long
	 */
	public void setAccessionNumber(String accessionNumber)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setAccessionNumber(
			StringUtil.setNullableString(accessionNumber, "accessionNumber",
				MAX_ACCESSION_NUMBER_LENGTH));
	}


	/**
	 * Get the {@link SearchResult} this PolyPeptide is associated with.
	 * 
	 * @return The <code>SearchResult</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public SearchResult getSearchResult()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(SearchResult.class,
//			getData().getSearchResult());
	}


	/**
	 * Set the {@link SearchResult} this PolyPeptide is associated with.
	 * 
	 * @param searchResult The new <code>SearchResult</code> item
	 * @throws BaseException If there is another error
	 */
	public void setSearchResult(SearchResult searchResult)
			throws BaseException
	{
		getData().setSearchResult(searchResult.getData());
	}


	/**
	 * @return Returns the modifications.
	 */
	public Set<ObservedModification> getObservedModifications()
			throws ItemNotFoundException, BaseException
	{
		Set<ObservedModification> modifications = new HashSet<ObservedModification>();
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			Iterator<ObservedModificationData> it = getData()
//				.getObservedModifications().iterator();
//			while (it.hasNext())
//			{
//				modifications.add(getDbControl().getItem(
//					ObservedModification.class, it.next()));
//			}
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
// 		return modifications;
	}


	/**
	 * Set the {@link ObservedModification} modifications set.
	 * 
	 * @param modifications The new <code>Set<ObservedModification></code>
	 *        item.
	 * @throws InvalidDataException If dataProcessingStep is null
	 * @throws BaseException If there is another error
	 */
	public void setObservedModifications(Set<ObservedModification> modifications)
	{
		Set<ObservedModificationData> dpData = new HashSet<ObservedModificationData>();
		Iterator<ObservedModification> it = modifications.iterator();
		while (it.hasNext())
		{
			dpData.add(it.next().getData());
		}
		getData().setObservedModifications(dpData);
	}


	/**
	 * Add a Modification
	 * 
	 * @param modification The Search Database to add to the Set
	 */
	public void addObservedModification(ObservedModification modification)
	{
		getData().getObservedModifications().add(
			modification.getData());
	}
}
