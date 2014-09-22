/*
 $Id: PeakListSetData.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This represents a peaklistset which is a starting point of a peaklist.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.PeakListSet
 * @see <a
 *      href="../../../../../../../development/overview/data/peaklistset.html">PeakListSets
 *      overview</a>
 * @proteios.modified $Date: 2006-05-31 14:31:54Z $
 * @hibernate.subclass discriminator-value="1"
 * @hibernate.class table="`PeakListSets`" lazy="true"
 */
public class PeakListSetData
		extends AnnotatedData
{
	public PeakListSetData()
	{}

	// -------------------------------------------
	private SampleData sample;


	/**
	 * Get the sample
	 * 
	 * @hibernate.many-to-one column="`sample_id`" not-null="false"
	 *                        outer-join="false"
	 * @return the SampleData
	 */
	public SampleData getSample()
	{
		return this.sample;
	}


	/**
	 * Set the sample
	 * 
	 * @param sample The SampleData
	 */
	public void setSample(SampleData sample)
	{
		this.sample = sample;
	}

	/**
	 * The {@link InstrumentConfigurationData} item otherInstrumentInfo contains
	 * required instrument configuration information not covered by other
	 * variables of the class. The variable is set to an instance of a class
	 * extending {@link InstrumentConfigurationData}, e.g.
	 * {@link HardwareConfigurationData} or {@link SoftwareConfigurationData}.
	 */
	private InstrumentConfigurationData otherInstrumentInfo;


	/**
	 * Get the otherInstrumentInfo
	 * 
	 * @hibernate.many-to-one column="`otherInstrumentInfo_id`" cascade="delete"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link InstrumentConfigurationData} otherInstrumentInfo
	 */
	public InstrumentConfigurationData getOtherInstrumentInfo()
	{
		return this.otherInstrumentInfo;
	}


	/**
	 * Set the otherInstrumentInfo
	 * 
	 * @param otherInstrumentInfo The {@link InstrumentConfigurationData}
	 *        otherInstrumentInfo
	 */
	public void setOtherInstrumentInfo(
			InstrumentConfigurationData otherInstrumentInfo)
	{
		this.otherInstrumentInfo = otherInstrumentInfo;
	}

	/**
	 * Analyzer configuration information. The variables are set to instances of
	 * class {@link HardwareConfigurationData}.
	 */
	private List<HardwareConfigurationData> analyzers;


	/**
	 * Get the analyzers
	 * 
	 * @hibernate.list table="`InstrumentConfigurations`" cascade="delete"
	 *                 lazy="true"
	 * @hibernate.collection-one-to-many column="`analyzer_id`"
	 *                                   class="org.proteios.core.data.HardwareConfigurationData"
	 *                                   not-null="false"
	 * @hibernate.collection-index column="`list_index`"
	 * @hibernate.collection-key column="`peakListSetId`"
	 * @return The {@link HardwareConfigurationData} analyzers list
	 */
	public List<HardwareConfigurationData> getAnalyzers()
	{
		if (analyzers == null)
		{
			analyzers = new ArrayList<HardwareConfigurationData>();
		}
		return this.analyzers;
	}


	/**
	 * Set the analyzers
	 * 
	 * @param analyzers The {@link HardwareConfigurationData} analyzers
	 */
	public void setAnalyzers(List<HardwareConfigurationData> analyzers)
	{
		this.analyzers = analyzers;
	}

	/**
	 * Detector configuration information. The variable is set to an instance of
	 * class {@link HardwareConfigurationData}.
	 */
	private HardwareConfigurationData detector;


	/**
	 * Get the detector
	 * 
	 * @hibernate.many-to-one column="`detector_id`" cascade="delete"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link HardwareConfigurationData} detector
	 */
	public HardwareConfigurationData getDetector()
	{
		return this.detector;
	}


	/**
	 * Set the detector
	 * 
	 * @param detector The {@link HardwareConfigurationData} detector
	 */
	public void setDetector(HardwareConfigurationData detector)
	{
		this.detector = detector;
	}

	/**
	 * Instrument hardware information. The variable is set to an instance of
	 * class {@link HardwareData}.
	 */
	private HardwareData instrument;


	/**
	 * Get the instrument
	 * 
	 * @hibernate.many-to-one column="`instrument_id`" cascade="none"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link HardwareData} instrument
	 */
	public HardwareData getInstrument()
	{
		return this.instrument;
	}


	/**
	 * Set the instrument
	 * 
	 * @param instrument The {@link HardwareData} instrument
	 */
	public void setInstrument(HardwareData instrument)
	{
		this.instrument = instrument;
	}

	/**
	 * Ionisation source configuration information. The variable is set to an
	 * instance of class {@link HardwareConfigurationData}.
	 */
	private HardwareConfigurationData ionisationSource;


	/**
	 * Get the ionisationSource
	 * 
	 * @hibernate.many-to-one column="`ionisationSource_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link HardwareConfigurationData} ionisationSource
	 */
	public HardwareConfigurationData getIonisationSource()
	{
		return this.ionisationSource;
	}


	/**
	 * Set the ionisationSource
	 * 
	 * @param ionisationSource The {@link HardwareConfigurationData}
	 *        ionisationSource
	 */
	public void setIonisationSource(HardwareConfigurationData ionisationSource)
	{
		this.ionisationSource = ionisationSource;
	}

	/**
	 * Source file. {@link FileData},
	 */
	private FileData sourceFile;


	/**
	 * Get the source file
	 * 
	 * @hibernate.many-to-one column="`sourcefile_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 * @return the FileData
	 */
	public FileData getSourceFile()
	{
		return this.sourceFile;
	}


	/**
	 * Set the source file
	 * 
	 * @param sourceFile The source File
	 */
	public void setSourceFile(FileData sourceFile)
	{
		this.sourceFile = sourceFile;
	}

	/**
	 * DataProcessingStep list information. The variables are set to instances
	 * of class {@link DataProcessingStepData}.
	 */
	private List<DataProcessingStepData> dataProcessingStepList;


	/**
	 * Get the dataProcessingStepList
	 * 
	 * @hibernate.list table="`DataProcessingSteps`" cascade="all" lazy="true"
	 * @hibernate.collection-one-to-many column="`data_proc_step_id`"
	 *                                   class="org.proteios.core.data.DataProcessingStepData"
	 *                                   not-null="false"
	 * @hibernate.collection-index column="`list_index`"
	 * @hibernate.collection-key column="`peakListSetId`"
	 * @return The {@link DataProcessingStepData} dataProcessingStepList
	 */
	public List<DataProcessingStepData> getDataProcessingStepList()
	{
		if (dataProcessingStepList == null)
		{
			dataProcessingStepList = new ArrayList<DataProcessingStepData>();
		}
		return this.dataProcessingStepList;
	}


	/**
	 * Set the dataProcessingStepList
	 * 
	 * @param dataProcessingStepList The {@link DataProcessingStepData}
	 *        dataProcessingStepList
	 */
	public void setDataProcessingStepList(
			List<DataProcessingStepData> dataProcessingStepList)
	{
		this.dataProcessingStepList = dataProcessingStepList;
	}

	private Set<SpectrumSearchData> spectrumSearches;


	/**
	 * @hibernate.set table="`SpectrumSearches`" cascade="none" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SpectrumSearchData"
	 *                                   cascade="none" not-null="false"
	 * @hibernate.collection-key column="`peakListSetId`"
	 * @return Returns the spectrumSearches.
	 */
	public Set<SpectrumSearchData> getSpectrumSearches()
	{
		return spectrumSearches;
	}


	/**
	 * @param spectrumSearches The spectrumSearches to set.
	 */
	public void setSpectrumSearches(Set<SpectrumSearchData> spectrumSearches)
	{
		this.spectrumSearches = spectrumSearches;
	}

	/***************************************************************************
	 * We add a reference to the peakLists in this set to enable cascading
	 * delete through hibernate. Ticket #88
	 */
	private Set<PeakListData> peakLists = new HashSet<PeakListData>();


	/**
	 * @hibernate.set table="`PeakLists`" cascade="delete" lazy="true"
	 *                inverse="true"
	 * @hibernate.collection-key column="`peakListSetId`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.PeakListData"
	 */
	public Set<PeakListData> getPeakLists()
	{
		return peakLists;
	}


	public void setPeakLists(Set<PeakListData> peakLists)
	{
		this.peakLists = peakLists;
	}
}
