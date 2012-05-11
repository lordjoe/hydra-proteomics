/*
 $Id: SearchResult.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander
 Copyright (C) 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

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

import org.proteios.core.data.HitData;
import org.proteios.core.data.PolyPeptideData;
import org.proteios.core.data.SearchResultData;
import org.proteios.io.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represent spectrumsearch.
 * 
 * @author Fredrik
 * @version 2.0
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class SearchResult
		extends AnnotatedItem<SearchResultData>
		implements Comparable<SearchResult>
{
	SearchResult(SearchResultData searchResultData)
	{
		super(searchResultData);
	}


	public int compareTo(SearchResult o)
	{
		Integer index2 = o.getIndex();
		return Integer.valueOf(getIndex()).compareTo(index2);
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_SEARCHRESULT
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_SEARCHRESULT;


	/**
	 * Get a query that returns spectrumsearchset items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<SearchResult> getQuery()
	{
		return new ItemQuery<SearchResult>(SearchResult.class);
	}


	// -------------------------------------------
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


	/*
	 * From the BasicItem class
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this peaklistset
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the {@link PeakList} this SearchResult is associated with.
	 * 
	 * @return The <code>PeakList</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public PeakList getPeakList()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return getDbControl().getItem(PeakList.class, getData().getPeakList());
	}


	/**
	 * Set the {@link PeakList} this SearchResult is associated with.
	 * 
	 * @param peakList The new <code>PeakList</code> item
	 * @throws BaseException If there is another error
	 */
	public void setPeakList(PeakList peakList)
			throws BaseException
	{
		getData().setPeakList(peakList.getData());
	}


	/**
	 * Get the {@link SpectrumSearch} this SearchResult is associated with.
	 * 
	 * @return The <code>SpectrumSearch</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
//	public SpectrumSearch getSpectrumSearch()
//			throws BaseException
//	{
//		return getDbControl().getItem(SpectrumSearch.class,
//			getData().getSpectrumSearch());
//	}


	/**
	 * Set the {@link SpectrumSearch} this SearchResult is associated with.
	 * 
	 * @param spectrumSearch The new <code>SpectrumSearch</code> item
	 * @throws BaseException If there is another error
	 */
//	public void setSpectrumSearch(SpectrumSearch spectrumSearch)
//			throws BaseException
//	{
//		getData().setSpectrumSearch(spectrumSearch.getData());
//	}


	/**
	 * @return Returns the index used for sorting.
	 */
	public int getIndex()
	{
		return getData().getIndex();
	}


	/**
	 * @param index The index for sorting.
	 */
	public void setIndex(int index)
	{
		getData().setIndex(index);
	}


	/**
	 * @return Returns the inputSpectrumId.
	 */
	public int getInputSpectrumId()
	{
		return getData().getInputSpectrumId();
	}


	/**
	 * @param inputSpectrumId The inputSpectrumId to set.
	 */
	public void setInputSpectrumId(int inputSpectrumId)
	{
		getData().setInputSpectrumId(inputSpectrumId);
	}


	/**
	 * @return Returns the score.
	 */
	public Float getScore()
	{
		return getData().getScore();
	}


	/**
	 * @param score The primary score.
	 */
	public void setScore(Float score)
	{
		getData().setScore(score);
	}


	/**
	 * @return Returns the expectation value.
	 */
	public Float getExpectationValue()
	{
		return getData().getExpectationValue();
	}


	/**
	 * @param eValue The primary expectation value.
	 */
	public void setExpectationValue(Float eValue)
	{
		getData().setExpectationValue(eValue);
	}

	/**
	 * The maximum length of the scoreType string that can be stored in the
	 * database.
	 * 
	 * @see #setScoreType(String)
	 */
	public static final int MAX_SCORETYPE_LENGTH = SearchResultData.MAX_SCORETYPE_LENGTH;


	/**
	 * Get the input filename of this <code>SearchResult</code>.
	 * 
	 * @return the filename
	 */
	public String getScoreType()
	{
		return getData().getScoreType();
	}


	/**
	 * Set the scoreType for this <code>SearchResult</code> item. The value
	 * may be null but must not be longer than the value specified by the
	 * {@link #MAX_SCORETYPE_LENGTH} constant.
	 * 
	 * @param scoreType The new scoreType for this item
	 * @throws InvalidDataException If the scoreType is longer than
	 *         {@link #MAX_SCORETYPE_LENGTH}
	 */
	public void setScoreType(String scoreType)
			throws InvalidDataException
	{
		getData().setScoreType(
			StringUtil.setNullableString(scoreType, "scoreType",
				MAX_SCORETYPE_LENGTH));
	}


	/**
	 * @return Returns the spectrumStringId.
	 */
	public String getSpectrumStringId()
	{
		return getData().getSpectrumStringId();
	}

	public static final int MAX_SPECTRUMSTRINGID_LENGTH = SearchResultData.MAX_SPECTRUMSTRINGID_LENGTH;


	/**
	 * Set the spectrumStringId for this <code>SearchResult</code> item. The
	 * value may be null but must not be longer than the value specified by the
	 * 
	 * @param spectrumStringId The spectrumStringId to set.
	 * @throws InvalidDataException If the spectrumStringId is longer than
	 *         {@link #MAX_SPECTRUMSTRINGID_LENGTH}
	 */
	public void setSpectrumStringId(String spectrumStringId)
			throws InvalidDataException
	{
		getData().setSpectrumStringId(
			StringUtil.setNullableString(spectrumStringId, "spectrumStringId",
				MAX_SPECTRUMSTRINGID_LENGTH));
	}


	/**
	 * Get the {@link SearchResult} referenced results.
	 * 
	 * @return A <code>SearchResult</code> list object
	 * @throws BaseException If there is another error
	 */
	public List<SearchResult> getReferencedResults()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			List<SearchResult> searchResultList = new ArrayList<SearchResult>();
