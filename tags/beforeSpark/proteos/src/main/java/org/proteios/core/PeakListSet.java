/*
 $Id: PeakListSet.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006 Fredrik Levander, Gregory Vincic, Olle Mansson
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
package org.proteios.core;

import org.proteios.core.data.DataProcessingStepData;
import org.proteios.core.data.HardwareConfigurationData;
import org.proteios.core.data.InstrumentConfigurationData;
import org.proteios.core.data.PeakListData;
import org.proteios.core.data.PeakListSetData;
import org.proteios.core.data.SpectrumSearchData;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represent peaklistset items. A peaklistset has information about
 * the peak lists in an experiment.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2006-05-31 12:33:12Z $
 */
public class PeakListSet
		extends AnnotatedItem<PeakListSetData>
{
	PeakListSet(PeakListSetData peakListSetData)
	{
		super(peakListSetData);
	}

	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#PROTEIOS_PEAKLISTSET
	 * @see #getType()
	 */
	public static final Item TYPE = Item.PROTEIOS_PEAKLISTSET;

//
//	/**
//	 * Get a <code>PeakListSet</code> item when you know the id.
//	 *
//	 * @param dc The <code>DbControl</code> which will be used for database
//	 *        access.
//	 * @param id The id of the item to load
//	 * @return The <code>PeakListSet</code> item
//	 * @throws ItemNotFoundException If an item with the specified id is not
//	 *         found
//	 * @throws BaseException If there is another error
//	 */
//	public static PeakListSet getById(DbControl dc, int id)
//			throws ItemNotFoundException, BaseException
//	{
//		PeakListSet ps = dc.loadItem(PeakListSet.class, id);
//		if (ps == null)
//			throw new ItemNotFoundException("PeakListSet[id=" + id + "]");
//		return ps;
//	}


	/**
	 * Get a query that returns peaklistset items.
	 * 
	 * @return An {@link ItemQuery} object.
	 */
	public static ItemQuery<PeakListSet> getQuery()
	{
		return new ItemQuery<PeakListSet>(PeakListSet.class);
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
	 * Get a query that returns all PeakList items created from this
	 * peaklistset.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public ItemQuery<PeakList> getPeakListsQuery()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		ItemQuery<PeakList> query = PeakList.getQuery();
//		query.restrictPermanent(Restrictions.eq(Hql.property("peakListSet"),
//			Hql.entity(this)));
//		return query;
	}


	/**
	 * Get the {@link Sample} /** Get the {@link Sample}
	 * 
	 * @return A <code>Sample</code> object
	 * @throws BaseException If there is another error
	 */
	public Sample getSample()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return getDbControl().getItem(Sample.class, getData().getSample());
	}


	/**
	 * Set the {@link Sample}
	 * 
	 * @param sample The new <code>Sample</code> item
	 * @throws InvalidDataException If the sample is null
	 * @throws BaseException If there is another error
	 */
	public void setSample(Sample sample)
			throws InvalidDataException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDoq
