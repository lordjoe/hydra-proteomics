/*
 $Id: TestFilenameParseUtil.java 2086 2007-10-05 13:38:11Z olle $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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

import junit.framework.TestCase;

/**
 * This class tests FilenameParseUtil methods, that does not need
 * an input file to be tested. The methods tested here are defined
 * in FilenameParseUtil, if not specified otherwise.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2007-01-15 12:33:12Z $
 */
public class TestFilenameParseUtil
		extends TestCase
{
	private FilenameParseUtil filenameParseUtil;

	protected void setUp()
			throws Exception
	{
		/*
		 * Test-specific Initializations may be placed here. 
		 */
		filenameParseUtil = new FilenameParseUtil();
	}


	protected void tearDown()
			throws Exception
	{
	}


	/**
	 * Tests fetchPureBaseString(String inStr, String separationStr)
	 */
	public void testFetchPureBaseString()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String separationStr = new String("_");
		String testString1 = filenameParseUtil.fetchPureBaseString(testString, separationStr);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchPureBaseString() should return the pure base String, i.e. the part before the separation string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchResidueString(String inStr, String separationStr)
	 */
	public void testFetchResidueString()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String separationStr = new String("_");
		String testString1 = filenameParseUtil.fetchResidueString(testString, separationStr);
		String testString2 = new String("060306_2@A1.txt");
		assertEquals(
			"fetchResidueString() should return the residue String, i.e. the part after the separation string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchFirstNumCharIndex(String inStr)
	 */
	public void testFetchFirstNumCharIndex()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		int testNumCharIndex1 = filenameParseUtil.fetchFirstNumCharIndex(testString);
		int testNumCharIndex2 = 2;
		assertEquals(
			"fetchFirstNumCharIndex() should return the index of the first numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}

	
	/**
	 * Tests fetchFirstNumCharIndex(String inStr) 2
	 */
	public void testFetchFirstNumCharIndex2()
	{
		String testString = new String("FL_060306_2@A1.txt");
		int testNumCharIndex1 = filenameParseUtil.fetchFirstNumCharIndex(testString);
		int testNumCharIndex2 = 3;
		assertEquals(
			"fetchFirstNumCharIndex() should return the index of the first numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}


	/**
	 * Tests fetchLastNumCharIndex(String inStr)
	 */
	public void testFetchLastNumCharIndex()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		int testNumCharIndex1 = filenameParseUtil.fetchLastNumCharIndex(testString);
		int testNumCharIndex2 = 14;
		assertEquals(
			"fetchLastNumCharIndex() should return the index of the first numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}

	
	/**
	 * Tests fetchLastNumCharIndex(String inStr) 2
	 */
	public void testFetchLastNumCharIndex2()
	{
		String testString = new String("FL1_060306_2@A.txt");
		int testNumCharIndex1 = filenameParseUtil.fetchLastNumCharIndex(testString);
		int testNumCharIndex2 = 11;
		assertEquals(
			"fetchLastNumCharIndex() should return the index of the first numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}


	/**
	 * Tests fetchFirstNonNumCharIndex(String inStr)
	 */
	public void testFetchFirstNonNumCharIndex()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		int testNonNumCharIndex1 = filenameParseUtil.fetchFirstNonNumCharIndex(testString);
		int testNonNumCharIndex2 = 0;
		assertEquals(
			"fetchFirstNonNumCharIndex() should return the index of the first non-numeric character.",
			testNonNumCharIndex2, testNonNumCharIndex1);
	}

	
	/**
	 * Tests fetchFirstNonNumCharIndex(String inStr) 2
	 */
	public void testFetchFirstNonNumCharIndex2()
	{
		String testString = new String("10603062@A1.txt");
		int testNonNumCharIndex1 = filenameParseUtil.fetchFirstNonNumCharIndex(testString);
		int testNonNumCharIndex2 = 8;
		assertEquals(
			"fetchFirstNonNumCharIndex() should return the index of the first non-numeric character.",
			testNonNumCharIndex2, testNonNumCharIndex1);
	}


	/**
	 * Tests fetchLastNonNumCharIndex(String inStr)
	 */
	public void testFetchLastNonNumCharIndex()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		int testNumCharIndex1 = filenameParseUtil.fetchLastNonNumCharIndex(testString);
		int testNumCharIndex2 = 18;
		assertEquals(
			"fetchLastNonNumCharIndex() should return the index of the lastst non-numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}

	
	/**
	 * Tests fetchLastNonNumCharIndex(String inStr) 2
	 */
	public void testFetchLastNonNumCharIndex2()
	{
		String testString = new String("FL10060306020");
		int testNumCharIndex1 = filenameParseUtil.fetchLastNonNumCharIndex(testString);
		int testNumCharIndex2 = 1;
		assertEquals(
			"fetchLastNonNumCharIndex() should return the index of the last non-numeric character.",
			testNumCharIndex2, testNumCharIndex1);
	}


	/**
	 * Tests fetchBasename(String filename)
	 */
	public void testFetchBasename()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchBasename(testString);
		String testString2 = new String("FL1_060306_2@A1");
		assertEquals(
			"fetchBasename() should return the basename, i.e. the part before the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchBasename(String filename) 2
	 */
	public void testFetchBasename2()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchBasename(testString);
		String testString2 = new String("FL1_060306_2@A1");
		assertEquals(
			"fetchBasename() should return the basename, i.e. the part before the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchBasename(String filename) 3
	 */
	public void testFetchBasename3()
	{
		String testString = new String("FL1_060306_2@A1.tar.gz");
		String testString1 = filenameParseUtil.fetchBasename(testString);
		String testString2 = new String("FL1_060306_2@A1.tar");
		assertEquals(
			"fetchBasename() should return the basename, i.e. the part before the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureBasename(String filename)
	 */
	public void testFetchPureBasename()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchPureBasename(testString);
		String testString2 = new String("FL1_060306_2@A1");
		assertEquals(
			"fetchPureBasename() should return the pure basename, i.e. the part before the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureBasename(String filename) 2
	 */
	public void testFetchPureBasename2()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchPureBasename(testString);
		String testString2 = new String("FL1_060306_2@A1");
		assertEquals(
			"fetchPureBasename() should return the pure basename, i.e. the part before the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureBasename(String filename) 3
	 */
	public void testFetchPureBasename3()
	{
		String testString = new String("FL1_060306_2@A1.tar.gz");
		String testString1 = filenameParseUtil.fetchPureBasename(testString);
		String testString2 = new String("FL1_060306_2@A1");
		assertEquals(
			"fetchPureBasename() should return the pure basename, i.e. the part before the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchFileExtension(String filename)
	 */
	public void testFetchFileExtension()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchFileExtension(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchFileExtension() should return the file extension, i.e. the part after the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchFileExtension(String filename) 2
	 */
	public void testFetchFileExtension2()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchFileExtension(testString);
		String testString2 = new String("txt");
		assertEquals(
			"fetchFileExtension() should return the file extension, i.e. the part after the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchFileExtension(String filename) 3
	 */
	public void testFetchFileExtension3()
	{
		String testString = new String("FL1_060306_2@A1.tar.gz");
		String testString1 = filenameParseUtil.fetchFileExtension(testString);
		String testString2 = new String("tar.gz");
		assertEquals(
			"fetchFileExtension() should return the file extension, i.e. the part after the first dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureFileExtension(String filename)
	 */
	public void testFetchPureFileExtension()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchPureFileExtension(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchPureFileExtension() should return the pure file extension, i.e. the part after the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureFileExtension(String filename) 2
	 */
	public void testFetchPureFileExtension2()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchPureFileExtension(testString);
		String testString2 = new String("txt");
		assertEquals(
			"fetchPureFileExtension() should return the pure file extension, i.e. the part after the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureFileExtension(String filename) 3
	 */
	public void testFetchPureFileExtension3()
	{
		String testString = new String("FL1_060306_2@A1.tar.gz");
		String testString1 = filenameParseUtil.fetchPureFileExtension(testString);
		String testString2 = new String("gz");
		assertEquals(
			"fetchPureFileExtension() should return the pure file extension, i.e. the part after the last dot \".\".",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureNonPositionName(String filename)
	 */
	public void testFetchNonPositionName()
	{
		String testString = new String("FL1_060306_2");
		String testString1 = filenameParseUtil.fetchNonPositionName(testString);
		String testString2 = new String("FL1_060306_2");
		assertEquals(
			"fetchNonPositionName() should return the non-position basename, i.e. the part before the first \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureNonPositionName(String filename) 2
	 */
	public void testFetchNonPositionName2()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchNonPositionName(testString);
		String testString2 = new String("FL1_060306_2");
		assertEquals(
			"fetchNonPositionName() should return the non-position basename, i.e. the part before the first \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchPureNonPositionName(String filename) 3
	 */
	public void testFetchNonPositionName3()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchNonPositionName(testString);
		String testString2 = new String("FL1_060306_2");
		assertEquals(
			"fetchNonPositionName() should return the non-position basename, i.e. the part before the first \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetId(String filename)
	 */
	public void testFetchTargetId()
	{
		String testString = new String("FL1");
		String testString1 = filenameParseUtil.fetchTargetId(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetId() should return the target id, i.e. the part before the first \".\", \"_\", or \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetId(String filename) 2
	 */
	public void testFetchTargetId2()
	{
		String testString = new String("FL1.txt");
		String testString1 = filenameParseUtil.fetchTargetId(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetId() should return the target id, i.e. the part before the first \".\", \"_\", or \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetId(String filename) 3
	 */
	public void testFetchTargetId3()
	{
		String testString = new String("FL1_060306_2.txt");
		String testString1 = filenameParseUtil.fetchTargetId(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetId() should return the target id, i.e. the part before the first \".\", \"_\", or \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetId(String filename) 4
	 */
	public void testFetchTargetId4()
	{
		String testString = new String("FL1_060306_2@A1");
		String testString1 = filenameParseUtil.fetchTargetId(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetId() should return the target id, i.e. the part before the first \".\", \"_\", or \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetId(String filename) 5
	 */
	public void testFetchTargetId5()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchTargetId(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetId() should return the target id, i.e. the part before the first \".\", \"_\", or \"@\" character.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetIdFromSHWFilename(String filename)
	 */
	public void testFetchTargetIdFromSHWFilename()
	{
		String testString = new String("18115042FL1");
		String testString1 = filenameParseUtil.fetchTargetIdFromSHWFilename(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetIdFromSHWFilename() should return the target id, i.e. the part after the first \"18115042\" string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetIdFromSHWFilename(String filename) 2
	 */
	public void testFetchTargetIdFromSHWFilename2()
	{
		String testString = new String("18115042FL1.txt");
		String testString1 = filenameParseUtil.fetchTargetIdFromSHWFilename(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetIdFromSHWFilename() should return the target id, i.e. the part after the first \"18115042\" string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetIdFromSHWFilename(String filename) 3
	 */
	public void testFetchTargetIdFromSHWFilename3()
	{
		String testString = new String("18115042FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchTargetIdFromSHWFilename(testString);
		String testString2 = new String("FL1");
		assertEquals(
			"fetchTargetIdFromSHWFilename() should return the target id, i.e. the part after the first \"18115042\" string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenterFromTarget(String targetId)
	 */
	public void testFetchExperimenterFromTarget()
	{
		String testString = new String("FL1");
		String testString1 = filenameParseUtil.fetchExperimenterFromTarget(testString);
		String testString2 = new String("FL");
		assertEquals(
			"fetchExperimenterFromTarget() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenterFromTarget(String targetId) 2
	 */
	public void testFetchExperimenterFromTarget2()
	{
		String testString = new String("FL");
		String testString1 = filenameParseUtil.fetchExperimenterFromTarget(testString);
		String testString2 = new String("FL");
		assertEquals(
			"fetchExperimenterFromTarget() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenterFromTarget(String targetId) 3
	 */
	public void testFetchExperimenterFromTarget3()
	{
		String testString = new String("123");
		String testString1 = filenameParseUtil.fetchExperimenterFromTarget(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchExperimenterFromTarget() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumberFromTarget(String targetId)
	 */
	public void testFetchTargetNumberFromTarget()
	{
		String testString = new String("FL1");
		String testString1 = filenameParseUtil.fetchTargetNumberFromTarget(testString);
		String testString2 = new String("1");
		assertEquals(
			"fetchTargetNumberFromTarget() should return the target number, i.e. the part starting with numerical characters in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumberFromTarget(String targetId) 2
	 */
	public void testFetchTargetNumberFromTarget2()
	{
		String testString = new String("FL");
		String testString1 = filenameParseUtil.fetchTargetNumberFromTarget(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchTargetNumberFromTarget() should return the target number, i.e. the part starting with numerical characters in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumberFromTarget(String targetId) 3
	 */
	public void testFetchTargetNumberFromTarget3()
	{
		String testString = new String("123");
		String testString1 = filenameParseUtil.fetchTargetNumberFromTarget(testString);
		String testString2 = new String("123");
		assertEquals(
			"fetchTargetNumberFromTarget() should return the target number, i.e. the part starting with numerical characters in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenter(String filename)
	 */
	public void testFetchExperimenter()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchExperimenter(testString);
		String testString2 = new String("FL");
		assertEquals(
			"fetchExperimenter() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenter(String filename) 2
	 */
	public void testFetchExperimenter2()
	{
		String testString = new String("FL1_060306.txt");
		String testString1 = filenameParseUtil.fetchExperimenter(testString);
		String testString2 = new String("FL");
		assertEquals(
			"fetchExperimenter() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchExperimenter(String filename) 3
	 */
	public void testFetchExperimenter3()
	{
		String testString = new String("1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchExperimenter(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchExperimenter() should return the experimenter, i.e. the part before any number in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumber(String filename)
	 */
	public void testFetchTargetNumber()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchTargetNumber(testString);
		String testString2 = new String("1");
		assertEquals(
			"fetchTargetNumber() should return the target number, i.e. the part starting with a numerical character in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumber(String filename) 2
	 */
	public void testFetchTargetNumber2()
	{
		String testString = new String("FL1_060306.txt");
		String testString1 = filenameParseUtil.fetchTargetNumber(testString);
		String testString2 = new String("1");
		assertEquals(
			"fetchTargetNumber() should return the target number, i.e. the part starting with a numerical character in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchTargetNumber(String filename) 3
	 */
	public void testFetchTargetNumber3()
	{
		String testString = new String("123_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchTargetNumber(testString);
		String testString2 = new String("123");
		assertEquals(
			"fetchTargetNumber() should return the target number, i.e. the part starting with a numerical character in the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchDate(String filename)
	 */
	public void testFetchDate()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchDate(testString);
		String testString2 = new String("060306");
		assertEquals(
			"fetchDate() should return the date, i.e. the first part after the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchDate(String filename) 2
	 */
	public void testFetchDate2()
	{
		String testString = new String("FL1_060306_2.txt");
		String testString1 = filenameParseUtil.fetchDate(testString);
		String testString2 = new String("060306");
		assertEquals(
			"fetchDate() should return the date, i.e. the first part after the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchDate(String filename) 3
	 */
	public void testFetchDate3()
	{
		String testString = new String("123_060306");
		String testString1 = filenameParseUtil.fetchDate(testString);
		String testString2 = new String("060306");
		assertEquals(
			"fetchDate() should return the date, i.e. the first part after the target id string.",
			testString2, testString1);
	}


	/**
	 * Tests fetchCounter(String filename)
	 */
	public void testFetchCounter()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchCounter(testString);
		String testString2 = new String("2");
		assertEquals(
			"fetchCounter() should return the counter, i.e. the first part after the target id and date strings.",
			testString2, testString1);
	}


	/**
	 * Tests fetchCounter(String filename) 2
	 */
	public void testFetchCounter2()
	{
		String testString = new String("FL1_060306_2");
		String testString1 = filenameParseUtil.fetchCounter(testString);
		String testString2 = new String("2");
		assertEquals(
			"fetchCounter() should return the counter, i.e. the first part after the target id and date strings.",
			testString2, testString1);
	}


	/**
	 * Tests fetchCounter(String filename) 3
	 */
	public void testFetchCounter3()
	{
		String testString = new String("FL1_060306@A1.txt");
		String testString1 = filenameParseUtil.fetchCounter(testString);
		String testString2 = new String("");
		assertEquals(
			"fetchCounter() should return the counter, i.e. the first part after the target id and date strings.",
			testString2, testString1);
	}


	/**
	 * Tests fetchPlatePosition(String filename)
	 */
	public void testFetchPlatePosition()
	{
		String testString = new String("FL1_060306_2@A1.txt");
		String testString1 = filenameParseUtil.fetchPlatePosition(testString);
		String testString2 = new String("A1");
		assertEquals(
			"fetchPlatePosition() should return the plate position, i.e. the part after an \"@\" character, or the last part of the pure basename separated by \"_\" characters.",
			testString2, testString1);
	}


	/**
	 * Tests fetchPlatePosition(String filename) 2
	 */
	public void testFetchPlatePosition2()
	{
		String testString = new String("FL1_060306_A1.txt");
		String testString1 = filenameParseUtil.fetchPlatePosition(testString);
		String testString2 = new String("A1");
		assertEquals(
			"fetchPlatePosition() should return the plate position, i.e. the part after an \"@\" character, or the last part of the pure basename separated by \"_\" characters.",
			testString2, testString1);
	}


	/**
	 * Tests fetchPlatePosition(String filename) 3
	 */
	public void testFetchPlatePosition3()
	{
		String testString = new String("FL1_060306.txt");
		String testString1 = filenameParseUtil.fetchPlatePosition(testString);
		String testString2 = new String("060306");
		assertEquals(
			"fetchPlatePosition() should return the plate position, i.e. the part after an \"@\" character, or the last part of the pure basename separated by \"_\" characters.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation()
	{
		String testString = new String("");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("");
		assertEquals(
			"convertWellNotation() should return null for an empty string as the well position.",
			null, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation2()
	{
		String testString = new String("C12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation3()
	{
		String testString = new String("c12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation4()
	{
		String testString = new String("C-12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation5()
	{
		String testString = new String("c-12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation6()
	{
		String testString = new String("C,12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation7()
	{
		String testString = new String("c,12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation8()
	{
		String testString = new String("12C");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation9()
	{
		String testString = new String("12c");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation10()
	{
		String testString = new String("12-C");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation11()
	{
		String testString = new String("12-c");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation12()
	{
		String testString = new String("12,C");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation13()
	{
		String testString = new String("12,c");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("C12");
		assertEquals(
			"convertWellNotation() should return the well position in \"C12\" type notation.",
			testString2, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation14()
	{
		String testString = new String("CB");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("");
		assertEquals(
			"convertWellNotation() should return null for a well position without number.",
			null, testString1);
	}


	/**
	 * Tests convertWellNotation(String wellname)
	 */
	public void testConvertWellNotation15()
	{
		String testString = new String("12");
		String testString1 = filenameParseUtil.convertWellNotation(testString);
		String testString2 = new String("");
		assertEquals(
			"convertWellNotation() should return null for a well position without letter.",
			null, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation1a()
	{
		String testPrototypeString = new String("C6");
		String testString = new String("");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for an empty string as the well position.",
			null, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation1b()
	{
		String testPrototypeString = new String("");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for an empty string as the prototype position.",
			null, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation2()
	{
		String testPrototypeString = new String("C6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("A12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"A12\" type notation for prototype notation \"C6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation3()
	{
		String testPrototypeString = new String("c6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("a12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"a12\" type notation for prototype notation \"c6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation4()
	{
		String testPrototypeString = new String("C-6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("A-12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"A-12\" type notation for prototype notation \"C-6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation5()
	{
		String testPrototypeString = new String("c-6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("a-12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"a-12\" type notation for prototype notation \"c-6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation6()
	{
		String testPrototypeString = new String("C,6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("A,12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"A,12\" type notation for prototype notation \"C,6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation7()
	{
		String testPrototypeString = new String("c,6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("a,12");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"a,12\" type notation for prototype notation \"c,6\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation8()
	{
		String testPrototypeString = new String("6C");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12A");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12A\" type notation for prototype notation \"6C\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation9()
	{
		String testPrototypeString = new String("6c");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12a");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12a\" type notation for prototype notation \"6c\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation10()
	{
		String testPrototypeString = new String("6-C");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12-A");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12-A\" type notation for prototype notation \"6-C\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation11()
	{
		String testPrototypeString = new String("6-c");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12-a");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12-a\" type notation for prototype notation \"6-c\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation12()
	{
		String testPrototypeString = new String("6,C");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12,A");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12,A\" type notation for prototype notation \"6,C\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation13()
	{
		String testPrototypeString = new String("6,c");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("12,a");
		assertEquals(
			"convertToLocalWellNotation() should return the well position in \"12,a\" type notation for prototype notation \"6,c\".",
			testString2, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation14a()
	{
		String testPrototypeString = new String("C6");
		String testString = new String("CB");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for a well position without number.",
			null, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation14b()
	{
		String testPrototypeString = new String("CB");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for a prototype position without number.",
			null, testString1);
	}


	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation15a()
	{
		String testPrototypeString = new String("C6");
		String testString = new String("12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for a well position without letter.",
			null, testString1);
	}
	/**
	 * Tests convertToLocalWellNotation(String wellname)
	 */
	public void testConvertToLocalWellNotation15b()
	{
		String testPrototypeString = new String("6");
		String testString = new String("A12");
		String testString1 = filenameParseUtil.convertToLocalWellNotation(testString, testPrototypeString);
		String testString2 = new String("");
		assertEquals(
			"convertToLocalWellNotation() should return null for a prototype position without letter.",
			null, testString1);
	}
}
