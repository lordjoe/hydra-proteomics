/*
	$Id: PeptideData.java 3207 2009-04-09 06:48:11Z gregory $

*/
package org.proteios.core.data;

/**
	This represents a peptide

	@author Fredrik
	@version 2.0
	@see org.proteios.core.Peptide
	@hibernate.subclass discriminator-value="2"
*/
public class PeptideData
	extends PolyPeptideData
{

	public PeptideData()
	{}
	
	public int startPosition;

	/**
	 @return Returns the startPosition.
	 @hibernate.property column="`start_position`"
	 */
	public int getStartPosition()
	{
		return startPosition;
	}

	/**
	 @param startPosition The startPosition to set.
	 */
	public void setStartPosition(int startPosition)
	{
		this.startPosition = startPosition;
	}
	
	public int endPosition;

	/**
	 @return Returns the endPosition.
	 @hibernate.property column="`end_position`"
	 */
	public int getEndPosition()
	{
		return endPosition;
	}

	/**
	 @param endPosition The endPosition to set.
	 */
	public void setEndPosition(int endPosition)
	{
		this.endPosition = endPosition;
	}
	
	private String preSequence;
	public static final int MAX_PRESEQUENCE_LENGTH = 255;


	/**
	 * @hibernate.property column="`presequence`" type="string" length="255"
	 *                     not-null="false"
	 * @return Returns the preSequence.
	 */
	public String getPreSequence()
	{
		return preSequence;
	}


	/**
	 * @param preSequence The preSequence to set.
	 */
	public void setPreSequence(String preSequence)
	{
		this.preSequence = preSequence;
	}

	private String postSequence;
	public static final int MAX_POSTSEQUENCE_LENGTH = 255;


	/**
	 * @hibernate.property column="`postsequence`" type="string" length="255"
	 *                     not-null="false"
	 * @return Returns the postSequence.
	 */
	public String getPostSequence()
	{
		return postSequence;
	}


	/**
	 * @param postSequence The postSequence to set.
	 */
	public void setPostSequence(String postSequence)
	{
		this.postSequence = postSequence;
	}

}