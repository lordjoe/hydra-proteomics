/*
 $Id: XMLValidator.java 3269 2009-05-07 09:43:55Z fredrik $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
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

import org.xml.sax.SAXException;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * This class validates XML input (file, input stream, or SourceStream) against
 * an XSD (XML Schema Definition) file. Based on Validate.java by Neeraj Bajaj,
 * Sun Microsystems, in example code described <a
 * href="http://java.sun.com/developer/EJTechTips/2005/tt1025.html">here</a>.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2009-05-07 02:43:55 -0700 (Thu, 07 May 2009) $
 */
public class XMLValidator
{
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
	.getLogger("org.proteios.io");

	/**
	 * Parses the XSD schema file and returns in-memory representation of the
	 * schema.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @return Schema In-memory representation of the XSD file schema.
	 * @throws SAXException
	 */
	public static Schema compileSchema(String xsdFilePath)
			throws SAXException
	{
		/*
		 * Get new SchemaFactory instance for W3C XML Schema language
		 */
		SchemaFactory sf = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		/*
		 * Try to get absolute path for xsdFile,
		 * using the class loader search rules.
		 */
		URL url = XMLValidator.class.getResource(xsdFilePath);
		URI uri = null;
		try
		{
			if (url != null)
			{
				uri = url.toURI();
			}
			else
			{
				/*
				 * Check if path for xsdFile is absolute path.
				 */
				uri = new URI("file:" + xsdFilePath);
			}
		}
		catch (URISyntaxException e)
		{
			System.out.println("XMLValidator" + "::compileSchema(): URISyntaxException when creating URI from path = \"" + xsdFilePath + "\" : " + e);
		}
		return sf.newSchema(new File(uri));
	}


	/**
	 * Validates an XML file against the schema in an XSD schema file.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlFilePath String with path to XML file to validate.
	 * @return True if XML file is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate(String xsdFilePath, String xmlFilePath)
			throws SAXException
	{
		/*
		 * Validate XML File against schema in XSD file
		 */
		return validate(xsdFilePath, new StreamSource(xmlFilePath));
	}


	/**
	 * Validates an XML input stream against the schema in an XSD schema file.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlInputStream XML input stream to validate.
	 * @return True if XML input stream is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate(String xsdFilePath,
			InputStream xmlInputStream)
			throws SAXException
	{
		/*
		 * Validate XML input stream against schema in XSD file
		 */
		return validate(xsdFilePath, new StreamSource(xmlInputStream));
	}


	/**
	 * Validates an XML file StreamSource against the schema in an XSD schema
	 * file.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlStreamSource XML StreamSource to validate.
	 * @return True if XML StreamSource is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate(String xsdFilePath,
			StreamSource xmlStreamSource)
			throws SAXException
	{
		/*
		 * Validate XML StreamSource against schema in XSD file
		 */
		try
		{
			/*
			 * Get new schema from parsed XSD schema file.
			 */
			Schema schema = compileSchema(xsdFilePath);
			/*
			 * Get new validator for XSD schema.
			 */
			Validator validator = schema.newValidator();
			/*
			 * Set ErrorHandler on the validator. Here we want to handle all
			 * errors ourselves, so we use an errorhandler that simply
			 * propagates all thrown SAXExceptions, except warnings, which are
			 * ignored.
			 */
			org.xml.sax.ErrorHandler errHandler = new XMLValidatorLazyErrorHandler();
			validator.setErrorHandler(errHandler);
			try
			{
				/*
				 * Validate the XML StreamSource against the schema from the
				 * parsed XSD schema file.
				 */
				validator.validate(xmlStreamSource);
			}
			catch (SAXException e)
			{
                e.printStackTrace();
                System.out
                    .println("Validator.validate(): Error when validating XML StreamSource. GET CAUSE:");
				/*
				 * SAXExceptions were thrown. The XML StreamSource is invalid,
				 * so false is returned.
				 */
				log.warn(e.getMessage());
				return false;
			}
			/*
			 * If no exceptions were thrown by the validator or via the error
			 * handler, the XML StreamSource is valid.
			 */
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out
				.println("Validator.validate(): Error when validating XML StreamSource. GET CAUSE:");
			//e.getCause().fillInStackTrace();
			/*
			 * Exceptions were thrown. Either the XML StreamSource was not
			 * valid, or there were problems connected with the validator. In
			 * both cases we were unable to validate the XML StreamSource, so
			 * false is returned.
			 */
			return false;
		}
	}


	/**
	 * Validates an XML file against the schema in an XSD schema file. Prints
	 * error information if file not valid.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlFilePath String with path to XML file to validate.
	 * @return True if XML file is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate_w_error_output(String xsdFilePath,
			String xmlFilePath)
			throws SAXException
	{
		/*
		 * Validate XML File against schema in XSD file
		 */
		return validate_w_error_output(xsdFilePath, new StreamSource(
			xmlFilePath));
	}


	/**
	 * Validates an XML input stream against the schema in an XSD schema file.
	 * Prints error information if XML input stream not valid.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlInputStream XML input stream to validate.
	 * @return True if XML input stream is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate_w_error_output(String xsdFilePath,
			InputStream xmlInputStream)
			throws SAXException
	{
		/*
		 * Validate XML input stream against schema in XSD file
		 */
		return validate_w_error_output(xsdFilePath, new StreamSource(
			xmlInputStream));
	}


	/**
	 * Validates an XML StreamSource against the schema in an XSD schema file.
	 * Prints error information if StreamSource not valid.
	 * 
	 * @param xsdFilePath String with path to XSD file with validation schema.
	 * @param xmlStreamSource XML StreamSource to validate.
	 * @return True if XML StreamSource is valid, else false.
	 * @throws SAXException
	 */
	public static boolean validate_w_error_output(String xsdFilePath,
			StreamSource xmlStreamSource)
			throws SAXException
	{
		/*
		 * Validate XML StreamSource against schema in XSD file
		 */
		try
		{
			/*
			 * Get new schema from parsed XSD schema file.
			 */
			Schema schema = compileSchema(xsdFilePath);
			/*
			 * Get new validator for XSD schema.
			 */
			Validator validator = schema.newValidator();
			/*
			 * Set ErrorHandler on the validator. Here we want to get
			 * information on errors, so we use an errorhandler with
			 * output.
			 */
			org.xml.sax.ErrorHandler errHandler = new XMLValidatorErrorHandler();
			validator.setErrorHandler(errHandler);
			try
			{
				/*
				 * Validate the XML StreamSource against the schema from the
				 * parsed XSD schema file.
				 */
				validator.validate(xmlStreamSource);
			}
			catch (SAXException e)
			{
				/*
				 * SAXExceptions were thrown. The XML StreamSource is invalid,
				 * so false is returned.
				 */
				return false;
			}
			/*
			 * If no exceptions were thrown by the validator or via the error
			 * handler, the XML StreamSource is valid.
			 */
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out
				.println("Validator.validate_w_error_output(): Error when validating XML StreamSource. GET CAUSE:");
			e.getCause().fillInStackTrace();
			/*
			 * Exceptions were thrown. Either the XML StreamSource was not
			 * valid, or there were problems connected with the validator. In
			 * both cases we were unable to validate the XML StreamSource, so
			 * false is returned.
			 */
			return false;
		}
	}
}
