/*
 $Id: NotificationConfiguration.java 3499 2009-11-26 13:16:04Z olle $

 Copyright (C) 2008 Olle Mansson

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

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a notification configuration.
 * 
 * @author olle
 * @version 2.0
 * @proteios.modified $Date: 2009-11-26 05:16:04 -0800 (Thu, 26 Nov 2009) $
 */
public class NotificationConfiguration
{
	/**
	 * Log core events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");


	/**
	 * NotificationConfiguration constructor.
	 */
	public NotificationConfiguration()
	{}

	
	/**
	 * Notification SMTP host.
	 */
	private String smtpHost;


	/**
	 * Notification "from" address.
	 */
	private String fromAddress;


	/**
	 * Notification "from" name.
	 */
	private String fromName;


	/**
	 * Notification "to" address.
	 */
	private String toAddress;


	/**
	 * Notification mode.
	 */
	private int mode = -1;


	/**
	 * Get the SMTP host to use for e-mail notification.
	 * 
	 * @return String The SMTP host to use for e-mail notification.
	 */
	public String getSmtpHost()
	{
		return this.smtpHost;
	}


	/**
	 * Set the SMTP host to use for e-mail notification.
	 * 
	 * @param smtpHost String The SMTP host to use for e-mail notification.
	 */
	public void setSmtpHost(String smtpHost)
	{
		this.smtpHost = smtpHost;
	}

	
	/**
	 * Get the "from" address to use for e-mail notification.
	 * 
	 * @return String The "from" address to use for e-mail notification.
	 */
	public String getFromAddress()
	{
		return this.fromAddress;
	}


	/**
	 * Set the "from" address to use for e-mail notification.
	 * 
	 * @param fromAddress String The "from" address to use for e-mail notification.
	 */
	public void setFromAddress(String fromAddress)
	{
		this.fromAddress = fromAddress;
	}

	
	/**
	 * Get the "from" name to use for e-mail notification.
	 * 
	 * @return String The "from" name to use for e-mail notification.
	 */
	public String getFromName()
	{
		return this.fromName;
	}


	/**
	 * Set the "from" name to use for e-mail notification.
	 * 
	 * @param fromName String The "from" name to use for e-mail notification.
	 */
	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

	
	/**
	 * Get the "to" address to use for e-mail notification.
	 * 
	 * @return String The "to" address to use for e-mail notification.
	 */
	public String getToAddress()
	{
		return this.toAddress;
	}


	/**
	 * Set the "to" address to use for e-mail notification.
	 * 
	 * @param toAddress String The "to" address to use for e-mail notification.
	 */
	public void setToAddress(String toAddress)
	{
		this.toAddress = toAddress;
	}

	
	/**
	 * Get the notification mode.
	 *
	 * 0 = "Never".
	 * 1 = "After every finished job".
	 * 2 = "After all jobs finished".
	 * 
	 * @return int The notification mode.
	 */
	public int getMode()
	{
		return this.mode;
	}


	/**
	 * Set the notification mode.
	 *
	 * 0 = "Never".
	 * 1 = "After every finished job".
	 * 2 = "After all jobs finished".
	 * 
	 * @param mode int The notification mode to use.
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	
	/**
	 * The notification mode.
	 */
	public enum Mode
	{
		/**
		 * Notification never sent.
		 */
		NEVER(0, "Never"),
		/**
		 * Notification after every finished job.
		 */
		EVERY_FINISHED_JOB(1, "After every finished job"),
		/**
		 * Notification after all jobs finished.
		 */
		ALL_JOBS_FINISHED(2, "After all jobs finished");
		/**
		 * Maps the integer that is stored in the database.
		 */
		private static final Map<Integer, Mode> valueMapping = new HashMap<Integer, Mode>();
		static
		{
			for (Mode mode : Mode.values())
			{
				Mode m = valueMapping.put(mode.getValue(), mode);
				assert m == null : "Another NotificationConfiguration.Mode with the value " + mode
					.getValue() + " already exists";
			}
		}


