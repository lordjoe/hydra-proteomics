/*
 $Id: MgfFileReader.java 3544 2010-01-12 14:37:56Z fredrik $

 Copyright (C) 2008 Fredrik Levander, Gregory Vincic, Olle Mansson

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

import org.proteios.core.BaseException;
import org.proteios.core.InvalidDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * This class contains methods to access spectra from an MGF peak list file.
 * 
 * @author olle
 */
public class MgfFileReader
		implements PeakListFileInterface, SpectrumIdReaderInterface,
		FileValidationInterface
{
	/*
	 * Logger used. Used to log specific events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");
	/*
	 * Input stream for MGF.
	 */
	private InputStream inputStream;
	/*
	 * Flag indicating if spectrum title is used as spectrum id value.
	 */
	private boolean titleUsedAsId = false;
	/*
	 * Peak List Data (in 'data' element)
	 */
	private List<String> spectrumIdsTarget;
	/*
	 * Current MGF spectrum id.
	 */
	private String currentSpectrumId = new String("");
	/*
	 * Spectrum data retrieved from the MGF input.
	 */
	private SpectrumImpl spectrum;
	/*
	 * Spectrum list data as array
	 */
	private SpectrumImpl spectrumArray[];
	/*
	 * List of spectrum id values found.
	 */
	private List<String> spectrumIdsFound = new ArrayList<String>();
	/*
	 * Variables related to search for spectra.
	 */
	private int spectrumIndex = -1;
	private int spectrumTagsFound = 0;
	/*
	 * Variables for temporary storage of precursor values
	 */
	private Double precursorMassToChargeRatio = null;
	private Double precursorIntensity = null;
	private Integer precursorChargeState = null;
	private float basePeakIntensity = 0, totalIntensity = 0;
	/*
	 * Flags indicating when parser is inside target line blocks.
	 */
	private boolean inTargetSpectrumBlock = false;


	/**
	 * Default constructor.
	 */
	public MgfFileReader()
	{}


	/**
	 * Constructor that takes an MGF input stream as parameter.
	 * 
	 * @param inputStream InputStream MGF input stream.
	 */
	public MgfFileReader(InputStream inputStream)
	{
		setInputStream(inputStream);
	}


	/**
	 * Get flag indicating if spectrum title is used as spectrum id value.
	 * 
	 * @return boolean Flag indicating if spectrum title is used as spectrum id
	 *         value.
	 */
	public boolean isTitleUsedAsId()
	{
		return this.titleUsedAsId;
	}


	/**
	 * Set flag indicating if spectrum title is used as spectrum id value.
	 * 
	 * @param titleUsedAsId boolean Flag indicating if spectrum title is used as
	 *        spectrum id value.
	 */
	public void setTitleUsedAsId(boolean titleUsedAsId)
	{
		this.titleUsedAsId = titleUsedAsId;
	}


	/**
	 * Get MGF input stream.
	 * 
	 * @return InputStream MGF input stream.
	 */
	public InputStream getInputStream()
	{
		return this.inputStream;
	}


	/**
	 * Set MGF input stream.
	 * 
	 * @param inputStream InputStream MGF input stream.
	 */
	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}


	/**
	 * Get the list of spectrum ids to search for.
	 * 
	 * @return List<String> list of spectrum ids to search for.
	 */
	private List<String> getSpectrumIdsTarget()
	{
		return this.spectrumIdsTarget;
	}


	/**
	 * Set the list of spectrum ids to search for.
	 * 
	 * @param spectrumIdsTarget List<String> with list of spectrum ids to
	 *        search for.
	 */
	private void setSpectrumIdsTarget(List<String> spectrumIdsTarget)
	{
		this.spectrumIdsTarget = spectrumIdsTarget;
	}


	/**
	 * Reset all lists of id values to search for to empty lists.
	 */
	private void resetIdsToFind()
	{
		/*
		 * Reset all lists of given search items to empty lists, indicating full
		 * search without filtering, if no search items are added.
		 */
		setSpectrumIdsTarget(new ArrayList<String>());
	}


	/**
	 * Get the current spectrum id.
	 * 
	 * @return String current spectrum id.
	 */
	private String getCurrentSpectrumId()
	{
		return this.currentSpectrumId;
	}


	/**
	 * Set the current spectrum id.
	 * 
	 * @param currentSpectrumId String with current spectrum id.
	 */
	private void setCurrentSpectrumId(String currentSpectrumId)
	{
		this.currentSpectrumId = currentSpectrumId;
	}


	/**
	 * Get the spectrum.
	 * 
	 * @return SpectrumImpl spectrum with mass and intensity values.
	 */
	private SpectrumImpl getSpectrum()
	{
		return this.spectrum;
	}


	/**
	 * Set the spectrum.
	 * 
	 * @param spectrum SpectrumImpl with mass and intensity values to set
	 */
	private void setSpectrum(SpectrumImpl spectrum)
	{
		this.spectrum = spectrum;
	}


	/**
	 * Get the list of spectrum id valus found.
	 * 
	 * @return List<String> list of spectrum id values found.
	 */
	private List<String> getSpectrumIdsFound()
	{
		return this.spectrumIdsFound;
	}


	/**
	 * Set the list of spectrum id values found.
	 * 
	 * @param spectrumIdsFound List<String> with list of spectrum id values
	 *        found.
	 */
	private void setSpectrumIdsFound(List<String> spectrumIdsFound)
	{
		this.spectrumIdsFound = spectrumIdsFound;
	}


	/**
	 * Check if the spectrum id is in the target list. If it is a new spectrum
	 * is initialised.
	 */
	private void initIfTargetSpectrum(String currentSpectrumId)
	{
		for (int i = 0; i < getSpectrumIdsTarget().size(); i++)
		{
			if (currentSpectrumId.equals(getSpectrumIdsTarget().get(i)))
			{
				/*
				 * Save information in order to get spectrum mass and intensity
				 * values.
				 */
				setCurrentSpectrumId(currentSpectrumId);
				inTargetSpectrumBlock = true;
				spectrumIndex = i;
				/*
				 * Get new spectrum object to store retrieved data in.
				 */
				SpectrumImpl currentSpectrum = SpectrumImpl.buildSpectrum();  // changed slewis to allow class to change
				setSpectrum(currentSpectrum);
				log
					.debug("SpectrumId (" + spectrumIndex + ") = \"" + getCurrentSpectrumId() + "\" found.");
			}
		}
		/*
		 * reset spectrum variables
		 */
		totalIntensity = 0;
		basePeakIntensity = 0;
		precursorMassToChargeRatio = null;
		precursorIntensity = null;
		precursorChargeState = null;
	}


	/**
	 * Fetch spectra with specified MGF spectrum id values from input stream.
	 * Since an MGF file does not specify explicit spectrum id values, a
	 * spectrum id is here identified with the ordering number for a spectra in
	 * the MGF file, starting with 1 for the first spectra in the file.
	 * 
	 * @param spectrumIds List<String> list with MGF spectrum id values for
	 *        desired spectra.
	 * @return List<SpectrumInterface> list with spectrum data.
	 */
	private List<SpectrumInterface> fetchSpectrum(List<String> spectrumIds)
	{
		if (spectrumIds == null)
		{
			return null;
		}
		InputStream iStream = getInputStream();
		// Remove any spectrum titles if retrieving by index
		List<String> pureSpectrumIds = new ArrayList<String>(0);
		if (!isTitleUsedAsId())
		{
			for (String idString : spectrumIds)
			{
				int strIndex = idString.indexOf(" ");
				if (strIndex >= 0)
				{
					// Remove part beginning with first blank
					idString = idString.substring(0, strIndex);
				}
				pureSpectrumIds.add(idString);
			}
			setSpectrumIdsTarget(pureSpectrumIds);
		}
		else
		{
			setSpectrumIdsTarget(spectrumIds);
		}
		/*
		 * Reset spectrum data.
		 */
		int nSpectra = spectrumIds.size();
		spectrumArray = new SpectrumImpl[nSpectra];
		/*
		 * Reset spectrum id list data.
		 */
		List<String> spectrumIdsFound = new ArrayList<String>();
		setSpectrumIdsFound(spectrumIdsFound);
		/*
		 * Create list of mass and intensity data and get dataLength as side
		 * result.
		 */
		int dataLength = 0;
		List<Double> peakMassData = new ArrayList<Double>();
		List<Double> peakIntensityData = new ArrayList<Double>();
		/*
		 * Process spectra in MGF file
		 */
		int numberOfSpectra = 0;
		/*
		 * Start of spectra reading
		 */
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
				iStream));
			spectrumTagsFound = 0;
			inTargetSpectrumBlock = false;
			String line;
			int lineNo = 0;
			while ((getSpectrumIdsTarget() != null && spectrumTagsFound < getSpectrumIdsTarget()
				.size()) && (line = in.readLine()) != null)
			{
				if (isTitleUsedAsId() && line.matches("^TITLE=.*"))
				{
					String title = line.substring(6);
					initIfTargetSpectrum(title);
				}
				if (line.equals("BEGIN IONS"))
				{
					/*
					 * New spectrum
					 */
					numberOfSpectra++;
					// log.debug("New spectrum: numberOfSpectra = " +
					// numberOfSpectra);
					// Use spectrum order number as spectrum id value
					if (!isTitleUsedAsId())
					{
						String currentSpectrumIdStr = Integer
							.toString(numberOfSpectra);
						setCurrentSpectrumId(currentSpectrumIdStr);
						/*
						 * Add spectrum id to list if not already in it.
						 */
						if (!getSpectrumIdsFound().contains(
							currentSpectrumIdStr))
						{
							getSpectrumIdsFound().add(currentSpectrumIdStr);
						}
						/*
						 * Check if desired spectrum id found.
						 */
						spectrumIndex = -1;
						initIfTargetSpectrum(currentSpectrumIdStr);
					}
				}
				else if (inTargetSpectrumBlock && line
					.matches("^RTINSECONDS=.*"))
				{
					double rtInSeconds = Double.parseDouble(line.substring(12));
					getSpectrum().setRetentionTimeInMinutes(
						new Double(rtInSeconds / 60));
				}
				else if (inTargetSpectrumBlock && line
					.matches("^PEPMASS=\\d+\\.?\\d*[[ \\t]+*\\d+\\.?\\d*]*"))
				{
					// Line with 1 or 2 columns, PEPMASS=(float), or
					// PEPMASS=(float, float)
					/*
					 * Collect data for precursor mass to charge ratio
					 */
					String precursorStr = line.substring(8);
					StringTokenizer st = new StringTokenizer(precursorStr);
					try
					{
						precursorMassToChargeRatio = Double.valueOf(st
							.nextToken());
					}
					catch (Exception e)
					{
						log
							.debug("Caught exception when trying to get precursor m/z value: " + e);
					}
					/*
					 * Collect optional data for precursor intensity
					 */
					try
					{
						precursorIntensity = Double.valueOf(st.nextToken());
					}
					catch (Exception e)
					{
						log
							.debug("Caught exception when trying to get precursor intensity value: " + e);
					}
				}
				else if (inTargetSpectrumBlock && line
					.matches("^CHARGE=\\d+.*"))
				{
					// Line with CHARGE=(int)*
					/*
					 * Collect data for precursor charge state
					 */
					try
					{
						String chargeStateDigitStr = null;
						String chargeStateSignStr = null;
						String tmpStr = line.substring(7);
						if (tmpStr != null && !tmpStr.equals(""))
						{
							// Get first character, should be digit
							chargeStateDigitStr = tmpStr.substring(0, 1);
							tmpStr = tmpStr.substring(0);
							if (tmpStr != null && !tmpStr.equals(""))
							{
								// Get next character, should be + or -
								chargeStateSignStr = tmpStr.substring(0, 1);
								tmpStr = tmpStr.substring(0);
							}
						}
						if (chargeStateDigitStr != null)
						{
							precursorChargeState = Integer
								.valueOf(chargeStateDigitStr);
							// Check if charge state is negative
							if (chargeStateSignStr != null && chargeStateSignStr
								.equals("-"))
							{
								precursorChargeState = -precursorChargeState;
							}
						}
					}
					catch (Exception e)
					{
						log.debug("Caught exception : " + e);
					}
				}
				else if (inTargetSpectrumBlock && line
					.matches("^\\d+\\.?\\d*[ \\t]+\\d+\\.?\\d*[ \\t]*"))
				{
					// Line with 2 columns (float, float)
					/*
					 * Collect data for peak.
					 */
					StringTokenizer st = new StringTokenizer(line);
					double peakMass = Double.valueOf(st.nextToken());
					double peakIntensity = Double.valueOf(st.nextToken());
					// log.debug("Spectra line (" + lineNo + ") = \"" + line +
					// "\", peakMass = " + peakMass + " peakIntensity = " +
					// peakIntensity);
					peakMassData.add(dataLength, peakMass);
					peakIntensityData.add(dataLength, peakIntensity);
					dataLength++;
					// Update statistical spectrum values
					if (peakIntensity > basePeakIntensity)
					{
						basePeakIntensity = (float) peakIntensity;
					}
					totalIntensity += peakIntensity;
				}
				if (line.equals("END IONS"))
				{
					/*
					 * End of spectrum
					 */
					// log.debug("End of spectrum: numberOfSpectra = " +
					// numberOfSpectra);
					/*
					 * Save peak data for previous peak list.
					 */
					if (inTargetSpectrumBlock)
					{
						/*
						 * Add retrieved spectrum data to all elements in result
						 * list corresponding to current spectrum ids.
						 */
						for (int i = 0; i < getSpectrumIdsTarget().size(); i++)
						{
							if (getCurrentSpectrumId().equals(
								getSpectrumIdsTarget().get(i)))
							{
								// Convert spectrum data arrays
								if (dataLength > 0)
								{
									double peakMassArray[] = new double[dataLength];
									double peakIntensityArray[] = new double[dataLength];
									for (int k = 0; k < dataLength; k++)
									{
										peakMassArray[k] = peakMassData.get(k)
											.doubleValue();
										peakIntensityArray[k] = peakIntensityData
											.get(k).doubleValue();
									}
									getSpectrum().setMass(peakMassArray);
									getSpectrum().setIntensities(
										peakIntensityArray);
								}
								spectrumArray[i] = getSpectrum();
								spectrumTagsFound++;
								log
									.debug("SpectrumId (" + i + ") = \"" + getCurrentSpectrumId() + "\" processed, spectrumTagsFound = " + spectrumTagsFound);
								log
									.debug("-----------------------------------------------");
							}
						}
						// Add precursor info
						if (precursorMassToChargeRatio != null || precursorIntensity != null || precursorChargeState != null)
						{
							SpectrumPrecursor spectrumPrecursor = SpectrumPrecursor.buildSpectrumPrecursor(); // changed slewis to support overrides
							if (precursorMassToChargeRatio != null)
							{
								spectrumPrecursor
									.setMassToChargeRatio(precursorMassToChargeRatio
										.doubleValue());
							}
							if (precursorIntensity != null)
							{
								spectrumPrecursor
									.setIntensity(precursorIntensity
										.doubleValue());
							}
							if (precursorChargeState != null)
							{
								spectrumPrecursor
									.setCharge(precursorChargeState);
							}
							List<SpectrumPrecursor> precursors = new ArrayList<SpectrumPrecursor>();
							precursors.add(spectrumPrecursor);
							getSpectrum().setPrecursors(precursors);
						}
					}
					inTargetSpectrumBlock = false;
					/*
					 * Reset peak list data.
					 */
					dataLength = 0;
					peakMassData = new ArrayList<Double>();
					peakIntensityData = new ArrayList<Double>();
				}
				else
				{
					/*
					 * Other line
					 */
					// log.debug("Unidentified line (" + lineNo + ") = \"" +
					// line + "\"");
				}
				lineNo++;
			}
			/*
			 * Return result of search for MGF spectra.
			 */
			List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>(
				nSpectra);
			for (int i = 0; i < nSpectra; i++)
			{
				spectrumList.add(i, spectrumArray[i]);
			}
			return spectrumList;
		}
		catch (IOException exept)
		{
			String message = exept.getMessage();
			log.warn(message);
			throw new BaseException(message);
		}
	}


	/**
	 * Fetch spectrum id values from input stream. Since an MGF file does not
	 * specify explicit spectrum id values, a spectrum id is here identified
	 * with the ordering number for a spectra in the MGF file, starting with 1
	 * for the first spectra in the file. If an optional spectrum title exists,
	 * it is appended after the spectrum order number and the string " - ".
	 * 
	 * @return List<String> list with spectrum id data.
	 */
	private List<String> fetchSpectrumIds()
			throws InvalidDataException
	{
		/*
		 * Reset search items.
		 */
		resetIdsToFind();
		/*
		 * Reset spectrum id list data.
		 */
		List<String> spectrumIdsFound = new ArrayList<String>();
		setSpectrumIdsFound(spectrumIdsFound);
		InputStream iStream = getInputStream();
		/*
		 * Process spectra in MGF file
		 */
		int numberOfSpectra = 0;
		String currentSpectrumIdStr = null;
		String spectrumTitle = null;
		/*
		 * Start of spectra reading
		 */
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
				iStream));
			String line;
			while ((line = in.readLine()) != null)
			{
				// Line starting new spectra
				if (line.equals("BEGIN IONS"))
				{
					// New spectrum
					numberOfSpectra++;
					// Reset spectrum title
					spectrumTitle = null;
					// Use spectra order number as spectrum id value
					currentSpectrumIdStr = Integer.toString(numberOfSpectra);
					// getSpectrumIdsFound().add(currentSpectrumIdStr);
				}
				else if (line.matches("^TITLE=.*"))
				{
					// Remove prefix "TITLE=" from spectrum title
					spectrumTitle = line.substring(6);
					// Use spectrum order number with optional title as spectrum
					// id value
					if (spectrumTitle != null && !spectrumTitle.equals(""))
					{
						currentSpectrumIdStr = currentSpectrumIdStr + " - " + spectrumTitle;
					}
				}
				else if (line.equals("END IONS"))
				{
					// Remove any trailing white space (always begins with
					// spectrum order number)
					currentSpectrumIdStr = currentSpectrumIdStr.trim();
					// Save spectrum order number with optional title
					getSpectrumIdsFound().add(currentSpectrumIdStr);
					// Reset spectrum id and title
					currentSpectrumIdStr = null;
					spectrumTitle = null;
				}
			}
		}
		catch (IOException exept)
		{
			String message = exept.getMessage();
			log.warn(message);
			return null;
		}
		/*
		 * Return null if no items found.
		 */
		if (getSpectrumIdsFound().size() == 0)
		{
			return null;
		}
		/*
		 * Return result of search for spectrum id values.
		 */
		return getSpectrumIdsFound();
	}


	/**
	 * Validate the entire MGF file.
	 * 
	 * @param instream input stream to validate.
	 * @return true if valid
	 */
	private boolean valid(InputStream instream)
			throws BaseException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(instream));
		String line = new String();
		try
		{
			while ((line = in.readLine()) != null)
			{
				// Line with 2 columns (float, float) or starting with capital
				// letter or comment
				if (line.length() > 0)
				{
					if (!(Pattern.matches("^\\d+\\.?\\d*[ \\t]+\\d+\\.?\\d*",
						line) || (Pattern.matches("^[A-Z#/!;].*", line))))
					{
						log.warn("Line:" + line + " not valid for MGF");
						return false;
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new BaseException(e);
		}
		return true;
	}


	/**
	 * Check if the input stream has lines starting with COM or BEGIN IONS. If
	 * so it is considered importable
	 * 
	 * @param in InputStream input stream to check for MGF start line.
	 * @return true if input stream contains MGF start line, else false.
	 */
	private final boolean isImportable(InputStream in)
	{
		boolean ok = false;
		try
		{
			BufferedReader inreader = new BufferedReader(new InputStreamReader(
				in));
			ok = false;
			boolean done = false;
			String line = new String();
			while (((line = inreader.readLine()) != null) && (!done))
			{
				if (line.startsWith("COM") || line.startsWith("BEGIN IONS"))
				{
					ok = true;
					done = true;
				}
			}
			inreader.close();
		}
		catch (IOException e)
		{
			ok = false;
		}
		return ok;
	}


	// -------------------------------------------
	/*
	 * From the PeakListFileInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get the spectrum data for specified spectrum id.
	 * 
	 * @param spectrumId String spectrum id.
	 * @return spectrum with specified spectrum id.
	 */
	public SpectrumInterface getSpectrum(String spectrumId)
	{
		List<String> spectrumIds = new ArrayList<String>();
		spectrumIds.add(0, spectrumId);
		/*
		 * Search for desired spectra.
		 */
		List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>();
		spectrumList = fetchSpectrum(spectrumIds);
		/*
		 * Return spectrum.
		 */
		return spectrumList.get(0);
	}


	/**
	 * Get an iterator for spectra with specified spectrum id values.
	 * 
	 * @param spectrumIds List<String> list with spectrum id values.
	 * @return iterator for spectra with specified spectrum id values.
	 */
	public Iterator<SpectrumInterface> getSpectrum(List<String> spectrumIds)
	{
		/*
		 * Search for desired spectra.
		 */
		List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>();
		spectrumList = fetchSpectrum(spectrumIds);
		log
			.debug("spectrumIds.size() = " + spectrumIds.size() + ", spectrumList.size() = " + spectrumList
				.size());
		/*
		 * Return spectrum iterator.
		 */
		return spectrumList.iterator();
	}


	// -------------------------------------------
	/*
	 * From the SpectrumIdReaderInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get spectrum id list. If spectra are containing titles, the spectrum id
	 * format will be 'spectrumIndex - TITLE'
	 * 
	 * @return List<String> List with spectrum id values.
	 */
	public List<String> getSpectrumIdList()
	{
		/*
		 * Return list of spectrum id values.
		 */
		return fetchSpectrumIds();
	}


	/**
	 * Specifies if the spectrum id values were obtained from the spectrum order
	 * numbers.
	 * 
	 * @return boolean True if the spectrum id values were obtained from
	 *         spectrum order numbers, else false.
	 */
	public boolean isSpectrumIdObtainedFromSpectrumOrderNumber()
	{
		return true;
	}


	// -------------------------------------------
	/*
	 * From the FileValidationInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Validates MGF input stream.
	 * 
	 * @return True if the MGF input stream is valid, else false
	 * @throws BaseException If there is an error
	 */
	public boolean valid()
	{
		return valid(getInputStream());
	}


	/**
	 * Validates first line of input stream.
	 * 
	 * @return True if the input stream is valid, else false
	 */
	public boolean importable()
	{
		return isImportable(getInputStream());
	}
}
