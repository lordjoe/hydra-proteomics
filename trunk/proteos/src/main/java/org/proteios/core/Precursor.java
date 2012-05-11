/*
 $Id: Precursor.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander, Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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

import org.proteios.core.data.InstrumentConfigurationData;
import org.proteios.core.data.PrecursorData;
import java.util.Set;

/**
 * This class represent precursor items. A precursor item has information about
 * the precursor in a 2-D mass spectrometry experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-06-02 12:33:12Z $
 */
public class Precursor
		extends BasicItem<PrecursorData>
{
	Precursor(PrecursorData precursorData)
	{
		super(precursorData);
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_PRECURSOR
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_PRECURSOR;


	/**
	 * Get a <code>Precursor</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for database
	 *        access.
	 * @param id The id of the item to load
	 * @return The <code>Precursor</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws BaseException If there is another error
	 */
//	public static Precursor getById(DbControl dc, int id)
//			throws ItemNotFoundException, BaseException
//	{
//		Precursor pc = dc.loadItem(Precursor.class, id);
//		if (pc == null)
//			throw new ItemNotFoundException("Precursor[id=" + id + "]");
//		return pc;
//	}


	/**
	 * Get a query that returns precursor items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<Precursor> getQuery()
	{
		return new ItemQuery<Precursor>(Precursor.class);
	}


	// -------------------------------------------
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
	 * From the BasicItem class
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this precursor
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the {@link InstrumentConfiguration} activation.
	 * 
	 * @return An <code>InstrumentConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public InstrumentConfiguration getActivation()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(InstrumentConfiguration.class,
//			getData().getActivation());
	}


	/**
	 * Set the {@link InstrumentConfiguration} activation.
	 * 
	 * @param activation The new <code>InstrumentConfiguration</code> item
	 * @throws InvalidDataException If activation is null
	 * @throws BaseException If there is another error
	 */
	public void setActivation(InstrumentConfiguration activation)
			throws InvalidDataException
	{
		if (activation == null)
			throw new InvalidUseOfNullException("activation");
		getData().setActivation(
			(InstrumentConfigurationData) activation.getData());
	}


	/**
	 * The chargeState value indicates the charge of the ion.
	 */
	/**
	 * Get the chargeState
	 * 
	 * @return the chargeState value
	 */
	public int getChargeState()
	{
		return getData().getChargeState();
	}


	/**
	 * Set the chargeState.
	 * 
	 * @param chargeState The chargeState value
	 */
	public void setChargeState(int chargeState)
	{
		getData().setChargeState(chargeState);
	}


	/**
	 * The {@link InstrumentConfiguration} item ionSelection contains
	 * information on the configuration of the intrument used. The variable is
	 * set to an instance of a class extending {@link InstrumentConfiguration},
	 * e.g. {@link HardwareConfiguration} or {@link SoftwareConfiguration}.
	 */
	/**
	 * Get the {@link InstrumentConfiguration} ionSelection.
	 * 
	 * @return An <code>InstrumentConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public InstrumentConfiguration getIonSelection()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(InstrumentConfiguration.class,
//			getData().getIonSelection());
	}


	/**
	 * Set the {@link InstrumentConfiguration} ionSelection.
	 * 
	 * @param ionSelection The new <code>InstrumentConfiguration</code> item
	 * @throws InvalidDataException If ionSelection is null
	 * @throws BaseException If there is another error
	 */
	public void setIonSelection(InstrumentConfiguration ionSelection)
			throws InvalidDataException
	{
		if (ionSelection == null)
			throw new InvalidUseOfNullException("ionSelection");
		getData().setIonSelection(
			(InstrumentConfigurationData) ionSelection.getData());
	}


	/**
	 * Get the mass to charge ratio.
	 * 
	 * @return A <code>Float</code> holding the massToChargeRatio
	 */
	public Float getMassToChargeRatio()
	{
		return getData().getMassToChargeRatio();
	}


	/**
	 * Set the mass to charge ratio
	 * 
	 * @param massToChargeRatio The massToChargeRatio value
	 * @throws InvalidDataException If massToChargeRatio is below zero
	 */
	public void setMassToChargeRatio(Float massToChargeRatio)
			throws InvalidDataException, BaseException
	{
		if (massToChargeRatio != null && massToChargeRatio < 0.0f)
		{
			throw new NumberOutOfRangeException("massToChargeRatio",
				massToChargeRatio, 0.0f, false);
		}
		getData().setMassToChargeRatio(massToChargeRatio);
	}


	/**
	 * Get the precursor intensity.
	 * 
	 * @return A <code>Float</code> holding the intensity
	 */
	public Float getIntensity()
	{
		return getData().getIntensity();
	}


	/**
	 * Set the mass to charge ratio
	 * 
	 * @param intensity The intensity value
	 * @throws InvalidDataException If intensity is below zero
	 */
	public void setIntensity(Float intensity)
			throws InvalidDataException, BaseException
	{
		if (intensity != null && intensity < 0.0f)
		{
			throw new NumberOutOfRangeException("intensity", intensity, 0.0f,
				false);
		}
		getData().setIntensity(intensity);
	}


	/**
	 * Get the msLevel
	 * 
	 * @return the msLevel value
	 */
	public int getMsLevel()
	{
		return getData().getMsLevel();
	}


	/**
	 * Set the msLevel.
	 * 
	 * @param msLevel The msLevel value
	 */
	public void setMsLevel(int msLevel)
	{
		getData().setMsLevel(msLevel);
	}


	/**
	 * Get the spectrumRef
	 * 
	 * @return the spectrumRef value
	 */
	public int getSpectrumRef()
	{
		return getData().getSpectrumRef();
	}


	/**
	 * Set the spectrumRef.
	 * 
	 * @param spectrumRef The spectrumRef value
	 */
	public void setSpectrumRef(int spectrumRef)
	{
		getData().setSpectrumRef(spectrumRef);
	}


	/**
	 * The creator PeakList item. The variable is set to an instance of class
	 * {@link PeakList}.
	 */
	/**
	 * Get the {@link PeakList} this precursor is created from.
	 * 
	 * @return The <code>PeakList</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public PeakList getPeakList()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(PeakList.class, getData().getPeakList());
	}


	/**
	 * Set the {@link PeakList} this precursor is created from.
	 * 
	 * @param peakList The new <code>PeakList</code> item
	 * @throws BaseException If there is another error
	 */
	public void setPeakList(PeakList peakList)
	{
		getData().setPeakList(peakList.getData());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.core.Annotatable#getAnnotationSet()
	 */
	public AnnotationSet getAnnotationSet()
			throws PermissionDeniedException, BaseException
	{
		/*
		 * Auto-generated method stub. Return null, as Precursor is not
		 * annotated.
		 */
		return null;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.core.Annotatable#isAnnotated()
	 */
	public boolean isAnnotated()
	{
		/*
		 * Auto-generated method stub. Return false, as Precursor is not
		 * annotated.
		 */
		return false;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.core.Annotatable#removeAnnotations()
	 */
	public void removeAnnotations()
			throws PermissionDeniedException, BaseException
	{
	/*
	 * Auto-generated method stub. Empty method, as Precursor is not annotated.
	 */
	}
}
