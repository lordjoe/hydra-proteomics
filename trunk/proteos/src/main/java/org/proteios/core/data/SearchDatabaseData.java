package org.proteios.core.data;

import java.util.Date;

/**
 * This class hold information about a database used for a SpectrumSearch.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.SearchDatabase
 * @see <a
 *      href="../../../../../../../development/overview/data/searchdatabase.html">SearchDatabase
 *      overview</a>
 * @proteios.modified $Date: 2010-06-09 05:17:16 -0700 (Wed, 09 Jun 2010) $
 * @hibernate.class table="`SearchDatabases`" lazy="true"
 */
public class SearchDatabaseData
extends CommonData
{
	public SearchDatabaseData()
	{}
	
	private SpectrumSearchData spectrumSearch;


	/**
	 * Get the {@link SpectrumSearchData} this SearchDatabase is created from.
	 * 
	 * @hibernate.many-to-one column="`spectrumSearchId`"
	 * @return The <code>SpectrumSearchData</code> item or null if not known
	 */
	public SpectrumSearchData getSpectrumSearch()
	{
		return spectrumSearch;
	}


	/**
	 * Set the {@link SpectrumSearchData} this peakList is created from.
	 * 
	 * @param spectrumSearch The creator spectrumSearch
	 */
	public void setSpectrumSearch(SpectrumSearchData spectrumSearch)
	{
		this.spectrumSearch = spectrumSearch;
	}

	public static final int MAX_NAME_LENGTH = 255;
	private String name;
	/**
	 @return Returns the name.
	 @hibernate.property column="`name`" type="string"
	 *                     length="255" not-null="false"
	 */
	@Override
	public String getName()
	{
		return name;
	}
	/**
	 @param name The name to set.
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	private int numberOfEntries;
	/**
	 @return Returns the numberOfEntries.
	 @hibernate.property column="`number_of_entries`" type="int"
	 *                     not-null="false"
	 */
	public int getNumberOfEntries()
	{
		return numberOfEntries;
	}
	/**
	 @param numberOfEntries The numberOfEntries to set.
	 */
	public void setNumberOfEntries(int numberOfEntries)
	{
		this.numberOfEntries = numberOfEntries;
	}
	
	private int numberOfEntriesAfterTaxonomy;
	/**
	 @return Returns the numberOfEntriesAfterTaxonomy.
	 @hibernate.property column="`number_of_entries_after_taxonomy`" type="int"
	 *                     not-null="false"
	 */
	public int getNumberOfEntriesAfterTaxonomy()
	{
		return numberOfEntriesAfterTaxonomy;
	}
	/**
	 @param numberOfEntriesAfterTaxonomy The numberOfEntriesAfterTaxonomy to set.
	 */
	public void setNumberOfEntriesAfterTaxonomy(int numberOfEntriesAfterTaxonomy)
	{
		this.numberOfEntriesAfterTaxonomy = numberOfEntriesAfterTaxonomy;
	}
	
	private long numberOfResidues;
	/**
	 @return Returns the numberOfResidues.
	 @hibernate.property column="`number_of_residues`" type="long"
	 *                     not-null="false"
	 */
	public long getNumberOfResidues()
	{
		return numberOfResidues;
	}
	/**
	 @param numberOfResidues The numberOfResidues to set.
	 */
	public void setNumberOfResidues(long numberOfResidues)
	{
		this.numberOfResidues = numberOfResidues;
	}
	
	private Date releaseDate;
	/**
	 @return Returns the releaseDate.
	 */
	public Date getReleaseDate()
	{
		return releaseDate;
	}
	/**
	 @param releaseDate The releaseDate to set.
	 */
	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}
	
	public static final int MAX_TAXONOMY_LENGTH = 255;
	private String taxonomy;
	/**
	 @return Returns the taxonomy.
	 @hibernate.property column="`taxonomy`" type="string"
	 *           length="255" not-null="false"

	 */
	public String getTaxonomy()
	{
		return taxonomy;
	}
	/**
	 @param taxonomy The taxonomy to set.
	 */
	public void setTaxonomy(String taxonomy)
	{
		this.taxonomy = taxonomy;
	}
	
	public static final int MAX_VERSIONIDENTIFIER_LENGTH = 255;
	private String versionIdentifier;
	/**
	 @return Returns the database version identifier.
	 @hibernate.property column="`version_identifier`" type="string"
	 *              length="255" not-null="false"
	 */
	public String getVersionIdentifier()
	{
		return versionIdentifier;
	}
	/**
	 @param versionIdentifier The database version identifier to set.
	 */
	public void setVersionIdentifier(String versionIdentifier)
	{
		this.versionIdentifier = versionIdentifier;
	}
	
}