//		if (sample == null)
//			throw new InvalidUseOfNullException("sample");
//		getData().setSample(sample.getData());
	}


	/**
	 * The {@link InstrumentConfiguration} item otherInstrumentInfo contains
	 * required instrument configuration information not covered by other
	 * variables of the class. The variable is set to an instance of a class
	 * extending {@link InstrumentConfiguration}, e.g.
	 * {@link HardwareConfiguration} or {@link SoftwareConfiguration}.
	 */
	/**
	 * Get the {@link InstrumentConfiguration} otherInstrumentInfo.
	 * 
	 * @return An <code>InstrumentConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public InstrumentConfiguration getOtherInstrumentInfo()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(InstrumentConfiguration.class,
//			getData().getOtherInstrumentInfo());
	}


	/**
	 * Set the {@link InstrumentConfiguration} otherInstrumentInfo.
	 * 
	 * @param otherInstrumentInfo The new <code>InstrumentConfiguration</code>
	 *        item
	 * @throws InvalidDataException If the otherInstrumentInfo is null
	 * @throws BaseException If there is another error
	 */
	public void setOtherInstrumentInfo(
			InstrumentConfiguration otherInstrumentInfo)
			throws InvalidDataException
	{
		if (otherInstrumentInfo == null)
			throw new InvalidUseOfNullException("otherInstrumentInfo");
		getData().setOtherInstrumentInfo(
			(InstrumentConfigurationData) otherInstrumentInfo.getData());
	}


	/**
	 * Analyzers configuration information. The variables are set to instances
	 * of class {@link HardwareConfiguration}.
	 */
	/**
	 * Get the {@link HardwareConfiguration} analyzers list.
	 * 
	 * @return A <code>HardwareConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public List<HardwareConfiguration> getAnalyzers()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			int arraySize = getData().getAnalyzers().size();
//			/*
//			 * Create new list of HardwareConfiguration items, and add the data
//			 * from the list of HardwareConfigurationData items.
//			 */
//			List<HardwareConfiguration> analyzers = new ArrayList<HardwareConfiguration>(
//				arraySize);
//			for (int i = 0; i < arraySize; i++)
//			{
//				analyzers.add(i, getDbControl().getItem(
//					HardwareConfiguration.class,
//					getData().getAnalyzers().get(i)));
//			}
//			return analyzers;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link HardwareConfiguration} analyzers.
	 * 
	 * @param analyzers The new <code>HardwareConfiguration</code> list item
	 * @throws InvalidDataException If analyzers hardware not equal to
	 *         instrument hardware
	 * @throws BaseException If there is another error
	 */
	public void setAnalyzers(List<HardwareConfiguration> analyzers)
			throws InvalidDataException
	{
		int arraySize = analyzers.size();
		/*
		 * Check that the hardware values of all list items are equal to the
		 * instrument hardware.
		 */
		for (int i = 0; i < arraySize; i++)
		{
			if (analyzers.get(i).getHardware() != this.getInstrument())
				throw new InvalidDataException(
					"analyzers hardware #" + i + " not equal to instrument hardware");
		}
		/*
		 * Create new list of HardwareConfigurationData items, and add the data
		 * from the list of HardwareConfiguration items.
		 */
		List<HardwareConfigurationData> analyzersData = new ArrayList<HardwareConfigurationData>(
			arraySize);
		for (int i = 0; i < arraySize; i++)
		{
			analyzersData.add(i, analyzers.get(i).getData());
		}
		getData().setAnalyzers(analyzersData);
	}


	/**
	 * Detector configuration information. The variable is set to an instance of
	 * class {@link HardwareConfiguration}.
	 */
	/**
	 * Get the {@link HardwareConfiguration} detector.
	 * 
	 * @return A <code>HardwareConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public HardwareConfiguration getDetector()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(HardwareConfiguration.class,
//			getData().getDetector());
	}


	/**
	 * Set the {@link HardwareConfiguration} detector.
	 * 
	 * @param detector The new <code>HardwareConfiguration</code> item
	 * @throws InvalidDataException If detector hardware not equal to instrument
	 *         hardware
	 * @throws BaseException If there is another error
	 */
	public void setDetector(HardwareConfiguration detector)
			throws InvalidDataException
	{
		if (detector.getHardware() != this.getInstrument())
			throw new InvalidDataException(
				"detector hardware not equal to instrument hardware");
		getData().setDetector(detector.getData());
	}


	/**
	 * Instrument hardware information. The variable is set to an instance of
	 * class {@link Hardware}.
	 */
	/**
	 * Get the {@link Hardware} instrument.
	 * 
	 * @return A <code>Hardware</code> object
	 * @throws BaseException If there is another error
	 */
	public Hardware getInstrument()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl()
