/*
 $Id: AHit.java 3835 2010-09-08 09:28:25Z fredrik $

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
package org.proteios.io;

import org.proteios.core.Annotatable;
import java.util.Date;
import java.util.List;

/**
 * @author gregory
 */
public class AHit
		implements HitInterface, Annotatable
{
	private Float rms;
	private Double totIntensity;
	private String plateId;
	private Boolean isProtein;
	private String wellPosition;
	private Integer charge;
	private Float coverage;
	private Float deltaMass;
	private String description;
	private Float evalue;
	private Float expMass;
	private String externalId;
	private Integer matchedPeaks;
	private Float mw;
	private Float pi;
	private Float score;
	private String scoreType;
	private Date searched;
	private Integer spotId;
	private Integer totPeaks;
	private Integer spectrumId;
	private String spectrumStringId;
	private Float retentionTimeInMinutes;
	private Float quantity;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getRMSError()
	 */
	public Float getRMSError()
	{
		return rms;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getTotalIntensity()
	 */
	public Double getTotalIntensity()
	{
		return totIntensity;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setRMSError(java.lang.Float)
	 */
	public void setRMSError(Float RMSError)
	{
		this.rms = RMSError;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setTotalIntensity(java.lang.Double)
	 */
	public void setTotalIntensity(Double totalIntensity)
	{
		this.totIntensity = totalIntensity;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getPlateId()
	 */
	public String getPlateId()
	{
		return plateId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getWellPosition()
	 */
	public String getWellPosition()
	{
		return wellPosition;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setPlateId(java.lang.String)
	 */
	public void setPlateId(String plateId)
	{
		this.plateId = plateId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setWellPosition(java.lang.String)
	 */
	public void setWellPosition(String wellPosition)
	{
		this.wellPosition = wellPosition;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getCharge()
	 */
	public Integer getCharge()
	{
		return charge;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getCoverage()
	 */
	public Float getCoverage()
	{
		return coverage;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getDeltaMassInDaltons()
	 */
	public Float getDeltaMassInDaltons()
	{
		return deltaMass;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getDescription()
	 */
	public String getDescription()
	{
		return description;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getEValue()
	 */
	public Float getEValue()
	{
		return evalue;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getExperimentalMassInDaltons()
	 */
	public Float getExperimentalMassInDaltons()
	{
		return expMass;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getExternalId()
	 */
	public String getExternalId()
	{
		return externalId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getMatchedPeaks()
	 */
	public Integer getMatchedPeaks()
	{
		return matchedPeaks;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getMw()
	 */
	public Float getMwInDaltons()
	{
		return mw;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getPi()
	 */
	public Float getPi()
	{
		return pi;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getRetentionTimeInMinutes()
	 */
	public Float getRetentionTimeInMinutes()
	{
		return retentionTimeInMinutes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getScore()
	 */
	public Float getScore()
	{
		return score;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getScoreType()
	 */
	public String getScoreType()
	{
		return scoreType;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getSearchDate()
	 */
	public Date getSearchDate()
	{
		return searched;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getSpotId()
	 */
	public Integer getSpotId()
	{
		return spotId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getTotalPeaks()
	 */
	public Integer getTotalPeaks()
	{
		return totPeaks;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getSpectrumId()
	 */
	public Integer getSpectrumId()
	{
		return spectrumId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getSpectrumStringId()
	 */
	public String getSpectrumStringId()
	{
		return spectrumStringId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#isProtein()
	 */
	public Boolean isProtein()
	{
		return isProtein;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setCharge(java.lang.Integer)
	 */
	public void setCharge(Integer charge)
	{
		this.charge = charge;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setCoverage(java.lang.Float)
	 */
	public void setCoverage(Float coverage)
	{
		this.coverage = coverage;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setDeltaMassInDaltons(java.lang.Float)
	 */
	public void setDeltaMassInDaltons(Float delta)
	{
		this.deltaMass = delta;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setDescription(java.lang.String)
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setEValue(java.lang.Float)
	 */
	public void setEValue(Float eValue)
	{
		this.evalue = eValue;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setExperimentalMassInDaltons(java.lang.Float)
	 */
	public void setExperimentalMassInDaltons(Float experimentalMass)
	{
		this.expMass = experimentalMass;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setExternalId(java.lang.String)
	 */
	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setMatchedPeaks(java.lang.Integer)
	 */
	public void setMatchedPeaks(Integer matchedPeaks)
	{
		this.matchedPeaks = matchedPeaks;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setMw(java.lang.Float)
	 */
	public void setMwInDaltons(Float mw)
	{
		this.mw = mw;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setPi(java.lang.Float)
	 */
	public void setPi(Float pi)
	{
		this.pi = pi;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setProtein(java.lang.Boolean)
	 */
	public void setProtein(Boolean protein)
	{
		this.isProtein = protein;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setRetentionTimeInMinutes(java.lang.Float)
	 */
	public void setRetentionTimeInMinutes(Float retentionTime)
	{
		this.retentionTimeInMinutes = retentionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setScore(java.lang.Float)
	 */
	public void setScore(Float score)
	{
		this.score = score;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setScoreType(java.lang.String)
	 */
	public void setScoreType(String scoreType)
	{
		this.scoreType = scoreType;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setSearchDate(java.sql.Date)
	 */
	public void setSearchDate(Date searchDate)
	{
		this.searched = searchDate;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setSpotId(java.lang.Integer)
	 */
	public void setSpotId(Integer spotId)
	{
		this.spotId = spotId;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setTotalPeaks(java.lang.Integer)
	 */
	public void setTotalPeaks(Integer totalPeaks)
	{
		this.totPeaks = totalPeaks;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setSpectrumId(java.lang.Integer)
	 */
	public void setSpectrumId(Integer spectrumId)
	{
		this.spectrumId = spectrumId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#setSpectrumStringId(java.lang.String)
	 */
	public void setSpectrumStringId(String spectrumStringId)
	{
		this.spectrumStringId = spectrumStringId;
	}
	

	public void setQuantity(Float quantity)
	{
		this.quantity = quantity;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getValueOf(java.lang.String)
	 */
	public String getValueOf(String identifier)
	{
		return null;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteios.io.HitInterface#getValueOf(java.util.List)
	 */
	public List<String> getValueOf(List<String> identifiers)
	{
		return null;
	}


	@Override
	public Float getQuantity()
	{
		return quantity;
	}
}
