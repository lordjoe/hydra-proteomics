/*
  $Id: MessageData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information about messages.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.Message
	@see <a href="../../../../../../../development/overview/data/plugins.html">Plugin/jobs overview</a>
	@hibernate.class table="`Messages`" lazy="false"
*/
public class MessageData
	extends BasicData
	implements NameableData, RemovableData
{
	public MessageData()
	{}

	/*
		From the NameableData interface
		-------------------------------------------
	*/	
	private String name;
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private String description;
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	// -------------------------------------------

	/*
		From the RemovableData interface
		-------------------------------------------
	*/
	private boolean removed;
	public boolean isRemoved()
	{
		return removed;
	}
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
	// -------------------------------------------

	private UserData to;
	/**
		The user this message is to.
		@hibernate.many-to-one column="`to_user_id`" not-null="true" outer-join="false" update="false"
	*/
	public UserData getTo()
	{
		return to;
	}
	public void setTo(UserData to)
	{
		this.to = to;
	}
	
	/**
		The maximum length of the from property.
		@see #getFrom()
	*/
	public static final int MAX_FROM_LENGTH = 255;
	private String from;
	/**
		The name of the user this message is from. It can also be another name
		such as 'SYSTEM'.
		@hibernate.property column="`from_name`" type="string" length="255" not-null="true" update="false"
	*/
	public String getFrom()
	{
		return from;
	}
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	private int fromId;
	/**
		The id of the user if this message was from a real user. We map the id value only
		to avoid foreign key problems.
		@hibernate.property column="`from_user_id`" type="int" not-null="false" update="false"
	*/
	public int getFromId()
	{
		return fromId;
	}
	public void setFromId(int fromId)
	{
		this.fromId = fromId;
	}
	
	private Date timeSent;
	/**
		Get the date and time this message was sent.
		@hibernate.property column="`time_sent`" type="timestamp" not-null="true" update="false"
	*/
	public Date getTimeSent()
	{
		return timeSent;
	}
	public void setTimeSent(Date timeSent)
	{
		this.timeSent = timeSent;
	}

	private JobData job;
	/**
		If this message is a message about a job completion (or failure)
		this property contains a reference to that job.
		@hibernate.many-to-one column="`job_id`" not-null="false" outer-join="false" update="false"
	*/
	public JobData getJob()
	{
		return job;
	}
	public void setJob(JobData job)
	{
		this.job = job;
	}
	
	private boolean isRead;
	/**
		Check if the message has been read or not.
		@hibernate.property column="`is_read`" type="boolean" not-null="true"
	*/
	public boolean isRead()
	{
		return isRead;
	}
	public void setRead(boolean isRead)
	{
		this.isRead = isRead;
	}
}
