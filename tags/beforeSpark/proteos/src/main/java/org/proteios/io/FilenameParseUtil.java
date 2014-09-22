/*
 $Id: FilenameParseUtil.java 3325 2009-06-24 10:39:32Z olle $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class supports simple extraction of data from filenames.
 * 
 * Used e.g. to parse the data in a gel mass spectrometry filename.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2007-01-15 13:37:53 +0100 (Thu, 09 Nov 2006) $
 */
public class FilenameParseUtil
{
	/**
	 * Logger used. Used to log specific events.
	 */
	protected static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");


	/**
	 * Default constructor.
	 */
	public FilenameParseUtil()
	{}


	/**
	 * Fetches the resulting string after parsing an input
	 * string with an input regex pattern.
	 * 
	 * @param inStr String the input string to parse.
	 * @param regexStr String The regex pattern string to use for parsing.
	 * @return String The resulting string.
	 */
	public String fetchRegexResult(String inStr, String regexStr)
	{
		log.debug("inStr = \"" + inStr + "\" regexStr = \"" + regexStr + "\"");
		String captureStr = null;
		Pattern pattern = Pattern.compile(regexStr);
		Matcher matcher = pattern.matcher(inStr);
		boolean isvalid = matcher.matches();
		if (isvalid)
		{
			log.debug("Group 0 match:" + matcher.group(0));
			captureStr = matcher.group(1);
			log.debug("Capture pattern of \"" + inStr + "\": " + captureStr);
		}
		else
		{
			log.error("String \"" + inStr + "\" not matching pattern " + regexStr);
		}
		// Return result string
		return captureStr;
	}


	/**
	 * Fetches the first or last part of an input string, after it
	 * has been cut in two pieces by the first or last occurrence
	 * of a separation string.
	 * 
	 * @param inStr String the input string to parse.
	 * @param separationStr String the separation string.
	 * @param firstCut boolean true for cut is at first occurrence of the separation string, else last.
	 * @param firstPart boolean true for first part after cut at separation string, else last.
	 * @return String the first or last part of the cut string.
	 */
	public String fetchPartAfterEndCut(String inStr, String separationStr, boolean firstCut, boolean firstPart)
	{
		log.debug("FilenameParseUtil:: fetchPartAfterEndCut(): inStr = \"" + inStr + "\" separationStr = \"" + separationStr + "\" firstCut = " + firstCut + " firstPart = " + firstPart);
		String firstPartStr = new String(inStr);
		String lastPartStr = new String("");
		int index = -1;
		if (firstCut)
		{
			index = inStr.indexOf(separationStr);
		}
		else
		{
			index = inStr.lastIndexOf(separationStr);
		}
		if (index >= 0)
		{
			/*
			 * String inStr contains at least one
			 * occurrence of separationStr.
			 */
			firstPartStr = inStr.substring(0, index);
			if (index < inStr.length()) {
				lastPartStr = inStr.substring(index + separationStr.length());
			}
		}
		log.debug("FilenameParseUtil:: fetchPartAfterEndCut(): firstPartStr = \"" + firstPartStr + "\"");
		log.debug("FilenameParseUtil:: fetchPartAfterEndCut(): lastPartStr = \"" + lastPartStr + "\"");
		/*
		 * Return first or last string part after cut.
		 */
		if (firstPart)
		{
			return firstPartStr;
		}
		return lastPartStr;
	}


	/**
	 * Fetches the pure base string from an input string given a
	 * separation string. The pure base string is the substring before
	 * the first occurrence of the separation string, i.e. input string
	 * "FL1_060306_2@A1.txt" and separation string "_" will give "FL1".
	 * 
	 * @param inStr String the input string to parse.
	 * @param separationStr String the separation string.
	 * @return String the pure base string.
	 */
	public String fetchPureBaseString(String inStr, String separationStr)
	{
		log.debug("FilenameParseUtil:: fetchPureBaseString(): inStr = \"" + inStr + "\" separationStr = \"" + separationStr + "\"");
		boolean firstCut = true;
		boolean firstPart = true;
		String pureBaseStr = fetchPartAfterEndCut(inStr, separationStr, firstCut, firstPart);
		log.debug("FilenameParseUtil:: fetchPureBaseString(): pureBaseStr = \"" + pureBaseStr + "\"");
		/*
		 * Return pure base string.
		 */
		return pureBaseStr;
	}


