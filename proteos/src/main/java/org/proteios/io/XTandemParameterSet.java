/*
 $Id: XTandemParameterSet.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2008 Gregory Vincic, Olle Mansson

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


/**
 * This file API should be used to get parameter data from XTandem input files.
 * 
 * @author olle
 */
public class XTandemParameterSet
{
	private String listPathDefaultParameters = null;
	private String listPathTaxonomyInformation = null;
	private String spectrumPath = null;
	private String spectrumFragmentMonoisotopicMassError = null;
	private String spectrumParentMonoisotopicMassErrorPlus = null;
	private String spectrumParentMonoisotopicMassErrorMinus = null;
	private String spectrumParentMonoisotopicMassIsotopeError = null;
	private String spectrumFragmentMonoisotopicMassErrorUnits = null;
	private String spectrumParentMonoisotopicMassErrorUnits = null;
	private String spectrumFragmentMassType = null;
	private String spectrumDynamicRange = null;
	private String spectrumTotalPeaks = null;
	private String spectrumMaximumParentCharge = null;
	private String spectrumUseNoiseSuppression = null;
	private String spectrumMinimumParentMPlusH = null;
	private String spectrumMaximumParentMPlusH = null;
	private String spectrumMinimumFragmentMz = null;
	private String spectrumMinimumPeaks = null;
	private String spectrumThreads = null;
	private String spectrumSequenceBatchSize = null;
	private String spectrumUseConditioning = null;
	private String residueModificationMass = null;
	private String residuePotentialModificationMass = null;
	private String residuePotentialModificationMotif = null;
	private String proteinTaxon = null;
	private String proteinCleavageSite = null;
	private String proteinModifiedResidueMassFile = null;
	private String proteinCleavageNTerminalMassChange = null;
	private String proteinCleavageCTerminalMassChange = null;
	private String proteinNTerminalResidueModificationMass = null;
	private String proteinCTerminalResidueModificationMass = null;
	private String proteinHomologManagement = null;
	private String refine = null;
	private String refineModificationMass = null;
	private String refineSequencePath = null;
	private String refineTicPercent = null;
	private String refineSpectrumSynthesis = null;
	private String refineMaximumValidExpectationValue = null;
	private String refinePotentialNTerminusModifications = null;
	private String refinePotentialCTerminusModifications = null;
	private String refineUnanticipatedCleavage = null;
	private String refinePotentialModificationMass = null;
	private String refinePointMutations = null;
	private String refineUsePotentialModificationsForFullRefinement = null;
	private String refinePotentialModificationMotif = null;
	private String scoringMinimumIonCount = null;
	private String scoringMaximumMissedCleavageSites = null;
	private String scoringXIons = null;
	private String scoringYIons = null;
	private String scoringZIons = null;
	private String scoringAIons = null;
	private String scoringBIons = null;
	private String scoringCIons = null;
	private String scoringCyclicPermutation = null;
	private String scoringIncludeReverse = null;
	private String scoringAlgorithm = null;
	private String outputPath = null;
	private String outputLogPath = null;
	private String outputMessage = null;
	private String outputSequencePath = null;
	private String outputSortResultsBy = null;
	private String outputPathHashing = null;
	private String outputXslPath = null;
	private String outputParameters = null;
	private String outputPerformance = null;
	private String outputSpectra = null;
	private String outputHistograms = null;
	private String outputProteins = null;
	private String outputSequences = null;
	private String outputOneSequenceCopy = null;
	private String outputResults = null;
	private String outputMaximumValidExpectationValue = null;
	private String outputHistogramColumnWidth = null;

	/**
	 * @return String list path of default parameters.
	 */
	public String getListPathDefaultParameters()
	{
		return listPathDefaultParameters;
	}


	/**
	 * @param listPathDefaultParameters String list path of default parameters.
	 */
	public void setListPathDefaultParameters(String listPathDefaultParameters)
	{
		this.listPathDefaultParameters = listPathDefaultParameters;
	}


	/**
	 * @return String list path of taxonomy information.
	 */
	public String getListPathTaxonomyInformation()
	{
		return listPathTaxonomyInformation;
	}


