/*
 $Id: PklFileReader.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This class contains methods to access spectra from a PKL peak list file.
 * 
 * @author olle
 */
public class PklFileReader
	implements PeakListFileInterface, SpectrumIdReaderInterface, FileValidationInterface
{
	/*
	 * Logger used. Used to log specific events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");
	/*
	 * Input stream for PKL.
	 */
	private InputStream inputStream;
	/*
	 * Peak List Data (in 'data' element)
	 */
	private int dataLength = 0;
	/*
	 * Precision of data
	 */
	private boolean mz_double_precision = false;
	private boolean inten_double_precision = false;
	/*
	 * List of PKL spectrum ids to search for.
	 */
	private List<String> spectrumIdsTarget;
	/*
	 * Current PKL spectrum id.
	 */
	private String currentSpectrumId = new String("");
	/*
	 * Spectrum data retrieved from the PKL input.
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
	 * Flags indicating when parser is inside target line blocks.
	 */
	private boolean inTargetSpectrumBlock = false;


	/**
	 * Default constructor.
	 */
	public PklFileReader()
	{}


	/**
	 * Constructor that takes a PKL input stream as parameter.
	 * 
	 * @param inputStream InputStream PKL input stream.
	 */
	public PklFileReader(InputStream inputStream)
	{
		setInputStream(inputStream);
	}


	/**
	 * Get PKL input stream.
	 * 
	 * @return InputStream PKL input stream.
	 */
	public InputStream getInputStream()
	{
		return this.inputStream;
	}


	/**
	 * Set PKL input stream.
	 * 
	 * @param inputStream InputStream PKL input stream.
	 */
	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}


	/**
	 * Get flag for m/z double precision.
	 * 
	 * @return double Flag for m/z double precision
	 */
	private boolean isMzDoublePrecision()
	{
		return this.mz_double_precision;
	}


	/**
	 * Set flag for m/z double precision.
	 * 
	 * @param mz_double_precision boolean The flag for m/z double precision to set.
	 */
	private void setMzDoublePrecision(boolean mz_double_precision)
	{
		this.mz_double_precision = mz_double_precision;
	}

	
	/**
	 * Get the dataLength.
	 * 
	 * @return dataLength int the dataLength
	 */
	private int getDataLength()
	{
		return this.dataLength;
	}


	/**
	 * Set the dataLength.
	 * 
	 * @param dataLength int the dataLength to set.
	 */
	private void setDataLength(int dataLength)
	{
		this.dataLength = dataLength;
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
	 * @param spectrumIdsTarget List<String> with list of spectrum ids to search for.
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
	 * @param spectrumIdsFound List<String> with list of spectrum id values found.
	 */
	private void setSpectrumIdsFound(List<String> spectrumIdsFound)
	{
		this.spectrumIdsFound = spectrumIdsFound;
	}


	/**
	 * Fetch spectra with specified PKL spectrum id values from input stream.
	 * Since a PKL file does not specify explicit
	 * spectrum id values, a spectrum id is here
	 * identified with the ordering number for a
	 * spectra in the PKL file, starting with 1 for
	 * the first spectra in the file. 
	 * 
	 * @param spectrumIds List<String> list with PKL spectrum id values for desired spectra.
	 * @return List<SpectrumInterface> list with spectrum data.
	 */
	private List<SpectrumInterface> fetchSpectrum(List<String> spectrumIds)
	{
		if (spectrumIds == null)
		{
			return null;
		}
		setSpectrumIdsTarget(spectrumIds);
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
		InputStream iStream = getInputStream();
		
		/*
		 * Create list of mass and intensity data and get dataLength as side
		 * result.
		 */
		int dataLength = 0;
		List<Double> peakMassData = new ArrayList<Double>();
		List<Double> peakIntensityData = new ArrayList<Double>();
		/*
		 * Process spectra in PKL file
		 */
		int numberOfSpectra = 0;
		float basePeakIntensity = 0, totalIntensity = 0;
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
			while ((line = in.readLine()) != null)
			{
				if (line.matches("^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*[ \\t]\\d"))
				{
					// Line with 3 columns (float, float, digit)
					/*
					 * New spectrum
					 */
					//log.debug("New spectrum: numberOfSpectra = " + numberOfSpectra);
					/*
					 * Check if data for previous peak list has been collected.
					 */
					if (numberOfSpectra > 0)
					{
						/*
						 * Save peak data for previous peak list.
						 */
						if (inTargetSpectrumBlock)
						{
							/*
							 * Add retrieved spectrum data to all elements
							 * in result list corresponding to current spectrum ids.
							 */
							for (int i = 0; i < getSpectrumIdsTarget().size(); i++) {
								if (getCurrentSpectrumId().equals(getSpectrumIdsTarget().get(i)))
								{
									// Convert spectrum data arrays
									if (dataLength > 0)
									{
										double peakMassArray[] = new double[dataLength];
										double peakIntensityArray[] = new double[dataLength];
										for (int k = 0; k < dataLength; k++)
										{
											peakMassArray[k] = peakMassData.get(k).doubleValue();
											peakIntensityArray[k] = peakIntensityData.get(k).doubleValue();
										}
										getSpectrum().setMass(peakMassArray);
										getSpectrum().setIntensities(peakIntensityArray);
									}
									spectrumArray[i] = getSpectrum();
									spectrumTagsFound++;
									log.debug("SpectrumId (" + i + ") = \"" + getCurrentSpectrumId() + "\" processed, spectrumTagsFound = " + spectrumTagsFound);
									log.debug("-----------------------------------------------");
								}
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
					numberOfSpectra++;
					// Use spectra order number as spectrum id value
					String currentSpectrumIdStr = Integer.toString(numberOfSpectra);
					setCurrentSpectrumId(currentSpectrumIdStr);
					/*
					 * Add spectrum id to list if not already in it.
					 */
					if (!getSpectrumIdsFound().contains(currentSpectrumIdStr))
					{
						getSpectrumIdsFound().add(currentSpectrumIdStr);
					}
					/*
					 * Check if desired spectrum id found.
					 */
					spectrumIndex = -1;
					for (int i = 0; i < getSpectrumIdsTarget().size(); i++) {
						if (currentSpectrumIdStr.equals(getSpectrumIdsTarget().get(i)))
						{
							/*
							 * Save information in order to get
							 * spectrum mass and intensity values.
							 */
							setCurrentSpectrumId(currentSpectrumIdStr);
							inTargetSpectrumBlock = true;
							spectrumIndex = i;
							/*
							 * Get new spectrum object to store retrieved data in.
							 */
							SpectrumImpl currentSpectrum = SpectrumImpl.buildSpectrum();  // changed slewis to allow class to change
							setSpectrum(currentSpectrum);
							log.debug("SpectrumId (" + spectrumIndex + ") = \"" + getCurrentSpectrumId() + "\" found.");
							/*
							 * Get spectrum header data from PKL file
							 */
							totalIntensity = 0;
							basePeakIntensity = 0;
							StringTokenizer st = new StringTokenizer(line);
							Float massToChargeRatio = Float.valueOf(st.nextToken());
							Float intensity = Float.valueOf(st.nextToken());
							Integer chargeState = Integer.valueOf(st.nextToken());
							// Store spectrum header values from PKL file
							//log.debug("SpectrumId = \"" + getCurrentSpectrumId() + "\" massToChargeRatio = " + massToChargeRatio + " intensity = " + intensity + " chargeState = " + chargeState);
							SpectrumPrecursor spectrumPrecursor = SpectrumPrecursor.buildSpectrumPrecursor(); // changed slewis to support overrides
							spectrumPrecursor.setMassToChargeRatio(massToChargeRatio.doubleValue());
							spectrumPrecursor.setIntensity(intensity.doubleValue());
							spectrumPrecursor.setCharge(chargeState);
							List<SpectrumPrecursor> precursors = new ArrayList<SpectrumPrecursor>();
							precursors.add(spectrumPrecursor);
							getSpectrum().setPrecursors(precursors);
						}
					}
				}
				else if (inTargetSpectrumBlock && line.matches("^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*"))
				{
					// Line with 2 columns (float, float)
					/*
					 * Collect data for peak.
					 */
					StringTokenizer st = new StringTokenizer(line);
					double peakMass = Double.valueOf(st.nextToken());
					double peakIntensity = Double.valueOf(st.nextToken());
					//log.debug("Spectra line (" + lineNo + ") = \"" + line + "\", peakMass = " + peakMass + " peakIntensity = " + peakIntensity);
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
				else
				{
					/*
					 * Other line
					 */
					//log.debug("Unidentified line (" + lineNo + ") = \"" + line + "\"");
				}
				lineNo++;
			}
			/*
			 * Check if data for peak list has been collected.
			 */
			if (numberOfSpectra > 0)
			{
				/*
				 * Save peak data for previous peak list.
				 */
				if (inTargetSpectrumBlock)
				{
					/*
					 * Add retrieved spectrum data to all elements
					 * in result list corresponding to current spectrum ids.
					 */
					for (int i = 0; i < getSpectrumIdsTarget().size(); i++) {
						if (getCurrentSpectrumId().equals(getSpectrumIdsTarget().get(i)))
						{
							// Convert spectrum data arrays
							if (dataLength > 0)
							{
								double peakMassArray[] = new double[dataLength];
								double peakIntensityArray[] = new double[dataLength];
								for (int k = 0; k < dataLength; k++)
								{
									peakMassArray[k] = peakMassData.get(k).doubleValue();
									peakIntensityArray[k] = peakIntensityData.get(k).doubleValue();
								}
								getSpectrum().setMass(peakMassArray);
								getSpectrum().setIntensities(peakIntensityArray);
							}
							spectrumArray[i] = getSpectrum();
							spectrumTagsFound++;
							log.debug("SpectrumId (" + i + ") = \"" + getCurrentSpectrumId() + "\" processed, spectrumTagsFound = " + spectrumTagsFound);
							log.debug("-----------------------------------------------");
						}
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
			/*
			 * Return result of search for PKL spectra.
			 */
			List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>(nSpectra);
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
	 * Fetch spectrum id values from input stream.
	 * Since a PKL file does not specify explicit
	 * spectrum id values, a spectrum id is here
	 * identified with the ordering number for a
	 * spectra in the PKL file, starting with 1 for
	 * the first spectra in the file. 
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
		 * Process spectra in PKL file
		 */
		int numberOfSpectra = 0;
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
				// Line with 3 columns (float, float, digit)
				if (line.matches("^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*[ \\t]\\d"))
				{
					// New spectrum
					numberOfSpectra++;
					// Use spectra order number as spectrum id value
					String currentSpectrumIdStr = Integer.toString(numberOfSpectra);
					getSpectrumIdsFound().add(currentSpectrumIdStr);
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
	 * Validate the entire PKL file.
	 * 
	 * @param instream input stream to validate.
	 * @return true if valid
	 */
	private boolean valid(InputStream instream)
			throws BaseException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(instream));
		String line = new String();
		boolean newspectrum = false;
		try
		{
			while ((line = in.readLine()) != null)
			{
				// Line with 3 columns (float, float, digit)
				if ((Pattern.matches("^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*[ \\t]\\d",
					line)))
				{
					newspectrum = false;
					while (!newspectrum)
					{
						if ((line = in.readLine()) != null)
						{
							if (line.length() > 0)
							{
								// Line with 2 columns (float, float)
								if (!Pattern.matches(
									"^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*", line))
								{
									return false;
								}
								// If m/z has 9 or more digits(+1 decimal sign),
								// annotate as
								// double precision
								else if (!mz_double_precision && Pattern
									.matches("^[\\d\\.]{10,}[ \\t]\\d+\\.?\\d*",
										line))
								{
									mz_double_precision = true;
									log.info("Double precision = true");
								}
							}
							else
							{
								newspectrum = true;
							}
						}
						else
						{
							newspectrum = true;
						}
					}
				}
				else if (line.length() > 0)
				{
					return false;
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
	 * Check if the input stream has PKL start line, and thus is importable.
	 * 
	 * @param in InputStream input stream to check for PKL start line.
	 * @return true if input stream contains PKL start line, else false.
	 */
	private final boolean isImportable(InputStream in)
	{
		boolean ok = false;
		try
		{
			BufferedReader inreader = new BufferedReader(new InputStreamReader(
				in));
			ok = false;
			String line = new String();
			if ((line = inreader.readLine()) != null)
			{
				// Line with 3 columns (float, float, digit)
				ok = Pattern.matches("^\\d+\\.\\d*[ \\t]\\d+\\.?\\d*[ \\t]\\d",
					line);
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
		log.debug("spectrumIds.size() = " + spectrumIds.size() + ", spectrumList.size() = " + spectrumList.size());

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
	 * Get spectrum id list.
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
	 * Specifies if the spectrum id values were obtained
	 * from the spectrum order numbers.
	 * 
	 * @return boolean True if the spectrum id values were obtained from spectrum order numbers, else false.
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
	 * Validates PKL input stream.
	 * 
	 * @return True if the PKL input stream is valid, else false
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