	/**
	 * Fetches the base string from an input string given a
	 * separation string. The base string is the substring before
	 * the last occurrence of the separation string, i.e. input string
	 * "FL1_060306_2@A1.txt" and separation string "_" will give "FL1_060306".
	 * 
	 * @param inStr String the input string to parse.
	 * @param separationStr String the separation string.
	 * @return String the base string.
	 */
	public String fetchBaseString(String inStr, String separationStr)
	{
		log.debug("FilenameParseUtil:: fetchBaseString(): inStr = \"" + inStr + "\" separationStr = \"" + separationStr + "\"");
		boolean firstCut = false;
		boolean firstPart = true;
		String baseStr = fetchPartAfterEndCut(inStr, separationStr, firstCut, firstPart);
		log.debug("FilenameParseUtil:: fetchBaseString(): baseStr = \"" + baseStr + "\"");
		/*
		 * Return base string.
		 */
		return baseStr;
	}


	/**
	 * Fetches the pure residue string from an input string given a
	 * separation string. The pure residue string is the substring after
	 * the last occurrence of the separation string, i.e. input string
	 * "FL1_060306_2@A1.txt" and separation string "_" will give "2@A1.txt".
	 * 
	 * @param inStr String the input string to parse.
	 * @param separationStr String the separation string.
	 * @return String the pure residue string.
	 */
	public String fetchPureResidueString(String inStr, String separationStr)
	{
		log.debug("FilenameParseUtil:: fetchPureResidueString(): inStr = \"" + inStr + "\" separationStr = \"" + separationStr + "\"");
		boolean firstCut = false;
		boolean firstPart = false;
		String pureResidueStr = fetchPartAfterEndCut(inStr, separationStr, firstCut, firstPart);
		log.debug("FilenameParseUtil:: fetchPureResidueString(): pureResidueStr = \"" + pureResidueStr + "\"");
		/*
		 * Return pure residue string.
		 */
		return pureResidueStr;
	}


	/**
	 * Fetches the residue string from an input string given a
	 * separation string. The residue string is the substring after
	 * the first occurrence of the separation string, i.e. input string
	 * "FL1_060306_2@A1.txt" and separation string "_" will give "060306_2@A1.txt".
	 * 
	 * @param inStr String the input string to parse.
	 * @param separationStr String the separation string.
	 * @return String the residue string.
	 */
	public String fetchResidueString(String inStr, String separationStr)
	{
		log.debug("FilenameParseUtil:: fetchResidueString(): inStr = \"" + inStr + "\" separationStr = \"" + separationStr + "\"");
		boolean firstCut = true;
		boolean firstPart = false;
		String residueStr = fetchPartAfterEndCut(inStr, separationStr, firstCut, firstPart);
		log.debug("FilenameParseUtil:: fetchResidueString(): residueStr = \"" + residueStr + "\"");
		/*
		 * Return residue string.
		 */
		return residueStr;
	}


	/**
	 * Fetches the position index of the first numerical character
	 * in a string. Returns -1 if no numerical character is found.
	 * 
	 * @param inStr String the string to parse.
	 * @return int index of the first numerical character, or -1 if none.
	 */
	public int fetchFirstNumCharIndex(String inStr)
	{
		log.debug("FilenameParseUtil:: fetchFirstNumCharString(): inStr = \"" + inStr + "\"");
		/*
		 * Find position of first (if any)
		 * numerical character in input string.
		 */
		int charIndex = -1;
		boolean finished = false;
		for (int i = 0; i < inStr.length() && !finished; i++)
		{
			char testChar = inStr.charAt(i);
			if (Character.isDigit(testChar)) {
				charIndex = i;
				finished = true;
			}
		}
		log.debug("FilenameParseUtil:: fetchFirstNumCharIndex(): charIndex = " + charIndex);
		/*
		 * Return position index of first numerical character.
		 */
		return charIndex;
	}


