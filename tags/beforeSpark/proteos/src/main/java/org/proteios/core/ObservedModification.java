/*
 $Id: ObservedModification.java 3207 2009-04-09 06:48:11Z gregory $

 */
package org.proteios.core;

import org.proteios.core.data.ObservedModificationData;
import java.util.Set;

/**
 * This class represent observedModifications.
 * 
 * @author Fredrik
 * @version 2.0
 */
public class ObservedModification
		extends Modification<ObservedModificationData>
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_OBSERVEDMODIFICATION
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_OBSERVEDMODIFICATION;


	/**
	 * Get an <code>ObservedModification</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for permission
	 *        checking and database access.
	 * @param id The id of the item to load
	 * @return The <code>ObservedModification</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws PermissionDeniedException If the logged in observed doesn't have
	 *         read permission to the item
	 * @throws BaseException If there is another error
	 */
//	public static ObservedModification getById(DbControl dc, int id)
//			throws ItemNotFoundException, PermissionDeniedException,
//			BaseException
//	{
//		ObservedModification bs = dc.loadItem(ObservedModification.class, id);
//		if (bs == null)
//			throw new ItemNotFoundException(
//				"ObservedModification[id=" + id + "]");
//		return bs;
//	}


	/**
	 * Get a query that returns observedModification items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<ObservedModification> getQuery()
	{
		return new ItemQuery<ObservedModification>(ObservedModification.class);
	}


	ObservedModification(ObservedModificationData observedModificationData)
	{
		super(observedModificationData);
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
	 * <li>no {@link Sample} has been created from this observedModification
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * @return Returns the modifiedAminoAcid.
	 */
	public char getModifiedAminoAcid()
	{
		return getData().getModifiedAminoAcid();
	}


	/**
	 * @param modifiedAminoAcid The modifiedAminoAcid to set.
	 */
	public void setModifiedAminoAcid(char modifiedAminoAcid)
	{
		getData().setModifiedAminoAcid(modifiedAminoAcid);
	}
}
