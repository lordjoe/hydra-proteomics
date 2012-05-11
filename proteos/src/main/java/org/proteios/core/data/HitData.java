/*
 $Id: HitData.java 3532 2010-01-02 20:16:05Z gregory $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios-2.x is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 Proteios is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 */
package org.proteios.core.data;

import java.util.Date;

/**
 * Represents information about a spot and it's identification.
 * 
 * @hibernate.class table="`Hits`" lazy="true"
 */
public class HitData
		extends BasicData
{
	private String gelExternalId = null;
	private Integer spotId = null;
	private Integer spectrumId = null;
	private String spectrumStringId = null;
	private Float retentionTimeInMinutes = null;
	private Float deltaMass = null;
	private String plateId = null;
	private String wellPosition = null;
	private String localSampleId = null;
	private String fractionId = null;
	private Boolean microPlate = null;
	private Date searchDate = null;
	private Float expectationValue = null;
	private Float score = null;
	private String scoreType = null;
	private Boolean protein = null;
	private String externalId = null;
	private String description = null;
	private Float coverage = null;
	private Float mw = null;
	private Float pi = null;
	private Integer matchedPeaks = null;
	private FeatureData feature=null;
	private Integer totalPeaks = null;
	private Float rootMeanSquareError = null;
	private Double totalIntensity = null;
	private Float experimentalMass = null;
	private Integer charge = null;
	private ProjectData project = null;
	private FileData peakListFile = null;
	private FileData identificationResultFile = null;
	private String extraString = null;
	private Float spotXPixel = null;
	private Float spotYPixel = null;
	private Float combinedFDR = null;
	private Boolean primaryCombined = null;
	private HitData firstInCombination = null;
	private Float precursorQuantity = null;


	/**
	 * @hibernate.property column="`gelExternalId`" length="32" type="string"
	 *                     not-null="false" index="gelExtId_idx"
	 */
	public String getGelExternalId()
	{
		return gelExternalId;
	}


	public void setGelExternalId(String gelExternalId)
	{
		this.gelExternalId = gelExternalId;
	}


	/**
	 * @hibernate.property column="`spotId`" type="int" not-null="false"
	 */
	public Integer getSpotId()
	{
		return spotId;
	}


	public void setSpotId(Integer spotId)
	{
		this.spotId = spotId;
	}


	/**
	 * Spectrum identifier
	 * 
	 * @hibernate.property column="`spectrumId`" type="int" not-null="false"
	 */
	public Integer getSpectrumId()
	{
		return spectrumId;
	}


	public void setSpectrumId(Integer spectrumId)
	{
		this.spectrumId = spectrumId;
	}


	/**
	 * The delta mass of this item measured in Dalton.
	 * 
	 * @hibernate.property column="`deltaMass`" type="float" not-null="false"
	 */
	public Float getDeltaMassInDaltons()
	{
		return deltaMass;
	}


	public void setDeltaMassInDaltons(Float deltaMass)
	{
		this.deltaMass = deltaMass;
	}


	/**
	 * A plates external id
	 * 
	 * @hibernate.property column="`plateId`" length="32" type="string"
	 *                     not-null="false"
	 */
	public String getPlateId()
	{
		return plateId;
	}


	public void setPlateId(String plateId)
	{
		this.plateId = plateId;
	}


	/**
	 * A well position, e.g. A1. Constrained to 8 characters.
	 * 
	 * @hibernate.property column="`wellPosition`" length="8" type="string"
	 *                     not-null="false"
	 */
	public String getWellPosition()
	{
		return wellPosition;
	}


	public void setWellPosition(String wellPosition)
	{
		this.wellPosition = wellPosition;
	}


	/**
	 * An analysis sample's external id
	 * 
	 * @hibernate.property column="`localSampleId`" length="32" type="string"
	 *                     not-null="false"
	 */
	public String getLocalSampleId()
	{
		return localSampleId;
	}


	public void setLocalSampleId(String localSampleId)
	{
		this.localSampleId = localSampleId;
	}


	/**
	 * The maximum length of the fraction ID that can be stored in the database.
	 * 
	 * @see #setFractionId(String)
	 */
	public static final int MAX_FRACTION_ID_LENGTH = 8;

	
	/**
	 * A fraction id, e.g. a serial number like 01. Constrained to 8 characters.
	 * 
	 * @hibernate.property column="`fractionId`" length="8" type="string"
	 *                     not-null="false"
	 */
	public String getFractionId()
	{
		return fractionId;
	}


	public void setFractionId(String fractionId)
	{
		this.fractionId = fractionId;
	}


	/**
	 * @hibernate.property column="`microPlate`" type="boolean" not-null="false"
	 */
	public Boolean getMicroPlate()
	{
		return microPlate;
	}


	public void setMicroPlate(Boolean microPlate)
	{
		this.microPlate = microPlate;
	}


	/**
	 * Date when the identification search was performed
	 * 
	 * @hibernate.property column="`searchDate`" type="timestamp"
	 *                     not-null="false"
	 */
	public Date getSearchDate()
	{
		return searchDate;
	}


	public void setSearchDate(Date searchDate)
	{
		this.searchDate = searchDate;
	}


	/**
	 * @hibernate.property column="`eValue`" type="float" not-null="false"
	 */
	public Float getExpectationValue()
	{
		return expectationValue;
	}


	public void setExpectationValue(Float eValue)
	{
		this.expectationValue = eValue;
	}


	/**
	 * @hibernate.property column="`score`" type="float" not-null="false"
	 */
	public Float getScore()
	{
		return score;
	}


	public void setScore(Float score)
	{
		this.score = score;
	}


	/**
	 * @hibernate.property column="`scoreType`" length="32" type="string"
	 *                     not-null="false"
	 */
	public String getScoreType()
	{
		return scoreType;
	}


	public void setScoreType(String scoreType)
	{
		this.scoreType = scoreType;
	}


	/**
	 * @hibernate.property column="`protein`" type="boolean" not-null="false"
	 */
	public Boolean getProtein()
	{
		return protein;
	}


	public void setProtein(Boolean protein)
	{
		this.protein = protein;
	}

	public static final int MAX_EXTERNALID_LENGTH = 255;


	/**
	 * For proteins/peptides this is the Accession number.
	 * 
	 * @hibernate.property column="`externalId`" length="255" type="string"
	 *                     not-null="false"
	 */
	public String getExternalId()
	{
		return externalId;
	}


	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}


	/**
	 * This field is used if the External id was to long.
	 * 
	 * @hibernate.property column="`extraString`" length="2500" type="string"
	 *                     not-null="false"
	 */
	public String getExtraString()
	{
		return extraString;
	}


	public void setExtraString(String extraString)
	{
		this.extraString = extraString;
	}


	/**
	 * The description of a proteion or peptide that was identified.
	 * 
	 * @hibernate.property column="`description`" type="text" not-null="false"
	 */
	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	/**
	 * @hibernate.property column="`coverage`" type="float" not-null="false"
	 */
	public Float getCoverage()
	{
		return coverage;
	}


	public void setCoverage(Float coverage)
	{
		this.coverage = coverage;
	}


	/**
	 * @hibernate.property column="`mw`" type="float" not-null="false"
	 */
	public Float getMwInDaltons()
	{
		return mw;
	}


	public void setMwInDaltons(Float mw)
	{
		this.mw = mw;
	}


	/**
	 * @hibernate.property column="`pi`" type="float" not-null="false"
	 */
	public Float getPi()
	{
		return pi;
	}


	public void setPi(Float pi)
	{
		this.pi = pi;
	}


	/**
	 * @hibernate.property column="`matchedPeaks`" type="int" not-null="false"
	 */
	public Integer getMatchedPeaks()
	{
		return matchedPeaks;
	}


	public void setMatchedPeaks(Integer matchedPeaks)
	{
		this.matchedPeaks = matchedPeaks;
	}


	/**
	 * @hibernate.property column="`totalPeaks`" type="int" not-null="false"
	 */
	public Integer getTotalPeaks()
	{
		return totalPeaks;
	}


	public void setTotalPeaks(Integer totalPeaks)
	{
		this.totalPeaks = totalPeaks;
	}


	/**
	 * @hibernate.property column="`RMSError`" type="float" not-null="false"
	 */
	public Float getRootMeanSquareError()
	{
		return rootMeanSquareError;
	}


	public void setRootMeanSquareError(Float RMSError)
	{
		this.rootMeanSquareError = RMSError;
	}


	/**
	 * The total intensity of a spot
	 * 
	 * @hibernate.property column="`totalIntensity`" type="double"
	 *                     not-null="false"
	 */
	public Double getTotalIntensity()
	{
		return totalIntensity;
	}


	public void setTotalIntensity(Double totalIntensity)
	{
		this.totalIntensity = totalIntensity;
	}


	/**
	 * The experimental mass measured in Dalton.
	 * 
	 * @hibernate.property column="`experimentalMass`" type="float"
	 *                     not-null="false"
	 */
	public Float getExperimentalMassInDaltons()
	{
		return experimentalMass;
	}


	public void setExperimentalMassInDaltons(Float experimentalMass)
	{
		this.experimentalMass = experimentalMass;
	}


	/**
	 * The charge measured in Dalton.
	 * 
	 * @hibernate.property column="`charge`" type="int" not-null="false"
	 */
	public Integer getCharge()
	{
		return charge;
	}


	public void setCharge(Integer charge)
	{
		this.charge = charge;
	}


	/**
	 * Project origin reference.
	 * 
	 * @hibernate.many-to-one column="`project`" not-null="true" update="false"
	 */
	public ProjectData getProject()
	{
		return project;
	}


	public void setProject(ProjectData project)
	{
		this.project = project;
	}


	/**
	 * peakListFile origin reference.
	 * 
	 * @hibernate.many-to-one column="`peakListFile`" not-null="false"
	 */
	public FileData getPeakListFile()
	{
		return peakListFile;
	}


	public void setPeakListFile(FileData peakListFile)
	{
		this.peakListFile = peakListFile;
	}


	/**
	 * identificationResultFile origin reference.
	 * 
	 * @hibernate.many-to-one column="`identificationResultFile`"
	 *                        not-null="false"
	 */
	public FileData getIdentificationResultFile()
	{
		return identificationResultFile;
	}


	public void setIdentificationResultFile(FileData identificationResultFile)
	{
		this.identificationResultFile = identificationResultFile;
	}


	/**
	 * X coordinate of gel spot
	 * 
	 * @hibernate.property column="`spotXPixel`" type="float" not-null="false"
	 * @return Returns the spotXPixel.
	 */
	public Float getSpotXPixel()
	{
		return spotXPixel;
	}


	/**
	 * @param spotXPixel The spotXPixel to set.
	 */
	public void setSpotXPixel(Float spotXPixel)
	{
		this.spotXPixel = spotXPixel;
	}


	/**
	 * Y coordinate of gel spot
	 * 
	 * @return Returns the spotYPixel.
	 * @hibernate.property column="`spotYPixel`" type="float" not-null="false"
	 */
	public Float getSpotYPixel()
	{
		return spotYPixel;
	}


	/**
	 * @param spotYPixel The spotYPixel to set.
	 */
	public void setSpotYPixel(Float spotYPixel)
	{
		this.spotYPixel = spotYPixel;
	}


	/**
	 * @return Returns the combinedFDR.
	 * @hibernate.property column="`combinedFDR`" type="float" not-null="false"
	 */
	public Float getCombinedFDR()
	{
		return combinedFDR;
	}


	/**
	 * @param combinedFDR The combinedFDR to set.
	 */
	public void setCombinedFDR(Float combinedFDR)
	{
		this.combinedFDR = combinedFDR;
	}


	/**
	 * @return Returns tru if this is the primary entry in a combined search.
	 * @hibernate.property column="`primaryCombined`" type="boolean"
	 *                     not-null="false"
	 */
	public Boolean getPrimaryCombined()
	{
		return primaryCombined;
	}


	/**
	 * @param primaryCombined The primaryCombined to set.
	 */
	public void setPrimaryCombined(Boolean primaryCombined)
	{
		this.primaryCombined = primaryCombined;
	}


	/**
	 * @return Returns the firstInCombination.
	 * @hibernate.many-to-one column="`firstHitInCombination`" not-null="false"
	 */
	public HitData getFirstInCombination()
	{
		return firstInCombination;
	}


	/**
	 * @param firstInCombination The firstInCombination to set.
	 */
	public void setFirstInCombination(HitData firstInCombination)
	{
		this.firstInCombination = firstInCombination;
	}

	public static final int MAX_SPECTRUMSTRINGID_LENGTH = 255;


	/**
	 * @return Returns the spectrumStringId.
	 * @hibernate.property column="`spectrumStringId`" length="255"
	 *                     type="string" not-null="false"
	 */
	public String getSpectrumStringId()
	{
		return spectrumStringId;
	}


	/**
	 * @param spectrumStringId The spectrumStringId to set.
	 */
	public void setSpectrumStringId(String spectrumStringId)
	{
		this.spectrumStringId = spectrumStringId;
	}


	/**
	 * @return Returns the retentionTimeInMinutes.
	 * @hibernate.property column="`retentionTime`" type="float"
	 *                     not-null="false"
	 */
	public Float getRetentionTimeInMinutes()
	{
		return retentionTimeInMinutes;
	}


	/**
	 * @param retentionTimeInMinutes The retentionTimeInMinutes to set.
	 */
	public void setRetentionTimeInMinutes(Float retentionTimeInMinutes)
	{
		this.retentionTimeInMinutes = retentionTimeInMinutes;
	}


	/**
	 * 
	 * @return Returns the precursorQuantity.
	 * 
	 * @hibernate.property column="`precursorQuantity`" type="float"
	 *                     not-null="false"
	 */
	public Float getPrecursorQuantity()
	{
		return precursorQuantity;
	}


	/**
	   @param precursorQuantity The precursorQuantity to set.
	 */
	public void setPrecursorQuantity(Float precursorQuantity)
	{
		this.precursorQuantity = precursorQuantity;
	}
	
	/**
	 * Best matching feature
	 * 
	 * @hibernate.many-to-one column="`feature`" not-null="false"
	 */
	public FeatureData getFeature()
	{
		return feature;
	}


	public void setFeature(FeatureData feature)
	{
		this.feature = feature;
	}

}