	/**
	 * Fetches the position index of the last numerical character
	 * in a string. Returns -1 if no numerical character is found.
	 * 
	 * @param inStr String the string to parse.
	 * @return int index of the last numerical character, or -1 if none.
	 */
	public int fetchLastNumCharIndex(String inStr)
	{
		log.debug("FilenameParseUtil:: fetchLastNumCharString(): inStr = \"" + inStr + "\"");
		/*
		 * Find position of last (if any)
		 * numerical character in input string.
		 */
		int charIndex = -1;
		boolean finished = false;
		for (int i = (inStr.length() - 1); i >= 0 && !finished; i--)
		{
			char testChar = inStr.charAt(i);
			if (Character.isDigit(testChar)) {
				charIndex = i;
				finished = true;
			}
		}
		log.debug("FilenameParseUtil:: fetchLastNumCharIndex(): charIndex = " + charIndex);
		/*
		 * Return position index of last numerical character.
		 */
		return charIndex;
	}


	/**
	 * Fetches the position index of the first non-numerical character
	 * in a string. Returns -1 if no non-numerical character is found.
	 * 
	 * @param inStr String the string to parse.
	 * @return int index of the first non-numerical character, or -1 if none.
	 */
	public int fetchFirstNonNumCharIndex(String inStr)
	{
		log.debug("FilenameParseUtil:: fetchFirstNonNumCharString(): inStr = \"" + inStr + "\"");
		/*
		 * Find position of first (if any)
		 * non-numerical character in input string.
		 */
		int charIndex = -1;
		boolean finished = false;
		for (int i = 0; i < inStr.length() && !finished; i++)
		{
			char testChar = inStr.charAt(i);
			if (!Character.isDigit(testChar)) {
				charIndex = i;
				finished = true;
			}
		}
		/*
		 * Return position index of first non-numerical character.
		 */
		log.debug("FilenameParseUtil:: fetchFirstNonNumCharIndex(): charIndex = " + charIndex);
		return charIndex;
	}


	/**
	 * Fetches the position index of the last non-numerical character
	 * in a string. Returns -1 if no non-numerical character is found.
	 * 
	 * @param inStr String the string to parse.
	 * @return int index of the last non-numerical character, or -1 if none.
	 */
	public int fetchLastNonNumCharIndex(String inStr)
	{
		log.debug("FilenameParseUtil:: fetchLastNonNumCharString(): inStr = \"" + inStr + "\"");
		/*
		 * Find position of last (if any)
		 * non-numerical character in input string.
		 */
		int charIndex = -1;
		boolean finished = false;
		for (int i = (inStr.length() - 1); i >= 0 && !finished; i--)
		{
			char testChar = inStr.charAt(i);
			if (!Character.isDigit(testChar)) {
				charIndex = i;
				finished = true;
			}
		}
		/*
		 * Return position index of last non-numerical character.
		 */
		log.debug("FilenameParseUtil:: fetchLastNonNumCharIndex(): charIndex = " + charIndex);
		return charIndex;
	}


	/**
	 * Fetches the basename from a filename, i.e. part of the
	 * filename before an optional file extension separated by
	 * a dot ".". If several dots exist in the filename, all but
	 * the last are considered part of the basename, e.g. filename
	 * "example.tar.gz" has basename "example.tar".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the basename.
	 * if found, else (String)null.
	 */
	public String fetchBasename(String filename)
	{
		/*
		 * Extract basename and optional fileextension,
		 * including ".".
		 */
		log.debug("FilenameParseUtil:: fetchBasename(): filename = \"" + filename + "\"");
		String basename = fetchBaseString(filename, ".");
		log.debug("FilenameParseUtil:: fetchBasename(): basename = \"" + basename + "\"");
		/*
		 * Return basename.
		 */
		return basename;
	}


	/**
	 * Fetches the pure basename from a filename, i.e. part of the
	 * filename before any optional dots ".". If several dots exist
	 * in the filename, none are considered part of the pure basename,
	 * e.g. filename "example.tar.gz" has pure basename "example".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the pure basename.
	 * if found, else (String)null.
	 */
	public String fetchPureBasename(String filename)
	{
		/*
		 * Extract pure basename.
		 */
		log.debug("FilenameParseUtil:: fetchPureBasename(): filename = \"" + filename + "\"");
		String pureBasename = fetchPureBaseString(filename, ".");
		log.debug("FilenameParseUtil:: fetchPureBasename(): pureBasename = \"" + pureBasename + "\"");
		/*
		 * Return pure basename.
		 */
		return pureBasename;
	}


