/*
 $Id: ColumnPreferences.java 3499 2009-11-26 13:16:04Z olle $

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
 
import org.proteios.core.SortOrder;

/**
 * This class represents column preferences.
 * 
 * @author olle
 * @version 2.0
 * @proteios.modified $Date: 2009-11-26 05:16:04 -0800 (Thu, 26 Nov 2009) $
 */
public class ColumnPreferences
{
	/**
	 * Log core events.
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager
		.getLogger("org.proteios.io");


	/**
	 * Column name.
	 */
	private String name = null;
	

	/**
	 * Get column name.
	 * 
	 * @return String The column name.
	 */
	public String getName()
	{
		return this.name;
	}
	

	/**
	 * Set column name.
	 * 
	 * @param name String The column name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Visibility flag.
	 */
	private Boolean visible = true;


	/**
	 * Get visibility flag value.
	 * 
	 * @return Boolean The visibility flag value.
	 */
	public Boolean isVisible()
	{
		return this.visible;
	}


	/**
	 * Set visibility flag value.
	 * 
	 * @param visible Boolean The visibility flag value to set.
	 */
	public void setVisible(Boolean visible)
	{
		this.visible = visible;
	}


	/**
	 * Sorting order.
	 */
	private SortOrder sortOrder = null;


	/**
	 * Get sorting order value.
	 * 
	 * @return SortOrder The sorting order value.
	 */
	public SortOrder getSortOrder()
	{
		return this.sortOrder;
	}


	/**
	 * Set sorting order value.
	 * 
	 * @param sortOrder SortOrder The sorting order value to set.
	 */
	public void setSortOrder(SortOrder sortOrder)
	{
		this.sortOrder = sortOrder;
	}


	/**
	 * Sort priority.
	 */
	private Integer sortPriority = null;


	/**
	 * Get sort priority value.
	 * 
	 * @return Integer The sort priority string value.
	 */
	public Integer getSortPriority()
	{
		return this.sortPriority;
	}


	/**
	 * Set sort priority value.
	 * 
	 * @param sortPriority Integer The sort priority value to set.
	 */
	public void setSortPriority(Integer sortPriority)
	{
		this.sortPriority = sortPriority;
	}


	/**
	 * Filter string.
	 */
	private String filterString = null;


	/**
	 * Get filter string value.
	 * 
	 * @return String The filter string value.
	 */
	public String getFilterString()
	{
		return this.filterString;
	}


	/**
	 * Set filter string value.
	 * 
	 * @param filterString String The filter string value to set.
	 */
	public void setFilterString(String filterString)
	{
		this.filterString = filterString;
	}


	/**
	 * Default constructor.
	 */
	public ColumnPreferences()
	{}


	/**
	 * Constructor with name argument.
	 * 
	 * @param name String The column name.
	 */
	public ColumnPreferences(String name)
	{
		setName(name);
	}
}