	/**
	 * @param listPathTaxonomyInformation String list path of taxonomy information.
	 */
	public void setListPathTaxonomyInformation(String listPathTaxonomyInformation)
	{
		this.listPathTaxonomyInformation = listPathTaxonomyInformation;
	}


	/**
	 * @return String spectrum path.
	 */
	public String getSpectrumPath()
	{
		return spectrumPath;
	}


	/**
	 * @param spectrumPath String spectrum path.
	 */
	public void setSpectrumPath(String spectrumPath)
	{
		this.spectrumPath = spectrumPath;
	}


	/**
	 * @return String spectrum mass error of monoisotopic fragment.
	 */
	public String getSpectrumFragmentMonoisotopicMassError()
	{
		return spectrumFragmentMonoisotopicMassError;
	}
	
	
	/**
	 * @param spectrumFragmentMonoisotopicMassError String spectrum mass error of monoisotopic fragment.
	 */
	public void setSpectrumFragmentMonoisotopicMassError(String spectrumFragmentMonoisotopicMassError)
	{
		this.spectrumFragmentMonoisotopicMassError = spectrumFragmentMonoisotopicMassError;
	}
	
	
	/**
	 * @return String spectrum mass error plus of monoisotopic parent.
	 */
	public String getSpectrumParentMonoisotopicMassErrorPlus()
	{
		return spectrumParentMonoisotopicMassErrorPlus;
	}
	
	
	/**
	 * @param spectrumParentMonoisotopicMassErrorPlus String spectrum mass error plus of monoisotopic parent.
	 */
	public void setSpectrumParentMonoisotopicMassErrorPlus(String spectrumParentMonoisotopicMassErrorPlus)
	{
		this.spectrumParentMonoisotopicMassErrorPlus = spectrumParentMonoisotopicMassErrorPlus;
	}
	
	
	/**
	 * @return String spectrum mass error minus of monoisotopic parent.
	 */
	public String getSpectrumParentMonoisotopicMassErrorMinus()
	{
		return spectrumParentMonoisotopicMassErrorMinus;
	}
	
	
	/**
	 * @param spectrumParentMonoisotopicMassErrorMinus String spectrum mass error minus of monoisotopic parent.
	 */
	public void setSpectrumParentMonoisotopicMassErrorMinus(String spectrumParentMonoisotopicMassErrorMinus)
	{
		this.spectrumParentMonoisotopicMassErrorMinus = spectrumParentMonoisotopicMassErrorMinus;
	}
	
	
	/**
	 * @return String spectrum mass isotope error of monoisotopic parent.
	 */
	public String getSpectrumParentMonoisotopicMassIsotopeError()
	{
		return spectrumParentMonoisotopicMassIsotopeError;
	}


	/**
	 * @param spectrumParentMonoisotopicMassIsotopeError String spectrum mass isotope error of monoisotopic parent.
	 */
	public void setSpectrumParentMonoisotopicMassIsotopeError(String spectrumParentMonoisotopicMassIsotopeError)
	{
		this.spectrumParentMonoisotopicMassIsotopeError = spectrumParentMonoisotopicMassIsotopeError;
	}


	/**
	 * @return String spectrum mass error units of monoisotopic fragment.
	 */
	public String getSpectrumFragmentMonoisotopicMassErrorUnits()
	{
		return spectrumFragmentMonoisotopicMassErrorUnits;
	}


	/**
	 * @param spectrumFragmentMonoisotopicMassErrorUnits String spectrum mass error units of monoisotopic fragment.
	 */
	public void setSpectrumFragmentMonoisotopicMassErrorUnits(String spectrumFragmentMonoisotopicMassErrorUnits)
	{
		this.spectrumFragmentMonoisotopicMassErrorUnits = spectrumFragmentMonoisotopicMassErrorUnits;
	}


	/**
	 * @return String spectrum mass error units of monoisotopic parent.
	 */
	public String getSpectrumParentMonoisotopicMassErrorUnits()
	{
		return spectrumParentMonoisotopicMassErrorUnits;
	}


