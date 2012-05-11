/*
	$Id: SearchModificationData.java 3207 2009-04-09 06:48:11Z gregory $

 */
package org.proteios.core.data;

/**
 * This represents a SearchModification of a Modification
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.SearchModification
 * @hibernate.subclass discriminator-value="2"
 */
public class SearchModificationData
		extends ModificationData
{
	public SearchModificationData()
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

	public static final int MAX_AASPECIFICITY_LENGTH = 255;
	private String aminoAcidSpecificity;


	/**
	 * @return Returns the aminoAcidSpecificity.
	 * @hibernate.property column="`aaSpecificity`" type="string" length="255"
	 *                     not-null="false"
	 * 
	 */
	public String getAminoAcidSpecificity()
	{
		return aminoAcidSpecificity;
	}


	/**
	 * @param aminoAcidSpecificity The aminoAcidSpecificity to set.
	 */
	public void setAminoAcidSpecificity(String aminoAcidSpecificity)
	{
		this.aminoAcidSpecificity = aminoAcidSpecificity;
	}

	private int terminalSpecificity;


	/**
	 * @return Returns the terminalSpecificity.
	 * @hibernate.property column="`terminalSpecificity`"
	 */
	public int getTerminalSpecificity()
	{
		return terminalSpecificity;
	}


	/**
	 * @param terminalSpecificity The terminalSpecificity to set.
	 */
	public void setTerminalSpecificity(int terminalSpecificity)
	{
		this.terminalSpecificity = terminalSpecificity;
	}

	private boolean fixed = true;


	/**
	 * @return Returns the fixed modification flag
	 * @hibernate.property column="`fixed`"
	 */
	public boolean isFixed()
	{
		return fixed;
	}


	public void setFixed(boolean fixed)
	{
		this.fixed = fixed;
	}
}