/*
 $Id: SessionData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Date;

/**
	This class holds information about a session.
 
	@author enell
	@version 2.0
	@see org.proteios.core.Session
	@see <a href="../../../../../../../development/overview/data/clients.html">Session and client overview</a>
	@hibernate.class table="`Sessions`" lazy="false"
*/
public class SessionData
	extends BasicData
{
	public SessionData()
	{}
	
	private UserData user;
	/**
		Get the user that logged in with this session.
		@hibernate.many-to-one column="`user_id`" not-null="true" update="false" outer-join="false"
	*/
	public UserData getUser()
	{
		return user;
	}
	
	public void setUser(UserData user)
	{
		this.user = user;
	}
		
	private Date loginTime;
	/**
		Get the date and time the user logged in.
		@hibernate.property column="`login_time`" type="timestamp" not-null="true" update="false"
	*/
	public Date getLoginTime()
	{
		return loginTime;
	}
	
	public void setLoginTime(Date loginTime)
	{
		this.loginTime = loginTime;
	}

	private Date logoutTime;
	/**
		Get the date and time the user logged out.
		@hibernate.property column="`logout_time`" type="timestamp" not-null="false"
	*/
	public Date getLogoutTime()
	{
		return logoutTime;
	}
	
	public void setLogoutTime(Date logoutTime)
	{
		this.logoutTime = logoutTime;
	}

	/**
		The maximum length of the login comment that can be stored in the database.
		@see #setLoginComment(String)
	*/
	public static final int MAX_LOGIN_COMMENT_LENGTH = 65535;
	private String loginComment;
	/**
		Get the login comment.
		@hibernate.property column="`login_comment`" type="text" not-null="false" update="false"
	*/
	public String getLoginComment()
	{
		return loginComment;
	}
	
	public void setLoginComment(String loginComment)
	{
		this.loginComment = loginComment;
	}

	private boolean impersonated = false;
	/**
		Check if another user is acting as the user of this session.
		@hibernate.property column="`impersonated`" type="boolean" not-null="true" update="false"
	*/
	public boolean getImpersonated()
	{
		return impersonated;
	}
	
	public void setImpersonated(boolean impersonated)
	{
		this.impersonated = impersonated;
	}

	private ClientData client;
	/**
		Get the client application the user was using.
		@hibernate.many-to-one column="`client_id`" not-null="false" update="false" outer-join="false"
	*/
	public ClientData getClient()
	{
		return client;
	}
	
	public void setClient(ClientData client)
	{
		this.client = client;
	}
	
	/**
		The maximum length of the remote ID that can be stored in the database.
		@see #setRemoteId(String)
	*/
	public static final int MAX_REMOTE_ID_LENGTH = 255;
	private String remoteId;
	/**
		Get the login comment.
		@hibernate.property column="`remote_id`" type="string" length="255" not-null="false" update="false"
	*/
	public String getRemoteId()
	{
		return remoteId;
	}
	public void setRemoteId(String remoteId)
	{
		this.remoteId = remoteId;
	}

}