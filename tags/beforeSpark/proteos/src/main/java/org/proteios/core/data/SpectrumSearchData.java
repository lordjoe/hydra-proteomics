/*
 $Id: SpectrumSearchData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander
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
package org.proteios.core.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This represents the top element of a protein identification search.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.SpectrumSearch
 * @see <a
 *      href="../../../../../../../development/overview/data/spectrumsearch.html">SpectrumSearch
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.subclass discriminator-value="1"
 * @hibernate.class table="`SpectrumSearches`" lazy="true"
 */
public class SpectrumSearchData
		extends AnnotatedData
{
	public SpectrumSearchData()
	{}

	private Date analysisTime;


	/**
	 * @hibernate.property column="`analysis_time`" type="timestamp"
	 *                     not-null="false"
	 * @return Returns the analysisTime.
	 */
	public Date getAnalysisTime()
	{
		return analysisTime;
	}


	/**
	 * @param analysisTime The analysisTime to set.
	 */
	public void setAnalysisTime(Date analysisTime)
	{
		this.analysisTime = analysisTime;
	}

	/**
	 * Results file. {@link FileData},
	 */
	private FileData resultFile;


	/**
	 * Get the result file that this spectrumsearch was generated from
	 * 
	 * @hibernate.many-to-one column="`resultFile`" not-null="false"
	 * @return the FileData
	 */
	public FileData getResultFile()
	{
		return this.resultFile;
	}


	/**
	 * Set the result file
	 * 
	 * @param resultFile The results File this spectrumSearch was generated from
	 */
	public void setResultFile(FileData resultFile)
	{
		this.resultFile = resultFile;
	}

	private String inputSpectrumFileName;
	public static final int MAX_INPUTSPECTRUMFILENAME_LENGTH = 255;


	/**
	 * @hibernate.property column="`input_spectrum_filename`" type="string"
	 *                     length="255" not-null="false"
	 * @return Returns the inputSpectrumFile.
	 */
	public String getInputSpectrumFileName()
	{
		return inputSpectrumFileName;
	}


	/**
	 * @param inputSpectrumFileName The inputSpectrumFile to set.
	 */
	public void setInputSpectrumFileName(String inputSpectrumFileName)
	{
		this.inputSpectrumFileName = inputSpectrumFileName;
	}

	private Set<DigestParameterData> digestParameters;


	/**
	 * Get the digestParameters.
	 * 
	 * @hibernate.set table="`DigestParameters`" cascade="all" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.DigestParameterData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`spectrumSearchId`"
	 * @return The {@link DigestParameterData} digestParameterList
	 */
	public Set<DigestParameterData> getDigestParameters()
	{
		if (digestParameters == null)
		{
			digestParameters = new HashSet<DigestParameterData>();
		}
		return this.digestParameters;
	}


	/**
	 * Set the searchDatabaes
	 * 
	 * @param digestParameters The {@link DigestParameterData} digestParameters
	 */
	public void setDigestParameters(Set<DigestParameterData> digestParameters)
	{
		this.digestParameters = digestParameters;
	}

	/**
	 * The {@link SoftwareData} item software contains information on the
	 * software used. The variable is set to an instance of class
	 * {@link SoftwareData}.
	 */
	private SoftwareData searchEngine;


	/**
	 * Get the software
	 * 
	 * @hibernate.many-to-one column="`software_id`" not-null="false"
	 *                        outer-join="false"
	 * @return The {@link SoftwareData} software
	 */
	public SoftwareData getSearchEngine()
	{
		return this.searchEngine;
	}


	/**
	 * @param searchEngine The {@link SoftwareData} software used in this search
	 */
	public void setSearchEngine(SoftwareData searchEngine)
	{
		this.searchEngine = searchEngine;
	}

	private Set<SearchDatabaseData> searchDatabases;


	/**
	 * Get the searchDatabases.
	 * 
	 * @hibernate.set table="`SearchDatabases`" cascade="all" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SearchDatabaseData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`spectrumSearchId`"
	 * @return The {@link SearchDatabaseData} searchDatabaseList
	 */
	public Set<SearchDatabaseData> getSearchDatabases()
	{
		if (searchDatabases == null)
		{
			searchDatabases = new HashSet<SearchDatabaseData>();
		}
		return this.searchDatabases;
	}


	/**
	 * Set the searchDatabaes
	 * 
	 * @param searchDatabases The {@link SearchDatabaseData} searchDatabases
	 */
	public void setSearchDatabases(Set<SearchDatabaseData> searchDatabases)
	{
		this.searchDatabases = searchDatabases;
	}

	/**
	 * This is a direct association used if the search was conducted with only
	 * one PeakListSet
	 */
	private PeakListSetData peakListSet;


	/**
	 * @hibernate.collection-many-to-one column="`peakListSetId`"
	 * @return Returns the peakListSet if only one was used. Otherwise go
	 *         through inputSpectra.
	 */
	public PeakListSetData getPeakListSet()
	{
		return peakListSet;
	}


	/**
	 * @param peakListSet The peakListSet to set.
	 */
	public void setPeakListSet(PeakListSetData peakListSet)
	{
		this.peakListSet = peakListSet;
	}

	/**
	 * Referenced to the collection of spectra used for the search. Use either
	 * this or the direct PeakListSet association
	 */
	private InputSpectraData inputSpectra;


	/**
	 * @hibernate.collection-many-to-one column="inputSpectra_id"
	 *                                   not-null="false"
	 * @return Returns the inputSpectra.
	 */
	public InputSpectraData getInputSpectra()
	{
		return inputSpectra;
	}


	/**
	 * @param inputSpectra The inputSpectra to set.
	 */
	public void setInputSpectra(InputSpectraData inputSpectra)
	{
		this.inputSpectra = inputSpectra;
	}

	private SortedSet<SearchResultData> searchResults;


	/**
	 * @hibernate.set table="`SearchResults`" sort="natural" cascade="all"
	 *                lazy="true" inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SearchResultData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`spectrumSearchId`"
	 * @return Returns the searchResults.
	 */
	public SortedSet<SearchResultData> getSearchResults()
	{
		if (searchResults == null)
		{
			searchResults = new TreeSet<SearchResultData>();
		}
		return searchResults;
	}


	/**
	 * @param searchResults The searchResults to set.
	 */
	public void setSearchResults(SortedSet<SearchResultData> searchResults)
	{
		this.searchResults = searchResults;
	}

	private Set<SearchModificationData> searchModifications = new HashSet<SearchModificationData>();


	/**
	 * Get the searchModifications.
	 * 
	 * @hibernate.set table="`Modifications`" cascade="all" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SearchModificationData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`spectrumSearchId`"
	 * @return The {@link SearchModificationData} searchModificationList
	 */
	public Set<SearchModificationData> getSearchModifications()
	{
		return searchModifications;
	}


	/**
	 * Set the searchDatabases
	 * 
	 * @param searchModifications The {@link SearchModificationData}
	 *        searchModifications
	 */
	public void setSearchModifications(
			Set<SearchModificationData> searchModifications)
	{
		this.searchModifications = searchModifications;
	}
}
