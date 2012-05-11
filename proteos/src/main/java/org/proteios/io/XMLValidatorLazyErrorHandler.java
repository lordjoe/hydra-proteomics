/*
 $Id: XMLValidatorLazyErrorHandler.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2006, 2007 Gregory Vincic

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

/**
 * This class handles errors when validating an XML file against an XSD (XML
 * Schema Definition) file. This error handler does not print any error
 * messages, but only propagates all non-warning exceptions further (that's why
 * it is called "lazy"). Intended to be used in connection with methods that
 * want to handle validation errors themselves, e.g. where one wants a simple
 * true/false response on whether an XML file is valid. Based on
 * MyErrorHandler.java by Neeraj Bajaj, Sun Microsystems, in example code
 * described <a
 * href="http://java.sun.com/developer/EJTechTips/2005/tt1025.html">here</a>.
 * 
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class XMLValidatorLazyErrorHandler
		implements org.xml.sax.ErrorHandler
{
	/**
	 * Default constructor.
	 */
	public XMLValidatorLazyErrorHandler()
	{}


	/**
	 * Handles validation non-fatal errors.
	 * 
	 * @param sAXParseException An org.xml.sax.SAXParseException exception.
	 * @throws org.xml.sax.SAXException
	 */
	public void error(org.xml.sax.SAXParseException sAXParseException)
			throws org.xml.sax.SAXException
	{
		/*
		 * Propagate the thrown SAXParseException
		 */
		throw sAXParseException;
	}


	/**
	 * Handles validation fatal errors.
	 * 
	 * @param sAXParseException An org.xml.sax.SAXParseException exception.
	 * @throws org.xml.sax.SAXException
	 */
	public void fatalError(org.xml.sax.SAXParseException sAXParseException)
			throws org.xml.sax.SAXException
	{
		/*
		 * Propagate the thrown SAXParseException
		 */
		throw sAXParseException;
	}


	/**
	 * Handles validation warnings.
	 * 
	 * @param sAXParseException An org.xml.sax.SAXParseException exception.
	 */
	public void warning(org.xml.sax.SAXParseException sAXParseException)
	{
	/*
	 * Do not propagate the thrown SAXParseException, as it is only a warning.
	 */
	}
}
