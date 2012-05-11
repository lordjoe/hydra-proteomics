/*
  $Id: NewsData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information about news items.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.News
	@see <a href="../../../../../../../development/overview/data/misc.html">Misc overview</a>
	@hibernate.class table="`News`" lazy="false"
 */
public class NewsData
	extends BasicData
	implements NameableData, RemovableData
{
	public NewsData()
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

	private Date startDate;
	/**
		Get the first day this news item should be shown.
		@hibernate.property column="`start_date`" not-null="true"
	*/
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	private Date newsDate;
	/**
		Get the news date of this news item.
		@hibernate.property column="`news_date`" not-null="true"
	*/
	public Date getNewsDate()
	{
		return newsDate;
	}
	public void setNewsDate(Date newsDate)
	{
		this.newsDate = newsDate;
	}

	private Date endDate;
	/**
		Get the last day this news item should be shown.
		@hibernate.property column="`end_date`"
	*/
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
}