	/**
	 * @param spectrumParentMonoisotopicMassErrorUnits String spectrum mass error units of monoisotopic parent.
	 */
	public void setSpectrumParentMonoisotopicMassErrorUnits(String spectrumParentMonoisotopicMassErrorUnits)
	{
		this.spectrumParentMonoisotopicMassErrorUnits = spectrumParentMonoisotopicMassErrorUnits;
	}


	/**
	 * @return String spectrum mass type of fragment.
	 */
	public String getSpectrumFragmentMassType()
	{
		return spectrumFragmentMassType;
	}


	/**
	 * @param spectrumFragmentMassType String spectrum mass type of fragment.
	 */
	public void setSpectrumFragmentMassType(String spectrumFragmentMassType)
	{
		this.spectrumFragmentMassType = spectrumFragmentMassType;
	}


	/**
	 * @return String spectrum dynamic range.
	 */
	public String getSpectrumDynamicRange()
	{
		return spectrumDynamicRange;
	}


	/**
	 * @param spectrumDynamicRange String spectrum dynamic range.
	 */
	public void setSpectrumDynamicRange(String spectrumDynamicRange)
	{
		this.spectrumDynamicRange = spectrumDynamicRange;
	}


	/**
	 * @return String spectrum total peaks.
	 */
	public String getSpectrumTotalPeaks()
	{
		return spectrumTotalPeaks;
	}


	/**
	 * @param spectrumTotalPeaks String spectrum total peaks.
	 */
	public void setSpectrumTotalPeaks(String spectrumTotalPeaks)
	{
		this.spectrumTotalPeaks = spectrumTotalPeaks;
	}


	/**
	 * @return String spectrum maximum parent charge.
	 */
	public String getSpectrumMaximumParentCharge()
	{
		return spectrumMaximumParentCharge;
	}


	/**
	 * @param spectrumMaximumParentCharge String spectrum maximum parent charge.
	 */
	public void setSpectrumMaximumParentCharge(String spectrumMaximumParentCharge)
	{
		this.spectrumMaximumParentCharge = spectrumMaximumParentCharge;
	}


	/**
	 * @return String spectrum use noise suppression.
	 */
	public String getSpectrumUseNoiseSuppression()
	{
		return spectrumUseNoiseSuppression;
	}


	/**
	 * @param spectrumUseNoiseSuppression String spectrum use noise suppression.
	 */
	public void setSpectrumUseNoiseSuppression(String spectrumUseNoiseSuppression)
	{
		this.spectrumUseNoiseSuppression = spectrumUseNoiseSuppression;
	}


	/**
	 * @return String spectrum minimum parent m+h.
	 */
	public String getSpectrumMinimumParentMPlusH()
	{
		return spectrumMinimumParentMPlusH;
	}


	/**
	 * @param spectrumMinimumParentMPlusH String spectrum minimum parent m+h.
	 */
	public void setSpectrumMinimumParentMPlusH(String spectrumMinimumParentMPlusH)
	{
		this.spectrumMinimumParentMPlusH = spectrumMinimumParentMPlusH;
	}


	/**
	 * @return String spectrum maximum parent m+h.
	 */
	public String getSpectrumMaximumParentMPlusH()
	{
		return spectrumMaximumParentMPlusH;
	}


	/**
	 * @param spectrumMaximumParentMPlusH String spectrum maximum parent m+h.
	 */
	public void setSpectrumMaximumParentMPlusH(String spectrumMaximumParentMPlusH)
	{
		this.spectrumMaximumParentMPlusH = spectrumMaximumParentMPlusH;
	}


	/**
	 * @return String spectrum minimum fragment mz.
	 */
	public String getSpectrumMinimumFragmentMz()
	{
		return spectrumMinimumFragmentMz;
	}


	/**
	 * @param spectrumMinimumFragmentMz String spectrum minimum fragment mz.
	 */
	public void setSpectrumMinimumFragmentMz(String spectrumMinimumFragmentMz)
	{
		this.spectrumMinimumFragmentMz = spectrumMinimumFragmentMz;
	}


	/**
	 * @return String spectrum minimum peaks.
	 */
	public String getSpectrumMinimumPeaks()
	{
		return spectrumMinimumPeaks;
	}


