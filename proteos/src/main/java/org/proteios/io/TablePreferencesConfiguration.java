/*
 $Id: TablePreferencesConfiguration.java 3499 2009-11-26 13:16:04Z olle $

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

//import org.proteios.core.PreferencesFile;
import org.proteios.core.User;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a table preferences configuration.
 * 
 * @author olle
 * @version 2.0
 * @proteios.modified $Date: 2009-11-26 05:16:04 -0800 (Thu, 26 Nov 2009) $
 */
public class TablePreferencesConfiguration
{
	/**
	 * Log core events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");


	/**
	 * TablePreferencesConfiguration constructor.
	 */
	public TablePreferencesConfiguration()
	{}

	
	/**
	 * Table preferences mode.
	 */
	private int mode = -1;


	/**
	 * Get the table preferences mode.
	 *
	 * 0 = "Never".
	 * 1 = "Always".
	 * 2 = "Use current preferences but do not update them".
	 * 
	 * @return int The table preferences mode.
	 */
	public int getMode()
	{
		return this.mode;
	}


	/**
	 * Set the table preferences mode.
	 *
	 * 0 = "Never".
	 * 1 = "Always".
	 * 
	 * @param mode int The table preferences mode to use.
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	
	/**
	 * The table preferences mode.
	 */
	public enum Mode
	{
		/**
		 * Table preferences never saved.
		 */
		NEVER(0, "Never"),
		/**
		 * Table preferences always saved.
		 */
		ALWAYS(1, "Always"),
		/**
		 * Table preferences always saved.
		 */
		USE_BUT_DO_NOT_UPDATE(2, "Use current preferences but do not update them");
		/**
		 * Maps the integer that is stored in the database.
		 */
		private static final Map<Integer, Mode> valueMapping = new HashMap<Integer, Mode>();
		static
		{
			for (Mode mode : Mode.values())
			{
				Mode m = valueMapping.put(mode.getValue(), mode);
				assert m == null : "Another TablePreferencesConfiguration.Mode with the value " + mode
					.getValue() + " already exists";
			}
		}


		/**
		 * Get the <code>TablePreferencesConfiguration.Mode</code> object when you know the integer
		 * code.
		 * 
		 * @param value The integer value.
		 * @return The type object or null if the value is unknown
		 */
		public static Mode fromValue(int value)
		{
			Mode mode = valueMapping.get(value);
			assert mode != null : "mode == null for value " + value;
			return mode;
		}

		private final int value;
		private final String displayValue;


		private Mode(int value, String displayValue)
		{
			this.value = value;
			this.displayValue = displayValue;
		}


		@Override
		public String toString()
		{
			return displayValue;
		}


		/**
		 * Get the integer value that is used when storing a
		 * <code>TablePreferencesConfiguration.Mode</code> to the database.
		 * 
		 * @return The integer value of this type
		 */
		public int getValue()
		{
			return value;
		}
	}


	/**
	 * Fetches table preferences configurations that have not been explicitly
	 * set in this class from properties files and other sources.
	 * 
	 * @param user User The user for which preferences should be obtained.
	 */
	public void fetchConfiguration(User user)
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		// Check if values for all configurations are set
//		log.debug("(1) getMode() = " + getMode() + " \"" + TablePreferencesConfiguration.Mode.fromValue(getMode()) + "\"");;
//		if (getMode() >= 0 && getMode() < TablePreferencesConfiguration.Mode.values().length)
//		{
//			// All configurations set, return directly
//			log.debug("All configurations set, return directly");
//			return;
//		}
//		// Get table preferences configuration from preferences file
//		String userName = user.getName();
//		log.debug("userName = \"" + userName + "\"");
//		PreferencesFile prefFile = new PreferencesFile(user);
//		int useTablePreferences = prefFile.getTablePreferencesMode();
//		log.debug("useTablePreferences = " + useTablePreferences + " \"" + TablePreferencesConfiguration.Mode.fromValue(useTablePreferences) + "\"");
//		// Set needed configuration settings
//		if (getMode() < 0 || getMode() >= TablePreferencesConfiguration.Mode.values().length)
//		{
//			setMode(useTablePreferences);
//		}
//		// Debug output
//		log.debug("(2) getMode() = " + getMode() + " \"" + TablePreferencesConfiguration.Mode.fromValue(getMode()) + "\"");;
	}
}
