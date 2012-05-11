/*
 $Id: ModificationData.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
 * This class is the root class for modifications.
 * 
 * @author Fredrik
 * @version 2.0
 * @see org.proteios.core.Modification
 * @hibernate.class table="`Modifications`" lazy="false" abstract="true"
 *                  discriminator-value="-1"
 * @hibernate.discriminator column="`discriminator`" type="int"
 */
public abstract class ModificationData
		extends CommonData
{
	public ModificationData()
	{}

	private boolean substitutionModification = false;


	/**
	 * @return Returns the substitutionModififcation flag
	 * @hibernate.property column="`substitutionModification`"
	 */
	public boolean isSubstitutionModification()
	{
		return substitutionModification;
	}


	public void setSubstitutionModification(boolean substitutionModification)
	{
		this.substitutionModification = substitutionModification;
	}
	
	private int position;


	/**
	 * @return Returns the position.
	 * @hibernate.property column="`position`"
	 */
	public int getPosition()
	{
		return position;
	}


	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position)
	{
		this.position = position;
	}

	public float averageMass;


	/**
	 * @return Returns the average mass.
	 * @hibernate.property column="`averageMass`"
	 */
	public float getAverageMass()
	{
		return averageMass;
	}


	/**
	 * @param averageMass The averageMass to set.
	 */
	public void setAverageMass(float averageMass)
	{
		this.averageMass = averageMass;
	}

	public float averageDeltaMass;


	/**
	 * @return Returns the average delta mass.
	 * @hibernate.property column="`averageDeltaMass`"
	 */
	public float getAverageDeltaMass()
	{
		return averageDeltaMass;
	}


	/**
	 * @param averageDeltaMass The averageDeltaMass to set.
	 */
	public void setAverageDeltaMass(float averageDeltaMass)
	{
		this.averageDeltaMass = averageDeltaMass;
	}

	public double monoisotopicMass;


	/**
	 * @return Returns the monoisotopic mass.
	 * @hibernate.property column="`monoisotopicMass`"
	 */
	public double getMonoisotopicMass()
	{
		return monoisotopicMass;
	}


	/**
	 * @param monoisotopicMass The monoisotopicMass to set.
	 */
	public void setMonoisotopicMass(double monoisotopicMass)
	{
		this.monoisotopicMass = monoisotopicMass;
	}

	public double monoisotopicDeltaMass;


	/**
	 * @return Returns the monoisotopic delta mass.
	 * @hibernate.property column="`monoisotopicDeltaMass`"
	 */
	public double getMonoisotopicDeltaMass()
	{
		return monoisotopicDeltaMass;
	}


	/**
	 * @param monoisotopicDeltaMass The monoisotopicDeltaMass to set.
	 */
	public void setMonoisotopicDeltaMass(double monoisotopicDeltaMass)
	{
		this.monoisotopicDeltaMass = monoisotopicDeltaMass;
	}

	/**
	 * Modification chemical differential formula
	 */
	public static final int MAX_DIFF_FORMULA_LENGTH = 255;
	
	private String diffFormula;


	/**
	 * Get the differential formula
	 * 
	 * @hibernate.property column="`diffFormula`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getDiffFormula()
	{
		return diffFormula;
	}


	public void setDiffFormula(String diffFormula)
	{
		this.diffFormula = diffFormula;
	}
	
	/**
	 * Modification Controlled vocabulary ID
	 */
	public static final int MAX_CVID_LENGTH = 255;
	
	private String cvId;


	/**
	 * Get the CV ID
	 * 
	 * @hibernate.property column="`cvId`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getCvId()
	{
		return cvId;
	}


	public void setCvId(String cvId)
	{
		this.cvId = cvId;
	}
}