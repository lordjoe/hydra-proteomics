/*
	$Id: TestXMLValidator.java 1922 2007-09-05 12:22:41Z fredrik $

	Copyright (C) 2006 Gregory Vincic, Olle Mansson
	Copyright (C) 2007 Olle Mansson

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

import org.proteios.PropertiesFile;

import org.xml.sax.SAXException;

import java.net.URL;

import org.junit.*;

/**
	This class tests XMLValidator items.

	The methods tested here are defined in XMLValidator,
	if not specified otherwise.

	@author Olle
	@version 2.0
	@proteios.modified $Date: 2006-06-30 12:33:12Z $
*/
public class TestXMLValidator
 {

	private String testDataDirURLStr = null;
	private URL testDataDirURL = null;
	private String testDataDir = null;

     @Before
	public void setUp()
		throws Exception
	{
		PropertiesFile pf = new PropertiesFile();
		/*
		 * Test-specific Initializations may be placed here.
		 */
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
		if (testDataDirURL != null && !testDataDirURL.toString().equals("") && testDataDirURL.toString().startsWith("file:"))
		{
			/*
			 * Store full local path for use im tests
			 * taking a local path as argument. 
			 */
			testDataDir = testDataDirURL.getPath();
		}
	}

	/**
		Tests validate(String xsdFname, String xmlFname)
	*/
	public void testValidateOk()
	{
		String testXsdFilePath = new String(testDataDir + "/personal.xsd");
		String testXmlFilePath = new String(testDataDir + "/personal-schema.xml");
		try
		{
			boolean testResult = XMLValidator.validate(testXsdFilePath, testXmlFilePath);

			Assert.assertEquals("validate() should return true for a valid XSD/XML file pair", (boolean)true, testResult);
		} catch (SAXException e) {

		}
	}

	/**
		Tests validate(String xsdFname, String xmlFname)
	*/
	public void testValidateNotOk()
	{
		String testXsdFilePath = new String(testDataDir + "/personal.xsd");
		String testXmlFilePath = new String(testDataDir + "/personal-wrong-schema.xml");
		try
		{
			boolean testResult = XMLValidator.validate(testXsdFilePath, testXmlFilePath);

			Assert.assertEquals("validate() should return false for an invalid XSD/XML file pair", (boolean)false, testResult);
		} catch (SAXException e) {
			
		}
	}
}

