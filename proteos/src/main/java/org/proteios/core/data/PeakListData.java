/*
 $Id: PeakListData.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
/**
 * This represents a peaklist which is a starting point of a peak.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.PeakList
 * @see <a
 *      href="../../../../../../../development/overview/data/peaklist.html">PeakLists
 *      overview</a>
 * @proteios.modified $Date: 2006-06-01 14:31:54Z $
 * @hibernate.subclass discriminator-value="1"
 * @hibernate.class table="`PeakLists`" lazy="true"
 */
public class PeakListData
		extends AnnotatedData implements BatchableData
{
	public PeakListData()
	{}

	// -------------------------------------------
	/**
	 * The int peakListSet contains the parent PeakListSet.
	 * The creator PeakListSet item.
	 */
	private PeakListSetData peakListSet;


	/**
	 * Get the {@link PeakListSetData} this peakList is created from.
	 * 
	 * @hibernate.many-to-one column="`peakListSetId`" not-null="false"
	 *                        outer-join="false"
	 * @return The <code>PeakListSetData</code> item or null if not known
	 */
	public PeakListSetData getPeakListSet()
	{
		return peakListSet;
	}


	/**
	 * Set the {@link PeakListSetData} this peakList is created from.
	 * 
	 * @param peakListSet The creator peakListSet
	 */
	public void setPeakListSet(PeakListSetData peakListSet)
	{
		this.peakListSet = peakListSet;
	}

	public static final int MAX_COMBINATIONMETHOD_LENGTH = 255;
	private String combinationMethod = null;


	/**
	 * Get the combinationMethod
	 * 
	 * @hibernate.property column="`combination_method`" type="string"
	 *                     length="255" not-null="false"
	 * @return the combinationMethod string
	 */
	public String getCombinationMethod()
	{
		return combinationMethod;
	}


	/**
	 * Set the combinationMethod
	 * 
	 * @param combinationMethod The combinationMethod string
	 */
	public void setCombinationMethod(String combinationMethod)
	{
		this.combinationMethod = combinationMethod;
	}

	public static final int MAX_DESCRIPTION_LENGTH = 255;
	private String description;


	/**
	 * Get the description
	 * 
	 * @hibernate.property column="`description`" type="string" length="255"
	 *                     not-null="false"
	 * @return the description string
	 */
	@Override
	public String getDescription()
	{
		return description;
	}


	/**
	 * Set the description
	 * 
	 * @param description The description string
	 */
	@Override
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * The mzDoublePrecison flag indicates whether the peak list m/z data is measured
	 * at double precison.
	 */
	private boolean mzDoublePrecision = true;


	/**
	 * Get the doublePrecision flag
	 * 
	 * @hibernate.property column="`mz_double_precision`" type="boolean"
	 *                     not-null="true"
	 * @return the mzDoublePrecision flag
	 */
	public boolean isMzDoublePrecision()
	{
		return mzDoublePrecision;
	}


	/**
	 * Set the mzDoublePrecision flag
	 * 
	 * @param doublePrecision The mzDoublePrecision flag
	 */
	public void setMzDoublePrecision(boolean doublePrecision)
	{
		this.mzDoublePrecision = doublePrecision;
	}

	/**
	 * The intensityDoublePrecison flag indicates whether the peak list intensity data is measured
	 * at double precison.
	 */
	private boolean intensityDoublePrecision = false;


	/**
	 * Get the doublePrecision flag
	 * 
	 * @hibernate.property column="`int_double_precision`" type="boolean"
	 *                     not-null="true"
	 * @return the mzDoublePrecision flag
	 */
	public boolean isIntensityDoublePrecision()
	{
		return intensityDoublePrecision;
	}


	/**
	 * Set the intensityDoublePrecision flag
	 * 
	 * @param doublePrecision The intensityDoublePrecision flag
	 */
	public void setIntensityDoublePrecision(boolean doublePrecision)
	{
		this.intensityDoublePrecision = doublePrecision;
	}

	/**
	 * The monoisotopic flag indicates whether the peak list data is
	 * monoisotopic.
	 */
	private boolean monoisotopic = false;


	/**
	 * Get the monoisotopic flag
	 * 
	 * @hibernate.property column="`monoisotopic`" type="boolean"
	 *                     not-null="true"
	 * @return the monoisotopic flag
	 */
	public boolean isMonoisotopic()
	{
		return monoisotopic;
	}


	/**
	 * Set the monoisotopic flag
	 * 
	 * @param monoisotopic The monoisotopic flag
	 */
	public void setMonoisotopic(boolean monoisotopic)
	{
		this.monoisotopic = monoisotopic;
	}

	/**
	 * The msLevel value indicates the level of the mass spectra data.
	 */
	private int msLevel = 1;


	/**
	 * Get the msLevel value
	 * 
	 * @hibernate.property column="`mslevel`" type="int" not-null="true"
	 * @return the msLevel value
	 */
	public int getMsLevel()
	{
		return msLevel;
	}


	/**
	 * Set the msLevel
	 * 
	 * @param msLevel The msLevel value
	 */
	public void setMsLevel(int msLevel)
	{
		this.msLevel = msLevel;
	}

	/**
	 * The total peak intensity.
	 */
	private Float totalIntensity;


	/**
	 * Get the total peak intensity.
	 * 
	 * @hibernate.property column="`total_intensity`" type="float"
	 *                     not-null="false"
	 * @return the totalIntensity value
	 */
	public Float getTotalIntensity()
	{
		return totalIntensity;
	}


	/**
	 * Set the total peak intensity.
	 * 
	 * @param totalIntensity The total intensity value
	 */
	public void setTotalIntensity(Float totalIntensity)
	{
		this.totalIntensity = totalIntensity;
	}
	
	/**
	 * The base peak intensity.
	 */
	private Float basePeakIntensity;


	/**
	 * Get the base peak intensity.
	 * 
	 * @hibernate.property column="`base_peak_intensity`" type="float"
	 *                     not-null="false"
	 * @return the basePeakIntensity value
	 */
	public Float getBasePeakIntensity()
	{
		return basePeakIntensity;
	}


	/**
	 * Set the base peak intensity.
	 * 
	 * @param basePeakIntensity The base peak intensity value
	 */
	public void setBasePeakIntensity(Float basePeakIntensity)
	{
		this.basePeakIntensity = basePeakIntensity;
	}
	
	/**
	 * The start of the mzRange.
	 */
	private Float mzRangeStart;


	/**
	 * Get the start of the mzRange.
	 * 
	 * @hibernate.property column="`mzrange_start`" type="float"
	 *                     not-null="false"
	 * @return the mzRangeStart value
	 */
	public Float getMzRangeStart()
	{
		return mzRangeStart;
	}


	/**
	 * Set the start of the mzRange
	 * 
	 * @param mzRangeStart The msRangeStart value
	 */
	public void setMzRangeStart(Float mzRangeStart)
	{
		this.mzRangeStart = mzRangeStart;
	}

	/**
	 * The stop of the mzRange.
	 */
	private Float mzRangeStop;


	/**
	 * Get the stop of the mzRange.
	 * 
	 * @hibernate.property column="`mzrange_stop`" type="float" not-null="false"
	 * @return the mzRangeStop value
	 */
	public Float getMzRangeStop()
	{
		return mzRangeStop;
	}


	/**
	 * Set the stop of the mzRange
	 * 
	 * @param mzRangeStop The msRangeStop value
	 */
	public void setMzRangeStop(Float mzRangeStop)
	{
		this.mzRangeStop = mzRangeStop;
	}

	/**
	 * Spectrum Id.
	 */
	private int spectrumId;


	/**
	 * Get the spectrumId value Note: No underscore is used in the database
	 * column name, i.e. "spectrumid" instead of "spectrum_id", as the "id" is
	 * part of the name, and doesn't indicate a database index.
	 * 
	 * @hibernate.property column="`spectrumid`" type="int" not-null="true"
	 * @return the spectrumId value
	 */
	public int getSpectrumId()
	{
		return spectrumId;
	}


	/**
	 * Set the spectrumId
	 * 
	 * @param spectrumId The spectrumId value
	 */
	public void setSpectrumId(int spectrumId)
	{
		this.spectrumId = spectrumId;
	}

	/**
	 * The {@link InstrumentConfigurationData} item spectrumInstrument contains
	 * information on the configuration of the intrument used. The variable is
	 * set to an instance of a class extending
	 * {@link InstrumentConfigurationData}, e.g.
	 * {@link HardwareConfigurationData} or {@link SoftwareConfigurationData}.
	 */
	private InstrumentConfigurationData spectrumInstrument;


	/**
	 * Get the spectrumInstrument
	 * 
	 * @hibernate.many-to-one column="`spectrumInstrument_id`" cascade="all"
	 *                        not-null="false" outer-join="false"
	 * @return The {@link InstrumentConfigurationData} spectrumInstrument
	 */
	public InstrumentConfigurationData getSpectrumInstrument()
	{
		return this.spectrumInstrument;
	}


	/**
	 * Set the spectrumInstrument
	 * 
	 * @param spectrumInstrument The {@link InstrumentConfigurationData}
	 *        spectrumInstrument
	 */
	public void setSpectrumInstrument(
			InstrumentConfigurationData spectrumInstrument)
	{
		this.spectrumInstrument = spectrumInstrument;
	}

	/**
	 * The time in minutes (float value)
	 */
	private Float timeInMinutes;


	/**
	 * Get the time in minutes.
	 * 
	 * @hibernate.property column="`time_in_minutes`" type="float"
	 *                     not-null="false"
	 * @return the timeInMinutes value
	 */
	public Float getTimeInMinutes()
	{
		return timeInMinutes;
	}


	/**
	 * Set the time in minutes
	 * 
	 * @param timeInMinutes The timeInMinutes value
	 */
	public void setTimeInMinutes(Float timeInMinutes)
	{
		this.timeInMinutes = timeInMinutes;
	}

	/**
	 * Acquisition list information. The variables are set to instances of class
	 * {@link AcquisitionData}.
	 */
	private List<AcquisitionData> acquisitionList;


	/**
	 * Get the acquisitionList.
	 * 
	 * @see AcquisitionData#getPeakList()
	 * @hibernate.list table="`Acquisitions`" cascade="delete" lazy="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.AcquisitionData"
	 *                                   not-null="false"
	 * @hibernate.collection-index column="`list_index`"
	 * @hibernate.collection-key column="`peakListId`"
	 * @return The {@link AcquisitionData} acquisitionList
	 */
	public List<AcquisitionData> getAcquisitionList()
	{
		if (acquisitionList == null)
		{
			acquisitionList = new ArrayList<AcquisitionData>();
		}
		return this.acquisitionList;
	}


	/**
	 * Set the acquisitionList
	 * 
	 * @param acquisitionList The {@link AcquisitionData} acquisitionList
	 */
	public void setAcquisitionList(List<AcquisitionData> acquisitionList)
	{
		this.acquisitionList = acquisitionList;
	}

	/**
	 * Precursor list information. The variables are set to instances of class
	 * {@link PrecursorData}.
	 */
	private List<PrecursorData> precursorList;


	/**
	 * Get the precursorList.
	 * 
	 * @see PrecursorData#getPeakList()
	 * @hibernate.list table="`Precursors`" cascade="delete" lazy="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.PrecursorData"
	 *                                   not-null="false"
	 * @hibernate.collection-index column="`list_index`"
	 * @hibernate.collection-key column="`peakListId`"
	 * @return The {@link PrecursorData} precursorList
	 */
	public List<PrecursorData> getPrecursorList()
	{
		if (precursorList == null)
		{
			precursorList = new ArrayList<PrecursorData>();
		}
		return this.precursorList;
	}


	/**
	 * Set the precursorList
	 * 
	 * @param precursorList The {@link PrecursorData} precursorList
	 */
	public void setPrecursorList(List<PrecursorData> precursorList)
	{
		this.precursorList = precursorList;
	}

	/**
	 * PeakSet information. The variables are set to instances of class
	 * {@link PeakData}.
	 */
	private Set<PeakData> peakSet=new HashSet<PeakData>();


	/**
	 * Get the peaks.
	 * 
	 * @hibernate.set table="`Peaks`" cascade="delete" lazy="true" inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.PeakData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`peakListId`"
	 * @return The {@link PeakData} peakSet
	 */
	public Set<PeakData> getPeaks()
	{
		return this.peakSet;
	}


	/**
	 * Set the peaks
	 * 
	 * @param peakSet The {@link PeakData} peakSet
	 */
	public void setPeaks(Set<PeakData> peakSet)
	{
		this.peakSet = peakSet;
	}
}