		/**
		 * Get the <code>NotificationConfiguration.Mode</code> object when you know the integer
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
		 * <code>NotificationConfiguration.Mode</code> to the database.
		 * 
		 * @return The integer value of this type
		 */
		public int getValue()
		{
			return value;
		}
	}

//
//	/**
//	 * Fetches notification configurations that have not been explicitly
//	 * set in this class from properties files and other sources.
//	 *
//	 * @param user User The user for which preferences should be obtained.
//	 */
//	public void fetchConfiguration(User user)
//	{
//		// Check if values for all configurations are set
//		log.debug("(1) getSmtpHost() = \"" + getSmtpHost() + "\"");
//		log.debug("(1) getFromAddress() = \"" + getFromAddress() + "\"");
//		log.debug("(1) getFromName() = \"" + getFromName() + "\"");
//		log.debug("(1) getToAdress() = \"" + getToAddress() + "\"");
//		log.debug("(1) getMode() = " + getMode() + " \"" + NotificationConfiguration.Mode.fromValue(getMode()) + "\"");;
//		if (getSmtpHost() != null
//				&& getFromAddress() != null
//				&& getFromName() != null
//				&& getToAddress() != null
//				&& getMode() >= 0
//				&& getMode() < NotificationConfiguration.Mode.values().length)
//		{
//			// All configurations set, return directly
//			log.debug("All configurations set, return directly");
//			return;
//		}
//		// Get SMTP host from general configuration file proteios.config
//		String smtpHostStr = Config.getString("mail.server.host");
//		log.debug("smtpHostStr from proteios.config = \"" + smtpHostStr + "\"");
//		// Get "from" address from general configuration file proteios.config
//		String fromAddressStr = Config.getString("mail.from.address");
//		log.debug("fromAddressStr from proteios.config = \"" + fromAddressStr + "\"");
//		if (fromAddressStr == null)
//		{
//			// Get "from" address from canonical host name
//			try
//			{
//				InetAddress host = InetAddress.getLocalHost();
//				fromAddressStr = "noreply@" + host.getCanonicalHostName();
//			}
//			catch (UnknownHostException e)
//			{
//				log.debug("UnknownHostException when calling InetAddress.getLocalHost(): " + e);
//			}
//		}
//		// Get notification configuration from preferences file
//		String userName = user.getName();
//		log.debug("userName = \"" + userName + "\"");
//		PreferencesFile prefFile = new PreferencesFile(user);
//		String fromNameStr = prefFile.getFromName();
//		String toAddressStr = prefFile.getToAddress();
//		int useNotification = prefFile.getNotificationMode();
//		log.debug("smtpHostStr = \"" + smtpHostStr + "\"");
//		log.debug("fromAddressStr = \"" + fromAddressStr + "\"");
//		log.debug("fromNameStr = \"" + fromNameStr + "\"");
//		log.debug("toAddressStr = \"" + toAddressStr + "\"");
//		log.debug("useNotification = " + useNotification + " \"" + NotificationConfiguration.Mode.fromValue(useNotification) + "\"");
//		// Set needed configuration settings
//		if (getSmtpHost() == null)
//		{
//			setSmtpHost(smtpHostStr);
//		}
//		if (getFromAddress() == null)
//		{
//			setFromAddress(fromAddressStr);
//		}
//		if (getFromName() == null)
//		{
//			setFromName(fromNameStr);
//		}
//		if (getToAddress() == null)
//		{
//			setToAddress(toAddressStr);
//		}
//		if (getMode() < 0 || getMode() >= NotificationConfiguration.Mode.values().length)
//		{
//			setMode(useNotification);
//		}
//		// Debug output
//		log.debug("(2) getSmtpHost() = \"" + getSmtpHost() + "\"");
//		log.debug("(2) getFromAddress() = \"" + getFromAddress() + "\"");
//		log.debug("(2) getFromName() = \"" + getFromName() + "\"");
//		log.debug("(2) getToAdress() = \"" + getToAddress() + "\"");
//		log.debug("(2) getMode() = " + getMode() + " \"" + NotificationConfiguration.Mode.fromValue(getMode()) + "\"");;
//	}
}
