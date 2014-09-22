/*
 $Id: MzMLFileReader.java 3277 2009-05-18 14:43:48Z olle $

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

import org.proteios.core.InvalidDataException;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class contains methods to access spectra from an mzML file.
 *
 *  changed  private to protected by slewis
 *
 * @author olle
 */
public class MzMLFileReader
	implements PeakListFileInterface, SpectrumIdReaderInterface, FileValidationInterface,
		SpectrumFileContactReaderInterface, SpectrumFileInstrumentReaderInterface
{
	/*
	 * Logger used. Used to log specific events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");
	/*
	 * Accession number constants to use when parsing mzML XML file.
	 */
	private final String ACC_CONTACT_NAME = "MS:1000586";
	private final String ACC_CONTACT_ADDRESS = "MS:1000587";
	private final String ACC_CONTACT_URL = "MS:1000588";
	private final String ACC_CONTACT_EMAIL = "MS:1000589";
	private final String ACC_INSTRUMENT_NAME = "MS:1000554";
	private final String ACC_INSTRUMENT_SERIAL_NO = "MS:1000529";
	private final String ACC_INSTRUMENT_MODEL = "MS:1000031";
	private final String ACC_SCAN_TIME = "MS:1000016";
	private final String ACC_UNIT_MINUTE_MS = "MS:1000038";
	private final String ACC_UNIT_SECOND_MS = "MS:1000039";
	private final String ACC_UNIT_MINUTE_UO = "UO:0000031";
	private final String ACC_UNIT_SECOND_UO = "UO:0000010";
	private final String ACC_MZ_ARRAY = "MS:1000514";
	private final String ACC_INTENSITY_ARRAY = "MS:1000515";
	private final String ACC_32_BIT_FLOAT = "MS:1000521";
	private final String ACC_64_BIT_FLOAT = "MS:1000523";
	private final String ACC_ZLIB_COMPRESSION = "MS:1000574";
	private final String ACC_NO_COMPRESSION = "MS:1000576";
	private final String ACC_SELECTED_MASS_TO_CHARGE_RATIO = "MS:1000744";
	private final String ACC_CHARGE_STATE = "MS:1000041";
	private final String ACC_INTENSITY = "MS:1000042";
	private final HashMap<String,String> accInstrumentModelHashMap = new HashMap<String,String>();
	/*
	 * XML Schema Definition file for mzML. Used to validate mzML xml
	 * documents.
	 */
	private String xsdFilePath = new String("xsd/mzML1.1.0.xsd");
	/*
	 * Input stream for mzML.
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
	private String compression = new String("");
	private String endian = new String("");
	private int dataLength = 0;
	private List<Double> decodedBase64List;
	/*
	 * List of mzML spectrum ids to search for.
	 */
	private List<String> spectrumIdsTarget;
	/*
	 * Current mzML spectrum id.
	 */
	private String currentSpectrumId = new String("");
	/*
	 * Spectrum data retrieved from the mzML XML input.
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
	 * Variables related to search for mzML XML tags.
	 */
	private int level = 0;
	private int spectrumIndex = -1;
	private int spectrumTagsFound = 0;
	/*
	 * Flags indicating when parser is inside target XML blocks.
	 */
	private boolean inTargetSpectrumBlock = false;
	private boolean inTargetSpectrumInstrumentBlock = false;
	// MzML specific
	private boolean inTargetPrecursorBlock = false;
	private boolean inTargetSelectedIonBlock = false;
	private boolean inCandidateBinaryDataArrayBlock = false;
	private boolean inMzTargetBinaryDataArrayBlock = false;
	private boolean inIntensityTargetBinaryDataArrayBlock = false;
	private boolean inTargetBinaryBlock = false;
	private boolean inTargetContactBlock = false;
	private boolean contactEndTagFound = false;
	private boolean inTargetInstrumentListBlock = false;
	private boolean instrumentListEndTagFound = false;
	private boolean inTargetInstrumentBlock = false;
	private boolean inTargetInstrumentComponentListBlock = false;
	private boolean inTargetInstrumentSourceBlock = false;
	private boolean inTargetInstrumentAnalyzerBlock = false;
	private boolean inTargetInstrumentDetectorBlock = false;
	//
	private boolean inReferenceableParamGroupBlock = false;
	private List<ReferenceableParamGroup> referenceableParamGroupList = new ArrayList<ReferenceableParamGroup>(0);


	protected class ReferenceableParamGroup
	{
		String id = new String("");
		String refParamInstrumentSerialNo = new String("");
		String refParamInstrumentModel = new String("");
		String refParamPrecision = new String("");
		String refParamCompression = new String("");
		String refParamArrayType = new String("");
	}


	/**
	 * Default constructor.
	 */
	public MzMLFileReader()
	{
		initializeInstrumentModelAccessionValues();
	}


	/**
	 * Constructor that takes an mzML XML input stream as parameter.
	 * 
	 * @param xmlInputStream InputStream mzML XML input stream.
	 */
	public MzMLFileReader(InputStream xmlInputStream)
	{
		setXMLInputStream(xmlInputStream);
		initializeInstrumentModelAccessionValues();
	}


    public boolean isInTargetSpectrumBlock() {
        return inTargetSpectrumBlock;
    }

    public void setInTargetSpectrumBlock(final boolean pInTargetSpectrumBlock) {
        inTargetSpectrumBlock = pInTargetSpectrumBlock;
    }

    /**
	 * Get mzML XML input stream.
	 * 
	 * @return xmlFilePath InputStream mzML XML input stream.
	 */
	public InputStream getXMLInputStream()
	{
		return this.xmlInputStream;
	}


	/**
	 * Set mzML XML input stream.
	 * 
	 * @param xmlInputStream InputStream mzML XML input stream.
	 */
	public void setXMLInputStream(InputStream xmlInputStream)
	{
		this.xmlInputStream = xmlInputStream;
	}


	/**
	 * Initialize list of instrument model accession values.
	 * The instrument model accession numbers and names are
	 * taken from ontology file psi-ms.obo. To support
	 * newer instrument models this data might be read
	 * directly from an ontology obo file. The accession
	 * number is used to identify a cvParam to contain
	 * instrument model data. The instrument model name
	 * should normally be obtained from the cvParam name
	 * attribute, and only if the latter is empty taken
	 * from the hash map.
	 */
	protected void initializeInstrumentModelAccessionValues()
	{
		accInstrumentModelHashMap.put("MS:1000121", "ABI / SCIEX instrument model");
		accInstrumentModelHashMap.put("MS:1000122", "Bruker Daltonics instrument model");
		accInstrumentModelHashMap.put("MS:1000123", "IonSpec instrument model");
		accInstrumentModelHashMap.put("MS:1000124", "Shimadzu instrument model");
		accInstrumentModelHashMap.put("MS:1000125", "Thermo Finnigan instrument model");
		accInstrumentModelHashMap.put("MS:1000126", "Waters instrument model");
		//
		accInstrumentModelHashMap.put("MS:1000139", "4000 Q TRAP");
		accInstrumentModelHashMap.put("MS:1000140", "4700 Proteomic Analyzer");
		accInstrumentModelHashMap.put("MS:1000141", "APEX IV");
		accInstrumentModelHashMap.put("MS:1000142", "APEX-Q");
		accInstrumentModelHashMap.put("MS:1000143", "API 150EX");
		accInstrumentModelHashMap.put("MS:1000144", "API 150EX Prep");
		accInstrumentModelHashMap.put("MS:1000145", "API 2000");
		accInstrumentModelHashMap.put("MS:1000146", "API 3000");
		accInstrumentModelHashMap.put("MS:1000147", "API 4000");
		accInstrumentModelHashMap.put("MS:1000148", "autoFlex II");
		accInstrumentModelHashMap.put("MS:1000149", "autoFlex TOF/TOF");
		accInstrumentModelHashMap.put("MS:1000150", "Auto Spec Ultima NT");
		accInstrumentModelHashMap.put("MS:1000151", "Bio TOF II");
		accInstrumentModelHashMap.put("MS:1000152", "Bio TOF Q");
		accInstrumentModelHashMap.put("MS:1000153", "DELTA plusAdvantage");
		accInstrumentModelHashMap.put("MS:1000154", "DELTAplusXP");
		accInstrumentModelHashMap.put("MS:1000155", "ELEMENT2");
		accInstrumentModelHashMap.put("MS:1000156", "esquire4000");
		accInstrumentModelHashMap.put("MS:1000157", "esquire6000");
		accInstrumentModelHashMap.put("MS:1000158", "explorer");
		accInstrumentModelHashMap.put("MS:1000159", "GCT");
		accInstrumentModelHashMap.put("MS:1000160", "HCT");
		accInstrumentModelHashMap.put("MS:1000161", "HCT Plus");
		accInstrumentModelHashMap.put("MS:1000162", "HiRes ESI");
		accInstrumentModelHashMap.put("MS:1000163", "HiRes MALDI");
		accInstrumentModelHashMap.put("MS:1000164", "IsoPrime");
		accInstrumentModelHashMap.put("MS:1000165", "IsoProbe");
		accInstrumentModelHashMap.put("MS:1000166", "IsoProbe T");
		accInstrumentModelHashMap.put("MS:1000167", "LCQ Advantage");
		accInstrumentModelHashMap.put("MS:1000168", "LCQ Classic");
		accInstrumentModelHashMap.put("MS:1000169", "LCQ Deca XP Plus");
		accInstrumentModelHashMap.put("MS:1000170", "M@LDI L");
		accInstrumentModelHashMap.put("MS:1000171", "M@LDI LR");
		accInstrumentModelHashMap.put("MS:1000172", "MAT253");
		accInstrumentModelHashMap.put("MS:1000173", "MAT900XP");
		accInstrumentModelHashMap.put("MS:1000174", "MAT900XP Trap");
		accInstrumentModelHashMap.put("MS:1000175", "MAT95XP");
		accInstrumentModelHashMap.put("MS:1000176", "MAT95XP Trap");
		accInstrumentModelHashMap.put("MS:1000177", "microFlex");
		accInstrumentModelHashMap.put("MS:1000178", "microTOFLC");
		accInstrumentModelHashMap.put("MS:1000179", "neptune");
		accInstrumentModelHashMap.put("MS:1000180", "NG-5400");
		accInstrumentModelHashMap.put("MS:1000181", "OMEGA");
		accInstrumentModelHashMap.put("MS:1000182", "OMEGA-2001");
		accInstrumentModelHashMap.put("MS:1000183", "OmniFlex");
		accInstrumentModelHashMap.put("MS:1000184", "Platform ICP");
		accInstrumentModelHashMap.put("MS:1000185", "PolarisQ");
		accInstrumentModelHashMap.put("MS:1000186", "proteomics solution 1");
		accInstrumentModelHashMap.put("MS:1000187", "Q TRAP");
		accInstrumentModelHashMap.put("MS:1000188", "Q-Tof micro");
		accInstrumentModelHashMap.put("MS:1000189", "Q-Tof ultima");
		accInstrumentModelHashMap.put("MS:1000190", "QSTAR");
		accInstrumentModelHashMap.put("MS:1000191", "quattro micro");
		accInstrumentModelHashMap.put("MS:1000192", "Quattro UItima");
		accInstrumentModelHashMap.put("MS:1000193", "Surveyor MSQ");
		accInstrumentModelHashMap.put("MS:1000194", "SymBiot I");
		accInstrumentModelHashMap.put("MS:1000195", "SymBiot XVI");
		accInstrumentModelHashMap.put("MS:1000196", "TEMPUS TOF");
		accInstrumentModelHashMap.put("MS:1000197", "TRACE DSQ");
		accInstrumentModelHashMap.put("MS:1000198", "TRITON");
		accInstrumentModelHashMap.put("MS:1000199", "TSQ Quantum");
		accInstrumentModelHashMap.put("MS:1000200", "ultima");
		accInstrumentModelHashMap.put("MS:1000201", "ultraFlex");
		accInstrumentModelHashMap.put("MS:1000202", "ultraFlex TOF/TOF");
		accInstrumentModelHashMap.put("MS:1000203", "Voyager-DE PRO");
		accInstrumentModelHashMap.put("MS:1000204", "Voyager-DE STR");
		//
		accInstrumentModelHashMap.put("MS:1000447", "LTQ");
		accInstrumentModelHashMap.put("MS:1000448", "LTQ FT");
		accInstrumentModelHashMap.put("MS:1000449", "LTQ Orbitrap");
		accInstrumentModelHashMap.put("MS:1000450", "LXQ");
		//
		accInstrumentModelHashMap.put("MS:1000467", "1200 series LC/MSD SL");
		accInstrumentModelHashMap.put("MS:1000468", "6110 Quadrupole LC/MS");
		accInstrumentModelHashMap.put("MS:1000469", "6120 Quadrupole LC/MS");
		accInstrumentModelHashMap.put("MS:1000470", "6130 Quadrupole LC/MS");
		accInstrumentModelHashMap.put("MS:1000471", "6140 Quadrupole LC/MS");
		accInstrumentModelHashMap.put("MS:1000472", "6210 Time-of-Flight LC/MS");
		accInstrumentModelHashMap.put("MS:1000473", "6310 Ion Trap LC/MS");
		accInstrumentModelHashMap.put("MS:1000474", "6320 Ion Trap LC/MS");
		accInstrumentModelHashMap.put("MS:1000475", "6330 Ion Trap LC/MS");
		accInstrumentModelHashMap.put("MS:1000476", "6340 Ion Trap LC/MS");
		accInstrumentModelHashMap.put("MS:1000477", "6410 Triple Quadrupole LC/MS");
		accInstrumentModelHashMap.put("MS:1000478", "1200 series LC/MSD VL");
		//
		accInstrumentModelHashMap.put("MS:1000483", "Thermo Fisher Scientific instrument model");
		//
		accInstrumentModelHashMap.put("MS:1000488", "Hitachi instrument model");
		accInstrumentModelHashMap.put("MS:1000489", "Varian instrument model");
		accInstrumentModelHashMap.put("MS:1000490", "Agilent instrument model");
		accInstrumentModelHashMap.put("MS:1000491", "Dionex instrument model");
		accInstrumentModelHashMap.put("MS:1000492", "Thermo Electron instrument model");
		accInstrumentModelHashMap.put("MS:1000493", "Finnigan MAT instrument model");
		accInstrumentModelHashMap.put("MS:1000494", "Thermo Scientific instrument model");
		accInstrumentModelHashMap.put("MS:1000495", "Applied Biosystems instrument model");
		//
		accInstrumentModelHashMap.put("MS:1000554", "LCQ Deca");
		accInstrumentModelHashMap.put("MS:1000555", "LTQ Orbitrap Discovery");
		accInstrumentModelHashMap.put("MS:1000556", "LTQ Orbitrap XL");
		accInstrumentModelHashMap.put("MS:1000557", "LTQ FT Ultra");
		accInstrumentModelHashMap.put("MS:1000558", "GC Quantum");
		//
		accInstrumentModelHashMap.put("MS:1000578", "LCQ Fleet");
		//
		accInstrumentModelHashMap.put("MS:1000602", "Shimadzu Biotech instrument model");
		accInstrumentModelHashMap.put("MS:1000603", "Shimadzu Scientific Instruments instrument model");
		accInstrumentModelHashMap.put("MS:1000604", "LCMS-IT-TOF");
		accInstrumentModelHashMap.put("MS:1000605", "LCMS-2010EV");
		accInstrumentModelHashMap.put("MS:1000606", "LCMS-2010A");
		accInstrumentModelHashMap.put("MS:1000607", "AXIMA CFR MALDI-TOF");
		accInstrumentModelHashMap.put("MS:1000608", "AXIMA-QIT");
		accInstrumentModelHashMap.put("MS:1000609", "AXIMA-CFR plus");
		accInstrumentModelHashMap.put("MS:1000610", "AXIMA Performance MALDI-TOF/TOF");
		accInstrumentModelHashMap.put("MS:1000611", "AXIMA Confidence MALDI-TOF");
		accInstrumentModelHashMap.put("MS:1000612", "AXIMA Assurance Linear MALDI-TOF");
		//
		accInstrumentModelHashMap.put("MS:1000622", "Surveyor PDA");
		accInstrumentModelHashMap.put("MS:1000623", "Accela PDA");
		//
		accInstrumentModelHashMap.put("MS:1000632", "Q-Tof Premier");
		//
		accInstrumentModelHashMap.put("MS:10634", "DSQ");
		accInstrumentModelHashMap.put("MS:10635", "ITQ 700");
		accInstrumentModelHashMap.put("MS:10636", "ITQ 900");
		accInstrumentModelHashMap.put("MS:10637", "ITQ 1100");
		accInstrumentModelHashMap.put("MS:10638", "LTQ XL ETD");
		accInstrumentModelHashMap.put("MS:10639", "LTQ Orbitrap XL ETD");
		accInstrumentModelHashMap.put("MS:10640", "DFS");
		accInstrumentModelHashMap.put("MS:10641", "DSQ II");
		accInstrumentModelHashMap.put("MS:10642", "MALDI LTQ XL");
		accInstrumentModelHashMap.put("MS:10643", "MALDI LTQ Orbitrap");
		accInstrumentModelHashMap.put("MS:10644", "TSQ Quantum Access");
		accInstrumentModelHashMap.put("MS:10645", "Element XR");
		accInstrumentModelHashMap.put("MS:10646", "Element 2");
		accInstrumentModelHashMap.put("MS:10647", "Element GD");
		accInstrumentModelHashMap.put("MS:10648", "GC IsoLink");
		accInstrumentModelHashMap.put("MS:10649", "Exactive");
		accInstrumentModelHashMap.put("MS:10650", "Proteomics Discoverer");
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
    // changed from private by slewis
	protected StringBuffer getTempStrBuf()
	{
		return this.tempStrBuf;
	}


	/**
	 * Set the StringBuffer tempStrBuf value.
	 * 
	 * @param tempStrBuf StringBuffer tempStrBuf value to set.
	 */
    // changed from private by slewis
	protected void setTempStrBuf(StringBuffer tempStrBuf)
	{
		this.tempStrBuf = tempStrBuf;
	}


	/**
	 * Get the precision.
	 * 
	 * @return precision String the precision
	 */
    // changed from private by slewis
	protected String getPrecision()
	{
		return this.precision;
	}


	/**
	 * Set the precision.
	 * 
	 * @param precision String the precision to set.
	 */
    // changed from private by slewis
	protected void setPrecision(String precision)
	{
		this.precision = precision;
	}

	
	/**
	 * Get the compression.
	 * 
	 * @return compression String the compression
	 */
    // changed from private by slewis
	protected String getCompression()
	{
		return this.compression;
	}


	/**
	 * Set the compression.
	 * 
	 * @param compression String the compression to set.
	 */
    // changed from private by slewis
	protected void setCompression(String compression)
	{
		this.compression = compression;
	}

	
	/**
	 * Get the endian.
	 * 
	 * @return endian String the endian
	 */
    // changed from private by slewis
	protected String getEndian()
	{
		return this.endian;
	}


	/**
	 * Set the endian.
	 * 
	 * @param endian String the endian to set.
	 */
    // changed from private by slewis
	protected void setEndian(String endian)
	{
		this.endian = endian;
	}

	/**
	 * Get the dataLength.
	 * 
	 * @return dataLength int the dataLength
	 */
    // changed from private by slewis
	protected int getDataLength()
	{
		return this.dataLength;
	}


	/**
	 * Set the dataLength.
	 * 
	 * @param dataLength int the dataLength to set.
	 */
    // changed from private by slewis
	protected void setDataLength(int dataLength)
	{
		this.dataLength = dataLength;
	}


	/**
	 * Get the doublePrecision.
	 * 
	 * @return doublePrecision boolean doublePrecision based on String precision
	 */
    // changed from private by slewis
	protected boolean isDoublePrecision()
	{
		/*
		 * Set value of boolean variable 'doublePrecision':
		 * precision = "32" : doublePrecision = false
		 * precision = "64" : doublePrecision = true
		 */
		boolean doublePrecision = true;
		if (getPrecision().equals("64")) {
			doublePrecision = true;
		} else if (getPrecision().equals("32")) {
			doublePrecision = false;
		}
		return doublePrecision;
	}


	/**
	 * Get the zLibCompression.
	 * 
	 * @return zLibCompression boolean zLibCompression based on String compression
	 */
    // changed from private by slewis
	protected boolean isZLibCompression()
	{
		/*
		 * Set value of boolean variable 'zLibCompression':
		 * compression = "none" : zLibCompression = false
		 * compression = "zlib" : zLibCompression = true
		 */
		boolean zLibCompression = false;
		if (getCompression().equals("zlib")) {
			zLibCompression = true;
		} else if (getCompression().equals("none")) {
			zLibCompression = false;
		}
		return zLibCompression;
	}


	/**
	 * Get the bigEndian.
	 * 
	 * @return bigEndian boolean bigEndian based on String endian
	 */
    // changed from private by slewis
	protected boolean isBigEndian()
	{
		/*
		 * Set value of boolean variable 'bigEndian':
		 * endian = "little" : bigEndian = false
		 * endian = "big"    : bigEndian = true
		 */
		boolean bigEndian = true;
		if (getEndian().equals("big")) {
			bigEndian = true;
		} else if (getEndian().equals("little")) {
			bigEndian = false;
		}
		return bigEndian;
	}


	/**
	 * Get the list of spectrum ids to search for.
	 * 
	 * @return List<String> list of spectrum ids to search for.
	 */
    // changed from private by slewis
	protected List<String> getSpectrumIdsTarget()
	{
		return this.spectrumIdsTarget;
	}


	/**
	 * Set the list of spectrum ids to search for.
	 * 
	 * @param spectrumIdsTarget List<String> with list of spectrum ids to search for.
	 */
    // changed from private by slewis
	protected void setSpectrumIdsTarget(List<String> spectrumIdsTarget)
	{
		this.spectrumIdsTarget = spectrumIdsTarget;
	}


	/**
	 * Reset all lists of id values to search for to empty lists.
	 */
    // changed from private by slewis
	protected void resetIdsToFind()
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
    // changed from private by slewis
	protected String getCurrentSpectrumId()
	{
		return this.currentSpectrumId;
	}


	/**
	 * Set the current spectrum id.
	 * 
	 * @param currentSpectrumId String with current spectrum id.
	 */
    // changed from private by slewis
	protected void setCurrentSpectrumId(String currentSpectrumId)
	{
		this.currentSpectrumId = currentSpectrumId;
	}


	/**
	 * Get the last precursor for the current spectrum. If there is none, one
	 * is generated
	 * 
	 * @return SpectrumPrecursor A spectrum precursor
	 */
    // changed from private by slewis
	protected SpectrumPrecursor getSpectrumPrecursor()
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
    // changed from private by slewis
	protected SpectrumFileContact getSpectrumContactData()
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
    // changed from private by slewis
	protected SpectrumFileInstrumentInterface getSpectrumInstrumentData()
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
    // changed from private by slewis
	protected List<SpectrumFileInstrumentInterface> getSpectrumInstrumentList()
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
    // changed from private by slewis
	protected SpectrumImpl getSpectrum()
	{
		return this.spectrum;
	}


	/**
	 * Set the spectrum.
	 * 
	 * @param spectrum SpectrumImpl with mass and intensity values to set
	 */
    // changed from private by slewis
	protected void setSpectrum(SpectrumImpl spectrum)
	{
		this.spectrum = spectrum;
	}


	/**
	 * Get the list of spectrum id valus found.
	 * 
	 * @return List<String> list of spectrum id values found.
	 */
    // changed from private by slewis
	protected List<String> getSpectrumIdsFound()
	{
		return this.spectrumIdsFound;
	}


	/**
	 * Set the list of spectrum id values found.
	 * 
	 * @param spectrumIdsFound List<String> with list of spectrum id values found.
	 */
    // changed from private by slewis
	protected void setSpectrumIdsFound(List<String> spectrumIdsFound)
	{
		this.spectrumIdsFound = spectrumIdsFound;
	}


	/**
	 * Fetch spectrum file contact info from input stream.
	 * 
	 * @return SpectrumContact Spectrum file contact data.
	 */
    // changed from private by slewis
	protected SpectrumFileContact fetchSpectrumFileContactInfo()
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
			 * mzML XML tag is found.
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
			 * Return result of search for mzML XML contact tag.
			 */
			return spectrumContactData;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzML contact data failed.
			 */
			return null;
		}
	}


	/**
	 * Fetch spectrum file instrument info from input stream.
	 * 
	 * @return List<SpectrumFileInstrumentInfo> Spectrum file instrument list.
	 */
    // changed from private by slewis
	protected List<SpectrumFileInstrumentInterface> fetchSpectrumFileInstrumentInfo()
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
			 * the document until end of document, or until the instrument list
			 * mzML XML tag is found.
			 */
			instrumentListEndTagFound = false;
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT && (!instrumentListEndTagFound); event = parser
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
			 * Return result of search for mzML XML instrument list tag.
			 */
			return spectrumInstrumentList;
		}
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for mzML instrument data failed.
			 */
			return null;
		}
	}

    /**
     * Run through the entire MZML file
     * added slewis
     */

    public void processXMLFile()
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
            // turn off validation
            xmlInFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
          // turn off dtd
             xmlInFactory.setProperty(XMLInputFactory.SUPPORT_DTD , Boolean.FALSE);


                 XMLStreamReader parser = xmlInFactory
                .createXMLStreamReader(iStream);
              /*
             * Parse the XML input stream using cursor-based XML parsing. Parse
             * the document until end of document, or until the contact
             * mzML XML tag is found.
             */
            contactEndTagFound = false;
            for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT  ; event = parser
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
             * Return result of search for mzML XML contact tag.
             */
          }
        catch (XMLStreamException e)
        {
            log.warn("Problem when parsing xml input stream : " + e);
            /*
             * Return null as search for mzML contact data failed.
             */
            throw new RuntimeException(e);
        }
    }




	/**
	 * Fetch spectra with specified mzML spectrum ids from input stream.
	 * 
	 * @param spectrumIds List<String> list with mzML spectrum ids for desired spectra.
	 * @return List<SpectrumInterface> list with spectrum data.
	 */
    // changed from private by slewis
	protected List<SpectrumInterface> fetchSpectrum(List<String> spectrumIds)
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
			 * Parse the XML input stream using cursor-based XML parsing.
			 * Parse the document until end of document, or until the
			 * desired spectrum mzML XML tag is found.
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
			 * Return result of search for mzML XML spectrum tag.
			 */
			List<SpectrumInterface> spectrumList = new ArrayList<SpectrumInterface>(nSpectra);
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
			 * Return null as search for mzML spectrum data failed.
			 */
			return null;
		}
	}


	/**
	 * Fetch spectrum id values from input stream.
	 * 
	 * @return List<String> list with spectrum id data.
	 */
    // changed from private by slewis
	protected List<String> fetchSpectrumIds()
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
		InputStream iStream = getXMLInputStream();
		/*
		 * Use the reference implementation for the XML input factory
		 */
	//	System.setProperty("javax.xml.stream.XMLInputFactory",
	//		"com.bea.xml.stream.MXParserFactory");
		/*
		 * Create a parser stream reader for the XML input stream.
		 */
		try
		{
			XMLInputFactory xmlInFactory = XMLInputFactory.newInstance();
			XMLStreamReader parser = xmlInFactory
				.createXMLStreamReader(iStream);
			//inTargetPositionBlockToProcess = false;
			spectrumTagsFound  = 0;
			/*
			 * The full XML file has to be processed
			 */
			for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser
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
		catch (XMLStreamException e)
		{
			log.warn("Problem when parsing xml input stream : " + e);
			/*
			 * Return null as search for spectrum id data failed.
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
    // changed from private by slewis
	protected void processEvent(int event, XMLStreamReader parser)
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
				if (tempStrBuf == null) {
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
    // changed from private by slewis
	protected void processStartElement(XMLStreamReader parser)
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
		//log.debug("parser.getLocalName() = \"" + localName + "\"");
		/*
		 * Search for mzML "contact" XML block.
		 */
		if (localName.equals("contact"))
		{
			inTargetContactBlock = true;
		}
		/*
		 * Search for mzML "instrumentList" XML block.
		 */
		else if (localName.equals("instrumentList") || localName.equals("instrumentConfigurationList"))
		{
			inTargetInstrumentListBlock = true;
		}
		else if (localName.equals("instrument") || localName.equals("instrumentConfiguration"))
		{
			inTargetInstrumentBlock = true;
			// Reset spectrum instrument data
			spectrumInstrumentData = new SpectrumFileInstrument();
			/*
			 * Get attributes
			 */
			String attrValue = XMLImportUtil.seekAttribute("id", parser);
			if (attrValue != null)
			{
				// Set instrument name to id string
				getSpectrumInstrumentData().setInstrumentName(attrValue);
			}
		}
		else if (localName.equals("componentList"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentComponentListBlock = true;
			}
		}
		else if (localName.equals("source"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentSourceBlock = true;
			}
		}
		else if (localName.equals("analyzer"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentAnalyzerBlock = true;
				SpectrumFileAnalyzerInterface spectrumFileAnalyzer = new SpectrumFileAnalyzer();
				getSpectrumInstrumentData().getAnalyzers().add(spectrumFileAnalyzer);
			}
		}
		else if (localName.equals("detector"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentDetectorBlock = true;
			}
		}
		//
		else if (localName.equals("referenceableParamGroupList"))
		{
			// Create new referenceableParamGroupList empty list
			referenceableParamGroupList = new ArrayList<ReferenceableParamGroup>(0);
		}
		else if (localName.equals("referenceableParamGroup"))
		{
			inReferenceableParamGroupBlock = true;
			// Add new referenceableParamGroupList list element
			referenceableParamGroupList.add(new ReferenceableParamGroup());
			/*
			 * Get attributes
			 */
			String attrValue = XMLImportUtil.seekAttribute("id", parser);
			if (attrValue != null) {
				// Set id string for last element in list
				referenceableParamGroupList.get(referenceableParamGroupList.size()-1).id = attrValue;
			}
		}
		else if (localName.equals("referenceableParamGroupRef"))
		{
			if (inTargetInstrumentBlock)
			{
				/*
				 * Get attributes
				 */
				String attrValue = XMLImportUtil.seekAttribute("ref", parser);
				if (attrValue != null) {
					String ref = attrValue;
					// Search for referenceable param group in list
					for (int i = 0; i < referenceableParamGroupList.size(); i++)
					{
						if (referenceableParamGroupList.get(i).id.equals(ref))
						{
							// Check if instrument serial no set in referenceable param group
							String instrumentSerialNo = referenceableParamGroupList.get(i).refParamInstrumentSerialNo;
							if (instrumentSerialNo != null)
							{
								getSpectrumInstrumentData().setInstrumentSerialNo(instrumentSerialNo);
								log.debug("instrumentSerialNo: \"" + instrumentSerialNo + "\"");
							}
							// Check if instrument model set in referenceable param group
							String instrumentModelStr = referenceableParamGroupList.get(i).refParamInstrumentModel;
							if (instrumentModelStr != null && !instrumentModelStr.equals(""))
							{
								StringPair stringPair = new StringPair();
								stringPair.setName("Model");
								stringPair.setValue(instrumentModelStr);
								getSpectrumInstrumentData().getAdditional().add(stringPair);
								log.debug("instrumentModel: \"" + instrumentModelStr + "\"");
							}
						}
					}
				}
			}
			if (inCandidateBinaryDataArrayBlock)
			{
				/*
				 * Get attributes
				 */
				String attrValue = XMLImportUtil.seekAttribute("ref", parser);
				if (attrValue != null) {
					String ref = attrValue;
					// Search for referenceable param group in list
					for (int i = 0; i < referenceableParamGroupList.size(); i++)
					{
						if (referenceableParamGroupList.get(i).id.equals(ref))
						{
							String precision = referenceableParamGroupList.get(i).refParamPrecision;
							String compression = referenceableParamGroupList.get(i).refParamCompression;
							String arrayType = referenceableParamGroupList.get(i).refParamArrayType;
							if (arrayType.equals("m/z array"))
							{
								inMzTargetBinaryDataArrayBlock = true;
								log.debug("m/z binaryDataArray block found");
							}
							else if (arrayType.equals("intensity array"))
							{
								inIntensityTargetBinaryDataArrayBlock = true;
								log.debug("intensity binaryDataArray block found");
							}
							/*
							 * Store data attribute values for later use.
							 */
							if (!precision.equals(""))
							{
								setPrecision(precision);
								log.debug("precision: \"" + precision + "\"");
							}
							if (!compression.equals(""))
							{
								setCompression(compression);
								log.debug("compression: \"" + compression + "\"");
							}
						}
					}
				}
			}
		}
		/*
		 * Search for desired mzML "spectrum" XML block.
		 */
		else if (localName.equals("spectrum"))
		{
			/*
			 * Get attributes
			 */
			String attrValue = XMLImportUtil.seekAttribute("id", parser);
			if (attrValue != null) {
				String currentSpectrumIdStr = attrValue;
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
				int nSpectrumId = 0;
				if (getSpectrumIdsTarget() != null)
				{
					nSpectrumId = getSpectrumIdsTarget().size();
				}
				for (int i = 0; i < nSpectrumId; i++) {
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
						SpectrumImpl currentSpectrum = (SpectrumImpl)SpectrumImpl.buildSpectrum();  // changed slewis to allow class to change
						setSpectrum(currentSpectrum);
						log.debug("SpectrumId (" + spectrumIndex + ") = \"" + getCurrentSpectrumId() + "\" found.");
					}
				}
			}
		}
		// Check if this is relevant for mzML
		else if (localName.equals("spectrumInstrument"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetSpectrumInstrumentBlock = true;
				log.debug("spectrumInstrument block found");
			}
		}
		else if (localName.equals("precursor"))
		{
			if (inTargetSpectrumBlock)
			{
				inTargetPrecursorBlock = true;
				getSpectrum().addPrecursor(SpectrumPrecursor.buildSpectrumPrecursor()); // changed slewis to support overrides
				log.debug("precursor block found");
			}
		}
		else if (localName.equals("selectedIon"))
		{
			if (inTargetPrecursorBlock)
			{
				inTargetSelectedIonBlock = true;
				getSpectrumPrecursor().newSelectedIon();
				log.debug("selectedIon block found");
			}
		}
		else if (localName.equals("cvParam"))
		{
			if (inTargetContactBlock)
			{
				/*
				 * Get contact data attributes
				 */
				String attrValue = XMLImportUtil.seekAttribute("accession", parser);
				if (attrValue != null) {
					String currentCvParamAccessionStr = attrValue;
					/*
					 * Check if desired cvParam found.
					 */
					if (currentCvParamAccessionStr.equals(ACC_CONTACT_NAME))
					{
						String contactNameStr = null;
						contactNameStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumContactData().setName(contactNameStr);
					}
					else if (currentCvParamAccessionStr.equals(ACC_CONTACT_ADDRESS))
					{
						String contactAddressStr = null;
						contactAddressStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumContactData().setAddress(contactAddressStr);
					}
					else if (currentCvParamAccessionStr.equals(ACC_CONTACT_URL))
					{
						String contactUrlStr = null;
						contactUrlStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumContactData().setUrl(contactUrlStr);
					}
					else if (currentCvParamAccessionStr.equals(ACC_CONTACT_EMAIL))
					{
						String contactEmailStr = null;
						contactEmailStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumContactData().setEmail(contactEmailStr);
					}
				}
			}
			else if (inTargetInstrumentBlock)
			{
				/*
				 * Get instrument data attributes
				 */
				String currentCvParamAccessionStr = XMLImportUtil.seekAttribute("accession", parser);
				if (currentCvParamAccessionStr != null) {
					/*
					 * Check if desired cvParam found.
					 */
					if (currentCvParamAccessionStr.equals(ACC_INSTRUMENT_NAME))
					{
						String instrumentNameStr = null;
						// Note that the instrument name is stored in the "name" part, not the "value" part.
						instrumentNameStr = XMLImportUtil.seekAttribute("name",
							parser);
						getSpectrumInstrumentData().setInstrumentName(instrumentNameStr);
					}
					else if (currentCvParamAccessionStr.equals(ACC_INSTRUMENT_SERIAL_NO))
					{
						String instrumentSerialNoStr = null;
						instrumentSerialNoStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumInstrumentData().setInstrumentSerialNo(instrumentSerialNoStr);
					}
					else if (currentCvParamAccessionStr.equals(ACC_INSTRUMENT_MODEL))
					{
						String instrumentModelStr = null;
						instrumentModelStr = XMLImportUtil.seekAttribute("value",
							parser);
						StringPair stringPair = new StringPair();
						stringPair.setName("Model");
						stringPair.setValue(instrumentModelStr);
						getSpectrumInstrumentData().getAdditional().add(stringPair);
					}
					else if (accInstrumentModelHashMap.containsKey(currentCvParamAccessionStr))
					{
						String instrumentModelStr = null;
						instrumentModelStr = XMLImportUtil.seekAttribute("name",
							parser);
						if (instrumentModelStr == null || instrumentModelStr.equals(""))
						{
							// Get instrument model name from accession number
							instrumentModelStr = accInstrumentModelHashMap.get(currentCvParamAccessionStr);
						}
						StringPair stringPair = new StringPair();
						stringPair.setName("Model");
						stringPair.setValue(instrumentModelStr);
						getSpectrumInstrumentData().getAdditional().add(stringPair);
					}
				}
				//
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
				}
			}
			else if (inCandidateBinaryDataArrayBlock || inReferenceableParamGroupBlock)
			{
				/*
				 * Get spectrum data attributes
				 */
				String precision = new String("");
				String compression = new String("");
				/*
				 * Get attributes
				 */
				String currentCvParamAccessionStr = XMLImportUtil.seekAttribute("accession", parser);
				if (currentCvParamAccessionStr != null) {
					/*
					 * Check if desired cvParam found.
					 */
					if (currentCvParamAccessionStr.equals(ACC_INSTRUMENT_SERIAL_NO))
					{
						String instrumentSerialNoStr = null;
						instrumentSerialNoStr = XMLImportUtil.seekAttribute("value",
							parser);
						getSpectrumInstrumentData().setInstrumentSerialNo(instrumentSerialNoStr);
						// Set refParamInstrumentSerialNo string for last element in list
						referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamInstrumentSerialNo = instrumentSerialNoStr;
					}
					else if (currentCvParamAccessionStr.equals(ACC_INSTRUMENT_MODEL))
					{
						String instrumentModelStr = null;
						instrumentModelStr = XMLImportUtil.seekAttribute("value",
							parser);
						StringPair stringPair = new StringPair();
						stringPair.setName("Model");
						stringPair.setValue(instrumentModelStr);
						getSpectrumInstrumentData().getAdditional().add(stringPair);
						// Set refParamInstrumentModel string for last element in list
						referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamInstrumentModel = instrumentModelStr;
					}
					else if (accInstrumentModelHashMap.containsKey(currentCvParamAccessionStr))
					{
						String instrumentModelStr = null;
						instrumentModelStr = XMLImportUtil.seekAttribute("name",
							parser);
						if (instrumentModelStr == null || instrumentModelStr.equals(""))
						{
							// Get instrument model name from accession number
							instrumentModelStr = accInstrumentModelHashMap.get(currentCvParamAccessionStr);
						}
						StringPair stringPair = new StringPair();
						stringPair.setName("Model");
						stringPair.setValue(instrumentModelStr);
						getSpectrumInstrumentData().getAdditional().add(stringPair);
						// Set refParamInstrumentModel string for last element in list
						referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamInstrumentModel = instrumentModelStr;
					}
					else if (currentCvParamAccessionStr.equals(ACC_32_BIT_FLOAT))
					{
						precision = new String("32");
					}
					else if (currentCvParamAccessionStr.equals(ACC_64_BIT_FLOAT))
					{
						precision = new String("64");
					}
					else if (currentCvParamAccessionStr.equals(ACC_NO_COMPRESSION))
					{
						compression = new String("none");
					}
					else if (currentCvParamAccessionStr.equals(ACC_ZLIB_COMPRESSION))
					{
						compression = new String("zlib");
					}
					else if (currentCvParamAccessionStr.equals(ACC_MZ_ARRAY))
					{
						if (inCandidateBinaryDataArrayBlock)
						{
							inMzTargetBinaryDataArrayBlock = true;
							log.debug("m/z binaryDataArray block found");
						}
						else if (inReferenceableParamGroupBlock)
						{
							// Set refParamArrayType string for last element in list
							referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamArrayType = new String("m/z array");
						}
					}
					else if (currentCvParamAccessionStr.equals(ACC_INTENSITY_ARRAY))
					{
						if (inCandidateBinaryDataArrayBlock)
						{
							inIntensityTargetBinaryDataArrayBlock = true;
							log.debug("intensity binaryDataArray block found");
						}
						else if (inReferenceableParamGroupBlock)
						{
							// Set refParamArrayType string for last element in list
							referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamArrayType = new String("intensity array");
						}
					}
				}
				/*
				 * Store data attribute values for later use.
				 */
				if (!precision.equals(""))
				{
					if (inCandidateBinaryDataArrayBlock)
					{
						setPrecision(precision);
						log.debug("precision: \"" + precision + "\"");
					}
					else if (inReferenceableParamGroupBlock)
					{
						// Set refParamPrecision string for last element in list
						referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamPrecision = precision;
					}
				}
				if (!compression.equals(""))
				{
					if (inCandidateBinaryDataArrayBlock)
					{
						setCompression(compression);
						log.debug("compression: \"" + compression + "\"");
					}
					else if (inReferenceableParamGroupBlock)
					{
						// Set refParamCompression string for last element in list
						referenceableParamGroupList.get(referenceableParamGroupList.size()-1).refParamCompression = compression;
					}
				}
			}
			// Check if this is relevant for mzML
			else if (inTargetSpectrumBlock)
			{
				/*
				 * Get attributes
				 */
				boolean cvParamProcessed = false;
				String attrValue = XMLImportUtil.seekAttribute("accession", parser);
				if (attrValue != null) {
					String currentCvParamAccessionStr = attrValue;
					/*
					 * Check if desired cvParam found.
					 */
					/*
					 * The first appearing scan time for a spectrum will be retained.
					 */
					if (currentCvParamAccessionStr.equals(ACC_SCAN_TIME) && spectrum.getRetentionTimeInMinutes()==null)
					{
						log.debug("Accession value when looking for scan time = \"" + currentCvParamAccessionStr + "\"");
						cvParamProcessed = true;
						// Check unit
						String unitStr = new String("");
						String currentCvParamUnitAccessionStr = XMLImportUtil.seekAttribute("unitAccession", parser);
						log.debug("currentCvParamUnitAccessionStr = \"" + currentCvParamUnitAccessionStr + "\"");
						if (currentCvParamUnitAccessionStr != null)
						{
							if (currentCvParamUnitAccessionStr.equals(ACC_UNIT_MINUTE_UO))
							{
								unitStr = new String("minute");
							}
							else if (currentCvParamUnitAccessionStr.equals(ACC_UNIT_SECOND_UO))
							{
								unitStr = new String("second");
							}
							else if (currentCvParamUnitAccessionStr.equals(ACC_UNIT_MINUTE_MS))
							{
								unitStr = new String("minute");
							}
							else if (currentCvParamUnitAccessionStr.equals(ACC_UNIT_SECOND_MS))
							{
								unitStr = new String("second");
							}
						}
						log.debug("unitStr = \"" + unitStr + "\"");
						String retentionTimeStr = null;
						retentionTimeStr = XMLImportUtil.seekAttribute("value", parser);
						log.debug("retentionTimeStr =\"" + retentionTimeStr + "\"");
						if (retentionTimeStr != null && !retentionTimeStr.equals(""))
						{
							Double retentionTime = Double.valueOf(retentionTimeStr);
							// Optional conversion to minute as unit
							if (unitStr.equals("second"))
							{
								retentionTime = retentionTime/60.0;
							}
							/*
							 * Add retention time to result spectrum.
							 */
							if (unitStr.equals("minute") || unitStr.equals("second"))
							{
								getSpectrum().setRetentionTimeInMinutes(retentionTime);
								log.debug("cvParam block with retentionTimeInMinutes found, time = " + retentionTime);
							}
						}
					}
					else if (currentCvParamAccessionStr.equals(ACC_SELECTED_MASS_TO_CHARGE_RATIO))
					{
						log.debug("Accession value when looking for precursor m/z = \"" + currentCvParamAccessionStr + "\"");
						cvParamProcessed = true;
						if (inTargetPrecursorBlock)
						{
							String pepmass = XMLImportUtil.seekAttribute("value", parser);
							if (pepmass != null)
							{
								getSpectrumPrecursor().setMassToChargeRatio(
									Double.valueOf(pepmass));
							}
							log.debug("precursor m/z =\"" + pepmass + "\"");
						}
					}
					else if (currentCvParamAccessionStr.equals(ACC_CHARGE_STATE))
					{
						log.debug("Accession value when looking for precursor charge state = \"" + currentCvParamAccessionStr + "\"");
						cvParamProcessed = true;
						if (inTargetPrecursorBlock)
						{
							String charge = XMLImportUtil.seekAttribute("value", parser);
							if (charge != null)
							{
								getSpectrumPrecursor().setCharge(
									Integer.valueOf(charge));
							}
							log.debug("precursor charge =\"" + charge + "\"");
						}
					}
					else if (currentCvParamAccessionStr.equals(ACC_INTENSITY))
					{
						log.debug("Accession value when looking for precursor intensity = \"" + currentCvParamAccessionStr + "\"");
						cvParamProcessed = true;
						if (inTargetPrecursorBlock)
						{
							String intensity = XMLImportUtil.seekAttribute("value", parser);
							if (intensity != null)
							{
								getSpectrumPrecursor().setIntensity(
									Double.valueOf(intensity));
							}
							log.debug("precursor intensity =\"" + intensity + "\"");
						}
					}
					else
					{
						/*
						 * Check for precursor fragmentation type data
						 */
						if (inTargetPrecursorBlock)
						{
							// Get fragmentation type from cvParam accession number string
							SpectrumPrecursor.FragmentationType fragmentationType =
								SpectrumPrecursor.FragmentationType.fromMsOntologyAccessionNumber(currentCvParamAccessionStr);
							if (fragmentationType != null)
							{
								getSpectrumPrecursor().setFragmentationType(fragmentationType);
							}
						}
					}
				}
				if (!cvParamProcessed)
				{				
					/*
					 * Process extra data
					 */
					String currentCvParamNameStr = XMLImportUtil.seekAttribute("name", parser);
					String currentCvParamValueStr = XMLImportUtil.seekAttribute("value", parser);
					if (currentCvParamNameStr != null && currentCvParamValueStr != null)
					{
						StringPair stringPair = new StringPair();
						stringPair.setName(currentCvParamNameStr);
						stringPair.setValue(currentCvParamValueStr);
						// Add cvParam data
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
		else if (localName.equals("userParam"))
		{			
			if (inTargetInstrumentBlock)
			{
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
				}
			}
		}
		else if (localName.equals("binaryDataArray"))
		{
			if (inTargetSpectrumBlock)
			{
				// Block may contain m/z or intensity values
				inCandidateBinaryDataArrayBlock = true;
				log.debug("Candidate binaryDataArray block found");
				
				/*
				 * Get spectrum data attributes
				 */
				int dataLength = 0;
				/*
				 * Get attributes
				 */
				String attrValue;
				attrValue = XMLImportUtil.seekAttribute("arrayLength", parser);
				if (attrValue != null) {
					//dataLength = Integer.parseInt(attrValue);
					dataLength = Integer.valueOf(attrValue).intValue();
				}
				/*
				 * Store data attribute values for later use.
				 */
				// MzML always uses little endian byte ordering
				//setPrecision(precision);
				setEndian("little");
				setDataLength(dataLength);
				log.debug("dataLength: " + dataLength);
				log.debug("endian: \"" + endian + "\" dataLength: " + dataLength);
			}
		}
		else if (localName.equals("binary"))
		{
			if (inMzTargetBinaryDataArrayBlock || inIntensityTargetBinaryDataArrayBlock)
			{
				inTargetBinaryBlock = true;
				log.debug("binary block found");

				log
					.debug("precision: \"" + precision + "\" endian: \"" + endian + "\" dataLength: " + dataLength);
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
    // changed from private by slewis
	protected void processEndElement(XMLStreamReader parser)
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
		if (localName.equals("contact"))
		{
			inTargetContactBlock = false;
			contactEndTagFound = true;
		}
		else if (localName.equals("source"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentSourceBlock = false;
			}
		}
		else if (localName.equals("analyzer"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentAnalyzerBlock = false;
			}
		}
		else if (localName.equals("detector"))
		{
			if (inTargetInstrumentComponentListBlock)
			{
				inTargetInstrumentDetectorBlock = false;
			}
		}
		else if (localName.equals("componentList"))
		{
			if (inTargetInstrumentBlock)
			{
				inTargetInstrumentComponentListBlock = false;
			}
		}
		else if (localName.equals("instrument")  || localName.equals("instrumentConfiguration"))
		{
			inTargetInstrumentBlock = false;
			// Add current instrument information to list
			getSpectrumInstrumentList().add(getSpectrumInstrumentData());
		}
		else if (localName.equals("instrumentList") || localName.equals("instrumentConfigurationList"))
		{
			inTargetInstrumentListBlock = false;
			instrumentListEndTagFound = true;
		}
		//
		if (localName.equals("referenceableParamGroup"))
		{
			inReferenceableParamGroupBlock = false;
		}
		else if (localName.equals("spectrum"))
		{
			if (inTargetSpectrumBlock)
			{
				/*
				 * Add retrieved spectrum data to all elements
				 * in result list corresponding to current spectrum ids.
				 */
				int nSpectrumId = 0;
				if (getSpectrumIdsTarget() != null)
				{
					nSpectrumId = getSpectrumIdsTarget().size();
				}
				for (int i = 0; i < nSpectrumId; i++) {
					if (getCurrentSpectrumId().equals(getSpectrumIdsTarget().get(i)))
					{
						spectrumArray[i] = getSpectrum();
						spectrumTagsFound++;
						log.debug("SpectrumId (" + i + ") = \"" + getCurrentSpectrumId() + "\" processed, spectrumTagsFound = " + spectrumTagsFound);
						log.debug("-----------------------------------------------");
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
		else if (localName.equals("selectedIon"))
		{
			inTargetSelectedIonBlock = false;
		}
		else if (localName.equals("binaryDataArray"))
		{
			inCandidateBinaryDataArrayBlock = false;
			inMzTargetBinaryDataArrayBlock = false;
			inIntensityTargetBinaryDataArrayBlock = false;
		}
		else if (localName.equals("binary"))
		{
			if (inTargetBinaryBlock)
			{
				/*
				 * Get data from temporary string buffer
				 */
				String dataBase64 = new String("");
				if (getTempStrBuf() != null) {
					dataBase64 = getTempStrBuf().toString();
				}

				/*
				 * Get Base64 coding parameters.
				 */
				boolean doublePrecision = isDoublePrecision();
				boolean bigEndian = isBigEndian();
				boolean zLibCompression = isZLibCompression();

				/*
				 * Process element.
				 */
				decodedBase64List = dataItem(doublePrecision, bigEndian, zLibCompression, dataBase64);

				/*
				 * Convert to double[] array.
				 */
				int nPeaks = decodedBase64List.size();
				double[] dataArray = new double[nPeaks];
				for (int i = 0; i < nPeaks; i++) {
					dataArray[i] = decodedBase64List.get(i);
				}

				/*
				 * Add data list to result spectrum.
				 */
				if (inMzTargetBinaryDataArrayBlock)
				{
					getSpectrum().setMass(dataArray);
				}
				else if (inIntensityTargetBinaryDataArrayBlock)
				{
					getSpectrum().setIntensities(dataArray);
				}
				/*
				 * Reset data attribute values.
				 */
				setPrecision("");
				setEndian("");
				setCompression("");
				setDataLength(0);
			}
			inTargetBinaryBlock = false;
		}
	}

	
	/**
	 * Processes a Characters event.
	 * 
	 * @param parser XMLStreamReader instance.
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
    // changed from private by slewis
	protected void processCharacters(XMLStreamReader parser)
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
	 * @param doublePrecision boolean precision = 64: true, precision = 32: false
	 * @param bigEndian boolean big endian: true; little endian: false
	 * @param zLibCompression boolean compression = zlib: true, compression = none: false
	 * @param dataBase64Raw String with Base64-coded data block
	 * @return decodedBase64 List with decoded data
	 */
    // changed from private by slewis
	protected List<Double> dataItem(boolean doublePrecision, boolean bigEndian, boolean zLibCompression,  String dataBase64Raw)
	{
		/*
		 * New data item
		 */
		/*
		 * Remove any line break characters from
		 * the base64-encoded block.
		 */
		StringBuffer dataBase64RawStrBuf = new StringBuffer(dataBase64Raw);
		StringBuffer dataBase64StrBuf = new StringBuffer("");
		int nChars = 0;
		int nLines = 0;
		int lineLength = 0;
		boolean newLineFlag = true;
		if (dataBase64Raw != null) {
			if (dataBase64Raw.length() > 0) {
				for (int i = 0; i < dataBase64RawStrBuf.length(); i++) {
					char c = dataBase64RawStrBuf.charAt(i);
					if (c == '\r' || c == '\n') {
						newLineFlag = true;
					} else {
						dataBase64StrBuf.append(c);
						nChars++;
						if (newLineFlag) {
							nLines++;
						}
						if (nLines == 1) { 
							lineLength++;
						}
						newLineFlag = false;
					}
				}
			}
		}
		String dataBase64 = dataBase64StrBuf.toString();
		log
			.debug("nLines = " + nLines + " lineLength = " + lineLength + " nChars = " + nChars + " dataBase64Raw.length() = " + dataBase64Raw.length());

		/*
		 * Decode Base64 coded data
		 * and put extracted double
		 * values in list decodeBase64.
		 */
		List<Double> decodedBase64 = new ArrayList<Double>(0);
		if (dataBase64 != null) {
			if (dataBase64.length() > 0) {
				decodedBase64 = Base64Util.decode(doublePrecision, bigEndian, zLibCompression, dataBase64);
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
		return false;
	}

	
	// -------------------------------------------
	/*
	 * From the FileValidationInterface interface
	 * -------------------------------------------
	 */
	/**
	 * Validates mzML XML input stream against current mzML XSD file.
	 * 
	 * @return True if the mzML XML input stream is valid, else false
	 * @throws XMLStreamException If there is an XML Stream related error
	 */
	public boolean valid()
			throws XMLStreamException, SAXException
	{
		boolean retVal = XMLValidator
			.validate(this.xsdFilePath, getXMLInputStream());
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
			 * Parse the first XML event
			 */
			if (parser.hasNext() && (parser.next() == XMLStreamConstants.START_ELEMENT))
			{
				if (parser.getLocalName().equals("mzML"))
				{
					importable = true;
				}
				else
				{
					log
						.warn("Start element local name=" + parser
							.getLocalName());
				}
			}
			else
			{
				log.warn("Not at start element." + parser
					.getLocalName());
			}
			parser.close();
		}
		catch (Exception e)
		{
			log.warn("Exception at importable():" + e);
		}
		return importable;
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