	/**
	 * @param spectrumMinimumPeaks String spectrum minimum peaks.
	 */
	public void setSpectrumMinimumPeaks(String spectrumMinimumPeaks)
	{
		this.spectrumMinimumPeaks = spectrumMinimumPeaks;
	}


	/**
	 * @return String spectrum threads.
	 */
	public String getSpectrumThreads()
	{
		return spectrumThreads;
	}


	/**
	 * @param spectrumThreads String spectrum threads.
	 */
	public void setSpectrumThreads(String spectrumThreads)
	{
		this.spectrumThreads = spectrumThreads;
	}


	/**
	 * @return String spectrum sequence batch size.
	 */
	public String getSpectrumSequenceBatchSize()
	{
		return spectrumSequenceBatchSize;
	}


	/**
	 * @param spectrumSequenceBatchSize String spectrum sequence batch size.
	 */
	public void setSpectrumSequenceBatchSize(String spectrumSequenceBatchSize)
	{
		this.spectrumSequenceBatchSize = spectrumSequenceBatchSize;
	}


	/**
	 * @return String spectrum use conditioning.
	 */
	public String getSpectrumUseConditioning()
	{
		return spectrumUseConditioning;
	}


	/**
	 * @param spectrumUseConditioning String spectrum use conditioning.
	 */
	public void setSpectrumUseConditioning(String spectrumUseConditioning)
	{
		this.spectrumUseConditioning = spectrumUseConditioning;
	}


	/**
	 * @return String residue modification mass.
	 */
	public String getResidueModificationMass()
	{
		return residueModificationMass;
	}


	/**
	 * @param residueModificationMass String residue modification mass.
	 */
	public void setResidueModificationMass(String residueModificationMass)
	{
		this.residueModificationMass = residueModificationMass;
	}


	/**
	 * @return String residue potential modification mass.
	 */
	public String getResiduePotentialModificationMass()
	{
		return residuePotentialModificationMass;
	}


	/**
	 * @param residuePotentialModificationMass String residue potential modification mass.
	 */
	public void setResiduePotentialModificationMass(String residuePotentialModificationMass)
	{
		this.residuePotentialModificationMass = residuePotentialModificationMass;
	}


	/**
	 * @return String residue potential modification motif.
	 */
	public String getResiduePotentialModificationMotif()
	{
		return residuePotentialModificationMotif;
	}


	/**
	 * @param residuePotentialModificationMotif String residue potential modification motif.
	 */
	public void setResiduePotentialModificationMotif(String residuePotentialModificationMotif)
	{
		this.residuePotentialModificationMotif = residuePotentialModificationMotif;
	}


	/**
	 * @return String protein taxon.
	 */
	public String getProteinTaxon()
	{
		return proteinTaxon;
	}


	/**
	 * @param proteinTaxon String protein taxon.
	 */
	public void setProteinTaxon(String proteinTaxon)
	{
		this.proteinTaxon = proteinTaxon;
	}


	/**
	 * @return String protein cleavage site.
	 */
	public String getProteinCleavageSite()
	{
		return proteinCleavageSite;
	}


	/**
	 * @param proteinCleavageSite String protein cleavage site.
	 */
	public void setProteinCleavageSite(String proteinCleavageSite)
	{
		this.proteinCleavageSite = proteinCleavageSite;
	}


	/**
	 * @return String protein modified residue mass file .
	 */
	public String getProteinModifiedResidueMassFile()
	{
		return proteinModifiedResidueMassFile;
	}


	/**
	 * @param proteinModifiedResidueMassFile String protein modified residue mass file.
	 */
	public void setProteinModifiedResidueMassFile(String proteinModifiedResidueMassFile)
	{
		this.proteinModifiedResidueMassFile = proteinModifiedResidueMassFile;
	}


	/**
	 * @return String protein cleavage N-terminal mass change.
	 */
	public String getProteinCleavageNTerminalMassChange()
	{
		return proteinCleavageNTerminalMassChange;
	}


	/**
	 * @param proteinCleavageNTerminalMassChange String protein cleavage N-terminal mass change.
	 */
	public void setProteinCleavageNTerminalMassChange(String proteinCleavageNTerminalMassChange)
	{
		this.proteinCleavageNTerminalMassChange = proteinCleavageNTerminalMassChange;
	}