	/**
	 * Fetches the file extension from a filename, i.e. part of the
	 * filename after an optional separation dot ".". If several dots
	 * exist in the filename, all but the first are considered part of
	 * the file extension, e.g. filename "example.tar.gz" has file
	 * extension "tar.gz".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the file extension.
	 * if found, else (String)null.
	 */
	public String fetchFileExtension(String filename)
	{
		/*
		 * Extract optional file extension, excluding ".".
		 */
		log.debug("FilenameParseUtil:: fetchFileExtension(): filename = \"" + filename + "\"");
		String fileextension = fetchResidueString(filename, ".");
		log.debug("FilenameParseUtil:: fetchFileExtension(): fileextension = \"" + fileextension + "\"");
		/*
		 * Return file extension.
		 */
		return fileextension;
	}


	/**
	 * Fetches the pure file extension from a filename, i.e. part of the
	 * filename after any optional dots ".". If several dots exist
	 * in the filename, none are considered part of the pure file extension,
	 * e.g. filename "example.tar.gz" has pure file extension "gz".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the pure file extension.
	 * if found, else (String)null.
	 */
	public String fetchPureFileExtension(String filename)
	{
		/*
		 * Extract pure file extension.
		 */
		log.debug("FilenameParseUtil:: fetchPureFileExtension(): filename = \"" + filename + "\"");
		String pureFileextension = fetchPureResidueString(filename, ".");
		log.debug("FilenameParseUtil:: fetchPureFileExtension(): pureFileextension = \"" + pureFileextension + "\"");
		/*
		 * Return pure file extension.
		 */
		return pureFileextension;
	}


	/**
	 * Fetches the pure basename excluding plate position from a filename,
	 * i.e. part of the filename before any optional dot "." and "@" characters,
	 * e.g. filename "FL1_060306_2@A1.txt" will give "FL1_060306_1".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the pure basename excluding plate position.
	 */
	public String fetchNonPositionName(String filename)
	{
		/*
		 * Extract basename and optional fileextension,
		 * including ".".
		 */
		log.debug("FilenameParseUtil:: fetchNonPositionName(): filename = \"" + filename + "\"");
		String basename = fetchPureBasename(filename);
		String nonPositionName = fetchPureBaseString(basename, "@");
		log.debug("FilenameParseUtil:: fetchNonPositionName(): basename = \"" + basename + "\"");
		log.debug("FilenameParseUtil:: fetchNonPositionName(): nonPositionName = \"" + nonPositionName + "\"");
		/*
		 * Return non-position name.
		 */
		return nonPositionName;
	}


