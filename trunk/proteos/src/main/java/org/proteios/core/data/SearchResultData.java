package org.proteios.core.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a search result. The index has to be unique within the
 * SearchResultSet in SpectrumSearchData, and is used for sorting.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.SearchResult
 * @see <a
 *      href="../../../../../../../development/overview/data/searchresult.html">SearchResult
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.subclass discriminator-value="1"
 * @hibernate.class table="`SearchResults`" lazy="true"
 */
public class SearchResultData
		extends AnnotatedData
		implements Comparable<SearchResultData>
{
	public SearchResultData()
	{}


	public int compareTo(SearchResultData o)
	{
		Integer index2 = o.getIndex();
		return Integer.valueOf(index).compareTo(index2);
	}

	/**
	 * The index is used as an internal id for the set
	 */
	private int index = 0;


	/**
	 * @hibernate.property column="`index`"
	 * @return Returns the index.
	 */
	public int getIndex()
	{
		return index;
	}


	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * The spectrum number in the infile
	 */
	private int inputSpectrumId = 0;


	/**
	 * @hibernate.property column="`inputSpectrumId`" not-null="false"
	 * @return Returns the inputSpectrumId.
	 */
	public int getInputSpectrumId()
	{
		return inputSpectrumId;
	}


	/**
	 * @param inputSpectrumId The inputSpectrumId to set.
	 */
	public void setInputSpectrumId(int inputSpectrumId)
	{
		this.inputSpectrumId = inputSpectrumId;
	}
	
	private Float expectationValue;


	/**
	 * @hibernate.property column="`e_value`" type="float" not-null="false"
	 * @return Returns the eValue.
	 */
	public Float getExpectationValue()
	{
		return expectationValue;
	}


	/**
	 * @param value The eValue to set.
	 */
	public void setExpectationValue(Float value)
	{
		expectationValue = value;
	}

	private Float score;


	/**
	 * @hibernate.property column="`score`" type="float" not-null="false"
	 * @return Returns the score.
	 */
	public Float getScore()
	{
		return score;
	}


	/**
	 * @param score The score to set.
	 */
	public void setScore(Float score)
	{
		this.score = score;
	}

	private String scoreType;
	public static final int MAX_SCORETYPE_LENGTH = 255;


	/**
	 * @hibernate.property column="`score_type`" type="string" length="255"
	 *                     not-null="false"
	 * @return Returns the scoreType.
	 */
	public String getScoreType()
	{
		return scoreType;
	}


	/**
	 * @param scoreType The scoreType to set.
	 */
	public void setScoreType(String scoreType)
	{
		this.scoreType = scoreType;
	}

	private PeakListData peakList;


	/**
	 * @return Returns the peakList.
	 * @hibernate.many-to-one column="`peaklist_id`"
	 */
	public PeakListData getPeakList()
	{
		return peakList;
	}


	/**
	 * @param peakList The peakList to set.
	 */
	public void setPeakList(PeakListData peakList)
	{
		this.peakList = peakList;
	}

	private SpectrumSearchData spectrumSearch;


	/**
	 * @return Returns the spectrumSearch.
	 * @hibernate.many-to-one column="`spectrumSearchId`"
	 */
	public SpectrumSearchData getSpectrumSearch()
	{
		return spectrumSearch;
	}


	/**
	 * @param spectrumSearch The spectrumSearch to set.
	 */
	public void setSpectrumSearch(SpectrumSearchData spectrumSearch)
	{
		this.spectrumSearch = spectrumSearch;
	}

	private List<SearchResultData> referencedResults;


	/**
	 * @hibernate.list table="`ReferencedResults`" lazy="true" 
	 * @hibernate.collection-key column="`id`"
	 * @hibernate.collection-index column="`index`"
	 * @hibernate.collection-many-to-many class="org.proteios.core.data.SearchResultData"
	 *                                    column="`resref_id`" not-null="false"
	 * @return Returns the referencedResults. Empty ArrayList if no referenced results
	 */
	public List<SearchResultData> getReferencedResults()
	{
		if (referencedResults==null)
		{
			referencedResults=new ArrayList<SearchResultData>();
		}
		return referencedResults;
	}


	/**
	 * @param referencedResults The referencedResults to set.
	 */
	public void setReferencedResults(List<SearchResultData> referencedResults)
	{
		this.referencedResults = referencedResults;
	}

	private Set<PolyPeptideData> polyPeptides=new HashSet<PolyPeptideData>();


	/**
	 * @hibernate.set table="`PolyPeptides`" cascade="delete" lazy="true"
	 *                 inverse="true"
	 * @hibernate.collection-key column="`searchResultId`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.PolyPeptideData"
	 *                                   column="`resref_id`" not-null="false"
	 * @return Returns the polyPeptides.
	 */
	public Set<PolyPeptideData> getPolyPeptides()
	{
		return polyPeptides;
	}


	/**
	 * @param polyPeptides The polyPeptides to set.
	 */
	public void setPolyPeptides(Set<PolyPeptideData> polyPeptides)
	{
		this.polyPeptides = polyPeptides;
	}
	
	private String spectrumStringId = null;
	public static final int MAX_SPECTRUMSTRINGID_LENGTH = 255;


	/**
	 * @return Returns the spectrumStringId.
	 * @hibernate.property column="`spectrumStringId`" length="255"
	 *                     type="string" not-null="false"
	 */
	public String getSpectrumStringId()
	{
		return spectrumStringId;
	}


	/**
	 * @param spectrumStringId The spectrumStringId to set.
	 */
	public void setSpectrumStringId(String spectrumStringId)
	{
		this.spectrumStringId = spectrumStringId;
	}
}