	/**
	 * @return String protein cleavage C-terminal mass change.
	 */
	public String getProteinCleavageCTerminalMassChange()
	{
		return proteinCleavageCTerminalMassChange;
	}


	/**
	 * @param proteinCleavageCTerminalMassChange String protein cleavage C-terminal mass change.
	 */
	public void setProteinCleavageCTerminalMassChange(String proteinCleavageCTerminalMassChange)
	{
		this.proteinCleavageCTerminalMassChange = proteinCleavageCTerminalMassChange;
	}


	/**
	 * @return String protein N-terminal residue modification mass.
	 */
	public String getProteinNTerminalResidueModificationMass()
	{
		return proteinNTerminalResidueModificationMass;
	}


	/**
	 * @param proteinNTerminalResidueModificationMass String protein N-terminal residue modification mass.
	 */
	public void setProteinNTerminalResidueModificationMass(String proteinNTerminalResidueModificationMass)
	{
		this.proteinNTerminalResidueModificationMass = proteinNTerminalResidueModificationMass;
	}


	/**
	 * @return String protein C-terminal residue modification mass.
	 */
	public String getProteinCTerminalResidueModificationMass()
	{
		return proteinCTerminalResidueModificationMass;
	}


	/**
	 * @param proteinCTerminalResidueModificationMass String protein C-terminal residue modification mass.
	 */
	public void setProteinCTerminalResidueModificationMass(String proteinCTerminalResidueModificationMass)
	{
		this.proteinCTerminalResidueModificationMass = proteinCTerminalResidueModificationMass;
	}


	/**
	 * @return String protein homolog management.
	 */
	public String getProteinHomologManagement()
	{
		return proteinHomologManagement;
	}


	/**
	 * @param proteinHomologManagement String protein homolog management.
	 */
	public void setProteinHomologManagement(String proteinHomologManagement)
	{
		this.proteinHomologManagement = proteinHomologManagement;
	}


	/**
	 * @return String refine.
	 */
	public String getRefine()
	{
		return refine;
	}


	/**
	 * @param refine String refine.
	 */
	public void setRefine(String refine)
	{
		this.refine = refine;
	}


	/**
	 * @return String refine modification mass.
	 */
	public String getRefineModificationMass()
	{
		return refineModificationMass;
	}


	/**
	 * @param refineModificationMass String refine modification mass.
	 */
	public void setRefineModificationMass(String refineModificationMass)
	{
		this.refineModificationMass = refineModificationMass;
	}


	/**
	 * @return String refine sequence path.
	 */
	public String getRefineSequencePath()
	{
		return refineSequencePath;
	}


	/**
	 * @param refineSequencePath String refine sequence path.
	 */
	public void setRefineSequencePath(String refineSequencePath)
	{
		this.refineSequencePath = refineSequencePath;
	}


	/**
	 * @return String refine tic percent.
	 */
	public String getRefineTicPercent()
	{
		return refineTicPercent;
	}


	/**
	 * @param refineTicPercent String refine tic percent.
	 */
	public void setRefineTicPercent(String refineTicPercent)
	{
		this.refineTicPercent = refineTicPercent;
	}


	/**
	 * @return String refine spectrum synthesis.
	 */
	public String getRefineSpectrumSynthesis()
	{
		return refineSpectrumSynthesis;
	}


	/**
	 * @param refineSpectrumSynthesis String refine spectrum synthesis.
	 */
	public void setRefineSpectrumSynthesis(String refineSpectrumSynthesis)
	{
		this.refineSpectrumSynthesis = refineSpectrumSynthesis;
	}


	/**
	 * @return String refine maximum valid expectation value.
	 */
	public String getRefineMaximumValidExpectationValue()
	{
		return refineMaximumValidExpectationValue;
	}


	/**
	 * @param refineMaximumValidExpectationValue String refine maximum valid expectation value.
	 */
	public void setRefineMaximumValidExpectationValue(String refineMaximumValidExpectationValue)
	{
		this.refineMaximumValidExpectationValue = refineMaximumValidExpectationValue;
	}