	/**
	 * Fetches the target id from a filename, i.e. part of the filename
	 * before any optional underscore "_", dot ".", and "@" characters,
	 * e.g. filename "FL1_060306_2@A1.txt" will give "FL1".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the target id.
	 */
	public String fetchTargetId(String filename)
	{
		/*
		 * Extract basename preceding and "@" and "." characters.
		 */
		log.debug("FilenameParseUtil:: fetchTargetId(): filename = \"" + filename + "\"");
		String basename = fetchNonPositionName(filename);
		String targetStr = fetchPureBaseString(basename, "_");
		log.debug("FilenameParseUtil:: fetchTargetId(): targetStr = \"" + targetStr + "\"");
		/*
		 * Return target id string.
		 */
		return targetStr;
	}

	
	/**
	 * Fetches the target id from a Spot Handling Worksatation (SHW) filename,
	 * i.e. part of the filename after string "18115042" and before any optional
	 * underscore "_", dot ".", and "@" characters, e.g. filename "18115042FL1"
	 * will give "FL1".
	 * 
	 * @param filename String the SHW filename to parse.
	 * @return String the target id.
	 */
	public String fetchTargetIdFromSHWFilename(String filename)
	{
		/*
		 * Extract basename preceding and "@" and "." characters.
		 */
		log.debug("FilenameParseUtil:: fetchTargetIdFromSHWFilename(): filename = \"" + filename + "\"");
		String basename = fetchNonPositionName(filename);
		String residueStr = fetchResidueString(basename, "18115042");
		String targetStr = fetchPureBaseString(residueStr, "_");
		log.debug("FilenameParseUtil:: fetchTargetIdFromSHWFilename(): targetStr = \"" + targetStr + "\"");
		/*
		 * Return target id string.
		 */
		return targetStr;
	}

	
	/**
	 * Fetches the experimenter from a target id, i.e. part of the
	 * target id before any number characters, e.g. target id "FL1"
	 * will give experimenter "FL".
	 * 
	 * @param targetId String the target id to parse.
	 * @return String the experimenter string.
	 */
	public String fetchExperimenterFromTarget(String targetId)
	{
		log.debug("FilenameParseUtil:: fetchExperimenterFromTarget(String targetId): targetId = \"" + targetId + "\"");
		String experimenterStr = new String(targetId);
		String targetNumStr = new String("");
		/*
		 * Find position of first (if any)
		 * numerical character in target id.
		 */
		int numCharIndex = fetchFirstNumCharIndex(targetId);
		if (numCharIndex >= 0)
		{
			/*
			 * String basename contains at least one numerical character.
			 */
			experimenterStr = targetId.substring(0, numCharIndex);
			if (numCharIndex < targetId.length()) {
				targetNumStr = targetId.substring(numCharIndex);
			}
		}
		log.debug("FilenameParseUtil:: fetchExperimenterFromTarget(): experimenterStr = \"" + experimenterStr + "\"");
		log.debug("FilenameParseUtil:: fetchExperimenterFromTarget(): targetNumStr = \"" + targetNumStr + "\"");
		/*
		 * Return experimenter string.
		 */
		return experimenterStr;
	}


	/**
	 * Fetches the target number from a target id, i.e. part of the
	 * target id starting with a number character, e.g. target id "FL1"
	 * will give target number "1".
	 * 
	 * @param targetId String the target id to parse.
	 * @return String the target number string.
	 */
	public String fetchTargetNumberFromTarget(String targetId)
	{
		log.debug("FilenameParseUtil:: fetchTargetNumberFromTarget(String targetId): targetId = \"" + targetId + "\"");
		String experimenterStr = new String(targetId);
		String targetNumStr = new String("");
		/*
		 * Find position of first (if any)
		 * numerical character in target id.
		 */
		int numCharIndex = fetchFirstNumCharIndex(targetId);
		if (numCharIndex >= 0)
		{
			/*
			 * String basename contains at least one "_".
			 */
			experimenterStr = targetId.substring(0, numCharIndex);
			if (numCharIndex < targetId.length()) {
				targetNumStr = targetId.substring(numCharIndex);
			}
		}
		log.debug("FilenameParseUtil:: fetchTargetNumberFromTarget(): experimenterStr = \"" + experimenterStr + "\"");
		log.debug("FilenameParseUtil:: fetchTargetNumberFromTarget(): targetNumStr = \"" + targetNumStr + "\"");
		/*
		 * Return target number string.
		 */
		return targetNumStr;
	}

	/**
	 * Fetches the experimenter from filename, i.e. part of the
	 * target id before any number characters, e.g. filename
	 * "FL1_060306_2@A1.txt" will give experimenter "FL".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the experimenter string.
	 */
	public String fetchExperimenter(String filename)
	{
		log.debug("FilenameParseUtil:: fetchExperimenter: filename = \"" + filename + "\"");
		String targetId = fetchTargetId(filename);
		String experimenterStr = fetchExperimenterFromTarget(targetId);
		log.debug("FilenameParseUtil:: fetchExperimenter(): experimenterStr = \"" + experimenterStr + "\"");
		/*
		 * Return experimenter string.
		 */
		return experimenterStr;
	}


