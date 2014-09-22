/*
	$Id: PasswordData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds the password for a user. It has a one-to-one
	relationship with the {@link UserData} class. We have enabled proxies
	to avoid loading passwords unless it is necessary. The second-level
	cache must never be enabled for this class.

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@hibernate.class table="`Passwords`" lazy="true"
*/
public class PasswordData
	extends BasicData
{

	public PasswordData()
	{}

	private String md5Password;
	/**
		Get the MD5 encrypted password. It is always returned as a string
		with 32 hexadecimal characters.
		@hibernate.property column="`md5password`" type="string" length="32" not-null="true"
	*/
	public String getMd5Password()
	{
		return md5Password;
	}
	public void setMd5Password(String md5Password)
	{
		this.md5Password = md5Password;
	}
	
	private UserData user;
	/**
		Get the user this password is referring to.
		@hibernate.one-to-one class="org.proteios.core.data.UserData"
	*/
	public UserData getUser()
	{
		return user;
	}
	void setUser(UserData user)
	{
		this.user = user;
	}

}
