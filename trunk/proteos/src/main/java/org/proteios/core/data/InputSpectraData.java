package org.proteios.core.data;

import java.util.Set;

/**
 * This represents a file with spectra or peaklists used for a spectrum search.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.data.FileData
 * @see org.proteios.core.data.SpectrumSearchData
 * @see <a
 *      href="../../../../../../../development/overview/data/inputspectra.html">SpectrumSearch
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`InputSpectra`" lazy="true"
 */
public class InputSpectraData
extends CommonData
{
	public InputSpectraData()
	{}

	private FileData file;


	/**
	 * @return Returns the file.
	 * @hibernate.many-to-one column="`file_id`" not-null="false" outer-join="false"
	 */
	public FileData getFile()
	{
		return file;
	}


	/**
	 * @param file The file to set.
	 */
	public void setFile(FileData file)
	{
		this.file = file;
	}

	private Set<PeakListData> peaklists;


	/**
	 * @return Returns the peaklists.
	 * @hibernate.set table="`InputPeakLists`" cascade="none" lazy="true"
	 * @hibernate.collection-many-to-many class="org.proteios.core.data.PeakListData"
	 *                   column="`peaklist_id`" cascade="none" not-null="false"
	 * @hibernate.collection-key column="`inputSpectra_id`"
	 */
	public Set<PeakListData> getPeaklists()
	{
		return peaklists;
	}


	/**
	 * @param peaklists The peaklists to set.
	 */
	public void setPeaklists(Set<PeakListData> peaklists)
	{
		this.peaklists = peaklists;
	}
	
	private Set<PeakListSetData> peakListSets;


	/**
	 * @return Returns the peakListSets.
	 * @hibernate.set table="`InputPeakListSets`" cascade="none" lazy="true"
	 * @hibernate.collection-many-to-many class="org.proteios.core.data.PeakListSetData"
	 *                   column="`peaklistset_id`"  cascade="none" not-null="false"
	 * @hibernate.collection-key column="`inputSpectra_id`"
	 */
	public Set<PeakListSetData> getPeakListSets()
	{
		return peakListSets;
	}


	/**
	 * @param peakListSets The peakListSets to set.
	 */
	public void setPeakListSets(Set<PeakListSetData> peakListSets)
	{
		this.peakListSets = peakListSets;
	}
	
	private Set<SpectrumSearchData> spectrumSearches;


	/**
	 * @hibernate.set table="`SpectrumSearches`" cascade="none" lazy="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SpectrumSearchData"
	 *                                   cascade="none" not-null="false"
	 * @hibernate.collection-key column="`inputSpectra_id`"
	 @return Returns the spectrumSearches.
	 */
	public Set<SpectrumSearchData> getSpectrumSearches()
	{
		return spectrumSearches;
	}


	/**
	 @param spectrumSearches The spectrumSearches to set.
	 */
	public void setSpectrumSearches(Set<SpectrumSearchData> spectrumSearches)
	{
		this.spectrumSearches = spectrumSearches;
	}
	
}