//			.getItem(Hardware.class, getData().getInstrument());
	}


	/**
	 * Set the {@link Hardware} instrument.
	 * 
	 * @param instrument The new <code>Hardware</code> item
	 * @throws InvalidDataException If instrument is null
	 * @throws BaseException If there is another error
	 */
	public void setInstrument(Hardware instrument)
			throws InvalidDataException
	{
		if (instrument == null)
			throw new InvalidUseOfNullException("instrument");
		getData().setInstrument(instrument.getData());
	}


	/**
	 * Ionisation source configuration information. The variable is set to an
	 * instance of class {@link HardwareConfiguration}.
	 */
	/**
	 * Get the {@link HardwareConfiguration} ionisationSource.
	 * 
	 * @return A <code>HardwareConfiguration</code> object
	 * @throws BaseException If there is another error
	 */
	public HardwareConfiguration getIonisationSource()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(HardwareConfiguration.class,
//			getData().getIonisationSource());
	}


	/**
	 * Set the {@link HardwareConfiguration} ionisationSource.
	 * 
	 * @param ionisationSource The new <code>HardwareConfiguration</code> item
	 * @throws InvalidDataException If ionisationSource hardware not equal to
	 *         instrument hardware
	 * @throws BaseException If there is another error
	 */
	public void setIonisationSource(HardwareConfiguration ionisationSource)
			throws InvalidDataException
	{
		if (ionisationSource.getHardware() != this.getInstrument())
			throw new InvalidDataException(
				"ionisationSource hardware not equal to instrument hardware");
		getData().setIonisationSource(
			ionisationSource.getData());
	}


//	/**
//	 * Get the {@link File} sourceFile.
//	 *
//	 * @return A <code>File</code> object
//	 * @throws BaseException If there is another error
//	 */
//	public File getSourceFile()
//			throws BaseException
//	{
//		return getDbControl().getItem(File.class, getData().getSourceFile());
//	}

//
//	/**
//	 * Set the {@link File} sourceFile.
//	 *
//	 * @param sourceFile The new <code>File</code> item
//	 * @throws InvalidDataException If sourceFile is null
//	 * @throws BaseException If there is another error
//	 */
//	public void setSourceFile(File sourceFile)
//			throws InvalidDataException
//	{
//		if (sourceFile == null)
//			throw new InvalidUseOfNullException("sourceFile");
//		getData().setSourceFile(sourceFile.getData());
//	}