	/**
	 * Fetches the target number from filename, i.e. part of the
	 * target id starting with a number character, e.g. filename
	 * "FL1_060306_2@A1.txt" will give target number "1".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the target number string.
	 */
	public String fetchTargetNumber(String filename)
	{
		log.debug("FilenameParseUtil:: fetchTargetNumber(): filename = \"" + filename + "\"");
		String targetId = fetchTargetId(filename);
		String targetNumStr = fetchTargetNumberFromTarget(targetId);
		log.debug("FilenameParseUtil:: fetchTargetNumber(): targetNumStr = \"" + targetNumStr + "\"");
		/*
		 * Return target number string.
		 */
		return targetNumStr;
	}


	/**
	 * Fetches the date from filename, i.e. part of the
	 * filename after the target id starting with a number character,
	 * e.g. filename "FL1_060306_2@A1.txt" will give date "060306".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the date string.
	 */
	public String fetchDate(String filename)
	{
		log.debug("FilenameParseUtil:: fetchDate(): filename = \"" + filename + "\"");
		String basename = fetchNonPositionName(filename);
		String residueStr = fetchResidueString(basename, "_");
		String dateStr = fetchPureBaseString(residueStr, "_");
		log.debug("FilenameParseUtil:: fetchDate(): dateStr = \"" + dateStr + "\"");
		/*
		 * Return date string.
		 */
		return dateStr;
	}


	/**
	 * Fetches the counter string from filename, i.e. part of the
	 * filename after the target id and date, excluding the position,
	 * e.g. filename "FL1_060306_2@A1.txt" will give counter "2".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the counter string.
	 */
	public String fetchCounter(String filename)
	{
		log.debug("FilenameParseUtil:: fetchCounter(): filename = \"" + filename + "\"");
		String basename = fetchNonPositionName(filename);
		String residueStr = fetchResidueString(basename, "_");
		String counterStr = fetchResidueString(residueStr, "_");
		log.debug("FilenameParseUtil:: fetchCounter(): counterStr = \"" + counterStr + "\"");
		/*
		 * Return counter string.
		 */
		return counterStr;
	}

	
	/**
	 * Fetches the plate position from filename, i.e. part of
	 * the basename after an "@" character, e.g. filename
	 * "FL1_060306_2@A1.txt" will give "A1".
	 * 
	 * @param filename String the filename to parse.
	 * @return String the plate position string.
	 */
	public String fetchPlatePosition(String filename)
	{
		log.debug("FilenameParseUtil:: fetchPlatePosition(): filename = \"" + filename + "\"");
		String positionStr = new String("");
		String basename = fetchPureBasename(filename);
		if (basename.indexOf("@") >= 0)
		{
			/*
			 * Filename contains "@" character, i.e. Maldi-TOF file.
			 * Example: "FL1_060306_2@A1.txt" with plate position "A1".
			 */
			positionStr = fetchResidueString(basename, "@");
			/* Remove trailing _pepex */
			if (positionStr.contains("_"))
			{
				positionStr=positionStr.substring(0,positionStr.indexOf("_"));
			}
		}
		else
		{
			/*
			 * Filename contains no "@" character, i.e. LC-MS file.
			 * Example: "FL1_060306_A1.txt" with plate position "A1".
			 * The plate position string is part after last
			 * occurrence of "_" (should also work if only one "_").
			 * We should allow names like '(.*)_(\w+)\.\w+'
			 */
			positionStr = fetchPureResidueString(basename, "_");
		}
		log.debug("FilenameParseUtil:: fetchPlatePosition(): positionStr = \"" + positionStr + "\"");
		/*
		 * Return counter string.
		 */
		return positionStr;
	}


