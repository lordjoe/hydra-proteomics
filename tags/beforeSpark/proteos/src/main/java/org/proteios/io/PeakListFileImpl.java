/*
 $Id: PeakListFileImpl.java 3274 2009-05-14 07:17:34Z olle $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

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

import org.xml.sax.SAXException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class implements the PeakListFileInterface that defines methods to
 * access spectra from an mzData file.
 * 
 * @author olle
 */
public class PeakListFileImpl
		implements PeakListFileInterface, FileValidationInterface,
		SpectrumIdReaderInterface, SpectrumFileContactReaderInterface,
		SpectrumFileInstrumentReaderInterface
{
	/*
	 * Logger used. Used to log specific events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");
	/*
	 * Accession number constants to use when parsing mzML XML file.
	 */
	private final String ACC_TIME_IN_SECONDS = "PSI:1000039";
	private final String ACC_ACTIVATION_METHOD = "PSI:1000044";
	/*
	 * XML Schema Definition file for mzData. Used to validate mzData xml
	 * documents.
	 */
	private String xsdFilePath = new String("xsd/mzdata.xsd");
	/*
	 * Input stream for mzData.
	 */
	private InputStream xmlInputStream;
	/*
	 * Temporary storage of parsed data
	 */
	private StringBuffer tempStrBuf;
	/*
	 * Peak List Data (in 'data' element)
	 */
	private String precision = new String("");
	private String endian = new String("");
	private int dataLength = 0;
	private List<Double> decodedBase64List;
	/*
	 * List of mzData spectrum ids to search for.
	 */
	private List<String> spectrumIdsTarget;
	/*
	 * All spectra in the file
	 */
	private List<String> allSpectrumIds = new ArrayList<String>();
	/*
	 * Current mzData spectrum id.
	 */
	private String currentSpectrumId = new String("");
	/*
	 * Spectrum data retrieved from the mzData XML input.
	 */
	private SpectrumImpl spectrum;
	/*
	 * Spectrum list data as array
	 */
	private SpectrumImpl spectrumArray[];
	/*
	 * Spectrum file contact data
	 */
	private SpectrumFileContact spectrumContactData;
	/*
	 * Spectrum file instrument data
	 */
	private SpectrumFileInstrumentInterface spectrumInstrumentData;
	/*
	 * Spectrum file instrument list
	 */
	private List<SpectrumFileInstrumentInterface> spectrumInstrumentList;
	/*
	 * Variables related to search for mzData XML tags.
	 */
	private int level = 0;
	private int spectrumIndex = -1;
	private int spectrumTagsFound = 0;
	/*
	 * Flags indicating when parser is inside target XML blocks.
	 */
	private boolean inTargetSpectrumBlock = false;
	private boolean inTargetSpectrumInstrumentBlock = false;
	private boolean inTargetPrecursorBlock = false;
	private boolean inTargetMzArrayBinaryBlock = false;
	private boolean inTargetIntenArrayBinaryBlock = false;
	private boolean inTargetDataBlock = false;
	private boolean inTargetContactBlock = false;
	private boolean contactEndTagFound = false;
	private boolean inTargetInstrumentBlock = false;
	private boolean instrumentEndTagFound = false;
	private boolean inTargetInstrumentSourceBlock = false;
	private boolean inTargetInstrumentAnalyzerListBlock = false;
	private boolean inTargetInstrumentAnalyzerBlock = false;
	private boolean inTargetInstrumentDetectorBlock = false;
	private boolean inTargetInstrumentAdditionalBlock = false;
	private boolean moreThanOnePossiblePrecursorChargeState = false;


	/**
	 * Default constructor.
	 */
	public PeakListFileImpl()
	{}


	/**
	 * Constructor that takes an mzData XML input stream as parameter.
	 * 
	 * @param xmlInputStream InputStream mzData XML input stream.
	 */
	public PeakListFileImpl(InputStream xmlInputStream)
	{
		setXMLInputStream(xmlInputStream);
	}


	/**
	 * Get mzData XML input stream.
	 * 
	 * @return xmlFilePath InputStream mzData XML input stream.
	 */
	public InputStream getXMLInputStream()
	{
		return this.xmlInputStream;
	}


	/**
	 * Set mzData XML input stream.
	 * 
	 * @param xmlInputStream InputStream mzData XML input stream.
	 */
	public void setXMLInputStream(InputStream xmlInputStream)
	{
		this.xmlInputStream = xmlInputStream;
	}


	/**
	 * Get XSD schema file to validate against.
	 * 
	 * @return xsdFilePath String with path for XSD schema file.
	 */
	public String getXsdFilePath()
	{
		return this.xsdFilePath;
	}


	/**
	 * Set XSD schema file to validate against.
	 * 
	 * @param xsdFilePath String with path for XSD schema file.
	 */
	public void setXsdFilePath(String xsdFilePath)
	{
		this.xsdFilePath = xsdFilePath;
	}


	/**
	 * Get the StringBuffer tempStrBuf value.
	 * 
	 * @return tempStrBuf StringBuffer tempStrBuf with temporary storage of
	 *         parsed data.
	 */
	private StringBuffer getTempStrBuf()
	{
		return this.tempStrBuf;
	}


	/**
	 * Set the StringBuffer tempStrBuf value.
	 * 
	 * @param tempStrBuf StringBuffer tempStrBuf value to set.
	 */
	private void setTempStrBuf(StringBuffer tempStrBuf)
	{
		this.tempStrBuf = tempStrBuf;
	}


	/**
	 * Get the precision.
	 * 
	 * @return precision String the precision
	 */
	private String getPrecision()
	{
		return this.precision;
	}


	/**
	 * Set the precision.
	 * 
	 * @param precision String the precision to set.
	 */
	private void setPrecision(String precision)
	{
		this.precision = precision;
	}


	/**
	 * Get the endian.
	 * 
	 * @return endian String the endian
	 */
	private String getEndian()
	{
		return this.endian;
	}


	/**
	 * Set the endian.
	 * 
	 * @param endian String the endian to set.
	 */
	private void setEndian(String endian)
	{
		this.endian = endian;
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
	 * Get the doublePrecision.
	 * 
	 * @return doublePrecision boolean doublePrecision based on String precision
	 */
	private boolean isDoublePrecision()
	{
		/*
		 * Set value of boolean variable 'doublePrecision': precision = "32" :
		 * doublePrecision = false precision = "64" : doublePrecision = true
		 */
		boolean doublePrecision = true;
		if (getPrecision().equals("64"))
		{
			doublePrecision = true;
		}
		else if (getPrecision().equals("32"))
		{
			doublePrecision = false;
		}
		return doublePrecision;
	}


	/**
	 * Get the bigEndian.
	 * 
	 * @return bigEndian boolean bigEndian based on String endian
	 */
	private boolean isBigEndian()
	{
		/*
		 * Set value of boolean variable 'bigEndian': endian = "little" :
		 * bigEndian = false endian = "big" : bigEndian = true
		 */
		boolean bigEndian = true;
		if (getEndian().equals("big"))
		{
			bigEndian = true;
		}
		else if (getEndian().equals("little"))
		{
			bigEndian = false;
		}
		return bigEndian;
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
	 * Get the last precursor for the current spectrum. If there is none, one
	 * is generated
	 * 
	 * @return SpectrumPrecursor A spectrum precursor
	 */
	private SpectrumPrecursor getSpectrumPrecursor()
	{
		SpectrumPrecursor prec = null;
		if (spectrum != null)
		{
			List<SpectrumPrecursor> precs = spectrum.getPrecursors();
			if (precs == null || precs.isEmpty())
			{
				spectrum.addPrecursor(SpectrumPrecursor.buildSpectrumPrecursor()); // changed slewis to support overrides
			}
			int nPrecursors = spectrum.getPrecursors().size();
			prec = spectrum.getPrecursors().get(nPrecursors - 1);
		}
		return prec;
	}


	/**
	 * Get spectrum file contact data. If there is none, one
	 * is generated.
	 * 
	 * @return SpectrumContact The spectrum contact data.
	 */
	private SpectrumFileContact getSpectrumContactData()
	{
		if (spectrumContactData == null)
		{
			spectrumContactData = new SpectrumFileContact();
		}
		return spectrumContactData;
	}


	/**
	 * Get spectrum file instrument data. If there is none, one
	 * is generated.
	 * 
	 * @return SpectrumFileInstrumentInterface The spectrum file instrument data.
	 */
	private SpectrumFileInstrumentInterface getSpectrumInstrumentData()
	{
		if (spectrumInstrumentData == null)
		{
			spectrumInstrumentData = new SpectrumFileInstrument();
		}
		return spectrumInstrumentData;
	}


	/**
	 * Get spectrum file instrument list. If there is none, one
	 * is generated.
	 * 
	 * @return List<SpectrumFileInstrumentInterface> The spectrum file instrument list.
	 */
	private List<SpectrumFileInstrumentInterface> getSpectrumInstrumentList()
	{
		if (spectrumInstrumentList == null)
		{
			spectrumInstrumentList = new ArrayList<SpectrumFileInstrumentInterface>();
		}
		return spectrumInstrumentList;
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
	 * Validates mzData XML input stream against current mzData XSD file.
	 * 
	 * @return True if the mzData XML input stream is valid, else false
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public boolean valid()
			throws XMLStreamException, SAXException
	{
		boolean retVal = XMLValidator.validate(this.xsdFilePath,
			getXMLInputStream());
		return retVal;
	}


	/**
	 * Validates first element of input stream.
	 * 
	 * @return True if the input stream is valid, else false
	 */
	public boolean importable()
	{
		InputStream iStream = getXMLInputStream();
		boolean importable = false;
		/*
		 * Use the reference implementation for the XML input factory
		 */
		//System.setProperty("javax.xml.stream.XMLInputFactory",
		//	"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			/*
			 * Parse to the first XML start event
			 */
			boolean done = false;
			while (parser.hasNext() && done != true)
			{
				if (parser.next() == XMLStreamConstants.START_ELEMENT)
				{
					done = true;
					if (parser.getLocalName().equals("mzData"))
					{
						importable = true;
					}
					else
					{
						log
							.warn("PeakListFileImpl: Start element local name=" + parser
								.getLocalName());
					}
				}
			}
		}
		catch (Exception e)
		{
			log.warn("PeakListFileImpl: Exception at importable():" + e);
		}
		return importable;
	}


	/**
	 * Fetch spectrum file contact info from input stream.
	 * 
	 * @return SpectrumContact Spectrum file contact data.
	 */
	private SpectrumFileContact fetchSpectrumFileContactInfo()
	{
		/*
		 * Get XML input stream.
		 */
		InputStream iStream = getXMLInputStream();
		/*
		 * Use the reference implementation for the XML input factory
		 */
		//System.setProperty("javax.xml.stream.XMLInputFactory",
		//	"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			/*
			 * Parse the XML input stream using cursor-based XML parsing. Parse
			 * the document until end of document, or until the contact
			 * mzData XML tag is found.
			 */
			contactEndTagFound = false;
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT && (!contactEndTagFound); event = parser
				.next())
			{
				/*
				 * Process current event
				 */
				processEvent(event, parser);
			}
			/*
			 * Close parser.
			 */
			parser.close();
			/*
			 * Return result of search for mzData XML contact tag.
			 */
			return spectrumContactData;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzData contact data failed.
			 */
			return null;
		}
	}


	/**
	 * Fetch spectrum file instrument info from input stream.
	 * 
	 * @return List<SpectrumFileInstrumentInfo> Spectrum file instrument list.
	 */
	private List<SpectrumFileInstrumentInterface> fetchSpectrumFileInstrumentInfo()
	{
		/*
		 * Get XML input stream.
		 */
		InputStream iStream = getXMLInputStream();
		/*
		 * Use the reference implementation for the XML input factory
		 */
		//System.setProperty("javax.xml.stream.XMLInputFactory",
		//	"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			/*
			 * Parse the XML input stream using cursor-based XML parsing. Parse
			 * the document until end of document, or until the instrument
			 * mzData XML tag is found.
			 */
			instrumentEndTagFound = false;
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT && (!instrumentEndTagFound); event = parser
				.next())
			{
				/*
				 * Process current event
				 */
				processEvent(event, parser);
			}
			/*
			 * Close parser.
			 */
			parser.close();
			/*
			 * Return result of search for mzData XML instrument tag.
			 */
			return spectrumInstrumentList;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzData instrument data failed.
			 */
			return null;
		}
	}


	/**
	 * Fetch spectra with specified mzData spectrum ids from input stream.
	 * 
	 * @param spectrumIds List<String> list with mzData spectrum ids for
	 *        desired spectra.
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
		InputStream iStream = getXMLInputStream();
		/*
		 * Use the reference implementation for the XML input factory
		 */
		//System.setProperty("javax.xml.stream.XMLInputFactory",
		//	"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			/*
			 * Parse the XML input stream using cursor-based XML parsing. Parse
			 * the document until end of document, or until the desired spectrum
			 * mzData XML tag is found.
			 */
			spectrumTagsFound = 0;
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT && (spectrumTagsFound < nSpectra); event = parser
				.next())
			{
				/*
				 * Process current event
				 */
				processEvent(event, parser);
			}
			/*
			 * Close parser.
			 */
			parser.close();
			/*
			 * Return result of search for mzData XML spectrum tag.
			 */
			List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>(
				nSpectra);
			for (int i = 0; i < nSpectra; i++)
			{
				spectrumList.add(i, spectrumArray[i]);
			}
			return spectrumList;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzData spectrum data failed.
			 */
			return null;
		}
	}


	/**
	 * Processes an event.
	 * 
	 * @param event int current event.
	 * @param parser XMLStreamReader instance.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	private void processEvent(int event, XMLStreamReader parser)
			throws XMLStreamException
	{
		/*
		 * Process event depending on type
		 */
		switch (event)
		{
			case XMLStreamConstants.START_ELEMENT:
				processStartElement(parser);
				break;
			case XMLStreamConstants.END_ELEMENT:
				processEndElement(parser);
				/*
				 * Create StringBuffer for temporary storage if not existing.
				 * Empty character data temporary string buffer
				 */
				if (tempStrBuf == null)
				{
					tempStrBuf = new StringBuffer();
				}
				tempStrBuf.setLength(0);
				break;
			case XMLStreamConstants.CHARACTERS:
				processCharacters(parser);
				break;
			case XMLStreamConstants.CDATA:
				break;
		} // end switch
	}


	/**
	 * Processes a StartElement event.
	 * 
	 * @param parser XMLStreamReader instance.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	private void processStartElement(XMLStreamReader parser)
			throws XMLStreamException
	{
		/*
		 * Get element name
		 */
		if (!parser.hasName())
		{
			return;
		}
		String localName = parser.getLocalName();
		/*
		 * Process item based on tag name.
		 */
		// log.debug("PeakListFileImpl::processStartElement():
		// parser.getLocalName() = \"" + localName + "\"");
		/*
		 * Search for mzData "contact" XML block.
		 */
		if (localName.equals("contact"))
		{
			inTargetContactBlock = true;
		}
		/*
		 * Search for mzData "instrument" XML block.
		 */
		else if (localName.equals("instrument"))
		{
			inTargetInstrumentBlock = true;
		}
		else if (localName.equals("source"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentSourceBlock = true;
			}
		}
		else if (localName.equals("analyzerList"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentAnalyzerListBlock = true;
			}
		}
		else if (localName.equals("analyzer"))
		{
			if (inTargetInstrumentAnalyzerListBlock)
			{
				inTargetInstrumentAnalyzerBlock = true;
				SpectrumFileAnalyzerInterface spectrumFileAnalyzer = new SpectrumFileAnalyzer();
				getSpectrumInstrumentData().getAnalyzers().add(spectrumFileAnalyzer);
			}
		}
		else if (localName.equals("detector"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentDetectorBlock = true;
			}
		}
		else if (localName.equals("additional"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentAdditionalBlock = true;
			}
		}
		/*
		 * Search for desired mzData "spectrum" XML block.
		 */
		if (localName.equals("spectrum"))
		{
			/*
			 * Get attributes
			 */
			String attrValue = XMLImportUtil.seekAttribute("id", parser);
			if (attrValue != null)
			{
				String currentSpectrumIdStr = attrValue;
				allSpectrumIds.add(currentSpectrumIdStr);
				/*
				 * Check if desired spectrum id found.
				 */
				spectrumIndex = -1;
				for (int i = 0; i < getSpectrumIdsTarget().size(); i++)
				{
					if (currentSpectrumIdStr.equals(getSpectrumIdsTarget().get(
						i)))
					{
						/*
						 * Save information in order to get spectrum mass and
						 * intensity values.
						 */
						setCurrentSpectrumId(currentSpectrumIdStr);
						inTargetSpectrumBlock = true;
						spectrumIndex = i;
						/*
						 * Get new spectrum object to store retrieved data in.
						 */
						SpectrumImpl currentSpectrum = SpectrumImpl.buildSpectrum();  // changed slewis to allow class to change
						setSpectrum(currentSpectrum);
						log
							.debug("PeakListFileImpl::processStartElement(): SpectrumId (" + spectrumIndex + ") = \"" + getCurrentSpectrumId() + "\" found.");
					}
				}
			}
		}
		else if (localName.equals("spectrumInstrument"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetSpectrumInstrumentBlock = true;
				log
					.debug("PeakListFileImpl::processStartElement(): spectrumInstrument block found");
			}
		}
		else if (localName.equals("precursor"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetPrecursorBlock = true;
				moreThanOnePossiblePrecursorChargeState = false;
				getSpectrum().addPrecursor(SpectrumPrecursor.buildSpectrumPrecursor()); // changed slewis to support overrides
				log
					.debug("PeakListFileImpl::processStartElement(): precursor block found");
			}
		}
		else if (localName.equals("cvParam"))
		{
			if (inTargetInstrumentBlock)
			{
				/*
				 * Get attributes
				 */
				String currentCvParamNameStr = XMLImportUtil.seekAttribute("name", parser);
				String currentCvParamValueStr = XMLImportUtil.seekAttribute("value", parser);
				if (currentCvParamNameStr != null)
				{
					StringPair stringPair = new StringPair();
					stringPair.setName(currentCvParamNameStr);
					stringPair.setValue(currentCvParamValueStr);
					// Add cvParam data
					if (inTargetInstrumentSourceBlock)
					{
						getSpectrumInstrumentData().getSource().add(stringPair);
					}
					if (inTargetInstrumentAnalyzerBlock)
					{
						List<SpectrumFileAnalyzerInterface> analyzerList =
							getSpectrumInstrumentData().getAnalyzers();
						int listSize = 0;
						if (analyzerList != null)
						{
							listSize = analyzerList.size();
						}
						if (listSize > 0)
						{
							// Add analyzer data to last list entry
							analyzerList.get(listSize-1).getAnalyzer().add(stringPair);
						}
					}
					if (inTargetInstrumentDetectorBlock)
					{
						getSpectrumInstrumentData().getDetector().add(stringPair);
					}
					if (inTargetInstrumentAdditionalBlock)
					{
						getSpectrumInstrumentData().getAdditional().add(stringPair);
					}
				}
			}
			else if (inTargetSpectrumInstrumentBlock || inTargetSpectrumBlock)
			{
				/*
				 * Get attributes
				 */
				String attrValue = XMLImportUtil.seekAttribute("name", parser);
				if (attrValue != null)
				{
					String currentCvParamNameStr = attrValue;
					String currentCvParamAcc = XMLImportUtil.seekAttribute(
						"accession", parser);
					/*
					 * Check if desired cvParam found.
					 */
					/* Store first retention time found */
					if (getSpectrum().getRetentionTimeInMinutes() == null && (currentCvParamNameStr
						.equals("TimeInMinutes") || currentCvParamAcc
						.equals(ACC_TIME_IN_SECONDS)))
					{
						String retentionTimeStr = null;
						retentionTimeStr = XMLImportUtil.seekAttribute("value",
							parser);
						if (retentionTimeStr != null && !retentionTimeStr
							.equals(""))
						{
							Double retentionTime = Double
								.valueOf(retentionTimeStr);
							if (currentCvParamAcc.equals(ACC_TIME_IN_SECONDS))
							{
								retentionTime = (retentionTime.doubleValue() / 60d);
							}
							/*
							 * Add retention time to result spectrum.
							 */
							getSpectrum().setRetentionTimeInMinutes(
								retentionTime);
							log
								.debug("PeakListFileImpl::processStartElement(): cvParam block with retentionTimeInMinutes found, time = " + retentionTime);
						}
					}
					else if (currentCvParamNameStr.equals("MassToChargeRatio"))
					{
						String pepmass = XMLImportUtil.seekAttribute("value",
							parser);
						if (pepmass != null)
						{
							getSpectrumPrecursor().setMassToChargeRatio(
								Double.valueOf(pepmass));
						}
					}
					else if (currentCvParamNameStr.equals("ChargeState"))
					{
						String charge = XMLImportUtil.seekAttribute("value",
							parser);
						if (charge != null)
						{
							// There can be more than one charge state assigned.
							if (spectrum.getPrecursors() != null && !spectrum
								.getPrecursors().isEmpty())
							{
								if (getSpectrumPrecursor().getCharge() != null)
								{
									// More than one possible charge state for precursor
									moreThanOnePossiblePrecursorChargeState = true;
									// Re-classify previous charge state as possible charge state
									String possibleChargeStateStr = new String("possible charge state");
									String possibleChargestateVal = getSpectrumPrecursor().getCharge().toString();
									StringPair stringPair = new StringPair();
									stringPair.setName(possibleChargeStateStr);
									stringPair.setValue(possibleChargestateVal);
									// Add cvParam data
									if (inTargetSpectrumBlock)
									{
										if (inTargetPrecursorBlock)
										{
											getSpectrumPrecursor().addExtraData(stringPair);
										}
									}
									// Reset precursor charge state
									getSpectrumPrecursor().setCharge(null);
								}
							}
							// Save charge state value
							if (!moreThanOnePossiblePrecursorChargeState)
							{
								// Add charge state
								getSpectrumPrecursor().setCharge(
									Integer.valueOf(charge));
							}
							else
							{
								// Add new possible charge state
								String possibleChargeStateStr = new String("possible charge state");
								String possibleChargestateVal = charge;
								StringPair stringPair = new StringPair();
								stringPair.setName(possibleChargeStateStr);
								stringPair.setValue(possibleChargestateVal);
								// Add cvParam data
								if (inTargetSpectrumBlock)
								{
									if (inTargetPrecursorBlock)
									{
										getSpectrumPrecursor().addExtraData(stringPair);
									}
								}
							}
						}
					}
					else if (currentCvParamNameStr.equals("Intensity"))
					{
						String intensity = XMLImportUtil.seekAttribute("value",
							parser);
						if (intensity != null)
						{
							getSpectrumPrecursor().setIntensity(
								Double.valueOf(intensity));
						}
					}
					else
					{
						String currentCvParamValueStr = XMLImportUtil.seekAttribute("value",
							parser);
						if (currentCvParamValueStr != null)
						{
							/*
							 * Check for precursor fragmentation type data
							 */
							if (currentCvParamAcc.equals(ACC_ACTIVATION_METHOD))
							{
								if (inTargetPrecursorBlock)
								{
									// Get fragmentation type from cvParam value string
									SpectrumPrecursor.FragmentationType fragmentationType =
										SpectrumPrecursor.FragmentationType.fromAbbreviation(currentCvParamValueStr);
									if (fragmentationType != null)
									{
										getSpectrumPrecursor().setFragmentationType(fragmentationType);
									}
								}								
							}
							/*
							 * Extra data
							 */
							StringPair stringPair = new StringPair();
							stringPair.setName(currentCvParamNameStr);
							stringPair.setValue(currentCvParamValueStr);
							// Add cvParam data
							if (inTargetSpectrumBlock)
							{
								if (inTargetPrecursorBlock)
								{
									getSpectrumPrecursor().addExtraData(stringPair);
								}
								else
								{
									getSpectrum().addExtraData(stringPair);
								}
							}
						}
					}
				}
			}
		}
		else if (localName.equals("userParam"))
		{
			if (inTargetInstrumentBlock)
			{
				/*
				 * Get attributes
				 */
				String currentCvParamNameStr = XMLImportUtil.seekAttribute("name", parser);
				String currentCvParamValueStr = XMLImportUtil.seekAttribute("value", parser);
				if (currentCvParamNameStr != null)
				{
					StringPair stringPair = new StringPair();
					stringPair.setName(currentCvParamNameStr);
					stringPair.setValue(currentCvParamValueStr);
					// Add cvParam data
					if (inTargetInstrumentSourceBlock)
					{
						getSpectrumInstrumentData().getSource().add(stringPair);
					}
					if (inTargetInstrumentAnalyzerBlock)
					{
						List<SpectrumFileAnalyzerInterface> analyzerList =
							getSpectrumInstrumentData().getAnalyzers();
						int listSize = 0;
						if (analyzerList != null)
						{
							listSize = analyzerList.size();
						}
						if (listSize > 0)
						{
							// Add analyzer data to last list entry
							analyzerList.get(listSize-1).getAnalyzer().add(stringPair);
						}
					}
					if (inTargetInstrumentDetectorBlock)
					{
						getSpectrumInstrumentData().getDetector().add(stringPair);
					}
					if (inTargetInstrumentAdditionalBlock)
					{
						getSpectrumInstrumentData().getAdditional().add(stringPair);
					}
				}
			}
		}
		else if (localName.equals("mzArrayBinary"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetMzArrayBinaryBlock = true;
				log
					.debug("PeakListFileImpl::processStartElement(): mzArrayBinary block found");
			}
		}
		else if (localName.equals("intenArrayBinary"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetIntenArrayBinaryBlock = true;
				log
					.debug("PeakListFileImpl::processStartElement(): intenArrayBinary block found");
			}
		}
		else if (localName.equals("data"))
		{
			if (inTargetMzArrayBinaryBlock || inTargetIntenArrayBinaryBlock)
			{
				inTargetDataBlock = true;
				log
					.debug("PeakListFileImpl::processStartElement(): data block found");
				/*
				 * Get spectrum data attributes
				 */
				String precision = new String("");
				String endian = new String("");
				int dataLength = 0;
				/*
				 * Get attributes
				 */
				String attrValue;
				attrValue = XMLImportUtil.seekAttribute("precision", parser);
				if (attrValue != null)
				{
					precision = attrValue;
				}
				attrValue = XMLImportUtil.seekAttribute("endian", parser);
				if (attrValue != null)
				{
					endian = attrValue;
				}
				attrValue = XMLImportUtil.seekAttribute("length", parser);
				if (attrValue != null)
				{
					// dataLength = Integer.parseInt(attrValue);
					dataLength = Integer.valueOf(attrValue).intValue();
				}
				/*
				 * Store data attribute values for later use.
				 */
				setPrecision(precision);
				setEndian(endian);
				setDataLength(dataLength);
				log
					.debug("PeakListFileImpl:: processStartElement(): precision: \"" + precision + "\" endian: \"" + endian + "\" dataLength: " + dataLength);
			}
		}
		/*
		 * Update level counter.
		 */
		level++;
	}


	/**
	 * Processes an EndElement event.
	 * 
	 * @param parser XMLStreamReader instance.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	private void processEndElement(XMLStreamReader parser)
			throws XMLStreamException
	{
		/*
		 * Update level counter.
		 */
		level--;
		/*
		 * Get element name
		 */
		if (!parser.hasName())
		{
			return;
		}
		String localName = parser.getLocalName();
		// Search for contact data
		if (localName.equals("name"))
		{
			if (inTargetContactBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String name = new String("");
				if (getTempStrBuf() != null)
				{
					name = getTempStrBuf().toString();
				}
				getSpectrumContactData().setName(name);
			}
		}
		else if (localName.equals("institution"))
		{
			if (inTargetContactBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String institution = new String("");
				if (getTempStrBuf() != null)
				{
					institution = getTempStrBuf().toString();
				}
				getSpectrumContactData().setInstitution(institution);
			}
		}
		else if (localName.equals("contactInfo"))
		{
			if (inTargetContactBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String contactInfo = new String("");
				if (getTempStrBuf() != null)
				{
					contactInfo = getTempStrBuf().toString();
				}
				getSpectrumContactData().setContactInfo(contactInfo);
			}
		}
		else if (localName.equals("contact"))
		{
			inTargetContactBlock = false;
			contactEndTagFound = true;
		}
		// Search for instrument data
		else if (localName.equals("instrumentName"))
		{
			if (inTargetInstrumentBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String name = new String("");
				if (getTempStrBuf() != null)
				{
					name = getTempStrBuf().toString();
				}
				getSpectrumInstrumentData().setInstrumentName(name);
			}
		}
		else if (localName.equals("source"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentSourceBlock = false;
			}
		}
		else if (localName.equals("analyzer"))
		{
			if (inTargetInstrumentAnalyzerListBlock)
			{
				inTargetInstrumentAnalyzerBlock = false;
			}
		}
		else if (localName.equals("analyzerList"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentAnalyzerListBlock = false;
			}
		}
		else if (localName.equals("detector"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentDetectorBlock = false;
			}
		}
		else if (localName.equals("additional"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentAdditionalBlock = false;
			}
		}
		else if (localName.equals("instrument"))
		{
			inTargetInstrumentBlock = false;
			// Add current instrument information to list
			getSpectrumInstrumentList().add(getSpectrumInstrumentData());
			instrumentEndTagFound = true;
		}
		// Search for spectrum data
		if (localName.equals("spectrum"))
		{
			if (inTargetSpectrumBlock)
			{
				/*
				 * Add retrieved spectrum data to all elements in result list
				 * corresponding to current spectrum ids.
				 */
				for (int i = 0; i < getSpectrumIdsTarget().size(); i++)
				{
					if (getCurrentSpectrumId().equals(
						getSpectrumIdsTarget().get(i)))
					{
						spectrumArray[i] = getSpectrum();
						spectrumTagsFound++;
						log
							.debug("PeakListFileImpl::processEndElement(): SpectrumId (" + i + ") = \"" + getCurrentSpectrumId() + "\" processed, spectrumTagsFound = " + spectrumTagsFound);
						log
							.debug("PeakListFileImpl::processEndElement(): -----------------------------------------------");
					}
				}
			}
			inTargetSpectrumBlock = false;
		}
		else if (localName.equals("spectrumInstrument"))
		{
			inTargetSpectrumInstrumentBlock = false;
		}
		else if (localName.equals("precursor"))
		{
			inTargetPrecursorBlock = false;
		}
		else if (localName.equals("mzArrayBinary"))
		{
			inTargetMzArrayBinaryBlock = false;
		}
		else if (localName.equals("intenArrayBinary"))
		{
			inTargetIntenArrayBinaryBlock = false;
		}
		else if (localName.equals("data"))
		{
			if (inTargetDataBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String dataBase64 = new String("");
				if (getTempStrBuf() != null)
				{
					dataBase64 = getTempStrBuf().toString();
				}
				/*
				 * Get Base64 coding parameters.
				 */
				boolean doublePrecision = isDoublePrecision();
				boolean bigEndian = isBigEndian();
				/*
				 * Process element.
				 */
				decodedBase64List = dataItem(doublePrecision, bigEndian,
					dataBase64);
				/*
				 * Convert to double[] array.
				 */
				int nPeaks = getDataLength();
				double[] dataArray = new double[nPeaks];
				for (int i = 0; i < nPeaks; i++)
				{
					dataArray[i] = decodedBase64List.get(i);
				}
				/*
				 * Add data list to result spectrum.
				 */
				if (inTargetMzArrayBinaryBlock)
				{
					getSpectrum().setMass(dataArray);
				}
				else if (inTargetIntenArrayBinaryBlock)
				{
					getSpectrum().setIntensities(dataArray);
				}
				/*
				 * Reset data attribute values.
				 */
				setPrecision("");
				setEndian("");
				setDataLength(0);
			}
			inTargetDataBlock = false;
		}
	}


	/**
	 * Processes a Characters event.
	 * 
	 * @param parser XMLStreamReader instance.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	private void processCharacters(XMLStreamReader parser)
			throws XMLStreamException
	{
		/*
		 * Create StringBuffer for temporary storage if not existing. Store
		 * character data in temporary buffer. If data starts with newline
		 * character, no data is expected before a new element, so the string
		 * buffer is emptied in this case.
		 */
		if (tempStrBuf == null)
		{
			tempStrBuf = new StringBuffer();
		}
		tempStrBuf.append(parser.getText());
		if (tempStrBuf.charAt(0) == '\n')
		{
			tempStrBuf.setLength(0);
		}
	}


	/**
	 * Creates a data item.
	 * 
	 * @param doublePrecision boolean precision = 64: true, precision = 32:
	 *        false
	 * @param bigEndian boolean big endian: true; little endian: false
	 * @param dataBase64Raw String with Base64-coded data block
	 * @return decodedBase64 List with decoded data
	 */
	private List<Double> dataItem(boolean doublePrecision, boolean bigEndian,
			String dataBase64Raw)
	{
		/*
		 * New data item
		 */
		/*
		 * Remove any line break characters from the base64-encoded block.
		 */
		StringBuffer dataBase64RawStrBuf = new StringBuffer(dataBase64Raw);
		StringBuffer dataBase64StrBuf = new StringBuffer("");
		int nChars = 0;
		int nLines = 0;
		int lineLength = 0;
		boolean newLineFlag = true;
		if (dataBase64Raw != null)
		{
			if (dataBase64Raw.length() > 0)
			{
				for (int i = 0; i < dataBase64RawStrBuf.length(); i++)
				{
					char c = dataBase64RawStrBuf.charAt(i);
					if (c == '\r' || c == '\n')
					{
						newLineFlag = true;
					}
					else
					{
						dataBase64StrBuf.append(c);
						nChars++;
						if (newLineFlag)
						{
							nLines++;
						}
						if (nLines == 1)
						{
							lineLength++;
						}
						newLineFlag = false;
					}
				}
			}
		}
		String dataBase64 = dataBase64StrBuf.toString();
		log
			.debug(this.getClass().getName() + ":: dataItem(): nLines = " + nLines + " lineLength = " + lineLength + " nChars = " + nChars + " dataBase64Raw.length() = " + dataBase64Raw
				.length());
		/*
		 * Decode Base64 coded data and put extracted double values in list
		 * decodeBase64.
		 */
		List<Double> decodedBase64 = new ArrayList<Double>(0);
		if (dataBase64 != null)
		{
			if (dataBase64.length() > 0)
			{
				decodedBase64 = Base64Util.decode(doublePrecision, bigEndian,
					dataBase64);
			}
		}
		/*
		 * Return list with decoded data
		 */
		return decodedBase64;
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
	 * Get an iterator for spectra with specified spectrum ids.
	 * 
	 * @param spectrumIds List<String> list with spectrum ids.
	 * @return iterator for spectra with specified spectrum ids.
	 */
	public Iterator<SpectrumInterface> getSpectrum(List<String> spectrumIds)
	{
		/*
		 * Search for desired spectra.
		 */
		List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>();
		spectrumList = fetchSpectrum(spectrumIds);
		log
			.debug("PeakListFileImpl::getSpectrum(List<String> spectrumIds): spectrumIds.size() = " + spectrumIds
				.size() + ", spectrumList.size() = " + spectrumList.size());
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
	 * Get list of all mzData spectrum ids from input stream. the function will
	 * read through the entire file.
	 * 
	 * @return List<String> list of spectrum ids.
	 */
	public List<String> getSpectrumIdList()
	{
		InputStream iStream = getXMLInputStream();
		allSpectrumIds = new ArrayList<String>();
		setSpectrumIdsTarget(new ArrayList<String>(0));
		/*
		 * Use the reference implementation for the XML input factory
		 */
		//System.setProperty("javax.xml.stream.XMLInputFactory",
		//	"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			/*
			 * Parse the XML input stream using cursor-based XML parsing. Parse
			 * the document until end of document.
			 */
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser
				.next())
			{
				/*
				 * Process current event
				 */
				processEvent(event, parser);
			}
			parser.close();
			return allSpectrumIds;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzData spectrum data failed.
			 */
			return null;
		}
	}


	public boolean isSpectrumIdObtainedFromSpectrumOrderNumber()
	{
		return false;
	}


	// -------------------------------------------
	/*
	 * From the SpectrumFileContactReaderInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get the spectrum contact data.
	 * 
	 * @return SpectrumContact The spectrum contact data.
	 */
	public SpectrumFileContact getSpectrumContact()
	{
		fetchSpectrumFileContactInfo();
		return this.spectrumContactData;
	}


	// -------------------------------------------
	/*
	 * From the SpectrumFileInstrumentReaderInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Get the spectrum file instrument list.
	 * 
	 * @return List<SpectrumFileInstrumentInterface> The spectrum file instrument list.
	 */
	public List<SpectrumFileInstrumentInterface> getSpectrumFileInstrumentList()
	{
		fetchSpectrumFileInstrumentInfo();
		return this.spectrumInstrumentList;
	}
}
