/*
  $Id: ClientData.java 3677 2010-04-20 12:02:33Z gregory $

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
package org.proteios.core.data;

import java.util.Set;

/**
	This class holds information about a client.
 
	@author enell
	@version 2.0
	@see org.proteios.core.Client
	@see <a href="../../../../../../../development/overview/data/clients.html">Session and client overview</a>
	@hibernate.class table="`Clients`" lazy="false"
*/
public class ClientData
	extends CommonData
{

	/**
		The maximum length of the external ID that can be stored in the database.
		@see #setExternalId(String)
	*/
	public static final int MAX_EXTERNAL_ID_LENGTH = 255;
	private String externalId;
	/**
		Get the external id for this <code>Client</code> item.
		@hibernate.property column="`external_id`" type="string" length="255" not-null="true" unique="true"
	*/
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	private Set<SessionData> sessions;
	/**
		This is the invers end.
		@see SessionData#getClient()
		@hibernate.set lazy="true" inverse="true" cascade="delete"
		@hibernate.collection-key column="`client_id`"
		@hibernate.collection-one-to-many class="org.proteios.core.data.SessionData"
	*/
	Set<SessionData> getSessions()
	{
		return sessions;
	}
	
	void setSessions(Set<SessionData> sessions)
	{
		this.sessions = sessions;
	}

	private Set<UserClientSettingData> userSettings;
	/**
		This is the inverse end.
		@see UserClientSettingData#getClient()
		@hibernate.set lazy="true" inverse="true" cascade="delete"
		@hibernate.collection-key column="`client_id`"
		@hibernate.collection-one-to-many class="org.proteios.core.data.UserClientSettingData"
	 */
	Set<UserClientSettingData> getUserSettings()
	{
		return userSettings;
	}
	
	void setUserSettings(Set<UserClientSettingData> userSettings)
	{
		this.userSettings = userSettings;
	}
	
	private Set<ClientDefaultSettingData> defaultSettings;
	/**
		This is the inverse end.
		@see ClientDefaultSettingData#getClient()
		@hibernate.set lazy="true" inverse="true" cascade="delete"
		@hibernate.collection-key column="`client_id`"
		@hibernate.collection-one-to-many class="org.proteios.core.data.ClientDefaultSettingData"
	 */
	Set<ClientDefaultSettingData> getDefaultSettings()
	{
		return defaultSettings;
	}
	
	void setDefaultSettings(Set<ClientDefaultSettingData> defaultSettings)
	{
		this.defaultSettings = defaultSettings;
	}
	

	
}