	/**
	 * @return String refine potential N-terminus modifications.
	 */
	public String getRefinePotentialNTerminusModifications()
	{
		return refinePotentialNTerminusModifications;
	}


	/**
	 * @param refinePotentialNTerminusModifications String refine potential N-terminus modifications.
	 */
	public void setRefinePotentialNTerminusModifications(String refinePotentialNTerminusModifications)
	{
		this.refinePotentialNTerminusModifications = refinePotentialNTerminusModifications;
	}


	/**
	 * @return String refine potential C-terminus modifications.
	 */
	public String getRefinePotentialCTerminusModifications()
	{
		return refinePotentialCTerminusModifications;
	}


	/**
	 * @param refinePotentialCTerminusModifications String refine potential C-terminus modifications.
	 */
	public void setRefinePotentialCTerminusModifications(String refinePotentialCTerminusModifications)
	{
		this.refinePotentialCTerminusModifications = refinePotentialCTerminusModifications;
	}


	/**
	 * @return String refine unanticipated cleavage.
	 */
	public String getRefineUnanticipatedCleavage()
	{
		return refineUnanticipatedCleavage;
	}


	/**
	 * @param refineUnanticipatedCleavage String refine unanticipated cleavage.
	 */
	public void setRefineUnanticipatedCleavage(String refineUnanticipatedCleavage)
	{
		this.refineUnanticipatedCleavage = refineUnanticipatedCleavage;
	}


	/**
	 * @return String refine potential modification mass.
	 */
	public String getRefinePotentialModificationMass()
	{
		return refinePotentialModificationMass;
	}


	/**
	 * @param refinePotentialModificationMass String refine potential modification mass.
	 */
	public void setRefinePotentialModificationMass(String refinePotentialModificationMass)
	{
		this.refinePotentialModificationMass = refinePotentialModificationMass;
	}


	/**
	 * @return String refine point mutations.
	 */
	public String getRefinePointMutations()
	{
		return refinePointMutations;
	}


	/**
	 * @param refinePointMutations String refine point mutations.
	 */
	public void setRefinePointMutations(String refinePointMutations)
	{
		this.refinePointMutations = refinePointMutations;
	}


	/**
	 * @return String refine use potential modifications for full refinement.
	 */
	public String getRefineUsePotentialModificationsForFullRefinement()
	{
		return refineUsePotentialModificationsForFullRefinement;
	}


	/**
	 * @param refineUsePotentialModificationsForFullRefinement String refine use potential modifications for full refinement.
	 */
	public void setRefineUsePotentialModificationsForFullRefinement(String refineUsePotentialModificationsForFullRefinement)
	{
		this.refineUsePotentialModificationsForFullRefinement = refineUsePotentialModificationsForFullRefinement;
	}


	/**
	 * @return String refine point modification motif.
	 */
	public String getRefinePotentialModificationMotif()
	{
		return refinePotentialModificationMotif;
	}


	/**
	 * @param refinePotentialModificationMotif String refine point modification motif.
	 */
	public void setRefinePotentialModificationMotif(String refinePotentialModificationMotif)
	{
		this.refinePotentialModificationMotif = refinePotentialModificationMotif;
	}


	/**
	 * @return String scoring minimum ion count.
	 */
	public String getScoringMinimumIonCount()
	{
		return scoringMinimumIonCount;
	}


	/**
	 * @param scoringMinimumIonCount String scoring minimum ion count.
	 */
	public void setScoringMinimumIonCount(String scoringMinimumIonCount)
	{
		this.scoringMinimumIonCount = scoringMinimumIonCount;
	}


	/**
	 * @return String scoring maximum missed cleavage sites.
	 */
	public String getScoringMaximumMissedCleavageSites()
	{
		return scoringMaximumMissedCleavageSites;
	}


	/**
	 * @param scoringMaximumMissedCleavageSites String scoring maximum missed cleavage sites.
	 */
	public void setScoringMaximumMissedCleavageSites(String scoringMaximumMissedCleavageSites)
	{
		this.scoringMaximumMissedCleavageSites = scoringMaximumMissedCleavageSites;
	}


	/**
	 * @return String scoring x ions.
	 */
	public String getScoringXIons()
	{
		return scoringXIons;
	}


