/*
 $Id: TestMzMLFileReader.java 2640 2008-04-11 10:42:03Z olle $

 Copyright (C) 2008 Gregory Vincic, Olle Mansson

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
package org.proteios.io;

import junit.framework.*;
import org.junit.Assert;
import org.proteios.PropertiesFile;
import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.net.URL;
import javax.xml.stream.XMLStreamException;
import org.junit.*;
import org.junit.Test;

/**
 * This class tests retrieving spectra from from an mzML file.
 * The tests in this class are part integration tests, and relies
 * on a fixed mzML XML file to be parsed. The methods tested here
 * are defined in MzMLFileReader and related classes, if not
 * specified otherwise.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2008-04-11 03:42:03 -0700 (Fri, 11 Apr 2008) $
 */
public class TestMzMLFileReader
 {
	private MzMLFileReader mzMLFileReader;
	private String testDataDirURLStr = null;
	private URL testDataDirURL = null;
	private String testDataDir = null;

     @Before
	public void setUp()
			throws Exception
	{
		PropertiesFile pf = new PropertiesFile();
		testDataDirURLStr = pf.getProperty("test.data.url");
		/*
		 * Path to be used in tests taking
		 * a local path as argument.
		 */
		testDataDir = null;
		testDataDirURL = null;
		testDataDirURL = new URL(testDataDirURLStr);
		/*
		 * If the url String is a relative local path,
		 * creating a URL will expand the full path.
		 */
        String s = testDataDirURL.toString();
        if (testDataDirURL != null && !testDataDirURL.toString().equals("") && s.startsWith("file:"))
		{
			/*
			 * Store full local path for use im tests
			 * taking a local path as argument. 
			 */
            testDataDir = s.replace("file://","");

		//	testDataDir = testDataDirURL.getPath();
		}
		/*
		 * Test-specific Initializations may be placed here. 
		 */
		mzMLFileReader = new MzMLFileReader();
	}


	 
	/**
	 * Tests setXsdFilePath(String xsdFilePath)
	 */
    @Test
	public void testSetXsdFilePath()
	{
		String testXsdFilePath1 = new String(testDataDir + "/mzML0.99.1.xsd");
		mzMLFileReader.setXsdFilePath(testXsdFilePath1);
		String testXsdFilePath2 = mzMLFileReader.getXsdFilePath();
		Assert.assertEquals(
			"getXsdFilePath() should return the same path String as set by setXsdFilePath()",
			testXsdFilePath1, testXsdFilePath2);
	}


    /**
     * Tests valid()
     */
    public void xtestValidOk()
    {
        String testResourcePath = "1min.mzML";
              InputStream iStream = getClass().getResourceAsStream(testResourcePath);
            if(iStream ==  null)
                 Assert.fail("IOException when creating input stream from xml file \"" + testResourcePath + "\"");
            mzMLFileReader.setXMLInputStream(iStream);
          try
        {
            boolean testResult = mzMLFileReader.valid();
            Assert.assertEquals(
                "valid() should return true for a valid XSD/XML file pair",
                (boolean) true, testResult);
        }
        catch (SAXException e)
        {
            Assert.fail("valid() throwed a SAXException for a valid XSD/XML file pair.");
        }
        catch (XMLStreamException e)
        {
            Assert.fail("valid() throwed an XMLStreamException for a valid XSD/XML file pair.");
        }
    }

    /**
     * Tests valid()
     */
    public void xtestValidOk2()
    {
        String testXmlFilePath = new String(testDataDir + "/tiny1.mzML0.99.1.mzML");
        try
        {
            java.io.File xmlFile = new java.io.File(testXmlFilePath);
            InputStream iStream = new FileInputStream(xmlFile);
            mzMLFileReader.setXMLInputStream(iStream);
        }
        catch (IOException e)
        {
            Assert.fail("IOException when creating input stream from xml file \"" + testXmlFilePath + "\"");
        }
        try
        {
            boolean testResult = mzMLFileReader.valid();
            Assert.assertEquals(
                "valid() should return true for a valid XSD/XML file pair",
                (boolean) true, testResult);
        }
        catch (SAXException e)
        {
            Assert.fail("valid() throwed a SAXException for a valid XSD/XML file pair.");
        }
        catch (XMLStreamException e)
        {
            Assert.fail("valid() throwed an XMLStreamException for a valid XSD/XML file pair.");
        }
    }


	/**
	 * Tests valid(String xmlFilePath)
	 */
    //@Test   fix  testDataDir
	public void testValidNotOk()
	{
		String testXmlFilePath = new String(testDataDir + "/mzDataNotOk.xml");
		try
		{
			java.io.File xmlFile = new java.io.File(testXmlFilePath);
			InputStream iStream = new FileInputStream(xmlFile);
			mzMLFileReader.setXMLInputStream(iStream);
		}
		catch (IOException e)
		{
			Assert.fail("IOException when creating input stream from xml file \"" + testXmlFilePath + "\"");
		}
		try
		{
			boolean testResult = mzMLFileReader.valid();
			Assert.assertEquals(
				"valid() should return false for an invalid XSD/XML file pair",
				(boolean) false, testResult);
		}
		catch (SAXException e)
		{
			Assert.fail("valid() throwed a SAXException for an invalid XSD/XML file pair.");
		}
		catch (XMLStreamException e)
		{
			Assert.fail("valid() throwed an XMLStreamException for an invalid XSD/XML file pair.");
		}
	}


	/**
	 * Tests getSpectrum(String spectrumId)
	 */
    //@Test   fix  testDataDir
	public void testGetSpectrum()
	{
		String testXmlFilePath = new String(testDataDir + "/tiny1.mzML0.99.1.mzML");
		try
		{
			java.io.File xmlFile = new java.io.File(testXmlFilePath);
			InputStream iStream = new FileInputStream(xmlFile);
			mzMLFileReader.setXMLInputStream(iStream);
		}
		catch (IOException e)
		{
			Assert.fail("IOException when creating input stream from xml file \"" + testXmlFilePath + "\"");
		}
		String spectrumIdStr = new String("S20");
		SpectrumInterface testSpectrum1 = SpectrumImpl.buildSpectrum();  // changed slewis to allow class to change
		testSpectrum1 = mzMLFileReader.getSpectrum(spectrumIdStr);
		int dataLength1 = 43;
		int dataLength2 = testSpectrum1.listMass().length;
		Assert.assertEquals(
			"getSpectrum() should return the correct number of data values",
			dataLength1, dataLength2);
		double lastMass = 0.0;
		double lastIntensity = 0.0;
		double[] massArray = testSpectrum1.listMass();
		double[] intensityArray = testSpectrum1.listIntensities();
		for (int i = 0; i < dataLength2; i++)
		{
			lastMass = massArray[i];
			lastIntensity = intensityArray[i];
			//System.out.println("TestMzMLFileReader::testGetSpectrum(): i = " + i + " mass = " + lastMass + " intensity = " + lastIntensity);
		}
		Assert.assertEquals(
			"listMass() should return the correct double",
			531.078369140625, lastMass, 0.000001);
		Assert.assertEquals(
			"listIntensities() should return the correct double",
			1880.0, lastIntensity, 0.000001);
	}

	/**
	 * Tests getSpectrum(String spectrumId)
	 */
    //@Test   fix  testDataDir
	public void testGetSpectrum2()
	{
		String testXmlFilePath = new String(testDataDir + "/tiny1.mzML0.99.1.mzML");
		try
		{
			java.io.File xmlFile = new java.io.File(testXmlFilePath);
			InputStream iStream = new FileInputStream(xmlFile);
			mzMLFileReader.setXMLInputStream(iStream);
		}
		catch (IOException e)
		{
			Assert.fail("IOException when creating input stream from xml file \"" + testXmlFilePath + "\"");
		}
		String spectrumIdStr1 = new String("S19");
		String spectrumIdStr2 = new String("S20");
		List<String> spectrumIds = new ArrayList<String>();
		spectrumIds.add(spectrumIdStr1);
		spectrumIds.add(spectrumIdStr2);
		Iterator<SpectrumInterface> testSpectrumIter = mzMLFileReader.getSpectrum(spectrumIds);
		int iSpectra = 0;
		double testMass = 0.0;
		double testIntensity = 0.0;
		while (testSpectrumIter.hasNext()) {
			iSpectra++;
			SpectrumInterface testSpectrum = testSpectrumIter.next();
			int dataLength = testSpectrum.listMass().length;
			//System.out.println("TestMzMLFileReader::testGetSpectrum2(): retrieved spectrum #"+iSpectra+" dataLength = " + dataLength);
			//System.out.println("TestMzMLFileReader::testGetSpectrum2(): ----------------------------------------------------");
			double lastMass = 0.0;
			double lastIntensity = 0.0;
			double[] massArray = testSpectrum.listMass();
			double[] intensityArray = testSpectrum.listIntensities();
			for (int i = 0; i < dataLength; i++)
			{
				lastMass = massArray[i];
				lastIntensity = intensityArray[i];
				//System.out.println("TestMzMLFileReader::testGetSpectrum2(): i = " + i + " mass = " + lastMass + " intensity = " + lastIntensity);
			}
			if (iSpectra == 2)
			{
				testMass = lastMass;
				testIntensity = lastIntensity;
			}
		}
		Assert.assertEquals(
			"listMass() should return the correct double",
			531.078369140625, testMass, 0.000001);
		Assert.assertEquals(
			"listIntensities() should return the correct double",
			1880.0, testIntensity, 0.000001);
	}


	/**
	 * Tests getSpectrumIdList()
	 */
    //@Test   fix  testDataDir
 	public void testGetSpectrumIdList()
	{
		String testXmlFilePath = new String(testDataDir + "/tiny1.mzML0.99.1.mzML");
		try
		{
			java.io.File xmlFile = new java.io.File(testXmlFilePath);
			InputStream iStream = new FileInputStream(xmlFile);
			mzMLFileReader.setXMLInputStream(iStream);
		}
		catch (IOException e)
		{
			Assert.fail("IOException when creating input stream from xml file \"" + testXmlFilePath + "\"");
		}
		String spectrumIdStr1 = new String("S19");
		String spectrumIdStr2 = new String("S20");
		List<String> spectrumIds1 = new ArrayList<String>();
		spectrumIds1.add(spectrumIdStr1);
		spectrumIds1.add(spectrumIdStr2);
		List<String> spectrumIds2 = mzMLFileReader.getSpectrumIdList();
		int nSpectra1 = 2;
		int nSpectra2 = 0;
		if (spectrumIds2 != null)
		{
			nSpectra2 = spectrumIds2.size();
		}
		else
		{
			System.out.println("TestMzMLFileReader::testGetSpectrum2(): mzMLFileReader.getSpectrumIdList() = null");
		}
		Assert.assertEquals(
			"getSpectrumIdList() should return the correct list size",
			nSpectra1, nSpectra2);
		Assert.assertEquals(
			"getSpectrumIdList().get(0) should return the correct String",
			spectrumIds1.get(0), spectrumIds2.get(0));
		Assert.assertEquals(
			"getSpectrumIdList().get(1) should return the correct String",
			spectrumIds1.get(1), spectrumIds2.get(1));
	}
}