//	/**
//	 * DataProcessingStep list information. The variables are set to instances
//	 * of class {@link DataProcessingStep}.
//	 */
//	/**
//	 * Get the {@link DataProcessingStep} dataProcessingStepList.
//	 *
//	 * @return A <code>DataProcessingStep</code> list object
//	 * @throws BaseException If there is another error
//	 */
//	public List<DataProcessingStep> getDataProcessingStepList()
//			throws ItemNotFoundException, BaseException
//	{
//		try
//		{
//			int arraySize = getData().getDataProcessingStepList().size();
//			/*
//			 * Create new list of DataProcessingStep items, and add the data
//			 * from the list of DataProcessingStepData items.
//			 */
//			List<DataProcessingStep> dataProcessingStepList = new ArrayList<DataProcessingStep>(
//				arraySize);
//			for (int i = 0; i < arraySize; i++)
//			{
//				dataProcessingStepList.add(i, getDbControl().getItem(
//					DataProcessingStep.class,
//					getData().getDataProcessingStepList().get(i)));
//			}
//			return dataProcessingStepList;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
//	}
//
//
//	/**
//	 * Set the {@link DataProcessingStep} dataProcessingStepList.
//	 *
//	 * @param dataProcessingStepList The new <code>DataProcessingStep</code>
//	 *        list item
//	 * @throws BaseException If there is another error
//	 */
//	public void setDataProcessingStepList(
//			List<DataProcessingStep> dataProcessingStepList)
//			throws InvalidDataException
//	{
//		int arraySize = dataProcessingStepList.size();
//		/*
//		 * Create new list of DataProcessingStepData items, and add the data
//		 * from the list of DataProcessingStep items.
//		 */
//		List<DataProcessingStepData> dataProcessingStepDataList = new ArrayList<DataProcessingStepData>(
//			arraySize);
//		for (int i = 0; i < arraySize; i++)
//		{
//			dataProcessingStepDataList.add(i,
//				dataProcessingStepList.get(i)
//					.getData());
//		}
//		getData().setDataProcessingStepList(
//			dataProcessingStepDataList);
//	}
//
//
//	/**
//	 * Add a DataProcessingStep
//	 *
//	 * @param dp The DataProcessingStep to add to the List
//	 */
//	public void addDataProcessingStep(DataProcessingStep dp)
//	{
//		getData().getDataProcessingStepList().add(
//			dp.getData());
//	}
//
//
//	/**
//	 * @return Returns the spectrumSearches.
//	 */
//	public Set<SpectrumSearch> getSpectrumSearches()
//			throws ItemNotFoundException, BaseException
//	{
//		Set<SpectrumSearch> spectrumSearches = new HashSet<SpectrumSearch>();
//		try
//		{
//			Iterator<SpectrumSearchData> it = getData().getSpectrumSearches()
//				.iterator();
//			while (it.hasNext())
//			{
//				spectrumSearches.add(getDbControl().getItem(
//					SpectrumSearch.class, it.next()));
//			}
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
//		return spectrumSearches;
//	}
//
//
//	/**
//	 * Set the {@link SpectrumSearch} spectrumSearches set.
//	 *
//	 * @param spectrumSearches The new <code>Set<SpectrumSearch></code> item.
//	 * @throws InvalidDataException If dataProcessingStep is null
//	 * @throws BaseException If there is another error
//	 */
//	public void setSpectrumSearches(Set<SpectrumSearch> spectrumSearches)
//	{
//		Set<SpectrumSearchData> ssData = new HashSet<SpectrumSearchData>();
//		Iterator<SpectrumSearch> it = spectrumSearches.iterator();
//		while (it.hasNext())
//		{
//			ssData.add(it.next().getData());
//		}
//		getData().setSpectrumSearches(ssData);
//	}
//

	/**
	 * Get the {@link PeakList} peakListArrayList. It is a set in the database,
	 * but will be returned as a sorted list in ascending spectrumId order
	 * 
	 * @return A <code>PeakList</code> list object
	 * @throws BaseException If there is another error
	 */
	public List<PeakList> getPeakListArrayList()
			throws ItemNotFoundException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		try
//		{
//			/*
//			 * Create new list of PeakList items, and add the data from the list
//			 * of PeakListData items.
//			 */
//			List<PeakList> peakLists = new ArrayList<PeakList>();
//			Iterator<PeakListData> pklit = getData().getPeakLists().iterator();
//			while (pklit.hasNext())
//			{
//				peakLists.add(getDbControl().getItem(PeakList.class,
//					pklit.next()));
//			}
//			Collections.sort(peakLists);
//			return peakLists;
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
	}


	/**
	 * Set the {@link PeakList} peakListArrayList. Observe that it is a set in
	 * the database
	 * 
	 * @param peakListArrayList The new <code>PeakList</code> list item
	 * @throws BaseException If there is another error
	 */
	public void setPeakListArrayList(List<PeakList> peakListArrayList)
			throws InvalidDataException
	{
		/*
		 * Create new list of peakListData items, and add the data from the list
		 * of PeakList items.
		 */
		Set<PeakListData> peakLists = new HashSet<PeakListData>();
		Iterator<PeakList> peakListIterator = peakListArrayList.iterator();
		while (peakListIterator.hasNext())
		{
			peakLists.add(peakListIterator.next().getData());
		}
		getData().setPeakLists(peakLists);
	}


	public void addPeakList(PeakList peakList)
	{
		peakList.setPeakListSet(this);
		getData().getPeakLists().add(peakList.getData());
	}


	@Override
	public boolean isUsed()
			throws BaseException
	{
		return false;
	}
}
