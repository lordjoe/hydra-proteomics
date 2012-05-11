package org.proteios.core.data;

/**
 * This represents a digestion parameter.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.DigestParameter
 * @see <a
 *      href="../../../../../../../development/overview/data/digestparameter.html">SpectrumSearch
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`DigestParameters`" lazy="true"
 */
public class DigestParameterData
		extends CommonData
{
	public DigestParameterData()
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

	private int maxNumMissedCleavages;


	/**
	 * @return Returns the maxNumMissedCleavages.
	 * @hibernate.property column="`missedCleavages`" type="int"
	 *                     not-null="false"
	 */
	public int getMaxNumMissedCleavages()
	{
		return maxNumMissedCleavages;
	}


	/**
	 * @param maxNumMissedCleavages The maxNumMissedCleavages to set.
	 */
	public void setMaxNumMissedCleavages(int maxNumMissedCleavages)
	{
		this.maxNumMissedCleavages = maxNumMissedCleavages;
	}

	public static final int MAX_NAME_LENGTH = 255;
	private String name;


	/**
	 * @return Returns the name.
	 * @hibernate.property column="`name`" type="string" length="255"
	 *                     not-null="false"
	 */
	@Override
	public String getName()
	{
		return name;
	}


	/**
	 * @param name The name to set.
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	public static final int MAX_SPECIFICITY_LENGTH = 255;
	private String specificity;


	/**
	 * @return Returns the specificity.
	 * @hibernate.property column="`specificity`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getSpecificity()
	{
		return specificity;
	}


	/**
	 * @param specificity The specificity to set.
	 */
	public void setSpecificity(String specificity)
	{
		this.specificity = specificity;
	}
}
