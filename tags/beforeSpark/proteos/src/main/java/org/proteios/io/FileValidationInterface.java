/*
 $Id: FileValidationInterface.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Fredrik Levander, Gregory Vincic

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
import javax.xml.stream.XMLStreamException;

/**
 * @author fredrtik
 */
public interface FileValidationInterface
{
	/* Check the entire file for validity */
	public abstract boolean valid()
			throws XMLStreamException, SAXException;


	/* Check if the start of the file is ok */
	public abstract boolean importable();
}