	/**
	 * Converts a well position to standard notation of type "A12",
	 * where the uppercase letter notates the column and the
	 * number the row. Accepts well positions notations of
	 * the following types, where 'A' or 'a' is a single upper-
	 * or lowercase letter describing the row, and "12" a number
	 * describing the column (no leading zero for numbers < 10):
	 * "A12", "a12", "A-12", "a-12", "A,12", "a,12",
	 * "12-A", "12-a", "12,A", "12,a".
	 * 
	 * @param wellname String the well position notation to convert.
	 * @return String the well position string in notation type "A12".
	 */
	public String convertWellNotation(String wellname)
	{
		log.debug("FilenameParseUtil:: convertWellNotation(): wellname = \"" + wellname + "\"");
		/*
		 * Return null if wellname is empty.
		 */
		if (wellname == null || wellname.length() == 0)
		{
			return null;
		}
		String str1 = new String("");
		String str2 = new String("");
		if (wellname.indexOf("-") >= 0)
		{
			/*
			 * Well position name contains hyphen "-" character.
			 * Example: "A-12", "a-12", "12-A", "12-a".
			 */
			str1 = fetchPureBaseString(wellname, "-");
			str2 = fetchResidueString(wellname, "-");
		}
		else if (wellname.indexOf(",") >= 0)
		{
			/*
			 * Well position name contains comma "," character.
			 * Example: "A,12", "a,12", "12,A", "12,a".
			 */
			str1 = fetchPureBaseString(wellname, ",");
			str2 = fetchResidueString(wellname, ",");
		}
		else
		{
			/*
			 * Well position name expected to be of simple type.
			 * Example: "A12", "a12", "12A", "12a".
			 */
			int numCharIndex = fetchFirstNumCharIndex(wellname);
			if (numCharIndex == -1)
			{
				/*
				 * No numerical part.
				 */
				str1 = wellname;
			}
			else if (numCharIndex > 0)
			{
				/*
				 * Non-numerical part first.
				 */
				str1 = wellname.substring(0, numCharIndex);
				if (numCharIndex < wellname.length()) {
					str2 = wellname.substring(numCharIndex);
				}
			}
			else
			{
				/*
				 * Numerical part first.
				 */
				int nonNumCharIndex = fetchFirstNonNumCharIndex(wellname);
				if (nonNumCharIndex == -1)
				{
					/*
					 * No non-numerical part.
					 */
					str1 = wellname;
				}
				else if (nonNumCharIndex > 0)
				{
					/*
					 * Numerical part first.
					 */
					str1 = wellname.substring(0, nonNumCharIndex);
					if (nonNumCharIndex < wellname.length()) {
						str2 = wellname.substring(nonNumCharIndex);
					}
				}
			}
		}
		log.debug("FilenameParseUtil:: convertWellNotation(): str1 = \"" + str1 + "\", str2 = \"" + str2 + "\"");
		if (str1.equals("") || str2.equals(""))
		{
			return null;
		}
		/*
		 * Both substrings non-empty.
		 */
		String rowStr = new String("");
		String clmStr = new String("");
		if (Character.isDigit(str1.charAt(0)))
		{
			rowStr = str2;
			clmStr = str1;
		}
		else
		{
			rowStr = str1;
			clmStr = str2;
		}
		/*
		 * Convert row string to uppercase.
		 */
		rowStr = rowStr.toUpperCase();
		
		/*
		 * Construct well position string in notation type "A12".
		 */
		String wellnameStd = rowStr + clmStr;
		log.debug("FilenameParseUtil:: convertWellNotation(): wellnameStd = \"" + wellnameStd + "\"");
		/*
		 * Return converted  well position string.
		 */
		return wellnameStd;
	}


