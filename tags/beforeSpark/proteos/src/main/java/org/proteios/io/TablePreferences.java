/*
 $Id: TablePreferences.java 3499 2009-11-26 13:16:04Z olle $

 Copyright (C) 2009 Olle Mansson

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

//import org.proteios.core.Config;
//import org.proteios.core.PreferencesFile;
//import org.proteios.core.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents table preferences.
 * 
 * @author olle
 * @version 2.0
 * @proteios.modified $Date: 2009-11-26 05:16:04 -0800 (Thu, 26 Nov 2009) $
 */
public class TablePreferences
{
	/**
	 * Log core events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");


	/**
	 * Table name.
	 */
	private String name = null;


	/**
	 * Get table name.
	 * 
	 * @return String The table name.
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * Set table name.
	 * 
	 * @param name String The table name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Column preferences list.
	 */
	private List<ColumnPreferences> columnPreferencesList = null;


	/**
	 * Get the column preferences list.
	 * 
	 * @return List<ColumnPreferences> The column preferences list.
	 */
	public List<ColumnPreferences> getColumnPreferencesList()
	{
		return this.columnPreferencesList;
	}


	/**
	 * Set the column preferences list.
	 * 
	 * @param columnPreferencesList List<ColumnPreferences> The column preferences list to set.
	 */
	public void setColumnPreferencesList(List<ColumnPreferences> columnPreferencesList)
	{
		this.columnPreferencesList = columnPreferencesList;
	}


	/**
	 * Default constructor.
	 */
	public TablePreferences()
	{}


	/**
	 * Constructor with name argument.
	 * 
	 * @param name String The table name.
	 */
	public TablePreferences(String name)
	{
		setName(name);
	}
}
