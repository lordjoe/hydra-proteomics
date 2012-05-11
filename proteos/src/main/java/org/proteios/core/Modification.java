/*
 $Id: Modification.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

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
package org.proteios.core;

import org.proteios.core.data.ModificationData;

/**
 * This is the base class for for the two types of Modifications:
 * {@link ObservedModification} and
 * {@link SearchModification}.
 * 
 * @author Fredrik
 * @version 2.0
 */
public abstract class Modification<D extends ModificationData>
		extends CommonItem<D>
{
	Modification(D modificationData)
	{
		super(modificationData);
	}

	

	/**
	 * @return Returns the substitutionModififcation flag
	 */
	public boolean isSubstitutionModification()
	{
		return getData().isSubstitutionModification();
	}

	/**
	 @param substitutionModification Whether the modification is an amino acid substitution.
	 */
	public void setSubstitutionModification(boolean substitutionModification)
	{
		getData().setSubstitutionModification(substitutionModification);
	}
	
	/**
	 @return Returns the position index.
	 */
	public int getPosition()
	{
		return getData().getPosition();
	}

	/**
	 @param position The position index to set.
	 */
	public void setPosition(int position)
	{
		getData().setPosition(position);
	}
	
	
	/**
	 @return Returns the average mass.
	 */
	public float getAverageMass()
	{
		return getData().getAverageMass();
	}

	/**
	 @param averageMass The averageMass index to set.
	 */
	public void setAverageMass(float averageMass)
	{
		getData().setAverageMass(averageMass);
	}
	
	/**
	 @return Returns the average delta mass.
	 */
	public float getAverageDeltaMass()
	{
		return getData().getAverageDeltaMass();
	}

	/**
	 @param averageDeltaMass The averageDeltaMass index to set.
	 */
	public void setAverageDeltaMass(float averageDeltaMass)
	{
		getData().setAverageDeltaMass(averageDeltaMass);
	}
	
	/**
	 @return Returns the monoisotopic mass.
	 */
	public double getMonoisotopicMass()
	{
		return getData().getMonoisotopicMass();
	}

	/**
	 @param monoisotopicMass The monoisotopicMass index to set.
	 */
	public void setMonoisotopicMass(double monoisotopicMass)
	{
		getData().setMonoisotopicMass(monoisotopicMass);
	}
	
	/**
	 @return Returns the monoisotopic delta mass.
	 */
	public double getMonoisotopicDeltaMass()
	{
		return getData().getMonoisotopicDeltaMass();
	}

	/**
	 @param monoisotopicDeltaMass The monoisotopicDeltaMass index to set.
	 */
	public void setMonoisotopicDeltaMass(double monoisotopicDeltaMass)
	{
		getData().setMonoisotopicDeltaMass(monoisotopicDeltaMass);
	}
	
	/**
	 * The maximum length of the formula that can be stored in the database.
	 * 
	 * @see #setDiffFormula(String)
	 */
	public static final int MAX_DIFF_FORMULA_LENGTH = ModificationData.MAX_DIFF_FORMULA_LENGTH;


	/**
	 * Get the differential chemical formula of the modification.
	 * 
	 * @return The formula for the modification
	 */
	public String getDiffFormula()
	{
		return getData().getDiffFormula();
	}


	/**
	 * Set the differential chemical formula of the Modification. The value may be null but must not
	 * be longer than the value specified by the {@link #MAX_DIFF_FORMULA_LENGTH}
	 * constant.
	 * 
	 * @param diffFormula String The new value for the formula
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the string is too long
	 */
	public void setDiffFormula(String diffFormula)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setDiffFormula(
			StringUtil
				.setNullableString(diffFormula, "diffFormula", MAX_DIFF_FORMULA_LENGTH));
	}
	
	/**
	 * The maximum length of the CV ID that can be saved in the database.
*/
	public static final int MAX_CVID_LENGTH = ModificationData.MAX_CVID_LENGTH;


	/**
	 * Get the controlled vocabulary ID of the modification.
	 * 
	 * @return The CV ID for the modification
	 */
	public String getCvId()
	{
		return getData().getCvId();
	}


	/**
	 * Set the CV ID of the Modification. The value may be null but must not
	 * be longer than the value specified by the {@link #MAX_CVID_LENGTH}
	 * constant.
	 * 
	 * @param cvId The new CV ID
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the string is too long
	 */
	public void setCvId(String cvId)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setCvId(
			StringUtil
				.setNullableString(cvId, "cvId", MAX_CVID_LENGTH));
	}
	
}
