package org.proteios.io;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.stream.XMLStreamException;

public class PiumsPepfilReader
		implements FileValidationInterface
{
 
	InputStream inputStream;
	String xsdFilePath = "/xsd/piumsPepfil.xsd";
	
	/**
	 * Constructor that takes a PKL input stream as parameter.
	 * 
	 * @param inputStream InputStream PKL input stream.
	 */
	public PiumsPepfilReader(InputStream inputStream)
	{
		this.inputStream=inputStream;
	}

	
	@Override
	public boolean importable()
	{
		boolean ok = false;
		try
		{
			BufferedReader inreader = new BufferedReader(new InputStreamReader(
				inputStream));
			ok = false;
			String line = new String();
			if ((line = inreader.readLine()) != null)
			{
				if ((line = inreader.readLine()) != null)
				{
					ok = line.matches("<pepfil3>*.");
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


	@Override
	public boolean valid()
			throws XMLStreamException, SAXException
	{
		boolean retVal = XMLValidator
		.validate(this.xsdFilePath, this.inputStream);
	return retVal;
	}
}
