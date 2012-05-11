/*
 $Id: PropertiesFile.java 1916 2007-08-31 09:54:00Z jari $
 
 Copyright (C) 2007 Olle Mansson
 
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public final class PropertiesFile
		extends Properties
{
	public PropertiesFile()
	{
		try
		{
			InputStream is = PropertiesFile.class.getResource(
				"/test.properties").openStream();
			super.load(is);
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
	}
}
