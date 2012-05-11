/*
 $Id: Peptide.java 3207 2009-04-09 06:48:11Z gregory $

 */
package org.proteios.core;

import org.proteios.core.data.PeptideData;
import java.util.Set;

/**
 * This class represent peptide.
 * 
 * @author Fredrik
 * @version 2.0
 */
public class Peptide
		extends PolyPeptide<PeptideData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_PEPTIDE
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_PEPTIDE;


	/**
	 * Get an <code>Peptide</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for permission
	 *        checking and database access.
	 * @param id The id of the item to load
	 * @return The <code>Peptide</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws PermissionDeniedException If the logged in user doesn't have read
	 *         permission to the item
	 * @throws BaseException If there is another error
	 */
//	public static Peptide getById(DbControl dc, int id)
//			throws ItemNotFoundException, PermissionDeniedException,
//			BaseException
//	{
//		Peptide bs = dc.loadItem(Peptide.class, id);
//		if (bs == null)
//			throw new ItemNotFoundException("Peptide[id=" + id + "]");
//		return bs;
//	}


	/**
	 * Get a query that returns protein items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
//	public static ItemQuery<Peptide> getQuery()
//	{
//		return new ItemQuery<Peptide>(Peptide.class);
//	}


	Peptide(PeptideData peptideData)
	{
		super(peptideData);
	}


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
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no {@link Sample} has been created from this protein
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}

	/**
	 * The maximum length of the preSequence string that can be stored in the
	 * database.
	 * 
	 * @see #setPreSequence(String)
	 */
	public static final int MAX_PRESEQUENCE_LENGTH = PeptideData.MAX_PRESEQUENCE_LENGTH;


	/**
	 * Get the sequence before this peptide.
	 * 
	 * @return the pre sequence
	 */
	public String getPreSequence()
	{
		return getData().getPreSequence();
	}


	/**
	 * Set the preSequence for this <code>SearchResult</code> item. The value
	 * may be null but must not be longer than the value specified by the
	 * {@link #MAX_PRESEQUENCE_LENGTH} constant.
	 * 
	 * @param preSequence The new preSequence for this item
	 * @throws InvalidDataException If the preSequence is longer than
	 *         {@link #MAX_PRESEQUENCE_LENGTH}
	 */
	public void setPreSequence(String preSequence)
			throws InvalidDataException
	{
		getData().setPreSequence(
			StringUtil.setNullableString(preSequence, "preSequence",
				MAX_PRESEQUENCE_LENGTH));
	}

	/**
	 * The maximum length of the postSequence string that can be stored in the
	 * database.
	 * 
	 * @see #setPostSequence(String)
	 */
	public static final int MAX_POSTSEQUENCE_LENGTH = PeptideData.MAX_POSTSEQUENCE_LENGTH;


	/**
	 * Get the sequence before this peptide.
	 * 
	 * @return the post sequence
	 */
	public String getPostSequence()
	{
		return getData().getPostSequence();
	}


	/**
	 * Set the postSequence for this <code>SearchResult</code> item. The value
	 * may be null but must not be longer than the value specified by the
	 * {@link #MAX_POSTSEQUENCE_LENGTH} constant.
	 * 
	 * @param postSequence The new postSequence for this item
	 * @throws InvalidDataException If the postSequence is longer than
	 *         {@link #MAX_POSTSEQUENCE_LENGTH}
	 */
	public void setPostSequence(String postSequence)
			throws InvalidDataException
	{
		getData().setPostSequence(
			StringUtil.setNullableString(postSequence, "postSequence",
				MAX_POSTSEQUENCE_LENGTH));
	}


	/**
	 * @return Returns the startPosition.
	 */
	public int getStartPosition()
	{
		return getData().getStartPosition();
	}


	/**
	 * @param startPosition The startPosition to set.
	 */
	public void setStartPosition(int startPosition)
	{
		getData().setStartPosition(startPosition);
	}


	/**
	 * @return Returns the endPosition.
	 */
	public int getEndPosition()
	{
		return getData().getEndPosition();
	}


	/**
	 * @param endPosition The endPosition to set.
	 */
	public void setEndPosition(int endPosition)
	{
		getData().setEndPosition(endPosition);
	}
}
