/*
 $Id: SpectrumFileInstrument.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2009 Gregory Vincic

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

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods to access instrument information
 * obtained from spectrum files.
 * 
 * @author olle
 */
public class SpectrumFileInstrument
	implements SpectrumFileInstrumentInterface
{
	private String instrumentName;
	private String instrumentSerialNo;
	private List<StringPairInterface> source;
	private List<SpectrumFileAnalyzerInterface> analyzers;
	private List<StringPairInterface> detector;
	private List<StringPairInterface> additional;
	
	/**
	 * Get the spectrum instrument name.
	 * 
	 * @return Name The spectrum instrument name.
	 */
	public String getInstrumentName()
	{
		return this.instrumentName;
	}


	/**
	 * Set the spectrum instrument name.
	 * 
	 * @param instrumentName String The spectrum instrument name to set.
	 */
	public void setInstrumentName(String instrumentName)
	{
		this.instrumentName = instrumentName;
	}


	/**
	 * Get the spectrum instrument serial no.
	 * 
	 * @return String The spectrum instrument serial no.
	 */
	public String getInstrumentSerialNo()
	{
		return this.instrumentSerialNo;
	}


	/**
	 * Set the spectrum instrument serial no.
	 * 
	 * @param instrumentSerialNo String The spectrum instrument serial no to set.
	 */
	public void setInstrumentSerialNo(String instrumentSerialNo)
	{
		this.instrumentSerialNo = instrumentSerialNo;
	}


	/**
	 * Get the spectrum instrument source.
	 * 
	 * @return List<StringPairInterface> The spectrum instrument source.
	 */
	public List<StringPairInterface> getSource()
	{
		return this.source;
	}


	/**
	 * Get the spectrum instrument analyzer list.
	 * 
	 * @return List<SpectrumFileAnalyzerInterface> The spectrum instrument analyzer list.
	 */
	public List<SpectrumFileAnalyzerInterface> getAnalyzers()
	{
		return this.analyzers;
	}


	/**
	 * Get the spectrum instrument detector.
	 * 
	 * @return List<StringPairInterface> The spectrum instrument detector.
	 */
	public List<StringPairInterface> getDetector()
	{
		return this.detector;
	}


	/**
	 * Get the spectrum instrument additional data.
	 * 
	 * @return List<StringPairInterface> The spectrum instrument additional data.
	 */
	public List<StringPairInterface> getAdditional()
	{
		return this.additional;
	}


	/**
	 * Default constructor.
	 */
	public SpectrumFileInstrument()
	{
		this.source = new ArrayList<StringPairInterface>();
		this.analyzers = new ArrayList<SpectrumFileAnalyzerInterface>();
		this.detector = new ArrayList<StringPairInterface>();
		this.additional = new ArrayList<StringPairInterface>();
	}
}