	/**
	 * @param scoringXIons String scoring x ions.
	 */
	public void setScoringXIons(String scoringXIons)
	{
		this.scoringXIons = scoringXIons;
	}


	/**
	 * @return String scoring y ions.
	 */
	public String getScoringYIons()
	{
		return scoringYIons;
	}


	/**
	 * @param scoringYIons String scoring y ions.
	 */
	public void setScoringYIons(String scoringYIons)
	{
		this.scoringYIons = scoringYIons;
	}


	/**
	 * @return String scoring z ions.
	 */
	public String getScoringZIons()
	{
		return scoringZIons;
	}


	/**
	 * @param scoringZIons String scoring z ions.
	 */
	public void setScoringZIons(String scoringZIons)
	{
		this.scoringZIons = scoringZIons;
	}


	/**
	 * @return String scoring a ions.
	 */
	public String getScoringAIons()
	{
		return scoringAIons;
	}


	/**
	 * @param scoringAIons String scoring a ions.
	 */
	public void setScoringAIons(String scoringAIons)
	{
		this.scoringAIons = scoringAIons;
	}


	/**
	 * @return String scoring b ions.
	 */
	public String getScoringBIons()
	{
		return scoringBIons;
	}


	/**
	 * @param scoringBIons String scoring b ions.
	 */
	public void setScoringBIons(String scoringBIons)
	{
		this.scoringBIons = scoringBIons;
	}


	/**
	 * @return String scoring c ions.
	 */
	public String getScoringCIons()
	{
		return scoringCIons;
	}


	/**
	 * @param scoringCIons String scoring c ions.
	 */
	public void setScoringCIons(String scoringCIons)
	{
		this.scoringCIons = scoringCIons;
	}


	/**
	 * @return String scoring cyclic permutation.
	 */
	public String getScoringCyclicPermutation()
	{
		return scoringCyclicPermutation;
	}


	/**
	 * @param scoringCyclicPermutation String scoring cyclic permutation.
	 */
	public void setScoringCyclicPermutation(String scoringCyclicPermutation)
	{
		this.scoringCyclicPermutation = scoringCyclicPermutation;
	}


	/**
	 * @return String scoring include reverse.
	 */
	public String getScoringIncludeReverse()
	{
		return scoringIncludeReverse;
	}


	/**
	 * @param scoringIncludeReverse String scoring include reverse.
	 */
	public void setScoringIncludeReverse(String scoringIncludeReverse)
	{
		this.scoringIncludeReverse = scoringIncludeReverse;
	}


	/**
	 * @return String scoring algorithm.
	 */
	public String getScoringAlgorithm()
	{
		return scoringAlgorithm;
	}


	/**
	 * @param scoringAlgorithm String scoring algorithm.
	 */
	public void setScoringAlgorithm(String scoringAlgorithm)
	{
		this.scoringAlgorithm = scoringAlgorithm;
	}


	/**
	 * @return String output path.
	 */
	public String getOutputPath()
	{
		return outputPath;
	}


