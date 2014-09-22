/*
 $Id: AttributeFilter.java 3207 2009-04-09 06:48:11Z gregory $
 
 Copyright (C) 2006, 2007 Gregory Vincic
 
 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.
 
 This file is part of Proteios.
 Available at http://www.proteios.org/
 
 Proteios is free software; you can redistribute it and/or
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
package org.proteios;

import java.util.List;

/**
 * Implement this filter when you want to describe a set of attributes as
 * default or not and their sorting order.
 * 
 * @author gregory
 */
public interface AttributeFilter
{
	/**
	 * @param attribute
	 * @return true if this attribute is considered a default, false otherwise
	 */
	public boolean isDefault(AttributeDefinition attribute);


	/**
	 * Get a list of overall order of all attributes.
	 * 
	 * @return List<String> A list of overall order of all attributes.
	 */
	public List<String> getDefaultOrder();
}
