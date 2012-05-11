/*
 $Id: UserDefaultSettingData.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
 * @author enell
 * @see <a
 *      href="../../../../../../../development/overview/data/clients.html">Session
 *      and client overview</a>
 * @hibernate.class table="`UserDefaultSettings`" lazy="false"
 */
public class UserDefaultSettingData
		extends SettingData
{
	public UserDefaultSettingData()
	{}

	private UserData user;


	/**
	 * Get the user this setting is valid for.
	 * 
	 * @hibernate.many-to-one update="false" outer-join="false"
	 * @hibernate.column name="`user_id`" not-null="true"
	 *                   unique-key="uniquesetting"
	 */
	public UserData getUser()
	{
		return user;
	}


	public void setUser(UserData user)
	{
		this.user = user;
	}
}
