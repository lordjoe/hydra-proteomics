/*
 $Id: XMLCrudeWriter3.java 3204 2009-04-07 09:17:04Z olle $

 Copyright (C) 2006 Gregory Vincic, Olle Mansson
 Copyright (C) 2007 Gregory Vincic

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

import javax.xml.stream.XMLStreamException;

/**
 * This interface defines crude XML writing, with some added convenience methods.
 * 
 * This interface contains some methods that may help writing
 * XML files. It was created as support for e.g. writing
 * single tags with XMLStreamWriter could not be found.
 *
 * It is up to the implementations to decide on indentation,
 * which may be ignored if desired, but if guidelines are
 * requested, recommended use is as follows:
 * To get consistent indentation and change to a new line,
 * the following rules are used:
 * 1. A start element or comment starts a new line.
 * 2. An end element starts a new line, unless following data.
 * 3. Indentation level is increased by one before start element. 
 * 4. Indentation level is decreased by one after end element.
 * To avoid having the outermost element block indented,
 * the indentation level starts at -1 instead of 0.
 *
 * @author Olle
 * @version 2.0
 * @proteios.modified $Date: 2009-04-07 02:17:04 -0700 (Tue, 07 Apr 2009) $
 */
public interface XMLCrudeWriter3
	extends XMLCrudeWriter2
{
	/**
	 * Writes indented text with optional extra indentation.
	 * The indentation level counter is not updated.
	 * 
	 * @param text String with text.
	 * @param extraIndentation int optional extra indentation.
	 * @throws XMLStreamException If there is an error
	 */
	public void writeIndentedText(String text, int extraIndentation) throws XMLStreamException;

	/**
	 * Get indentation level.
	 * 
	 * @return int level the indentation level.
	 */
	public int getIndentationLevel();

	/**
	 * Set indentation level.
	 * 
	 * @param level int The indentation level to set.
	 */
	public void setIndentationLevel(int level);
}
