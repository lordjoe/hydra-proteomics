/*
 $Id: PeakList.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006, 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

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

import org.proteios.core.data.AcquisitionData;
import org.proteios.core.data.InstrumentConfigurationData;
import org.proteios.core.data.PeakData;
import org.proteios.core.data.PeakListData;
import org.proteios.core.data.PrecursorData;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represent peaklist items. A peaklist has information about the
 * peaks in an experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-06-01 12:33:12Z $
 */
public class PeakList
		extends AnnotatedItem<PeakListData>
		implements Comparable<PeakList>
{
	PeakList(PeakListData peakListData)
	{
		super(peakListData);
	}


	public int compareTo(PeakList p)
	{
		return Integer.valueOf(getSpectrumId()).compareTo(
			Integer.valueOf(p.getSpectrumId()));
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_PEAKLIST
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_PEAKLIST;


	/**
	 * Get a <code>PeakList</code> item when you know the id.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for database
	 *        access.
	 * @param id The id of the item to load
	 * @return The <code>PeakList</code> item
	 * @throws ItemNotFoundException If an item with the specified id is not
	 *         found
	 * @throws BaseException If there is another error
	 */
//	public static PeakList getById(DbControl dc, int id)
//			throws ItemNotFoundException, BaseException
//	{
//		PeakList pl = dc.loadItem(PeakList.class, id);
//		if (pl == null)
//			throw new ItemNotFoundException("PeakList[id=" + id + "]");
//		return pl;
//	}


	/**
	 * Get a query that returns peaklist items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<PeakList> getQuery()
	{
		return new ItemQuery<PeakList>(PeakList.class);
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


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Check that:
	 * <ul>
	 * <li>no item has been created from this peaklist
	 * </ul>
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}


	/**
	 * Get the {@link PeakListSet} this peakList is created from.
	 * 
	 * @return The <code>PeakListSet</code> item or null if not known
	 * @throws BaseException If there is another error
	 */
	public PeakListSet getPeakListSet()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(PeakListSet.class,
//			getData().getPeakListSet());
	}


	/**
	 * Set the {@link PeakListSet} this peakList is created from.
	 * 
	 * @param peakListSet The new <code>PeakListSet</code> item
	 * @throws BaseException If there is another error
	 */
	public void setPeakListSet(PeakListSet peakListSet)
			throws BaseException
	{
		getData().setPeakListSet(peakListSet.getData());
	}

	/**
	 * The maximum length of the combinationMethod string that can be stored in
	 * the database.
	 * 
	 * @see #setCombinationMethod(String)
	 */
	public static final int MAX_COMBINATIONMETHOD_LENGTH = PeakListData.MAX_COMBINATIONMETHOD_LENGTH;


	/**
	 * Get the combination method of this <code>PeakList</code>.
	 * 
	 * @return the combination method of this peak list
	 */
	public String getCombinationMethod()
	{
		return getData().getCombinationMethod();
	}


	/**
	 * Set the combination method for this <code>PeakList</code> item. The
	 * value may be null but must not be longer than the value specified by the
	 * {@link #MAX_COMBINATIONMETHOD_LENGTH} constant.
	 * 
	 * @param combinationMethod The new combination method for this item
	 * @throws InvalidDataException If the combinationMethod is longer than
	 *         {@link #MAX_COMBINATIONMETHOD_LENGTH}
	 */
	public void setCombinationMethod(String combinationMethod)
			throws InvalidDataException
	{
		getData().setCombinationMethod(
			StringUtil.setNullableString(combinationMethod,
				"combinationMethod", MAX_COMBINATIONMETHOD_LENGTH));
	}

	/**
	 * The maximum length of the description string that can be stored in the
	 * database.
	 * 
	 * @see #setDescription(String)
	 */
	public static final int MAX_DESCRIPTION_LENGTH = PeakListData.MAX_DESCRIPTION_LENGTH;


	/**
	 * Get the description of this <code>PeakList</code>.
	 * 
	 * @return the description of this peak list
	 */
	@Override
	public String getDescription()
	{
		return getData().getDescription();
	}


	/**
	 * Set the description for this <code>PeakList</code> item. The value may
	 * be null but must not be longer than the value specified by the
	 * {@link #MAX_DESCRIPTION_LENGTH} constant.
	 * 
	 * @param description The new description for this item
	 * @throws InvalidDataException If the description is longer than
	 *         {@link #MAX_DESCRIPTION_LENGTH}
	 */
	@Override
	public void setDescription(String description)
			throws InvalidDataException
	{
		getData().setDescription(
			StringUtil.setNullableString(description, "description",
				MAX_DESCRIPTION_LENGTH));
	}


	/**
	 * The doublePrecison flag indicates whether the peaks are at double
	 * precison.
	 */
	/**
	 * Get the mzDoublePrecision flag
	 * 
	 * @return the mzDoublePrecision flag
	 */
	public boolean isMzDoublePrecision()
	{
		return getData().isMzDoublePrecision();
	}


	/**
	 * Set the mzDoublePrecision flag
	 * 
	 * @param doublePrecision The mzDoublePrecision flag
	 */
	public void setMzDoublePrecision(boolean doublePrecision)
	{
		getData().setMzDoublePrecision(doublePrecision);
	}


	/**
	 * The doublePrecison flag indicates whether the peaks are at double
	 * precison.
	 */
	/**
	 * Get the intensityDoublePrecision flag
	 * 
	 * @return the intensityDoublePrecision flag
	 */
	public boolean isIntensityDoublePrecision()
	{
		return getData().isIntensityDoublePrecision();
	}


	/**
	 * Set the intensityDoublePrecision flag
	 * 
	 * @param doublePrecision The intensityDoublePrecision flag
	 */
	public void setIntensityDoublePrecision(boolean doublePrecision)
	{
		getData().setIntensityDoublePrecision(doublePrecision);
	}


	/**
	 * The monoisotopic flag indicates whether the peak list data is
	 * monoisotopic.
	 */
	/**
	 * Get the monoisotopic flag
	 * 
	 * @return the monoisotopic flag
	 */
	public boolean isMonoisotopic()
	{
		return getData().isMonoisotopic();
	}


	/**
	 * Set the monoisotopic flag
	 * 
	 * @param monoisotopic The monoisotopic flag
	 */
	public void setMonoisotopic(boolean monoisotopic)
	{
		getData().setMonoisotopic(monoisotopic);
	}


	/**
	 * The msLevel value indicates the level of the mass spectra data.
	 */
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
	 * @throws InvalidDataException If the msLevel is lower than zero
	 */
	public void setMsLevel(int msLevel)
			throws InvalidDataException
	{
		getData().setMsLevel(IntegerUtil.checkMin(msLevel, "msLevel", 0));
	}


	/**
	 * Get the total peak intensity. Try to calculate and set it if it was not
	 * given TODO: Commit to database and perform checks
	 * 
	 * @return A <code>Float</code> holding the totalIntensity
	 */
	public Float getTotalIntensity()
	{
		Float totalIntensity = getData().getTotalIntensity();
		if (totalIntensity == null)
		{
			totalIntensity = calculateTotalIntensity();
			try
			{
				this.checkPermission(Permission.WRITE);
				this.setTotalIntensity(totalIntensity);
			}
			catch (Exception e)
			{}
		}
		return totalIntensity;
	}


	/**
	 * Count the total intensity of all peaks in this peaklist
	 * 
	 * @return A <code>Float</code> holding the totalIntensity
	 */
	public Float calculateTotalIntensity()
	{
		float totalIntensity = 0;
		List<Peak> peaks = this.getPeakArrayList();
		Iterator<Peak> peakit = peaks.iterator();
		while (peakit.hasNext())
		{
			totalIntensity += peakit.next().getIntensity();
		}
		return totalIntensity;
	}


	/**
	 * Set the total intensity
	 * 
	 * @param totalIntensity The totalIntensity value
	 * @throws InvalidDataException If totalIntensity is below zero
	 */
	public void setTotalIntensity(Float totalIntensity)
			throws InvalidDataException, BaseException
	{
		if (totalIntensity != null && totalIntensity < 0.0f)
		{
			throw new NumberOutOfRangeException("totalIntensity",
				totalIntensity, 0.0f, false);
		}
		getData().setTotalIntensity(totalIntensity);
	}


	/**
	 * Get the base peak intensity.
	 * 
	 * @return A <code>Float</code> holding the base peak intensity
	 */
	public Float getBasePeakIntensity()
	{
		Float basePeakIntensity = getData().getBasePeakIntensity();
		if (basePeakIntensity == null)
		{
			basePeakIntensity = findBasePeakIntensity();
		}
		return basePeakIntensity;
	}


	/**
	 * Set the base peak intensity
	 * 
	 * @param basePeakIntensity The basePeakIntensity value
	 * @throws InvalidDataException If basePeakIntensity is below zero
	 */
	public void setBasePeakIntensity(Float basePeakIntensity)
			throws InvalidDataException, BaseException
	{
		if (basePeakIntensity != null && basePeakIntensity < 0.0f)
		{
			throw new NumberOutOfRangeException("basePeakIntensity",
				basePeakIntensity, 0.0f, false);
		}
		getData().setBasePeakIntensity(basePeakIntensity);
	}


	/**
	 * Find the most intense peak in this peaklist and return the intensity
	 * 
	 * @return A <code>Float</code> holding the intensity
	 */
	public Float findBasePeakIntensity()
	{
		double intensity = 0;
		List<Peak> peaks = this.getPeakArrayList();
		Iterator<Peak> peakit = peaks.iterator();
		while (peakit.hasNext())
		{
			double newint = peakit.next().getIntensity();
			if (newint > intensity)
			{
				intensity = newint;
			}
		}
		return Float.valueOf((float) intensity);
	}


	/**
	 * Get the start of the mzRange.
	 * 
	 * @return A <code>Float</code> holding the mzRangeStart
	 */
	public Float getMzRangeStart()
	{
		return getData().getMzRangeStart();
	}


	/**
	 * Set the start of the mzRange
	 * 
	 * @param mzRangeStart The msRangeStart value
	 * @throws InvalidDataException If mzRangeStart is below zero
	 */
	public void setMzRangeStart(Float mzRangeStart)
			throws InvalidDataException, BaseException
	{
		if (mzRangeStart != null && mzRangeStart < 0.0f)
		{
			throw new NumberOutOfRangeException("mzRangeStart", mzRangeStart,
				0.0f, false);
		}
		getData().setMzRangeStart(mzRangeStart);
	}


	/**
	 * Get the stop of the mzRange.
	 * 
	 * @return A <code>Float</code> holding the mzRangeStop
	 */
	public Float getMzRangeStop()
	{
		return getData().getMzRangeStop();
	}


	/**
	 * Set the stop of the mzRange
	 * 
	 * @param mzRangeStop The msRangeStop value
	 * @throws InvalidDataException If mzRangeStop is below zero
	 */
	public void setMzRangeStop(Float mzRangeStop)
			throws InvalidDataException, BaseException
	{
		if (mzRangeStop != null && mzRangeStop < 0.0f)
		{
			throw new NumberOutOfRangeException("mzRangeStop", mzRangeStop,
				0.0f, false);
		}
		getData().setMzRangeStop(mzRangeStop);
	}


	/**
	 * Get the spectrumId
	 * 
	 * @return the spectrumId value
	 */
	public int getSpectrumId()
	{
		return getData().getSpectrumId();
	}


	/**
	 * Set the spectrumId.
	 * 
	 * @param spectrumId The spectrumId value
	 */
	public void setSpectrumId(int spectrumId)
	{
		getData().setSpectrumId(spectrumId);
	}


	/**
	 * The {@link InstrumentConfiguration} item spectrumInstrument contains
	 * information on the configuration of the intrument used. The variable is
	 * set to an instance of a class extending {@link InstrumentConfiguration},
	 * e.g. {@link HardwareConfiguration} or {@link SoftwareConfiguration}.
	 */
	/**
	 * Get the {@link InstrumentConfiguration} spectrumInstrument.
	 * 
	 * @return An <code>InstrumentConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public InstrumentConfiguration getSpectrumInstrument()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(InstrumentConfiguration.class,
//			getData().getSpectrumInstrument());
	}


	/**
	 * Set the {@link InstrumentConfiguration} spectrumInstrument.
	 * 
	 * @param spectrumInstrument The new <code>InstrumentConfiguration</code>
	 *        item
	 * @throws InvalidDataException If spectrumInstrument is null
	 * @throws BaseException If there is another error
	 */
	public void setSpectrumInstrument(InstrumentConfiguration spectrumInstrument)
			throws InvalidDataException
	{
		if (spectrumInstrument == null)
			throw new InvalidUseOfNullException("spectrumInstrument");
		getData().setSpectrumInstrument(
			(InstrumentConfigurationData) spectrumInstrument.getData());
	}


	/**
	 * Get the time in minutes.
	 * 
	 * @return A <code>Float</code> holding the timeInMinutes
	 */
	public Float getTimeInMinutes()
	{
		return getData().getTimeInMinutes();
	}


	/**
	 * Set the time in minutes
	 * 
	 * @param timeInMinutes The timeInMinutes value
	 * @throws InvalidDataException If timeInMinutes is below zero
	 */
	public void setTimeInMinutes(Float timeInMinutes)
			throws InvalidDataException, BaseException
	{
		if (timeInMinutes != null && timeInMinutes < 0.0f)
		{
			throw new NumberOutOfRangeException("timeInMinutes", timeInMinutes,
				0.0f, false);
		}
		getData().setTimeInMinutes(timeInMinutes);
	}


	/**
	 * AcquisitionList information. The variables are set to instances of class
	 * {@link Acquisition}.
	 */
	/**
	 * Get the {@link Acquisition} acquisitionList.
	 * 
	 * @return An <code>Acquisition</code> list object
	 * @throws BaseException If there is another error
	 */
	public List<Acquisition> getAcquisitionList()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			int arraySize = getData().getAcquisitionList().size();
//			/*
//			 * Create new list of Acquistion items, and add the data from the
//			 * list of AcquisitionData items.
//			 */
//			List<Acquisition> acquisitionList = new ArrayList<Acquisition>(
//				arraySize);
//			for (int i = 0; i < arraySize; i++)
//			{
//				acquisitionList.add(i, getDbControl().getItem(
//					Acquisition.class, getData().getAcquisitionList().get(i)));
//			}
//			return acquisitionList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link Acquisition} acquisitionList.
	 * 
	 * @param acquisitionList The new <code>Acquisition</code> list item
	 * @throws BaseException If there is another error
	 */
	public void setAcquisitionList(List<Acquisition> acquisitionList)
			throws InvalidDataException
	{
		int arraySize = acquisitionList.size();
		/*
		 * Create new list of acquisitionData items, and add the data from the
		 * list of Acquisition items.
		 */
		List<AcquisitionData> acquisitionDataList = new ArrayList<AcquisitionData>(
			arraySize);
		for (int i = 0; i < arraySize; i++)
		{
			acquisitionDataList.add(i, acquisitionList.get(i)
				.getData());
		}
		getData().setAcquisitionList(
			acquisitionDataList);
	}


	/**
	 * Get a query that returns all Acquisition items created from this
	 * peaklist.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public ItemQuery<Acquisition> getAcquisitionsQuery()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDoq
//		ItemQuery<Acquisition> query = Acquisition.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("peakList"), Hql
//			.entity(this)));
//		return query;
	}


	/**
	 * PrecursorList information. The variables are set to instances of class
	 * {@link Precursor}.
	 */
	/**
	 * Get the {@link Precursor} precursorList.
	 * 
	 * @return A <code>Precursor</code> list object
	 * @throws BaseException If there is another error
	 */
	public List<Precursor> getPrecursorList()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			int arraySize = getData().getPrecursorList().size();
//			/*
//			 * Create new list of Precursor items, and add the data from the
//			 * list of PrecursorData items.
//			 */
//			List<Precursor> precursorList = new ArrayList<Precursor>(arraySize);
//			for (int i = 0; i < arraySize; i++)
//			{
//				precursorList.add(i, getDbControl().getItem(Precursor.class,
//					getData().getPrecursorList().get(i)));
//			}
//			return precursorList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link Precursor} precursorList.
	 * 
	 * @param precursorList The new <code>Precursor</code> list item
	 * @throws BaseException If there is another error
	 */
	public void setPrecursorList(List<Precursor> precursorList)
			throws InvalidDataException
	{
		int arraySize = precursorList.size();
		/*
		 * Create new list of precursorData items, and add the data from the
		 * list of Precursor items.
		 */
		List<PrecursorData> precursorDataList = new ArrayList<PrecursorData>(
			arraySize);
		for (int i = 0; i < arraySize; i++)
		{
			precursorDataList.add(i, precursorList.get(i)
				.getData());
		}
		getData().setPrecursorList(precursorDataList);
	}


	/**
	 * Get a query that returns all Precursor items created from this peaklist.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public ItemQuery<Precursor> getPrecursorsQuery()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<Precursor> query = Precursor.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("peakList"), Hql
//			.entity(this)));
//		return query;
	}


	/**
	 * PeakArrayList information. The variables are set to instances of class
	 * {@link Peak}.
	 */
	/**
	 * Get the {@link Peak} peakArrayList.
	 * 
	 * @return A <code>Peak</code> list object
	 * @throws BaseException If there is another error
	 */
	public List<Peak> getPeakArrayList()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			/*
//			 * Create new list of Peak items, and add the data from the list of
//			 * PeakData items.
//			 */
//			List<Peak> peakArrayList = new ArrayList<Peak>();
//			Iterator<PeakData> peakit = getData().getPeaks().iterator();
//			while (peakit.hasNext())
//			{
//				peakArrayList.add(getDbControl().getItem(Peak.class,
//					peakit.next()));
//			}
//			Collections.sort(peakArrayList);
//			return peakArrayList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link Peak} peakArrayList.
	 * 
	 * @param peakArrayList The new <code>Peak</code> list item
	 * @throws BaseException If there is another error
	 */
	public void setPeakArrayList(List<Peak> peakArrayList)
			throws InvalidDataException
	{
		/*
		 * Create new list of peakData items, and add the data from the list of
		 * Peak items.
		 */
		Set<PeakData> peaks = new HashSet<PeakData>();
		Iterator<Peak> peakit = peakArrayList.iterator();
		while (peakit.hasNext())
		{
			peaks.add(peakit.next().getData());
		}
		getData().setPeaks(peaks);
	}


	public void addPeak(Peak peak)
	{
		peak.setPeakList(this);
		getData().getPeaks().add(peak.getData());
	}


	/**
	 * Get a query that returns all Peak items created from this peaklist.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public ItemQuery<Peak> getPeaksQuery()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<Peak> query = Peak.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("peakList"), Hql
//			.entity(this)));
//		return query;
	}
}
