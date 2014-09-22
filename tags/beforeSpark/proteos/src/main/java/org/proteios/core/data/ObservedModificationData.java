/*
	$Id: ObservedModificationData.java 3207 2009-04-09 06:48:11Z gregory $

*/
package org.proteios.core.data;

/**
	This represents a ObservedModification of a Modification

	@author Fredrik
	@version 2.0
	@see org.proteios.core.ObservedModification
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="1"
*/
public class ObservedModificationData
	extends ModificationData
{

	public ObservedModificationData()
	{}

	char modifiedAminoAcid;

	/**
	 @return Returns the modifiedAminoAcid.
	 * @hibernate.property column="`modifiedAminoAcid`"
	 */
	public char getModifiedAminoAcid()
	{
		return modifiedAminoAcid;
	}

	/**
	 @param modifiedAminoAcid The modifiedAminoAcid to set.
	 */
	public void setModifiedAminoAcid(char modifiedAminoAcid)
	{
		this.modifiedAminoAcid = modifiedAminoAcid;
	}
	
	
}