	/**
	 * Converts a well position of standard notation of type "A12"
	 * to local notation. Accepts local prototype notations of
	 * the following types, where 'A' or 'a' is a single upper-
	 * or lowercase letter describing the row, and "12" a number
	 * describing the column (no leading zero for numbers < 10):
	 * "A12", "a12", "A-12", "a-12", "A,12", "a,12",
	 * "12-A", "12-a", "12,A", "12,a". As an example, wellname
	 * "A12" in standard notation and prototype name "6,c"
	 * will result in conversion of "A12" to "12,a".
	 * 
	 * @param wellname String the well position standard notation to convert.
	 * @param prototypeName String An example (prototype) of the local notation.
	 * @return String the well position string in local notation type.
	 */
	public String convertToLocalWellNotation(String wellname, String prototypeName)
	{
		log.debug("FilenameParseUtil:: convertToLocalWellNotation(): wellname = \"" + wellname + "\", prototypeName = \"" + prototypeName + "\"");
		/*
		 * Return null if wellname is empty.
		 */
		if (wellname == null || wellname.length() == 0)
		{
			return null;
		}
		/*
		 * Return null if prototypeName is empty.
		 */
		if (prototypeName == null || prototypeName.length() == 0)
		{
			return null;
		}
		/*
		 * Get row and column strings for wellname in standard notation.
		 */
		int numCharIndexStd = fetchFirstNumCharIndex(wellname);
		if (numCharIndexStd == -1)
		{
			/*
			 * No numerical part in standard notation wellname, return null.
			 */
			return null;
		}
		if (numCharIndexStd == 0)
		{
			/*
			 * No non-numerical part in standard notation wellname, return null.
			 */
			return null;
		}
		String rowStrStd = wellname.substring(0, numCharIndexStd);
		String clmStrStd = wellname.substring(numCharIndexStd);
		/*
		 * Parse prototype name string.
		 */
		String str1 = new String("");
		String str2 = new String("");
		String separatorString = new String("");
		if (prototypeName.indexOf("-") >= 0)
		{
			/*
			 * Prototype name contains hyphen "-" character.
			 * Example: "A-12", "a-12", "12-A", "12-a".
			 */
			separatorString = "-";
			str1 = fetchPureBaseString(prototypeName, "-");
			str2 = fetchResidueString(prototypeName, "-");
		}
		else if (prototypeName.indexOf(",") >= 0)
		{
			/*
			 * Prototype name contains comma "," character.
			 * Example: "A,12", "a,12", "12,A", "12,a".
			 */
			separatorString = ",";
			str1 = fetchPureBaseString(prototypeName, ",");
			str2 = fetchResidueString(prototypeName, ",");
		}
		else
		{
			/*
			 * Prototype name expected to be of simple type.
			 * Example: "A12", "a12", "12A", "12a".
			 */
			int numCharIndex = fetchFirstNumCharIndex(prototypeName);
			if (numCharIndex == -1)
			{
				/*
				 * No numerical part.
				 */
				str1 = prototypeName;
			}
			else if (numCharIndex > 0)
			{
				/*
				 * Non-numerical part first.
				 */
				str1 = prototypeName.substring(0, numCharIndex);
				if (numCharIndex < prototypeName.length()) {
					str2 = prototypeName.substring(numCharIndex);
				}
			}
			else
			{
				/*
				 * Numerical part first.
				 */
				int nonNumCharIndex = fetchFirstNonNumCharIndex(prototypeName);
				if (nonNumCharIndex == -1)
				{
					/*
					 * No non-numerical part.
					 */
					str1 = prototypeName;
				}
				else if (nonNumCharIndex > 0)
				{
					/*
					 * Numerical part first.
					 */
					str1 = prototypeName.substring(0, nonNumCharIndex);
					if (nonNumCharIndex < prototypeName.length()) {
						str2 = prototypeName.substring(nonNumCharIndex);
					}
				}
			}
		}
		log.debug("FilenameParseUtil:: convertToLocalWellNotation(): str1 = \"" + str1 + "\", str2 = \"" + str2 + "\"");
		if (str1.equals("") || str2.equals(""))
		{
			return null;
		}
		/*
		 * Both substrings non-empty.
		 */
		/*
		 * Construct well position string in local notation type.
		 */
		String rowStrLocal = new String("");
		String wellnameLocal = new String("");
		if (Character.isLetter(str1.charAt(0)))
		{
			/*
			 * Non-numerical part (letter) first.
			 */
			rowStrLocal = str1;
			if (Character.isUpperCase(rowStrLocal.charAt(0)))
			{
				wellnameLocal = rowStrStd.toUpperCase() + separatorString + clmStrStd;
			}
			else
			{
				wellnameLocal = rowStrStd.toLowerCase() + separatorString + clmStrStd;
			}
		}
		else
		{
			/*
			 * Non-numerical part (letter) last.
			 */
			rowStrLocal = str2;
			if (Character.isUpperCase(rowStrLocal.charAt(0)))
			{
				wellnameLocal = clmStrStd + separatorString + rowStrStd.toUpperCase();
			}
			else
			{
				wellnameLocal = clmStrStd + separatorString + rowStrStd.toLowerCase();
			}
		}
		log.debug("FilenameParseUtil:: convertToLocalWellNotation(): wellnameLocal = \"" + wellnameLocal + "\"");
		/*
		 * Return converted  well position string.
		 */
		return wellnameLocal;
	}
}