//			Iterator<SearchResultData> it = getData().getReferencedResults()
//				.iterator();
//			while (it.hasNext())
//			{
//				searchResultList.add(getDbControl().getItem(SearchResult.class,
//					it.next()));
//			}
//			return searchResultList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link SearchResult} referenced results.
	 * 
	 * @param referencedResults The new <code>SearchResult</code> list item
	 * @throws BaseException If there is another error
	 */
	public void setReferencedResults(List<SearchResult> referencedResults)
			throws InvalidDataException
	{
		List<SearchResultData> searchResultDataList = new ArrayList<SearchResultData>();
		Iterator<SearchResult> it = referencedResults.iterator();
		while (it.hasNext())
		{
			searchResultDataList.add(it.next().getData());
		}
		getData().setReferencedResults(searchResultDataList);
	}


	/**
	 * Add a result to the list of referenced results.
	 */
	public void addReferencedResult(SearchResult referencedResult)
	{
		getData().getReferencedResults().add(referencedResult.getData());
	}


	@SuppressWarnings("unchecked")
	/**
	 * Get the {@link PolyPeptide} polypeptides.
	 * 
	 * @return A <code>PolyPeptide</code> set object
	 * @throws BaseException If there is another error
	 */
	public Set<PolyPeptide> getPolyPeptides()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			Set<PolyPeptide> ppList = new HashSet<PolyPeptide>();
//			Iterator<PolyPeptideData> it = getData().getPolyPeptides()
//				.iterator();
//			while (it.hasNext())
//			{
//				ppList
//					.add(getDbControl().getItem(PolyPeptide.class, it.next()));
//			}
//			return ppList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link PolyPeptide} polypeptides
	 * 
	 * @param polyPeptides The new <code>PolyPeptide</code> set item
	 * @throws BaseException If there is another error
	 */
	@SuppressWarnings("unchecked")
	public void setPolyPeptides(Set<PolyPeptide> polyPeptides)
			throws InvalidDataException
	{
		Set<PolyPeptideData> ppDataList = new HashSet<PolyPeptideData>();
		Iterator<PolyPeptide> it = polyPeptides.iterator();
		while (it.hasNext())
		{
			ppDataList.add((PolyPeptideData) it.next().getData());
		}
		getData().setPolyPeptides(ppDataList);
	}


	/**
	 * Add a PolyPeptide to the collection. Backward reference is set
	 * 
	 * @param pp the PolyPeptide to set
	 */
	@SuppressWarnings("unchecked")
	public void addPolyPeptide(PolyPeptide pp)
	{
		pp.setSearchResult(this);
		getData().getPolyPeptides().add((PolyPeptideData) pp.getData());
	}


	/**
	 * Get the {@link Peptide} polypeptides. Only returns peptides from the
	 * polypeptide collection
	 * 
	 * @return A <code>Peptide</code> set object
	 * @throws BaseException If there is another error
	 */
	@SuppressWarnings("unchecked")
	public Set<Peptide> getPeptides()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			Set<Peptide> ppList = new HashSet<Peptide>();
//			Iterator<PolyPeptideData> it = getData().getPolyPeptides()
//				.iterator();
//			while (it.hasNext())
//			{
//				PolyPeptide pop = getDbControl().getItem(PolyPeptide.class,
//					it.next());
//				if (pop instanceof Peptide)
//				{
//					ppList.add((Peptide) pop);
//				}
//			}
//			return ppList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Get the {@link Peptide} polypeptides. Only returns peptides from the
	 * polypeptide collection
	 * 
	 * @return A <code>Peptide</code> set object
	 * @throws BaseException If there is another error
	 */
	@SuppressWarnings("unchecked")
	public Set<TandemFactory.Protein> getProteins()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			Set<TandemFactory.Protein> ppList = new HashSet<TandemFactory.Protein>();
//			Iterator<PolyPeptideData> it = getData().getPolyPeptides()
//				.iterator();
//			while (it.hasNext())
//			{
//				PolyPeptide pop = getDbControl().getItem(PolyPeptide.class,
//					it.next());
//				if (pop instanceof TandemFactory.Protein)
//				{
//					ppList.add((TandemFactory.Protein) pop);
//				}
//			}
//			return ppList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}
}
