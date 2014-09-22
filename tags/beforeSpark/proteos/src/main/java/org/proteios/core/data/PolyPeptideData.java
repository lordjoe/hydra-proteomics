/*
 $Id: PolyPeptideData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the root class for biomaterials.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.PolyPeptide
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.class table="`PolyPeptides`" lazy="false" abstract="true" discriminator-value="-1"
 * @hibernate.discriminator column="`discriminator`" type="int"
 */
public abstract class PolyPeptideData
		extends AnnotatedData
{
	public PolyPeptideData()
	{}

	/**
	 * Protein accession number
	 */
	public static final int MAX_ACCESSION_NUMBER_LENGTH = 255;
	private String accessionNumber;


	/**
	 * @hibernate.property column="`accession_number`" type="string"
	 *                     length="255" not-null="false"
	 */
	public String getAccessionNumber()
	{
		return accessionNumber;
	}


	public void setAccessionNumber(String accessionNumber)
	{
		this.accessionNumber = accessionNumber;
	}

	/**
	 * Protein / Peptide sequence
	 */
	public static final int MAX_SEQUENCE_LENGTH = 65000;
	private String sequence;


	/**
	 * Get the sequence
	 * 
	 * @hibernate.property column="`sequence`" type="string" length="65000"
	 *                     not-null="false"
	 */
	public String getSequence()
	{
		return sequence;
	}


	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}

	/**
	 * Protein LSID
	 */
	public static final int MAX_LSID_LENGTH = 255;
	private String lsid;


	/**
	 * Get the external id for the biomaterial
	 * 
	 * @hibernate.property column="`lsid`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getLsid()
	{
		return lsid;
	}


	public void setLsid(String lsid)
	{
		this.lsid = lsid;
	}
	
	private SearchResultData searchResult;


	/**
	 * @return Returns the searchResult.
	 * @hibernate.many-to-one column="`searchResultId`"
	 */
	public SearchResultData getSearchResult()
	{
		return searchResult;
	}


	/**
	 * @param searchResult The searchResult to set.
	 */
	public void setSearchResult(SearchResultData searchResult)
	{
		this.searchResult = searchResult;
	}
	
	
	private Set<ObservedModificationData> modifications = new HashSet<ObservedModificationData>();
	
	/**
	 * @hibernate.set table="`Modifications`" cascade="delete" lazy="true"
	 *       
	 * @hibernate.collection-key column="`polypeptideId`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.ModificationData"
	 *                                   not-null="false"
	 * @return Returns the Modifications.
	 */
	public Set<ObservedModificationData> getObservedModifications()
	{
		return modifications;
	}


	/**
	 * @param modifications The modification set to set.
	 */
	public void setObservedModifications(Set<ObservedModificationData> modifications)
	{
		this.modifications = modifications;
	}

}