	/**
	 * @param outputPath String output path.
	 */
	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}


	/**
	 * @return String output log path.
	 */
	public String getOutputLogPath()
	{
		return outputLogPath;
	}


	/**
	 * @param outputLogPath String output log path.
	 */
	public void setOutputLogPath(String outputLogPath)
	{
		this.outputLogPath = outputLogPath;
	}


	/**
	 * @return String output message.
	 */
	public String getOutputMessage()
	{
		return outputMessage;
	}


	/**
	 * @param outputMessage String output message.
	 */
	public void setOutputMessage(String outputMessage)
	{
		this.outputMessage = outputMessage;
	}


	/**
	 * @return String output sequence path.
	 */
	public String getOutputSequencePath()
	{
		return outputSequencePath;
	}


	/**
	 * @param outputSequencePath String output sequence path.
	 */
	public void setOutputSequencePath(String outputSequencePath)
	{
		this.outputSequencePath = outputSequencePath;
	}


	/**
	 * @return String output sort results by.
	 */
	public String getOutputSortResultsBy()
	{
		return outputSortResultsBy;
	}


	/**
	 * @param outputSortResultsBy String output sort results by.
	 */
	public void setOutputSortResultsBy(String outputSortResultsBy)
	{
		this.outputSortResultsBy = outputSortResultsBy;
	}


	/**
	 * @return String output path hashing.
	 */
	public String getOutputPathHashing()
	{
		return outputPathHashing;
	}


	/**
	 * @param outputPathHashing String output path hashing.
	 */
	public void setOutputPathHashing(String outputPathHashing)
	{
		this.outputPathHashing = outputPathHashing;
	}


	/**
	 * @return String output xsl path.
	 */
	public String getOutputXslPath()
	{
		return outputXslPath;
	}


	/**
	 * @param outputXslPath String output xsl path.
	 */
	public void setOutputXslPath(String outputXslPath)
	{
		this.outputXslPath = outputXslPath;
	}


	/**
	 * @return String output parameters.
	 */
	public String getOutputParameters()
	{
		return outputParameters;
	}


	/**
	 * @param outputParameters String output parameters.
	 */
	public void setOutputParameters(String outputParameters)
	{
		this.outputParameters = outputParameters;
	}


	/**
	 * @return String output performance.
	 */
	public String getOutputPerformance()
	{
		return outputPerformance;
	}


	/**
	 * @param outputPerformance String output performance.
	 */
	public void setOutputPerformance(String outputPerformance)
	{
		this.outputPerformance = outputPerformance;
	}


	/**
	 * @return String output spectra.
	 */
	public String getOutputSpectra()
	{
		return outputSpectra;
	}


	/**
	 * @param outputSpectra String output spectra.
	 */
	public void setOutputSpectra(String outputSpectra)
	{
		this.outputSpectra = outputSpectra;
	}


	/**
	 * @return String output histograms.
	 */
	public String getOutputHistograms()
	{
		return outputHistograms;
	}


	/**
	 * @param outputHistograms String output histograms.
	 */
	public void setOutputHistograms(String outputHistograms)
	{
		this.outputHistograms = outputHistograms;
	}


	/**
	 * @return String output proteins.
	 */
	public String getOutputProteins()
	{
		return outputProteins;
	}


	/**
	 * @param outputProteins String output proteins.
	 */
	public void setOutputProteins(String outputProteins)
	{
		this.outputProteins = outputProteins;
	}


	/**
	 * @return String output sequences.
	 */
	public String getOutputSequences()
	{
		return outputSequences;
	}


	/**
	 * @param outputSequences String output sequences.
	 */
	public void setOutputSequences(String outputSequences)
	{
		this.outputSequences = outputSequences;
	}


	/**
	 * @return String output one sequence copy.
	 */
	public String getOutputOneSequenceCopy()
	{
		return outputOneSequenceCopy;
	}


	/**
	 * @param outputOneSequenceCopy String output one sequence copy.
	 */
	public void setOutputOneSequenceCopy(String outputOneSequenceCopy)
	{
		this.outputOneSequenceCopy = outputOneSequenceCopy;
	}


	/**
	 * @return String output results.
	 */
	public String getOutputResults()
	{
		return outputResults;
	}


	/**
	 * @param outputResults String output results.
	 */
	public void setOutputResults(String outputResults)
	{
		this.outputResults = outputResults;
	}


	/**
	 * @return String output maximum valid expectation value.
	 */
	public String getOutputMaximumValidExpectationValue()
	{
		return outputMaximumValidExpectationValue;
	}


	/**
	 * @param outputMaximumValidExpectationValue String output maximum valid expectation value.
	 */
	public void setOutputMaximumValidExpectationValue(String outputMaximumValidExpectationValue)
	{
		this.outputMaximumValidExpectationValue = outputMaximumValidExpectationValue;
	}


	/**
	 * @return outputHistogramColumnWidth String output histogram column width.
	 */
	public String getOutputHistogramColumnWidth()
	{
		return outputHistogramColumnWidth;
	}


	/**
	 * @param outputHistogramColumnWidth String output histogram column width.
	 */
	public void setOutputHistogramColumnWidth(String outputHistogramColumnWidth)
	{
		this.outputHistogramColumnWidth = outputHistogramColumnWidth;
	}
}
