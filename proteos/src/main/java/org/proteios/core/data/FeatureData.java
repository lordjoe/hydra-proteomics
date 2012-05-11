package org.proteios.core.data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents information about features from an LC-MS run.
 * 
 * @hibernate.class table="`Features`" lazy="true"
 */
public class FeatureData
		extends BasicData
{
	private Float apexIntensity;
	private Float apexRetentionTimeInMinutes;
	private Integer chargeState;
	private Integer featureExternalId;
	private Double massToChargeRatio;
	private Float startRetentionTimeInMinutes;
	private Float endRetentionTimeInMinutes;
	private Float integratedIntensity;
	private Float signalToNoiseRatio;
	private FileData featureFile;
	private FileData msFile;
	private ProjectData project = null;
	private String peptideSequence = null;
	private Set<HitData> hits = null;


	/**
	 * feature file.
	 * 
	 * @hibernate.many-to-one column="`featureFile`" not-null="false"
	 */
	public FileData getFeatureFile()
	{
		return featureFile;
	}


	public void setFeatureFile(FileData featureFile)
	{
		this.featureFile = featureFile;
	}


	/**
	 * mzML / mzXML file.
	 * 
	 * @hibernate.many-to-one column="`msFile`" not-null="false"
	 */
	public FileData getMsFile()
	{
		return msFile;
	}


	public void setMsFile(FileData msFile)
	{
		this.msFile = msFile;
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
	 * @return Returns the apexIntensity.
	 * @hibernate.property column="`apexIntensity`" type="float"
	 *                     not-null="false"
	 */
	public Float getApexIntensity()
	{
		return apexIntensity;
	}


	/**
	 * @param apexIntensity The apexIntensity to set.
	 */
	public void setApexIntensity(Float apexIntensity)
	{
		this.apexIntensity = apexIntensity;
	}


	/**
	 * @return Returns the apexRetentionTimeInMinutes.
	 * @hibernate.property column="`apexRetentionTimeInMinutes`" type="float"
	 *                     not-null="false"
	 */
	public Float getApexRetentionTimeInMinutes()
	{
		return apexRetentionTimeInMinutes;
	}


	/**
	 * @param apexRetentionTimeInMinutes The apexRetentionTimeInMinutes to set.
	 */
	public void setApexRetentionTimeInMinutes(Float apexRetentionTimeInMinutes)
	{
		this.apexRetentionTimeInMinutes = apexRetentionTimeInMinutes;
	}


	/**
	 * @return Returns the chargeState.
	 * @hibernate.property column="`chargeState`" type="int" not-null="false"
	 */
	public Integer getChargeState()
	{
		return chargeState;
	}


	/**
	 * @param chargeState The chargeState to set.
	 */
	public void setChargeState(Integer chargeState)
	{
		this.chargeState = chargeState;
	}


	/**
	 * @return Returns the massToChargeRatio.
	 * @hibernate.property column="`mz`" type="double" not-null="false"
	 */
	public Double getMassToChargeRatio()
	{
		return massToChargeRatio;
	}


	/**
	 * @param massToChargeRatio The massToChargeRatio to set.
	 */
	public void setMassToChargeRatio(Double massToChargeRatio)
	{
		this.massToChargeRatio = massToChargeRatio;
	}


	/**
	 * @return Returns the startRetentionTimeInMinutes.
	 * @hibernate.property column="`startRetentionTimeInMinutes`" type="float"
	 *                     not-null="false"
	 */
	public Float getStartRetentionTimeInMinutes()
	{
		return startRetentionTimeInMinutes;
	}


	/**
	 * @param startRetentionTimeInMinutes The startRetentionTimeInMinutes to
	 *        set.
	 */
	public void setStartRetentionTimeInMinutes(Float startRetentionTimeInMinutes)
	{
		this.startRetentionTimeInMinutes = startRetentionTimeInMinutes;
	}


	/**
	 * @return Returns the endRetentionTimeInMinutes.
	 * @hibernate.property column="`endRetentionTimeInMinutes`" type="float"
	 *                     not-null="false"
	 */
	public Float getEndRetentionTimeInMinutes()
	{
		return endRetentionTimeInMinutes;
	}


	/**
	 * @param endRetentionTimeInMinutes The endRetentionTimeInMinutes to set.
	 */
	public void setEndRetentionTimeInMinutes(Float endRetentionTimeInMinutes)
	{
		this.endRetentionTimeInMinutes = endRetentionTimeInMinutes;
	}


	/**
	 * @return Returns the integratedIntensity.
	 * @hibernate.property column="`integratedIntensity`" type="float"
	 *                     not-null="false"
	 */
	public Float getIntegratedIntensity()
	{
		return integratedIntensity;
	}


	/**
	 * @param integratedIntensity The integratedIntensity to set.
	 */
	public void setIntegratedIntensity(Float integratedIntensity)
	{
		this.integratedIntensity = integratedIntensity;
	}


	/**
	 * @return Returns the signalToNoiseRatio.
	 * @hibernate.property column="`signalToNoiseRatio`" type="float"
	 *                     not-null="false"
	 */
	public Float getSignalToNoiseRatio()
	{
		return signalToNoiseRatio;
	}


	/**
	 * @param signalToNoiseRatio The signalToNoiseRatio to set.
	 */
	public void setSignalToNoiseRatio(Float signalToNoiseRatio)
	{
		this.signalToNoiseRatio = signalToNoiseRatio;
	}

	public static final int MAX_PEPTIDESEQUENCE_LENGTH = 255;

	/**
	 * @return Returns the peptideSequence.
	 * @hibernate.property column="`peptideSequence`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getPeptideSequence()
	{
		return peptideSequence;
	}


	/**
	 * @param peptideSequence The peptideSequence to set.
	 */
	public void setPeptideSequence(String peptideSequence)
	{
		this.peptideSequence = peptideSequence;
	}


	/**
	 * Get the Hits matched to this feature. Note that the there is no cascading for delete 
	 * 
	 * @hibernate.set table="`Hits`" cascade="none" lazy="true" inverse="true"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.HitData"
	 *                                   not-null="false"
	 * @hibernate.collection-key column="`feature`"
	 * @return The {@link HitData} set of hits
	 */
	public Set<HitData> getHits()
	{
		if (hits == null)
		{
			hits = new HashSet<HitData>();
		}
		return this.hits;
	}


	/**
	 * Set the Hits
	 */
	public void setHits(Set<HitData> hits)
	{
		this.hits = hits;
	}


	/**
	 * @hibernate.property column="`featureExternalId`" type="int" not-null="false"
	   @return Returns the featureExternalId.
	 */
	public Integer getFeatureExternalId()
	{
		return featureExternalId;
	}


	/**
	   @param featureExternalId The featureExternalId to set.
	 */
	public void setFeatureExternalId(Integer featureExternalId)
	{
		this.featureExternalId = featureExternalId;
	}
}
